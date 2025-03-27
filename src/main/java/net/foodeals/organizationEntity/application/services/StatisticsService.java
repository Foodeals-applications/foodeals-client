package net.foodeals.organizationEntity.application.services;

import java.time.LocalDate;

import net.foodeals.organizationEntity.application.dtos.responses.StatisticsResponse;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;


public interface StatisticsService {

	StatisticsResponse getStatistics(OrganizationEntity organizationEntity, LocalDate startDate, LocalDate endDate) ;
}
