package com.inventory.backend.repos;

import com.inventory.backend.entities.CompanyDemand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
public interface CompanyDemandRepository extends JpaRepository<CompanyDemand, UUID> {
    @Query("SELECT cd FROM CompanyDemand cd WHERE cd.supplier.id = :supplierId AND cd.status = 'PENDING'")
    List<CompanyDemand> findAllBySupplierIdAndStatusPending(@Param("supplierId") UUID supplierId);

}