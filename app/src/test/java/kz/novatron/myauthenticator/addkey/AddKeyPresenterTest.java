package kz.novatron.myauthenticator.addkey;

import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import kz.novatron.myauthenticator.data.AuthKey;
import kz.novatron.myauthenticator.data.KeysRepository;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by SMustafa on 24.07.2017.
 */

public class AddKeyPresenterTest {
    public static final String SECRETKEY_TEST = "AAAA-SSSS-DDDD-FFFF";

    public static final String EMAIL_TEST = "q@example.com";

    @Mock
    AddKeyContract.View mAddKeyView;

    @Mock
    private KeysRepository mKeyRepository;

    private AddKeyPresenter mAddKeyPresenter;

    @Before
    public void setupKeysPresenter(){
        MockitoAnnotations.initMocks(this);
        mAddKeyPresenter = new AddKeyPresenter(mAddKeyView, mKeyRepository);
    }

    @Test
    public void saveKey() {
        final EditText editText = Mockito.mock(EditText.class);
        final EditText editText1 = Mockito.mock(EditText.class);
        mAddKeyPresenter.saveKey(editText, SECRETKEY_TEST, editText1, EMAIL_TEST);
        verify(mKeyRepository).saveKey(any(AuthKey.class));
        verify(mAddKeyView).showKeysList();
    }
}
