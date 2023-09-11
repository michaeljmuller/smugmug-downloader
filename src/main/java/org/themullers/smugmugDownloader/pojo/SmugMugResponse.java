package org.themullers.smugmugDownloader.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/*
 * All SmugMug API responses are wrapped in this object.
 */
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class SmugMugResponse<T> {
    private T responseObject;
    private int code;
    private String message;

    @JsonProperty("Response")
    public T getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(T responseObject) {
        this.responseObject = responseObject;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}