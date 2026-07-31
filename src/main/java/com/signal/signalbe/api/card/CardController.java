package com.signal.signalbe.api.card;

import com.signal.signalbe.domain.card.Card;
import com.signal.signalbe.domain.card.CardSearchCriteria;
import com.signal.signalbe.domain.card.CardService;
import com.signal.signalbe.domain.card.CardSort;
import com.signal.signalbe.domain.card.CardSourceCreation;
import com.signal.signalbe.domain.card.CardStatus;
import com.signal.signalbe.domain.verification.AiVerification;
import com.signal.signalbe.domain.verification.AiVerificationStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDateTime;
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

        return ResponseEntity.status(HttpStatus.CREATED).body(CardResponse.from(cardService.getCardDetail(card.getId())));
    }

    @PostMapping("/{cardId}/ai-review")
    public AiReviewResponse requestAiReview(@PathVariable Long cardId) {
        AiVerification verification = cardService.requestAiReview(cardId);
        return AiReviewResponse.from(verification);
    }

    @PutMapping("/{cardId}/price")
    public CardResponse setPrice(@PathVariable Long cardId, @Valid @RequestBody PriceSettingRequest request) {
        cardService.setPrice(cardId, request.salePrice());
        return CardResponse.from(cardService.getCardDetail(cardId));
    }

    @PostMapping("/{cardId}/publish")
    public CardResponse publish(@PathVariable Long cardId) {
        cardService.publish(cardId);
        return CardResponse.from(cardService.getCardDetail(cardId));
    }

    @GetMapping
    public List<CardResponse> getCards(
            @RequestParam(required = false) CardStatus status,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Integer priceMin,
            @RequestParam(required = false) Integer priceMax,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime resultDueFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime resultDueTo,
            @RequestParam(required = false) AiVerificationStatus aiVerificationStatus,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "LATEST") CardSort sort) {
        CardSearchCriteria criteria = new CardSearchCriteria(
                status, authorId, priceMin, priceMax, resultDueFrom, resultDueTo, aiVerificationStatus, keyword, sort);
        return cardService.getCardDetails(criteria).stream().map(CardResponse::from).toList();
    }

    @GetMapping("/{cardId}")
    public CardResponse getCard(@PathVariable Long cardId) {
        return CardResponse.from(cardService.getCardDetail(cardId));
    }

    @GetMapping("/recommended")
    public List<CardResponse> getRecommendedCards(
            @RequestParam Long userId, @RequestParam(defaultValue = "3") int limit) {
        return cardService.getRecommendedCards(userId, limit).stream().map(CardResponse::from).toList();
    }
}
