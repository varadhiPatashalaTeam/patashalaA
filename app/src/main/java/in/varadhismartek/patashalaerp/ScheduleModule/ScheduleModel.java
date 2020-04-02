package in.varadhismartek.patashalaerp.ScheduleModule;

import java.io.Serializable;

public class ScheduleModel implements Serializable {

    private String date, type, division;

    private String strclasses,strsection,note, added_datetime, to_date, schedule_title,
            schedule_image, added_by_employee_lastname, strdivision,
            added_by_employee_firstname, schedule_type, from_date, academic_start_date,academic_end_date,
            session_serial_no,strType,added_employeeid,academicStart,academicEnd,sessionNo,className,start_time,exam_duration;



    public ScheduleModel(String note, String added_datetime, String to_date, String schedule_title,
                               String schedule_image, String added_by_employee_lastname, String strdivision,
                               String added_by_employee_firstname, String schedule_type, String from_date,
                         String academic_start_date,String academic_end_date,String session_serial_no,String strType) {
        this.note = note;
        this.added_datetime = added_datetime;
        this.to_date = to_date;
        this.schedule_title = schedule_title;
        this.schedule_image = schedule_image;
        this.added_by_employee_lastname = added_by_employee_lastname;
        this.strdivision = strdivision;
        this.added_by_employee_firstname = added_by_employee_firstname;
        this.schedule_type = schedule_type;
        this.from_date = from_date;
        this.academic_start_date = academic_start_date;
        this.academic_end_date = academic_end_date;
        this.session_serial_no = session_serial_no;
        this.strType = strType;
    }


    // new Module
    public ScheduleModel(String added_employeeid,String note,String added_by_employee_lastname ,String to_date,
                         String added_by_employee_firstname,String added_datetime,String schedule_title ,
                         String schedule_type,String division, String from_date ,String schedule_image,
                         String academicStart, String academicEnd, String sessionNo, String typeTest){

        this.added_employeeid = added_employeeid;
        this.note = note;
        this.added_by_employee_lastname = added_by_employee_lastname;
        this.to_date = to_date;
        this.added_by_employee_firstname = added_by_employee_firstname;
        this.added_datetime = added_datetime;
        this.schedule_title = schedule_title;
        this.division = division;
        this.schedule_type = schedule_type;
        this.from_date = from_date;
        this.schedule_image = schedule_image;
        this.academicStart = academicStart;
        this.academicEnd = academicEnd;
        this.sessionNo = sessionNo;
    }


    public ScheduleModel(String date, String type, String division) {
        this.date = date;
        this.type = type;
        this.division = division;
    }

    public ScheduleModel(String strclasses, String strsection, String note, String added_datetime,
                         String to_date, String schedule_title, String schedule_image,
                         String added_by_employee_lastname, String strdivision, String added_by_employee_firstname,
                         String schedule_type, String from_date, String academic_start_date,
                         String academic_end_date, String session_serial_no, String strType) {
        this.strclasses = strclasses;
        this.strsection = strsection;
        this.note = note;
        this.added_datetime = added_datetime;
        this.to_date = to_date;
        this.schedule_title = schedule_title;
        this.schedule_image = schedule_image;
        this.added_by_employee_lastname = added_by_employee_lastname;
        this.strdivision = strdivision;
        this.added_by_employee_firstname = added_by_employee_firstname;
        this.schedule_type = schedule_type;
        this.from_date = from_date;
        this.academic_start_date = academic_start_date;
        this.academic_end_date = academic_end_date;
        this.session_serial_no = session_serial_no;
        this.strType = strType;

    }

    //Exam Row View
    ScheduleModel(String className,String section,String start_time,String subject,String exam_duration, String message){
        this.className = className;
        this.section = section;
        this.start_time = start_time;
        this.subject = subject;
        this.exam_duration = exam_duration;
        this.message = message;
    }

    public String getClassName() {
        return className;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getExam_duration() {
        return exam_duration;
    }

    public String getAdded_employeeid() {
        return added_employeeid;
    }

    public String getAcademicStart() {
        return academicStart;
    }

    public String getAcademicEnd() {
        return academicEnd;
    }

    public String getSessionNo() {
        return sessionNo;
    }

    public String getStrclasses() {
        return strclasses;
    }

    public void setStrclasses(String strclasses) {
        this.strclasses = strclasses;
    }

    public String getStrsection() {
        return strsection;
    }

    public void setStrsection(String strsection) {
        this.strsection = strsection;
    }

    public String getSession_serial_no() {
        return session_serial_no;
    }

    public void setSession_serial_no(String session_serial_no) {
        this.session_serial_no = session_serial_no;
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }

    public String getAcademic_start_date() {
        return academic_start_date;
    }

    public void setAcademic_start_date(String academic_start_date) {
        this.academic_start_date = academic_start_date;
    }

    public String getAcademic_end_date() {
        return academic_end_date;
    }

    public void setAcademic_end_date(String academic_end_date) {
        this.academic_end_date = academic_end_date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAdded_datetime() {
        return added_datetime;
    }

    public void setAdded_datetime(String added_datetime) {
        this.added_datetime = added_datetime;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getSchedule_title() {
        return schedule_title;
    }

    public void setSchedule_title(String schedule_title) {
        this.schedule_title = schedule_title;
    }

    public String getSchedule_image() {
        return schedule_image;
    }

    public void setSchedule_image(String schedule_image) {
        this.schedule_image = schedule_image;
    }

    public String getAdded_by_employee_lastname() {
        return added_by_employee_lastname;
    }

    public void setAdded_by_employee_lastname(String added_by_employee_lastname) {
        this.added_by_employee_lastname = added_by_employee_lastname;
    }

    public String getStrdivision() {
        return strdivision;
    }

    public void setStrdivision(String strdivision) {
        this.strdivision = strdivision;
    }

    public String getAdded_by_employee_firstname() {
        return added_by_employee_firstname;
    }

    public void setAdded_by_employee_firstname(String added_by_employee_firstname) {
        this.added_by_employee_firstname = added_by_employee_firstname;
    }

    public String getSchedule_type() {
        return schedule_type;
    }

    public void setSchedule_type(String schedule_type) {
        this.schedule_type = schedule_type;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getDivision() {
        return division;
    }



    String scheduleType, classess, section, message, image, time, toDate,eventTitle,addedFirstName,subject;

    public ScheduleModel(String scheduleType, String type, String date, String toDate, String time, String classess, String section, String division,
                         String message, String image, String addedFirstName, String eventTitle, String subject){

        this.scheduleType = scheduleType;
        this.type = type;
        this.date = date;
        this.toDate = toDate;
        this.time = time;
        this.classess = classess;
        this.section = section;
        this.division = division;
        this.message = message;
        this.image = image;
        this.addedFirstName = addedFirstName;
        this.eventTitle = eventTitle;
        this.subject = subject;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public String getClassess() {
        return classess;
    }

    public String getSection() {
        return section;
    }

    public String getMessage() {
        return message;
    }

    public String getImage() {
        return image;
    }

    public String getTime() {
        return time;
    }

    public String getToDate() {
        return toDate;
    }

    public String getAddedFirstName() {
        return addedFirstName;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getSubject() {
        return subject;
    }
}
