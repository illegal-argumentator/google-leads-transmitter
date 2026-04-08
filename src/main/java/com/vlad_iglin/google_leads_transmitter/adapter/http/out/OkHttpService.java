package com.vlad_iglin.google_leads_transmitter.adapter.http.out;

import com.vlad_iglin.google_leads_transmitter.adapter.http.out.dto.ResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OkHttpService {

    private final OkHttpClient okHttpClient;

    public ResponseBody handleApiRequest(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            String body = response.body().string();
            if (!response.isSuccessful()) {
                throw new RuntimeException("response: " + body + ", code: " + response.code());
            }
            return new ResponseBody(response.code(), body, null);
        } catch (Exception e) {
            return new ResponseBody(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage());
        }
    }
}
