package vn.winwindeal.android.app;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class Constant {
    public final static int HTTP_CONNECTION_TIMEOUT = 30000;
    public final static int HTTP_READ_TIMEOUT = 20000;
    public static final String POST_REQUEST = "POST";
    public static final String GET_REQUEST = "GET";
    public static final int PER_PAGE = 10;

    public final static int HTTP_STATUS_OK = 200;
    public static final int AUTHENTICATION_REQUEST = 201;
    public final static int HTTP_STATUS_INVALID_SESSION = 401;
    public final static int HTTP_STATUS_SETTING_ERROR = 403;
    public static final int AGREEMENT_CHANGE = 406;
    public static final int REQUEST_NETWORK_FAILED = 500;
    public static final int REQUEST_BAD_GATEWAY = 502;
    public static final int REQUEST_NOT_FOUND = 404;
    public static final int REQUEST_SERVER_DIE = 505;
    public static final int AUTO_LOG_OUT_REQUEST = 600;
    public static final int REQUEST_NETWORK_TIMEOUT = -1;

    public final static String JSON_TAG_EMAIL = "email";
    public static final String JSON_TAG_USER_TYPE = "user_type";
    public static final String JSON_TAG_USER_ID = "user_id";
    public final static String JSON_TAG_REFRESH_TOKEN = "refresh_token";
    public final static String JSON_TAG_ACCESS_TOKEN = "session_token";

    public static final String APP_API = "http://108.61.216.223:2018";
    public static final String API_REGISTER = "/register";
    public static final String API_LOGIN = "/login";
    public static final String API_LOGOUT = "/logout";
    public static final String API_ADD_PRODUCT = "/product/add";
    public static final String API_EDIT_PRODUCT = "/product/edit";
    public static final String API_LIST_ALL_PRODUCT = "/product/listProduct";

    public static final int REQUEST_API_REGISTER = 100;
    public static final int REQUEST_API_LOGIN = 101;
    public static final int REQUEST_API_LOGOUT = 102;
    public static final int REQUEST_API_ADD_PRODUCT = 103;
    public static final int REQUEST_API_EDIT_PRODUCT = 104;
    public static final int REQUEST_API_LIST_ALL_PRODUCT = 105;

    public static final String SELECTED_PAGE = "selected_page";
    public static final int HOME_FRAGMENT = 0;
}
