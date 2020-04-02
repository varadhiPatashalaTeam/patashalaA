package in.varadhismartek.patashalaerp.DashboardModule.extraUtils;

import java.io.Serializable;


public class ModuleModel implements Serializable {

    public ModuleModel(int image, String name) {
        this.name = name;
        this.image = image;
    }

    public String name;
    public int image;

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }
}
