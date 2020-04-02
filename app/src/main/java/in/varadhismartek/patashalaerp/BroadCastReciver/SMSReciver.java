package in.varadhismartek.patashalaerp.BroadCastReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.Interface.SMSListener;
import in.varadhismartek.patashalaerp.LoginAndRegistation.ThirdOTPFragment;

public class SMSReciver extends BroadcastReceiver {
    private static SMSListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("broadcast","is calling"+intent.getExtras());
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null)
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj .length; i++)
                {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[])                                                                                                    pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber ;
                    String message = currentMessage .getMessageBody();
                    try
                    {
                        Pattern pattern = Pattern.compile("(\\d{6})");

                        Matcher matcher = pattern.matcher(message);
                        String val = "";
                        if (matcher.find()) {
                            val = matcher.group(0);  // 6 digit number
                            Constant.mobile_otp=val;
                            ThirdOTPFragment.getinstance().SetTextToMpin(val);
                        }
                        Log.d("theotp","the otp is "+val);

                    }
                    catch(Exception e){
                        Log.d("errror",""+e);
                    }

                }
            }

        } catch (Exception e)
        {
            Log.d("errror",""+e);
        }

    }

}
