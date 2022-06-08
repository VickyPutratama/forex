package com.a2fulgentiusvicky.forex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.provider.DocumentsContract;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private ProgressBar loadingProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout1;
    private TextView eurTextView, btcTextView, cnyTextView, idrTextView, hkdTextView, jpyTextView, krwTextView, sarTextView, sgdTextView, usdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout1 = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout1);
        eurTextView = (TextView)findViewById(R.id.eurTextView);
        btcTextView = (TextView)findViewById(R.id.btcTextView);
        cnyTextView = (TextView)findViewById(R.id.cnyTextView);
        idrTextView = (TextView)findViewById(R.id.idrTextView);
        hkdTextView = (TextView)findViewById(R.id.hkdTextView);
        jpyTextView = (TextView)findViewById(R.id.jpyTextView);
        krwTextView = (TextView)findViewById(R.id.krwTextView);
        sarTextView = (TextView)findViewById(R.id.sarTextView);
        sgdTextView = (TextView)findViewById(R.id.sgdTextView);
        usdTextView = (TextView)findViewById(R.id.usdTextView);

        initSwipeRefreshLayout();
        initForex();
    }
    private void initSwipeRefreshLayout() {
        swipeRefreshLayout1.setOnRefreshListener(() ->{
            initForex();
            swipeRefreshLayout1.setRefreshing(false);
        });
    }
    public String formatNumber(double number, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(number);
    }
    private void initForex() {
        loadingProgressBar.setVisibility(TextView.VISIBLE);

        String url = "https://openexchangerates.org/api/latest.json?app_id=fcd4c0ac50cb4808a7e5c2bd676a4b59";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson ();
                RootModel rootModel = new gson.fromJson(new String(responseBody), RootModel.class);
                RatesModel ratesModel = rootModel.getRatesModel();

                double eur = ratesModel.getIDR() / ratesModel.getEUR();
                double btc = ratesModel.getIDR() / ratesModel.getBTC();
                double cny = ratesModel.getIDR() / ratesModel.getCNY();
                double idr = ratesModel.getIDR();
                double hkd = ratesModel.getIDR() / ratesModel.getHKD();
                double jpy = ratesModel.getIDR() / ratesModel.getJPY();
                double krw = ratesModel.getIDR() / ratesModel.getKRW();
                double sar = ratesModel.getIDR() / ratesModel.getSAR();
                double sgd = ratesModel.getIDR() / ratesModel.getSGD();
                double usd = ratesModel.getIDR() / ratesModel.getUSD();

                eurTextView.setText(formatNumber(eur, "###,##0.##"));
                btcTextView.setText(formatNumber(btc, "###,##0.##"));
                cnyTextView.setText(formatNumber(cny, "###,##0.##"));
                idrTextView.setText(formatNumber(idr, "###,##0.##"));
                hkdTextView.setText(formatNumber(hkd, "###,##0.##"));
                jpyTextView.setText(formatNumber(jpy, "###,##0.##"));
                krwTextView.setText(formatNumber(krw, "###,##0.##"));
                sarTextView.setText(formatNumber(sar, "###,##0.##"));

                loadingProgressBar.setVisibility(TextView.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), error.getMessage() ,Toast.LENGTH_SHORT).show();

                loadingProgressBar.setVisibility(TextView.INVISIBLE);
            }
        });
    }
}