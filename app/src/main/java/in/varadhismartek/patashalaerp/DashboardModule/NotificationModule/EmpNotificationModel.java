package in.varadhismartek.patashalaerp.DashboardModule.NotificationModule;

import java.util.ArrayList;

public class EmpNotificationModel {

    private String applyDate;
    private ArrayList<NotificationModel> notificationList;

    public EmpNotificationModel(String applyDate, ArrayList<NotificationModel> notificationList) {
        this.applyDate = applyDate;
        this.notificationList = notificationList;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public ArrayList<NotificationModel> getNotificationList() {
        return notificationList;
    }
}


/*
*
*               "status": true,
                "employee_uuid": "fc22c806-4e4b-4a38-9390-e2a03d7588c6",
                "approver_comment": null,
                "message": "Tomorrow is holiday due to heavy rain",
                "notification_attachment": "varadhi_smartech/Notification_files/image.jpeg",
                "send_date": "2019-12-20",
                "notification_id": "8df5070c-d157-4c0a-be7f-bf0309983448",
                "added_datetime": "2019-12-14T06:59:03.265Z",
                "added_employee_last_name": "Warner",
                "employee_fist_name": "Sharath",
                "added_employee_first_name": "David",
                "employee_last_name": "Kumar",
                "send_time": "11:50 AM",
                "approver_status": "Approved",
                "added_employeeid": "6548c1a6-3538-4b75-9f0d-554cdcab4747",
                "send_to": "staffs",
                "updated_datetime": "2019-12-14T07:20:47.367Z"

* */