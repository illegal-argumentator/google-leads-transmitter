package com.vlad_iglin.google_leads_transmitter.adapter.smartmoving.out;

import com.vlad_iglin.google_leads_transmitter.adapter.http.out.OkHttpService;
import com.vlad_iglin.google_leads_transmitter.adapter.http.out.dto.ResponseBody;
import com.vlad_iglin.google_leads_transmitter.domain.lead.Lead;
import com.vlad_iglin.google_leads_transmitter.port.MovingLeadCommandPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
class MovingLeadCommandAdapter implements MovingLeadCommandPort {

    @Value("${smartmoving.provider-key}")
    private String PROVIDER_KEY;

    private final ObjectMapper objectMapper;

    private final OkHttpService okHttpService;

    @Override
    public boolean save(Lead lead) {
        String jsonBody = objectMapper.writeValueAsString(lead);

        Request request = new Request.Builder()
                .url(MovingApiPathBuilder.buildLeads(PROVIDER_KEY))
                .post(RequestBody.create(jsonBody, MediaType.get(APPLICATION_JSON_VALUE)))
                .build();
        ResponseBody response = okHttpService.handleApiRequest(request);
        log.info("SmartMoving response: {} for lead: {}.", response, lead);

        return response.isSuccessful();
    }
}
