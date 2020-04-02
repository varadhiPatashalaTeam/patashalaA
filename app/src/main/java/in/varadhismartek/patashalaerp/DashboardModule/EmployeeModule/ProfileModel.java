package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

public class ProfileModel {

    private String id,work_exp_no ,work_experience_details_id ,work_experience_attachments ;
    private String employee_id , organisation_place ,no_of_days_experience ,experience_to_date,
            organisation_name , experience_from_date , responsibilty,role,contact_no;
    private String docsName,  docsNo,  picOne,  picTwo;
    private String sportsName, teamName, position, year, location, level;
    private String title,description;
    private String name, dOB;
    private String type,  firstName, middleName,  lastName,  aadharID,  addressID, address, landmark, picThree, picFour;


    public ProfileModel(String id, String work_exp_no, String work_experience_details_id, String work_experience_attachments,
                        String employee_id, String organisation_place, String no_of_days_experience, String experience_to_date,
                        String organisation_name, String experience_from_date, String responsibilty, String role, String contact_no) {
        this.id = id;
        this.work_exp_no = work_exp_no;
        this.work_experience_details_id = work_experience_details_id;
        this.work_experience_attachments = work_experience_attachments;
        this.employee_id = employee_id;
        this.organisation_place = organisation_place;
        this.no_of_days_experience = no_of_days_experience;
        this.experience_to_date = experience_to_date;
        this.organisation_name = organisation_name;
        this.experience_from_date = experience_from_date;
        this.responsibilty = responsibilty;
        this.role = role;
        this.contact_no = contact_no;
    }

    public ProfileModel(String docsName, String docsNo, String picOne, String picTwo){

        this.docsName = docsName;
        this.docsNo = docsNo;
        this.picOne = picOne;
        this.picTwo = picTwo;
    }

    public ProfileModel(String sportsName, String teamName, String level, String position, String year, String location){

        this.sportsName = sportsName;
        this.teamName = teamName;
        this.position = position;
        this.year = year;
        this.location = location;
        this.level = level;
    }

    public ProfileModel(String title, String description, String year){

        this.title = title;
        this.description = description;
        this.year = year;
    }

    public ProfileModel(String name, String dOB){

        this.name = name;
        this.dOB = dOB;
    }

    public ProfileModel(String type, String firstName, String middleName, String lastName, String aadharID, String addressID,
                        String address, String landmark, String picOne, String picTwo, String picThree, String picFour){

        this.type = type;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.aadharID = aadharID;
        this.addressID = addressID;
        this.picOne = picOne;
        this.picTwo = picTwo;
        this.picThree = picThree;
        this.picFour = picFour;
        this.address = address;
        this.landmark = landmark;
    }

    public String getId() {
        return id;
    }

    public String getWork_exp_no() {
        return work_exp_no;
    }

    public String getWork_experience_details_id() {
        return work_experience_details_id;
    }

    public String getWork_experience_attachments() {
        return work_experience_attachments;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public String getOrganisation_place() {
        return organisation_place;
    }

    public String getNo_of_days_experience() {
        return no_of_days_experience;
    }

    public String getExperience_to_date() {
        return experience_to_date;
    }

    public String getOrganisation_name() {
        return organisation_name;
    }

    public String getExperience_from_date() {
        return experience_from_date;
    }

    public String getResponsibilty() {
        return responsibilty;
    }

    public String getRole() {
        return role;
    }

    public String getContact_no() {
        return contact_no;
    }

    public String getDocsName() {
        return docsName;
    }

    public String getDocsNo() {
        return docsNo;
    }

    public String getPicOne() {
        return picOne;
    }

    public String getPicTwo() {
        return picTwo;
    }

    public String getSportsName() {
        return sportsName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getPosition() {
        return position;
    }

    public String getYear() {
        return year;
    }

    public String getLocation() {
        return location;
    }

    public String getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getDOB() {
        return dOB;
    }

    public String getdOB() {
        return dOB;
    }

    public String getType() {
        return type;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAadharID() {
        return aadharID;
    }

    public String getAddressID() {
        return addressID;
    }

    public String getAddress() {
        return address;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getPicThree() {
        return picThree;
    }

    public String getPicFour() {
        return picFour;
    }
}
