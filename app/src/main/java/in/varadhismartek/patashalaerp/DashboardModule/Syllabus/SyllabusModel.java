package in.varadhismartek.patashalaerp.DashboardModule.Syllabus;

import java.io.Serializable;
import java.util.ArrayList;

public class SyllabusModel implements Serializable {

    String classes,syllabus_title,added_by,syllabus_uuid,subject,school_id,
            sections,exam_type,added_datetime,description;

    ArrayList<String> imageList;

    String className;
    ArrayList<String> sectionList;

    public SyllabusModel(String className, ArrayList<String> sectionList) {
        this.className = className;
        this.sectionList = sectionList;
    }

    public SyllabusModel(String classes, String syllabus_title, String added_by, String syllabus_uuid,
                         String subject, String school_id, String sections,String exam_type, String added_datetime,
                         String description, ArrayList<String> imageList) {
        this.classes = classes;
        this.syllabus_title = syllabus_title;
        this.added_by = added_by;
        this.syllabus_uuid = syllabus_uuid;
        this.subject = subject;
        this.school_id = school_id;
        this.sections = sections;
        this.exam_type = exam_type;
        this.added_datetime = added_datetime;
        this.description = description;
        this.imageList = imageList;
    }


    public String getExam_type() {
        return exam_type;
    }

    public void setExam_type(String exam_type) {
        this.exam_type = exam_type;
    }

    public String getClassName() {
        return className;
    }

    public ArrayList<String> getSectionList() {
        return sectionList;
    }

    public String getClasses() {
        return classes;
    }

    public String getSyllabus_title() {
        return syllabus_title;
    }

    public String getAdded_by() {
        return added_by;
    }

    public String getSyllabus_uuid() {
        return syllabus_uuid;
    }

    public String getSubject() {
        return subject;
    }

    public String getSchool_id() {
        return school_id;
    }

    public String getSections() {
        return sections;
    }

    public String getAdded_datetime() {
        return added_datetime;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }
}
