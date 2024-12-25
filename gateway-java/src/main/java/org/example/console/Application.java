package org.example.console;

import org.example.controllers.BlockchainController;
import org.example.enums.TransactionType;
import org.example.metier.BlockchainMetierImpl;

import java.util.Map;

public class Application {

    public static void main(String[] args) throws Exception {
        BlockchainMetierImpl metier = new BlockchainMetierImpl();
        BlockchainController controller = new BlockchainController(metier);

        System.out.println("Test blockchain");

        // Ensure proper calls to BlockchainController methods
        System.out.println("Init ledger");
        controller.initLedger();
        System.out.println("Done init ledger");

        System.out.println("Get exchanges by organization");
        System.out.println(controller.getExchangesByOrganization("company"));
        System.out.println("Done get exchanges by organization");

        System.out.println("Get inventory");
        System.out.println(controller.getInventory("supplier1"));
        System.out.println("Done get inventory");

        System.out.println("Let's trade some goods");
        controller.trade("supplier1","company","product1", 80);
        System.out.println("Done trading goods");

        System.out.println("-------------------------------- after trading --------------------------------");

        System.out.println("Get inventory");
        System.out.println(controller.getInventory("company"));
        System.out.println("Done get inventory");


        System.out.println("Get inventory");
        System.out.println(controller.getInventory("supplier1"));
        System.out.println("Done get inventory");
    }
}