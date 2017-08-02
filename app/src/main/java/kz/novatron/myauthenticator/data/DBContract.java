package kz.novatron.myauthenticator.data;

import android.provider.BaseColumns;

/**
 * Created by SMustafa on 28.06.2017.
 */

public class DBContract {
    static class AuthKey implements BaseColumns{
        static final String TABLE_NAME = "auth_key";
        static final String COLUMN_NAME_SECRET_KEY = "secret_key";
        static final String COLUMN_NAME_EMAIL = "email";
        static final String COLUMN_NAME_CREATE_DATE = "create_date";
    }
}
