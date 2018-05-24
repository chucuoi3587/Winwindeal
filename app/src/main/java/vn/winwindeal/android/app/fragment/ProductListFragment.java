package vn.winwindeal.android.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import vn.winwindeal.android.app.Constant;
import vn.winwindeal.android.app.CreateEditProductActivity;
import vn.winwindeal.android.app.GlobalSharedPreference;
import vn.winwindeal.android.app.HomeActivity;
import vn.winwindeal.android.app.CartActivity;
import vn.winwindeal.android.app.ProductDetailActivity;
import vn.winwindeal.android.app.R;
import vn.winwindeal.android.app.adapter.ProductListAdapter;
import vn.winwindeal.android.app.model.Product;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.util.CommonUtil;
import vn.winwindeal.android.app.util.FontUtil;
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
    private TextView textCartItemCount;
    private UserInfo ui;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.product_list_fragment, container, false);
        setHasOptionsMenu(true);
        initComponents();
        return mView;
    }

    private void initComponents() {
        toolbar = (Toolbar) mView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setTitle("");
        ((TextView) toolbar.findViewById(R.id.actionbarTitle)).setText(getString(R.string.app_name));
        ((TextView) toolbar.findViewById(R.id.actionbarTitle)).setTypeface(FontUtil.getFontAssets(getActivity(), FontUtil.ROBOTO_MEDIUM));
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
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, CommonUtil.dpToPx(5, getActivity()), true));
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
                                if (ui.user_type != 1 && p.is_deleted == 0) {
                                    mProducts.add(p);
                                } else {
                                    mProducts.add(p);
                                }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.product_list_menu, menu);
        if (ui.user_type == 1 || ui.user_type == 2) {
            menu.findItem(R.id.action_cart).setVisible(false);
        } else {
            final MenuItem menuItem = menu.findItem(R.id.action_cart);

            View actionView = menuItem.getActionView();
            textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
            setupBadge();
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(menuItem);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_cart: {
                // Do something
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {
        if (textCartItemCount != null) {
            HashMap<String, String> map = GlobalSharedPreference.getProductOrder(getActivity());
            int CartItemCount = 0;
            try {
                JSONObject json = new JSONObject(map.get(Constant.QUANTITY));
                CartItemCount = json.length();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (CartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(CartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

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
            if (ui.user_type == 1) { // admin
                Product p = mProducts.get(position);
                Intent intent = new Intent(getActivity(), CreateEditProductActivity.class);
                intent.putExtra("product", p);
                startActivityForResult(intent, Constant.REQUEST_EDIT_PRODUCT);
            } else if (ui.user_type == 3) { //customer
                Product p = mProducts.get(position);
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("product", p);
                startActivity(intent);
            }
        }
    };

    public class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int mPosition;

        public MyMenuItemClickListener() {

        }

        public void setPosition(int pos) {
            this.mPosition = pos;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Product product = mProducts.get(mPosition);
            switch (item.getItemId()) {
                case R.id.action_order:
                    CommonUtil.addProductToCart(getActivity(), product);
                    Intent intent = new Intent(getActivity(), CartActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.action_add_to_cart:
                    CommonUtil.addProductToCart(getActivity(), product);
                    setupBadge();
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
