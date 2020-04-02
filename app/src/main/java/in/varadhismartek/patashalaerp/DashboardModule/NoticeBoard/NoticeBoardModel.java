package in.varadhismartek.patashalaerp.DashboardModule.NoticeBoard;

import java.io.Serializable;

public class NoticeBoardModel implements Serializable {

    private String noticeID, firstName, title, addedID, message, addedDate, addedTime, lastName;

    public NoticeBoardModel(String noticeID, String firstName, String title, String addedID,
                            String message, String addedDate, String addedTime, String lastName) {
        this.noticeID = noticeID;
        this.firstName = firstName;
        this.title = title;
        this.addedID = addedID;
        this.message = message;
        this.addedDate = addedDate;
        this.addedTime = addedTime;
        this.lastName = lastName;
    }

    public String getNoticeID() {
        return noticeID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getTitle() {
        return title;
    }

    public String getAddedID() {
        return addedID;
    }

    public String getMessage() {
        return message;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public String getLastName() {
        return lastName;
    }
}
