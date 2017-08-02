package kz.novatron.myauthenticator.data;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.Objects;

/**
 * Created by SMustafa on 28.06.2017.
 */

public final class AuthKey implements Comparable{
    private String mSecretKey;
    private String mOutputKey;
    private String mEmail;
    private Date mCreateDate;

    public AuthKey(){}

    public AuthKey(String secretKey, String email, Date createDate){
        mSecretKey = secretKey;
        mEmail = email;
        mCreateDate = createDate;
    }

    public AuthKey(String secretKey, String email, Date createDate, String outputKey){
        mSecretKey = secretKey;
        mEmail = email;
        mCreateDate = createDate;
        mOutputKey = outputKey;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        AuthKey authKey = (AuthKey) o;
        Integer int1 = (int) (long) this.mCreateDate.getTime();
        Integer int2 = (int) (long) authKey.mCreateDate.getTime();
        return Integer.compare(int1, int2);
    }

    public String getSecretKey() {
        return mSecretKey;
    }

    public void setSecretKey(String mSecretKey) {
        this.mSecretKey = mSecretKey;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getOutputKey() {
        return mOutputKey;
    }

    public void setOutputKey(String mOutputKey) {
        this.mOutputKey = mOutputKey;
    }

    public Date getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(Date mCreateDate) {
        this.mCreateDate = mCreateDate;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        AuthKey authKey = (AuthKey) obj;
        return Objects.equals(mSecretKey, authKey.getSecretKey()) &&
                Objects.equals(mOutputKey, authKey.getOutputKey()) &&
                Objects.equals(mEmail, authKey.getEmail()) &&
                Objects.equals(mCreateDate, authKey.getCreateDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(mSecretKey, mOutputKey, mEmail, mCreateDate);
    }
}
