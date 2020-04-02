package in.varadhismartek.patashalaerp.Timetable;

import java.util.ArrayList;

public class PeriodTableModel {
    private  String duration,start_time,addedUUID,end_time,type_of;

    private String strDay;
    private ArrayList<TimetableDetails> timetableDetails = new ArrayList<>();

    public PeriodTableModel(String duration, String start_time, String addedUUID, String end_time, String type_of) {
        this.duration = duration;
        this.start_time = start_time;
        this.addedUUID = addedUUID;
        this.end_time = end_time;
        this.type_of = type_of;
    }

    public PeriodTableModel(String strDay, ArrayList<TimetableDetails> timetableDetails) {
        this.strDay = strDay;
        this.timetableDetails = timetableDetails;
    }

    public String getStrDay() {
        return strDay;
    }

    public void setStrDay(String strDay) {
        this.strDay = strDay;
    }

    public ArrayList<TimetableDetails> getTimetableDetails() {
        return timetableDetails;
    }

    public void setTimetableDetails(ArrayList<TimetableDetails> timetableDetails) {
        this.timetableDetails = timetableDetails;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getAddedUUID() {
        return addedUUID;
    }

    public void setAddedUUID(String addedUUID) {
        this.addedUUID = addedUUID;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getType_of() {
        return type_of;
    }

    public void setType_of(String type_of) {
        this.type_of = type_of;
    }
}
