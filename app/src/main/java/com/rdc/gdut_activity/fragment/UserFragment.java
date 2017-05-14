package com.rdc.gdut_activity.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rdc.gdut_activity.R;
import com.rdc.gdut_activity.base.BaseFragment;
import com.rdc.gdut_activity.bean.Student;
import com.rdc.gdut_activity.ui.AboutActivity;
import com.rdc.gdut_activity.ui.UserActivitiesActivity;
import com.rdc.gdut_activity.ui.UserDetailActivity;
import com.rdc.gdut_activity.ui.UserGDUTActivity;
import com.rdc.gdut_activity.view.CircleImageView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserFragment extends BaseFragment {

    @InjectView(R.id.civ_user_icon)
    CircleImageView civUserIcon;
    @InjectView(R.id.tv_user_name)
    TextView tvUserName;
    @InjectView(R.id.iv_user_sex)
    ImageView ivUserSex;
    @InjectView(R.id.tv_user_age)
    TextView tvUserAge;
    @InjectView(R.id.tv_user_area)
    TextView tvUserArea;
    @InjectView(R.id.ll_user_details)
    LinearLayout llUserDetails;
    @InjectView(R.id.ll_user_activities)
    LinearLayout llUserActivities;
    @InjectView(R.id.ll_user_gdut)
    LinearLayout llUserGDUT;
    @InjectView(R.id.ll_user_about)
    LinearLayout llUserAbout;
    @InjectView(R.id.ll_user_logout)
    LinearLayout llUserLogout;
    private int mTitle;
    private String mMessage;
    private String mPath = "/sdcard/myHeadIcon/";
    private Boolean mIsMale;
    private String mPassword;
    private Student mStudent;

    /**
     * 用来获取fragment实例的方法，这里可以让Activity给fragment设置参数,参数可以在下面的initData方法中的bundle中取出
     */
    public static UserFragment newInstance(int title, String message) {
        UserFragment userFragment = new UserFragment();
        Bundle bundle = new Bundle(2);
        bundle.putInt("title", title);
        bundle.putString("message", message);
        userFragment.setArguments(bundle);
        return userFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        Log.e("error", "onResume");
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void initData(Bundle bundle) {
        setParams(bundle);
    }


    @Override
    protected void initView() {
        Bitmap bitmap = BitmapFactory.decodeFile(mPath + "head.jpg");
        if (bitmap != null) {
            Drawable drawable = new BitmapDrawable(bitmap);
            civUserIcon.setImageDrawable(drawable);
        } else {
            // TODO: 2017/5/12 从云端下载
        }
        BmobQuery<Student> studentBmobQuery = new BmobQuery<>();
        studentBmobQuery.addWhereEqualTo("objectId", BmobUser.getCurrentUser().getObjectId());
        studentBmobQuery.findObjects(new FindListener<Student>() {
            @Override
            public void done(List<Student> list, BmobException e) {
                if (e == null) {
                    mStudent = list.get(0);
                    if (mStudent != null) {
                        if (mStudent.isSex() == false) {
                            ivUserSex.setImageDrawable(getResources().getDrawable(R.drawable.ic_female));
                            mIsMale = false;
                        } else {
                            ivUserSex.setImageDrawable(getResources().getDrawable(R.drawable.ic_male));
                            mIsMale = true;
                        }
                        tvUserName.setText(mStudent.getUsername());
                        if (mStudent.getArea() == null || mStudent.getArea().equals("")) {
                            tvUserArea.setText("广东,广州,番禺");
                        } else {
                            tvUserArea.setText(mStudent.getArea());
                        }
                        if(mStudent.getAge() == null) {
                            tvUserAge.setText("20岁");
                        } else {
                            tvUserAge.setText(mStudent.getAge());
                        }
                        mPassword = mStudent.getGdutPassword();
                    } else {
                        Log.e("error", "mStudent is null");
                    }
                    Toast.makeText(getContext(), "find success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "find failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void setListener() {

    }

    private void setParams(Bundle bundle) {
        mTitle = bundle.getInt("title");
        mMessage = bundle.getString("message");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.ll_user_details, R.id.ll_user_activities, R.id.ll_user_gdut, R.id.ll_user_about, R.id.ll_user_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_user_details:
                startUserDetailActivity();
                break;
            case R.id.ll_user_activities:
                startUserActivitiesActivity();
                break;
            case R.id.ll_user_gdut:
                startUserGDUTActivity();
                break;
            case R.id.ll_user_about:
                startAboutActivity();
                break;
            case R.id.ll_user_logout:
                showLogoutDialog();
                break;
        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("退出登录");
        builder.setIcon(R.drawable.ic_logout);
        builder.setMessage("您确定要退出登录吗？退出登录将无法正常接收消息通知！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: 2017/5/13 退出登录
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void startAboutActivity() {
        Intent aboutIntent = new Intent(getContext(), AboutActivity.class);
        startActivity(aboutIntent);
    }

    private void startUserGDUTActivity() {
        Intent userGDUTIntent = new Intent(getContext(), UserGDUTActivity.class);
        startActivity(userGDUTIntent);
    }

    private void startUserActivitiesActivity() {
        Intent userActivitiesIntent = new Intent(getContext(), UserActivitiesActivity.class);
        startActivity(userActivitiesIntent);
    }

    private void startUserDetailActivity() {
        Log.e("error", "startUserDetailActivity");
        Intent userDetailIntent = new Intent(getContext(), UserDetailActivity.class);
        userDetailIntent.putExtra("username", tvUserName.getText().toString());
        userDetailIntent.putExtra("sex", mIsMale);
        userDetailIntent.putExtra("age", tvUserAge.getText().toString());
        userDetailIntent.putExtra("area", tvUserArea.getText().toString());
        userDetailIntent.putExtra("password", mPassword);
        startActivity(userDetailIntent);
    }
}
