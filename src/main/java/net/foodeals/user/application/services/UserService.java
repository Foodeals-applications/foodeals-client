package net.foodeals.user.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.user.application.dtos.requests.UserRequest;
import net.foodeals.user.domain.entities.User;

public interface UserService extends CrudService<User, Integer, UserRequest> {


    User findByEmail(String email);
    
    User changeAvatarPhoto(Integer id, String avatarPath);

    User getConnectedUser();

	void changePassword(Integer idUser, String password);
	
	User setPositionClient(Integer id ,Coordinates coordinates,int raduis);
}
