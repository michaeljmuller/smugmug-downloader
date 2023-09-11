package org.themullers.smugmugDownloader.pojo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * This object is returned as a property of paginated responses from
 * the SmugMug API.
 */
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public record SmugMugPagination(int total,
                                int start,
                                int count,
                                int requestedCount,
                                String firstPage,
                                String lastPage,
                                String nextPage) {
}
