package vn.winwindeal.android.app.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.view.Gravity;
import android.view.View;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import vn.winwindeal.android.app.BaseActivity;
import vn.winwindeal.android.app.Constant;
import vn.winwindeal.android.app.GlobalSharedPreference;
import vn.winwindeal.android.app.LoginActivity;
import vn.winwindeal.android.app.R;
import vn.winwindeal.android.app.util.DialogUtil;
import vn.winwindeal.android.app.util.NetworkUtil;

public abstract class DataLoader {
	private JsonLoader mJsonLoader;
	protected Context mContext;
	protected static String mKPSessionToken = "";
	protected static long mKPSessionTokenExpired = 0;
	protected String appSecret;
	protected String appID;
	protected String api;
	protected String accToken;
	protected String refreshToken; 
	public static boolean hasPopup = false;
	public static boolean mIsAutoRenewConfirm = false;
	public static int READ_TIMEOUT;
	public static int CONNECT_TIMEOUT;
	public boolean isUploadFile = false;
	public String dataPath = "";
	public String dataKey = "";

	public static final int TEN_MINUTES = 1000 * 60 * 10;
	public static final String GOOGLE_MAP_API = "http://www.google.com/glm/mmap";
	private boolean mIsAutoLogoutConfirm = false;
	String strMess = "";

	public interface DataLoaderInterface {
		public void loadDataDone(int requestIndex, int resultCode, Object result);

		public void loadDataFail(int requestIndex, int resultCode, Object result);
	}

	public DataLoader(Context context) {
		mContext = context;
		appSecret = "";
		appID = "";
		api = Constant.APP_API;
		accToken = GlobalSharedPreference.getAccessToken(mContext);
		refreshToken = GlobalSharedPreference.getRefreshToken(mContext);
		READ_TIMEOUT = Constant.HTTP_READ_TIMEOUT;
		CONNECT_TIMEOUT = Constant.HTTP_CONNECTION_TIMEOUT;
	}

	/*
	 * Check valid session status. Subclass need store all params from its
	 * request to it own class and call this method. Then build url request and
	 * params in startExecuteAfterAuthenticate()
	 */
	protected void checkSessionTokenAndBuildRequest() {
		if (!NetworkUtil.isNetworkAvailable(mContext) && mContext instanceof BaseActivity) {
//			((BaseActivity) mContext).hideLoading();
//			((BaseActivity) mContext).displayErrorNetworkDialog();
			processResultsFail(500, -1, new JSONObject());
		} else {
			startExecuteAfterAuthenticate();
		}
	}

