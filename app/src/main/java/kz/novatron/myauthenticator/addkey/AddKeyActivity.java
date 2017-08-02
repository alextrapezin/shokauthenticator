package kz.novatron.myauthenticator.addkey;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import kz.novatron.myauthenticator.R;
import kz.novatron.myauthenticator.data.KeysRepositoryImpl;

import static kz.novatron.myauthenticator.Helpers.Utility.INVALID_QR_CODE;

public class AddKeyActivity extends AppCompatActivity implements AddKeyContract.View{
    private AddKeyContract.Presenter mAddKeyPresenter;
    private EditText etSecretKey, etMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAddKeyPresenter = new AddKeyPresenter(this, KeysRepositoryImpl.getInstance(this));
        etSecretKey = (EditText) findViewById(R.id.etSecretKey);
        etMail = (EditText) findViewById(R.id.etEmail);
        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddKeyPresenter.saveKey(etSecretKey, etSecretKey.getText().toString(), etMail, etMail.getText().toString());
            }
        });
    }

    @Override
    public void showKeysList() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showWrongSecretKeyError() {
        etSecretKey.setError(getResources().getString(R.string.wrong_secret_key_format));
    }

    @Override
    public void showEmptyFieldsError(EditText editText) {
        editText.setError(getResources().getString(R.string.required_field));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showMsgDialog(int messageId){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        if(messageId == INVALID_QR_CODE)
            alertDialogBuilder.setMessage(R.string.invalid_qr_code);
        else
            alertDialogBuilder.setMessage(R.string.wrong_secret_key_format);
        alertDialogBuilder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
