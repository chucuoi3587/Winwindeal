package vn.winwindeal.android.app.util;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.TypedValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import vn.winwindeal.android.app.Constant;
import vn.winwindeal.android.app.GlobalSharedPreference;
import vn.winwindeal.android.app.ProductDetailActivity;
import vn.winwindeal.android.app.model.Product;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class CommonUtil {
    public static final String DATE_FORMAT_ISODATE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_FORMAT_ISODATE_OFFSET = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String DATE_FORMATE_FULL_PATTERN = "dd-MM-yyyy, HH:mm";
    public static File cacheDir;

    public static int getColor(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(id);
        }
    }

    public static Drawable getResourceDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(id);
        } else {
            return context.getResources().getDrawable(id, null);
        }
    }

    public static String makeMd5(URL url) {
        String md5string;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] md5hash = digest.digest(url.toString().getBytes());
            BigInteger md5hex = new BigInteger(1, md5hash);
            md5string = md5hex.toString(16);
        } catch (NoSuchAlgorithmException e) {
            md5string = Long.toHexString(url.hashCode());
            // Log.e("CommonUtil.urlToMd5String", e.getMessage());
        }
        return md5string;
    }

    public static String makeMd5(String stringBeforeSign) {
        String md5string;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] md5hash = digest.digest(stringBeforeSign.getBytes());
            BigInteger md5hex = new BigInteger(1, md5hash);
            md5string = md5hex.toString(16);
            // Now we need to zero pad it if you actually want the full 32
            // chars.
            while (md5string.length() < 32) {
                md5string = "0" + md5string;
            }
        } catch (NoSuchAlgorithmException e) {
            md5string = Long.toHexString(stringBeforeSign.hashCode());
            // Log.e("CommonUtil.urlToMd5String", e.getMessage());
        }
        return md5string;
    }

    // https connection code
    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[] { new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } }, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Converting dp to pixel
     */
    public static int dpToPx(int dp, Context context) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static void addProductToCart(Context context, Product product) {
        JSONObject json;
        JSONObject quantityJson;
        JSONArray jarray = new JSONArray();
        product.quantity = 1;
        HashMap<String, String> map = GlobalSharedPreference.getProductOrder(context);
        try {
            if (!map.get(Constant.ORDER).equals("") && !map.get(Constant.QUANTITY).equals("")) {
                json = new JSONObject(map.get(Constant.ORDER));
                quantityJson = new JSONObject(map.get(Constant.QUANTITY));
                if (json != null) {
                    jarray = json.optJSONArray(Constant.JSON_TAG_ORDER);
                    boolean isExist = false;
                    if (jarray.length() > 0) {
                        for (int i = 0;i < jarray.length(); i++) {
                            Product p = new Product(jarray.optJSONObject(i));
                            if (p.product_id == product.product_id) {
                                int quantity = quantityJson.getInt(String.valueOf(p.product_id));
                                quantity += 1;
                                quantityJson.put(String.valueOf(p.product_id), quantity);
                                isExist = true;
                                break;
                            }
                        }
                    }
                    map = new HashMap<>();
                    if (isExist) {
                        map.put(Constant.ORDER, json.toString());
                        map.put(Constant.QUANTITY, quantityJson.toString());
                    } else {
                        jarray.put(product.parseToJson());
                        json = new JSONObject();
                        json.accumulate(Constant.JSON_TAG_ORDER, jarray);
                        quantityJson.accumulate(String.valueOf(product.product_id), 1);
                        map.put(Constant.QUANTITY, quantityJson.toString());
                        map.put(Constant.ORDER, json.toString());
                    }
                    GlobalSharedPreference.saveProductOrder(context, map);
                }
            } else {
                jarray.put(product.parseToJson());
                json = new JSONObject();
                json.accumulate(Constant.JSON_TAG_ORDER, jarray);
                quantityJson = new JSONObject();
                quantityJson.accumulate(String.valueOf(product.product_id), 1);
                map = new HashMap<>();
                map.put(Constant.QUANTITY, quantityJson.toString());
                map.put(Constant.ORDER, json.toString());
                GlobalSharedPreference.saveProductOrder(context, map);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String addBitmapToStorage(Context context, Bitmap bmp, String name) {
        String result = "";
        try {
            File dist = new File(getFileDir(context) + "/" + name);
            OutputStream os = new FileOutputStream(dist);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            result = dist.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getFileDir(Context context) {
        File fileDir = new File(android.os.Environment.getExternalStorageDirectory(),
                "Android/data/" + context.getPackageName() + "/files/");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir.getPath();
    }

    public static void clearFileDir(Context context) {
        File fileDir = new File(android.os.Environment.getExternalStorageDirectory(),
                "Android/data/" + context.getPackageName() + "/files/");
        if (fileDir.exists()) {
            File[] files = fileDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                file.delete();
            }
        }
    }

    public static Date parseDate(String strDate, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        formatter.setTimeZone(TimeZone.getDefault());
        try {
            // Date date = formatter.parse(strDate);
            return formatter.parse(strDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String getDate(Date date, String strFormate) {
        SimpleDateFormat formatter = new SimpleDateFormat(strFormate, Locale.US);
        // formatter.setTimeZone(TimeZone.getDefault());
        TimeZone timezone = TimeZone.getDefault();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MILLISECOND, timezone.getRawOffset());
        return formatter.format(cal.getTime());
    }

    public static String getDateWithoutOffset(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return formatter.format(cal.getTime());
    }

    public static String parseNumberToString(double number, String formatpattern) {
        DecimalFormat format = new DecimalFormat(formatpattern);
        return format.format(number);
    }

    public static String getPathFromUri(Context context, Uri uri) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        String path = "";
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            if (columnIndex != -1) {
                path = cursor.getString(columnIndex);
            } else {
                path = cursor.getString(0);
            }
            cursor.close();
        } else {
            path = uri.getPath();
        }
        return path;
    }
}
