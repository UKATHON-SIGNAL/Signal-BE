package com.signal.signalbe.client.signalai;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "signal-ai")
public record SignalAiProperties(String baseUrl) {
}
