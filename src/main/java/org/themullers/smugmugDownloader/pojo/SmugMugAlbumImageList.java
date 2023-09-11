package org.themullers.smugmugDownloader.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

/**
 * This is the object type returned by SmugMug's API when queried for
 * information about the contents of an album (AKA gallery).
 */
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public record SmugMugAlbumImageList(String uri,
                                    String locator,
                                    String locatorType,
                                    @JsonProperty("AlbumImage") List<SmugMugAlbumImage> albumImages,
                                    SmugMugPagination pages) {
}
