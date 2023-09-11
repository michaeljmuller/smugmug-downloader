package org.themullers.smugmugDownloader.pojo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * This is the object returned by a SmugMug API request for info about an album (AKA gallery)
 */
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public record SmugMugAlbum(String niceName,
                           String urlName,
                           String title,
                           String name,
                           String urlPath,
                           String uri,
                           SmugMugAlbumURIs uris,
                           SmugMugPagination pages) {

    @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
    public record SmugMugAlbumURIs(SmugMugURI albumImages) {
    }
}
