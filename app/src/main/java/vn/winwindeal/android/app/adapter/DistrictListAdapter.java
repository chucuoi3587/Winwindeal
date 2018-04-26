package vn.winwindeal.android.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import vn.winwindeal.android.app.R;
import vn.winwindeal.android.app.model.District;
import vn.winwindeal.android.app.util.FontUtil;

/**
 * Created by nhannguyen on 4/26/2018.
 */

public class DistrictListAdapter extends BaseAdapter {

    private ArrayList<District> mDistricts;
    private ArrayList<Integer> mSelectedDistricts;
    private LayoutInflater mInflater;
    private Context mContext;

    public DistrictListAdapter(Context context, ArrayList<District> districts, ArrayList<Integer> selectedDistricts) {
        this.mContext = context;
        this.mDistricts = districts;
        this.mSelectedDistricts = selectedDistricts;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDistricts.size();
    }

    @Override
    public Object getItem(int position) {
        return mDistricts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.district_list_item, parent, false);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.nameTv);
            holder.imgv = (ImageView) convertView.findViewById(R.id.checkIcon);
            holder.tv.setTypeface(FontUtil.getFontAssets(mContext, FontUtil.ROBOTO_REGULAR));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        District d = mDistricts.get(position);
        if (d != null) {
            holder.tv.setText(d.district_name);
            if (!mSelectedDistricts.isEmpty() && mSelectedDistricts.indexOf(d.district_id) != -1) {
                holder.imgv.setImageResource(R.drawable.checkbox_on);
            } else {
                holder.imgv.setImageResource(R.drawable.checkbox_off);
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv;
        ImageView imgv;
    }
}
