package org.themullers.smugmugDownloader.scribe;

import com.github.scribejava.core.builder.api.DefaultApi10a;

/**
 * These are the SmugMug OAuth endpoints provided in the format needed by Scribe Java's
 * ServiceBuilder.build() method.
 */
public class SmugMugOAuthEndpoints extends DefaultApi10a  {

    private static final String AUTHORIZE_URL = "https://api.smugmug.com/services/oauth/1.0a/getRequestToken";
    private static final String REQUEST_TOKEN_RESOURCE = "https://api.smugmug.com/services/oauth/1.0a/getRequestToken";
    private static final String ACCESS_TOKEN_RESOURCE = "https://api.smugmug.com/services/oauth/1.0a/getAccessToken";

    protected SmugMugOAuthEndpoints() {
    }

    private static class InstanceHolder {
        private static final SmugMugOAuthEndpoints INSTANCE = new SmugMugOAuthEndpoints();
    }

    public static SmugMugOAuthEndpoints instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_RESOURCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_RESOURCE;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }
}
