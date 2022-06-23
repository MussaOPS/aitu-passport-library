package io.github.mussaops.aitu.passport.dto;

public class UploadDocumentRequest {
    private String name;
    private String bytes;

    public UploadDocumentRequest(String name, String bytes) {
        this.name = name;
        this.bytes = bytes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }
}
