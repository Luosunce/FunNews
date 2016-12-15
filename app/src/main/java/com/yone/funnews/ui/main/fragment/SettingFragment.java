package com.yone.funnews.ui.main.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yone.funnews.R;
import com.yone.funnews.app.Constants;
import com.yone.funnews.base.SimpleFragment;
import com.yone.funnews.component.ACache;
import com.yone.funnews.component.RxBus;
import com.yone.funnews.model.been.NightModeEvent;
import com.yone.funnews.util.ShareUtil;
import com.yone.funnews.util.SharedPreferenceUtil;
import com.yone.funnews.util.SnackbarUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Yoe on 2016/10/11.
 */

public class SettingFragment extends SimpleFragment implements CompoundButton.OnCheckedChangeListener{

    @BindView(R.id.cb_setting_cache)
    AppCompatCheckBox mCbSettingCache;
    @BindView(R.id.cb_setting_image)
    AppCompatCheckBox mCbSettingImage;
    @BindView(R.id.cb_setting_night)
    AppCompatCheckBox mCbSettingNight;
    @BindView(R.id.ll_setting_feedback)
    LinearLayout mLlSettingFeedback;
    @BindView(R.id.tv_setting_clear)
    TextView mTvSettingClear;
    @BindView(R.id.ll_setting_clear)
    LinearLayout mLlSettingClear;
    @BindView(R.id.tv_setting_update)
    TextView mTvSettingUpdate;
    @BindView(R.id.ll_setting_update)
    LinearLayout mLlSettingUpdate;

    File cacheFile;
    boolean isNull = true;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initEventAndData() {
        cacheFile = new File(Constants.PATH_CACHE);
        mTvSettingClear.setText(ACache.getCacheSize(cacheFile));
        mCbSettingCache.setChecked(SharedPreferenceUtil.getAutoCacheState());
        mCbSettingImage.setChecked(SharedPreferenceUtil.getNoImageState());
        mCbSettingNight.setChecked(SharedPreferenceUtil.getNightModeState());
        mCbSettingCache.setOnCheckedChangeListener(this);
        mCbSettingImage.setOnCheckedChangeListener(this);
        mCbSettingNight.setOnCheckedChangeListener(this);

        try{
            PackageManager pm = getActivity().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getActivity().getPackageName(),PackageManager.GET_ACTIVITIES);
            String versionName = pi.versionName;
            mTvSettingUpdate.setText("当前版本号 V" + versionName);
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        isNull = savedInstanceState == null;
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.ll_setting_feedback, R.id.ll_setting_clear, R.id.ll_setting_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_setting_feedback:
                ShareUtil.sendEmail(mContext,"选择邮件客户端：");
                break;
            case R.id.ll_setting_clear:
                ACache.deleteDir(cacheFile);
                mTvSettingClear.setText(ACache.getCacheSize(cacheFile));
                break;
            case R.id.ll_setting_update:
                SnackbarUtil.showShort(mLlSettingUpdate,"已经是最新版本");
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.cb_setting_night:
                if (isNull){   //防止夜间模式MainActivity执行reCreate后重复调用
                    SharedPreferenceUtil.setNightModeState(b);
                    NightModeEvent event = new NightModeEvent();
                    event.setNightMode(b);
                    RxBus.getDefault().post(event);
                }
                break;
            case R.id.cb_setting_image:
                SharedPreferenceUtil.setNoImageState(b);
                break;
            case R.id.cb_setting_cache:
                SharedPreferenceUtil.setAutoCacheState(b);
                break;
        }
    }
}
