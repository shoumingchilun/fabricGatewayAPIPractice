package DAO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.grpc.ManagedChannel;
import org.hyperledger.fabric.client.*;
import pojo.Cargo;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @auther 齿轮
 * @create 2023-03-02-23:04
 */
public class CargoDAOImpl implements CargoDAO {
    public static ManagedChannel channel;
    public static Gateway.Builder builder;
    public static Gateway gateway;
    private static GateWayConfig gateWayConfig;

    @Override
    public void init(String PATH_CATALOGUE) throws Exception {
        channel = GateWayConfig.init(PATH_CATALOGUE, "GatewayConfig.properties");
        builder = Gateway.newInstance().identity(GateWayConfig.newIdentity()).signer(GateWayConfig.newSigner()).connection(channel)
                // Default timeouts for different gRPC calls
                .evaluateOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .endorseOptions(options -> options.withDeadlineAfter(15, TimeUnit.SECONDS))
                .submitOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .commitStatusOptions(options -> options.withDeadlineAfter(1, TimeUnit.MINUTES));
        gateway = builder.connect();
        gateWayConfig = new GateWayConfig(gateway);
    }

    @Override
    public void initLedger() throws CommitException, GatewayException, InterruptedException {
        gateWayConfig.initLedger();
    }

    @Override
    public List<Cargo> QueryAllCargo() throws GatewayException {
        byte[] bytes = gateWayConfig.QueryAllCargo();
        String json = gateWayConfig.prettyJson(bytes);
        return JSON.parseObject(json, new TypeReference<List<Cargo>>() {
        });
    }

    @Override
    public Cargo ReadCargo(String ID) throws GatewayException {
        byte[] bytes = gateWayConfig.ReadCargo(ID);
        String json = gateWayConfig.prettyJson(bytes);
        return JSON.parseObject(json, new TypeReference<Cargo>() {
        });
    }

    @Override
    public boolean SwapVerify(String ID, String Org) throws GatewayException {
        byte[] bytes = gateWayConfig.SwapVerify(ID, Org);
        return (new String(bytes)).equals("true");
    }

    @Override
    public void UpdateKey(String ID, String KEY) throws EndorseException, SubmitException, CommitStatusException {
        gateWayConfig.UpdateKey(ID, KEY);
    }

    @Override
    public void close() throws InterruptedException {
        channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }


    @Override
    public void RegisterCargo(Cargo cargo) throws EndorseException, CommitException, SubmitException, CommitStatusException {
        gateWayConfig.RegisterCargo(
                cargo.getID(),
                cargo.getProducer(),
                cargo.getName(),
                cargo.getIntroduce(),
                cargo.getOwner());
    }

    @Override
    public void UpdateCargo(Cargo cargo) throws EndorseException, CommitException, SubmitException, CommitStatusException {
        gateWayConfig.UpdateCargo(
                cargo.getID(),
                cargo.getProducer(),
                String.valueOf(cargo.getTransportState()),
                cargo.getName(),
                cargo.getIntroduce(),
                cargo.getOwner(),
                cargo.getKeyHash(),
                cargo.getContractHash(),
                cargo.getSettle(),
                cargo.getLogisticsInfo().getLogisticsOrg(),
                cargo.getLogisticsInfo().getRecipientOrg(),
                cargo.getLogisticsInfo().getCurRouteInfo());
    }
}
