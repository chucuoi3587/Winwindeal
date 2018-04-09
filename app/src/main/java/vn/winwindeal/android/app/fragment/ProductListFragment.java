package vn.winwindeal.android.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.winwindeal.android.app.Constant;
import vn.winwindeal.android.app.CreateEditProductActivity;
import vn.winwindeal.android.app.GlobalSharedPreference;
import vn.winwindeal.android.app.HomeActivity;
import vn.winwindeal.android.app.R;
import vn.winwindeal.android.app.adapter.ProductListAdapter;
import vn.winwindeal.android.app.model.Product;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.util.CommonUtil;
import vn.winwindeal.android.app.webservice.ListAllProductWS;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class ProductListFragment extends Fragment implements View.OnClickListener{

    private View mView;
    private ListAllProductWS mListAllWs;
    private ArrayList<Product> mProducts;
    private RecyclerView recyclerView;
    private ProductListAdapter mAdapter;
    private UserInfo ui;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.product_list_fragment, container, false);
        initComponents();
        return mView;
    }

    private void initComponents() {
        ui = GlobalSharedPreference.getUserInfo(getActivity());
        if (ui.user_type != 1) {
            mView.findViewById(R.id.plusIcon).setVisibility(View.GONE);
        }
        mListAllWs = new ListAllProductWS(getActivity(), mHandler);
        mListAllWs.doGetAllProduct();
        ((HomeActivity) getActivity()).showLoading();
        mView.findViewById(R.id.plusIcon).setOnClickListener(this);
        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, CommonUtil.dpToPx(10, getActivity()), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private DataLoader.DataLoaderInterface mHandler = new DataLoader.DataLoaderInterface() {
        @Override
        public void loadDataDone(int requestIndex, int resultCode, Object result) {
            ((HomeActivity) getActivity()).hideLoading();
            switch (requestIndex) {
                case Constant.REQUEST_API_LIST_ALL_PRODUCT:
                    if (mProducts == null) {
                        mProducts = new ArrayList<>();
                    }
                    JSONArray jarray = ((JSONObject) result).optJSONArray("data");
                    if (jarray != null) {
                        for (int i = 0; i < jarray.length(); i++) {
                            Product p = new Product(jarray.optJSONObject(i));
                            if (p != null) {
                                mProducts.add(p);
                            }
                        }
                        renderData();
                    }
                    break;
            }
        }

        @Override
        public void loadDataFail(int requestIndex, int resultCode, Object result) {
            ((HomeActivity) getActivity()).hideLoading();
            switch (requestIndex) {
                case Constant.REQUEST_API_LIST_ALL_PRODUCT:

                    break;
            }
        }
    };

    private void renderData() {
        if (mAdapter == null) {
            mAdapter = new ProductListAdapter(getActivity(), mProducts, new MyMenuItemClickListener(), ui.user_type);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setItemClickListener(mListener);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private ProductListAdapter.ProductItemClickInterface mListener = new ProductListAdapter.ProductItemClickInterface() {
        @Override
        public void onItemClickedListener(int position) {
            Product p = mProducts.get(position);
            Intent intent = new Intent(getActivity(), CreateEditProductActivity.class);
            intent.putExtra("product", p);
            startActivityForResult(intent, Constant.REQUEST_EDIT_PRODUCT);
        }
    };

    public class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_order:

                    return true;
                case R.id.action_add_to_cart:

                    return true;
            }
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plusIcon:
                Intent intent = new Intent(getActivity(), CreateEditProductActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CREATE_PRODUCT);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_CREATE_PRODUCT:
                if (resultCode == Activity.RESULT_OK) {
                    mListAllWs.doGetAllProduct();
                    ((HomeActivity) getActivity()).showLoading();
                }
                break;
        }
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
