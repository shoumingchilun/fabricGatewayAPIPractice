package DAO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.hyperledger.fabric.client.CommitException;
import org.hyperledger.fabric.client.CommitStatusException;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.EndorseException;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.GatewayException;
import org.hyperledger.fabric.client.SubmitException;
import org.hyperledger.fabric.client.identity.Identities;
import org.hyperledger.fabric.client.identity.Identity;
import org.hyperledger.fabric.client.identity.Signer;
import org.hyperledger.fabric.client.identity.Signers;
import org.hyperledger.fabric.client.identity.X509Identity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.util.Properties;

/**
 * @auther 齿轮
 * @create 2023-02-28-20:07
 */
public class GateWayConfig {
    private static Properties properties;

    private static String MSP_ID;
    private static String CHANNEL_NAME;
    private static String CHAINCODE_NAME;

    // Path to crypto materials.
    private static Path CRYPTO_PATH;
    // Path to user certificate.
    private static Path CERT_PATH;
    // Path to user private key directory.
    private static Path KEY_DIR_PATH;
    // Path to peer tls certificate.
    private static Path TLS_CERT_PATH;

    // Gateway peer end point.
    private static String PEER_ENDPOINT;
    private static String OVERRIDE_AUTH;

    private final Contract contract;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public static Identity newIdentity() throws IOException, CertificateException {
        var certReader = Files.newBufferedReader(CERT_PATH);
        var certificate = Identities.readX509Certificate(certReader);

        return new X509Identity(MSP_ID, certificate);
    }

    public static Signer newSigner() throws IOException, InvalidKeyException {
        var keyReader = Files.newBufferedReader(getPrivateKeyPath());
        var privateKey = Identities.readPrivateKey(keyReader);

        return Signers.newPrivateKeySigner(privateKey);
    }

    private static ManagedChannel newGrpcConnection() throws IOException, CertificateException {
        var tlsCertReader = Files.newBufferedReader(TLS_CERT_PATH);
        var tlsCert = Identities.readX509Certificate(tlsCertReader);

        return NettyChannelBuilder.forTarget(PEER_ENDPOINT)
                .sslContext(GrpcSslContexts.forClient().trustManager(tlsCert).build()).overrideAuthority(OVERRIDE_AUTH)
                .build();
    }

    private static Path getPrivateKeyPath() throws IOException {
        try (var keyFiles = Files.list(KEY_DIR_PATH)) {
            return keyFiles.findFirst().orElseThrow();
        }
    }

    public static ManagedChannel init(String PATH_CATALOGUE, String PROPERTIES_PATH) throws Exception {
        InputStream inputStream = new FileInputStream(PATH_CATALOGUE+"\\GatewayConfig.properties");
        //D:\暂时存储\tempStorage\GatewayConfig.properties
        properties = new Properties();
        properties.load(inputStream);
        MSP_ID = properties.getProperty("MSP_ID");
        CHANNEL_NAME = properties.getProperty("CHANNEL_NAME");
        CHAINCODE_NAME = properties.getProperty("CHAINCODE_NAME");
        CRYPTO_PATH = Paths.get(PATH_CATALOGUE +"\\"+ properties.getProperty("CRYPTO_PATH"));
        CERT_PATH = CRYPTO_PATH.resolve(Paths.get(properties.getProperty("CERT_PATH")));
        KEY_DIR_PATH = CRYPTO_PATH.resolve(Paths.get(properties.getProperty("KEY_DIR_PATH")));
        TLS_CERT_PATH = CRYPTO_PATH.resolve(Paths.get(properties.getProperty("TLS_CERT_PATH")));
        PEER_ENDPOINT = properties.getProperty("PEER_ENDPOINT");
        OVERRIDE_AUTH = properties.getProperty("OVERRIDE_AUTH");

        // The gRPC client connection should be shared by all Gateway connections to
        // this endpoint.
        ManagedChannel channel = newGrpcConnection();
        return channel;

//        var builder = Gateway.newInstance().identity(newIdentity()).signer(newSigner()).connection(channel)
//                // Default timeouts for different gRPC calls
//                .evaluateOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
//                .endorseOptions(options -> options.withDeadlineAfter(15, TimeUnit.SECONDS))
//                .submitOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
//                .commitStatusOptions(options -> options.withDeadlineAfter(1, TimeUnit.MINUTES));

//        try (var gateway = builder.connect()) {
//            new GateWayConfig(gateway).run();
//        } finally {
////            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
//        }
    }

    public GateWayConfig(final Gateway gateway) {
        // Get a network instance representing the channel where the smart contract is
        // deployed.
        var network = gateway.getNetwork(CHANNEL_NAME);

        // Get the smart contract from the network.
        contract = network.getContract(CHAINCODE_NAME);
    }

