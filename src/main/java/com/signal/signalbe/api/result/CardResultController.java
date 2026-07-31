package com.signal.signalbe.api.result;

import com.signal.signalbe.domain.result.CardResult;
import com.signal.signalbe.domain.result.CardResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards/{cardId}/result")
@RequiredArgsConstructor
public class CardResultController {

    private final CardResultService cardResultService;

    @PostMapping
    public ResponseEntity<CardResultResponse> submitResult(
            @PathVariable Long cardId, @Valid @RequestBody CardResultCreateRequest request) {
        CardResult cardResult = cardResultService.submitResult(
                cardId, request.submittedById(), request.actualResult(), request.evidenceSummary());
        return ResponseEntity.status(HttpStatus.CREATED).body(CardResultResponse.from(cardResult));
    }

    @GetMapping
    public CardResultResponse getResult(@PathVariable Long cardId) {
        return CardResultResponse.from(cardResultService.getResult(cardId));
    }
}
