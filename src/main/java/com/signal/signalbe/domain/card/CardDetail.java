package com.signal.signalbe.domain.card;

import com.signal.signalbe.domain.user.CreatorProfile;
import com.signal.signalbe.domain.verification.AiVerification;

import java.util.List;

public record CardDetail(
        Card card, CreatorProfile authorProfile, AiVerification latestVerification, List<CardSource> sources) {
}
