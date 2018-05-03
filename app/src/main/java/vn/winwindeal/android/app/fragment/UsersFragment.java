package vn.winwindeal.android.app.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.winwindeal.android.app.Constant;
import vn.winwindeal.android.app.GlobalSharedPreference;
import vn.winwindeal.android.app.HomeActivity;
import vn.winwindeal.android.app.R;
import vn.winwindeal.android.app.UserDetailActivity;
import vn.winwindeal.android.app.adapter.UserListAdapter;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.util.CommonUtil;
import vn.winwindeal.android.app.webservice.GetSaleLocationWS;
import vn.winwindeal.android.app.webservice.SearchUserWS;

public class UsersFragment extends Fragment {

    private View mView;
    Toolbar toolbar;
    TabLayout tabLayout;
    private SearchUserWS mSearchUserWs;
    private GetSaleLocationWS mGetSaleLocationWs;
    private ArrayList<UserInfo> mUsers = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private UserListAdapter mAdapter;
    private int mSearchType = 0; // 0 is staff | 1 is customer
    UserInfo ui;
    JSONArray jarrayRoleIds, jarrayDistIds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.users_fragment, container, false);
        initComponents();
        return mView;
    }

    private void initComponents() {
        toolbar = (Toolbar) mView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        SpannableString ss = new SpannableString(getResources().getString(R.string.user_manage));
        ss.setSpan(new RelativeSizeSpan(0.85f), 0, ss.length(), 0);
        ((AppCompatActivity) getActivity()).setTitle(ss);

        ui = GlobalSharedPreference.getUserInfo(getActivity());
        tabLayout = (TabLayout) mView.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.staff_lbl)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.customer_lbl)));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) { // Staff
                    mSearchType = 0;
                    mUsers.clear();
                    jarrayRoleIds.put(2);
                    doSearch();
                } else {
                    mSearchType = 1;
                    mUsers.clear();
                    jarrayRoleIds.put(3);
                    doSearch();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        DividerItemDecoration dividerDecor = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerDecor.setDrawable(CommonUtil.getResourceDrawable(getActivity(), R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerDecor);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mSearchUserWs = new SearchUserWS(getActivity(), mHandler);
        mGetSaleLocationWs = new GetSaleLocationWS(getActivity(), mHandler);
        ((HomeActivity) getActivity()).showLoading();
        if (ui.user_type == 2) {
            // sale
            tabLayout.setVisibility(View.GONE);
            mGetSaleLocationWs.doGetSaleLocation(ui.user_id, null);
        } else {
            jarrayRoleIds = new JSONArray();
            jarrayRoleIds.put(2);
            doSearch();
        }
    }

    private void doSearch() {
        mSearchUserWs.doSearch(jarrayRoleIds, null, jarrayDistIds);
        if (!((HomeActivity) getActivity()).isLoading()) {
            ((HomeActivity) getActivity()).showLoading();
        }
    }

    private DataLoader.DataLoaderInterface mHandler = new DataLoader.DataLoaderInterface() {
        @Override
        public void loadDataDone(int requestIndex, int resultCode, Object result) {
            ((HomeActivity) getActivity()).hideLoading();
            switch (requestIndex) {
                case Constant.REQUEST_API_SEARCH_USER:
                    if (result instanceof JSONObject) {
                        JSONArray jarray = ((JSONObject) result).optJSONArray("data");
                        if (jarray != null && jarray.length() > 0) {
                            for (int i = 0; i < jarray.length(); i++) {
                                UserInfo usr = new UserInfo(jarray.optJSONObject(i));
                                if (usr != null) {
                                    mUsers.add(usr);
                                }
                            }
                        }
                        renderData();
                    }
                    break;
                case Constant.REQUEST_API_DISTRICT__SALE_GET:
                    JSONArray jsonArray = ((JSONObject) result).optJSONArray("data");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        if (jarrayDistIds == null) {
                            jarrayDistIds = new JSONArray();
                        }
                        for (int i = 0;i < jsonArray.length(); i++) {
                            jarrayDistIds.put(jsonArray.optJSONObject(i).optInt("district_id", -1));
                        }
                        jarrayRoleIds = new JSONArray();
                        jarrayRoleIds.put(3);
                        doSearch();
                    }
                    break;
            }
        }

        @Override
        public void loadDataFail(int requestIndex, int resultCode, Object result) {
            ((HomeActivity) getActivity()).hideLoading();
        }
    };

    private void renderData() {
        if (mAdapter == null) {
            mAdapter = new UserListAdapter(getActivity(), mUsers, mSearchType, mListener);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private UserListAdapter.itemClickListener mListener = new UserListAdapter.itemClickListener() {
        @Override
        public void onItemClickListener(int position) {
            UserInfo usr = mUsers.get(position);
            Intent intent = new Intent(getActivity(), UserDetailActivity.class);
            intent.putExtra("user_id", usr.user_id);
            intent.putExtra("is_editable", ui.user_type == 1 ? true : false);
            startActivity(intent);
        }

        @Override
        public void onPhoneClickListener(int position) {
            UserInfo usr = mUsers.get(position);
            if (!usr.phone.equals("") && !usr.phone.equals("null")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + usr.phone));
                startActivity(intent);
            }
        }
    };
}
