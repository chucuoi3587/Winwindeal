package vn.winwindeal.android.app.fragment;

import android.content.Intent;
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
import vn.winwindeal.android.app.HomeActivity;
import vn.winwindeal.android.app.OrderActivity;
import vn.winwindeal.android.app.R;
import vn.winwindeal.android.app.UserDetailActivity;
import vn.winwindeal.android.app.adapter.UserListAdapter;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.util.CommonUtil;
import vn.winwindeal.android.app.webservice.SearchUserWS;

public class UsersFragment extends Fragment {

    private View mView;
    Toolbar toolbar;
    TabLayout tabLayout;
    private SearchUserWS mSearchUserWs;
    private ArrayList<UserInfo> mUsers = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private UserListAdapter mAdapter;
    private int mSearchType = 0; // 0 is staff | 1 is customer

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
        tabLayout = (TabLayout) mView.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.staff_lbl)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.customer_lbl)));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) { // Staff
                    mSearchType = 0;
                    mUsers.clear();
                    doSearch();
                } else {
                    mSearchType = 1;
                    mUsers.clear();
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
        doSearch();
    }

    private void doSearch() {
        JSONArray jarray = new JSONArray();
        mSearchUserWs.doSearch(mSearchType == 0? jarray.put(2) : jarray.put(3), null);
        ((HomeActivity) getActivity()).showLoading();
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
            intent.putExtra("user", usr);
            startActivity(intent);
        }
    };
}
