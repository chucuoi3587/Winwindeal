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
    public static final String JSON_TAG_ORDER = "orders";
    public static final String JSON_TAG_QUANTITY = "quantities";
    public static final String JSON_TAG_AVATAR = "avatar";
    public static final String JSON_TAG_ADDRESS = "address";
    public static final String JSON_TAG_PHONE = "phone";
    public static final String JSON_TAG_DISTRICT_ID = "district_id";
    public static final String JSON_TAG_USER_STATUS = "status";

    public final static String DEVICE_ID_TAG = "device_id";

    public static final String DISTRICTS = "districts";
    public static final String ROLES ="roles";
    public static final String USER_STATUS = "user_status";
    public static final String QUANTITY = "quantity";
    public static final String ORDER = "order";

    public static final String APP_API = "http://108.61.216.223:2018";
    public static final String API_REGISTER = "/register";
    public static final String API_LOGIN = "/login";
    public static final String API_LOGOUT = "/logout";
    public static final String API_ADD_PRODUCT = "/product/add";
    public static final String API_EDIT_PRODUCT = "/product/edit";
    public static final String API_LIST_ALL_PRODUCT = "/product/listProduct";
    public static final String API_SEARCH_USER ="/user/search";
    public static final String API_EDIT_USER = "/user/edit";
    public static final String API_ORDER_ADD = "/order/add";
    public static final String API_ORDER_EDIT = "/order/edit";
    public static final String API_ORDER_SEARCH = "/order/search";
    public static final String API_ORDER_DETAIL = "/order/detail";

    public static final int REQUEST_API_REGISTER = 100;
    public static final int REQUEST_API_LOGIN = 101;
    public static final int REQUEST_API_LOGOUT = 102;
    public static final int REQUEST_API_ADD_PRODUCT = 103;
    public static final int REQUEST_API_EDIT_PRODUCT = 104;
    public static final int REQUEST_API_LIST_ALL_PRODUCT = 105;
    public static final int REQUEST_API_SEARCH_USER = 106;
    public static final int REQUEST_API_EDIT_USER = 107;
    public static final int REQUEST_API_ORDER_ADD = 108;
    public static final int REQUEST_API_ORDER_EDIT = 109;
    public static final int REQUEST_API_ORDER_SEARCH = 110;
    public static final int REQUEST_API_ORDER_DETAIL = 111;

    public static final String SELECTED_PAGE = "selected_page";
    public static final int HOME_FRAGMENT = 0;
    public static final int USERS_MANAGE_FRAGMENT = 1;
    public static final int SETTINGS_FRAGMENT = 2;

    public static final int REQUEST_CREATE_PRODUCT = 1000;
    public static final int REQUEST_EDIT_PRODUCT = 1001;
    public static final int REQUEST_EDIT_USER = 1002;
    public static final int REQUEST_GET_IMAGE_CHOOSER_INTENT = 1003;

}
