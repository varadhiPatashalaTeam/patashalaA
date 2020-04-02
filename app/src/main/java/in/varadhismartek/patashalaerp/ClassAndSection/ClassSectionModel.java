package in.varadhismartek.patashalaerp.ClassAndSection;

import java.util.ArrayList;

public class ClassSectionModel {
    private String className;
    private ArrayList<String> listSection;

    private  String sectionName;
    private ArrayList<String>listSubject;


    public ClassSectionModel(String className, ArrayList<String> listSection) {
        this.className = className;
        this.listSection = listSection;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public ArrayList<String> getListSubject() {
        return listSubject;
    }

    public void setListSubject(ArrayList<String> listSubject) {
        this.listSubject = listSubject;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ArrayList<String> getListSection() {
        return listSection;
    }

    public void setListSection(ArrayList<String> listSection) {
        this.listSection = listSection;
    }
}
