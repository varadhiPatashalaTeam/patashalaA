package in.varadhismartek.patashalaerp.Visitors;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class VisitorPassFragment extends Fragment {

    private CircleImageView civ_visitorPic;
    private ImageView iv_qrCode;
    private TextView tv_visitorName, tv_visitorId, tv_gender, tv_mobile, tv_address, tv_whomToMeet,
            tv_addedBy,tv_entryDate,tv_entryTime;

    String added_by_employee_firstname = "", staff_first_name = "",gender = "",
            visitor_id = "",visitor_photo = "",contact_number = "",entry_time = "",
            address = "",entry_date = "",visitor_name = "";


    public VisitorPassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visitor_pass, container, false);

        initViews(view);
        initListeners();
        getBundles();

        return view;
    }

    private void getBundles() {

        Bundle bundle = getArguments();

        assert bundle != null;

        added_by_employee_firstname = bundle.getString("added_by_employee_firstname");
        staff_first_name = bundle.getString("staff_first_name");
        gender = bundle.getString("gender");
        visitor_id = bundle.getString("visitor_id");
        visitor_photo = bundle.getString("visitor_photo");
        contact_number = bundle.getString("contact_number");
        entry_time = bundle.getString("entry_time");
        address = bundle.getString("address");
        entry_date = bundle.getString("entry_date");
        visitor_name = bundle.getString("visitor_name");

        if (!visitor_photo.equalsIgnoreCase("")){
            Glide.with(getActivity()).load(Constant.IMAGE_URL+visitor_photo).into(civ_visitorPic);
        }

        //iv_qrCode

        String qrCodeDetails = "VisitorId "+visitor_id+"\n gender "+gender+"\n visitor_name "+visitor_name+"\n contact_number "+contact_number+
                "\n entry_time "+entry_time+"\n entry_date "+entry_date+"\n Meeting Person "+staff_first_name+"\n address "+address;


        generateQRCode(iv_qrCode,qrCodeDetails);

        tv_visitorName.setText(visitor_name);
        tv_visitorId.setText(visitor_id);
        tv_gender.setText(gender);
        tv_mobile.setText(contact_number);
        tv_address.setText(address);
        tv_whomToMeet.setText(staff_first_name);
        tv_addedBy.setText(added_by_employee_firstname);
        tv_entryDate.setText(entry_date);
        tv_entryTime.setText(entry_time);

    }

    private void generateQRCode(ImageView iv_qrCode, String qrCodeDetails ) {

        try {
            Bitmap qrbitmap;
            qrbitmap = TextToImageEncode(qrCodeDetails);

            ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
            qrbitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes1);

            String qrpath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), qrbitmap, "QrScanner", null);

            Uri qrscannerpath = Uri.parse(qrpath);
            iv_qrCode.setImageBitmap(qrbitmap);
        } catch (WriterException e) {

            e.printStackTrace();

        }



    }

    Bitmap TextToImageEncode(String text) throws WriterException {


        BitMatrix bitMatrix;

        try {

            bitMatrix = new MultiFormatWriter().encode(text,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    500, 500, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {

            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        Color.BLACK : Color.WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);

        return bitmap;

    }



    private void initListeners() {

    }

    private void initViews(View view) {

        civ_visitorPic = view.findViewById(R.id.civ_visitorPic);
        iv_qrCode = view.findViewById(R.id.iv_qrCode);
        tv_visitorName = view.findViewById(R.id.tv_visitorName);
        tv_visitorId = view.findViewById(R.id.tv_visitorId);
        tv_gender = view.findViewById(R.id.tv_gender);
        tv_mobile = view.findViewById(R.id.tv_mobile);
        tv_address = view.findViewById(R.id.tv_address);
        tv_whomToMeet = view.findViewById(R.id.tv_whomToMeet);
        tv_addedBy = view.findViewById(R.id.tv_addedBy);
        tv_entryDate = view.findViewById(R.id.tv_entryDate);
        tv_entryTime = view.findViewById(R.id.tv_entryTime);
    }

}
