package in.varadhismartek.patashalaerp.StudentModule;

public class StudentModel {
    private String strDivision,strClass,strSection,strFirstName,strLastName,strDOB,strStudentID,strCertificateNo,strCustomId,
            strStatus,strdeleted,strPhoto,acdaemicFromDate,acdaemicToDate,sessionFromDate,sessionToDate,
            session_serial_no,model_type;

    private String added_by_emp_firstname,session_to,session,added_by,student_last_name,
            student_id,student_birth_certificate_number,student_first_name,attStatus,
            student_dob,added_datetime, session_from,student_attendance_id,photo,
            added_by_emp_lastname,updated_datetime;


    private  String leave_type,  subject,  to_date,  from_date,  message;


    public StudentModel(String added_by_emp_firstname, String session_to, String session, String added_by, String student_last_name, String student_id, String student_birth_certificate_number, String student_first_name, String attStatus, String student_dob, String added_datetime, String session_from, String student_attendance_id, String photo, String added_by_emp_lastname, String updated_datetime) {
        this.added_by_emp_firstname = added_by_emp_firstname;
        this.session_to = session_to;
        this.session = session;
        this.added_by = added_by;
        this.student_last_name = student_last_name;
        this.student_id = student_id;
        this.student_birth_certificate_number = student_birth_certificate_number;
        this.student_first_name = student_first_name;
        this.attStatus = attStatus;
        this.student_dob = student_dob;
        this.added_datetime = added_datetime;
        this.session_from = session_from;
        this.student_attendance_id = student_attendance_id;
        this.photo = photo;
        this.added_by_emp_lastname = added_by_emp_lastname;
        this.updated_datetime = updated_datetime;
    }

    public StudentModel(String strFirstName, String strLastName, String strDOB, String strStudentID,
                        String strCertificateNo, String strCustomId, String strPhoto) {
        this.strFirstName = strFirstName;
        this.strLastName = strLastName;
        this.strDOB = strDOB;
        this.strStudentID = strStudentID;
        this.strCertificateNo = strCertificateNo;
        this.strCustomId = strCustomId;
        this.strPhoto = strPhoto;
    }

    public StudentModel(String strDivision, String strClass, String strSection, String strFirstName, String strLastName, String strDOB, String strStudentID, String strCertificateNo, String strStatus, String strdeleted, String strPhoto) {
        this.strDivision = strDivision;
        this.strClass = strClass;
        this.strSection = strSection;
        this.strFirstName = strFirstName;
        this.strLastName = strLastName;
        this.strDOB = strDOB;
        this.strStudentID = strStudentID;
        this.strCertificateNo = strCertificateNo;
        this.strStatus = strStatus;
        this.strdeleted = strdeleted;
        this.strPhoto = strPhoto;
    }

    public StudentModel(String leave_type, String subject, String to_date, String from_date, String message, String strSection, String student_id,String strClass, String student_first_name) {
        this.leave_type = leave_type;
        this.subject = subject;
        this.to_date = to_date;
        this.from_date = from_date;
        this.message = message;
        this.strSection = strSection;
        this.student_id = student_id;
        this.strClass = strClass;
        this.student_first_name = student_first_name;
    }


    public StudentModel(String strDivision, String strClass, String strSection, String strFirstName,
                        String strLastName, String strDOB, String strStudentID,
                        String strCertificateNo, String strCustomId, String strStatus,
                        String strdeleted, String strPhoto, String acdaemicFromDate,
                        String acdaemicToDate, String sessionFromDate, String sessionToDate,
                        String session_serial_no, String model_type) {

        this.strDivision = strDivision;
        this.strClass = strClass;
        this.strSection = strSection;
        this.strFirstName = strFirstName;
        this.strLastName = strLastName;
        this.strDOB = strDOB;
        this.strStudentID = strStudentID;
        this.strCertificateNo = strCertificateNo;
        this.strCustomId = strCustomId;
        this.strStatus = strStatus;
        this.strdeleted = strdeleted;
        this.strPhoto = strPhoto;
        this.acdaemicFromDate = acdaemicFromDate;
        this.acdaemicToDate = acdaemicToDate;
        this.sessionFromDate = sessionFromDate;
        this.sessionToDate = sessionToDate;
        this.session_serial_no = session_serial_no;
        this.model_type = model_type;
    }




    public String getAcdaemicFromDate() {
        return acdaemicFromDate;
    }

    public void setAcdaemicFromDate(String acdaemicFromDate) {
        this.acdaemicFromDate = acdaemicFromDate;
    }

    public String getAcdaemicToDate() {
        return acdaemicToDate;
    }

