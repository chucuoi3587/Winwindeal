package vn.winwindeal.android.app.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class Product {
    public int product_id;
    public String product_name;
    public String description;
    public int quantity;
    public double price;
    public String product_origin;
    public String thumbnail;
    public int type; // 1 : banh | 2 : do uong | 3 : sua tam | 4 : snack
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

    public JSONObject parseToJsonString() {
        try {
            JSONObject json = new JSONObject();
            json.accumulate("product_id", product_id);
            json.accumulate("product_name", product_name);
            json.accumulate("description", description);
            json.accumulate("quantity", quantity);
            json.accumulate("price", price);
            json.accumulate("product_origin", product_origin);
            json.accumulate("thumbnail", thumbnail);
            json.accumulate("type", type);
            json.accumulate("isHotdeal",isHotdeal);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
