package in.varadhismartek.patashalaerp.DashboardModule.Attendance;

public class EmpAttendanceModel {
    String employee_name,logOut_time,empStatus,total_time,logIn_time,attendance_id,employee_id,employee_image,employee_department;

    public EmpAttendanceModel(String employee_name, String logOut_time, String empStatus, String total_time, String logIn_time, String attendance_id, String employee_id, String employee_image,String employee_department) {
        this.employee_name = employee_name;
        this.logOut_time = logOut_time;
        this.empStatus = empStatus;
        this.total_time = total_time;
        this.logIn_time = logIn_time;
        this.attendance_id = attendance_id;
        this.employee_id = employee_id;
        this.employee_image = employee_image;
        this.employee_department = employee_department;
    }

    public String getEmployee_department() {
        return employee_department;
    }

    public void setEmployee_department(String employee_department) {
        this.employee_department = employee_department;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getLogOut_time() {
        return logOut_time;
    }

    public void setLogOut_time(String logOut_time) {
        this.logOut_time = logOut_time;
    }

    public String getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(String empStatus) {
        this.empStatus = empStatus;
    }

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
    }

    public String getLogIn_time() {
        return logIn_time;
    }

    public void setLogIn_time(String logIn_time) {
        this.logIn_time = logIn_time;
    }

    public String getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(String attendance_id) {
        this.attendance_id = attendance_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_image() {
        return employee_image;
    }

    public void setEmployee_image(String employee_image) {
        this.employee_image = employee_image;
    }
}
