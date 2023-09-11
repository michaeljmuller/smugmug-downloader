package org.themullers.smugmugDownloader.pojo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * All SmugMug responses containing a URI are returned in this structure.
 */
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public record SmugMugURI(String uri,
                         String locator,
                         String locatorType,
                         String uriDescription,
                         String endpointType) {
}
