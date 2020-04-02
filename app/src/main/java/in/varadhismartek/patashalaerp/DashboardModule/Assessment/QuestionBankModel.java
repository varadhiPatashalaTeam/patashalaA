package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import java.util.ArrayList;

public class QuestionBankModel {
    private String className,section,subject,quesBankTitle,desc,quesBankId,addedBy,addedDate;
    private ArrayList<String> questionBankAttachList;

    public QuestionBankModel(String className, String section, String subject, String quesBankTitle, String desc, String quesBankId, String addedBy, String addedDate, ArrayList<String> questionBankAttachList) {
        this.className = className;
        this.section = section;
        this.subject = subject;
        this.quesBankTitle = quesBankTitle;
        this.desc = desc;
        this.quesBankId = quesBankId;
        this.addedBy = addedBy;
        this.addedDate = addedDate;
        this.questionBankAttachList = questionBankAttachList;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public String getQuesBankTitle() {
        return quesBankTitle;
    }

    public void setQuesBankTitle(String quesBankTitle) {
        this.quesBankTitle = quesBankTitle;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getQuesBankId() {
        return quesBankId;
    }

    public void setQuesBankId(String quesBankId) {
        this.quesBankId = quesBankId;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public ArrayList<String> getQuestionBankAttachList() {
        return questionBankAttachList;
    }

}

