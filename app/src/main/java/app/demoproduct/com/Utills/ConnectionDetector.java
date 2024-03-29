package app.demoproduct.com.Utills;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import app.demoproduct.com.MyApplication;


// Internet connection checker
public class ConnectionDetector {
	private Context _context;

	public ConnectionDetector(Context context) {
		this._context = MyApplication.getContext();
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}
}
