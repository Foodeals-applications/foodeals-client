package net.foodeals.offer.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.foodeals.offer.domain.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.offer.domain.entities.Box;
import net.foodeals.offer.domain.enums.BoxStatus;
import net.foodeals.offer.domain.enums.BoxType;
import org.springframework.data.repository.query.Param;

public interface BoxRepository extends BaseRepository<Box, UUID> {

    List<Box> findByType(BoxType type);

    @Query("SELECT b FROM Box b WHERE b.offer.id = :offerId")
    public Box getBoxByOfferId(UUID offerId);


    @Query("""
                SELECT b FROM Box b
                JOIN b.offer o
                JOIN o.openTime ot
                WHERE b.boxStatus = :unavailableStatus
                OR (b.boxStatus = :expiredStatus AND ot.to < :today AND b.type = :type)
            """)
    Page<Box> findExpiredAndUnavailableBoxs(BoxStatus unavailableStatus, BoxStatus expiredStatus,
                                            BoxType type,
                                            String today,
                                            Pageable pageable);


    Optional<Box> findByIdAndType(UUID id, BoxType type);


    @Query("""
        SELECT DISTINCT b
        FROM Box b
        JOIN b.products p
        WHERE p IN (
            SELECT p2 FROM Box b2 JOIN b2.products p2 WHERE b2.id = :boxId
        )
        AND b.id <> :boxId
    """)
    List<Box> findSimilarBoxes(@Param("boxId") UUID boxId);
    
    @Query("SELECT DISTINCT b FROM Box b JOIN b.products p WHERE p.id IN :productIds AND b.id <> :boxId")
    List<Box> findSimilarBoxesByProductIds(List<UUID> productIds, UUID boxId);


    Page<Box> findByCategory(Category category, Pageable pageable);


    List<Box> findByIsFeaturedTrueAndIsActiveTrue();
}


