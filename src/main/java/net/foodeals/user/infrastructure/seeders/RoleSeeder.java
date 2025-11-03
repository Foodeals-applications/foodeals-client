package net.foodeals.user.infrastructure.seeders;

import lombok.RequiredArgsConstructor;
import net.foodeals.common.annotations.Seeder;
import net.foodeals.core.domain.entities.Role;
import net.foodeals.core.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.UUID;

@Seeder
@Order(2)
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {
	private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(
                    List.of(
                            Role.create(UUID.fromString("d7d7a9c5-b153-4526-ac16-05f19bf97270"), "ADMIN"),
                            Role.create(UUID.randomUUID(), "SUPER_ADMIN"),
                            Role.create(UUID.randomUUID(), "MANAGER"),
                            Role.create(UUID.randomUUID(), "MANAGER_REGIONALE"),
                            Role.create(UUID.randomUUID(), "MARCHANDISER"),
                            Role.create(UUID.randomUUID(), "SALES_MANAGER"),
                            Role.create(UUID.randomUUID(), "CLIENT"),
                            Role.create(UUID.randomUUID(), "DELIVERY_MAN")
                    )
            );
            System.out.println("roles seeded");
        }
    }
}
