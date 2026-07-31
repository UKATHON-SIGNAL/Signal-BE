package com.signal.signalbe.domain.transaction;

import com.signal.signalbe.domain.card.Card;
import com.signal.signalbe.domain.card.CardRepository;
import com.signal.signalbe.domain.card.CardStatus;
import com.signal.signalbe.domain.user.User;
import com.signal.signalbe.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    @Transactional
    public Purchase createPurchase(Long buyerId, Long cardId) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + buyerId));
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 정보 카드입니다. id=" + cardId));

        if (card.getStatus() != CardStatus.PUBLISHED) {
            throw new IllegalStateException("발행된 카드만 구매할 수 있습니다. 현재 상태=" + card.getStatus());
        }
        if (purchaseRepository.existsByBuyerIdAndCardId(buyerId, cardId)) {
            throw new IllegalStateException("이미 구매한 카드입니다.");
        }

        Purchase purchase = new Purchase(buyer, card, card.getSalePrice());
        return purchaseRepository.save(purchase);
    }

    public List<Purchase> getPurchasesByBuyer(Long buyerId) {
        return purchaseRepository.findByBuyerId(buyerId);
    }

    @Transactional
    public void confirmResult(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구매입니다. id=" + purchaseId));
        purchase.markResultChecked(LocalDateTime.now());
    }
}
