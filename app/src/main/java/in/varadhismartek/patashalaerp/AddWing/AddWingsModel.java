package in.varadhismartek.patashalaerp.AddWing;

/**
 * Created by varadhi on 25/9/18.
 */

public class AddWingsModel {

    private String wingsName;
    private boolean wingsSelected;

    public AddWingsModel(String wingsName, boolean wingsSelected) {
        this.wingsName = wingsName;
        this.wingsSelected = wingsSelected;
    }

    public String getWingsName() {
        return wingsName;
    }

    public void setWingsName(String wingsName) {
        this.wingsName = wingsName;
    }

    public void setWingsSelected(boolean wingsSelected) {
        this.wingsSelected = wingsSelected;
    }

    public boolean isWingsSelected() {
        return wingsSelected;
    }
}
