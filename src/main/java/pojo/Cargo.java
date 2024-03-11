package pojo;

// Cargo 货物类
public class Cargo {
    String ID;   // ID 是每个货物独有的标志信息，注册时后端自动生成，不可修改，要求：不重复，重复则返回注册失败。
    String Producer;  // Producer 货物的生产组织，不可修改，建议格式为"组织名:注册员工身份"。
    String Name;  // Name 货物名称，不可修改，要求：简单明了，方便检索。
    String Introduce;  // Introduce 货物简介，不可修改。
    String Owner;  // Owner 指明货物目前在谁手里。
    String KeyHash;  // KeyHash 交接密钥的Hash，未设置情况下为空字符串。
    String ContractHash;  // ContractHash 签订合同的Hash值，用于证明，不可修改。
    String Settle;  // Settle 结算信息。
    int TransportState;  // TransportState 运输的状态。
    LogisticsInfo LogisticsInfo;  // LogisticsInfo 物流信息接口。

    public Cargo(){}

    @Override
    public String toString() {
        return "Cargo{" +
                "ID='" + ID + '\'' +
                ", Producer='" + Producer + '\'' +
                ", Name='" + Name + '\'' +
                ", Introduce='" + Introduce + '\'' +
                ", Owner='" + Owner + '\'' +
                ", KeyHash='" + KeyHash + '\'' +
                ", ContractHash='" + ContractHash + '\'' +
                ", Settle='" + Settle + '\'' +
                ", TransportState=" + TransportState +
                ", LogisticsInfo=" + LogisticsInfo +
                '}';
    }

    public Cargo(String ID, String producer, String name, String introduce, String owner) {
        this.ID = ID;
        Producer = producer;
        Name = name;
        Introduce = introduce;
        Owner = owner;
    }

    public Cargo(String ID, String producer, String name, String introduce, String owner,
                 String keyHash, String contractHash, String settle,
                 int transportState, pojo.LogisticsInfo logisticsInfo) {
        this.ID = ID;
        Producer = producer;
        Name = name;
        Introduce = introduce;
        Owner = owner;
        KeyHash = keyHash;
        ContractHash = contractHash;
        Settle = settle;
        TransportState = transportState;
        LogisticsInfo = logisticsInfo;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getProducer() {
        return Producer;
    }

    public void setProducer(String producer) {
        Producer = producer;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIntroduce() {
        return Introduce;
    }

    public void setIntroduce(String introduce) {
        Introduce = introduce;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getKeyHash() {
        return KeyHash;
    }

    public void setKeyHash(String keyHash) {
        KeyHash = keyHash;
    }

    public String getContractHash() {
        return ContractHash;
    }

    public void setContractHash(String contractHash) {
        ContractHash = contractHash;
    }

    public String getSettle() {
        return Settle;
    }

    public void setSettle(String settle) {
        Settle = settle;
    }

    public int getTransportState() {
        return TransportState;
    }

    public void setTransportState(int transportState) {
        TransportState = transportState;
    }

    public pojo.LogisticsInfo getLogisticsInfo() {
        return LogisticsInfo;
    }

    public void setLogisticsInfo(pojo.LogisticsInfo logisticsInfo) {
        LogisticsInfo = logisticsInfo;
    }
}
