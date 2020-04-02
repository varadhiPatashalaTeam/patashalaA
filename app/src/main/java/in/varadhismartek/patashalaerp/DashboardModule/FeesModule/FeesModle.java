package in.varadhismartek.patashalaerp.DashboardModule.FeesModule;

public class FeesModle {
    private String strSerialNo;
    private String strFeeType,steFeeCode,strInstallment,strDueDate;


    public FeesModle(String strSerialNo, String strFeeType, String steFeeCode, String strInstallment, String strDueDate) {
        this.strSerialNo = strSerialNo;
        this.strFeeType = strFeeType;
        this.steFeeCode = steFeeCode;
        this.strInstallment = strInstallment;
        this.strDueDate = strDueDate;
    }

    public String getStrSerialNo() {
        return strSerialNo;
    }

    public void setStrSerialNo(String strSerialNo) {
        this.strSerialNo = strSerialNo;
    }

    public String getStrFeeType() {
        return strFeeType;
    }

    public void setStrFeeType(String strFeeType) {
        this.strFeeType = strFeeType;
    }

    public String getSteFeeCode() {
        return steFeeCode;
    }

    public void setSteFeeCode(String steFeeCode) {
        this.steFeeCode = steFeeCode;
    }

    public String getStrInstallment() {
        return strInstallment;
    }

    public void setStrInstallment(String strInstallment) {
        this.strInstallment = strInstallment;
    }

    public String getStrDueDate() {
        return strDueDate;
    }

    public void setStrDueDate(String strDueDate) {
        this.strDueDate = strDueDate;
    }
}
