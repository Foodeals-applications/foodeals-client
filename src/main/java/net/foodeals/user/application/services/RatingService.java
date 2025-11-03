package net.foodeals.user.application.services;

import lombok.RequiredArgsConstructor;

import net.foodeals.core.domain.entities.Rating;
import net.foodeals.core.domain.entities.User;
import net.foodeals.core.repositories.RatingRepository;
import net.foodeals.user.application.dtos.requests.RatingUpdateRequest;
import net.foodeals.user.application.dtos.responses.RatingResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;


    // Service methods
    public List<RatingResponse> getUserRatings(User user) {
        return ratingRepository.findByUser(user).stream()
                .map(RatingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public RatingResponse updateRating(UUID id, RatingUpdateRequest request, User user) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found"));

        if (!rating.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this rating");
        }

        rating.setRating(request.getRating());
        rating.setComment(request.getComment());
        Rating saved = ratingRepository.save(rating);

        return RatingResponse.fromEntity(saved);
    }
}
