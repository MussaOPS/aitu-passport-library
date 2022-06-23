package io.github.mussaops.aitu.passport.service;

import io.github.mussaops.aitu.passport.dto.*;
import io.github.mussaops.aitu.passport.config.AituPassportProperties;
import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Objects;

import static java.util.concurrent.TimeUnit.*;

@Service
public class AituPassportService {
    @Autowired
    private AituPassportHelperService helperService;
    private final AituPassportProperties properties;
    private OkHttpClient http;
    ObjectMapper mapper = new ObjectMapper();

    public AituPassportService(AituPassportProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        http = new OkHttpClient.Builder().readTimeout(60, MINUTES).build();
    }

    public String getSessionId() throws IOException {
        Request request = new Request.Builder()
                .url(properties.getUrl() + "/api/v1/oauth/session")
                .post(RequestBody.create("", MediaType.parse("application/json")))
                .header("Authorization", Credentials.basic(properties.getClientId(), properties.getSecret()))
                .header("User-Agent", "Other")
                .build();

        try {
            Response response = http.newCall(request).execute();

            helperService.checkResponseCode(response);

            SessionResponse sessionResponse = mapper.readValue(Objects.requireNonNull(response.body()).string(), SessionResponse.class);

            return sessionResponse.getSessionId();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public TokenResponse getToken(String code) throws IOException {
        RequestBody body = new FormBody.Builder()
                .addEncoded("grant_type", "authorization_code")
                .addEncoded("code", code)
                .addEncoded("redirect_uri", properties.getRedirectUri())
                .build();

        Request request = new Request.Builder()
                .url(properties.getUrl() + "/api/v1/oauth/token")
                .post(body)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", Credentials.basic(properties.getClientId(), properties.getSecret()))
                .header("User-Agent", "Other")
                .build();

        try {
            Response response = http.newCall(request).execute();

            helperService.checkResponseCode(response);

            return mapper.readValue(Objects.requireNonNull(response.body()).string(), TokenResponse.class);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public SignatureResponse getSignature(TokenResponse token) throws IOException {
        Request request = new Request.Builder()
                .url(properties.getUrl() + "/api/v2/oauth/signatures")
                .get()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .header("User-Agent", "Other")
                .build();

        try {
            Response response = http.newCall(request).execute();

            helperService.checkResponseCode(response);

            return mapper.readValue(Objects.requireNonNull(response.body()).string(), SignatureResponse.class);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public String uploadDocument(String name, File file) throws IOException {
        return uploadDocument(name, new FileInputStream(file));
    }

    public String uploadDocument(String name, InputStream is) throws IOException {
        return uploadDocument(Base64Utils.encodeToString(IOUtils.toByteArray(is)), name);
    }

    public String uploadDocument(String contentHash, String document) throws IOException {
        UploadDocumentRequest dto = new UploadDocumentRequest(contentHash, document);

        Request request = new Request.Builder()
                .url(properties.getUrl() + "/api/v2/oauth/signable")
                .post(RequestBody.create(mapper.writeValueAsString(dto), MediaType.parse("application/json")))
                .header("Authorization", Credentials.basic(properties.getClientId(), properties.getSecret()))
                .header("User-Agent", "Other")
                .build();

        try {
            Response response = http.newCall(request).execute();

            helperService.checkResponseCode(response);

            return mapper.readTree(Objects.requireNonNull(response.body()).string())
                    .path("signableId")
                    .asText();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public boolean verifySignature(File document, String signature) throws IOException {
        return verifySignature(new FileInputStream(document), signature);
    }

    public boolean verifySignature(InputStream is, String signature) throws IOException {
        return verifySignature(Base64Utils.encodeToString(IOUtils.toByteArray(is)), signature);
    }

    public boolean verifySignature(String document, String signature) throws IOException {
        VerifySignatureRequest dto = new VerifySignatureRequest(document, signature);

        Request request = new Request.Builder()
                .url(properties.getUrl() + "/api/v2/oauth/signatures/verify")
                .post(RequestBody.create(mapper.writeValueAsString(dto), MediaType.parse("application/json")))
                .header("Authorization", Credentials.basic(properties.getClientId(), properties.getSecret()))
                .header("User-Agent", "Other")
                .build();

        try {
            Response response = http.newCall(request).execute();

            helperService.checkResponseCode(response);

            return mapper.readTree(Objects.requireNonNull(response.body()).string())
                    .path("valid")
                    .asBoolean(false);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }
}
