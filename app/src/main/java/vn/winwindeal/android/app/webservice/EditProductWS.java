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

public class EditProductWS extends DataLoader {

    private String mCode, mName, mOrigin, mThumbnail, mDescription;
    private Double mPrice;
    private int mQuantity, mIsDelete;
    private DataLoaderInterface mHandler;

    public EditProductWS(Context context) {
        super(context);
    }
    public EditProductWS(Context context, DataLoaderInterface handler) {
        super(context);
        this.mHandler = handler;
    }

    public void doEditProduct(String code, String name, double price, String origin, String thumbnail, int quantity, String description, int isDelete) {
        this.mCode = code;
        this.mName = name;
        this.mPrice = price;
        this.mOrigin = origin;
        this.mThumbnail = thumbnail;
        this.mQuantity = quantity;
        this.mDescription = description;
        this.mIsDelete = isDelete;
        isUploadFile = true;
        checkSessionTokenAndBuildRequest();
    }

    @Override
    protected void startExecuteAfterAuthenticate() {
        try {
            List<NameValuePair> paramsSearch = new ArrayList<>();
            JSONObject json = new JSONObject();
            json.accumulate("code", mCode);
            json.accumulate("name", mName);
            json.accumulate("price", String.valueOf(mPrice));
            json.accumulate("origin", mOrigin);
            json.accumulate("quantity", mQuantity);
            json.accumulate("description", mDescription);
            json.accumulate("is_deleted", mIsDelete);
            if (mThumbnail != null && !mThumbnail.equals("")) {
                dataPath = mThumbnail;
                dataKey = "thumbnail";
            }

            StringBuilder query = new StringBuilder(api + Constant.API_EDIT_PRODUCT);

            // query.append("sig=" + signature);

            Log.d("TAG", "TEST WSEditProduct " + query.toString());

            URL url = new URL(query.toString());
            execute(Constant.POST_REQUEST, Constant.REQUEST_API_EDIT_PRODUCT,
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
