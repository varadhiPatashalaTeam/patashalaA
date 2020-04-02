package in.varadhismartek.patashalaerp.DashboardModule.TransportModule;

import java.util.ArrayList;

public class RouteList {
    private String route_uuid, route_number;

    private String latitudePoint, locationManually, stopImage, approxStopTime, longitudePoint,
            stopNumber,stopName,stopDistance;


    private  String  type_of_body,GPS_Details,seating_capacity,registration_no,
            class_of_vehicle,vehicle_uuid,vehicle_id,added_datetime;
    private ArrayList<RouteList> routeLists;


    public RouteList(String type_of_body, String GPS_Details, String seating_capacity, String registration_no, String class_of_vehicle,
                         String vehicle_uuid, String vehicle_id, String added_datetime,ArrayList<RouteList> routeLists) {
        this.type_of_body = type_of_body;
        this.GPS_Details = GPS_Details;
        this.seating_capacity = seating_capacity;
        this.registration_no = registration_no;
        this.class_of_vehicle = class_of_vehicle;
        this.vehicle_uuid = vehicle_uuid;
        this.vehicle_id = vehicle_id;
        this.added_datetime = added_datetime;
        this.routeLists = routeLists;

    }


    public RouteList(String type_of_body, String GPS_Details, String seating_capacity, String registration_no, String class_of_vehicle,
                         String vehicle_uuid, String vehicle_id, String added_datetime,String tag) {
        this.type_of_body = type_of_body;
        this.GPS_Details = GPS_Details;
        this.seating_capacity = seating_capacity;
        this.registration_no = registration_no;
        this.class_of_vehicle = class_of_vehicle;
        this.vehicle_uuid = vehicle_uuid;
        this.vehicle_id = vehicle_id;
        this.added_datetime = added_datetime;

    }


    public RouteList(String latitudePoint, String locationManually, String stopImage, String approxStopTime, String longitudePoint, String stopNumber, String stopName, String stopDistance) {
        this.latitudePoint = latitudePoint;
        this.locationManually = locationManually;
        this.stopImage = stopImage;
        this.approxStopTime = approxStopTime;
        this.longitudePoint = longitudePoint;
        this.stopNumber = stopNumber;
        this.stopName = stopName;
        this.stopDistance = stopDistance;
    }

    public RouteList(String route_uuid, String route_number) {
        this.route_uuid = route_uuid;
        this.route_number = route_number;
    }

    public String getLatitudePoint() {
        return latitudePoint;
    }

    public void setLatitudePoint(String latitudePoint) {
        this.latitudePoint = latitudePoint;
    }

    public String getLocationManually() {
        return locationManually;
    }

    public void setLocationManually(String locationManually) {
        this.locationManually = locationManually;
    }

    public String getStopImage() {
        return stopImage;
    }

    public void setStopImage(String stopImage) {
        this.stopImage = stopImage;
    }

    public String getApproxStopTime() {
        return approxStopTime;
    }

    public void setApproxStopTime(String approxStopTime) {
        this.approxStopTime = approxStopTime;
    }

    public String getLongitudePoint() {
        return longitudePoint;
    }

    public void setLongitudePoint(String longitudePoint) {
        this.longitudePoint = longitudePoint;
    }

    public String getStopNumber() {
        return stopNumber;
    }

    public void setStopNumber(String stopNumber) {
        this.stopNumber = stopNumber;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getStopDistance() {
        return stopDistance;
    }

    public void setStopDistance(String stopDistance) {
        this.stopDistance = stopDistance;
    }

    public String getRoute_uuid() {
        return route_uuid;
    }

    public void setRoute_uuid(String route_uuid) {
        this.route_uuid = route_uuid;
    }

    public String getRoute_number() {
        return route_number;
    }

    public void setRoute_number(String route_number) {
        this.route_number = route_number;
    }

    public String getType_of_body() {
        return type_of_body;
    }

    public String getGPS_Details() {
        return GPS_Details;
    }

    public String getSeating_capacity() {
        return seating_capacity;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public String getClass_of_vehicle() {
        return class_of_vehicle;
    }

    public String getVehicle_uuid() {
        return vehicle_uuid;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public String getAdded_datetime() {
        return added_datetime;
    }

    public ArrayList<RouteList> getRouteLists() {
        return routeLists;
    }
}
