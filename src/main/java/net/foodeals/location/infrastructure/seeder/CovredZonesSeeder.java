package net.foodeals.location.infrastructure.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import net.foodeals.delivery.domain.entities.CoveredZones;
import net.foodeals.delivery.domain.repositories.CoveredZonesRepository;
import net.foodeals.location.domain.entities.Region;
import net.foodeals.location.domain.repositories.RegionRepository;

@Component
@Transactional
@Order(4)
public class CovredZonesSeeder  implements CommandLineRunner {
	
	private final RegionRepository regionRepository;
	
	private final CoveredZonesRepository coveredZonesRepository;
	
	
	
	public CovredZonesSeeder(RegionRepository regionRepository, CoveredZonesRepository coveredZonesRepository) {
		super();
		this.regionRepository = regionRepository;
		this.coveredZonesRepository = coveredZonesRepository;
	}



	@Override
	public void run(String... args) throws Exception {
		
		Region region =regionRepository.findByName("maarif");
		CoveredZones coveredZones=new CoveredZones();
		coveredZones.setRegion(region);
		coveredZonesRepository.save(coveredZones);
	}

}
