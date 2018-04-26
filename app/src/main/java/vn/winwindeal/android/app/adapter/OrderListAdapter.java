package vn.winwindeal.android.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.winwindeal.android.app.GlobalSharedPreference;
import vn.winwindeal.android.app.R;
import vn.winwindeal.android.app.model.Order;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.util.CommonUtil;
import vn.winwindeal.android.app.util.FontUtil;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private ArrayList<Order> mOrders;
    private Context mContext;
    private JSONObject mDistrictJSON;
    int type;
    public boolean isEdit = false;
    private itemClickListener mlistener;

    public interface itemClickListener {
        void onItemClickListener(int position);
        void onPhoneClickListener(int position);
    }

    public OrderListAdapter(Context context, ArrayList<Order> orders, int type, itemClickListener listener){
        this.mContext = context;
        this.mOrders = orders;
        this.type = type;
        this.mlistener = listener;
        try {
            mDistrictJSON = new JSONObject(GlobalSharedPreference.getDistricts(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, parent.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Order order = mOrders.get(position);
        holder.dateTv.setText(order.updated_at);
        holder.emailTv.setText(order.email);
        holder.addressTv.setText(order.address);
        if (order.order_status_id == 1) {
            // new
            holder.statusTv.setText(mContext.getResources().getString(R.string.order_stt_new));
            holder.statusTv.setBackgroundResource(R.drawable.order_create_border_bg);
            holder.statusTv.setTextColor(CommonUtil.getColor(mContext, R.color.colorWhite));
        } else if (order.order_status_id == 2) {
            // processing
            holder.statusTv.setText(mContext.getResources().getString(R.string.order_stt_processing));
            holder.statusTv.setBackgroundResource(R.drawable.order_process_border_bg);
            holder.statusTv.setTextColor(CommonUtil.getColor(mContext, R.color.colorWhite));
        } else {
            // done
            holder.statusTv.setText(mContext.getResources().getString(R.string.order_stt_done));
            holder.statusTv.setBackgroundResource(R.drawable.order_done_border_bg);
            holder.statusTv.setTextColor(CommonUtil.getColor(mContext, R.color.colorTextView));
        }
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.onItemClickListener(position);
            }
        });
        if (type == 1 || type == 2) {
            // admin or sale
            holder.phoneImgv.setVisibility(View.VISIBLE);
            holder.phoneImgv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.onPhoneClickListener(position);
                }
            });
        } else {
            // customer
            holder.phoneImgv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView phoneImgv;
        public TextView emailTv, dateTv, addressTv, statusTv;
        public LinearLayout mainLayout;
        public ViewHolder(View view, Context context) {
            super(view);
            mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);
            statusTv = (TextView) view.findViewById(R.id.orderSttTv);
            emailTv = (TextView) view.findViewById(R.id.emailTv);
            addressTv = (TextView) view.findViewById(R.id.addressTv);
            dateTv = (TextView) view.findViewById(R.id.dateTv);
            emailTv.setTypeface(FontUtil.getFontAssets(context, FontUtil.ROBOTO_MEDIUM));
            dateTv.setTypeface(FontUtil.getFontAssets(context, FontUtil.ROBOTO_REGULAR));
            addressTv.setTypeface(FontUtil.getFontAssets(context, FontUtil.ROBOTO_REGULAR));
            phoneImgv = (ImageView) view.findViewById(R.id.phoneIcon);
        }
    }
}
