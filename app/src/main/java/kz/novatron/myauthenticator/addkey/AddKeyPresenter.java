package kz.novatron.myauthenticator.addkey;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;

import com.google.zxing.Result;

import org.jboss.aerogear.security.otp.Totp;

import java.util.Date;

import kz.novatron.myauthenticator.Helpers.AES;
import kz.novatron.myauthenticator.data.AuthKey;
import kz.novatron.myauthenticator.data.KeysRepository;

import static kz.novatron.myauthenticator.Helpers.Utility.INVALID_QR_CODE;
import static kz.novatron.myauthenticator.Helpers.Utility.INVALID_SECRET_IN_QR_CODE;

/**
 * Created by SMustafa on 28.06.2017.
 */

public class AddKeyPresenter implements AddKeyContract.Presenter{

    private static final String SECRET_PARAM = "secret";


    private AddKeyContract.View mAddKeyView;
    private AddKeyContract.ScannerView mAddKeyScannerView;
    private KeysRepository mKeysRepository;

    public AddKeyPresenter(@NonNull AddKeyContract.View addKeyView, @NonNull KeysRepository keysRepository){
        mAddKeyView = addKeyView;
        mKeysRepository = keysRepository;
    }

    public AddKeyPresenter(@NonNull AddKeyContract.ScannerView addKeyScannerView, @NonNull KeysRepository keysRepository){
        mAddKeyScannerView = addKeyScannerView;
        mKeysRepository = keysRepository;
    }

    @Override
    public void saveKey(EditText etSecretKey, String secretKey, EditText etEmail, String email) {
        try{
            if(secretKey == null || secretKey.length() < 1 || secretKey.trim().equals(""))
                mAddKeyView.showEmptyFieldsError(etSecretKey);
            else
                if(email == null || email.length() < 1 || email.trim().equals(""))
                    mAddKeyView.showEmptyFieldsError(etEmail);
            else {
                    Log.i("Trying to add key", String.valueOf(new Totp(secretKey).now()));
                    AuthKey authKey = new AuthKey(AES.encrypt(secretKey, AES.SECRET_KEY), email, new Date());
                    mKeysRepository.saveKey(authKey);
                    mAddKeyView.showKeysList();
                }
        }
        catch (Exception e){
            if(e.getMessage().contains("length=0; index=-1"))
                mAddKeyView.showWrongSecretKeyError();
        }

    }

    @Override
    public void saveKey(String secretKey, String email) {
        try{
            Log.i("Trying to add key", String.valueOf(new Totp(secretKey).now()));
            AuthKey authKey = new AuthKey(AES.encrypt(secretKey, AES.SECRET_KEY), email, new Date());
            mKeysRepository.saveKey(authKey);
            mAddKeyView.showKeysList();
        }
        catch (Exception e){
            if(e.getMessage().contains("length=0; index=-1"))
                mAddKeyView.showMsgDialog(INVALID_SECRET_IN_QR_CODE);
        }
    }

    @Override
    public void startQrScanner(){
        mAddKeyScannerView.startQrScanner();
    }

    @Override
    public void handleScanResult(Result rawResult) {
        Uri resultUri = Uri.parse(rawResult.getText());
        String email = validateEmailInPath(resultUri.getPath());
        if(email == null)
            mAddKeyView.showMsgDialog(INVALID_QR_CODE);
        else {
            saveKey(resultUri.getQueryParameter(SECRET_PARAM), email);
            mAddKeyScannerView.stopQrScanner();
        }
    }

    private String validateEmailInPath(String path) {
        if (path == null || !path.startsWith("/")) {
            return null;
        }
        String user = path.substring(1).trim();
        if (user.length() == 0) {
            return null;
        }
        return user;
    }

}
