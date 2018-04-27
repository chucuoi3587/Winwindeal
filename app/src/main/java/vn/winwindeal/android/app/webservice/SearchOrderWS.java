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
import vn.winwindeal.android.app.network.DataLoader;

public class SearchOrderWS extends DataLoader {

    private JSONArray mOrderId, mUserId, mOrderStatusId, mDistrictId;
    private DataLoaderInterface mHandler;

    public SearchOrderWS(Context context) {
        super(context);
    }
    public SearchOrderWS(Context context, DataLoaderInterface handler) {
        super(context);
        this.mHandler = handler;
    }

    public void doSearchOrder(JSONArray orderId, JSONArray userId, JSONArray orderStatusId, JSONArray districtId) {
        this.mOrderId = orderId;
        this.mUserId = userId;
        this.mOrderStatusId = orderStatusId;
        this.mDistrictId = districtId;
        checkSessionTokenAndBuildRequest();
    }

    @Override
    protected void startExecuteAfterAuthenticate() {
        try {
            List<NameValuePair> paramsSearch = new ArrayList<>();
            JSONObject json = new JSONObject();
            json.accumulate("session_token", GlobalSharedPreference.getUserInfo(mContext).access_token);
            if (mOrderId != null) {
                json.accumulate("order_ids", mOrderId);
            }
            if (mUserId != null) {
                json.accumulate("user_ids", mUserId);
            }
            if (mOrderStatusId != null) {
                json.accumulate("order_status_ids", mOrderStatusId);
            }
            if (mDistrictId != null) {
                json.accumulate("district_ids", mDistrictId);
            }

            StringBuilder query = new StringBuilder(api + Constant.API_ORDER_SEARCH);

            Log.d("TAG", "TEST WSSearchOrder " + query.toString());

            URL url = new URL(query.toString());
            execute(Constant.POST_REQUEST, Constant.REQUEST_API_ORDER_SEARCH,
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
