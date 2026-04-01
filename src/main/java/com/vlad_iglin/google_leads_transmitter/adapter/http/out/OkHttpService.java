package com.vlad_iglin.google_leads_transmitter.adapter.http.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OkHttpService {

    private final OkHttpClient okHttpClient;

    public String handleApiRequest(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            if (!response.isSuccessful()) {
                throw new RuntimeException("response: " + responseBody + ", code: " + response.code());
            }
            return responseBody;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
