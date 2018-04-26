package vn.winwindeal.android.app.model;

import org.json.JSONObject;

import java.util.Date;

import vn.winwindeal.android.app.util.CommonUtil;

/**
 * Created by nhannguyen on 4/24/2018.
 */

public class Order {
    public int user_id;
    public int order_status_id;
    public Double total_money;
    public String created_at;
    public String updated_at;
    public String address;
    public String phone;
    public String email;

    public Order(JSONObject json) {
        if (json != null) {
            user_id = json.optInt("user_id", -1);
            order_status_id = json.optInt("order_status_id", -1);
            total_money = json.optDouble("total_money", 0);

            String strCreate = json.optString("created_at", "");
            if (!strCreate.equals("")) {
                Date createDate = CommonUtil.parseDate(strCreate, CommonUtil.DATE_FORMAT_ISODATE);
                if (createDate != null) {
                    created_at = CommonUtil.getDate(createDate, CommonUtil.DATE_FORMATE_FULL_PATTERN);
                }
            } else {
                created_at = "";
            }
            String strUpdate = json.optString("updated_at", "");
            if (!strUpdate.equals("")) {
                Date updateDate = CommonUtil.parseDate(strUpdate, CommonUtil.DATE_FORMAT_ISODATE);
                if (updateDate != null) {
                    updated_at = CommonUtil.getDate(updateDate, CommonUtil.DATE_FORMATE_FULL_PATTERN);
                }
            } else {
                updated_at = "";
            }
            address = json.optString("address", "");
            phone = json.optString("phone", "");
            email = json.optString("email", "");
        }
    }
}
