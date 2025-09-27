package net.foodeals.referals.domain.repositories;

import net.foodeals.referals.domain.entities.Referral;
import net.foodeals.user.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReferralRepository extends JpaRepository<Referral, UUID> {
    List<Referral> findBySender(User sender);
}