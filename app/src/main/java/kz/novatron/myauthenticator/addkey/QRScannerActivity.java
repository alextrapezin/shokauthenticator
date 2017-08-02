package kz.novatron.myauthenticator.addkey;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.zxing.Result;

import kz.novatron.myauthenticator.R;
import kz.novatron.myauthenticator.data.KeysRepositoryImpl;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, AddKeyContract.ScannerView{
    private ZXingScannerView mScannerView;
    private AddKeyContract.Presenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPresenter = new AddKeyPresenter(this, KeysRepositoryImpl.getInstance(this));

        mPresenter.startQrScanner();
    }

    @Override
    public void startQrScanner(){
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        mPresenter.handleScanResult(rawResult);
    }

    @Override
    public void stopQrScanner() {
        mScannerView.stopCamera();
        setResult(RESULT_OK);
        finish();
    }
}
