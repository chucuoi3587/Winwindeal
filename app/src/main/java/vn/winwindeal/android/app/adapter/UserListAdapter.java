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
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.util.FontUtil;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private ArrayList<UserInfo> mUsers;
    private int mType;
    private Context mContext;
    private JSONObject mDistrictJSON;
    public boolean isEdit = false;
    private itemClickListener mlistener;

    public interface itemClickListener {
        void onItemClickListener(int position);
    }

    public UserListAdapter(Context context, ArrayList<UserInfo> users, int type, itemClickListener listener){
        this.mContext = context;
        this.mUsers = users;
        this.mType = type;
        this.mlistener = listener;
        try {
            mDistrictJSON = new JSONObject(GlobalSharedPreference.getDistricts(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, parent.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final UserInfo usr = mUsers.get(position);
        holder.emailTv.setText(usr.email);
        holder.addressTv.setText(usr.address);
        if (!usr.phone.equals("") && !usr.phone.equals("null")) {
            holder.phoneTv.setText(usr.phone);
        } else {
            holder.phoneTv.setText(mContext.getResources().getString(R.string.data_empty));
        }
        holder.emailTv.setText(usr.email);
        holder.districtTv.setText(mDistrictJSON.optString(String.valueOf(usr.district_id)));
        if (!usr.avatar.equals("") && !usr.avatar.equals("null")) {
            Glide.with(mContext).load(usr.avatar).into(holder.avatar);
        }
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.onItemClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatar;
        public TextView emailTv, districtTv, phoneTv, addressTv;
        LinearLayout mainLayout;
        public ViewHolder(View view, Context context) {
            super(view);
            mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);
            avatar = (ImageView) view.findViewById(R.id.avatarImgv);
            emailTv = (TextView) view.findViewById(R.id.emailTv);
            districtTv = (TextView) view.findViewById(R.id.districtTv);
            phoneTv = (TextView) view.findViewById(R.id.phoneTv);
            addressTv = (TextView) view.findViewById(R.id.addressTv);
            emailTv.setTypeface(FontUtil.getFontAssets(context, FontUtil.ROBOTO_MEDIUM));
            districtTv.setTypeface(FontUtil.getFontAssets(context, FontUtil.ROBOTO_REGULAR));
            phoneTv.setTypeface(FontUtil.getFontAssets(context, FontUtil.ROBOTO_REGULAR));
            addressTv.setTypeface(FontUtil.getFontAssets(context, FontUtil.ROBOTO_REGULAR));
        }
    }
}
