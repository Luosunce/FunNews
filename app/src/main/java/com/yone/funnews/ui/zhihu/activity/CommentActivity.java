package com.yone.funnews.ui.zhihu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.yone.funnews.R;
import com.yone.funnews.base.SimpleActivity;
import com.yone.funnews.ui.zhihu.fragment.CommentFragment;
import com.yone.funnews.ui.zhihu.adapter.CommentMainAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Yoe on 2016/10/17.
 */

public class CommentActivity extends SimpleActivity {

    @BindView(R.id.toolbar_comment)
    Toolbar mToolbarComment;
    @BindView(R.id.tab_comment)
    TabLayout mTabComment;
    @BindView(R.id.vp_comment)
    ViewPager mVpComment;

    CommentMainAdapter mAdapter;
    List<CommentFragment> fragments = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_comment;
    }

    @Override
    protected void initEventAndData() {
        Intent intent = getIntent();
        int allNum = intent.getExtras().getInt("allNum");
        int shortNum= intent.getExtras().getInt("short");
        int longNum = intent.getExtras().getInt("long");
        int id = intent.getExtras().getInt("id");
        intent.getExtras().getInt("shortNum");
        setToolBar(mToolbarComment,String.format("%d条评论",allNum));
        getSupportActionBar().setElevation(0f);

        CommentFragment shortCommentFragment = new CommentFragment();
        Bundle shortBundle = new Bundle();
        shortBundle.putInt("id",id);
        shortBundle.putInt("kind",0);
        shortCommentFragment.setArguments(shortBundle);
        CommentFragment longCommentFragment = new CommentFragment();
        Bundle longBundle = new Bundle();
        longBundle.putInt("id",id);
        longBundle.putInt("kind",1);
        longCommentFragment.setArguments(longBundle);
        fragments.add(shortCommentFragment);
        fragments.add(longCommentFragment);
        mAdapter = new CommentMainAdapter(getSupportFragmentManager(),fragments);
        mVpComment.setAdapter(mAdapter);
        mTabComment.addTab(mTabComment.newTab().setText(String.format("短评论(%d)",shortNum)));
        mTabComment.addTab(mTabComment.newTab().setText(String.format("长评论(%d)",longNum)));
        mTabComment.setupWithViewPager(mVpComment);
        mTabComment.getTabAt(0).setText(String.format("短评论(%d)",shortNum));
        mTabComment.getTabAt(1).setText(String.format("长评论(%d)",longNum));
    }

}
