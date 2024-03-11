package pojo;

public class LogisticsInfo {
    String logisticsOrg;
    String RecipientOrg;
    String CurRouteInfo;

    public LogisticsInfo() {
    }

    public LogisticsInfo(String logisticsOrg, String recipientOrg, String curRouteInfo) {
        this.logisticsOrg = logisticsOrg;
        RecipientOrg = recipientOrg;
        CurRouteInfo = curRouteInfo;
    }

    public String getRecipientOrg() {
        return RecipientOrg;
    }

    public void setRecipientOrg(String recipientOrg) {
        RecipientOrg = recipientOrg;
    }

    public String getCurRouteInfo() {
        return CurRouteInfo;
    }

    public void setCurRouteInfo(String curRouteInfo) {
        CurRouteInfo = curRouteInfo;
    }

    public String getLogisticsOrg() {
        return logisticsOrg;
    }

    public void setLogisticsOrg(String logisticsOrg) {
        this.logisticsOrg = logisticsOrg;
    }

    @Override
    public String toString() {
        return "LogisticsInfo{" +
                "logisticsOrg='" + logisticsOrg + '\'' +
                ", RecipientOrg='" + RecipientOrg + '\'' +
                ", CurRouteInfo='" + CurRouteInfo + '\'' +
                '}';
    }
}
