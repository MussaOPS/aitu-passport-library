package io.github.mussaops.aitu.passport.dto;

public class SignatureResponse {
    private String signableId;
    private String signature;

    public String getSignableId() {
        return signableId;
    }

    public void setSignableId(String signableId) {
        this.signableId = signableId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
