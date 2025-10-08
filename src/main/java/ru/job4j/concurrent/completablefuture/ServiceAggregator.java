package ru.job4j.concurrent.completablefuture;

import java.util.concurrent.*;

public class ServiceAggregator {

    // Имитация клиентов к микросервисам
    static class PricingClient {
        CompletableFuture<String> getPrice(String productId) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return "Price: $99.99";
            });
        }
    }

    static class InventoryClient {
        CompletableFuture<String> getInventory(String productId) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(150);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return "Inventory: 10 items";
            });
        }
    }

    static class ReviewsClient {
        CompletableFuture<String> getReviews(String productId) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(120);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return "Reviews: 4.5 stars";
            });
        }
    }

    // DTO для ответа
    record ProductInfo(String price, String inventory, String reviews) {
    }

    private final PricingClient pricingClient = new PricingClient();
    private final InventoryClient inventoryClient = new InventoryClient();
    private final ReviewsClient reviewsClient = new ReviewsClient();

    public CompletableFuture<ProductInfo> getProductInfo(String productId) {
        long start = System.currentTimeMillis();

        CompletableFuture<String> priceFuture = pricingClient.getPrice(productId);
        CompletableFuture<String> inventoryFuture = inventoryClient.getInventory(productId);
        CompletableFuture<String> reviewsFuture = reviewsClient.getReviews(productId);

        return CompletableFuture.allOf(priceFuture, inventoryFuture, reviewsFuture)
                .thenApply(v -> {
                    // join() здесь безопасен, тк. allOf гарантирует завершение
                    String price = priceFuture.join();
                    String inventory = inventoryFuture.join();
                    String reviews = reviewsFuture.join();

                    System.out.println("Aggregation took: " + (System.currentTimeMillis() - start) + " ms");
                    return new ProductInfo(price, inventory, reviews);
                });
    }

    public static void main(String[] args) {
        ServiceAggregator aggregator = new ServiceAggregator();
        ProductInfo productInfo = aggregator.getProductInfo("prod-123").join();
        System.out.println(productInfo);
    }
}
