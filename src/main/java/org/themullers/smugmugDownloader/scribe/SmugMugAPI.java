package org.themullers.smugmugDownloader.scribe;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import org.themullers.smugmugDownloader.pojo.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A java wrapper around the SmugMug REST API.
 *
 * This code leverages the "Scribe Java" OAuth API and the "Jackson" JSON parser.
 */
public class SmugMugAPI {

    private static final String API_BASE_URL = "https://api.smugmug.com";
    private static final String AUTHORIZED_USER_ENDPOINT = API_BASE_URL + "/api/v2!authuser";

    private OAuth10aService service = null;
    private OAuth1AccessToken accessToken = null;
    private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * This constructor initializes the SmugMugAPI object.
     *
     * @param apiKey  The key issued by SmugMug when you register for API use
     * @param apiSecret  The secret associated with the API key
     * @param userKey  The SmugMug account's user key (to prevent interactive authentication)
     * @param userSecret  The secret associated with the user key
     */
    public SmugMugAPI(String apiKey, String apiSecret, String userKey, String userSecret) {

        // create a service object through which we can make RESTful API calls that are OAuth authenticated
        service = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .build(SmugMugOAuthEndpoints.instance());

        // build an access token that we can use to sign API requests
        accessToken = new OAuth1AccessToken(userKey, userSecret);
    }

    /**
     * Get info about the current user.
     *
     * @return info about the current user.
     */
    public SmugMugAuthorizedUser fetchAuthorizedUser() {
        return get(AUTHORIZED_USER_ENDPOINT, SmugMugAuthorizedUser.class);
    }

    /**
     * Get a list of albums (AKA galleries) associated by the current user.
     *
     * @param authorizedUser  the "current user" object
     * @return  a list of the current user's albums
     */
    public List<SmugMugAlbum> fetchAlbums(SmugMugAuthorizedUser authorizedUser) {
        var albums = new LinkedList<SmugMugAlbum>();

        // determine the endpoint where we can query for a list of albums
        var endpoint = API_BASE_URL + authorizedUser.user().uris().userAlbums().uri();

        // as long as there are more pages of albums
        while (endpoint != null) {

            // get a page of albums
            var page = get(endpoint, SmugMugAlbumList.class);

            // add each album on the page to the list
            for (var album : page.albums()) {
                albums.add(album);
            }

            // determine the URL for the next page of albums
            var nextPage = page.pages().nextPage();
            endpoint = nextPage == null ? null : API_BASE_URL + nextPage;
        }

        return albums;
    }

    /**
     * Get a list of images (and videos) in a given album (AKA gallery).
     *
     * @param album  the album to inspect
     * @return  a list of images (and videos) in the album
     */
    public List<SmugMugAlbumImage> fetchImages(SmugMugAlbum album) {
        var images = new LinkedList<SmugMugAlbumImage>();

        // determine the endpoint where we can query for a list of images for the given album
        var endpoint = API_BASE_URL + album.uris().albumImages().uri();

        // as long as there are more pages of images
        while (endpoint != null) {

            // get a page of images
            var page = get(endpoint, SmugMugAlbumImageList.class);

            // add each image on the page to the list
            for (var image : page.albumImages()) {
                images.add(image);
            }

            // determine the URL for the next page of images
            var nextPage = page.pages().nextPage();
            endpoint = nextPage == null ? null : API_BASE_URL + nextPage;
        }

        return images;
    }

    /**
     * Get the largest video associated with an album entry (called an album image even if it's a video).
     *
     * @param image  the album image to inspect.  must be a video or results are undetermined.
     * @return  the video info
     */
    public SmugMugLargestVideo fetchLargestVideo(SmugMugAlbumImage image) {
        var largest = get(API_BASE_URL + image.uris().largestVideo().uri(), SmugMugLargestVideo.class);
        return largest;
    }

    /**
     * Download binary content from SmugMug.
     *
     * @param url  The url referencing binary content (like an image or video).
     * @param destination  A local file system path + filename where the content should get written.
     */
    public void downloadBinary(String url, File destination) {

        // build and sign the request using the URL
        var request = new OAuthRequest(Verb.GET, url);
        service.signRequest(accessToken, request);

        // download the content from the response
        try {
            var response = service.execute(request);
            Files.copy(response.getStream(), destination.toPath());
        }
        catch (IOException | InterruptedException | ExecutionException e) {
            throw new SmugMugException(e);
        }
    }

    /**
     * Submit a GET request to the SmugMug API.
     *
     * @param requestURL  The request's endpoint.
     * @param responseClass  A java class representing the structure of the expected response.
     * @param <T>  The type of the java class that represents the structure of the expected response.
     * @return  A java object containing the information from the SmugMug response (parsed from JSON)
     */
    protected <T> T get(String requestURL, Class<T> responseClass) {
        return submitRequest(Verb.GET, requestURL, responseClass);
    }

    /**
     * Submit a request to the SmugMug API.
     *
     * @param method  The HTTP method to be used (GET/POST/PUT, etc.)
     * @param requestURL  The request's endpoint.
     * @param responseClass  A java class representing the structure of the expected response.
     * @param <T>  The type of the java class that represents the structure of the expected response.
     * @return  A java object containing the information from the SmugMug response (parsed from JSON)
     */
    protected <T> T submitRequest(Verb method, String requestURL, Class<T> responseClass) {

        // build a request object expecting a JSON response
        var request = new OAuthRequest(Verb.GET, requestURL);
        request.addHeader("Accept", "application/json");

        // sign the request with our authorization token
        service.signRequest(accessToken, request);

        try {
            // send the request and check response code for success
            var response = service.execute(request);
            if (response.getCode() != 200) {
                throw new SmugMugException("call to " + requestURL + " failed with HTTP code " + response.getCode() + " and message: " + response.getMessage());
            }

            // parse the response from JSON into a java object
            var json = response.getBody();
            var parsedResponse = (SmugMugResponse<T>) mapper.readValue(json, mapper.getTypeFactory().constructParametricType(SmugMugResponse.class, responseClass));

            // return the java object
            return parsedResponse.getResponseObject();
        }
        catch (IOException | InterruptedException | ExecutionException e) {
            throw new SmugMugException(e);
        }
    }
}
