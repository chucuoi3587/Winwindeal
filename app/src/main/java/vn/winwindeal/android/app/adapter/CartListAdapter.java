package vn.winwindeal.android.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.winwindeal.android.app.R;
import vn.winwindeal.android.app.model.Product;
import vn.winwindeal.android.app.util.DialogUtil;
import vn.winwindeal.android.app.util.FontUtil;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private ArrayList<Product> mProducts;
    private JSONObject mQuantityJson;
    private Context mContext;
    public boolean isEdit = false;

    public CartListAdapter(Context context, ArrayList<Product> products, JSONObject quantity){
        this.mContext = context;
        this.mProducts = products;
        this.mQuantityJson = quantity;
    }

    public ArrayList<Product> getProducts() {
        return mProducts;
    }

    public JSONObject getQuantityJson() {
        return mQuantityJson;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, parent.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Product p = mProducts.get(position);
        holder.nameTv.setText(p.product_name);
        if (p.price > 0) {
            holder.priceTv.setText(String.valueOf(p.price));
        } else {
            holder.priceTv.setText(mContext.getResources().getString(R.string.price_call));
        }
        try {
            int quantity = mQuantityJson.getInt(String.valueOf(p.product_id));
            holder.quantityTv.setText(String.valueOf(quantity));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Glide.with(mContext).load(p.thumbnail).into(holder.thumbnail);
        holder.leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(holder.quantityTv.getText().toString().trim());
                if (i == 1) {
                    DialogUtil.showConfirmDialog(mContext, null, mContext.getResources().getString(R.string.remove_product_order_warning), null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mProducts.remove(position);
                            mQuantityJson.remove(String.valueOf(p.product_id));
                            notifyDataSetChanged();
                            isEdit = true;
                        }
                    }, null, true);
                } else {
                    i -= 1;
                    holder.quantityTv.setText(String.valueOf(i));
                    try {
                        mQuantityJson.put(String.valueOf(p.product_id), i);
                        isEdit = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        holder.rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(holder.quantityTv.getText().toString().trim());
                i += 1;
                holder.quantityTv.setText(String.valueOf(i));
                try {
                    mQuantityJson.put(String.valueOf(p.product_id), i);
                    isEdit = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail, leftArrow, rightArrow;
        public TextView nameTv, priceTv, quantityTv;
        public ViewHolder(View view, Context context) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            nameTv = (TextView) view.findViewById(R.id.nameTv);
            priceTv = (TextView) view.findViewById(R.id.priceTv);
            quantityTv = (TextView) view.findViewById(R.id.quantityTv);
            leftArrow = (ImageView) view.findViewById(R.id.leftArrow);
            rightArrow = (ImageView) view.findViewById(R.id.rightArrow);
            nameTv.setTypeface(FontUtil.getFontAssets(context, FontUtil.ROBOTO_MEDIUM));
            priceTv.setTypeface(FontUtil.getFontAssets(context, FontUtil.ROBOTO_REGULAR));
            quantityTv.setTypeface(FontUtil.getFontAssets(context, FontUtil.ROBOTO_REGULAR));
        }
    }
}