    public void setAcdaemicToDate(String acdaemicToDate) {
        this.acdaemicToDate = acdaemicToDate;
    }

    public String getSessionFromDate() {
        return sessionFromDate;
    }

    public void setSessionFromDate(String sessionFromDate) {
        this.sessionFromDate = sessionFromDate;
    }

    public String getSessionToDate() {
        return sessionToDate;
    }

    public void setSessionToDate(String sessionToDate) {
        this.sessionToDate = sessionToDate;
    }

    public String getSession_serial_no() {
        return session_serial_no;
    }

    public void setSession_serial_no(String session_serial_no) {
        this.session_serial_no = session_serial_no;
    }

    public String getModel_type() {
        return model_type;
    }

    public void setModel_type(String model_type) {
        this.model_type = model_type;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_first_name() {
        return student_first_name;
    }

    public void setStudent_first_name(String student_first_name) {
        this.student_first_name = student_first_name;
    }

    public String getAdded_by_emp_firstname() {
        return added_by_emp_firstname;
    }

    public void setAdded_by_emp_firstname(String added_by_emp_firstname) {
        this.added_by_emp_firstname = added_by_emp_firstname;
    }

    public String getSession_to() {
        return session_to;
    }

    public void setSession_to(String session_to) {
        this.session_to = session_to;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getStudent_last_name() {
        return student_last_name;
    }

    public void setStudent_last_name(String student_last_name) {
        this.student_last_name = student_last_name;
    }

    public String getStudent_birth_certificate_number() {
        return student_birth_certificate_number;
    }

    public void setStudent_birth_certificate_number(String student_birth_certificate_number) {
        this.student_birth_certificate_number = student_birth_certificate_number;
    }

    public String getAttStatus() {
        return attStatus;
    }

    public void setAttStatus(String attStatus) {
        this.attStatus = attStatus;
    }

    public String getStudent_dob() {
        return student_dob;
    }

    public void setStudent_dob(String student_dob) {
        this.student_dob = student_dob;
    }

    public String getAdded_datetime() {
        return added_datetime;
    }

    public void setAdded_datetime(String added_datetime) {
        this.added_datetime = added_datetime;
    }

    public String getSession_from() {
        return session_from;
    }

    public void setSession_from(String session_from) {
        this.session_from = session_from;
    }

    public String getStudent_attendance_id() {
        return student_attendance_id;
    }

    public void setStudent_attendance_id(String student_attendance_id) {
        this.student_attendance_id = student_attendance_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAdded_by_emp_lastname() {
        return added_by_emp_lastname;
    }

    public void setAdded_by_emp_lastname(String added_by_emp_lastname) {
        this.added_by_emp_lastname = added_by_emp_lastname;
    }

    public String getUpdated_datetime() {
        return updated_datetime;
    }

    public void setUpdated_datetime(String updated_datetime) {
        this.updated_datetime = updated_datetime;
    }

    public String getStrCustomId() {
        return strCustomId;
    }

    public void setStrCustomId(String strCustomId) {
        this.strCustomId = strCustomId;
    }

    public String getLeave_type() {
        return leave_type;
    }

    public void setLeave_type(String leave_type) {
        this.leave_type = leave_type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStrDivision() {
        return strDivision;
    }

    public void setStrDivision(String strDivision) {
        this.strDivision = strDivision;
    }

    public String getStrClass() {
        return strClass;
    }

    public void setStrClass(String strClass) {
        this.strClass = strClass;
    }

    public String getStrSection() {
        return strSection;
    }

    public void setStrSection(String strSection) {
        this.strSection = strSection;
    }

    public String getStrFirstName() {
        return strFirstName;
    }

    public void setStrFirstName(String strFirstName) {
        this.strFirstName = strFirstName;
    }

    public String getStrLastName() {
        return strLastName;
    }

    public void setStrLastName(String strLastName) {
        this.strLastName = strLastName;
    }

    public String getStrDOB() {
        return strDOB;
    }

    public void setStrDOB(String strDOB) {
        this.strDOB = strDOB;
    }

    public String getStrStudentID() {
        return strStudentID;
    }

    public void setStrStudentID(String strStudentID) {
        this.strStudentID = strStudentID;
    }

    public String getStrCertificateNo() {
        return strCertificateNo;
    }

    public void setStrCertificateNo(String strCertificateNo) {
        this.strCertificateNo = strCertificateNo;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public String getStrdeleted() {
        return strdeleted;
    }

    public void setStrdeleted(String strdeleted) {
        this.strdeleted = strdeleted;
    }

    public String getStrPhoto() {
        return strPhoto;
    }

    public void setStrPhoto(String strPhoto) {
        this.strPhoto = strPhoto;
    }
}
