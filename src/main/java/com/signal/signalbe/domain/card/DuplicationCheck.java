package com.signal.signalbe.domain.card;

import java.math.BigDecimal;

record DuplicationCheck(BigDecimal score, Long mostSimilarCardId, String mostSimilarCardTitle) {
}
