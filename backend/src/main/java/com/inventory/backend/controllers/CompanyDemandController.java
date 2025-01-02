package com.inventory.backend.controllers;

import com.inventory.backend.dtos.company_demands.CompanyDemandCreate;
import com.inventory.backend.dtos.company_demands.CompanyDemandResponse;
import com.inventory.backend.entities.CompanyDemand;
import com.inventory.backend.entities.Organization;
import com.inventory.backend.entities.Product;
import com.inventory.backend.mappers.CompanyDemandMapper;
import com.inventory.backend.services.DemandService;
import com.inventory.backend.services.OrganizationService;
import com.inventory.backend.services.ProductService;
import com.inventory.backend.sockets.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/company-demands")
@RequiredArgsConstructor
public class CompanyDemandController {

    private final DemandService demandService;
    private final CompanyDemandMapper companyDemandMapper;
    private final ProductService productService;
    private final WebSocketHandler webSocketHandler;

    @GetMapping
    public ResponseEntity<List<CompanyDemandResponse>> findAll() {
        return ResponseEntity.ok(demandService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDemandResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(companyDemandMapper.toResponse(demandService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDemandResponse> updateById(@PathVariable UUID id) {
        return ResponseEntity.ok(companyDemandMapper.toResponse(demandService.updateById(id ,null)));
    }

    @PostMapping
    public ResponseEntity<CompanyDemandResponse> save(@RequestBody CompanyDemandCreate companyDemandCreate) {
        Product product = productService.findById(companyDemandCreate.getProductId());
        CompanyDemand companyDemand = demandService.save(companyDemandMapper.toEntity(companyDemandCreate, product));
        webSocketHandler.sendNotification("Company has demanded " + companyDemandCreate.getQuantity() + " " + product.getName() + " with id " + companyDemand.getId());
        return ResponseEntity.ok(companyDemandMapper.toResponse(companyDemand));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        demandService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}