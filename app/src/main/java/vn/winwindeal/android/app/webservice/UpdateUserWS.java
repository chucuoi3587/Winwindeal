package vn.winwindeal.android.app.webservice;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import vn.winwindeal.android.app.Constant;
import vn.winwindeal.android.app.network.DataLoader;

public class UpdateUserWS extends DataLoader {

    private String mMail, mAddress, mPhone, mThumbnail;
    private int mDistrictId, mUserId, mStatus;
    private DataLoaderInterface mHandler;

    public UpdateUserWS(Context context) {
        super(context);
    }
    public UpdateUserWS(Context context, DataLoaderInterface handler) {
        super(context);
        this.mHandler = handler;
    }

    public void doUpdateUser(String email, String address, String phone, int district_id, String thumbnail, int userId, int status) {
        this.mMail = email;
        this.mAddress = address;
        this.mPhone = phone;
        this.mDistrictId = district_id;
        this.mThumbnail = thumbnail;
        this.mStatus = status;
        this.mUserId = userId;
        isUploadFile = true;
        checkSessionTokenAndBuildRequest();
    }

    @Override
    protected void startExecuteAfterAuthenticate() {
        try {
            List<NameValuePair> paramsSearch = new ArrayList<>();
            JSONObject json = new JSONObject();
            json.accumulate("email", mMail);
            json.accumulate("address", mAddress);
            json.accumulate("phone", mPhone);
            json.accumulate("district_id", mDistrictId);
            json.accumulate("id", mUserId);
            json.accumulate("status", mStatus);
            if (mThumbnail != null && !mThumbnail.equals("")) {
                dataPath = mThumbnail;
                dataKey = "avatar";
            }

            StringBuilder query = new StringBuilder(api + Constant.API_EDIT_USER);

            // query.append("sig=" + signature);

            Log.d("TAG", "TEST WSUpdateUser " + query.toString());

            URL url = new URL(query.toString());
            execute(Constant.POST_REQUEST, Constant.REQUEST_API_EDIT_USER,
                    paramsSearch, json.toString(), url);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processResultsDone(int requestIndex, int resultCode, JSONObject jsonObject) {
        if (jsonObject.optInt("status") == Constant.HTTP_STATUS_OK) {
            if (mHandler != null) {
                mHandler.loadDataDone(requestIndex, resultCode, jsonObject);
            } else if (DataLoaderInterface.class.isAssignableFrom(mContext
                    .getClass())) {
                ((DataLoaderInterface) mContext).loadDataDone(requestIndex,
                        resultCode, jsonObject);
            }
        } else {
            if (mHandler != null) {
                mHandler.loadDataFail(requestIndex, resultCode, jsonObject);
            } else if (DataLoaderInterface.class.isAssignableFrom(mContext
                    .getClass())) {
                ((DataLoaderInterface) mContext).loadDataFail(requestIndex,
                        resultCode, jsonObject);
            }
        }
    }

    @Override
    protected void processResultsFail(int requestIndex, int failCode, JSONObject errorObject) {
        if (mHandler != null) {
            mHandler.loadDataFail(requestIndex, failCode, errorObject);
        } else if (DataLoaderInterface.class.isAssignableFrom(mContext
                .getClass())) {
            ((DataLoaderInterface) mContext).loadDataFail(requestIndex,
                    failCode, errorObject);
        }
    }
}
