package vn.winwindeal.android.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.winwindeal.android.app.AboutUsActivity;
import vn.winwindeal.android.app.Constant;
import vn.winwindeal.android.app.ContactUsActivity;
import vn.winwindeal.android.app.GlobalSharedPreference;
import vn.winwindeal.android.app.HomeActivity;
import vn.winwindeal.android.app.LoginActivity;
import vn.winwindeal.android.app.CartActivity;
import vn.winwindeal.android.app.OrderListActivity;
import vn.winwindeal.android.app.R;
import vn.winwindeal.android.app.UserDetailActivity;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.util.DialogUtil;
import vn.winwindeal.android.app.webservice.LogoutWS;
import vn.winwindeal.android.app.webservice.SearchUserWS;

public class SettingsFragment extends Fragment implements View.OnClickListener{

    private View mView;
    private LogoutWS mLogoutWs;
    private SearchUserWS mSearchUserWs;
    private CircleImageView mAvatar;
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
        mSearchUserWs = new SearchUserWS(getActivity(), mHandler);
        mView.findViewById(R.id.logoutBtn).setOnClickListener(this);
        ui = GlobalSharedPreference.getUserInfo(getActivity());
        JSONArray jsonArray = new JSONArray();
        mSearchUserWs.doSearch(null, jsonArray.put(ui.user_id));
        ((HomeActivity) getActivity()).showLoading();
        ((TextView) mView.findViewById(R.id.emailTv)).setText(ui.email);
        mAvatar = (CircleImageView) mView.findViewById(R.id.avatarImgv);
        mView.findViewById(R.id.aboutUsLayout).setOnClickListener(this);
        mView.findViewById(R.id.contactUsLayout).setOnClickListener(this);
        mView.findViewById(R.id.cartLayout).setOnClickListener(this);
        mView.findViewById(R.id.avatarImgv).setOnClickListener(this);
        mView.findViewById(R.id.emailTv).setOnClickListener(this);
        mView.findViewById(R.id.orderHistoryLayout).setOnClickListener(this);

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
                case Constant.REQUEST_API_SEARCH_USER:
                    if (result instanceof JSONObject) {
                        JSONArray jarray = ((JSONObject) result).optJSONArray("data");
                        if (jarray != null && jarray.length() > 0) {
                            for (int i = 0; i < jarray.length(); i++) {
                                UserInfo usrInfo = new UserInfo(jarray.optJSONObject(i));
                                if (usrInfo != null) {
                                    ui.address = usrInfo.address;
                                    ui.phone = usrInfo.phone;
                                    ui.avatar = usrInfo.avatar;
                                    GlobalSharedPreference.login(getActivity(), ui);
                                    break;
                                }
                            }
                            if (!ui.avatar.equals("") && !ui.avatar.equals("null")) {
                                Glide.with(getActivity()).load(ui.avatar).into(mAvatar);
                            }
                        }
                    }
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
                intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
                break;
            case R.id.emailTv:
            case R.id.avatarImgv:
                intent = new Intent(getActivity(), UserDetailActivity.class);
                intent.putExtra("user_id", ui.user_id);
                intent.putExtra("is_editable", true);
                startActivityForResult(intent, Constant.REQUEST_EDIT_USER);
                break;
            case R.id.orderHistoryLayout:
                intent = new Intent(getActivity(), OrderListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_EDIT_USER:
                if (resultCode == Activity.RESULT_OK) {
                    UserInfo usr = data.getParcelableExtra("user");
                    if (!usr.avatar.equals("") && !usr.avatar.equals("null")) {
                        Glide.with(getActivity()).load(usr.avatar).into(mAvatar);
                    }
                    if (!ui.email.equals(usr.email)) {
                        ((TextView) mView.findViewById(R.id.emailTv)).setText(usr.email);
                    }
                    GlobalSharedPreference.login(getActivity(), usr);
                    ui = usr;
                }
                break;
        }
    }
}
