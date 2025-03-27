package net.foodeals.user.application.dtos.responses;



import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.location.application.dtos.responses.AddressResponse;
import net.foodeals.organizationEntity.application.dtos.responses.SolutionResponse;
import net.foodeals.user.domain.enums.Civility;
import net.foodeals.user.domain.enums.Nationality;
import net.foodeals.user.domain.enums.UserStatus;
import net.foodeals.user.domain.valueObjects.Name;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String avatarPath;
    private Civility civility;
    private Name name;
    private Nationality nationality;
    private String cin;
    private RoleResponse role;
    private String email;
    private String phone;
    private String rayon ;
    private Integer managerId;
    private Name managerName;
    private List<SolutionResponse>solutions;
    private AddressResponse address;
    private List<WorkScheduleResponse>workSchedules;
    private UserStatus status;
    private Boolean deliveryBoyAvailable;
    private Integer distanceOfDeliveryBoy;
    private Coordinates coordinates;
    
}

