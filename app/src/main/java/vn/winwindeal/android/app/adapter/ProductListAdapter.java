package vn.winwindeal.android.app.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import vn.winwindeal.android.app.R;
import vn.winwindeal.android.app.fragment.ProductListFragment;
import vn.winwindeal.android.app.model.Product;
import vn.winwindeal.android.app.util.CommonUtil;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Product> mProducts;
    private ProductListFragment.MyMenuItemClickListener mListener;
    private int mRoleId;
    public interface ProductItemClickInterface {
        void onItemClickedListener(int position);
    }
    private ProductItemClickInterface mItemListener;

    public void setItemClickListener(ProductItemClickInterface listener) {
        this.mItemListener = listener;
    }

    public ProductListAdapter(Context context, ArrayList<Product> products, ProductListFragment.MyMenuItemClickListener listener, int roleId) {
        this.mContext = context;
        this.mProducts = products;
        this.mListener = listener;
        this.mRoleId = roleId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Product p = mProducts.get(position);
        holder.title.setText(p.product_name);
        holder.originTv.setText(mContext.getResources().getString(R.string.product_origin_hint) + ": " + p.product_origin);
        if (p.price > 0) {
            holder.priceTv.setText(String.valueOf(p.price));
        } else {
            holder.priceTv.setText(mContext.getResources().getString(R.string.price_call));
        }
        if (p.available_qty == 0) {
            holder.statusTv.setBackgroundColor(CommonUtil.getColor(mContext, R.color.colorSubTextView));
            holder.statusTv.setText(mContext.getResources().getString(R.string.out_of_stock));
        } else {
            holder.statusTv.setBackgroundColor(CommonUtil.getColor(mContext, R.color.colorPrimary));
            holder.statusTv.setText(mContext.getResources().getString(R.string.alive_lbl));
        }
        Glide.with(mContext).load(p.thumbnail).into(holder.thumbnail);
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.overflow, position);
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onItemClickedListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView title, priceTv, originTv, statusTv;
        public ImageView thumbnail, overflow;
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            priceTv = (TextView) view.findViewById(R.id.priceTv);
            originTv = (TextView) view.findViewById(R.id.originTv);
            statusTv = (TextView) view.findViewById(R.id.statusTv);
            if (mRoleId == 1) {
                overflow.setVisibility(View.GONE);
            } else {
                overflow.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, int position) {
        // inflate menu
        mListener.setPosition(position);
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.product_item_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(mListener);
        popup.show();
    }
}
