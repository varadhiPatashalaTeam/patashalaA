package in.varadhismartek.patashalaerp.TeacherModule;

public class TeacherHomeworkModle {

    private String completePercentage="",note="",reportDate=""
            ,assignerID="",assigerFName="",assignDate="",
            assignerLName="",startDate="",endDate="",
            studentFName="",studentLName="",StudentId="",homeTitle="",
            homeDescription="",homeClass="", homeSubject="",homeSection="",homeworkID="",DOB="",birthCertificate="";


    public TeacherHomeworkModle(String completePercentage, String note, String reportDate, String assignerID, String assigerFName,  String assignerLName, String startDate, String endDate, String studentFName, String studentLName, String studentId, String homeTitle, String homeDescription, String homeClass, String homeSubject, String homeSection, String homeworkID,String DOB,String birthCertificate,String assignDate ) {
        this.completePercentage = completePercentage;
        this.note = note;
        this.reportDate = reportDate;
        this.assignerID = assignerID;
        this.assigerFName = assigerFName;

        this.assignerLName = assignerLName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.studentFName = studentFName;
        this.studentLName = studentLName;
        StudentId = studentId;
        this.homeTitle = homeTitle;
        this.homeDescription = homeDescription;
        this.homeClass = homeClass;
        this.homeSubject = homeSubject;
        this.homeSection = homeSection;
        this.homeworkID = homeworkID;
        this.DOB = DOB;
        this.birthCertificate = birthCertificate;
        this.assignDate = assignDate;
    }

    public TeacherHomeworkModle(String completePercentage, String note, String reportDate, String assignerID, String assigerFName, String assignerLName, String startDate, String endDate, String studentFName, String studentLName, String studentId, String homeTitle, String homeDescription, String homeClass, String homeSubject, String homeSection) {
        this.completePercentage = completePercentage;
        this.note = note;
        this.reportDate = reportDate;
        this.assignerID = assignerID;
        this.assigerFName = assigerFName;
        this.assignerLName = assignerLName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.studentFName = studentFName;
        this.studentLName = studentLName;
        this.StudentId = studentId;
        this.homeTitle = homeTitle;
        this.homeDescription = homeDescription;
        this.homeClass = homeClass;
        this.homeSubject = homeSubject;
        this.homeSection = homeSection;
    }


    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getBirthCertificate() {
        return birthCertificate;
    }

    public void setBirthCertificate(String birthCertificate) {
        this.birthCertificate = birthCertificate;
    }

    public String getHomeworkID() {
        return homeworkID;
    }

    public void setHomeworkID(String homeworkID) {
        this.homeworkID = homeworkID;
    }

    public String getCompletePercentage() {
        return completePercentage;
    }

    public void setCompletePercentage(String completePercentage) {
        this.completePercentage = completePercentage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getAssignerID() {
        return assignerID;
    }

    public void setAssignerID(String assignerID) {
        this.assignerID = assignerID;
    }

    public String getAssigerFName() {
        return assigerFName;
    }

    public void setAssigerFName(String assigerFName) {
        this.assigerFName = assigerFName;
    }

    public String getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(String assignDate) {
        this.assignDate = assignDate;
    }

    public String getAssignerLName() {
        return assignerLName;
    }

    public void setAssignerLName(String assignerLName) {
        this.assignerLName = assignerLName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStudentFName() {
        return studentFName;
    }

    public void setStudentFName(String studentFName) {
        this.studentFName = studentFName;
    }

    public String getStudentLName() {
        return studentLName;
    }

    public void setStudentLName(String studentLName) {
        this.studentLName = studentLName;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    public String getHomeTitle() {
        return homeTitle;
    }

    public void setHomeTitle(String homeTitle) {
        this.homeTitle = homeTitle;
    }

    public String getHomeDescription() {
        return homeDescription;
    }

    public void setHomeDescription(String homeDescription) {
        this.homeDescription = homeDescription;
    }

    public String getHomeClass() {
        return homeClass;
    }

    public void setHomeClass(String homeClass) {
        this.homeClass = homeClass;
    }

    public String getHomeSubject() {
        return homeSubject;
    }

    public void setHomeSubject(String homeSubject) {
        this.homeSubject = homeSubject;
    }

    public String getHomeSection() {
        return homeSection;
    }

    public void setHomeSection(String homeSection) {
        this.homeSection = homeSection;
    }
}
