package com.signal.signalbe.client.signalai.dto;

public record DraftAssistRequest(DraftAssistAction action, String text, String category) {
}
