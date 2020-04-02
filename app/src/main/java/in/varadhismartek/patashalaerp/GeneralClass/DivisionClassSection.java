package in.varadhismartek.patashalaerp.GeneralClass;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DivisionClassSection  {
    APIService mApiService;
     public  static ArrayList<String> listDivision12 = new ArrayList<>();
    ArrayList<String> listClass;
    ArrayList<String> listSection;
    CustomSpinnerAdapter customSpinnerAdapter;

    public DivisionClassSection(Context context) {

    }



    /*public ArrayList<String> myNumbers()    {
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        numbers.add(5);
        numbers.add(11);
        numbers.add(3);
        return(numbers);
    }
}*/


   /* public ArrayList<String> getDivisionApi() {
        Constant.listDivisionNew = new ArrayList<>();
        mApiService = ApiUtils.getAPIService();
        mApiService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                listDivision12.clear();
                listDivision12.add(0, "Select Division");


                boolean statusDivision = false;
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);

                                String added_datetime = object1.getString("added_datetime");
                                String Id = object1.getString("id");
                                statusDivision = object1.getBoolean("status");
                                if (statusDivision) {
                                    String division = object1.getString("division");
                                    String school_id = object1.getString("school_id");
                                    String added_by_id = object1.getString("added_by_id");




                                    listDivision12.add(division);
                                    Constant.listDivisionNew=listDivision12;
                                }


                            }
                            System.out.println("listDivision****1"+listDivision12);
                            System.out.println("listDivision****2"+  Constant.listDivisionNew);
                          //  customSpinnerAdapter = new CustomSpinnerAdapter(this, listDivision);
                           // spDivision.setAdapter(customSpinnerAdapter);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                  //  Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("GET_DIVISION_EXCEPTION", t.getMessage());

            }
        });




        return  Constant.listDivisionNew;


    }*/

    public void getDivisionApi(final ArrayList<String> listDivision12) {

        Constant.listDivisionNew = new ArrayList<>();
        mApiService = ApiUtils.getAPIService();
        mApiService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                listDivision12.clear();
                listDivision12.add(0, "Select Division");


                boolean statusDivision = false;
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);

                                String added_datetime = object1.getString("added_datetime");
                                String Id = object1.getString("id");
                                statusDivision = object1.getBoolean("status");
                                if (statusDivision) {
                                    String division = object1.getString("division");
                                    String school_id = object1.getString("school_id");
                                    String added_by_id = object1.getString("added_by_id");




                                    listDivision12.add(division);
                                    Constant.listDivisionNew=listDivision12;
                                }


                            }
                            System.out.println("listDivision****1"+listDivision12);
                            System.out.println("listDivision****2"+  Constant.listDivisionNew);
                            //  customSpinnerAdapter = new CustomSpinnerAdapter(this, listDivision);
                            // spDivision.setAdapter(customSpinnerAdapter);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                    //  Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("GET_DIVISION_EXCEPTION", t.getMessage());

            }
        });


    }
}
