package kz.novatron.myauthenticator.keys;

import java.util.List;

import kz.novatron.myauthenticator.data.AuthKey;

/**
 * Created by SMustafa on 28.06.2017.
 */

public interface KeysContract {
    interface View{
        void showAddKey();
        void showKeys(List<AuthKey> authKeys);
        void setSeekBarProgress(int progress);
        KeysActivity getKeyActivity();
        void showMsgDialog();
    }
    interface Presenter{
        void addKey();
        void loadKeysList();
        void deleteKey(AuthKey authKey);
        void startSeekBar();
        KeysActivity getKeyActivity();
        void showMsgDialog();
    }
}
