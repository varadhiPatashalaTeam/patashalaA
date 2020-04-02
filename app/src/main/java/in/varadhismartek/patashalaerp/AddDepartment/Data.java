package in.varadhismartek.patashalaerp.AddDepartment;

import java.util.ArrayList;

/**
 * Created by varadhi10 on 9/5/2018.
 */

public class Data {
    private String name;
    private boolean select;
    private String checker_name;
    private String spin;
    private String make;
    private String check;
    private ArrayList<String> checker;
    private ArrayList<String> maker;
    private String role;

    public Data(ArrayList<String> checker, ArrayList<String> maker, String role) {

        this.maker = new ArrayList<>();
        this.checker = new ArrayList<>();
        this.checker.addAll(checker);
        this.maker.addAll(maker);
        this.role = role;
    }

    public Data(String spinner, String maker_values, String checker_values) {
        this.spin=spinner;
        this.make=maker_values;
        this.check=checker_values;
    }

    public Data(String name, boolean select) {
        this.name = name;
        this.select = select;

    }


    public ArrayList<String> getChecker() {
        return checker;
    }

    public ArrayList<String> getMaker() {
        return maker;
    }

    public String getRole() {
        return role;
    }

    public String getSpin() {
        return spin;
    }

    public void setSpin(String spin) {
        this.spin = spin;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public Data(String checker_name) {
        this.checker_name=checker_name;
    }

    public String getChecker_name() {
        return checker_name;
    }

    public void setChecker_name(String checker_name) {
        this.checker_name = checker_name;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }



}
