package vn.winwindeal.android.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import vn.winwindeal.android.app.R;
import vn.winwindeal.android.app.model.Product;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private ArrayList<Product> mProducts;
    private Context mContext;

    public OrderListAdapter(Context context, ArrayList<Product> products){
        this.mContext = context;
        this.mProducts = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product p = mProducts.get(position);
        holder.nameTv.setText(p.product_name);
        if (p.price > 0) {
            holder.priceTv.setText(String.valueOf(p.price));
        } else {
            holder.priceTv.setText(mContext.getResources().getString(R.string.price_call));
        }
        Glide.with(mContext).load(p.thumbnail).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView nameTv, priceTv;
        public Spinner spinner;
        public ViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            nameTv = (TextView) view.findViewById(R.id.nameTv);
            priceTv = (TextView) view.findViewById(R.id.priceTv);
            spinner = (Spinner) view.findViewById(R.id.quantitySpinner);
        }
    }
}
