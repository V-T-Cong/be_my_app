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
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedRolesAndPermissions();
        seedCategories();
        seedProducts();
        seedAdminUser();
    }

    private void seedAdminUser() {
        String adminEmail = "admin@gmail.com";

        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }

        System.out.println("Seeding Admin User...");

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

        User admin = new User();
        admin.setUsername("Administrator");
        admin.setEmail(adminEmail);
        admin.setPhoneNumber("0000000000");
        admin.setPassword(passwordEncoder.encode("admin@1234"));
        admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));

        userRepository.save(admin);
        System.out.println("Admin User created successfully!");
    }

    private void seedCategories() {
        if (categoryRepository.count() > 0) {
            return;
        }

        System.out.println("Seeding Category Data...");

        List<Category> categories = List.of(
                Category.builder().name("Operating Systems").description("Windows, Linux, and macOS licenses").color("#4A90E2").build(),
                Category.builder().name("Streaming Services").description("Netflix, Hulu, Disney+, and more").color("#E50914").build(),
                Category.builder().name("Office Tools").description("Productivity software like Microsoft Office and Adobe Acrobat").color("#D32F2F").build(),
                Category.builder().name("Graphic Design").description("Tools for designers: Photoshop, Illustrator, Canva Pro").color("#9C27B0").build(),
                Category.builder().name("Gaming").description("Game keys, Steam credits, and digital deluxe editions").color("#FF9800").build()
        );

        categoryRepository.saveAll(categories);
    }

    private void seedRolesAndPermissions() {
        Permission readUser = createPermissionIfNotFound("READ_USER");
        Permission writeUser = createPermissionIfNotFound("WRITE_USER");
        Permission deleteUser = createPermissionIfNotFound("DELETE_USER");

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
            return;
        }

        System.out.println("Seeding Product Data...");

        // PRODUCT 1: Windows 11 Pro
        Product windows = Product.builder()
                .name("Windows 11 Pro")
                .slug("windows-11-pro")
                .description("Lifetime license for Windows 11 Professional. Instant delivery via email.")
                .imageUrls(List.of("https://example.com/images/win11-box.jpg", "https://example.com/images/win11-logo.jpg"))
                .isActive(true)
                .build();
        productRepository.save(windows);

        ProductVariant winKeyVariant = ProductVariant.builder()
                .product(windows)
                .type(ProductType.KEY)
                .variantName("Global Retail Key")
                .price(new BigDecimal("19.99"))
                .stockQuantity(3)
                .build();
        variantRepository.save(winKeyVariant);

        inventoryRepository.saveAll(List.of(
                createItem(winKeyVariant, "W11-AAAA-BBBB-CCCC-DDDD"),
                createItem(winKeyVariant, "W11-EEEE-FFFF-GGGG-HHHH"),
                createItem(winKeyVariant, "W11-IIII-JJJJ-KKKK-LLLL")
        ));

        // PRODUCT 2: Netflix Premium
        Product netflix = Product.builder()
                .name("Netflix Premium (1 Month)")
                .slug("netflix-premium-1-month")
                .description("4K Ultra HD streaming. Supports 4 devices.")
                .imageUrls(List.of("https://example.com/images/netflix-main.jpg", "https://example.com/images/netflix-ui.jpg"))
                .isActive(true)
                .build();
        productRepository.save(netflix);

        ProductVariant netflixShared = ProductVariant.builder()
                .product(netflix)
                .type(ProductType.SHARED_ACCOUNT)
                .variantName("Shared Account (Profile 1)")
                .price(new BigDecimal("2.50"))
                .stockQuantity(2)
                .build();
        variantRepository.save(netflixShared);

        inventoryRepository.saveAll(List.of(
                createItem(netflixShared, "{\"u\":\"shared1@mail.com\",\"p\":\"pass123\",\"profile\":\"1\"}"),
                createItem(netflixShared, "{\"u\":\"shared1@mail.com\",\"p\":\"pass123\",\"profile\":\"2\"}")
        ));

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