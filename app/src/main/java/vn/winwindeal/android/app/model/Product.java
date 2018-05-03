package vn.winwindeal.android.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import vn.winwindeal.android.app.util.CommonUtil;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class Product implements Parcelable{
    public int product_id;
    public String product_name;
    public String code;
    public String description;
    public int quantity;
    public int available_qty;
    public int pending_qty;
    public double price;
    public String product_origin;
    public String thumbnail;
    public int type; // 1 : banh | 2 : do uong | 3 : sua tam | 4 : snack
    public int status;
    public int is_deleted; // 0 is alive | 1 is deleted
    public boolean isHotdeal;
    public String created_at;
    public String updated_at;

    public Product(int product_id, String product_name, String description, int quantity,
                   double price, String product_origin, String thumbnail, int type, boolean isHotdeal) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.product_origin = product_origin;
        this.thumbnail = thumbnail;
        this.type = type;
        this.isHotdeal = isHotdeal;
    }

    public JSONObject parseToJson() {
        try {
            JSONObject json = new JSONObject();
            json.accumulate("id", product_id);
            json.accumulate("name", product_name);
            json.accumulate("description", description);
            json.accumulate("quantity", quantity);
            json.accumulate("price", price);
            json.accumulate("code", code);
            json.accumulate("origin", product_origin);
            json.accumulate("thumbnail", thumbnail);
            json.accumulate("type_id", type);
            json.accumulate("status", status);
            json.accumulate("is_deleted", is_deleted);
            json.accumulate("isHotdeal",isHotdeal);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Product(JSONObject json) {
        if (json != null) {
            product_id = json.optInt("id", -1);
            if (product_id == -1) {
                product_id = json.optInt("product_id", -1);
            }
            code = json.optString("code");
            product_name = json.optString("name");
            price = json.optDouble("price");
            thumbnail = json.optString("thumbnail");
            product_origin = json.optString("origin");
            status = json.optInt("status");
            description = json.optString("description", "");
            quantity = json.optInt("quantity",0);
            available_qty = json.optInt("available_qty",0);
            pending_qty = json.optInt("pending_qty",0);
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
            is_deleted = json.optInt("is_deleted", 0);
        }
    }

    public Product(Parcel in) {
        product_id = in.readInt();
        code = in.readString();
        product_name = in.readString();
        product_origin = in.readString();
        thumbnail = in.readString();
        price = in.readDouble();
        status = in.readInt();
        description = in.readString();
        quantity = in.readInt();
        available_qty = in.readInt();
        pending_qty = in.readInt();
        is_deleted = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(product_id);
        dest.writeString(code);
        dest.writeString(product_name);
        dest.writeString(product_origin);
        dest.writeString(thumbnail);
        dest.writeDouble(price);
        dest.writeInt(status);
        dest.writeString(description);
        dest.writeInt(quantity);
        dest.writeInt(available_qty);
        dest.writeInt(pending_qty);
        dest.writeInt(is_deleted);
    }

    public static final Creator CREATOR = new Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
