package vn.winwindeal.android.app.network;

import android.content.Context;
import android.content.res.Resources;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import vn.winwindeal.android.app.util.CommonUtil;

public class JsonClient {
	private static final int CACHE_EXPIRED = 0;

	public static final int NO_RESULT = 201;

	private String mChecksum;
	private URL mSource;
	private Context mContext;
	private JSONObject mJson;
	private String mRequestMethod;
	private String mBodyContent;
	private int mResponseCode;
	private List<NameValuePair> mListOfRequestProperty = null;
	private int mReadTimeout;
	private int mConnectTimeout;
	private String mDataPath = "";
	private String mDataKey = "";
	public interface DataResponse {
		void ResultData(int requestIndex);
	}
	private DataResponse mDataResponse;

	// Just for showtimes
	public Boolean isShowtimes = false;

	// private String mJsonStream;

	public JsonClient(Context context, URL source, String requestMethod,
			List<NameValuePair> requestProperties, String bodyContent, int readTimeout, int connectTimeout) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			CommonUtil.cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"Android/data/vn.winwindeal.android.app/files/");
		else
			CommonUtil.cacheDir = context.getCacheDir();
		if (!CommonUtil.cacheDir.exists())
			CommonUtil.cacheDir.mkdirs();
		mContext = context;
		mSource = source;
		mChecksum = CommonUtil.makeMd5(source);
		mRequestMethod = requestMethod;
		mBodyContent = bodyContent;
		mListOfRequestProperty = requestProperties;
		mReadTimeout = readTimeout;
		mConnectTimeout = connectTimeout;
	}

	public void setDataPath(String path, String key) {
		this.mDataPath = path;
		this.mDataKey = key;
	}

	private int mRequestIndex;
	public void setDataResponseCallback(int requestIndex, DataResponse callback) {
		this.mRequestIndex = requestIndex;
		this.mDataResponse = callback;
	}

//	public void setSource(URL source) {
//		mSource = source;
//		mChecksum = CommonUtil.makeMd5(source);
//	}

	public String getChecksum() {
		return mChecksum;
	}

	public void connect() {
		connect(false);
	}

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");//MediaType.parse("application/json; charset=utf-8");

	public void connect(boolean forceLoad) {
		mJson = null;
		mResponseCode = -1;
		OkHttpClient client = new OkHttpClient()
				.newBuilder().retryOnConnectionFailure(false)
				.connectTimeout(mConnectTimeout, TimeUnit.MILLISECONDS)
				.readTimeout(mReadTimeout, TimeUnit.MILLISECONDS)
				.build();
//		CommonUtil.trustEveryone();

		Request.Builder request = new Request.Builder();
		request.url(mSource);
		if (mListOfRequestProperty != null) {
			NameValuePair item;
			for (int i = 0; i < mListOfRequestProperty.size(); i++) {
				item = mListOfRequestProperty.get(i);
				request.addHeader(item.getName(), item.getValue().toString().equals("android") ? "Android" : item.getValue());
			}
		}
		if (isGotOutPut(mRequestMethod)) {
			if (!mDataPath.equals("")) {
				MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
				File f = new File(mDataPath);
				builder.addFormDataPart(mDataKey, f.getName(), RequestBody.create(MediaType.parse("image/jpeg"), f));
				if (!mBodyContent.equals("")) {
					try {
						JSONObject json = new JSONObject(mBodyContent);
						Iterator<String> keys = json.keys();
						while(keys.hasNext()) {
							String key = keys.next();
							Object value = json.opt(key);
							builder.addFormDataPart(key, value instanceof String ? (String) value : String.valueOf(value));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				RequestBody body = builder.build();
				request.post(body);
				request.method(mRequestMethod, body);
			} else {
				RequestBody body = RequestBody.create(JSON, mBodyContent);
				request.method(mRequestMethod, body);
				request.post(body);
			}
		}

		client.newCall(request.build()).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, final IOException e) {
				printToJson(mResponseCode, e.getMessage());
				mDataResponse.ResultData(mRequestIndex);
			}

			@Override
			public void onResponse(Call call, final Response response) throws IOException {
				mResponseCode = response.code();
				try {
					String respStr = response.body().string().toString();
					mJson = new JSONObject(respStr);
					mDataResponse.ResultData(mRequestIndex);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private boolean isGotOutPut(String method) {
		return method.equals("GET") ? false : true;
	}

	private void printToJson(int code, String message) {
		try {
			Resources res = mContext.getResources();
			mJson = new JSONObject();
			mJson.put("resultCode", code);
			mJson.put("resultMessage", message);
			// Log.e("JsonClient", message);
		} catch (JSONException e) {
			// Log.e("JsonClient.connect", message);
		}
	}

	public JSONObject getJsonResult() {
		return mJson;
	}

	public void setJsonResult(JSONObject json) {
		mJson = json;
	}

	public String getToken() {
		if (mJson != null) {
			try {
				return mJson.getString("sessionToken");
			} catch (JSONException e) {
				// Log.e("JsonClient.getSessionToken", e.getMessage());
				return null;
			}
		}
		return null;
	}

	public boolean isSuccess() {
		// TODO: check appropriate status
		return false;
	}

	public int getResponseCode() {
		return mResponseCode;
	}

	public void cleanGarbage() {

		if (mJson != null)
			mJson = null;
		System.gc();

	}

}
