package com.signal.signalbe.api.card;

import com.signal.signalbe.domain.card.Card;
import com.signal.signalbe.domain.card.CardService;
import com.signal.signalbe.domain.card.CardSourceCreation;
import com.signal.signalbe.domain.card.CardStatus;
import com.signal.signalbe.domain.verification.AiVerification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CardCreateRequest request) {
        List<CardSourceCreation> sources = request.sources().stream()
                .map(s -> new CardSourceCreation(s.url(), s.title(), s.publisher(), s.sourcePublishedAt()))
                .toList();

        Card card = cardService.createDraft(
                request.authorId(), request.categoryId(), request.claim(), request.successCondition(),
                request.failureCondition(), request.evidenceSummary(), request.resultDueAt(),
                request.topicIds(), sources);

        return ResponseEntity.status(HttpStatus.CREATED).body(CardResponse.from(card));
    }

    @PostMapping("/{cardId}/ai-review")
    public AiReviewResponse requestAiReview(@PathVariable Long cardId) {
        AiVerification verification = cardService.requestAiReview(cardId);
        return AiReviewResponse.from(verification);
    }

    @PutMapping("/{cardId}/price")
    public CardResponse setPrice(@PathVariable Long cardId, @Valid @RequestBody PriceSettingRequest request) {
        Card card = cardService.setPrice(cardId, request.salePrice());
        return CardResponse.from(card);
    }

    @PostMapping("/{cardId}/publish")
    public CardResponse publish(@PathVariable Long cardId) {
        Card card = cardService.publish(cardId);
        return CardResponse.from(card);
    }

    @GetMapping
    public List<CardResponse> getCards(@RequestParam(required = false) CardStatus status) {
        return cardService.getCards(status).stream().map(CardResponse::from).toList();
    }

    @GetMapping("/{cardId}")
    public CardResponse getCard(@PathVariable Long cardId) {
        return CardResponse.from(cardService.getCard(cardId));
    }
}
