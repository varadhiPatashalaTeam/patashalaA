package in.varadhismartek.patashalaerp.CheckerMaker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.AddDepartment.Data;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.SelectModules.SelectModuleModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MakerCheckerFragment extends Fragment implements View.OnClickListener {
    Context context;
    private Spinner spinnerSelectModule;
    private ImageButton buttonMarker, buttonChecker;
    private Dialog dialog = null;
    private Button btnAdd;
    private RecyclerView rvMaker, rvChecker, rvModule;
    private ImageView imageViewSave;
    ArrayList<String> maker, checker;
    ArrayList<Data> maker_choice, checker_choice, module_choice, checkerMakerRole;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> checkerItems = new ArrayList<>();
    ArrayList<String> makerItems = new ArrayList<>();
    boolean[] maker_boolean;
    boolean[] checker_boolean;
    String[] maker_values_a, checker_values_a;
    private String maker_values = "", checker_values = "", spinnervalue, myData;
    CustomSpinnerAdapter customSpinnerAdapter;
    int countMaker = 0, countChecker = 0;


    APIService mApiService;
    boolean roles_status=false,module_status =false;
    private ArrayList<SelectModuleModel> modulesName;

    public MakerCheckerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maker_checker, container, false);


        initView(view);
        initListener();

        module_choice = new ArrayList<>();
        maker_choice = new ArrayList<>();
        checker_choice = new ArrayList<>();
        checkerMakerRole = new ArrayList<>();

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinnerSelectModule);

            // Set popupWindow height to 500px
            popupWindow.setHeight(800);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        getModules();
        getModuleListInSpinner();
        getMakerChecker();
        addData();
        return view;
    }
    private void getMakerChecker() {
        mApiService.getMakerChecker(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {


                JSONArray jsonArray = null;
                JSONArray jsonArray2 = null;

                if (response.isSuccessful()) {
                    try {
                        Log.i("MAKER_GET",""+response.body());
                        module_choice.clear();

                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");
                        ArrayList<String>arrayListModule = new ArrayList<>();
                        Data data;
                        if (status.equalsIgnoreCase("Success")) {
                            JSONObject jsonObject2 = json1.getJSONObject("data");

                            Iterator<String> keys = jsonObject2.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();

                                JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                String moduleName = jsonObjectValue.getString("module_name");
                                name.add(moduleName);
                                 jsonArray = jsonObjectValue.getJSONArray("makers");
                                 jsonArray2 = jsonObjectValue.getJSONArray("checkers");
                                String strMaker ="",strChecker= "";

                                for (int i=0;i<jsonArray.length();i++){
                                    strMaker = strMaker + "\n"+jsonArray.getString(i);

                                }

                                for (int i=0;i<jsonArray2.length();i++){
                                    strChecker = strChecker + "\n"+jsonArray2.getString(i);
                                }

                               Data data3 = new Data(moduleName,strMaker, strChecker);

                                module_choice.add(data3);

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                                rvModule.setLayoutManager(linearLayoutManager);
                                rvModule.setHasFixedSize(true);

                                RecyclerMakerAdapter recyclerAdapter = new RecyclerMakerAdapter(getActivity(),
                                        module_choice, Constant.SELECTED_FRAG,imageViewSave);
                                rvModule.setAdapter(recyclerAdapter);

                            }

                        }
                        else{

                                Log.d("MAKER_FAIL", String.valueOf(response.code()));
                                if (String.valueOf(response.code()).equals("409")) {
                                    Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();

                                }
                                if (String.valueOf(response.code()).equals("404")) {
                                      Toast.makeText(getActivity(), "Data not exists.", Toast.LENGTH_SHORT).show();
                                }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {


                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    private void initListener() {
        imageViewSave.setOnClickListener(this);
        buttonMarker.setOnClickListener(this);
        buttonChecker.setOnClickListener(this);
    }
    private void initView(View view) {
        mApiService = ApiUtils.getAPIService();
        maker = new ArrayList<>();
        checker = new ArrayList<>();
        spinnerSelectModule = view.findViewById(R.id.spinner_module);
        rvModule = view.findViewById(R.id.recycler_module);
        rvMaker = view.findViewById(R.id.recycler_maker);
        rvChecker = view.findViewById(R.id.recycler_checker);
        buttonMarker = view.findViewById(R.id.maker_button);
        buttonChecker = view.findViewById(R.id.checker_button);
        btnAdd = view.findViewById(R.id.btn_add);
        imageViewSave = view.findViewById(R.id.iv_saveCheckerMaker);
    }
    private void getModules() {

      //  name.add("Select Module");
        mApiService.getModuleList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try{
                    if (response.isSuccessful()){
                        name.clear();
                        Log.i("MAKER_CHECKER",""+response.body());
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {

                            JSONObject jsonObject1 = object.getJSONObject("data");
                            Iterator<String> keys = jsonObject1.keys();

                            while (keys.hasNext()) {
                                String key = keys.next();

                                JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);
                                Log.d("MAKER_CHECKER_DATA", jsonObjectValue.toString());

                                 module_status = jsonObjectValue.getBoolean("active_status");
                                if (module_status){
                                    String module_name = jsonObjectValue.getString("module_name");
                                    name.add(module_name);
                                }
                                getModuleListInSpinner();


                            }
                        }

                    }else {
                        Log.i("SELECTMODULE","NO Data");
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("SELECTMODULEERROR",""+t.toString());
            }
        });
    }
    private void getModuleListInSpinner() {
        customSpinnerAdapter = new CustomSpinnerAdapter(getContext(), name);
        spinnerSelectModule.setAdapter(customSpinnerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.maker_button:
                getDepartmentList();
                break;
            case R.id.checker_button:
                getDeptListForChecker();
                break;
        }

    }
    private void getDepartmentList() {

        JSONObject wingsJsonObject = new JSONObject();
        JSONObject deptJsonObject = new JSONObject();
        JSONArray deptArray = new JSONArray();
        try {
            deptArray.put("All");
            wingsJsonObject.put("wings", deptArray);
            deptJsonObject.put("departments", deptArray);
        }catch (JSONException j){

        }

        mApiService.gettingRoles(Constant.SCHOOL_ID, wingsJsonObject, deptJsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        maker.clear();
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("roles");


                            Iterator<String> keys = jsonObject2.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                String rolesName = jsonObjectValue.getString("role");
                                 roles_status = Boolean.parseBoolean(jsonObjectValue.getString("status"));
                                if (roles_status){
                                    maker.add(rolesName);
                                }
                            }
                            Log.i("makerDATA",""+maker);
                            makerDialogOpen(maker);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("ROLES_FAIL", String.valueOf(response.code()));
                    if (String.valueOf(response.code()).equals("409")) {

                    }
                    if (String.valueOf(response.code()).equals("404")) {

                    }
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }
    private void makerDialogOpen(ArrayList<String>maker) {
        Log.i("makerList DAta*****",""+maker);
        makerItems.clear();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        maker_values_a = maker.toArray(new String[maker.size()]);
        maker_boolean = new boolean[maker_values_a.length];
        TextView myMsg = new TextView(getActivity());
        myMsg.setText("Select Roles");
        myMsg.setGravity(Gravity.CENTER | Gravity.LEFT);
        myMsg.setBackgroundDrawable(getResources().getDrawable(R.drawable.checker_maker));
        myMsg.setTextSize(15);
        myMsg.setPadding(20, 0, 0, 0);
        myMsg.setHeight(80);
        myMsg.setTextColor(Color.WHITE);


        builder.setCustomTitle(myMsg);
        builder.setMultiChoiceItems(maker_values_a, maker_boolean,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        //maker_boolean=null;
                        if (isChecked) {
                            makerItems.add(maker_values_a[position]);
                        } else {
                            makerItems.remove(maker_values_a[position]);
                        }
                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        maker_choice.clear();
                        for (String maker_name : makerItems) {
                            maker_choice.add(new Data(maker_name));
                        }
                        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
                        rvMaker.setLayoutManager(linearLayoutManager);
                        rvMaker.setHasFixedSize(true);

                        RecyclerMakerAdapter recyclerAdapter = new RecyclerMakerAdapter(getActivity(),
                                maker_choice, Constant.RV_MAKER_TYPE);
                        rvMaker.setAdapter(recyclerAdapter);

                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();

        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        layoutParams.weight = 10;
        layoutParams.height =100;

        negativeButton.setTextColor(Color.WHITE);
        negativeButton.setGravity(Gravity.CENTER);
        negativeButton.setTextSize(12f);
        negativeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.cancel));
        negativeButton.setLayoutParams(layoutParams);

        positiveButton.setTextSize(12f);
        positiveButton.setTextColor(Color.WHITE);
        positiveButton.setGravity(Gravity.CENTER);
        positiveButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.maker_submit));
        positiveButton.setLayoutParams(layoutParams);

        rvMaker.setVisibility(View.VISIBLE);
    }


    private void getDeptListForChecker() {
        JSONObject wingsJsonObject = new JSONObject();
        JSONObject deptJsonObject = new JSONObject();
        JSONArray deptArray = new JSONArray();
        try {
            deptArray.put("All");
            wingsJsonObject.put("wings", deptArray);
            deptJsonObject.put("departments", deptArray);
        }catch (JSONException j){

        }

        mApiService.gettingRoles(Constant.SCHOOL_ID, wingsJsonObject, deptJsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        checker.clear();
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("roles");


                            Iterator<String> keys = jsonObject2.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                String rolesName = jsonObjectValue.getString("role");
                                roles_status = Boolean.parseBoolean(jsonObjectValue.getString("status"));
                                if (roles_status){
                                    checker.add(rolesName);
                                }
                            }
                            Log.i("CheckerData",""+checker);
                            checkerDialogOpen(checker);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("ROLES_FAIL", String.valueOf(response.code()));
                    if (String.valueOf(response.code()).equals("409")) {

                    }
                    if (String.valueOf(response.code()).equals("404")) {

                    }
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }
    private void checkerDialogOpen(ArrayList<String> checker) {
        checkerItems.clear();
        /*if (spinnerSelectModule.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Select Checker ", Toast.LENGTH_SHORT).show();
        } else {*/
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            checker_values_a = checker.toArray(new String[checker.size()]);
            checker_boolean = new boolean[checker_values_a.length];

            TextView myMsg = new TextView(getActivity());
            myMsg.setText("Select Roles");
            myMsg.setGravity(Gravity.CENTER | Gravity.LEFT);
            myMsg.setBackgroundDrawable(getResources().getDrawable(R.drawable.checker_maker));
            myMsg.setTextSize(15);
            myMsg.setPadding(20, 0, 0, 0);
            myMsg.setHeight(80);
            myMsg.setTextColor(Color.WHITE);
            builder.setCustomTitle(myMsg);
            builder.setCancelable(true);
            builder.setMultiChoiceItems(checker_values_a,
                    checker_boolean, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                            if (isChecked) {
                                checkerItems.add(checker_values_a[position]);

                                Log.d("checker", "" + checker_values_a);
                            } else {
                                checkerItems.remove(checker_values_a[position]);
                            }
                        }
                    })
                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            checker_choice.clear();
                            for (String checker_name : checkerItems) {
                                checker_choice.add(new Data(checker_name));
                            }
                            LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
                            rvChecker.setLayoutManager(linearLayoutManager);
                            rvChecker.setHasFixedSize(true);

                            RecyclerMakerAdapter recyclerAdapter = new RecyclerMakerAdapter(getActivity(),
                                    checker_choice, Constant.RV_CHECKER_TYPE);
                            rvChecker.setAdapter(recyclerAdapter);


                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.show();

            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
            layoutParams.weight = 10;
            layoutParams.height =100;

            negativeButton.setTextColor(Color.WHITE);
            negativeButton.setTextSize(12f);
            negativeButton.setGravity(Gravity.CENTER);
            negativeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.cancel));
            negativeButton.setLayoutParams(layoutParams);

            positiveButton.setTextColor(Color.WHITE);
            positiveButton.setTextSize(12f);
            positiveButton.setGravity(Gravity.CENTER);
            positiveButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.maker_submit));
            positiveButton.setLayoutParams(layoutParams);
            rvChecker.setVisibility(View.VISIBLE);


       // }

    }
    private void convertStringArrayToString(String role, ArrayList<String> checker, ArrayList<String> maker) {
       // countChecker = 0;
      //  countMaker = 0;


        for (String checkerName : checker) {
            countChecker++;
        }

        for (String makerName : maker) {
            countMaker++;

        }


        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        for (int i=0;i< checker.size();i++){

            jsonArray.put(checker.get(i));
        }
        try {
            jsonObject.put("checkers",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray1 = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();

        for (int i=0;i< maker.size();i++){

            jsonArray1.put(maker.get(i));
        }
        try {
            jsonObject1.put("makers",jsonArray1);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        mApiService.addMakerChecker(Constant.SCHOOL_ID,role,Constant.EMPLOYEE_BY_ID,jsonObject1,jsonObject)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(getActivity(),"Add Successfully",Toast.LENGTH_SHORT).show();
                            getMakerChecker();
                        }else {
                            if (String.valueOf(response.code()).equals("409")) {
                                Toast.makeText(getActivity()," Maker and checker barriers already added for this module",
                                        Toast.LENGTH_SHORT).show();
                                getMakerChecker();

                            }
                            if (String.valueOf(response.code()).equals("404")) {
                                Toast.makeText(getActivity()," Module name does not exists",
                                        Toast.LENGTH_SHORT).show();
                                getMakerChecker();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.i("****objectMaker***err",""+t.toString());
                    }
                });

    }
    private void addData() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker.clear();
                maker.clear();
                if (checker_choice.isEmpty()) {
                    Toast.makeText(getContext(), " Select Maker and Checker ", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < makerItems.size(); i++) {
                        maker_values = maker_values + makerItems.get(i);
                        if (i != makerItems.size() - 1) {
                            maker_values = maker_values + "\n";
                        }
                    }

                    for (int i = 0; i < checkerItems.size(); i++) {
                        checker_values = checker_values + checkerItems.get(i);
                        if (i != checkerItems.size() - 1) {
                            checker_values = checker_values + "\n";
                        }
                    }


                    spinnervalue = spinnerSelectModule.getSelectedItem().toString();
                    Data data = new Data(spinnervalue, maker_values, checker_values);

                    Data data1 = new Data(checkerItems, makerItems, spinnervalue);
                    checkerMakerRole.add(data1);
                    module_choice.add(data);


                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    rvModule.setLayoutManager(linearLayoutManager);
                    rvModule.setHasFixedSize(true);

                    RecyclerMakerAdapter recyclerAdapter = new RecyclerMakerAdapter(getActivity(),
                            module_choice, Constant.SELECTED_FRAG,imageViewSave);
                    rvModule.setAdapter(recyclerAdapter);

                    myData = spinnerSelectModule.getSelectedItem().toString();
                    name.remove(myData);
                    customSpinnerAdapter.notifyDataSetChanged();

                    for (int i = 0; i < checkerMakerRole.size(); i++) {
                        String role = checkerMakerRole.get(i).getRole();

                  checker = checkerMakerRole.get(i).getChecker();
                     maker = checkerMakerRole.get(i).getMaker();

                        convertStringArrayToString(role, checker, maker);
                    }

                    //It will go to first position
                    spinnerSelectModule.setSelection(0);
                    rvChecker.setVisibility(View.GONE);
                    rvMaker.setVisibility(View.GONE);
                    checker_values = "";
                    maker_values = "";
                    spinnervalue = "";
                    checker_choice.clear();
                    maker_choice.clear();
                    checkerItems.clear();
                    makerItems.clear();
                    getModules();

                }
            }
        });

    }

}
