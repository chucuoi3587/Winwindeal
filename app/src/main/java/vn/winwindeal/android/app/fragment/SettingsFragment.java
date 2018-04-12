package vn.winwindeal.android.app.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.winwindeal.android.app.AboutUsActivity;
import vn.winwindeal.android.app.Constant;
import vn.winwindeal.android.app.ContactUsActivity;
import vn.winwindeal.android.app.GlobalSharedPreference;
import vn.winwindeal.android.app.HomeActivity;
import vn.winwindeal.android.app.LoginActivity;
import vn.winwindeal.android.app.OrderActivity;
import vn.winwindeal.android.app.R;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.util.DialogUtil;
import vn.winwindeal.android.app.webservice.LogoutWS;

public class SettingsFragment extends Fragment implements View.OnClickListener{

    private View mView;
    private LogoutWS mLogoutWs;
    UserInfo ui;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.settings_fragment, container, false);
        initComponents();
        return mView;
    }

    private void initComponents(){
        mLogoutWs = new LogoutWS(getActivity(), mHandler);
        mView.findViewById(R.id.logoutBtn).setOnClickListener(this);
        ui = GlobalSharedPreference.getUserInfo(getActivity());
        ((TextView) mView.findViewById(R.id.emailTv)).setText(ui.email);
        mView.findViewById(R.id.aboutUsLayout).setOnClickListener(this);
        mView.findViewById(R.id.contactUsLayout).setOnClickListener(this);
        mView.findViewById(R.id.cartLayout).setOnClickListener(this);

        if (ui.user_type == 1) {
            mView.findViewById(R.id.cartLayout).setVisibility(View.GONE);
            mView.findViewById(R.id.cartSeparate).setVisibility(View.GONE);
            mView.findViewById(R.id.orderHistoryLayout).setVisibility(View.GONE);
            mView.findViewById(R.id.orderHistorySeparate).setVisibility(View.GONE);
        }
    }

    private DataLoader.DataLoaderInterface mHandler = new DataLoader.DataLoaderInterface() {
        @Override
        public void loadDataDone(int requestIndex, int resultCode, Object result) {
            ((HomeActivity) getActivity()).hideLoading();
            switch (requestIndex) {
                case Constant.REQUEST_API_LOGOUT:
                    GlobalSharedPreference.logout(getActivity());
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finishAffinity();
                    break;
            }
        }

        @Override
        public void loadDataFail(int requestIndex, int resultCode, Object result) {
            ((HomeActivity) getActivity()).hideLoading();
        }
    };

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.logoutBtn:
                DialogUtil.showConfirmDialog(getActivity(), null, getResources().getString(R.string.log_out_warning_message), getResources().getString(R.string.ok_lbl), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLogoutWs.doLogout();
                        ((HomeActivity) getActivity()).showLoading();
                    }
                }, getResources().getString(R.string.cancel_lbl), false);
                break;
            case R.id.aboutUsLayout:
                intent = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.contactUsLayout:
                intent = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);
                break;
            case R.id.cartLayout:
                intent = new Intent(getActivity(), OrderActivity.class);
                startActivity(intent);
                break;
        }
    }
}
