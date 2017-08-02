package kz.novatron.myauthenticator.keys;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import kz.novatron.myauthenticator.data.AuthKey;
import kz.novatron.myauthenticator.data.KeysRepository;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;

/**
 * Created by SMustafa on 24.07.2017.
 */

public class KeysPresenterTest {

    @Mock
    private KeysContract.View mKeysView;

    @Mock
    private KeysRepository mKeyRepository;

    private KeysPresenter mKeysPresenter;

    @Before
    public void setupPresenter(){
        MockitoAnnotations.initMocks(this);
        mKeysPresenter = new KeysPresenter(mKeysView, mKeyRepository);
    }

    @Test
    public void showsAddKeyUi(){
        mKeysPresenter.addKey();
        verify(mKeysView).showAddKey();
    }


    @Test
    public void loadKeysFromRepositoryAndShowOnUi(){
        mKeysPresenter.loadKeysList();
        verify(mKeysView).showKeys(anyListOf(AuthKey.class));
    }

    @Test
    public void deleteKey(){
        mKeysPresenter.deleteKey(any(AuthKey.class));
        verify(mKeyRepository).deleteKey(any(AuthKey.class));
    }
}
