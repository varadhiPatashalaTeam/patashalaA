package in.varadhismartek.patashalaerp.ScheduleModule;

import java.util.ArrayList;

public class MyNewModel {

    String date,className,message,holiday_name,  holiday_image ;
    ArrayList<ScheduleModel> scheduleModelList;

    public MyNewModel(String date, ArrayList<ScheduleModel> scheduleModelList) {
        this.date = date;
        this.scheduleModelList = scheduleModelList;
    }

    public MyNewModel(String className, String message, String holiday_name, String holiday_image) {
        this.className = className;
        this.message = message;
        this.holiday_name = holiday_name;
        this.holiday_image = holiday_image;

    }

    public String getClassName() {
        return className;
    }

    public String getMessage() {
        return message;
    }

    public String getHoliday_name() {
        return holiday_name;
    }

    public String getHoliday_image() {
        return holiday_image;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<ScheduleModel> getScheduleModelList() {
        return scheduleModelList;
    }
}
