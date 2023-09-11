package org.themullers;

import org.themullers.smugmugDownloader.pojo.SmugMugAlbumImage;
import org.themullers.smugmugDownloader.scribe.SmugMugAPI;
import org.themullers.smugmugDownloader.scribe.SmugMugException;
import org.themullers.smugmugDownloader.utils.MD5;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Downloads all the images in a SmugMug account to a local directory.
 *
 * The following environment variables must be set for the application to run properly:
 *  - API_KEY
 *  - API_SECRET
 *  - USER_KEY
 *  - USER_SECRET
 *  - TARGET_DIRECTORY
 *
 * The API key and secret can be obtained by applying as a developer on SmugMug's web site.
 *
 * The user key and secret can be obtained from SmugMug's privacy page under "Authorized
 * Services".
 *
 * The target directory should be set to the place on the local file system you want the photos written.
 *
 * TODO: Implement multi-threading with the ExecutorService for faster downloads.
 *
 * @see <a href="https://api.smugmug.com/api/developer/apply">SmugMug API Key request form</a>
 * @see <a href="https://www.smugmug.com/app/account/settings/?#section=privacy">SmugMug Privacy Page</a>
 */
public class SmugMugDownloader implements Runnable {

    protected SmugMugAPI smugmug = null;
    public String targetDirectory = null;

    /**
     * Constructor to initialize the SmugMugDownloader object.
     *
     * The following environment variables must be set or the constructor fails:
     *  - API_KEY
     *  - API_SECRET
     *  - USER_KEY
     *  - USER_SECRET
     *  - TARGET_DIRECTORY
     */
    public SmugMugDownloader() {

        // get the credentials we should use to connect to SmugMug
        var apiKey = getArg("API_KEY");
        var apiSecret = getArg("API_SECRET");
        var userKey = getArg("USER_KEY");
        var userSecret = getArg("USER_SECRET");

        // get the location where we should write the downloaded images
        targetDirectory = getArg("TARGET_DIRECTORY");

        // instantiate the smugmug interface object
        smugmug = new SmugMugAPI(apiKey, apiSecret, userKey, userSecret);
    }

    /**
     * Gets the value of an environment variable
     *
     * @param arg  the environment variable to examine
     * @return  the environment variable's value
     * @throws SmugMugException  if the environment variable is not set (or if it's set to a blank value)
     */
    protected String getArg(String arg) {
        var value = System.getenv(arg);
        if (value == null || value.trim().length() == 0) {
            throw new SmugMugException("Missing required environment variable " + arg);
        }
        return value;
    }

    /**
     * Main entry point for the app.
     *
     * @param args  Any arguments passed in are ignored; configuration is done via environment variables.
     */
    public static void main( String[] args ) {
        new SmugMugDownloader().run();
    }

    /**
     * Downloads each image (or video) in each of the current user's albums (AKA galleries).
     */
    @Override
    public void run() {
        // for each of the user's albums
        var user = smugmug.fetchAuthorizedUser();
        var albums = smugmug.fetchAlbums(user);
        for (var album : albums) {

            // TODO: remove (it's here for debugging)
            if (!album.urlPath().equals("/2023/April-2023")) {
                continue;
            }

            // make the path to this album (if it's not already there)
            var albumFolder = new File(targetDirectory, album.urlPath());
            if (!albumFolder.exists()) {
                System.out.println("making path " + albumFolder);
                albumFolder.mkdirs();
            }

            // for each image in the album
            var images = smugmug.fetchImages(album);
            for (var image : images) {

                // TODO: remove this (it's here for debugging)
                if (!image.fileName().equals("IMG_3419.JPG")) {
                    continue;
                }

                // download the image
                download(image, albumFolder);
            }
        }
    }

    /**
     * Download an image (or video)
     * @param image  the SmugMugAlbumImage object representing the image (or video)
     * @param albumFolder  the filename (and path) where the image should be written
     */
    protected void download(SmugMugAlbumImage image, File albumFolder) {

        // generate the filename for the image
        var imageFile = new File(albumFolder, image.imageKey() + "-" + image.fileName());

        // file size and URL for images
        var remoteSize = image.archivedSize();
        var remoteChecksum = image.archivedMD5();
        var downloadUrl = image.archivedUri();

        // file size and URL for videos
        if (image.isVideo()) {
            var largest = smugmug.fetchLargestVideo(image);
            remoteSize = largest.video().size();
            remoteChecksum = largest.video().MD5();
            downloadUrl = largest.video().url();
        }

        // if the file doesn't exist, download it
        if (!imageFile.exists()) {
            System.out.println("*** " + imageFile + " does not exist!");
            smugmug.downloadBinary(downloadUrl, imageFile);
            System.out.println("*** downloaded");
            validate(imageFile, remoteSize, remoteChecksum);
        }

        // if the file does exist, validate
        else {
            validate(imageFile, remoteSize, remoteChecksum);
        }
    }

    /**
     * Perform file size and MD5 checksum comparisons to validate that the download
     * completed without corruption.
     *
     * @param imageFile  The file that was downloaded
     * @param remoteSize  The size of the file in SmugMug
     * @param remoteChecksum  The checksum of the file in SmugMug
     * @return  True if the validation passes, false otherwise
     */
    protected boolean validate(File imageFile, long remoteSize, String remoteChecksum) {
        try {
            // compare file size
            var localSize = imageFile.length();
            var sizeMatch = remoteSize == localSize;

            // compare checksum
            var localChecksum = MD5.checksum(imageFile);
            var checksumMatch = remoteChecksum.equals(localChecksum);

            var valid = sizeMatch && checksumMatch;

            if (valid) {
                System.out.println("file " + imageFile + " passes validation for file size and checksum");
            }
            else {
                System.out.println("*** file " + imageFile + " fails validation check");
                System.out.println("*** remote size = " + remoteSize + ", local size = " + localSize);
                System.out.println("*** file sizes " + (sizeMatch ? "match" : "do not match"));
                System.out.println("*** remote checksum = " + remoteChecksum + ", local checksum = " + localChecksum);
                System.out.println("*** checksums " + (checksumMatch ? "match" : "do not match"));
            }

            return valid;
        }
        catch (NoSuchAlgorithmException | IOException e) {
            throw new SmugMugException(e);
        }
    }
}
