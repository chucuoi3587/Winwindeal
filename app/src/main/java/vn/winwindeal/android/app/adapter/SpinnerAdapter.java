package vn.winwindeal.android.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import vn.winwindeal.android.app.R;
import vn.winwindeal.android.app.model.SpinnerObj;
import vn.winwindeal.android.app.util.CommonUtil;

/**
 * Created by nhannguyen on 4/11/2018.
 */

public class SpinnerAdapter extends ArrayAdapter<SpinnerObj> {

    private SpinnerObj[] datas;
    int resource;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull SpinnerObj[] objects) {
        super(context, resource, objects);
        this.resource = resource;
        datas = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SpinnerObj obj = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(/*android.R.layout.simple_list_item_1*/resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textview = (TextView) convertView.findViewById(android.R.id.text1);
            viewHolder.textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            viewHolder.textview.setTextColor(CommonUtil.getColor(getContext(), R.color.colorTextView));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textview.setText(obj.value);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SpinnerObj obj = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(/*android.R.layout.simple_list_item_1*/resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textview = (TextView) convertView.findViewById(android.R.id.text1);
            viewHolder.textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            viewHolder.textview.setTextColor(CommonUtil.getColor(getContext(), R.color.colorTextView));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textview.setText(obj.value);
        return convertView;
    }

    class ViewHolder {
        public TextView textview;
    }
}
