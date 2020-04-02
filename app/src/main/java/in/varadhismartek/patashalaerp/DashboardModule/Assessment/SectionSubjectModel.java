package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import java.util.ArrayList;

public class SectionSubjectModel {
    private String SectionName,subjectName;
    private String subCode,subType,divisionName,className;
    private boolean isselect;



    public SectionSubjectModel(String sectionName, String subjectName, String subCode, String subType, String divisionName, String className, boolean isselect) {
        SectionName = sectionName;
        this.subjectName = subjectName;
        this.subCode = subCode;
        this.subType = subType;
        this.divisionName = divisionName;
        this.className = className;
        this.isselect = isselect;
    }

    public SectionSubjectModel(String subjectName, String subType) {
        this.subjectName = subjectName;
        this.subType = subType;
    }


    private ArrayList<SubjectModel> subjectModels;

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isIsselect() {
        return isselect;
    }

    public void setIsselect(boolean isselect) {
        this.isselect = isselect;
    }

    public String getSectionName() {
        return SectionName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setSectionName(String sectionName) {
        SectionName = sectionName;
    }

    public ArrayList<SubjectModel> getSubjectModels() {
        return subjectModels;
    }

    public void setSubjectModels(ArrayList<SubjectModel> subjectModels) {
        this.subjectModels = subjectModels;
    }

    private class SubjectModel {

        private String subject_type,status,subject_code;

        public SubjectModel(String subject_type, String status, String subject_code) {
            this.subject_type = subject_type;
            this.status = status;
            this.subject_code = subject_code;
        }

        public String getSubject_type() {
            return subject_type;
        }

        public void setSubject_type(String subject_type) {
            this.subject_type = subject_type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSubject_code() {
            return subject_code;
        }

        public void setSubject_code(String subject_code) {
            this.subject_code = subject_code;
        }
    }
}
