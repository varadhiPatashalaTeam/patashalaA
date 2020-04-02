package in.varadhismartek.patashalaerp.StudentModule;

import java.util.ArrayList;

public class FeesMonthModel {

    private String month, upcoming_total, paid_total, pending_total;
    private ArrayList<FeesDetailsModel> feesDetailsList;

    public FeesMonthModel(String month, String upcoming_total, String paid_total,
                          String pending_total, ArrayList<FeesDetailsModel> feesDetailsList) {
        this.month = month;
        this.upcoming_total = upcoming_total;
        this.paid_total = paid_total;
        this.pending_total = pending_total;
        this.feesDetailsList = feesDetailsList;
    }

    public String getMonth() {
        return month;
    }

    public String getUpcoming_total() {
        return upcoming_total;
    }

    public String getPaid_total() {
        return paid_total;
    }

    public String getPending_total() {
        return pending_total;
    }

    public ArrayList<FeesDetailsModel> getFeesDetailsList() {
        return feesDetailsList;
    }
}
