package com.signal.signalbe.domain.home;

import com.signal.signalbe.domain.card.Card;
import com.signal.signalbe.domain.card.CardRepository;
import com.signal.signalbe.domain.card.CardStatus;
import com.signal.signalbe.domain.category.UserInterestRepository;
import com.signal.signalbe.domain.result.CardResultRepository;
import com.signal.signalbe.domain.result.ResultStatus;
import com.signal.signalbe.domain.transaction.BookmarkRepository;
import com.signal.signalbe.domain.transaction.Purchase;
import com.signal.signalbe.domain.transaction.PurchaseRepository;
import com.signal.signalbe.domain.user.CreatorProfile;
import com.signal.signalbe.domain.user.CreatorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeSummaryService {

    private final PurchaseRepository purchaseRepository;
    private final BookmarkRepository bookmarkRepository;
    private final UserInterestRepository userInterestRepository;
    private final CardRepository cardRepository;
    private final CreatorProfileRepository creatorProfileRepository;
    private final CardResultRepository cardResultRepository;

    public HomeSummary getSummary(Long userId) {
        List<Purchase> purchases = purchaseRepository.findByBuyerId(userId);
        LocalDateTime lastPurchasedAt = purchases.stream()
                .map(Purchase::getPurchasedAt)
                .max(Comparator.naturalOrder())
                .orElse(null);

        int savedCount = bookmarkRepository.findByUserId(userId).size();
        int interestTopicCount = userInterestRepository.findByUserId(userId).size();

        List<Card> authoredCards = cardRepository.findByAuthorId(userId);
        int inReviewCount = (int) authoredCards.stream()
                .filter(card -> card.getStatus() == CardStatus.AI_REVIEWING
                        || card.getStatus() == CardStatus.PRICE_SETTING)
                .count();
        int publishedCount = creatorProfileRepository.findByUserId(userId)
                .map(CreatorProfile::getTotalPublishedCount)
                .orElse(0);

        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        int resultsThisWeek = (int) cardResultRepository.findByCard_Author_Id(userId).stream()
                .filter(result -> result.getStatus() == ResultStatus.EVALUATED
                        && result.getEvaluatedAt() != null
                        && result.getEvaluatedAt().isAfter(weekAgo))
                .count();

        return new HomeSummary(
                purchases.size(), lastPurchasedAt, savedCount, interestTopicCount,
                publishedCount, inReviewCount, resultsThisWeek);
    }
}
