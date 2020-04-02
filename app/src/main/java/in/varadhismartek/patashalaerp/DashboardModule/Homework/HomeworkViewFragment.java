package in.varadhismartek.patashalaerp.DashboardModule.Homework;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule.EmployeeActivity;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeworkViewFragment extends Fragment implements View.OnClickListener {

    ImageView iv_backBtn;
    TextView tv_work_title, tv_description, tv_fromDate, tv_toDate, tvReports;
    RecyclerView recycler_view_book, recycler_view_url, recycler_view_attachment;

    APIService apiService;
    String HomeWork_UUID = "", urlReference = "", urlDescription = "", strAttachment = "", click_Staff = "";
    ArrayList<HomeworkModel> bookHomeWorkList, urlHomeWorkList, attachmentHomeworkList;


    public HomeworkViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homework_view, container, false);

        initView(view);
        initListeners();

        getStringBundle();
        getHomeWorkDetailsAPI();


        return view;
    }

    private void getStringBundle() {

        Bundle bundle = getArguments();
        HomeWork_UUID = bundle.getString("HOMEWORK_UUID");
        click_Staff = bundle.getString("STAFF");
        Log.i("HomeWork_UUID**", "" + HomeWork_UUID + "**" + click_Staff);


        if (click_Staff.equals("EMPLOYEE")) {
            tvReports.setVisibility(View.GONE);
        } else if (click_Staff.equals("ADMIN")) {
            tvReports.setVisibility(View.VISIBLE);
        }

    }

    private void initView(View view) {

        apiService = ApiUtils.getAPIService();
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        tvReports = view.findViewById(R.id.tvReports);
        tv_work_title = view.findViewById(R.id.tv_work_title);
        tv_description = view.findViewById(R.id.tv_description);
        tv_fromDate = view.findViewById(R.id.tv_fromDate);
        tv_toDate = view.findViewById(R.id.tv_toDate);
        recycler_view_book = view.findViewById(R.id.recycler_view_book);
        recycler_view_url = view.findViewById(R.id.recycler_view_url);
        recycler_view_attachment = view.findViewById(R.id.recycler_view_attachment);

        bookHomeWorkList = new ArrayList<>();
        urlHomeWorkList = new ArrayList<>();
        attachmentHomeworkList = new ArrayList<>();



        /*bookHomeWorkList.clear();
        urlHomeWorkList.clear();
        attachmentHomeworkList.clear();*/
    }

    private void initListeners() {
        iv_backBtn.setOnClickListener(this);
        tvReports.setOnClickListener(this);

       /* iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("click_Staff***", "Homework" + click_Staff);
                if (click_Staff.equals("ADMIN")) {
                    DashBoardMenuActivity dashBoardMenuActivity = (DashBoardMenuActivity) getActivity();
                    dashBoardMenuActivity.loadFragment(Constant.HOMEWORK_INBOX_FRAGMENT, null);
                } else if (click_Staff.equals("EMPLOYEE")) {
                    EmployeeActivity dashBoardMenuActivity1 = (EmployeeActivity) getActivity();
                    dashBoardMenuActivity1.loadFragment(Constant.HOMEWORK_INBOX_FRAGMENT, null);
                }


            }
        });


        tvReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DashBoardMenuActivity dashBoardMenuActivity = (DashBoardMenuActivity) getActivity();
                Bundle bundle = new Bundle();

                bundle.putString("HOMEWORK_UUID", HomeWork_UUID);
                bundle.putString("HOMEWORK_TITLE", tv_work_title.getText().toString());
                bundle.putString("HOMEWORK_DESC", tv_description.getText().toString());
                dashBoardMenuActivity.loadFragment(Constant.HOMEWORK_REPORT_LIST, bundle);

            }
        });*/
    }

    private void getHomeWorkDetailsAPI() {

        apiService.getHomwWorkDetails(Constant.SCHOOL_ID, HomeWork_UUID).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("HOMWVIEW****", "" + response.code());
                Log.i("HOMWVIEW****", "" + response.body());
                Log.i("HOMWVIEW****", "" + HomeWork_UUID);

                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");


                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonObject1 = object.getJSONObject("data");
                            if (object.getJSONObject("data").toString().equals("{}")) {
                                //modelArrayList.clear();
                                // customSpinnerAdapter.notifyDataSetChanged();
                                //   setSpinnerForClass();


                            } else {

                                JSONObject booksJSON = jsonObject1.getJSONObject("books");
                                Iterator<String> book_Keys = booksJSON.keys();

                                while (book_Keys.hasNext()) {
                                    String bookAuthor = "null", bookCover = "null", bookName = "", bookPageNo = "";

                                    String strBookKey = book_Keys.next();
                                    JSONObject jsonObjectValue = booksJSON.getJSONObject(strBookKey);


                                    Log.i("jsonObjectValue****", "" + strBookKey + "***" + jsonObjectValue);
                                    Log.i("bookName****", "" + jsonObjectValue.get("book_name"));


                                    // bookAuthor = jsonObjectValue.getString("book_author_name");
                                    // bookCover = jsonObjectValue.getString("book_cover");
                                    bookName = jsonObjectValue.getString("book_name");
                                    bookPageNo = jsonObjectValue.getString("book_page_no");


                                    bookHomeWorkList.add(new HomeworkModel(bookAuthor, bookCover, bookName, bookPageNo));
                                }


                                JSONObject urlsJSON = jsonObject1.getJSONObject("urls");
                                Iterator<String> url_Keys = urlsJSON.keys();

                                /*URL******/

                                while (url_Keys.hasNext()) {
                                    String strUrlkey = url_Keys.next();
                                    JSONObject jsonObjectValue = urlsJSON.getJSONObject(strUrlkey);

                                    Log.i("jsonObjectValueURl****", "" + jsonObjectValue);
                                    urlReference = jsonObjectValue.getString("reference_url");
                                    urlDescription = jsonObjectValue.getString("description");

                                    urlHomeWorkList.add(new HomeworkModel(urlReference, urlDescription));
                                }


                                JSONObject attachmentsJSON = jsonObject1.getJSONObject("attachments");
                                Iterator<String> attachments_Keys = attachmentsJSON.keys();

                                while (attachments_Keys.hasNext()) {
                                    String strAttachmentskey = attachments_Keys.next();
                                    JSONObject jsonObjectValue = attachmentsJSON.getJSONObject(strAttachmentskey);

                                    Log.i("jsonObjectValueA****", "" + jsonObjectValue);
                                    strAttachment = jsonObjectValue.getString("attachment");
                                    System.out.println("homeTitle****" + strAttachment);
                                    attachmentHomeworkList.add(new HomeworkModel(strAttachment));

                                }


                                JSONObject homeWorkJSON = jsonObject1.getJSONObject("homework_detail");
                                String homeTitle, homeDescription, startDate, endDate;

                                homeTitle = homeWorkJSON.getString("homework_title");
                                homeDescription = homeWorkJSON.getString("description");
                                startDate = homeWorkJSON.getString("start_date");
                                endDate = homeWorkJSON.getString("end_date");

                                tv_work_title.setText(homeTitle);
                                tv_description.setText(homeDescription);
                                tv_fromDate.setText("From: " + startDate);
                                tv_toDate.setText("To: " + endDate);


                                setRecyclerViewBook();
                                setRecyclerViewURL();
                                setRecyclerViewAttachment();

                            }
                        }


                    } catch (JSONException je) {

                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void setRecyclerViewAttachment() {
        HomeworkAdapter adapter = new HomeworkAdapter(attachmentHomeworkList, Constant.RV_HOMEWORK_VIEW_ATTACHMENT, getActivity());
        recycler_view_attachment.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_view_attachment.setAdapter(adapter);

    }

    private void setRecyclerViewURL() {
        HomeworkAdapter adapter = new HomeworkAdapter(getActivity(), urlHomeWorkList, Constant.RV_HOMEWORK_URL);
        recycler_view_url.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view_url.setAdapter(adapter);
    }


    private void setRecyclerViewBook() {

        HomeworkAdapter adapter = new HomeworkAdapter(bookHomeWorkList, Constant.RV_HOMEWORK_BOOK, getActivity());
        recycler_view_book.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view_book.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_backBtn:
                Log.i("click_Staff***", "Homework" + click_Staff);
                if (click_Staff.equals("ADMIN")) {
                    DashBoardMenuActivity dashBoardMenuActivity = (DashBoardMenuActivity) getActivity();
                    dashBoardMenuActivity.loadFragment(Constant.HOMEWORK_INBOX_FRAGMENT, null);
                } else if (click_Staff.equals("EMPLOYEE")) {
                    Log.i("click_Staff***", "Homeworkhjkh" + click_Staff);

                    getActivity().onBackPressed();

                }

                break;

            case R.id.tvReports:
                final DashBoardMenuActivity dashBoardMenuActivity = (DashBoardMenuActivity) getActivity();
                Bundle bundle = new Bundle();

                bundle.putString("HOMEWORK_UUID", HomeWork_UUID);
                bundle.putString("HOMEWORK_TITLE", tv_work_title.getText().toString());
                bundle.putString("HOMEWORK_DESC", tv_description.getText().toString());
                dashBoardMenuActivity.loadFragment(Constant.HOMEWORK_REPORT_LIST, bundle);
                break;
        }

    }
}
