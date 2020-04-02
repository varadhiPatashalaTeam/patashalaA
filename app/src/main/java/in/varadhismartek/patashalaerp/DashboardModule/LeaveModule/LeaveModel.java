package in.varadhismartek.patashalaerp.DashboardModule.LeaveModule;

public class LeaveModel {

    private String leavetype, emp_name, from_date,status, subject, toDate, appliedDate;
    private boolean checked;

    private String leaveCode, NoOfLeave, noticePeriod, availability, leaveID;

    private String totalLeave, usedLeave, leftLeave, schoolId, empID, empPhoto;

    // This for school leave barrier api
    public LeaveModel(String leavetype, String leaveCode, String noOfLeave, String noticePeriod, String availability, String status, String leaveID) {
        this.leavetype = leavetype;
        this.leaveCode = leaveCode;
        this.NoOfLeave = noOfLeave;
        this.noticePeriod = noticePeriod;
        this.availability = availability;
        this.status = status;
        this.leaveID = leaveID;
    }

    // leave barrier api
    public LeaveModel(String leavetype, String leaveID, boolean checked) {
        this.leavetype = leavetype;
        this.leaveID = leaveID;
        this.checked = checked;
    }


    // this is for teacher List inbox
    public LeaveModel(String leavetype, String subject, String appliedDate, String from_date,
                      String toDate, String status, String leaveID, String empID){
        this.leavetype = leavetype;
        this.subject = subject;
        this.appliedDate = appliedDate;
        this.from_date = from_date;
        this.toDate = toDate;
        this.status = status;
        this.leaveID = leaveID;
        this.empID = empID;
    }

    // this is for leave Statement

    public LeaveModel(String leavetype, String totalLeave, String usedLeave, String leftLeave){
        this.leavetype = leavetype;
        this.totalLeave = totalLeave;
        this.usedLeave = usedLeave;
        this.leftLeave = leftLeave;

    }

    // this is for admin list

    public LeaveModel(String emp_name, String from_date, String empPhoto, String status, String leavetype,
                      String empID, String toDate, String schoolId, String subject, String leaveID){

        this.emp_name = emp_name;
        this.from_date = from_date;
        this.empPhoto = empPhoto;
        this.leavetype = leavetype;
        this.empID = empID;
        this.toDate = toDate;
        this.status = status;
        this.schoolId = schoolId;
        this.subject = subject;
        this.leaveID = leaveID;
    }



    public String getLeavetype() {
        return leavetype;
    }


    public String getEmp_name() {
        return emp_name;
    }

    public String getFrom_date() {
        return from_date;
    }

    public String getStatus() {
        return status;
    }


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getLeaveCode() {
        return leaveCode;
    }

    public String getNoOfLeave() {
        return NoOfLeave;
    }

    public String getNoticePeriod() {
        return noticePeriod;
    }

    public String getAvailability() {
        return availability;
    }

    public String getSubject() {
        return subject;
    }

    public String getToDate() {
        return toDate;
    }

    public String getLeaveID() {
        return leaveID;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public String getTotalLeave() {
        return totalLeave;
    }

    public String getUsedLeave() {
        return usedLeave;
    }

    public String getLeftLeave() {
        return leftLeave;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public String getEmpID() {
        return empID;
    }

    public String getEmpPhoto() {
        return empPhoto;
    }
}
