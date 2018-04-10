package vn.winwindeal.android.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import vn.winwindeal.android.app.R;

public class UsersFragment extends Fragment {

    private View mView;
    Toolbar toolbar;
    TabLayout tabLayout;
    private ListView mLv;

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

        mLv = (ListView) mView.findViewById(R.id.listView);
    }
}
