package in.varadhismartek.patashalaerp.StudentModule;

public class FeesDetailsModel {


    private String paid_status, added_datetime, penalty_to, fee_payment_id, penalty_from, fee_type,
            due_date, amount_due, penalty_amount, from_date, net_fees;

    public FeesDetailsModel(String paid_status, String added_datetime, String penalty_to, String fee_payment_id,
                            String penalty_from, String fee_type, String due_date, String amount_due,
                            String penalty_amount, String from_date, String net_fees) {

        this.paid_status = paid_status;
        this.added_datetime = added_datetime;
        this.penalty_to = penalty_to;
        this.fee_payment_id = fee_payment_id;
        this.penalty_from = penalty_from;
        this.fee_type = fee_type;
        this.due_date = due_date;
        this.amount_due = amount_due;
        this.penalty_amount = penalty_amount;
        this.from_date = from_date;
        this.net_fees = net_fees;
    }

    public String getPaid_status() {
        return paid_status;
    }

    public String getAdded_datetime() {
        return added_datetime;
    }

    public String getPenalty_to() {
        return penalty_to;
    }

    public String getFee_payment_id() {
        return fee_payment_id;
    }

    public String getPenalty_from() {
        return penalty_from;
    }

    public String getFee_type() {
        return fee_type;
    }

    public String getDue_date() {
        return due_date;
    }

    public String getAmount_due() {
        return amount_due;
    }

    public String getPenalty_amount() {
        return penalty_amount;
    }

    public String getFrom_date() {
        return from_date;
    }

    public String getNet_fees() {
        return net_fees;
    }
}
