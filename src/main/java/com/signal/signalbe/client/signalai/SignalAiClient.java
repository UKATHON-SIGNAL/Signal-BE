package com.signal.signalbe.client.signalai;

import com.signal.signalbe.client.signalai.dto.BriefingRequest;
import com.signal.signalbe.client.signalai.dto.BriefingResponse;
import com.signal.signalbe.client.signalai.dto.DraftAssistRequest;
import com.signal.signalbe.client.signalai.dto.DraftAssistResponse;
import com.signal.signalbe.client.signalai.dto.ResolveRequest;
import com.signal.signalbe.client.signalai.dto.ResolveResponse;
import com.signal.signalbe.client.signalai.dto.VerifyRequest;
import com.signal.signalbe.client.signalai.dto.VerifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Component
@RequiredArgsConstructor
public class SignalAiClient {

    private final RestClient signalAiRestClient;

    public DraftAssistResponse draftAssist(DraftAssistRequest request) {
        return post("/api/draft-assist", request, DraftAssistResponse.class);
    }

    public VerifyResponse verify(VerifyRequest request) {
        return post("/api/verify", request, VerifyResponse.class);
    }

    public ResolveResponse resolve(ResolveRequest request) {
        return post("/api/resolve", request, ResolveResponse.class);
    }

    public BriefingResponse briefing(BriefingRequest request) {
        return post("/api/briefing", request, BriefingResponse.class);
    }

    private <T> T post(String path, Object body, Class<T> responseType) {
        try {
            return signalAiRestClient.post()
                    .uri(path)
                    .body(body)
                    .retrieve()
                    .body(responseType);
        } catch (RestClientResponseException e) {
            throw new SignalAiException(
                    "Signal-AI 호출 실패: " + path + " (" + e.getStatusCode() + ") " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            throw new SignalAiException("Signal-AI 호출 실패: " + path, e);
        }
    }
}
