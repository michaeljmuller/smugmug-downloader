package org.themullers.smugmugDownloader.pojo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * This is the list of URIs available to access user-specific information from
 * the SmugMug API.
 */
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public record SmugMugUserURIs(SmugMugURI bioImage,
                              SmugMugURI coverImage,
                              SmugMugURI userProfile,
                              SmugMugURI node,
                              SmugMugURI folder,
                              SmugMugURI features,
                              SmugMugURI siteSettings,
                              SmugMugURI userAlbums,
                              SmugMugURI userGeoMedia,
                              SmugMugURI userPopularMedia,
                              SmugMugURI userFeaturedAlbums,
                              SmugMugURI userRecentImages,
                              SmugMugURI userImageSearch,
                              SmugMugURI userTopKeywords,
                              SmugMugURI urlPathLookup,
                              SmugMugURI userAlbumTemplates,
                              SmugMugURI sortUserFeaturedAlbums,
                              SmugMugURI userTasks,
                              SmugMugURI userWatermarks,
                              SmugMugURI userPrintmarks,
                              SmugMugURI userUploadLimits,
                              SmugMugURI userAssetsAlbum,
                              SmugMugURI userGuideStates,
                              SmugMugURI userHideGuides,
                              SmugMugURI userGrants,
                              SmugMugURI duplicateImageSearch,
                              SmugMugURI userDeletedAlbums,
                              SmugMugURI userDeletedFolders,
                              SmugMugURI userDeletedPages,
                              SmugMugURI userContacts,
                              SmugMugURI rawManagementAddOnStatus) {
}
