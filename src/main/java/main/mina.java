package main;

import DAO.CargoDAO;
import DAO.CargoDAOImpl;
import pojo.Cargo;
import pojo.LogisticsInfo;

/**
 * @auther 齿轮
 * @create 2023-03-02-23:32
 */
public class mina {
    public static void main(String[] args) throws Exception {
        CargoDAO cargoDAO = new CargoDAOImpl();

        cargoDAO.init("D:\\暂时存储\\tempStorage\\RecipientIdentity");
//        Cargo cargo =new Cargo("TestCargo3","Supplier","car3","第一次完全测试！","Supplier");
//        cargoDAO.RegisterCargo(cargo);
//        System.out.println(cargoDAO.ReadCargo("TestCargo3"));
//        cargo.setContractHash("1234");
//        cargo.setSettle("测试的信息");
//        cargo.setTransportState(1);
//        LogisticsInfo logisticsInfo = new LogisticsInfo("Logistics","Recipient","实时物理信息");
//        cargo.setLogisticsInfo(logisticsInfo);
//        cargo.setKeyHash("key的hash");
//        cargoDAO.UpdateCargo(cargo);
        System.out.println(cargoDAO.ReadCargo("TestCargo3"));
        cargoDAO.close();

//
//        cargoDAO.init("D:\\暂时存储\\tempStorage\\LogisticsIdentity");
//        System.out.println(cargoDAO.ReadCargo("TestCargo3"));
//        cargoDAO.UpdateKey("TestCargo3","12345");
//        System.out.println(cargoDAO.SwapVerify("TestCargo3", "Supplier"));
//        cargoDAO.UpdateKey("TestCargo3","1234");
//        System.out.println(cargoDAO.SwapVerify("TestCargo3", "Supplier"));
//        cargoDAO.close();
    }
}
