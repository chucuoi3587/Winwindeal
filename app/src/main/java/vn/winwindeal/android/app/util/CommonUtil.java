package vn.winwindeal.android.app.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class CommonUtil {
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
}
