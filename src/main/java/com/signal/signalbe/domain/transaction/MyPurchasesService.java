package com.signal.signalbe.domain.transaction;

import com.signal.signalbe.domain.card.Card;
import com.signal.signalbe.domain.result.CardResultRepository;
import com.signal.signalbe.domain.result.ResultStatus;
import com.signal.signalbe.domain.verification.AiVerificationRepository;
import com.signal.signalbe.domain.verification.AiVerificationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPurchasesService {

    private final PurchaseRepository purchaseRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CardResultRepository cardResultRepository;
    private final AiVerificationRepository aiVerificationRepository;

    public List<MyPurchaseItem> getMyPurchases(Long userId) {
        List<Purchase> purchases = purchaseRepository.findByBuyerId(userId);
        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId);

        Set<Long> purchasedCardIds = purchases.stream()
                .map(purchase -> purchase.getCard().getId())
                .collect(Collectors.toSet());
        Set<Long> bookmarkedCardIds = bookmarks.stream()
                .map(bookmark -> bookmark.getCard().getId())
                .collect(Collectors.toSet());

        List<MyPurchaseItem> items = new ArrayList<>();

        for (Purchase purchase : purchases) {
            Card card = purchase.getCard();
            boolean bookmarked = bookmarkedCardIds.contains(card.getId());
            items.add(new MyPurchaseItem(
                    purchase, null, card, resolveStatus(purchase), bookmarked, resolveAiVerificationStatus(card)));
        }

        for (Bookmark bookmark : bookmarks) {
            if (!purchasedCardIds.contains(bookmark.getCard().getId())) {
                items.add(new MyPurchaseItem(
                        null, bookmark, bookmark.getCard(), MyPurchaseStatus.SAVED, true,
                        resolveAiVerificationStatus(bookmark.getCard())));
            }
        }

        return items.stream()
                .sorted(Comparator.comparing(MyPurchaseItem::sortDate).reversed())
                .toList();
    }

    public MyPurchasesSummary getSummary(Long userId) {
        List<Purchase> purchases = purchaseRepository.findByBuyerId(userId);
        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId);

        long totalAmount = purchases.stream().mapToLong(Purchase::getPurchasePrice).sum();
        int pendingConfirmationCount = (int) purchases.stream()
                .filter(purchase -> resolveStatus(purchase) == MyPurchaseStatus.PENDING_CONFIRMATION)
                .count();

        return new MyPurchasesSummary(purchases.size(), totalAmount, pendingConfirmationCount, bookmarks.size());
    }

    private AiVerificationStatus resolveAiVerificationStatus(Card card) {
        return aiVerificationRepository.findByCardIdOrderByCreatedAtDesc(card.getId()).stream()
                .findFirst()
                .map(verification -> verification.getStatus())
                .orElse(null);
    }

    private MyPurchaseStatus resolveStatus(Purchase purchase) {
        return cardResultRepository.findByCardId(purchase.getCard().getId())
                .filter(result -> result.getStatus() == ResultStatus.EVALUATED)
                .map(result -> purchase.getResultCheckedAt() != null
                        ? MyPurchaseStatus.CONFIRMED
                        : MyPurchaseStatus.PENDING_CONFIRMATION)
                .orElse(MyPurchaseStatus.IN_PROGRESS);
    }
}
