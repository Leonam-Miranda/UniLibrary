package com.example.unilibrary.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unilibrary.R;

public class ReaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        String url = getIntent().getStringExtra("book_url");
        String title = getIntent().getStringExtra("book_title");

        if (url == null || url.isEmpty()) {
            Toast.makeText(this, "Link inválido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView tvTitle = findViewById(R.id.tvReaderBookTitle);
        if (title != null) tvTitle.setText(title);

        ImageView btnBack = findViewById(R.id.btnBackReader);
        btnBack.setOnClickListener(v -> finish());

        setupWebView(url);
    }

    private void setupWebView(String url) {
        WebView webView = findViewById(R.id.readerWebView);
        ProgressBar progressBar = findViewById(R.id.readerProgress);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient());

        // Se for um PDF, podemos tentar usar o visualizador do Google
        if (url.toLowerCase().endsWith(".pdf")) {
            webView.loadUrl("https://docs.google.com/viewer?embedded=true&url=" + url);
        } else {
            webView.loadUrl(url);
        }
    }

    @Override
    public void onBackPressed() {
        WebView webView = findViewById(R.id.readerWebView);
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
