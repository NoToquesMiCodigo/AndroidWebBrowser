package com.example.testweb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

public class Inicio extends Activity {

	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
	EditText txtUrl = null;

	public final String HOME = "http://notoquesmicodigo.blogspot.com/";

	private class MiWebViewClient extends WebViewClient {

		private void iniciaCarga(WebView v) {
			if(progressBar==null){
				progressBar = new ProgressDialog(v.getContext());
			}
			if (progressBar != null && !progressBar.isShowing()) {
				progressBar.setTitle("Espere...");
				progressBar.setMessage("Cargando página...");
				progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressBar.setCancelable(false);
				pbShow();
				progressBarStatus = 0;
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						progressBarHandler.post(new Runnable() {
							public void run() {
								try {
									progressBar
											.setProgress(progressBarStatus++);
								} catch (Exception e) {
								}
							}
						});
					}
				});
			}
 		}

		private void pbCancel() {
			try {
				progressBar.cancel();
			} catch (Exception e) {
				Log.e("ERROR", e.getMessage());
			}
		}

		private void pbShow() {
			try {
				progressBar.show();
			} catch (Exception e) {
				Log.e("ERROR", e.getMessage());
			}
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			iniciaCarga(view);
			if (txtUrl != null) {
				txtUrl.setText(url);
			}
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			pbCancel();
		}
	}

	private class MiChromeWebViewClient extends WebChromeClient {

	}

	WebView wv_pagina;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicio);
		wv_pagina = (WebView) findViewById(R.id.wv_pagina);
		txtUrl = (EditText) findViewById(R.id.txtUrl);
		wv_pagina.clearHistory();
		wv_pagina.clearCache(true);
		wv_pagina.setWebViewClient(new MiWebViewClient());
		wv_pagina.setWebChromeClient(new MiChromeWebViewClient());
		wv_pagina.getSettings().setJavaScriptEnabled(true);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				wv_pagina.loadUrl(HOME);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_inicio, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (wv_pagina != null) {
			switch (item.getItemId()) {
			case R.id.menu_back:
				if (wv_pagina.canGoBack()) {
					wv_pagina.goBack();
				}
				break;
			case R.id.menu_forward:
				if (wv_pagina.canGoForward()) {
					wv_pagina.goForward();
				}
				break;
			}
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

}
