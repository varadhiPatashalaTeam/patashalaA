package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

public class HouseModule {

    String number_of_students="",house_name="",house_color="",house_uuid="",
            mentor_fname,mentor_lname="",mentor_id="",mentor_no="",mentor_adharno="",
            viceC_fname="",viceC_lname="",viceC_id="",viceCDOB="",viceCbirthID="",
            C_fname="",C_lname,C_Id="",C_DOB="",C_BID="",mName="",CName="";

String  vCName;

    public HouseModule(String number_of_students, String house_name, String house_color, String house_uuid,
                       String mentor_id, String mentor_no, String mentor_adharno,
                       String c_Id, String c_DOB, String c_BID,
                       String mName, String CName) {

        this.number_of_students = number_of_students;
        this.house_name = house_name;
        this.house_color = house_color;
        this.house_uuid = house_uuid;
        this.mentor_id = mentor_id;
        this.mentor_no = mentor_no;
        this.mentor_adharno = mentor_adharno;
        this.C_Id = c_Id;
        this.C_DOB = c_DOB;
        this.C_BID = c_BID;
        this.mName = mName;
        this.CName = CName;
    }

    public HouseModule(String number_of_students, String house_name, String house_color, String house_uuid, String mentor_fname, String mentor_lname, String mentor_id, String mentor_no, String mentor_adharno) {
        this.number_of_students = number_of_students;
        this.house_name = house_name;
        this.house_color = house_color;
        this.house_uuid = house_uuid;
        this.mentor_fname = mentor_fname;
        this.mentor_lname = mentor_lname;
        this.mentor_id = mentor_id;
        this.mentor_no = mentor_no;
        this.mentor_adharno = mentor_adharno;
    }

/*    public HouseModule(String number_of_students, String house_name, String house_color, String house_uuid, String mName, String cName, String vCName) {

    }*/

    public HouseModule(String number_of_students, String house_name, String house_color, String house_uuid, String mName, String CName, String vCName) {
        this.number_of_students = number_of_students;
        this.house_name = house_name;
        this.house_color = house_color;
        this.house_uuid = house_uuid;
        this.mName = mName;
        this.CName = CName;
        this.vCName = vCName;
    }

    public String getvCName() {
        return vCName;
    }

    public void setvCName(String vCName) {
        this.vCName = vCName;
    }

    public String getCName() {
        return CName;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getNumber_of_students() {
        return number_of_students;
    }

    public void setNumber_of_students(String number_of_students) {
        this.number_of_students = number_of_students;
    }

    public String getHouse_name() {
        return house_name;
    }

    public void setHouse_name(String house_name) {
        this.house_name = house_name;
    }

    public String getHouse_color() {
        return house_color;
    }

    public void setHouse_color(String house_color) {
        this.house_color = house_color;
    }

    public String getHouse_uuid() {
        return house_uuid;
    }

    public void setHouse_uuid(String house_uuid) {
        this.house_uuid = house_uuid;
    }

    public String getMentor_fname() {
        return mentor_fname;
    }

    public void setMentor_fname(String mentor_fname) {
        this.mentor_fname = mentor_fname;
    }

    public String getMentor_lname() {
        return mentor_lname;
    }

    public void setMentor_lname(String mentor_lname) {
        this.mentor_lname = mentor_lname;
    }

    public String getMentor_id() {
        return mentor_id;
    }

    public void setMentor_id(String mentor_id) {
        this.mentor_id = mentor_id;
    }

    public String getMentor_no() {
        return mentor_no;
    }

    public void setMentor_no(String mentor_no) {
        this.mentor_no = mentor_no;
    }

    public String getMentor_adharno() {
        return mentor_adharno;
    }

    public void setMentor_adharno(String mentor_adharno) {
        this.mentor_adharno = mentor_adharno;
    }

    public String getViceC_fname() {
        return viceC_fname;
    }

    public void setViceC_fname(String viceC_fname) {
        this.viceC_fname = viceC_fname;
    }

    public String getViceC_lname() {
        return viceC_lname;
    }

    public void setViceC_lname(String viceC_lname) {
        this.viceC_lname = viceC_lname;
    }

    public String getViceC_id() {
        return viceC_id;
    }

    public void setViceC_id(String viceC_id) {
        this.viceC_id = viceC_id;
    }

    public String getViceCDOB() {
        return viceCDOB;
    }

    public void setViceCDOB(String viceCDOB) {
        this.viceCDOB = viceCDOB;
    }

    public String getViceCbirthID() {
        return viceCbirthID;
    }

    public void setViceCbirthID(String viceCbirthID) {
        this.viceCbirthID = viceCbirthID;
    }

    public String getC_fname() {
        return C_fname;
    }

    public void setC_fname(String c_fname) {
        C_fname = c_fname;
    }

    public String getC_lname() {
        return C_lname;
    }

    public void setC_lname(String c_lname) {
        C_lname = c_lname;
    }

    public String getC_Id() {
        return C_Id;
    }

    public void setC_Id(String c_Id) {
        C_Id = c_Id;
    }

    public String getC_DOB() {
        return C_DOB;
    }

    public void setC_DOB(String c_DOB) {
        C_DOB = c_DOB;
    }

    public String getC_BID() {
        return C_BID;
    }

    public void setC_BID(String c_BID) {
        C_BID = c_BID;
    }
}
