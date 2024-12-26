package com.inventory.backend;

import com.inventory.backend.blockchain.GatewaySingleton;
import org.hyperledger.fabric.client.Gateway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {

        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public Gateway gateway() throws Exception {
        return GatewaySingleton.getInstance();
    }
}
