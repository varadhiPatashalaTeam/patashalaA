package in.varadhismartek.patashalaerp.GalleryModule;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GalleryGridFragment extends Fragment {


    ImageView iv_backBtn;
    RecyclerView recycler_view;
    TextView tv_date, tv_event_title;
    String galleryId = "";
    APIService mApiServicePatashala;

    ArrayList<String> list;
    GalleryAdapter adapter;

    public GalleryGridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery_grid, container, false);

        initViews(view);
        getBundles();

        iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    private void initViews(View view) {
        mApiServicePatashala = ApiUtilsPatashala.getService();
        recycler_view = view.findViewById(R.id.recycler_view);
        tv_date = view.findViewById(R.id.tv_date);
        tv_event_title = view.findViewById(R.id.tv_event_title);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        list = new ArrayList<>();
    }

    private void getBundles() {

        Bundle bundle = getArguments();
        assert bundle != null;
        //noinspection unchecked
        String date =  bundle.getString("date");
        String title =  bundle.getString("title");

        GalleryModel galleryModel = (GalleryModel) bundle.getSerializable("GalleryModel");

        galleryId = galleryModel.getGalleryId();

        String[] str = date.split("-");

        int day = Integer.parseInt(str[0]);
        int month = Integer.parseInt(str[1]);
        int year = Integer.parseInt(str[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.YEAR, year);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
        String strMonth = simpleDateFormat.format(calendar.getTime());

        Log.d("lkejhlkehfnl", strMonth);

        tv_date.setText(strMonth+" "+day+", "+year);
        tv_event_title.setText(title);

        setRecyclerView();
        gettingSchoolFullGalleryAPI();
    }

    private void setRecyclerView() {

        adapter = new GalleryAdapter(getActivity(), list, Constant.RV_GALLERY_IMAGES);
        recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void gettingSchoolFullGalleryAPI(){

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait");
        progressDialog.show();

        if (list.size()>0)
            list.clear();

        mApiServicePatashala.gettingSchoolFullGallery(Constant.SCHOOL_ID, galleryId).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));

                        String status1 = object.getString("status");
                        String message1 = object.getString("message");

                        if (status1.equalsIgnoreCase("Success")){
                            Log.d("Gallery_success_1", response.code()+" " +message1);

                            JSONObject jsonData = object.getJSONObject("data");
                            JSONObject gallery_details = jsonData.getJSONObject("gallery_details");

                            JSONArray gallery_image = gallery_details.getJSONArray("gallery_image");

                            for (int i=0; i<gallery_image.length();i++){

                                list.add(gallery_image.get(i).toString());
                            }
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();

                        }else {
                            Log.d("Gallery_else_1", response.code()+" " +message1);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("Gallery_fail_1", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }

}

























/*

    ImageView iv_backBtn;
    RecyclerView recycler_view;
    TextView tv_date, tv_event_title;

    public GalleryGridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery_grid, container, false);

        initViews(view);
        getBundles();

        iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    private void initViews(View view) {

        recycler_view = view.findViewById(R.id.recycler_view);
        tv_date = view.findViewById(R.id.tv_date);
        tv_event_title = view.findViewById(R.id.tv_event_title);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
    }

    private void getBundles() {

        Bundle bundle = getArguments();
        assert bundle != null;
        //noinspection unchecked
        ArrayList<String> list = (ArrayList<String>) bundle.getSerializable("list");
        String date =  bundle.getString("date");
        String title =  bundle.getString("title");

        String[] str = date.split("-");

        int day = Integer.parseInt(str[0]);
        int month = Integer.parseInt(str[1]);
        int year = Integer.parseInt(str[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.YEAR, year);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
        String strMonth = simpleDateFormat.format(calendar.getTime());

        Log.d("lkejhlkehfnl", strMonth);

        tv_date.setText(strMonth+" "+day+", "+year);
        tv_event_title.setText(title);

        setRecyclerView(list);

    }

    private void setRecyclerView(ArrayList<String> list) {

        GalleryAdapter adapter = new GalleryAdapter(getActivity(), list, Constant.RV_GALLERY_IMAGES);
        recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
*/


//}
