package com.mplayer.exo_player;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kingz.customdemo.R;
import com.kingz.play.MediaParams;
import com.kingz.play.PlayerActivity;
import com.kingz.play.VideoInfo;
import com.kingz.play.fragment.PlayFragment;
import com.kingz.play.fragment.VodDetailFragment;
import com.kingz.play.fragment.VodInfoFragment;
import com.kingz.play.presenter.VodInfoPresenter;

/**
 * author：KingZ
 * date：2019/4/23
 * description：手机版本的详情页
 * 基于exo播放器组件
 */
public class DetailPageActivty extends PlayerActivity {
    public static final String TAG = "DetailPageActivty";

    private PlayFragment playFragment;
    private VodInfoFragment vodInfoFragment;
    private VodInfoPresenter vodInfoPresenter;
    //影片详情介绍的Fragment
    private VodDetailFragment vodDetailFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            initFragment();
        }
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // 播放区域
        playFragment = (PlayFragment) fm.findFragmentByTag(TAG_VOD_PLAY);
        if (playFragment == null) {
            playFragment = PlayFragment.newInstance(new MediaParams());
            fragmentTransaction.add(R.id.player_content, playFragment, TAG_VOD_PLAY);
        }

        // 影片信息区域
        vodInfoFragment = (VodInfoFragment) fm.findFragmentByTag(TAG_VOD_INFO);
        if (vodInfoFragment == null) {
            vodInfoFragment = new VodInfoFragment();
            fragmentTransaction.add(R.id.content_layout, vodInfoFragment, TAG_VOD_INFO);
        } else {
            vodInfoPresenter = new VodInfoPresenter(vodInfoFragment);
        }
        fragmentTransaction.show(vodInfoFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        playFragment.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //目前是横屏的话
            playFragment.onBackPressed();
        } else if (vodDetailFragment != null && vodDetailFragment.isAdded() && vodDetailFragment.isVisible()) {
            // 隐藏影片详情简介fragment
            getSupportFragmentManager().beginTransaction().remove(vodDetailFragment).commit();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 展示&收起 详情页
     * @param isShow    是否显示
     */
    public void showOrDismissVideoDetail(boolean isShow,VideoInfo videoInfo) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        vodDetailFragment = (VodDetailFragment) fm.findFragmentByTag(TAG_VOD_DETAIL);
        if (vodDetailFragment == null) {
            vodDetailFragment = VodDetailFragment.newInstance(videoInfo);
        }
        if (isShow) {
            if (!vodDetailFragment.isAdded()) {
                fragmentTransaction.add(R.id.content_layout, vodDetailFragment, TAG_VOD_DETAIL);
            }
            fragmentTransaction.show(vodDetailFragment);
        } else {
            fragmentTransaction.remove(vodDetailFragment);
        }
        fragmentTransaction.commit();
    }


}
