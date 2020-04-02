package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

public class AssesmentModel {


    private String grade;
    private Long minMarks;
    private Long maxMarks;

    private String name;
    private String count;

    private String marks;
    private boolean isSelect;

    private String examType,divisionNmae,className,subjectName,examDuration;

    private  String sportsName, mentors, mentorPosition_name, maxPlayers, supportPlayers, sportName;

    public AssesmentModel(String mentors, String mentorPosition_name, String maxPlayers, String supportPlayers, String sportName) {
        this.mentors = mentors;
        this.mentorPosition_name = mentorPosition_name;
        this.maxPlayers = maxPlayers;
        this.supportPlayers = supportPlayers;
        this.sportName = sportName;
    }

    public AssesmentModel(Long minMarks, Long maxMarks, String examType, String divisionNmae,
                          String className, String subjectName, String examDuration) {
        this.minMarks = minMarks;
        this.maxMarks = maxMarks;
        this.examType = examType;
        this.divisionNmae = divisionNmae;
        this.className = className;
        this.subjectName = subjectName;
        this.examDuration = examDuration;
    }

    public String getSportsName() {
        return sportsName;
    }

    public void setSportsName(String sportsName) {
        this.sportsName = sportsName;
    }

    public String getMentors() {
        return mentors;
    }

    public void setMentors(String mentors) {
        this.mentors = mentors;
    }

    public String getMentorPosition_name() {
        return mentorPosition_name;
    }

    public void setMentorPosition_name(String mentorPosition_name) {
        this.mentorPosition_name = mentorPosition_name;
    }

    public String getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(String maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getSupportPlayers() {
        return supportPlayers;
    }

    public void setSupportPlayers(String supportPlayers) {
        this.supportPlayers = supportPlayers;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getDivisionNmae() {
        return divisionNmae;
    }

    public void setDivisionNmae(String divisionNmae) {
        this.divisionNmae = divisionNmae;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getExamDuration() {
        return examDuration;
    }

    public void setExamDuration(String examDuration) {
        this.examDuration = examDuration;
    }

    public AssesmentModel(String grade, Long minMarks, Long maxMarks) {
        this.grade = grade;
        this.minMarks = minMarks;
        this.maxMarks = maxMarks;
    }
    public AssesmentModel(String name, String marks, boolean isSelect) {
        this.name = name;
        this.marks = marks;
        this.isSelect = isSelect;
    }

    public AssesmentModel(String name, String count){
        this.name = name;
        this.count = count;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }


    public String getGrade() {
        return grade;
    }

    public Long getMinMarks() {
        return minMarks;
    }

    public void setMinMarks(Long minMarks) {
        this.minMarks = minMarks;
    }

    public Long getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(Long maxMarks) {
        this.maxMarks = maxMarks;
    }
}
