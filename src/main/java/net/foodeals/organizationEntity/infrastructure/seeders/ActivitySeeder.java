package net.foodeals.organizationEntity.infrastructure.seeders;

import lombok.RequiredArgsConstructor;
import net.foodeals.common.annotations.Seeder;
import net.foodeals.core.repositories.ActivityRepository;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

@Seeder
@RequiredArgsConstructor
public class ActivitySeeder implements CommandLineRunner {

    private final ActivityRepository repository;

    @Override
    public void run(String... args) throws Exception {
       /* if (repository.count() == 0) {
            repository.saveAll(List.of(
                    Activity.create("SUPERMARCHE"),
                    Activity.create("SUPERETTES")
            ));
        }*/
    }
}
