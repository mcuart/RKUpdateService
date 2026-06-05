package android.rockchip.update.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class NotifyDeleteActivity extends Activity {
	private static String TAG = "NotifyDeleteActivity";
	private Context mContext;
	private RKUpdateService.LocalBinder mBinder = null;
	private String mPath;
	private Button mBtnOk;
	private Button mBtnCancel;
    
    private ServiceConnection mConnection = new ServiceConnection() { 
        public void onServiceConnected(ComponentName className, IBinder service) { 
        	mBinder = (RKUpdateService.LocalBinder)service;
        	Log.d(TAG, "bind rkupdateservice completed!");
        } 

        public void onServiceDisconnected(ComponentName className) { 
        	mBinder = null;
        } 
    }; 
	
	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestory.........");
		if(mBinder != null) {
			mBinder.unLockWorkHandler();
			mContext.unbindService(mConnection);
		}
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.notify_dialog);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                android.R.drawable.ic_dialog_alert);
        setFinishOnTouchOutside(false);
        Intent startIntent = getIntent();
        TextView text= (TextView)this.findViewById(R.id.notify);
        int flag = startIntent.getIntExtra("flag", 0);
        mPath = startIntent.getStringExtra("path");
        if(flag == RKUpdateService.UPDATE_SUCCESS) {
        	text.setText(getString(R.string.update_success) + getString(R.string.ask_delete_package));
        }else if(flag == RKUpdateService.UPDATE_FAILED) {
        	text.setText(getString(R.string.update_failed) + getString(R.string.ask_delete_package));
        }
        
        mContext.bindService(new Intent(mContext, RKUpdateService.class), mConnection, Context.BIND_AUTO_CREATE);
        
		mBtnOk = (Button)this.findViewById(R.id.button_ok);
		mBtnCancel = (Button)this.findViewById(R.id.button_cancel);
		TvFocusHelper.setupDialogButtons(mBtnOk, mBtnCancel);
		
		mBtnOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "click ok!");
				if(mBinder != null) {
					mBinder.deletePackage(mPath);
				}
				finish();
			}
		});
		
		mBtnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "click cancel!");
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		TvFocusHelper.requestFocusOnResume(this, mBtnOk);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			TvFocusHelper.requestFocusWhenReady(this, mBtnOk);
		}
	}
	
}
