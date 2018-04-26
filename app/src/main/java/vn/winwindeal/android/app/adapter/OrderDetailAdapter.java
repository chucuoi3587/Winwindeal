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

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    private ArrayList<Product> mProducts;
    private Context mContext;
    public boolean isEdit = false;

    public OrderDetailAdapter(Context context, ArrayList<Product> products){
        this.mContext = context;
        this.mProducts = products;
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
        holder.quantityTv.setText(String.valueOf(p.quantity));
        holder.leftArrow.setVisibility(View.INVISIBLE);
        holder.rightArrow.setVisibility(View.INVISIBLE);
//        Glide.with(mContext).load(p.thumbnail).into(holder.thumbnail);
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
