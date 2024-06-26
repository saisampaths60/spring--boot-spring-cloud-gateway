package com.sample.springcloudgatewayapplication.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitByPathGatewayFilterFactory implements GatewayFilterFactory<RateLimitByPathGatewayFilterFactory.Config> {

    private final Map<String, Map<Long, Long>> counters = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);

    public RateLimitByPathGatewayFilterFactory() {
        scheduler.scheduleAtFixedRate(this::resetCounters, 1, 1, TimeUnit.MINUTES);
    }

    private void resetCounters() {
        counters.clear();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().value();
            long now = System.currentTimeMillis();
            long oneMinuteAgo = now - 60000;

            Map<Long, Long> pathCounters = counters.computeIfAbsent(path, k -> new ConcurrentHashMap<>());
            long count = pathCounters.entrySet().stream()
                    .filter(entry -> entry.getKey() > oneMinuteAgo)
                    .mapToLong(Map.Entry::getValue)
                    .sum();

            if (count < config.getLimit()) {
                pathCounters.put(now, pathCounters.getOrDefault(now, 0L) + 1);
                return chain.filter(exchange);
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            }
        };
    }

    @Override
    public Config newConfig() {
        return new Config();
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    public static class Config {
        private int limit = 5; // Default limit

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }
    }
}
