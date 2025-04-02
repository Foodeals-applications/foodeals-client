package net.foodeals.user.application.services.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.user.application.dtos.requests.UserRequest;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.exceptions.UserNotFoundException;
import net.foodeals.user.domain.repositories.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

	private final UserRepository repository;

	@Value("${upload.directory}")
	private String uploadDir;
	
	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Page<User> findAll(Integer pageNumber, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public User findById(Integer id) {
		return repository.findById(id).orElseGet(null);
	}
	@Override
	public User create(UserRequest dto) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public User update(Integer id, UserRequest dto) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public User findByEmail(String email) {
		return repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
	}
	
	@Override
	public User changeAvatarPhoto(Integer id, String avatarPath) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public User getConnectedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String email = userDetails.getUsername();
			return repository.findByEmail(email).get();
		}

		return null;

	}
	@Override
	public void changePassword(Integer idUser, String password) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public User setPositionClient(Integer id ,Coordinates coordinates, int raduis) {
		User user =repository.findById(id).orElse(null);
		if(!Objects.isNull(user)) {
			user.setCoordinates(coordinates);
			user.setRaduis(raduis);
			return repository.save(user);
		}
		return null;
	}

	
	
}