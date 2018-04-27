package vn.winwindeal.android.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.winwindeal.android.app.Constant;
import vn.winwindeal.android.app.GlobalSharedPreference;
import vn.winwindeal.android.app.HomeActivity;
import vn.winwindeal.android.app.OrderDetailActivity;
import vn.winwindeal.android.app.OrderListActivity;
import vn.winwindeal.android.app.R;
import vn.winwindeal.android.app.adapter.OrderListAdapter;
import vn.winwindeal.android.app.model.Order;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.webservice.SearchOrderWS;

/**
 * Created by nhannguyen on 4/27/2018.
 */

public class OrderFragment extends Fragment {

    private View mView;
    private SearchOrderWS mSearchWs;
    private ArrayList<Order> mOrders;
    private RecyclerView mRecyclerView;
    private OrderListAdapter mAdapter;
    private UserInfo ui;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.order_fragment_layout, container, false);
        initComponents();
        return mView;
    }

    private void initComponents() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
//        DividerItemDecoration dividerDecor = new DividerItemDecoration(OrderListActivity.this, DividerItemDecoration.VERTICAL);
//        dividerDecor.setDrawable(CommonUtil.getResourceDrawable(OrderListActivity.this, R.drawable.divider));
//        mRecyclerView.addItemDecoration(dividerDecor);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        ui = GlobalSharedPreference.getUserInfo(getActivity());

        mSearchWs = new SearchOrderWS(getActivity(), mHandler);
        getData();
    }

    private void getData() {
        if (ui.user_type == 1) {
            // admin
            JSONArray sttArray = new JSONArray();
            sttArray.put(1);
            sttArray.put(2);
            mSearchWs.doSearchOrder(null, null, sttArray, null);
            ((HomeActivity) getActivity()).showLoading();
        } else if (ui.user_type == 2) {
            // sale
        } else {
            JSONArray userArray = new JSONArray();
            userArray.put(ui.user_id);
            JSONArray sttArray = new JSONArray();
            sttArray.put(1);
            sttArray.put(2);
            mSearchWs.doSearchOrder(null, userArray, sttArray, null);
            ((HomeActivity) getActivity()).showLoading();
        }
    }

    private DataLoader.DataLoaderInterface mHandler = new DataLoader.DataLoaderInterface() {
        @Override
        public void loadDataDone(int requestIndex, int resultCode, Object result) {
            switch (requestIndex) {
                case Constant.REQUEST_API_ORDER_SEARCH:
                    JSONArray jarray = ((JSONObject) result).optJSONArray("data");
                    if (mOrders == null) {
                        mOrders = new ArrayList<>();
                    }
                    if (jarray != null && jarray.length() > 0) {
                        for (int i = 0; i < jarray.length(); i++) {
                            Order o = new Order(jarray.optJSONObject(i));
                            if (o != null) {
                                mOrders.add(o);
                            }
                        }
                        renderData();
                    }
                    break;
            }
            ((HomeActivity) getActivity()).hideLoading();
        }

        @Override
        public void loadDataFail(int requestIndex, int resultCode, Object result) {
            switch (requestIndex) {
                case Constant.REQUEST_API_ORDER_SEARCH:

                    break;
            }
            ((HomeActivity) getActivity()).hideLoading();
        }
    };

    private void renderData() {
        if (mAdapter == null) {
            mAdapter = new OrderListAdapter(getActivity(), mOrders, ui.user_type, mListener);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private OrderListAdapter.itemClickListener mListener = new OrderListAdapter.itemClickListener() {
        @Override
        public void onItemClickListener(int position) {
            Order od = mOrders.get(position);
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtra("order_id", od.order_id);
            intent.putExtra("address", od.address);
            startActivityForResult(intent, Constant.REQUEST_ORDER_DETAIL);
        }

        @Override
        public void onPhoneClickListener(int position) {
            Order od = mOrders.get(position);
            if (!od.phone.equals("")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + od.phone));
                startActivity(intent);
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_ORDER_DETAIL && resultCode == Activity.RESULT_OK) {
            mOrders.clear();
            getData();
        }
    }
}