	private void doLogOut() {
//		try {
//			List<NameValuePair> properties = new ArrayList<NameValuePair>();
//			JSONObject jsonParams = new JSONObject();
//			UserInfo ui = GlobalSharedPreference.getUserInfo(mContext);
//			jsonParams.accumulate("session_token", ui.session_token);
//			jsonParams.accumulate("refresh_token", ui.refresh_token);
//			jsonParams.accumulate("device_id", GlobalSharedPreference.getDeviceId(mContext));
//			jsonParams.accumulate("ip", "");
//
//			JSONObject json = new JSONObject();
//			json.accumulate("json", Base64.encodeToString(jsonParams.toString().getBytes(), Base64.DEFAULT));
//
//			String signature = CommonUtil.getSignatureWithJSONParams(
//					Constant.WS_API_LOG_OUT, Base64.encodeToString(jsonParams.toString().getBytes(), Base64.DEFAULT), appSecret,
//					appID);
//			StringBuilder query = new StringBuilder(api
//					+ Constant.WS_API_LOG_OUT);
//
//			query.append("?");
//			query.append(convertListParamsToURLString(properties));
//			query.append("sig=" + signature);
//			// prepare request property
//			properties.add(new BasicNameValuePair("Content-Type","base64"));
//
//			Log.d("TAG", "TEST WSLogout " + query.toString());
//
//			URL url = new URL(query.toString());
//			execute(Constant.POST_REQUEST, Constant.AUTO_LOG_OUT_REQUEST, properties, json.toString(), url);
//
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	private JsonClient mClient;
	public void execute(String method, int requestIndex,
			List<NameValuePair> requestProperty, String content, URL url) {
//		if (mJsonLoader != null) {
//			mJsonLoader.cancel(true);
//		}
//
//		mJsonLoader = new JsonLoader(method, requestIndex, requestProperty,
//				content, url);
//		mJsonLoader.execute(url);

		mClient = new JsonClient(mContext, url, method,
				requestProperty, content, READ_TIMEOUT, CONNECT_TIMEOUT);
		if (isUploadFile) {
			mClient.setDataPath(isUploadFile, dataPath, dataKey);
		}
		mClient.setDataResponseCallback(requestIndex, new JsonClient.DataResponse() {
			@Override
			public void ResultData(final int requestIndex) {
				if (mContext instanceof Activity) {
					((Activity) mContext).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							setObjectDueToRequest(requestIndex, mClient);
						}
					});
				}
			}
		});
		mClient.connect();
	}

	private class JsonLoader extends AsyncTask<URL, Void, JsonClient> {
		private String mRequestMethod; // GET, POST
		private String mBodyContent; // data
		private int mRequestIndex;
		private URL mURL;
		private JsonClient mClient;
		private List<NameValuePair> mRequestProperty; // set header property for
														// HTTP connection

		protected JsonLoader(String method, int requestIndex,
				List<NameValuePair> requestProperties, String content, URL url) {
			mRequestMethod = method;
			mBodyContent = content;
			mRequestIndex = requestIndex;
			mURL = url;
			mRequestProperty = requestProperties;
		}

		@Override
		protected JsonClient doInBackground(URL... arg0) {
			try {
				mClient.connect();
			} catch (Exception ex) {
				return null;
			}
			return mClient;
		}

		@Override
		protected void onPreExecute() {
			mClient = new JsonClient(mContext, mURL, mRequestMethod,
					mRequestProperty, mBodyContent, READ_TIMEOUT, CONNECT_TIMEOUT);
		}

		@Override
		protected void onPostExecute(JsonClient result) {
			mBodyContent = null;
			mRequestMethod = null;
			mRequestProperty = null;
			setObjectDueToRequest(mRequestIndex, result);
		}
	}

	public void setObjectDueToRequest(int requestIndex, final JsonClient jsonClient) {
		try {
			if ((jsonClient != null)
					&& (jsonClient.getResponseCode() == Constant.HTTP_STATUS_OK)) {
				final JSONObject json = jsonClient.getJsonResult();
				if (requestIndex == Constant.AUTHENTICATION_REQUEST) {
					mKPSessionToken = json.optString("session_token");
					mKPSessionTokenExpired = json.optLong("expire_at");
					startExecuteAfterAuthenticate();
				} else if (requestIndex == Constant.AUTO_LOG_OUT_REQUEST/* && !mIsAutoLogoutConfirm*/) {
					hasPopup = false;
					Intent intent = new Intent(mContext, LoginActivity.class);
					intent.putExtra("force_log_out", true);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					mContext.startActivity(intent);
					if (mContext instanceof Activity) {
						((Activity) mContext).finishAffinity();
					}
				} else {
					// base on subclass is created, its method will be called
					int agreementStt = json.optInt("status", 0);
					String message = json.optString("message", "");
					if (agreementStt == Constant.REQUEST_NOT_FOUND && !hasPopup) {
						hasPopup = true;
						DialogUtil.showWarningDialog(mContext, null, mContext.getResources().getString(R.string.message_error_server), new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if (mContext instanceof BaseActivity) {
									((BaseActivity) mContext).hideLoading();
								}
								hasPopup = false;
								processResultsFail(Constant.REQUEST_SERVER_DIE, jsonClient.getResponseCode(), json);
							}
						}, Gravity.LEFT, false);
					} else if (agreementStt == Constant.HTTP_STATUS_INVALID_SESSION && !hasPopup&&!mIsAutoRenewConfirm) {
						hasPopup = true;
						strMess = "";
						if (message.equals("iap_purchase_renew")){
							mIsAutoRenewConfirm = true;
//							Intent intent = new Intent(mContext, AccountExpired2Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//							mContext.startActivity(intent);
						}
						else{
							GlobalSharedPreference.logout(mContext);
							hasPopup = false;
							Intent intent = new Intent(mContext, LoginActivity.class);
							intent.putExtra("force_log_out", true);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							mContext.startActivity(intent);
							if (mContext instanceof Activity) {
								((Activity) mContext).finishAffinity();
							}
						}
					} else {
						processResultsDone(requestIndex, jsonClient.getResponseCode(), json);
					}
				}
			} else if (jsonClient != null) {
				final JSONObject json = jsonClient.getJsonResult();
				if (jsonClient.getResponseCode() == Constant.HTTP_STATUS_INVALID_SESSION) {
				} else if (jsonClient.getResponseCode() == Constant.REQUEST_BAD_GATEWAY && !hasPopup) {
					hasPopup = true;
					DialogUtil.showWarningDialog(mContext, null, mContext.getResources().getString(R.string.message_error_server), new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (mContext instanceof BaseActivity) {
								((BaseActivity) mContext).hideLoading();
							}
							hasPopup = false;
							processResultsFail(Constant.REQUEST_SERVER_DIE, jsonClient.getResponseCode(), json);
						}
					}, Gravity.LEFT, false);
				} else {
					processResultsFail(requestIndex, jsonClient.getResponseCode(), json);
				}
			} else {
				processResultsFail(requestIndex, -1, new JSONObject());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Subclass must implement this method to build url with its params
	 */
	abstract protected void startExecuteAfterAuthenticate();

	/*
	 * Subclass must implement this method to receive results
	 */
	abstract protected void processResultsDone(int requestIndex,
			int resultCode, JSONObject jsonObject);

	/*
	 * Subclass must implement this method to receive fail results
	 */
	abstract protected void processResultsFail(int requestIndex, int failCode,
			JSONObject errorObject);

	public void cancelTransaction() {
		if (mJsonLoader != null && mJsonLoader.getStatus() != Status.FINISHED) {
			mJsonLoader.cancel(true);
		}
	}

	protected String convertListParamsToURLString(List<NameValuePair> params) {
		// Prepare body
		StringBuilder body = new StringBuilder();
		NameValuePair item;
		if (params.size() > 0) {
			try {
				int i = 0;
				for (i = 0; i < params.size(); i++) {
					item = params.get(i);

					body.append(item.getName()
							+ "="
							+ URLEncoder.encode(item.getValue() == null ? ""
									: item.getValue(), "UTF-8") + "&");

					item = null;
				}

				item = null;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return body.toString();

	}
	
	protected String convertListNameValuePairToStringForSign(List<NameValuePair> list) {
        String result = "";
        NameValuePair item;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                item = list.get(i);
                try {
                    result += item.getName()
                                        + URLEncoder.encode(item.getValue() == null ? "" : item.getValue(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
