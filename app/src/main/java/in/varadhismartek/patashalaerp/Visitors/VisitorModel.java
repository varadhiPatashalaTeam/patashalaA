package in.varadhismartek.patashalaerp.Visitors;

public class VisitorModel {

    private String entry_time, added_employeeid, visitor_id, entry_date, added_by_first_name
            ,purpose, visitor_uuid, staff_id, added_datetime, visitor_name, visitor_photo, added_by_last_name;

    private String photo, first_name, last_name, department, role, employeeID,custom_employee_id;


    public VisitorModel(String entry_time, String added_employeeid, String visitor_id, String entry_date,
                        String added_by_first_name, String purpose, String visitor_uuid, String staff_id,
                        String added_datetime, String visitor_name, String visitor_photo, String added_by_last_name) {

        this.entry_time = entry_time;
        this.added_employeeid = added_employeeid;
        this.visitor_id = visitor_id;
        this.entry_date = entry_date;
        this.added_by_first_name = added_by_first_name;
        this.purpose = purpose;
        this.visitor_uuid = visitor_uuid;
        this.staff_id = staff_id;
        this.added_datetime = added_datetime;
        this.visitor_name = visitor_name;
        this.visitor_photo = visitor_photo;
        this.added_by_last_name = added_by_last_name;
    }


    public String getDepartment() {
        return department;
    }

    public String getRole() {
        return role;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public VisitorModel(String photo, String first_name, String last_name, String department,
                        String role, String employeeID, String custom_employee_id){
        this.photo = photo;
        this.first_name = first_name;
        this.last_name = last_name;
        this.department = department;
        this.role = role;
        this.employeeID = employeeID;
        this.custom_employee_id = custom_employee_id;


    }

    public String getCustom_employee_id() {
        return custom_employee_id;
    }

    public String getPhoto() {
        return photo;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEntry_time() {
        return entry_time;
    }

    public String getAdded_employeeid() {
        return added_employeeid;
    }

    public String getVisitor_id() {
        return visitor_id;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public String getAdded_by_first_name() {
        return added_by_first_name;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getVisitor_uuid() {
        return visitor_uuid;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public String getAdded_datetime() {
        return added_datetime;
    }

    public String getVisitor_name() {
        return visitor_name;
    }

    public String getVisitor_photo() {
        return visitor_photo;
    }

    public String getAdded_by_last_name() {
        return added_by_last_name;
    }
}
