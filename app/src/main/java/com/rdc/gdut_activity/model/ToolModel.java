package com.rdc.gdut_activity.model;

import com.rdc.gdut_activity.bean.LoginBackBean;
import com.rdc.gdut_activity.constant.Constant;
import com.rdc.gdut_activity.contract.ToolContract;
import com.rdc.gdut_activity.utils.OkHttpResultCallback;
import com.rdc.gdut_activity.utils.OkHttpUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by PC on 2017/5/17.
 */

public class ToolModel {
    private static final String TAG = "ToolModel";
    private Map<String,String> mBodyMap;
    private ToolContract.Presenter mPresenter;

    public ToolModel(ToolContract.Presenter presenter){
        mBodyMap = new HashMap<>();
        mPresenter = presenter;
    }


    public void loginSystem(String studengtId,String pwd){
        mBodyMap.clear();
        mBodyMap.put(Constant.LOGIN_BODY_NAME_ACCOUNT,studengtId);
        mBodyMap.put(Constant.LOGIN_BODY_NAME_PWD,pwd);
        mBodyMap.put(Constant.LOGIN_BODY_NAME_VERIFYCODE,Constant.LOGIN_BODY_VALUE_VERIFYCODE);
        OkHttpUtil.getInstance().postAsync(Constant.EDUCATION_SYSTEM_LOGIN_URL, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mPresenter.getError(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes) {
                String s = new String(bytes);
                LoginBackBean bean = LoginBackBean.objectFromData(s);
                if (bean.getStatus().equals("y")){
                    mPresenter.loginSystemSuccess();
                }else if (bean.getStatus().equals("n")){
                    mPresenter.loginSystemFailure(bean.getMsg());
                }
            }
        },mBodyMap,null);
    }



}
