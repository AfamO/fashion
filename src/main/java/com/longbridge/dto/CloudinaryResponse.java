package com.longbridge.dto;

/**
 * Created by Longbridge on 23/02/2018.
 */
public class CloudinaryResponse {
    private String publicId;
    private String url;

    public CloudinaryResponse() {
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
