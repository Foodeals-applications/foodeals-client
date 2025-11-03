package net.foodeals.location.infrastructure.seeder;

import net.foodeals.core.domain.entities.CoveredZones;
import net.foodeals.core.domain.entities.Region;
import net.foodeals.core.repositories.CoveredZonesRepository;
import net.foodeals.core.repositories.RegionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;


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
