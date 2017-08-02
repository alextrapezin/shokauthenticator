package kz.novatron.myauthenticator.keys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import kz.novatron.myauthenticator.Helpers.Utility;
import kz.novatron.myauthenticator.R;
import kz.novatron.myauthenticator.addkey.AddKeyActivity;
import kz.novatron.myauthenticator.addkey.QRScannerActivity;
import kz.novatron.myauthenticator.data.AuthKey;
import kz.novatron.myauthenticator.data.KeysRepositoryImpl;

import static com.google.common.base.Preconditions.checkNotNull;

public class KeysActivity extends AppCompatActivity implements KeysContract.View{
    public KeysContract.Presenter mPresenter;
    public SeekBar seekBar;
    private RecyclerView recyclerView;
    public static int DURATION = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keys);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utility.checkPermissionCamera(this);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(DURATION);
        mPresenter = new KeysPresenter(this, KeysRepositoryImpl.getInstance(this));
        recyclerView = (RecyclerView) findViewById(R.id.ouputkeys_list);
        mPresenter.startSeekBar();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i==DURATION) {
                    seekBar.setProgress(0);
                    mPresenter.loadKeysList();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_keys);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.showMsgDialog();
            }
        });

    }

    @Override
    public void setSeekBarProgress(int progress){
        seekBar.setProgress(progress);
    }

    @Override
    public KeysActivity getKeyActivity() {
        return this;
    }

    @Override
    public void showMsgDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.add_key_type));
        String addKeyOptions [] = {getResources().getString(R.string.scan_qr), getResources().getString(R.string.type_manually)};
        builder.setItems(addKeyOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        startActivity(new Intent(KeysActivity.this, QRScannerActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    case 1:
                        mPresenter.addKey();
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadKeysList();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void showAddKey() {
        Intent intent = new Intent(KeysActivity.this,AddKeyActivity.class);
        startActivity(intent);
    }

    @Override
    public void showKeys(List<AuthKey> authKeys) {
        AuthKeysAdapter mListAdapter;
        mListAdapter = new AuthKeysAdapter(authKeys);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(mListAdapter);
        mListAdapter.replaceData(authKeys);
        if(authKeys.size()<1)
            seekBar.setVisibility(View.GONE);
        else {
            seekBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private class AuthKeysAdapter extends RecyclerView.Adapter<AuthKeysAdapter.ViewHolder> {

        private List<AuthKey> mAuthKeys;

        AuthKeysAdapter(List<AuthKey> authKeys) {
            setList(authKeys);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.key_list_item, parent, false);

            return new ViewHolder(noteView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            final AuthKey authKey = mAuthKeys.get(viewHolder.getAdapterPosition());

            viewHolder.outputKey.setText(authKey.getOutputKey());
            viewHolder.email.setText(authKey.getEmail());
            viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mAuthKeys.size()>0) {
                        mPresenter.deleteKey(authKey);
                        mAuthKeys.remove(viewHolder.getAdapterPosition());
                        if (mAuthKeys.size() < 1) {
                            seekBar.setVisibility(View.GONE);
                        }
                        notifyItemRemoved(position);
                    }
                }
            });
        }

        void replaceData(List<AuthKey> authKeys) {
            setList(authKeys);
            notifyDataSetChanged();
        }

        private void setList(List<AuthKey> authKeys) {
            mAuthKeys = checkNotNull(authKeys);
        }

        @Override
        public int getItemCount() {
            return mAuthKeys.size();
        }

        public AuthKey getItem(int position) {
            return mAuthKeys.get(position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView outputKey;
            TextView email;
            Button btnDelete;

            ViewHolder(View itemView) {
                super(itemView);
                outputKey = (TextView) itemView.findViewById(R.id.tvAuthKey);
                email = (TextView) itemView.findViewById(R.id.tvEmail);
                btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            }
        }
    }
}
