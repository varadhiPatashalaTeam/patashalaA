package in.varadhismartek.patashalaerp.DashboardModule.Homework;

public class MyModel {

    private String className;
    private String sectionName;
    private String workTitle;
    private String fromDate;
    private String toDate;
    private String description;
    private String status;

    private String houseName, teacher, caption , viceCaption, studentNo,colorName;

    public MyModel(String houseName, String teacher, String caption, String viceCaption, String studentNo, String colorName) {
        this.houseName = houseName;
        this.teacher = teacher;
        this.caption = caption;
        this.viceCaption = viceCaption;
        this.studentNo = studentNo;
        this.colorName = colorName;
    }

    public MyModel(String className, String sectionName, String workTitle, String fromDate, String toDate, String description, String status) {
        this.className = className;
        this.sectionName = sectionName;
        this.workTitle = workTitle;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.description = description;
        this.status = status;
    }

    public String getClassName() {
        return className;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWorkTitle() {
        return workTitle;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getViceCaption() {
        return viceCaption;
    }

    public void setViceCaption(String viceCaption) {
        this.viceCaption = viceCaption;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}
