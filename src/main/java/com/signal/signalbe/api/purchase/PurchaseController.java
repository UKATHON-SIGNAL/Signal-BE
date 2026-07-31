package com.signal.signalbe.api.purchase;

import com.signal.signalbe.domain.transaction.Purchase;
import com.signal.signalbe.domain.transaction.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase(@Valid @RequestBody PurchaseCreateRequest request) {
        Purchase purchase = purchaseService.createPurchase(request.buyerId(), request.cardId());
        return ResponseEntity.status(HttpStatus.CREATED).body(PurchaseResponse.from(purchase));
    }

    @GetMapping
    public List<PurchaseResponse> getPurchases(@RequestParam Long buyerId) {
        return purchaseService.getPurchasesByBuyer(buyerId).stream().map(PurchaseResponse::from).toList();
    }
}
