package kz.novatron.myauthenticator.keys;

import org.jboss.aerogear.security.otp.Totp;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import kz.novatron.myauthenticator.Helpers.AES;
import kz.novatron.myauthenticator.Helpers.SeekBarTimer;
import kz.novatron.myauthenticator.data.AuthKey;
import kz.novatron.myauthenticator.data.KeysRepository;

/**
 * Created by SMustafa on 28.06.2017.
 */

public class KeysPresenter implements KeysContract.Presenter{

    private KeysContract.View mKeysView;
    private KeysRepository mKeysRepository;
    private static boolean firstLaunch = true;

    public KeysPresenter(KeysContract.View keysView, KeysRepository keysRepository){
        mKeysView = keysView;
        mKeysRepository = keysRepository;
    }

    @Override
    public void addKey() {
        mKeysView.showAddKey();
    }

    @Override
    public void loadKeysList() {
        List<AuthKey> authKeys = mKeysRepository.getAllKeys();
        if(authKeys!= null && authKeys.size()>0)
            for(AuthKey authKey: authKeys){
                Totp totp = new Totp(AES.decrypt(authKey.getSecretKey(), AES.SECRET_KEY));
                String output = totp.now();
                authKey.setOutputKey(output);
            }
        if(authKeys!= null)
            Collections.sort(authKeys, Collections.reverseOrder());
        mKeysView.showKeys(authKeys);
    }

    @Override
    public void deleteKey(AuthKey authKey) {
        mKeysRepository.deleteKey(authKey);
    }

    public void startSeekBar(){
        if(firstLaunch) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int secs = calendar.get(Calendar.SECOND);
            int currentSeekBarProgress = 0;
            if (secs <= 30)
                currentSeekBarProgress = secs;
            else
                currentSeekBarProgress = secs - 30;
            mKeysView.setSeekBarProgress(currentSeekBarProgress);
            Timer timer = new Timer();
            SeekBarTimer seekBarTimer = SeekBarTimer.getInstance(this);
            timer.schedule(seekBarTimer, 0, 1000);
            firstLaunch = false;
        }
    }

    @Override
    public KeysActivity getKeyActivity(){
        return mKeysView.getKeyActivity();
    }

    @Override
    public void showMsgDialog() {
        mKeysView.showMsgDialog();
    }
}
