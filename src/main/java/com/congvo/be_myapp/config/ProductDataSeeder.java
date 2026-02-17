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
        // Prevent duplicate seeding by checking for a specific new product
        if (productRepository.existsBySlug("elden-ring-shadow-erdtree")) {
            return;
        }

        seedGames();
        seedStreamingAndApps();
        seedSoftwareAndVPNs();

        System.out.println("Seeded 20+ additional products successfully!");
    }

    private void seedGames() {
        // 1. Elden Ring
        addProduct("Elden Ring", "elden-ring-shadow-erdtree", "Action RPG developed by FromSoftware.",
                "https://example.com/elden.jpg", ProductType.KEY, "Global Steam Key", "49.99", "ER-XXXX-YYYY-ZZZZ");

        // 2. Cyberpunk 2077
        addProduct("Cyberpunk 2077", "cyberpunk-2077-ultimate", "Open-world action-adventure RPG.",
                "https://example.com/cp77.jpg", ProductType.KEY, "GOG Global Key", "29.99", "CP-AAAA-BBBB-CCCC");

        // 3. Minecraft
        addProduct("Minecraft: Java & Bedrock", "minecraft-pc-bundle", "Build and explore your own world.",
                "https://example.com/mc.jpg", ProductType.KEY, "Microsoft Store Key", "19.50", "MC-1111-2222-3333");

        // 4. Red Dead Redemption 2
        addProduct("Red Dead Redemption 2", "rdr2-standard", "Epic tale of life in America’s unforgiving heartland.",
                "https://example.com/rdr2.jpg", ProductType.KEY, "Rockstar Social Club Key", "24.99", "RDR-9999-8888-7777");

        // 5. Grand Theft Auto V
        addProduct("GTA V: Premium Edition", "gta-v-premium", "Includes the Criminal Enterprise Starter Pack.",
                "https://example.com/gtav.jpg", ProductType.KEY, "Epic Games Key", "14.99", "GTA-5555-4444-3333");

        // 6. Steam Gift Card
        addProduct("Steam Gift Card $50", "steam-gift-card-50", "Digital code to add funds to your Steam Wallet.",
                "https://example.com/steam50.jpg", ProductType.KEY, "Digital Code", "50.00", "STM-WAL-LET-CODE");

        // 7. PlayStation Plus
        addProduct("PlayStation Plus Essential (1 Month)", "ps-plus-essential-1m", "Online multiplayer and monthly games.",
                "https://example.com/psplus.jpg", ProductType.KEY, "USA Region Code", "9.99", "PSN-CODE-SAMPLE");
    }

    private void seedStreamingAndApps() {
        // 8. YouTube Premium
        addProduct("YouTube Premium (6 Months)", "youtube-premium-6m", "Ad-free YouTube and Music.",
                "https://example.com/yt.jpg", ProductType.SHARED_ACCOUNT, "Family Slot", "12.00", "{\"u\":\"yt_user@mail.com\",\"p\":\"pass123\"}");

        // 9. Disney+
        addProduct("Disney+ Premium", "disney-premium", "Disney, Pixar, Marvel, Star Wars, and Nat Geo.",
                "https://example.com/disney.jpg", ProductType.SHARED_ACCOUNT, "Profile Access", "3.50", "{\"u\":\"disney@mail.com\",\"p\":\"magic123\"}");

        // 10. Canva Pro
        addProduct("Canva Pro (1 Year)", "canva-pro-1y", "Professional design tools and templates.",
                "https://example.com/canva.jpg", ProductType.SHARED_ACCOUNT, "Team Invite Link", "15.00", "https://canva.com/join/sample-link");

        // 11. Midjourney
        addProduct("Midjourney Basic Plan", "midjourney-basic", "AI image generation via Discord.",
                "https://example.com/mj.jpg", ProductType.PRIVATE_ACCOUNT, "Full Month Access", "10.00", "{\"u\":\"ai_artist@discord.com\",\"p\":\"mjpass\"}");

        // 12. Apple Music
        addProduct("Apple Music (3 Months)", "apple-music-3m", "Stream 100 million songs ad-free.",
                "https://example.com/apple.jpg", ProductType.KEY, "Redeem Code", "5.00", "APL-MSC-CODE-001");
    }

    private void seedSoftwareAndVPNs() {
        // 13. NordVPN
        addProduct("NordVPN (2 Years)", "nordvpn-2y", "Top-rated VPN for security and privacy.",
                "https://example.com/nord.jpg", ProductType.PRIVATE_ACCOUNT, "Personal Account", "45.00", "{\"u\":\"nord_user@mail.com\",\"p\":\"secure456\"}");

        // 14. ExpressVPN
        addProduct("ExpressVPN (1 Month)", "expressvpn-1m", "High-speed, secure, and anonymous VPN.",
                "https://example.com/express.jpg", ProductType.SHARED_ACCOUNT, "Shared Device Access", "4.00", "{\"u\":\"exp_vpn@mail.com\",\"p\":\"pass888\"}");

        // 15. Adobe Photoshop
        addProduct("Adobe Photoshop 2024", "adobe-photoshop-2024", "Create and enhance your photos.",
                "https://example.com/ps.jpg", ProductType.PRIVATE_ACCOUNT, "1 Month Subscription", "20.00", "{\"u\":\"photoshop@adobe.com\",\"p\":\"ps2024\"}");

        // 16. Malwarebytes Premium
        addProduct("Malwarebytes Premium", "malwarebytes-premium", "Real-time protection against malware.",
                "https://example.com/mb.jpg", ProductType.KEY, "1 Year / 1 Device", "12.50", "MB-KEYS-1234-5678");

        // 17. JetBrains All Products Pack
        addProduct("JetBrains All Products Pack", "jetbrains-all-pack", "Access to IntelliJ, PyCharm, WebStorm, etc.",
                "https://example.com/jb.jpg", ProductType.PRIVATE_ACCOUNT, "Student License", "25.00", "{\"u\":\"student@edu.com\",\"p\":\"jb-edu-pass\"}");

        // 18. GitHub Pro
        addProduct("GitHub Pro (1 Year)", "github-pro-1y", "Advanced features for developers.",
                "https://example.com/gh.jpg", ProductType.PRIVATE_ACCOUNT, "Account Upgrade", "30.00", "{\"u\":\"coder@mail.com\",\"p\":\"gh-pass\"}");

        // 19. ChatGPT Plus
        addProduct("ChatGPT Plus (Shared)", "chatgpt-plus-shared", "Access to GPT-4 and DALL-E.",
                "https://example.com/gpt.jpg", ProductType.SHARED_ACCOUNT, "Profile 2", "6.00", "{\"u\":\"gpt_user@mail.com\",\"p\":\"ai_is_cool\"}");

        // 20. Windows 10 Home
        addProduct("Windows 10 Home", "win-10-home", "Digital license for Windows 10 Home Edition.",
                "https://example.com/win10.jpg", ProductType.KEY, "Retail Key", "12.00", "W10-XXXX-YYYY-ZZZZ");
    }

    private void addProduct(String name, String slug, String desc, String thumb, ProductType type, String variantName, String price, String secret) {
        Product product = Product.builder()
                .name(name)
                .slug(slug)
                .description(desc)
                .thumbnailUrl(thumb)
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
