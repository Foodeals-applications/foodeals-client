package net.foodeals.user.application.services;

import java.util.UUID;

public interface LikeService {

    public void addLike(UUID subEntityId, Integer userId);


    public void removeLike(UUID subEntityId, Integer userId);


}
