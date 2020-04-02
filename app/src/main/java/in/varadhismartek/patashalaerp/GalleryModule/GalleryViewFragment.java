package in.varadhismartek.patashalaerp.GalleryModule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GalleryModule.GalleryView.ImageGallery;
import in.varadhismartek.patashalaerp.GalleryModule.Utils.LanguageHelper;
import in.varadhismartek.patashalaerp.GalleryModule.Utils.OnClickCallback;
import in.varadhismartek.patashalaerp.R;

public class GalleryViewFragment extends Fragment {

    public GalleryViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery_view2, container, false);

        ImageGallery imageGallery = view.findViewById(R.id.imageGallery);

        Bundle bundle = getArguments();
        List<String> list = (List<String>) bundle.getSerializable("list");

        ArrayList<String> myList = new ArrayList<>();

        for (int i=0; i<list.size();i++){

            myList.add(Constant.IMAGE_LINK +list.get(i));
        }

        imageGallery
                .setImages(myList)
                .setLanguageHelper(new LanguageHelper(getActivity())
                        .setNoImagesAvailable("No images are available!")
                        .setOutOf("out of"))
                .setOnLargeImageClickCallback(new OnClickCallback() {
                    @Override
                    public void OnClick(String currentImageUrl) {
                        Toast.makeText(getActivity(), "Clicked image", Toast.LENGTH_LONG).show();
                    }
                })
                .start();


        return view;
    }

}