    public void initLedger() throws EndorseException, SubmitException, CommitStatusException, CommitException {
        System.out.println("\n--> Submit Transaction: InitLedger, function creates the initial set of Cargos on the ledger");
        contract.submitTransaction("InitLedger");
        System.out.println("*** Transaction committed successfully");
    }

    /**
     * Evaluate a transaction to query ledger state.
     */
    public byte[] QueryAllCargo() throws GatewayException {
        return contract.evaluateTransaction("QueryAllCargo");
    }

    /**
     * Submit a transaction synchronously, blocking until it has been committed to
     * the ledger.
     */
    public void RegisterCargo(String cargoId, String Producer, String Name, String Introduce, String Owner) throws EndorseException, SubmitException, CommitStatusException, CommitException {
        contract.submitTransaction("RegisterCargo", cargoId, Producer, Name, Introduce, Owner);
    }

    public byte[] ReadCargo(String ID) throws GatewayException {
        return contract.evaluateTransaction("ReadCargo", ID);
    }

    public void UpdateCargo(String ID, String Producer, String TransportState, String Name, String Introduce, String Owner, String KeyHash, String ContractHash, String Settle, String LogisticsOrg, String RecipientOrg, String CurRouteInfo) throws EndorseException, CommitException, SubmitException, CommitStatusException {
        contract.submitTransaction("UpdateCargo", ID, Producer, TransportState, Name, Introduce, Owner, KeyHash, ContractHash, Settle, LogisticsOrg, RecipientOrg, CurRouteInfo);
    }

    public byte[] SwapVerify(String ID, String Org) throws GatewayException {
        return contract.evaluateTransaction("SwapVerify", ID, Org);
    }

    public void UpdateKey(String ID, String Key) throws EndorseException, SubmitException, CommitStatusException {
        var commit = contract.newProposal("UpdateKey").putTransient("KEY_DATA", "{\"ID\":\"" + ID + "\",\"Key\":\"" + Key + "\"}").build().endorse().submitAsync();
        var status = commit.getStatus();
        if (!status.isSuccessful()) {
            throw new RuntimeException("failed to commit transaction with status code " + status.getCode());
        } else {
            System.out.println("\n*** UpdateKey committed successfully");
        }
    }

    public void run() throws GatewayException, CommitException {
        // useless function, just be used to test.
//        System.out.println("\n--> Submit Transaction: InitLedger, function creates the initial set of Cargos on the ledger");
//        initLedger();
//        System.out.println("*** Transaction committed successfully");

        // Return all the current cargos on the ledger.
//        System.out.println("\n--> Evaluate Transaction: QueryAllCargo, function returns all the current Cargos on the ledger");
//        byte[] result = QueryAllCargo();
//        System.out.println("*** Result: " + prettyJson(result));

        // Create a new cargo on the ledger.
//        System.out.println("\n--> Submit Transaction: RegisterCargo, creates new cargo with ID, Color, Size, Owner and AppraisedValue arguments");
//        RegisterCargo("TestCargo2", "Supplier", "Car2", "some info..", "Supplier");
//        System.out.println("*** Transaction committed successfully");
//
//        System.out.println("\n--> Evaluate Transaction: ReadCargo, function returns the selected Cargo on the ledger");
//        byte[] result2 = ReadCargo("TestCargo1");
//        System.out.println("*** Result: " + prettyJson(result2));
//
//        System.out.println("\n--> Submit Transaction: UpdateCargo, update  cargo with all arguments");
//        UpdateCargo("TestCargo1", "Supplier", "0", "Car1", "some info...", "Supplier", "", "1234", "", "Logistics", "Recipient", "unbegin");
//        System.out.println("*** Transaction committed successfully");
//
//        System.out.println("\n--> Evaluate Transaction: ReadCargo, function returns the selected Cargo on the ledger");
//        result2 = ReadCargo("TestCargo1");
//        System.out.println("*** Result: " + prettyJson(result2));
//
//        System.out.println("\n--> Submit Transaction: UpdateKey, update  cargo's Key");
//        UpdateKey("TestCargo1", "1234567");
//        System.out.println("*** Transaction committed successfully");
//
//        System.out.println("\n--> Evaluate Transaction: SwapVerify, function returns a boolean to verify whether is the key  same.");
//        byte[] result3 = SwapVerify("TestCargo1", "Logistics");
//        System.out.println("*** Result: " + prettyJson(result3));

    }


    public String prettyJson(final byte[] json) {
        return prettyJson(new String(json, StandardCharsets.UTF_8));
    }

    public String prettyJson(final String json) {
        var parsedJson = JsonParser.parseString(json);
        return gson.toJson(parsedJson);
    }

}
