package org.themullers.smugmugDownloader.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

/**
 * This is the structure returned by the SmugMug API when queried for
 * a list of albums (AKA galleries).
 */
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public record SmugMugAlbumList(String uri,
                               String locator,
                               String locatorType,
                               @JsonProperty("Album") List<SmugMugAlbum> albums,
                               SmugMugPagination pages) {
}
