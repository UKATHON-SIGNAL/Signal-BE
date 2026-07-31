package com.signal.signalbe.domain.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByBuyerId(Long buyerId);

    List<Purchase> findByCardId(Long cardId);

    Optional<Purchase> findByBuyerIdAndCardId(Long buyerId, Long cardId);

    boolean existsByBuyerIdAndCardId(Long buyerId, Long cardId);
}
