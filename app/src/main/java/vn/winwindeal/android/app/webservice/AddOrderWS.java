package vn.winwindeal.android.app.webservice;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import vn.winwindeal.android.app.Constant;
import vn.winwindeal.android.app.GlobalSharedPreference;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.network.DataLoader;

public class AddOrderWS extends DataLoader {

    private String mAddress, mPhone;
    private JSONArray mProducts;
    private DataLoaderInterface mHandler;

    public AddOrderWS(Context context) {
        super(context);
    }
    public AddOrderWS(Context context, DataLoaderInterface handler) {
        super(context);
        this.mHandler = handler;
    }

    public void doAddOrder(String address, String phone, JSONArray products) {
        this.mAddress = address;
        this.mPhone = phone;
        this.mProducts = products;
        checkSessionTokenAndBuildRequest();
    }

    @Override
    protected void startExecuteAfterAuthenticate() {
        try {
            List<NameValuePair> paramsSearch = new ArrayList<>();
            UserInfo ui = GlobalSharedPreference.getUserInfo(mContext);
            JSONObject json = new JSONObject();
            json.accumulate("session_token", ui.access_token);
            json.accumulate("address", mAddress);
            json.accumulate("phone", mPhone);
            json.accumulate("products", mProducts);

            StringBuilder query = new StringBuilder(api + Constant.API_ADD_PRODUCT);

            Log.d("TAG", "TEST WSAddOrder " + query.toString());

            URL url = new URL(query.toString());
            execute(Constant.POST_REQUEST, Constant.REQUEST_API_ADD_PRODUCT,
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
