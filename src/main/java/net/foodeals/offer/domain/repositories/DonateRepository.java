package net.foodeals.offer.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.offer.domain.entities.Donate;
import net.foodeals.user.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface DonateRepository extends BaseRepository<Donate, UUID> {
    List<Donate> findByUserDonor(User user);
}
