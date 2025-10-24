package net.foodeals.dlc.infrastructure.interfaces.web;

import lombok.RequiredArgsConstructor;
import net.foodeals.dlc.application.dtos.requests.CreateDlcRequest;
import net.foodeals.dlc.application.dtos.requests.CreateUserProductRequest;
import net.foodeals.dlc.application.dtos.requests.UpdateDlcRequest;
import net.foodeals.dlc.application.dtos.responses.DlcDto;
import net.foodeals.dlc.application.dtos.responses.ScanCreateResponse;
import net.foodeals.dlc.application.dtos.responses.ScanLookupResponse;
import net.foodeals.dlc.application.dtos.responses.UserProductResponse;
import net.foodeals.dlc.application.services.DlcService;
;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("v1/dlcs")
@RequiredArgsConstructor
public class DlcController {


    private final DlcService dlcService;

    @GetMapping
    public ResponseEntity<List<DlcDto>> getAllDlcs() {
        return ResponseEntity.ok(dlcService.getAllDlcs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DlcDto> getDlc(@PathVariable UUID id) {
        return ResponseEntity.ok(dlcService.getDlcById(id));
    }

    @PostMapping
    public ResponseEntity<DlcDto> createDlc(@RequestBody CreateDlcRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dlcService.createDlc(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DlcDto> updateDlc(@PathVariable UUID id, @RequestBody UpdateDlcRequest request) {
        return ResponseEntity.ok(dlcService.updateDlc(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDlc(@PathVariable UUID id) {
        dlcService.deleteDlc(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("me/products")
    public ResponseEntity<?> listUserProducts(@RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(dlcService.listUserProducts(page, limit));
    }


    @GetMapping("me/products/{id}")
    public ResponseEntity<UserProductResponse> getUserProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(dlcService.getById(id));
    }


    @PostMapping("me/products")
    public ResponseEntity<UserProductResponse> createUserProduct(@RequestBody CreateUserProductRequest req) {
        UserProductResponse created = dlcService.createFromRequest(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PutMapping("me/products/{id}")
    public ResponseEntity<UserProductResponse> updateUserProduct(@PathVariable UUID id,
                                                                 @RequestBody CreateUserProductRequest req) {
        return ResponseEntity.ok(dlcService.update(id, req));
    }


    @DeleteMapping("me/products/{id}")
    public ResponseEntity<?> deleteUserProduct(@PathVariable UUID id) {
        dlcService.deleteDlc(id);
        return ResponseEntity.ok().body(java.util.Collections.singletonMap("message", "Product deleted"));
    }

    @PostMapping("/lookup")
    public ResponseEntity<ScanLookupResponse> lookup(@RequestBody Map<String, String> body) {
        String barcode = body.get("barcode");
        return ResponseEntity.ok(dlcService.lookupBarcode(barcode));
    }


    @PostMapping("/create-from-barcode")
    public ResponseEntity<ScanCreateResponse> createFromBarcode(@RequestBody Map<String, String> body) {
        String barcode = body.get("barcode");
        return ResponseEntity.status(201).body(dlcService.createFromBarcode(barcode));
    }

}
