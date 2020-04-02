package in.varadhismartek.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;

public class Utilities {
    private static ProgressDialog mProgressDialog = null;

    @SuppressLint("NewApi")
    public static void displayProgressDialog(Context context, String message, Boolean backButtonCancelable) {
        try {
            if (mProgressDialog == null && context != null) {
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setMessage(message);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(backButtonCancelable);
                mProgressDialog.show();
            }
        } catch (Exception e) {

        }
    }

    public static void cancelProgressDialog() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog.cancel();
                mProgressDialog = null;
            }
        } catch (Exception e) {

        }
    }

   /* public void showInternetError(final Context context, final Class class_name) {

        final Dialog alertD = new Dialog(context);
        alertD.setCancelable(false);
        alertD.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertD.setContentView(R.layout.internet_activity);

        final TextView InternetCheck = (TextView) alertD.findViewById(R.id.internet_error);
        InternetCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.cancel();
                Intent intent = new Intent(context, class_name);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });

        alertD.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        alertD.show();
    }*/
}