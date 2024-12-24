package com.inventory.backend.console;

import com.inventory.backend.controllers.BlockchainController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.inventory.backend")
public class Application implements CommandLineRunner {

    // Use dependency injection with Spring to manage beans
    private final BlockchainController controller;

    @Autowired // Automatically inject the BlockchainController bean
    public Application(BlockchainController controller) {
        this.controller = controller;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Test blockchain");

        // Ensure proper calls to BlockchainController methods
        System.out.println("Init ledger");
        controller.initLedger();
        System.out.println("Done init ledger");

        System.out.println("Get exchanges by organization");
        System.out.println(controller.getExchangesByOrganization("Company"));
        System.out.println("Done get exchanges by organization");
    }
}