package com.inventory.ui.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.ui.dtos.company_demand.CompanyDemandRequest;
import com.inventory.ui.models.CompanyDemand;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CompanyDemandService {
    private static CompanyDemandService instance;
    private final HttpService httpService;

    public CompanyDemandService() {
        this.httpService = new HttpService();
    }

    public static CompanyDemandService getInstance() {
        if (instance == null) {
            instance = new CompanyDemandService();
        }
        return instance;
    }

    public List<CompanyDemand> findAll() {
        try {
            Response response = httpService.get("company-demands");
            if (response.isSuccessful()) {
                ObjectMapper objectMapper = new ObjectMapper();
                String responseBody = response.body().string();
                return objectMapper.readValue(responseBody, new TypeReference<List<CompanyDemand>>() {
                });
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public boolean create(CompanyDemandRequest companyDemandCreate) {
        try {
            Response response = httpService.post("company-demands", companyDemandCreate);
            return response.isSuccessful();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean update(UUID id) {
        System.out.println("CompanyDemandService.update");
        try {
            Response response = httpService.put("company-demands/" + id , null);
            return response.isSuccessful();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
