package kz.novatron.myauthenticator.data;

import java.util.List;

/**
 * Created by SMustafa on 28.06.2017.
 */

public interface KeysRepository {
    List<AuthKey> getAllKeys();
    void saveKey(AuthKey authKey);
    void deleteKey(AuthKey authKey);
}
