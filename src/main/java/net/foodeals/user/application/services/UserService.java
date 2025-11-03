package net.foodeals.user.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.core.domain.entities.Coordinates;
import net.foodeals.core.domain.entities.User;
import net.foodeals.user.application.dtos.requests.InfosProfileRequest;
import net.foodeals.user.application.dtos.requests.UserRequest;
import net.foodeals.user.application.dtos.responses.FavorisOfferPartenerResponse;
import net.foodeals.user.application.dtos.responses.FavorisOfferResponse;
import net.foodeals.user.application.dtos.responses.InfosProfileResponse;
import net.foodeals.user.application.dtos.responses.UserStatisticsResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService extends CrudService<User, Integer, UserRequest> {

    String saveFile(MultipartFile file);

    User findByEmail(String email);

   String uploadAvatar(Integer userId, MultipartFile file) throws IOException;

    User getConnectedUser();

	void changePassword(Integer idUser, String password);
	
	User setPositionClient(Integer id , Coordinates coordinates, int raduis);

    List<FavorisOfferResponse> getListFavorisOffers();

    public UserStatisticsResponse getStatistics();

    List<FavorisOfferPartenerResponse> getListFavorisOffersPartners();
    
    InfosProfileResponse getInfosProfile();
    
    InfosProfileResponse updateInfosProfile(InfosProfileRequest request);
}
