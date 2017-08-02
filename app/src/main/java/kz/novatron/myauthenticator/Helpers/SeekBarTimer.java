package kz.novatron.myauthenticator.Helpers;

import java.util.TimerTask;

import kz.novatron.myauthenticator.keys.KeysContract;

import static kz.novatron.myauthenticator.keys.KeysActivity.DURATION;

/**
 * Created by SMustafa on 29.06.2017.
 */

public class SeekBarTimer extends TimerTask {
    private KeysContract.Presenter mPresenter;
    private int DIV = DURATION / 100;

    private static SeekBarTimer sInstance;

    public static synchronized SeekBarTimer getInstance(KeysContract.Presenter presenter){
        if(sInstance==null){
            sInstance = new SeekBarTimer(presenter);
        }
        return sInstance;
    }

    private SeekBarTimer(KeysContract.Presenter presenter){
        mPresenter = presenter;
    }
    @Override
    public void run() {
        mPresenter.getKeyActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!(DIV * mPresenter.getKeyActivity().seekBar.getProgress() >= DURATION)) {
                    int p = mPresenter.getKeyActivity().seekBar.getProgress();
                    p += 1;
                    mPresenter.getKeyActivity().seekBar.setProgress(p);
                }
            }
        });
    };
}
