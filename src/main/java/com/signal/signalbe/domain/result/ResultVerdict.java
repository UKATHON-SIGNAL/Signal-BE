package com.signal.signalbe.domain.result;

public enum ResultVerdict {
    SUCCESS(100),
    PARTIAL(70),
    DIRECTION_ONLY(40),
    FAILURE(0),
    INVALID(null);

    private final Integer score;

    ResultVerdict(Integer score) {
        this.score = score;
    }

    public Integer getScore() {
        return score;
    }
}
