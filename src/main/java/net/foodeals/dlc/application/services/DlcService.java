package net.foodeals.dlc.application.services;


import net.foodeals.dlc.application.dtos.requests.CreateDlcRequest;
import net.foodeals.dlc.application.dtos.requests.CreateUserProductRequest;
import net.foodeals.dlc.application.dtos.requests.UpdateDlcRequest;
import net.foodeals.dlc.application.dtos.responses.*;

import java.util.List;
import java.util.UUID;

public interface DlcService {


    public List<DlcDto> getAllDlcs();

    public DlcDto getDlcById(UUID id);

    public DlcDto createDlc(CreateDlcRequest request);

    public DlcDto updateDlc(UUID id, UpdateDlcRequest request);

    public void deleteDlc(UUID id);

    public PaginatedUserProducts listUserProducts(int page, int limit);

    public UserProductResponse getById(UUID id);

    public UserProductResponse createFromRequest(CreateUserProductRequest req);

    public UserProductResponse update(UUID id, CreateUserProductRequest req);

    public ScanLookupResponse lookupBarcode(String barcode);

    public ScanCreateResponse createFromBarcode(String barcode);



}
