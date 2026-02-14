package com.congvo.be_myapp.config;

import com.congvo.be_myapp.emuns.InventoryStatus;
import com.congvo.be_myapp.emuns.ProductType;
import com.congvo.be_myapp.entity.*;
import com.congvo.be_myapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    public DataSeeder(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            ProductRepository productRepository,
            ProductVariantRepository variantRepository,
            InventoryItemRepository inventoryRepository
    ) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedRolesAndPermissions();
        seedProducts();
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