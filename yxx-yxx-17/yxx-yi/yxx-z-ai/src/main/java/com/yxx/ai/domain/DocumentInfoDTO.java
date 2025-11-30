package com.yxx.ai.domain;

public class DocumentInfoDTO {

    private Integer size;

    private Integer contentLength;

    public Integer getSize() {
        return size;
    }

    public DocumentInfoDTO setSize(Integer size) {
        this.size = size;
        return this;
    }

    public Integer getContentLength() {
        return contentLength;
    }

    public DocumentInfoDTO setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
        return this;
    }
}
