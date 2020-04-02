package in.varadhismartek.patashalaerp.DashboardModule.Homework;

public class HomeworkModel {


    private String homeworkId, homeworkTitle, description, homeClass, section, subject, startDate, endDate;
    private String bookAuthor="", bookCover="", bookName="", bookPageNo="";


    private String divisionClassName;
    private String noOfHomework;
    private boolean cheked;
    private String strAttachment;

    private String urlReference="", urlDescription="";
private Integer count;
    private String student_last_name,homework_title,student_birth_certificate_number,roll_no,
            student_first_name,student_photo,homework_id,completion_percentage,
            start_date,rep_description,end_date,student_id,student_dob;


    public HomeworkModel(Integer count,String student_last_name, String homework_title, String student_birth_certificate_number, String roll_no, String student_first_name, String student_photo, String homework_id, String completion_percentage, String start_date, String rep_description, String end_date, String student_id, String student_dob) {
        this.count = count;
        this.student_last_name = student_last_name;
        this.homework_title = homework_title;
        this.student_birth_certificate_number = student_birth_certificate_number;
        this.roll_no = roll_no;
        this.student_first_name = student_first_name;
        this.student_photo = student_photo;
        this.homework_id = homework_id;
        this.completion_percentage = completion_percentage;
        this.start_date = start_date;
        this.rep_description = rep_description;
        this.end_date = end_date;
        this.student_id = student_id;
        this.student_dob = student_dob;
    }

    public HomeworkModel(String urlReference, String urlDescription) {
        this.urlReference = urlReference;
        this.urlDescription = urlDescription;
    }

    public HomeworkModel(String divisionClassName, String noOfHomework, boolean cheked) {
        this.divisionClassName = divisionClassName;
        this.noOfHomework = noOfHomework;
        this.cheked = cheked;
    }

    public HomeworkModel(String homeworkId, String homeworkTitle, String description, String homeClass, String section, String subject, String startDate, String endDate) {
        this.homeworkId = homeworkId;
        this.homeworkTitle = homeworkTitle;
        this.description = description;
        this.homeClass = homeClass;
        this.section = section;
        this.subject = subject;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public HomeworkModel(String bookAuthor, String bookCover, String bookName, String bookPageNo) {
        this.bookAuthor = bookAuthor;
        this.bookCover = bookCover;
        this.bookName = bookName;
        this.bookPageNo = bookPageNo;
    }


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getStudent_last_name() {
        return student_last_name;
    }

    public void setStudent_last_name(String student_last_name) {
        this.student_last_name = student_last_name;
    }

    public String getHomework_title() {
        return homework_title;
    }

    public void setHomework_title(String homework_title) {
        this.homework_title = homework_title;
    }

    public String getStudent_birth_certificate_number() {
        return student_birth_certificate_number;
    }

    public void setStudent_birth_certificate_number(String student_birth_certificate_number) {
        this.student_birth_certificate_number = student_birth_certificate_number;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getStudent_first_name() {
        return student_first_name;
    }

    public void setStudent_first_name(String student_first_name) {
        this.student_first_name = student_first_name;
    }

    public String getStudent_photo() {
        return student_photo;
    }

    public void setStudent_photo(String student_photo) {
        this.student_photo = student_photo;
    }

    public String getHomework_id() {
        return homework_id;
    }

    public void setHomework_id(String homework_id) {
        this.homework_id = homework_id;
    }

    public String getCompletion_percentage() {
        return completion_percentage;
    }

    public void setCompletion_percentage(String completion_percentage) {
        this.completion_percentage = completion_percentage;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getRep_description() {
        return rep_description;
    }

    public void setRep_description(String rep_description) {
        this.rep_description = rep_description;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_dob() {
        return student_dob;
    }

    public void setStudent_dob(String student_dob) {
        this.student_dob = student_dob;
    }

    public HomeworkModel(String strAttachment) {
        this.strAttachment= strAttachment;
    }

    public String getStrAttachment() {
        return strAttachment;
    }

    public void setStrAttachment(String strAttachment) {
        this.strAttachment = strAttachment;
    }

    public String getUrlReference() {
        return urlReference;
    }

    public void setUrlReference(String urlReference) {
        this.urlReference = urlReference;
    }

    public String getUrlDescription() {
        return urlDescription;
    }

    public void setUrlDescription(String urlDescription) {
        this.urlDescription = urlDescription;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookCover() {
        return bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookPageNo() {
        return bookPageNo;
    }

    public void setBookPageNo(String bookPageNo) {
        this.bookPageNo = bookPageNo;
    }

    public String getDivisionClassName() {
        return divisionClassName;
    }

    public void setDivisionClassName(String divisionClassName) {
        this.divisionClassName = divisionClassName;
    }

    public String getNoOfHomework() {
        return noOfHomework;
    }

    public void setNoOfHomework(String noOfHomework) {
        this.noOfHomework = noOfHomework;
    }

    public boolean isCheked() {
        return cheked;
    }

    public void setCheked(boolean cheked) {
        this.cheked = cheked;
    }

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getHomeworkTitle() {
        return homeworkTitle;
    }

    public void setHomeworkTitle(String homeworkTitle) {
        this.homeworkTitle = homeworkTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomeClass() {
        return homeClass;
    }

    public void setHomeClass(String homeClass) {
        this.homeClass = homeClass;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
}
