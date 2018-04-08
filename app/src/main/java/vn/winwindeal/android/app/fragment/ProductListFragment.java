package vn.winwindeal.android.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.winwindeal.android.app.Constant;
import vn.winwindeal.android.app.GlobalSharedPreference;
import vn.winwindeal.android.app.HomeActivity;
import vn.winwindeal.android.app.R;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.webservice.ListAllProductWS;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class ProductListFragment extends Fragment {

    private View mView;
    private ListAllProductWS mListAllWs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.product_list_fragment, container, false);
        initComponents();
        return mView;
    }

    private void initComponents() {
        UserInfo ui = GlobalSharedPreference.getUserInfo(getActivity());
        if (ui.user_type != 1) {
            mView.findViewById(R.id.plusIcon).setVisibility(View.GONE);
        }
        mListAllWs = new ListAllProductWS(getActivity(), mHandler);
        mListAllWs.doGetAllProduct();
        ((HomeActivity) getActivity()).showLoading();
    }

    private DataLoader.DataLoaderInterface mHandler = new DataLoader.DataLoaderInterface() {
        @Override
        public void loadDataDone(int requestIndex, int resultCode, Object result) {
            ((HomeActivity) getActivity()).hideLoading();
            switch (requestIndex) {
                case Constant.REQUEST_API_LIST_ALL_PRODUCT:

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
}
