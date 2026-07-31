package com.signal.signalbe.api.purchase;

import com.signal.signalbe.domain.transaction.MyPurchasesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/my-purchases")
@RequiredArgsConstructor
public class MyPurchasesController {

    private final MyPurchasesService myPurchasesService;

    @GetMapping
    public List<MyPurchaseItemResponse> getMyPurchases(@PathVariable Long userId) {
        return myPurchasesService.getMyPurchases(userId).stream().map(MyPurchaseItemResponse::from).toList();
    }

    @GetMapping("/summary")
    public MyPurchasesSummaryResponse getSummary(@PathVariable Long userId) {
        return MyPurchasesSummaryResponse.from(myPurchasesService.getSummary(userId));
    }

    @GetMapping("/recent-updates")
    public List<RecentUpdateResponse> getRecentUpdates(
            @PathVariable Long userId, @RequestParam(defaultValue = "5") int limit) {
        return myPurchasesService.getRecentUpdates(userId, limit).stream()
                .map(RecentUpdateResponse::from)
                .toList();
    }
}
