package com.congvo.be_myapp.config;

import com.congvo.be_myapp.emuns.InventoryStatus;
import com.congvo.be_myapp.emuns.ProductType;
import com.congvo.be_myapp.entity.InventoryItem;
import com.congvo.be_myapp.entity.Product;
import com.congvo.be_myapp.entity.ProductVariant;
import com.congvo.be_myapp.repository.InventoryItemRepository;
import com.congvo.be_myapp.repository.ProductRepository;
import com.congvo.be_myapp.repository.ProductVariantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@Order(2)
public class ProductDataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final InventoryItemRepository inventoryRepository;

    public ProductDataSeeder(
            ProductRepository productRepository,
            ProductVariantRepository variantRepository,
            InventoryItemRepository inventoryRepository
    ) {
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (productRepository.existsBySlug("elden-ring-shadow-erdtree")) {
            return;
        }

        seedGames();
        seedStreamingAndApps();
        seedSoftwareAndVPNs();

        System.out.println("Seeded additional products with multiple images successfully!");
    }

    private void seedGames() {
        // Updated to pass a list of images
        addProduct("Elden Ring", "elden-ring-shadow-erdtree", "Action RPG developed by FromSoftware.",
                List.of("https://example.com/elden1.jpg", "https://example.com/elden2.jpg"),
                ProductType.KEY, "Global Steam Key", "49.99", "ER-XXXX-YYYY-ZZZZ");

        addProduct("Cyberpunk 2077", "cyberpunk-2077-ultimate", "Open-world action-adventure RPG.",
                List.of("https://example.com/cp77_main.jpg", "https://example.com/cp77_alt.jpg"),
                ProductType.KEY, "GOG Global Key", "29.99", "CP-AAAA-BBBB-CCCC");

        addProduct("Minecraft: Java & Bedrock", "minecraft-pc-bundle", "Build and explore your own world.",
                List.of("https://example.com/mc_cover.jpg"),
                ProductType.KEY, "Microsoft Store Key", "19.50", "MC-1111-2222-3333");
    }

    private void seedStreamingAndApps() {
        addProduct("YouTube Premium (6 Months)", "youtube-premium-6m", "Ad-free YouTube and Music.",
                List.of("https://example.com/yt_premium.jpg"),
                ProductType.SHARED_ACCOUNT, "Family Slot", "12.00", "{\"u\":\"yt_user@mail.com\",\"p\":\"pass123\"}");

        addProduct("Disney+ Premium", "disney-premium", "Disney, Pixar, Marvel, Star Wars, and Nat Geo.",
                List.of("https://example.com/disney1.jpg", "https://example.com/disney_logo.jpg"),
                ProductType.SHARED_ACCOUNT, "Profile Access", "3.50", "{\"u\":\"disney@mail.com\",\"p\":\"magic123\"}");
    }

    private void seedSoftwareAndVPNs() {
        addProduct("NordVPN (2 Years)", "nordvpn-2y", "Top-rated VPN for security and privacy.",
                List.of("https://example.com/nord_banner.jpg"),
                ProductType.PRIVATE_ACCOUNT, "Personal Account", "45.00", "{\"u\":\"nord_user@mail.com\",\"p\":\"secure456\"}");

        addProduct("ChatGPT Plus (Shared)", "chatgpt-plus-shared", "Access to GPT-4 and DALL-E.",
                List.of("https://example.com/gpt_main.jpg", "https://example.com/gpt_ui.jpg"),
                ProductType.SHARED_ACCOUNT, "Profile 2", "6.00", "{\"u\":\"gpt_user@mail.com\",\"p\":\"ai_is_cool\"}");
    }

    private void addProduct(String name, String slug, String desc, List<String> images, ProductType type, String variantName, String price, String secret) {
        Product product = Product.builder()
                .name(name)
                .slug(slug)
                .description(desc)
                .imageUrls(images) // Updated to use imageUrls list
                .isActive(true)
                .build();
        productRepository.save(product);

        ProductVariant variant = ProductVariant.builder()
                .product(product)
                .type(type)
                .variantName(variantName)
                .price(new BigDecimal(price))
                .stockQuantity(1)
                .build();
        variantRepository.save(variant);

        inventoryRepository.save(InventoryItem.builder()
                .variant(variant)
                .secretValue(secret)
                .status(InventoryStatus.AVAILABLE)
                .build());
    }
}