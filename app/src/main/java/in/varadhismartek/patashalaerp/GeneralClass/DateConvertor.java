package in.varadhismartek.patashalaerp.GeneralClass;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class DateConvertor {

    private String inputTZ_SSS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private String inputTZ = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private String inputDMY = "dd-MM-yyyy";

    private String outputFormat = "dd-MM-yyyy";
    private String outputFormatTime = "HH:mm";
    private String outputYMD = "yyyy-MM-dd";
    private String appliedDate;
    private String appliedTime;



    public String getDateByTZ_SSS(String inputDate){

        @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat(inputTZ_SSS);
        input.setTimeZone(TimeZone.getTimeZone("IST"));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat(outputFormat);
        output.setTimeZone(TimeZone.getDefault());

        try {

            Date applied = input.parse(inputDate);
            appliedDate = output.format(applied);

        }catch (Exception e){
            e.printStackTrace();
        }

        return appliedDate;
    }

    public String getTimeByTZ_SSS(String inputDate){

        @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat(inputTZ_SSS);
        input.setTimeZone(TimeZone.getTimeZone("IST"));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat(outputFormatTime);
        output.setTimeZone(TimeZone.getDefault());

        try {

            Date applied = input.parse(inputDate);
            appliedTime = output.format(applied);

        }catch (Exception e){
            e.printStackTrace();
        }

        return appliedTime;
    }

    public String getDateByTZ(String inputDate){

        @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat(inputTZ);
        input.setTimeZone(TimeZone.getTimeZone("IST"));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat(outputFormat);
        output.setTimeZone(TimeZone.getDefault());

        try {

            Date applied = input.parse(inputDate);
            appliedDate = output.format(applied);

        }catch (Exception e){
            e.printStackTrace();
        }

        return appliedDate;
    }



    public String getDateDMY_to_YMD(String inputDate){

        @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat(inputDMY);
        input.setTimeZone(TimeZone.getTimeZone("IST"));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat(outputYMD);
        output.setTimeZone(TimeZone.getDefault());

        try {

            Date applied = input.parse(inputDate);
            appliedTime = output.format(applied);

        }catch (Exception e){
            e.printStackTrace();
        }

        return appliedTime;
    }

    public String getTimeByTZ(String inputDate){

        @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat(inputTZ);
        input.setTimeZone(TimeZone.getTimeZone("IST"));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat(outputFormatTime);
        output.setTimeZone(TimeZone.getDefault());

        try {

            Date applied = input.parse(inputDate);
            appliedTime = output.format(applied);

        }catch (Exception e){
            e.printStackTrace();
        }

        return appliedTime;
    }
}
