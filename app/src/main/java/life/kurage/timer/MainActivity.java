package life.kurage.timer;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {

	WebView webView;
	Notification notification;
	NotificationManager notificationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		webView = (WebView) findViewById(R.id.webView1);
		setup();
		webView.addJavascriptInterface(new JavaScriptCallInterface(this), "javascriptcallinterface");
		webView.loadUrl("file:///android_asset/index.html");
	}

	public void setup(){
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});
		webView.setWebChromeClient(new WebChromeClient() {
			public boolean onConsoleMessage(ConsoleMessage cm) {
				Log.d("MyApplication", cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId());
				return true;
			}
		});
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WebView.setWebContentsDebuggingEnabled(true);
		}
		WebSettings settings = webView.getSettings();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			settings.setJavaScriptEnabled(true);
			settings.setJavaScriptCanOpenWindowsAutomatically(true);
		}
		webView.clearCache(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		notify_setup();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void notify_setup() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel("main", getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
			channel.setDescription("すべての通知");
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}

	public class JavaScriptCallInterface {
		Context con;

		public JavaScriptCallInterface(Context c) {
			con = c;
		}

		@JavascriptInterface
		public void notify(String msg) {
			NotificationCompat.Builder builder = new NotificationCompat.Builder(con, getString(R.string.app_name))
					.setSmallIcon(android.R.drawable.ic_menu_info_details)
					.setContentTitle(getString(R.string.app_name))
					.setContentText(msg)
					.setPriority(NotificationCompat.PRIORITY_DEFAULT);
			NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(con);
			notificationManagerCompat.notify(NotificationManager.IMPORTANCE_DEFAULT, builder.build());
		}
	}
}