package com.signal.signalbe.client.signalai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.signal.signalbe.domain.result.ResultVerdict;

public record ResolveResponse(ResultVerdict verdict, @JsonProperty("ai_reason") String aiReason) {
}
