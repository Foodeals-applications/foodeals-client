package net.foodeals.dlc.application.services.impl;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.dlc.application.dtos.requests.ModificationRequest;
import net.foodeals.dlc.application.dtos.responses.DlcResponse;
import net.foodeals.dlc.application.dtos.responses.ModificationDetailsResponse;
import net.foodeals.dlc.application.services.DlcService;
import net.foodeals.dlc.application.services.ModificationService;
import net.foodeals.dlc.domain.entities.Dlc;
import net.foodeals.dlc.domain.entities.Modification;
import net.foodeals.dlc.domain.repositories.ModificationRepository;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;

@Service
@Transactional
@RequiredArgsConstructor
public class ModificationServiceImpl implements ModificationService {

    private final ModificationRepository modificationRepository;
    private final DlcService dlcService;
    private final UserService userService;
    private final ModelMapper mapper;

    @Override
    public List<Modification> findAll() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Page<Modification> findAll(Integer pageNumber, Integer pageSize) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Modification findById(UUID id) {
        return modificationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Modification not found with ID: " + id));
    }

    @Override
    public Modification create(ModificationRequest dto) {
        Dlc dlc = dlcService.findById(dto.dlcId());
        User user = userService.findById(dto.userId());
        
 
        Modification modification = Modification.create(dlc, user, dto.previousQuantity(), dto.modifiedQuantity(),
                dto.previousDiscount(), dto.modifiedDiscount());
                
        return modificationRepository.save(modification);
    }

    @Override
    public Modification update(UUID id, ModificationRequest dto) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void delete(UUID id) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public List<Modification> getModificationsByDlc(UUID dlcId) {
        return modificationRepository.findByDlcId(dlcId);
    }

    @Override
    public ModificationDetailsResponse getUserModificationDetails(UUID dlcId, Integer userId) {
        Dlc dlc = dlcService.findById(dlcId);

        List<Modification> modifications = modificationRepository.findLastModificationByDlcIdAndUserId(dlcId, userId);

        
        if (modifications.isEmpty()) {
            throw new IllegalArgumentException("No modification found for the given DlcId and UserId.");
        }

        Modification modification = modifications.get(0);

    
        return new ModificationDetailsResponse(
                mapper.map(dlc, DlcResponse.class),
                modification.getPreviousQuantity(),
                modification.getModifiedQuantity(),
                modification.getPreviousDiscount(),
                modification.getModifiedDiscount()
        );
    }
}


