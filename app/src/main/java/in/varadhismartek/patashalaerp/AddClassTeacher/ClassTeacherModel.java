package in.varadhismartek.patashalaerp.AddClassTeacher;

public class ClassTeacherModel {

    private String photo, first_name, last_name,  department, role,  employeeID;

    ClassTeacherModel(String photo, String first_name, String last_name, String department,
                      String role, String employeeID){
        this.photo = photo;
        this.first_name = first_name;
        this.last_name = last_name;
        this.department = department;
        this.role = role;
        this.employeeID = employeeID;
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

    public String getDepartment() {
        return department;
    }

    public String getRole() {
        return role;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    String className, sectionName, teacher_custom_id, teacher_first_name,teacher_last_name, teacher_uuid;
    boolean status;

    public ClassTeacherModel(String className, String sectionName, String teacher_custom_id,
                             String teacher_first_name, String teacher_last_name, boolean status, String teacher_uuid) {
        this.className = className;
        this.sectionName = sectionName;
        this.teacher_custom_id = teacher_custom_id;
        this.teacher_first_name = teacher_first_name;
        this.teacher_last_name = teacher_last_name;
        this.status = status;
        this.teacher_uuid = teacher_uuid;
    }

    public String getClassName() {
        return className;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getTeacher_custom_id() {
        return teacher_custom_id;
    }

    public String getTeacher_first_name() {
        return teacher_first_name;
    }

    public String getTeacher_last_name() {
        return teacher_last_name;
    }

    public String getTeacher_uuid() {
        return teacher_uuid;
    }

    public boolean isStatus() {
        return status;
    }
}
