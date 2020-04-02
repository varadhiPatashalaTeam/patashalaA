package in.varadhismartek.patashalaerp.LoginAndRegistation;

public class SchoolModel {
    private String  emailId,organisationName,schoolName,pocEmail,strSchoolLogo,
            schoolBannerImage,website,pocImage,classTo,strSchoolPocName,
            organizationUuid,organizationRNo,pocDesignation,classFrom,
            strSchoolNo,strSchoolID;

    public SchoolModel(String emailId, String organisationName, String schoolName, String pocEmail, String strSchoolLogo, String schoolBannerImage, String website, String pocImage, String classTo, String strSchoolPocName, String organizationUuid, String organizationRNo, String pocDesignation, String classFrom, String strSchoolNo, String strSchoolID) {

        this.emailId = emailId;
        this.organisationName = organisationName;
        this.schoolName = schoolName;
        this.pocEmail = pocEmail;
        this.strSchoolLogo = strSchoolLogo;
        this.schoolBannerImage = schoolBannerImage;
        this.website = website;
        this.pocImage = pocImage;
        this.classTo = classTo;
        this.strSchoolPocName = strSchoolPocName;
        this.organizationUuid = organizationUuid;
        this.organizationRNo = organizationRNo;
        this.pocDesignation = pocDesignation;
        this.classFrom = classFrom;
        this.strSchoolNo = strSchoolNo;
        this.strSchoolID = strSchoolID;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getPocEmail() {
        return pocEmail;
    }

    public void setPocEmail(String pocEmail) {
        this.pocEmail = pocEmail;
    }

    public String getStrSchoolLogo() {
        return strSchoolLogo;
    }

    public void setStrSchoolLogo(String strSchoolLogo) {
        this.strSchoolLogo = strSchoolLogo;
    }

    public String getSchoolBannerImage() {
        return schoolBannerImage;
    }

    public void setSchoolBannerImage(String schoolBannerImage) {
        this.schoolBannerImage = schoolBannerImage;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPocImage() {
        return pocImage;
    }

    public void setPocImage(String pocImage) {
        this.pocImage = pocImage;
    }

    public String getClassTo() {
        return classTo;
    }

    public void setClassTo(String classTo) {
        this.classTo = classTo;
    }

    public String getStrSchoolPocName() {
        return strSchoolPocName;
    }

    public void setStrSchoolPocName(String strSchoolPocName) {
        this.strSchoolPocName = strSchoolPocName;
    }

    public String getOrganizationUuid() {
        return organizationUuid;
    }

    public void setOrganizationUuid(String organizationUuid) {
        this.organizationUuid = organizationUuid;
    }

    public String getOrganizationRNo() {
        return organizationRNo;
    }

    public void setOrganizationRNo(String organizationRNo) {
        this.organizationRNo = organizationRNo;
    }

    public String getPocDesignation() {
        return pocDesignation;
    }

    public void setPocDesignation(String pocDesignation) {
        this.pocDesignation = pocDesignation;
    }

    public String getClassFrom() {
        return classFrom;
    }

    public void setClassFrom(String classFrom) {
        this.classFrom = classFrom;
    }

    public String getStrSchoolNo() {
        return strSchoolNo;
    }

    public void setStrSchoolNo(String strSchoolNo) {
        this.strSchoolNo = strSchoolNo;
    }

    public String getStrSchoolID() {
        return strSchoolID;
    }

    public void setStrSchoolID(String strSchoolID) {
        this.strSchoolID = strSchoolID;
    }
}
