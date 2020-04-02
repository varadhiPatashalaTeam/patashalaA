package in.varadhismartek.patashalaerp.SelectModules;

/**
 * Created by varadhi on 25/9/18.
 */

public class SelectModuleModel {

    private String moduleNAme;
    private boolean moduleSelected;

    public SelectModuleModel(String moduleNAme, boolean moduleSelected) {
        this.moduleNAme = moduleNAme;
        this.moduleSelected = moduleSelected;
    }

    public String getModuleNAme() {
        return moduleNAme;
    }

    public void setModuleSelected(boolean moduleSelected) {
        this.moduleSelected = moduleSelected;
    }

    public boolean isModuleSelected() {
        return moduleSelected;
    }
}
