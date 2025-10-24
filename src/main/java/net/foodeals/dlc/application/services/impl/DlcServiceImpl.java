package net.foodeals.dlc.application.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.dlc.application.dtos.requests.CreateDlcRequest;
import net.foodeals.dlc.application.dtos.requests.CreateUserProductRequest;
import net.foodeals.dlc.application.dtos.requests.ScanActions;
import net.foodeals.dlc.application.dtos.requests.UpdateDlcRequest;
import net.foodeals.dlc.application.dtos.responses.*;
import net.foodeals.dlc.application.services.DlcService;
import net.foodeals.dlc.domain.entities.Dlc;
import net.foodeals.dlc.domain.enums.ValorisationType;
import net.foodeals.dlc.domain.repositories.DlcRepository;
import net.foodeals.dlc.infrastructure.interfaces.web.mappers.DlcMapper;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DlcServiceImpl implements DlcService {

    private final DlcRepository dlcRepository;
    private final ProductRepository productRepository;

    @Override
    public List<DlcDto> getAllDlcs() {
        return dlcRepository.findAll().stream().map(DlcMapper::toDto).toList();
    }

    @Override
    public DlcDto getDlcById(UUID id) {
        Dlc dlc = dlcRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("DLC not found"));
        return DlcMapper.toDto(dlc);
    }

    @Override
    public DlcDto createDlc(CreateDlcRequest request) {
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Dlc dlc = new Dlc(product, request.getExpiryDate(), request.getQuantity());
        dlc.calculateValType();

        dlcRepository.save(dlc);
        return DlcMapper.toDto(dlc);
    }

    @Override
    public DlcDto updateDlc(UUID id, UpdateDlcRequest request) {
        Dlc dlc = dlcRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("DLC not found"));

        dlc.setQuantity(request.getQuantity());
        dlc.setExpiryDate(request.getExpiryDate());
        dlc.calculateValType();

        dlcRepository.save(dlc);
        return DlcMapper.toDto(dlc);
    }

    @Override
    public void deleteDlc(UUID id) {
        dlcRepository.deleteById(id);
    }

    @Override
    public PaginatedUserProducts listUserProducts(int page, int limit) {
        List<UserProductResponse> all = dlcRepository.findAll().stream().map(DlcMapper::toUserProductResponse).collect(Collectors.toList());
        int from = Math.max(0, (page - 1) * limit);
        int to = Math.min(all.size(), from + limit);
        return new PaginatedUserProducts(all.subList(from, to), page, limit, all.size());
    }

    @Override
    public UserProductResponse getById(UUID id) {
        Dlc dlc = dlcRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("DLC not found"));
        return DlcMapper.toUserProductResponse(dlc);
    }

    @Override
    public UserProductResponse createFromRequest(CreateUserProductRequest req) {
        Product product = null;
        if (req.getProductId() != null) {
            product = productRepository.findById(req.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        }
        Dlc dlc = new Dlc();
        dlc.setProduct(product);
        dlc.setExpiryDate(req.getExpiresAt());
        dlc.setQuantity(req.getQuantity());
        dlc.setTimeRemaining(dlc.calculateTimeRemaining());
        dlc.calculateValType();

        dlcRepository.save(dlc);
        return DlcMapper.toUserProductResponse(dlc);
    }


    public UserProductResponse update(UUID id, CreateUserProductRequest req) {
        Dlc dlc = dlcRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("DLC not found"));
        ValorisationType oldType = dlc.getValorisationType();


        if (req.getExpiresAt() != null) dlc.setExpiryDate(req.getExpiresAt());
        if (req.getQuantity() != null) dlc.setQuantity(req.getQuantity());
        dlc.setTimeRemaining(dlc.calculateTimeRemaining());
        dlc.calculateValType();


        dlcRepository.save(dlc);


       /* if (oldType != dlc.getValorisationType()) {
            notificationService.sendStatusChange(dlc, oldType, dlc.getValorisationType());
        }
        */

        return DlcMapper.toUserProductResponse(dlc);
    }

    @Override
    public ScanLookupResponse lookupBarcode(String barcode) {
        Product prod = productRepository.findByBarcode(barcode).orElse(null);
        Dlc userDlc = dlcRepository.findByProductBarcode(barcode).orElse(null);


        ScanLookupResponse resp = new ScanLookupResponse();
        resp.setFound(prod != null);
        resp.setGlobalProduct(prod == null ? null : DlcMapper.toGlobalProductDto(prod));
        resp.setUserProduct(userDlc == null ? null : DlcMapper.toUserProductResponse(userDlc));
        resp.setActions(new ScanActions(prod != null, userDlc == null));
        resp.setMessage(prod != null ? "Found " + prod.getName() : "Not found");
        return resp;
    }

    @Override
    public ScanCreateResponse createFromBarcode(String barcode) {
        Product prod = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));


        Dlc dlc = new Dlc();
        dlc.setProduct(prod);
        dlc.setExpiryDate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30)); // default 30 days
        dlc.setQuantity(1);
        dlc.setTimeRemaining(dlc.calculateTimeRemaining());
        dlc.calculateValType();
        dlcRepository.save(dlc);


        return DlcMapper.toScanCreateResponse(dlc);
    }
}
