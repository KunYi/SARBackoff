package com.uti.sartool;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyProperties;

import com.android.internal.telephony.RILConstants;
import android.os.AsyncResult;
import android.os.Message;
import android.os.Handler;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.util.Log;


public class MainActivity extends Activity {
    private final static int EVENT_OEM_RIL_MESSAGE = 1;

    private Message mMsg;
    private Handler mHandler;
    
    private Button  mSetButton;
    private SeekBar mSARValueBar;
    private Phone   mPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
        setContentView(R.layout.activity_main);

        // Get the default phone
        mPhone = PhoneFactory.getDefaultPhone();
        Log.d("KunYi", "get Phone" + mPhone);

        mSARValueBar = (SeekBar) findViewById(R.id.SARValue);
        mSetButton = (Button) findViewById(R.id.SetButton);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what) {
                case EVENT_OEM_RIL_MESSAGE:
                    AsyncResult ret = (AsyncResult)msg.obj;
                    Log.d("KunYi", "SAR Back-off, get userObj:" + ret.userObj);
                    Log.d("KunYi", "SAR Back-off, get Value:" + ret.result);
                    String[] strs = (String[])ret.result;
                    for (int i = 0; i < strs.length; i++)
                    Log.d("KunYi", "SAR get value string:" + strs[i]);
                    break;
                }
                super.handleMessage(msg);
            }
        };

        mSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = mSARValueBar.getProgress();

                String s[] = new String[2];
                s[0] = RILConstants.OEM_HOOK_STRING_SAR_SET;
                s[1] = String.valueOf(value);
                Log.d("KunYi", "SAR Back-off, set value:" + value);
                mPhone.invokeOemRilRequestStrings(s, null);

                s[0] = RILConstants.OEM_HOOK_STRING_SAR_GET;
                s[1] = null;
                mPhone.invokeOemRilRequestStrings(s, mHandler.obtainMessage(EVENT_OEM_RIL_MESSAGE));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
