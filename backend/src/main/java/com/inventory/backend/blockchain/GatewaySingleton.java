package com.inventory.backend.blockchain;

import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.TlsChannelCredentials;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.Hash;
import org.hyperledger.fabric.client.identity.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

@Configuration
public class GatewaySingleton {

    private static final String MSP_ID = System.getenv().getOrDefault("MSP_ID", "Org1MSP");
    private static final Path CRYPTO_PATH = Paths.get("../private-blockchain/network/organizations/peerOrganizations/org1.example.com");
    private static final Path CERT_DIR_PATH = CRYPTO_PATH.resolve(Paths.get("users/User1@org1.example.com/msp/signcerts"));
    private static final Path KEY_DIR_PATH = CRYPTO_PATH.resolve(Paths.get("users/User1@org1.example.com/msp/keystore"));
    private static final Path TLS_CERT_PATH = CRYPTO_PATH.resolve(Paths.get("peers/peer0.org1.example.com/tls/ca.crt"));
    private static final String PEER_ENDPOINT = "localhost:7051";
    private static final String OVERRIDE_AUTH = "peer0.org1.example.com";

    @Bean
    public Gateway gateway() throws Exception {
        return createGateway();
    }

    private Gateway createGateway() throws Exception {
        var channel = newGrpcConnection();
        return Gateway.newInstance()
                .identity(newIdentity())
                .signer(newSigner())
                .hash(Hash.SHA256)
                .connection(channel)
                .evaluateOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .endorseOptions(options -> options.withDeadlineAfter(15, TimeUnit.SECONDS))
                .submitOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .commitStatusOptions(options -> options.withDeadlineAfter(1, TimeUnit.MINUTES))
                .connect();
    }

    private ManagedChannel newGrpcConnection() {
        try {
            System.out.println("Attempting to create gRPC connection...");
            var credentials = TlsChannelCredentials.newBuilder()
                    .trustManager(TLS_CERT_PATH.toFile())
                    .build();
            System.out.println("TLS credentials successfully created.");
            var channel = Grpc.newChannelBuilder(PEER_ENDPOINT, credentials)
                    .overrideAuthority(OVERRIDE_AUTH)
                    .build();
            System.out.println("gRPC connection successfully created to " + PEER_ENDPOINT);
            return channel;
        } catch (IOException e) {
            System.out.println("Error creating gRPC connection: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create gRPC connection", e);
        }
    }

    private Identity newIdentity() {
        try {
            System.out.println("Attempting to load identity certificate from " + CERT_DIR_PATH);
            try (var certReader = Files.newBufferedReader(getFirstFilePath(CERT_DIR_PATH))) {
                var certificate = Identities.readX509Certificate(certReader);
                System.out.println("Identity certificate successfully loaded.");
                return new X509Identity(MSP_ID, certificate);
            }
        } catch (IOException | CertificateException e) {
            System.out.println("Error loading identity certificate: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load identity certificate", e);
        }
    }

    private Signer newSigner() {
        try {
            System.out.println("Attempting to load private key from " + KEY_DIR_PATH);
            try (var keyReader = Files.newBufferedReader(getFirstFilePath(KEY_DIR_PATH))) {
                var privateKey = Identities.readPrivateKey(keyReader);
                System.out.println("Private key successfully loaded.");
                return Signers.newPrivateKeySigner(privateKey);
            }
        } catch (IOException | InvalidKeyException e) {
            System.out.println("Error loading private key: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load private key", e);
        }
    }

    private Path getFirstFilePath(Path dirPath) {
        try {
            System.out.println("Looking for files in directory: " + dirPath);
            try (var keyFiles = Files.list(dirPath)) {
                var path = keyFiles.findFirst().orElseThrow(() -> new IOException("No files found in " + dirPath));
                System.out.println("Found file: " + path);
                return path;
            }
        } catch (IOException e) {
            System.out.println("Error accessing directory: " + dirPath + " - " + e.getMessage());
            throw new RuntimeException("Failed to access directory " + dirPath, e);
        }
    }
}
