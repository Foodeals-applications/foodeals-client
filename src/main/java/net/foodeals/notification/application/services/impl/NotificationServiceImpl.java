package net.foodeals.notification.application.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.notification.application.dtos.requests.NotificationRequest;
import net.foodeals.notification.application.dtos.responses.NotificationResponse;
import net.foodeals.notification.application.services.NotificationService;
import net.foodeals.notification.domain.entity.Notification;
import net.foodeals.notification.domain.enums.NotificationStatus;
import net.foodeals.notification.domain.enums.TypeRequest;
import net.foodeals.notification.domain.repositories.NotificationRepository;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.organizationEntity.domain.repositories.OrganizationEntityRepository;
import net.foodeals.organizationEntity.domain.repositories.SubEntityRepository;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.repositories.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository repository;
	private final OrganizationEntityRepository organizationEntityRepository;
	private final UserRepository userRepository;
	private final UserService userService;
	private final SubEntityRepository subEntityRepository;

	@Value("${upload.directory}")
	private String uploadDir;

	private final ModelMapper mapper;

	@Override
	public List<Notification> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Notification> findAll(Integer pageNumber, Integer pageSize) {
		return repository.findAll(PageRequest.of(pageNumber, pageSize));
	}

	@Override
	public Notification findById(UUID id) {
		Notification notification = repository.findById(id).get();
		if (notification.getStatus().equals(NotificationStatus.UNREAD)) {
			notification.setStatus(NotificationStatus.READ);
			notification = repository.save(notification);
		}
		return notification;
	}

	@Override
	public Notification create(NotificationRequest dto) {
		return null;
	}

	@Override
	public Notification create(NotificationRequest dto, String filePath) {
		Notification notification = new Notification();
		notification.setContent(dto.content());
		notification.setTitle(dto.title());
		notification.setAttachmentPath(filePath);
		User sender = userService.getConnectedUser();
		OrganizationEntity organizationEntity = sender.getOrganizationEntity();

		SubEntity subEntity = subEntityRepository.findById(dto.subEntityId())
				.orElseThrow(() -> new EntityNotFoundException("SubEntity not found with id: " + dto.subEntityId()));

		notification.setSubEntity(subEntity);
		notification.setUser(sender);
		notification.setOrganizationEntity(organizationEntity);

		notification.setStatus(NotificationStatus.UNREAD);
        notification.setTypeRequest(dto.typeRequest());
		return repository.save(notification);
	}

	@Override
	public Notification update(UUID id, NotificationRequest dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(UUID id) {
		// TODO Auto-generated method stub

	}

	@Override
	public String saveFile(MultipartFile file) {

		Path path = Paths.get(uploadDir, file.getOriginalFilename());
		try {
			Files.createDirectories(path.getParent());
			Files.write(path, file.getBytes());
		} catch (IOException e) {
			throw new RuntimeException("Problem while saving the file.", e);
		}

		return path.toString();
	}

	@Override
	public Page<Notification> findAllByStatus(NotificationStatus status, PageRequest pageRequest) {
		return repository.findAllByStatus(status, pageRequest);
	}

}
