package org.themullers.smugmugDownloader.pojo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * This is the object returned by a SmugMug API request for info about an image (or video)
 */
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public record SmugMugAlbumImage(String fileName,
                                String imageKey,
                                String archivedMD5,
                                String archivedUri,
                                long archivedSize,
                                boolean isVideo,
                                SmugMugAlbumImageURIs uris) {

    @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
    public record SmugMugAlbumImageURIs(SmugMugURI largestVideo) {
    }
}
