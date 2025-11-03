package net.foodeals.organizationEntity.application.services;

import java.time.LocalDate;

import net.foodeals.core.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.application.dtos.responses.StatisticsResponse;


public interface StatisticsService {

	StatisticsResponse getStatistics(OrganizationEntity organizationEntity, LocalDate startDate, LocalDate endDate) ;
}
