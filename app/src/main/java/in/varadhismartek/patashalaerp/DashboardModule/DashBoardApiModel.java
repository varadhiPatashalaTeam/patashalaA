package in.varadhismartek.patashalaerp.DashboardModule;

public class DashBoardApiModel {

    /* registration_no = keyData.getString("registration_no");
                                seating_capacity = keyData.getString("seating_capacity");
                                GPS_Details = keyData.getString("GPS_Details");
                                school_id = keyData.getString("school_id");
                                added_datetime = keyData.getString("added_datetime");
                                vehicle_uuid = keyData.getString("vehicle_uuid");
                                vehicle_id = keyData.getString("vehicle_id");
                                class_of_vehicle = keyData.getString("class_of_vehicle");
                                type_of_body = keyData.getString("type_of_body");
                                added_by = keyData.getString("added_by");
*/


    String firstName,lastName,customID,
            image, uuid,birthDate;

    String notice_id,notice_title,notice_message;

    String registration_no,seating_capacity,GPS_Details,vehicle_uuid,vehicle_id,class_of_vehicle,type_of_body,route_uuid,route_number;
    /*----------------------Notice Board----------------------------*/

    private String scheduleType, scheduleTitle ,scheduleName, className, section, division, examTime;


    public DashBoardApiModel(String notice_id, String notice_title, String notice_message) {
        this.notice_id = notice_id;
        this.notice_title = notice_title;
        this.notice_message = notice_message;
    }


    public DashBoardApiModel(String firstName, String lastName, String customID, String image, String uuid, String birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.customID = customID;
        this.image = image;
        this.uuid = uuid;
        this.birthDate = birthDate;
    }


    // Schedule
    public DashBoardApiModel(String scheduleType, String scheduleTitle, String scheduleName,
                             String className, String section, String division, String examTime){
        this.scheduleType = scheduleType;
        this.scheduleTitle = scheduleTitle;
        this.scheduleName = scheduleName;
        this.className = className;
        this.section = section;
        this.division = division;
        this.examTime = examTime;
    }



    public DashBoardApiModel(String registration_no, String seating_capacity, String GPS_Details, String vehicle_uuid, String vehicle_id, String class_of_vehicle, String type_of_body,String route_uuid, String route_number ) {
        this.registration_no = registration_no;
        this.seating_capacity = seating_capacity;
        this.GPS_Details = GPS_Details;
        this.vehicle_uuid = vehicle_uuid;
        this.vehicle_id = vehicle_id;
        this.class_of_vehicle = class_of_vehicle;
        this.type_of_body = type_of_body;
        this.route_uuid = route_uuid;
        this.route_number = route_number;
    }

    public String getRoute_uuid() {
        return route_uuid;
    }

    public String getRoute_number() {
        return route_number;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public String getSeating_capacity() {
        return seating_capacity;
    }

    public String getGPS_Details() {
        return GPS_Details;
    }

    public String getVehicle_uuid() {
        return vehicle_uuid;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public String getClass_of_vehicle() {
        return class_of_vehicle;
    }

    public String getType_of_body() {
        return type_of_body;
    }

    public String getNotice_id() {
        return notice_id;
    }

    public String getNotice_title() {
        return notice_title;
    }

    public String getNotice_message() {
        return notice_message;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCustomID() {
        return customID;
    }

    public String getImage() {
        return image;
    }

    public String getUuid() {
        return uuid;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public String getScheduleTitle() {
        return scheduleTitle;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public String getClassName() {
        return className;
    }

    public String getSection() {
        return section;
    }

    public String getDivision() {
        return division;
    }

    public String getExamTime() {
        return examTime;
    }
}
