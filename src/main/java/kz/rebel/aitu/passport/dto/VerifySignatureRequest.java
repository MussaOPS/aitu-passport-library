package kz.rebel.aitu.passport.dto;

public class VerifySignatureRequest {
    private String document;
    private String signature;

    public VerifySignatureRequest(String document, String signature) {
        this.document = document;
        this.signature = signature;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
