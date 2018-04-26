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

public class GetSaleLocationWS extends DataLoader {

    private Integer mSaleId, mDistrictId;
    private DataLoaderInterface mHandler;

    public GetSaleLocationWS(Context context) {
        super(context);
    }
    public GetSaleLocationWS(Context context, DataLoaderInterface handler) {
        super(context);
        this.mHandler = handler;
    }

    public void doGetSaleLocation(Integer saleId, Integer districtId) {
        this.mSaleId = saleId;
        this.mDistrictId = districtId;
        checkSessionTokenAndBuildRequest();
    }

    @Override
    protected void startExecuteAfterAuthenticate() {
        try {
            List<NameValuePair> paramsSearch = new ArrayList<>();
            JSONObject json = new JSONObject();
            UserInfo ui = GlobalSharedPreference.getUserInfo(mContext);
            json.accumulate("session_token", ui.access_token);
            if (mSaleId != null && mSaleId != -1) {
                json.accumulate("sale_id", mSaleId);
            }
            if (mDistrictId != null && mDistrictId != -1) {
                json.accumulate("district_id", mDistrictId);
            }

            StringBuilder query = new StringBuilder(api + Constant.API_DISTRICT_SALE_GET);

            Log.d("TAG", "TEST WSGet Sale Location " + query.toString());

            URL url = new URL(query.toString());
            execute(Constant.POST_REQUEST, Constant.REQUEST_API_DISTRICT__SALE_GET,
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
