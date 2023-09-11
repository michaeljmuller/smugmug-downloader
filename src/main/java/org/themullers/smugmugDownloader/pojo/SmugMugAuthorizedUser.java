package org.themullers.smugmugDownloader.pojo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * This is the object structure returned by the SmugMug API when
 * queried for information about the current user.
 */
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public record SmugMugAuthorizedUser(String uri,
                                    String locator,
                                    String locatorType,
                                    SmugMugUser user,
                                    String uriDescription,
                                    String endpointType) {
    @JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
    public record SmugMugUser(String accountStatus,
                       String firstName,
                       boolean friendsView,
                       long imageCount,
                       boolean isGettingStarted,
                       boolean isTrial,
                       String lastName,
                       String nickName,
                       String sortBy,
                       String viewPassHint,
                       String viewPassword,
                       String domain,
                       String domainOnly,
                       String refTag,
                       String name,
                       String plan,
                       boolean quickShare,
                       String timeZone,
                       String uri,
                       String webUri,
                       String uriDescription,
                       SmugMugUserURIs uris,
                       String responseLevel) {
    }
}
