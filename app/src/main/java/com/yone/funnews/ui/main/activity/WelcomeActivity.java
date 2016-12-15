package com.yone.funnews.ui.main.activity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yone.funnews.R;
import com.yone.funnews.base.BaseActivity;
import com.yone.funnews.component.ImageLoader;
import com.yone.funnews.model.been.WelcomeBean;
import com.yone.funnews.presenter.WelcomePresenter;
import com.yone.funnews.presenter.contract.WelcomeContract;

import butterknife.BindView;

/**
 * Created by Yoe on 2016/10/10.
 */

public class WelcomeActivity extends BaseActivity<WelcomePresenter> implements WelcomeContract.View {


    @BindView(R.id.iv_welcome_bg)
    ImageView mIvWelcomeBg;
    @BindView(R.id.tv_welcome_author)
    TextView mTvWelcomeAuthor;

    private String WelcomeBg;
    @Override
    protected void initInject() {
       getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getWelcomeData();
    }

    @Override
    public void showContent(WelcomeBean welcomeBean) {
        ImageLoader.load(this,welcomeBean.getImg(),mIvWelcomeBg);
        WelcomeBg = welcomeBean.getImg();
        mIvWelcomeBg.animate().scaleX(1.12f).scaleY(1.12f).setDuration(2000).setStartDelay(100).start();
        mTvWelcomeAuthor.setText(welcomeBean.getText());
    }

    @Override
    public void jumpToMain() {
        Intent intent = new Intent();
        intent.putExtra("HeaderImg",WelcomeBg);
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    protected void onDestroy() {
        Glide.clear(mIvWelcomeBg);
        super.onDestroy();
    }
}
