package net.foodeals.user.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.user.application.dtos.requests.InfosProfileRequest;
import net.foodeals.user.application.dtos.requests.UserRequest;
import net.foodeals.user.application.dtos.responses.FavorisOfferPartenerResponse;
import net.foodeals.user.application.dtos.responses.FavorisOfferResponse;
import net.foodeals.user.application.dtos.responses.InfosProfileResponse;
import net.foodeals.user.application.dtos.responses.UserStatisticsResponse;
import net.foodeals.user.domain.entities.User;

import java.util.List;

public interface UserService extends CrudService<User, Integer, UserRequest> {


    User findByEmail(String email);
    
    User changeAvatarPhoto(Integer id, String avatarPath);

    User getConnectedUser();

	void changePassword(Integer idUser, String password);
	
	User setPositionClient(Integer id ,Coordinates coordinates,int raduis);

    List<FavorisOfferResponse> getListFavorisOffers();

    public UserStatisticsResponse getStatistics();

    List<FavorisOfferPartenerResponse> getListFavorisOffersPartners();
    
    InfosProfileResponse getInfosProfile();
    
    InfosProfileResponse updateInfosProfile(InfosProfileRequest request);
}
