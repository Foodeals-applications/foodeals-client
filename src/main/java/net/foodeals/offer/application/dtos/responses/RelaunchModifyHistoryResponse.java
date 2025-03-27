package net.foodeals.offer.application.dtos.responses;

import java.time.LocalDateTime;

public class RelaunchModifyHistoryResponse {

	private String organizationNameRelaunch;
	private LocalDateTime relaunchDate;

	private String organizationNameModifiy;
	private LocalDateTime modifiyDate;

	public RelaunchModifyHistoryResponse(String organizationNameRelaunch, LocalDateTime relaunchDate,
    		String organizationNameModifiy,
     LocalDateTime modifiyDate	) {
        this.organizationNameRelaunch=organizationNameRelaunch;
        this.relaunchDate = relaunchDate;
        this.organizationNameModifiy=organizationNameModifiy;
        this.modifiyDate=modifiyDate;
       
    }

	public String getOrganizationNameRelaunch() {
		return organizationNameRelaunch;
	}

	public void setOrganizationNameRelaunch(String organizationNameRelaunch) {
		this.organizationNameRelaunch = organizationNameRelaunch;
	}

	public LocalDateTime getRelaunchDate() {
		return relaunchDate;
	}

	public void setRelaunchDate(LocalDateTime relaunchDate) {
		this.relaunchDate = relaunchDate;
	}

	public String getOrganizationNameModifiy() {
		return organizationNameModifiy;
	}

	public void setOrganizationNameModifiy(String organizationNameModifiy) {
		this.organizationNameModifiy = organizationNameModifiy;
	}

	public LocalDateTime getModifiyDate() {
		return modifiyDate;
	}

	public void setModifiyDate(LocalDateTime modifiyDate) {
		this.modifiyDate = modifiyDate;
	}

	

}