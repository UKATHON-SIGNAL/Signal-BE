package com.signal.signalbe.domain.card;

import com.signal.signalbe.domain.user.CreatorProfile;
import com.signal.signalbe.domain.verification.AiVerification;

public record CardDetail(Card card, CreatorProfile authorProfile, AiVerification latestVerification, int sourceCount) {
}
