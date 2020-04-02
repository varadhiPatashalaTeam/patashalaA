package in.varadhismartek.patashalaerp.GeneralClass;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationViews {
    static String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    static String emailpattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";


    public static boolean EditTextNullValidate(EditText... testObj) {
        for (int i = 0; i < testObj.length; i++) {
            if (testObj[i].getText().toString().trim().equals("")) {
                testObj[i].setError("Fill data");
                testObj[i].requestFocus();
                return false;
            }
        }
        return true;
    }



    public static boolean EditTextMobileNumberValidate(EditText... testObj) {
        for (int i = 0; i < testObj.length; i++) {
            if (testObj[i].getText().toString().length() != 10) {
                testObj[i].setError("Invalid mobile");
                testObj[i].requestFocus();

                return false;
            }
        }
        return true;
    }


    public static boolean EditTextEmailPatternValidate(EditText... testObj) {
        for (int i = 0; i < testObj.length; i++) {
            if (!testObj[i].getText().toString().trim().matches(emailpattern) && !testObj[i].getText().toString().trim().matches(emailpattern2)) {
                testObj[i].setError("Invalid email");
                testObj[i].requestFocus();

                return false;
            }

        }
        return true;
    }

    public static boolean EditTextNumberOfDigitsValidate(EditText testObj, int noOfDigits) {
        if (testObj.getText().toString().length()<noOfDigits) {
            testObj.setError("Invalid data");
            testObj.requestFocus();

            return false;
        }
        return true;
    }


    public static boolean EditTextPinCodeValidate(EditText...testObj){
        for(int i = 0 ; i<testObj.length; i++){
            if (testObj[i].getText().toString().length()!=6)
                return false;
        }
        return true;
    }

    public static void showToast(Context c, String s) {
        Toast.makeText(c, s, Toast.LENGTH_LONG).show();
    }


    /*public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }*/

    public boolean isValidConfirmPasswrod(String confirmPassword, String password) {
        if (!confirmPassword.equals(password)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isValidPincode(String pincode) {
        if (pincode == null) {
            return false;
        } else {
            String PINCODE_PATTERN = "^[0-9]{6}$";

            Pattern pattern = Pattern.compile(PINCODE_PATTERN);
            Matcher matcher = pattern.matcher(pincode);
            return matcher.matches();
        }
    }


    public boolean isValidPassword(String password) {
        Pattern p = Pattern.compile("((?!\\s)\\A)(\\s|(?<!\\s)\\S){8,20}\\Z");
        if (password == null) {
            return false;
        } else {
            Matcher m = p.matcher(password);
            return m.matches();
        }
    }




}
