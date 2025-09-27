package net.foodeals.support.domain.repositories;

import net.foodeals.support.domain.entities.SupportTicket;
import net.foodeals.user.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, UUID> {
    List<SupportTicket> findByUser(User user);
}