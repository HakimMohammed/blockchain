package com.inventory.backend;

import com.inventory.backend.controllers.BlockchainController;
import com.inventory.backend.entities.Category;
import com.inventory.backend.entities.Organization;
import com.inventory.backend.entities.Product;
import com.inventory.backend.entities.User;
import com.inventory.backend.enums.OrganizationType;
import com.inventory.backend.metier.BlockchainMetierImpl;
import com.inventory.backend.services.CategoryService;
import com.inventory.backend.services.OrganizationService;
import com.inventory.backend.services.ProductService;
import com.inventory.backend.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner testBlockchain() {
        return args -> {
            System.out.println("Test blockchain");
            BlockchainMetierImpl metier = new BlockchainMetierImpl();
            BlockchainController controller = new BlockchainController(metier);
            System.out.println("Init ledger");
            controller.initLedger();
            System.out.println("Get exchanges by organization");
            System.out.println(controller.getExchangesByOrganization("Company"));
        };
    }

}
