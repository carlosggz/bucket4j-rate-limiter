package com.example.bucket4jratelimiter.schedule;

import io.github.bucket4j.postgresql.PostgreSQLadvisoryLockBasedProxyManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RemoveExpiredBuckets {
    private final PostgreSQLadvisoryLockBasedProxyManager<String> proxyManager;
    private final int maxToRemove;
    private final int threshold;

    @Getter
    private int removedOnLastExecution;

    public RemoveExpiredBuckets(
            PostgreSQLadvisoryLockBasedProxyManager<String> proxyManager,
            @Value("${app.housekeeping.max-buckets}") int maxToRemove,
            @Value("${app.housekeeping.threshold}") int threshold) {
        this.proxyManager = proxyManager;
        this.maxToRemove = maxToRemove;
        this.threshold = threshold;
        this.removedOnLastExecution = 0;
    }

    @Scheduled(cron = "${app.housekeeping.cron}")
    public void doHousekeeping() {
        int removedKeysCount = 0;
        int totalRemoved = 0;

        do {
            removedKeysCount = proxyManager.removeExpired(maxToRemove);
            totalRemoved += removedKeysCount;

            if (removedKeysCount > 0) {
                log.info("Removed {} expired buckets", removedKeysCount);
            } else {
                log.info("There are no expired buckets to remove");
            }
        } while (removedKeysCount >= threshold);

        removedOnLastExecution = totalRemoved;
    }
}
