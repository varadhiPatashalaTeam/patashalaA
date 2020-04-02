package in.varadhismartek.patashalaerp.DivisionModule;

import java.util.ArrayList;

public class ClassSectionAndDivisionModel {

    private String name;
    private boolean checked;

    private String className;
    private ArrayList<String> listSection;


    private String added_employeeid, added_employee_name, session_serial_no, acd_from_date,acd_to_date, respStartDate, respEndDate;



    public ClassSectionAndDivisionModel(String divisionName, boolean divisionChecked) {
        this.name = divisionName;
        this.checked = divisionChecked;
    }

    public ClassSectionAndDivisionModel(String className, ArrayList<String> listSection) {
        this.className = className;
        this.listSection = listSection;
    }

    public ClassSectionAndDivisionModel(String added_employeeid, String added_employee_name, String session_serial_no, String acd_from_date, String acd_to_date, String respStartDate, String respEndDate) {
        this.added_employeeid = added_employeeid;
        this.added_employee_name = added_employee_name;
        this.session_serial_no = session_serial_no;
        this.acd_from_date = acd_from_date;
        this.acd_to_date = acd_to_date;
        this.respStartDate = respStartDate;
        this.respEndDate = respEndDate;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ArrayList<String> getListSection() {
        return listSection;
    }

    public void setListSection(ArrayList<String> listSection) {
        this.listSection = listSection;
    }

    public String getAdded_employeeid() {
        return added_employeeid;
    }

    public void setAdded_employeeid(String added_employeeid) {
        this.added_employeeid = added_employeeid;
    }

    public String getAdded_employee_name() {
        return added_employee_name;
    }

    public void setAdded_employee_name(String added_employee_name) {
        this.added_employee_name = added_employee_name;
    }

    public String getSession_serial_no() {
        return session_serial_no;
    }

    public void setSession_serial_no(String session_serial_no) {
        this.session_serial_no = session_serial_no;
    }

    public String getAcd_from_date() {
        return acd_from_date;
    }

    public void setAcd_from_date(String acd_from_date) {
        this.acd_from_date = acd_from_date;
    }

    public String getAcd_to_date() {
        return acd_to_date;
    }

    public void setAcd_to_date(String acd_to_date) {
        this.acd_to_date = acd_to_date;
    }

    public String getRespStartDate() {
        return respStartDate;
    }

    public void setRespStartDate(String respStartDate) {
        this.respStartDate = respStartDate;
    }

    public String getRespEndDate() {
        return respEndDate;
    }

    public void setRespEndDate(String respEndDate) {
        this.respEndDate = respEndDate;
    }


    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
