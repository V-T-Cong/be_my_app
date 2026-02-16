package com.congvo.be_myapp.config;

import com.congvo.be_myapp.emuns.InventoryStatus;
import com.congvo.be_myapp.emuns.ProductType;
import com.congvo.be_myapp.entity.*;
import com.congvo.be_myapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final InventoryItemRepository inventoryRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            ProductRepository productRepository,
            ProductVariantRepository variantRepository,
            InventoryItemRepository inventoryRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.inventoryRepository = inventoryRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository; //
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedRolesAndPermissions();
        seedCategories();
        seedProducts();
        seedAdminUser();
        seedProducts();
    }

    private void seedAdminUser() {
        String adminEmail = "admin@gmail.com"; //

        if (userRepository.existsByEmail(adminEmail)) { //
            return;
        }

        System.out.println("Seeding Admin User..."); //

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found")); //

        User admin = new User(); //
        admin.setUsername("Administrator"); //
        admin.setEmail(adminEmail); //
        admin.setPhoneNumber("0000000000"); //
        admin.setPassword(passwordEncoder.encode("admin@1234")); //
        admin.setRoles(new HashSet<>(Collections.singletonList(adminRole))); //

        userRepository.save(admin); //
        System.out.println("Admin User created successfully!");
    }

    private void seedCategories() {
        if (categoryRepository.count() > 0) {
            return; //
        }

        System.out.println("Seeding Category Data..."); //

        List<Category> categories = List.of(
                Category.builder().name("Operating Systems").description("Windows, Linux, and macOS licenses").color("#4A90E2").build(),
                Category.builder().name("Streaming Services").description("Netflix, Hulu, Disney+, and more").color("#E50914").build(),
                Category.builder().name("Office Tools").description("Productivity software like Microsoft Office and Adobe Acrobat").color("#D32F2F").build(),
                Category.builder().name("Graphic Design").description("Tools for designers: Photoshop, Illustrator, Canva Pro").color("#9C27B0").build(),
                Category.builder().name("Video Editing").description("Premiere Pro, Final Cut, and DaVinci Resolve").color("#673AB7").build(),
                Category.builder().name("Gaming").description("Game keys, Steam credits, and digital deluxe editions").color("#FF9800").build(),
                Category.builder().name("Music & Audio").description("Spotify, Apple Music, and DAW software").color("#1DB954").build(),
                Category.builder().name("Antivirus & Security").description("Kaspersky, Norton, and McAfee licenses").color("#F44336").build(),
                Category.builder().name("Cloud Storage").description("Google One, Dropbox, and iCloud storage plans").color("#2196F3").build(),
                Category.builder().name("VPN Services").description("NordVPN, ExpressVPN, and Surfshark").color("#3F51B5").build(),
                Category.builder().name("Educational Software").description("Language learning, coding bootcamps, and academic tools").color("#4CAF50").build(),
                Category.builder().name("Development Tools").description("IDE licenses, GitHub Pro, and hosting credits").color("#333333").build(),
                Category.builder().name("SEO & Marketing").description("Semrush, Ahrefs, and social media management tools").color("#FF5722").build(),
                Category.builder().name("E-books & Reading").description("Kindle Unlimited, Scribd, and digital libraries").color("#795548").build(),
                Category.builder().name("AI & Automation").description("ChatGPT Plus, Midjourney, and Zapier").color("#00BCD4").build(),
                Category.builder().name("Project Management").description("Trello Gold, Asana, and Monday.com").color("#FFC107").build(),
                Category.builder().name("Finance & Accounting").description("QuickBooks, Xero, and personal finance apps").color("#009688").build(),
                Category.builder().name("Stock Media").description("Shutterstock, Envato Elements, and Freepik Premium").color("#8BC34A").build(),
                Category.builder().name("Communication").description("Zoom Pro, Slack, and Discord Nitro").color("#7289DA").build(),
                Category.builder().name("Utilities").description("System optimizers, file recovery, and compression tools").color("#607D8B").build(),
                Category.builder().name("Mobile Apps").description("Premium Android and iOS applications").color("#A4C639").build(),
                Category.builder().name("E-commerce").description("Shopify themes, plugins, and dropshipping tools").color("#95BF47").build(),
                Category.builder().name("Web Hosting").description("Domain registration and server management").color("#FF4081").build(),
                Category.builder().name("Cybersecurity Training").description("Courses and labs for ethical hacking").color("#263238").build(),
                Category.builder().name("Fitness & Wellness").description("Workout apps and meditation subscriptions").color("#FF1744").build()
        );

        categoryRepository.saveAll(categories); //
    }

    private void seedRolesAndPermissions() {
        // 1. Create Permissions
        Permission readUser = createPermissionIfNotFound("READ_USER");
        Permission writeUser = createPermissionIfNotFound("WRITE_USER");
        Permission deleteUser = createPermissionIfNotFound("DELETE_USER");

        // 2. Create Roles
        Set<Permission> userPermissions = new HashSet<>();
        userPermissions.add(readUser);
        createRoleIfNotFound("ROLE_USER", userPermissions);

        Set<Permission> adminPermissions = new HashSet<>();
        adminPermissions.add(readUser);
        adminPermissions.add(writeUser);
        adminPermissions.add(deleteUser);
        createRoleIfNotFound("ROLE_ADMIN", adminPermissions);
    }

    private void seedProducts() {
        if (productRepository.count() > 0) {
            return; // Data already exists
        }

        System.out.println("Seeding Product Data...");

        // ==========================================
        // PRODUCT 1: Windows 11 Pro
        // ==========================================
        Product windows = Product.builder()
                .name("Windows 11 Pro")
                .slug("windows-11-pro")
                .description("Lifetime license for Windows 11 Professional. Instant delivery via email.")
                .thumbnailUrl("https://example.com/images/win11.jpg")
                .isActive(true)
                .build();
        productRepository.save(windows);

        // Variant: Retail Key
        ProductVariant winKeyVariant = ProductVariant.builder()
                .product(windows)
                .type(ProductType.KEY)
                .variantName("Global Retail Key")
                .price(new BigDecimal("19.99"))
                .stockQuantity(3)
                .build();
        variantRepository.save(winKeyVariant);

        // Add Inventory for Windows
        inventoryRepository.saveAll(List.of(
                createItem(winKeyVariant, "W11-AAAA-BBBB-CCCC-DDDD"),
                createItem(winKeyVariant, "W11-EEEE-FFFF-GGGG-HHHH"),
                createItem(winKeyVariant, "W11-IIII-JJJJ-KKKK-LLLL")
        ));

        // ==========================================
        // PRODUCT 2: Netflix Premium
        // ==========================================
        Product netflix = Product.builder()
                .name("Netflix Premium (1 Month)")
                .slug("netflix-premium-1-month")
                .description("4K Ultra HD streaming. Supports 4 devices.")
                .thumbnailUrl("https://example.com/images/netflix.jpg")
                .isActive(true)
                .build();
        productRepository.save(netflix);

        // Variant A: Shared Account (Cheaper)
        ProductVariant netflixShared = ProductVariant.builder()
                .product(netflix)
                .type(ProductType.SHARED_ACCOUNT)
                .variantName("Shared Account (Profile 1)")
                .price(new BigDecimal("2.50"))
                .stockQuantity(2)
                .build();
        variantRepository.save(netflixShared);

        // Inventory for Shared Account (JSON format for user/pass)
        inventoryRepository.saveAll(List.of(
                createItem(netflixShared, "{\"u\":\"shared1@mail.com\",\"p\":\"pass123\",\"profile\":\"1\"}"),
                createItem(netflixShared, "{\"u\":\"shared1@mail.com\",\"p\":\"pass123\",\"profile\":\"2\"}")
        ));

        // Variant B: Private Account (Expensive)
        ProductVariant netflixPrivate = ProductVariant.builder()
                .product(netflix)
                .type(ProductType.PRIVATE_ACCOUNT)
                .variantName("Private Account (Full Access)")
                .price(new BigDecimal("12.00"))
                .stockQuantity(1)
                .build();
        variantRepository.save(netflixPrivate);

        // Inventory for Private Account
        inventoryRepository.save(
                createItem(netflixPrivate, "{\"u\":\"private@mail.com\",\"p\":\"secretPass!\"}")
        );

        System.out.println("Product Data Seeded Successfully!");
    }

    private InventoryItem createItem(ProductVariant variant, String secret) {
        return InventoryItem.builder()
                .variant(variant)
                .secretValue(secret)
                .status(InventoryStatus.AVAILABLE)
                .build();
    }

    private Permission createPermissionIfNotFound(String name) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> {
                    Permission permission = new Permission();
                    permission.setName(name);
                    return permissionRepository.save(permission);
                });
    }

    private Role createRoleIfNotFound(String name, Set<Permission> permissions) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);
                    role.setPermissions(permissions);
                    return roleRepository.save(role);
                });
    }
}