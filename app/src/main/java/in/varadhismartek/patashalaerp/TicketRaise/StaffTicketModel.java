package in.varadhismartek.patashalaerp.TicketRaise;

import java.util.ArrayList;

public class StaffTicketModel {


    //--------------------Ticket Inbox----------------------------------------------------------
    private String updated_datetime, ticket_datetime,ticket_number,ticket_subject,
    ticket_department,issue_title,ticket_priority,ticket_id, ticket_level,ticket_type, ticket_status,empCustomId;

    //--------------------View Level Employee List---------------------------------------------
    private String role, department, custom_employee_id, wing, phone_number, employee_uuid;



    //--------------------Ticket Inbox----------------------------------------------------------
    public StaffTicketModel(String updated_datetime, String ticket_datetime, String ticket_number, String ticket_subject,
                            String ticket_department , String issue_title , String ticket_priority, String ticket_id,
                            String ticket_level, String ticket_type , String ticket_status,String empCustomId, String tag) {

        this.updated_datetime = updated_datetime;
        this.ticket_datetime = ticket_datetime;
        this.ticket_number = ticket_number;
        this.ticket_subject = ticket_subject;
        this.ticket_department = ticket_department;
        this.issue_title = issue_title;
        this.ticket_priority = ticket_priority;
        this.ticket_id = ticket_id;
        this.ticket_level = ticket_level;
        this.ticket_type = ticket_type;
        this.ticket_status = ticket_status;
        this.empCustomId=empCustomId;

    }

    //--------------------View Level Employee List---------------------------------------------
    public StaffTicketModel(String role, String aadhar_no, String last_name, String middle_name, String first_name,
                            String department, String custom_employee_id, String wing, String employee_photo,
                            String phone_number, String email, String employee_uuid) {

        this.role = role;
        this.aadhar_no = aadhar_no;
        this.last_name = last_name;
        this.middle_name = middle_name;
        this.first_name = first_name;
        this.department = department;
        this.custom_employee_id = custom_employee_id;
        this.wing = wing;
        this.employee_photo = employee_photo;
        this.phone_number = phone_number;
        this.email = email;
        this.employee_uuid = employee_uuid;
    }

    public String getEmpCustomId() {
        return empCustomId;
    }

    public String getUpdated_datetime() {
        return updated_datetime;
    }

    public String getTicket_datetime() {
        return ticket_datetime;
    }

    public String getTicket_number() {
        return ticket_number;
    }

    public String getTicket_subject() {
        return ticket_subject;
    }

    public String getTicket_department() {
        return ticket_department;
    }

    public String getIssue_title() {
        return issue_title;
    }

    public String getTicket_priority() {
        return ticket_priority;
    }

    public String getTicket_id() {
        return ticket_id;
    }

    public String getTicket_level() {
        return ticket_level;
    }

    public String getTicket_type() {
        return ticket_type;
    }

    public String getTicket_status() {
        return ticket_status;
    }

    private String name,id_No,date,description;
    private ArrayList<String> list=new ArrayList<>();

    public StaffTicketModel(String name, String id_No, String date, String description, ArrayList<String> list) {
        this.name = name;
        this.id_No = id_No;
        this.date = date;
        this.description = description;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_No() {
        return id_No;
    }

    public void setId_No(String id_No) {
        this.id_No = id_No;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getList() {
        return list;
    }



    private String first_name, employee, escalated_status, employee_photo, last_name, ticket_message,
            pan_no, middle_name, aadhar_no, email, mobile, response_datetime, escalated_level;
    private ArrayList<String> images;

    public StaffTicketModel(String first_name, String employee, String escalated_status, String employee_photo, String last_name,
                            String ticket_message, String pan_no, String middle_name, String aadhar_no, String email,
                            String mobile, String response_datetime, String escalated_level, ArrayList<String> images){

        this.first_name = first_name;
        this.employee = employee;
        this.escalated_status = escalated_status;
        this.employee_photo = employee_photo;
        this.last_name = last_name;
        this.ticket_message = ticket_message;
        this.pan_no = pan_no;
        this.middle_name = middle_name;
        this.aadhar_no = aadhar_no;
        this.email = email;
        this.mobile = mobile;
        this.response_datetime = response_datetime;
        this.escalated_level = escalated_level;
        this.images = images;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getEmployee() {
        return employee;
    }

    public String getEscalated_status() {
        return escalated_status;
    }

    public String getEmployee_photo() {
        return employee_photo;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getTicket_message() {
        return ticket_message;
    }

    public String getPan_no() {
        return pan_no;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public String getAadhar_no() {
        return aadhar_no;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getResponse_datetime() {
        return response_datetime;
    }

    public String getEscalated_level() {
        return escalated_level;
    }

    public ArrayList<String> getImages() {
        return images;
    }


    public String getRole() {
        return role;
    }

    public String getDepartment() {
        return department;
    }

    public String getCustom_employee_id() {
        return custom_employee_id;
    }

    public String getWing() {
        return wing;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmployee_uuid() {
        return employee_uuid;
    }
}
