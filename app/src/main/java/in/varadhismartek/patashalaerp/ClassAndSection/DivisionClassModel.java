package in.varadhismartek.patashalaerp.ClassAndSection;

import java.util.ArrayList;

import in.varadhismartek.patashalaerp.DivisionModule.ClassSectionAndDivisionModel;

public class DivisionClassModel {

    private String divisionName;
    private ArrayList<ClassSectionAndDivisionModel> classList;

    public DivisionClassModel(String divisionName, ArrayList<ClassSectionAndDivisionModel> classList) {
        this.divisionName = divisionName;
        this.classList = classList;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public ArrayList<ClassSectionAndDivisionModel> getClassList() {
        return classList;
    }

    public void setClassList(ArrayList<ClassSectionAndDivisionModel> classList) {
        this.classList = classList;
    }
}
