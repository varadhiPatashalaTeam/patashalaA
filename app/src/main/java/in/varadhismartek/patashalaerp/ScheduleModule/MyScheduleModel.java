package in.varadhismartek.patashalaerp.ScheduleModule;

import java.io.Serializable;
import java.util.ArrayList;

import in.varadhismartek.patashalaerp.DashboardModule.Homework.MyModel;

public class MyScheduleModel implements Serializable {

    String date,title, subject, time,className;
    ArrayList<ScheduleModel> scheduleModelList;

    String from_date, to_date, divisionName, examName,scheduleType;
    ArrayList<MyNewModel> myList;
    ArrayList<String> sectionList;

    public MyScheduleModel(String date, ArrayList<ScheduleModel> scheduleModelList) {
        this.date = date;
        this.scheduleModelList = scheduleModelList;
    }

    public MyScheduleModel(ArrayList<String> sectionList, String className) {
        this.className = className;
        this.sectionList = sectionList;
    }

    public MyScheduleModel(String date, ArrayList<ScheduleModel> scheduleModelList,String title) {
        this.date = date;
        this.scheduleModelList = scheduleModelList;
        this.title  = title;
    }

    public MyScheduleModel(String date, String subject, String time) {
        this.date = date;
        this.subject = subject;
        this.time = time;
    }

    public MyScheduleModel(String scheduleType,String from_date,String to_date, String divisionName,
                           String examName, ArrayList<MyNewModel> myList){

        this.scheduleType = scheduleType;
        this.from_date = from_date;
        this.to_date = to_date;
        this.divisionName = divisionName;
        this.examName = examName;
        this.myList =  myList;
    }

    public String getSubject() {
        return subject;
    }

    public String getTime() {
        return time;
    }

    public String getClassName() {
        return className;
    }

    public ArrayList<String> getSectionList() {
        return sectionList;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<ScheduleModel> getScheduleModelList() {
        return scheduleModelList;
    }

    public String getTitle() {
        return title;
    }

    public String getFrom_date() {
        return from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public String getExamName() {
        return examName;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public ArrayList<MyNewModel> getMyList() {
        return myList;
    }
}
