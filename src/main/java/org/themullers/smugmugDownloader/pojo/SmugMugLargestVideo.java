package org.themullers.smugmugDownloader.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * This is the structure returned by SmugMug's API when queried for
 * information about the largest video file associated with an AlbumImage.
 */
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public record SmugMugLargestVideo(String uri,
                           String locator,
                           String locatorType,
                           @JsonProperty("LargestVideo") SmugMugVideo video) {

    @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
    public record SmugMugVideo(String url,
                               long size,
                               String MD5) {
    }
}
