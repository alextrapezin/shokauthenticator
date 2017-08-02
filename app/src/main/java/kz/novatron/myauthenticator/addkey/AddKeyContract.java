package kz.novatron.myauthenticator.addkey;

import android.widget.EditText;

import com.google.zxing.Result;

/**
 * Created by SMustafa on 27.06.2017.
 */

public interface AddKeyContract {
    interface View{
        void showKeysList();
        void showWrongSecretKeyError();
        void showEmptyFieldsError(EditText editText);
        void showMsgDialog(int messageId);
    }
    interface ScannerView{
        void startQrScanner();
        void stopQrScanner();
    }
    interface Presenter{
        void saveKey(EditText etSecretKey, String secretKey, EditText etEmail, String email);
        void saveKey(String secretKey, String email);
        void startQrScanner();
        void handleScanResult(Result rawResult);
    }
}
