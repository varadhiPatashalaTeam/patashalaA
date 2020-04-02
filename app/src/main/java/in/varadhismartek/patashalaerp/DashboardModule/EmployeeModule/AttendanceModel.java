package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

public class AttendanceModel {

    private String date, status, total_time, added_datetime, logOut_time, logIn_time, added_by, attendance_id;

    public AttendanceModel(String date, String status, String total_time, String added_datetime,
                           String logOut_time, String logIn_time, String added_by, String attendance_id) {

        this.date = date;
        this.status = status;
        this.total_time = total_time;
        this.added_datetime = added_datetime;
        this.logOut_time = logOut_time;
        this.logIn_time = logIn_time;
        this.added_by = added_by;
        this.attendance_id = attendance_id;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getTotal_time() {
        return total_time;
    }

    public String getAdded_datetime() {
        return added_datetime;
    }

    public String getLogOut_time() {
        return logOut_time;
    }

    public String getLogIn_time() {
        return logIn_time;
    }

    public String getAdded_by() {
        return added_by;
    }

    public String getAttendance_id() {
        return attendance_id;
    }
}
