package in.varadhismartek.patashalaerp.Birthday;

public class BirthdayModel {

    private String firstName, lastName, customID, image, uuid, birthDate;
    private  String  type_of_body,GPS_Details,seating_capacity,registration_no,
            class_of_vehicle,vehicle_uuid,vehicle_id,added_datetime;

    public BirthdayModel(String firstName, String lastName, String customID, String image, String uuid, String birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.customID = customID;
        this.image = image;
        this.uuid = uuid;
        this.birthDate = birthDate;
    }

    public BirthdayModel(String type_of_body, String GPS_Details, String seating_capacity, String registration_no, String class_of_vehicle, String vehicle_uuid, String vehicle_id, String added_datetime) {
        this.type_of_body = type_of_body;
        this.GPS_Details = GPS_Details;
        this.seating_capacity = seating_capacity;
        this.registration_no = registration_no;
        this.class_of_vehicle = class_of_vehicle;
        this.vehicle_uuid = vehicle_uuid;
        this.vehicle_id = vehicle_id;
        this.added_datetime = added_datetime;
    }

    public String getType_of_body() {
        return type_of_body;
    }

    public void setType_of_body(String type_of_body) {
        this.type_of_body = type_of_body;
    }

    public String getGPS_Details() {
        return GPS_Details;
    }

    public void setGPS_Details(String GPS_Details) {
        this.GPS_Details = GPS_Details;
    }

    public String getSeating_capacity() {
        return seating_capacity;
    }

    public void setSeating_capacity(String seating_capacity) {
        this.seating_capacity = seating_capacity;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public void setRegistration_no(String registration_no) {
        this.registration_no = registration_no;
    }

    public String getClass_of_vehicle() {
        return class_of_vehicle;
    }

    public void setClass_of_vehicle(String class_of_vehicle) {
        this.class_of_vehicle = class_of_vehicle;
    }

    public String getVehicle_uuid() {
        return vehicle_uuid;
    }

    public void setVehicle_uuid(String vehicle_uuid) {
        this.vehicle_uuid = vehicle_uuid;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getAdded_datetime() {
        return added_datetime;
    }

    public void setAdded_datetime(String added_datetime) {
        this.added_datetime = added_datetime;
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
}
