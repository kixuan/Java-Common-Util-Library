package org.xuan.RedisLimiter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RedissonClient;
import org.xuan.exception.BusinessException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RedisLimiterManagerTest {

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RRateLimiter rateLimiter;

    @InjectMocks
    private RedisLimiterManager redisLimiterManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redissonClient.getRateLimiter(anyString())).thenReturn(rateLimiter);
    }

    @Test
    void testDoRateLimit_Success() {
        when(rateLimiter.tryAcquire(1)).thenReturn(true);

        redisLimiterManager.doRateLimit("testUserId");

        verify(rateLimiter).tryAcquire(1);
    }

    @Test
    void testDoRateLimit_TooManyRequests() {
        when(rateLimiter.tryAcquire(1)).thenReturn(false);

        assertThrows(BusinessException.class, () -> {
            redisLimiterManager.doRateLimit("testUserId");
        });

        verify(rateLimiter).tryAcquire(1);
    }
}
