package net.foodeals.notification.application.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.notification.application.dtos.requests.NotificationRequest;
import net.foodeals.notification.domain.entity.Notification;
import net.foodeals.notification.domain.enums.NotificationStatus;

public interface NotificationService extends CrudService<Notification, UUID, NotificationRequest>{

	public String saveFile(MultipartFile file) ;
	public Notification create(NotificationRequest dto,String filePath);
	public Page<Notification> findAllByStatus(NotificationStatus status, PageRequest pageRequest);
}
