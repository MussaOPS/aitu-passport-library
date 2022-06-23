package kz.rebel.aitu.passport.service;

import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class AituPassportHelperService {
    public void checkResponseCode(Response response) throws IOException {
        if (response.code() < 200 || response.code() > 299) {
            throw new RuntimeException(Objects.requireNonNull(response.body()).string());
        }
    }
}
