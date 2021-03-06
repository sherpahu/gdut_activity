package com.rdc.gdut_activity.model;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdc.gdut_activity.R;
import com.rdc.gdut_activity.fragment.MineFragment;
import com.rdc.gdut_activity.fragment.PublishFragment;
import com.rdc.gdut_activity.fragment.ReleasedActListFragment;

public class PublisherFragmentModel {

    private static int[] mTabRes = new int[]{R.drawable.selector_released_activity, R.drawable.selector_publish_activity, R.drawable.selector_publisher};
    private static String[] mTabTitle = new String[]{"已发布", "发布", "我的"};

    public static Fragment[] getFragment(String from) {
        Fragment[] fragments = new Fragment[3];
        fragments[0] = ReleasedActListFragment.newInstance();
        fragments[1] = PublishFragment.newInstance();
        fragments[2] = MineFragment.newInstance();
        return fragments;
    }

    public static View getTabItemView(Context context, int position) {
        // 防止越界
        if (position > mTabRes.length) {
            return null;
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_publisher_tab, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_user_icon);
        TextView textView = (TextView) view.findViewById(R.id.tv_item_name);

        imageView.setImageResource(mTabRes[position]);
        textView.setText(mTabTitle[position]);
        return view;
    }

}
