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

public class AssignSaleLocationWS extends DataLoader {

    private JSONArray mDistricts;
    private int mSaleId;
    private DataLoaderInterface mHandler;

    public AssignSaleLocationWS(Context context) {
        super(context);
    }
    public AssignSaleLocationWS(Context context, DataLoaderInterface handler) {
        super(context);
        this.mHandler = handler;
    }

    public void doAssign(int saleId, JSONArray districts) {
        this.mSaleId = saleId;
        this.mDistricts = districts;
        checkSessionTokenAndBuildRequest();
    }

    @Override
    protected void startExecuteAfterAuthenticate() {
        try {
            List<NameValuePair> paramsSearch = new ArrayList<>();
            JSONObject json = new JSONObject();
            json.accumulate("sale_id", mSaleId);
            json.accumulate("districts", mDistricts);
            UserInfo ui = GlobalSharedPreference.getUserInfo(mContext);
            json.accumulate("session_token", ui.access_token);

            StringBuilder query = new StringBuilder(api + Constant.API_DISTRICT_SALE_ASSIGN);

            Log.d("TAG", "TEST WSAssign Sale Location " + query.toString());

            URL url = new URL(query.toString());
            execute(Constant.POST_REQUEST, Constant.REQUEST_API_DISTRICT_SALE_ASSIGN,
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
