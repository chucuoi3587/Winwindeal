package vn.winwindeal.android.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class Product implements Parcelable{
    public int product_id;
    public String product_name;
    public String code;
    public String description;
    public int quantity;
    public double price;
    public String product_origin;
    public String thumbnail;
    public int type; // 1 : banh | 2 : do uong | 3 : sua tam | 4 : snack
    public int status;
    public boolean isHotdeal;

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
            json.accumulate("thumnail", thumbnail);
            json.accumulate("type_id", type);
            json.accumulate("status", status);
            json.accumulate("isHotdeal",isHotdeal);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Product(JSONObject json) {
        if (json != null) {
            product_id = json.optInt("id");
            code = json.optString("code");
            product_name = json.optString("name");
            price = json.optDouble("price");
            thumbnail = json.optString("thumnail");
            product_origin = json.optString("origin");
            status = json.optInt("status");
            description = json.optString("description", "");
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
