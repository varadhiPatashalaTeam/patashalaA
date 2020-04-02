package in.varadhismartek.patashalaerp.Timetable;

public class TimetableDetails {
    String subject,teacher,strStart_time,strEnd_time,strDuration;

    public TimetableDetails(String subject, String teacher, String strStart_time, String strEnd_time, String strDuration) {
        this.subject = subject;
        this.teacher = teacher;
        this.strStart_time = strStart_time;
        this.strEnd_time = strEnd_time;
        this.strDuration = strDuration;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getStrStart_time() {
        return strStart_time;
    }

    public void setStrStart_time(String strStart_time) {
        this.strStart_time = strStart_time;
    }

    public String getStrEnd_time() {
        return strEnd_time;
    }

    public void setStrEnd_time(String strEnd_time) {
        this.strEnd_time = strEnd_time;
    }

    public String getStrDuration() {
        return strDuration;
    }

    public void setStrDuration(String strDuration) {
        this.strDuration = strDuration;
    }
}
