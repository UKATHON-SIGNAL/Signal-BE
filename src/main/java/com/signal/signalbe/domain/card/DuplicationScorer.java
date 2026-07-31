package com.signal.signalbe.domain.card;

import java.util.HashSet;
import java.util.Set;

final class DuplicationScorer {

    private static final int SHINGLE_SIZE = 2;

    private DuplicationScorer() {
    }

    static double similarity(String a, String b) {
        Set<String> shinglesA = shingles(a);
        Set<String> shinglesB = shingles(b);
        if (shinglesA.isEmpty() || shinglesB.isEmpty()) {
            return 0.0;
        }

        Set<String> intersection = new HashSet<>(shinglesA);
        intersection.retainAll(shinglesB);
        Set<String> union = new HashSet<>(shinglesA);
        union.addAll(shinglesB);

        return (double) intersection.size() / union.size();
    }

    private static Set<String> shingles(String text) {
        String normalized = text == null ? "" : text.replaceAll("\\s+", "");
        Set<String> result = new HashSet<>();
        for (int i = 0; i <= normalized.length() - SHINGLE_SIZE; i++) {
            result.add(normalized.substring(i, i + SHINGLE_SIZE));
        }
        return result;
    }
}
