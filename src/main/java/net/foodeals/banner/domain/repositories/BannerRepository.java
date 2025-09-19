package net.foodeals.banner.domain.repositories;

import net.foodeals.banner.domain.entities.Banner;
import net.foodeals.common.contracts.BaseRepository;

import java.util.UUID;

public interface BannerRepository extends BaseRepository<Banner, UUID> {
}
