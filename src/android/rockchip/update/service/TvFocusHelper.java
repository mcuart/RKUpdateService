package android.rockchip.update.service;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class TvFocusHelper {

	private TvFocusHelper() {
	}

	public static void enableRemoteFocus(View view) {
		if (view == null) {
			return;
		}
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
	}

	public static void setupDialogButtons(Button ok, Button cancel) {
		enableRemoteFocus(ok);
		enableRemoteFocus(cancel);
		if (ok != null && cancel != null) {
			ok.setNextFocusRightId(cancel.getId());
			cancel.setNextFocusLeftId(ok.getId());
		}
	}

	public static void requestFocusOnResume(final Activity activity, final View view) {
		if (view == null) {
			return;
		}
		view.post(new Runnable() {
			@Override
			public void run() {
				if (!activity.isFinishing()) {
					view.requestFocus();
				}
			}
		});
	}
}
