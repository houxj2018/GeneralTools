package com.houxj.generaltools.tools;

import android.media.MediaPlayer;
import android.net.Uri;

import com.houxj.generaltools.utils.JAppCache;
import com.houxj.generaltools.utils.JLogEx;

import java.io.IOException;

/**
 * Created by 侯晓戬 on 2018/7/24.
 * 音频播放工具类
 */

public class JAudioPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, JTimer.ITimeCallBack {
    private static JAudioPlayer mInstance;
    private IAudioListener mAudioCallBack = null;
    private int mAudioSeekto= 0;//开始播放的位置
    private MediaPlayer mMediaPlay = null;//播放器
    private JTimer mPlayTimer = null;   //播放进度回调定时器

    private JAudioPlayer(){
        mMediaPlay = new MediaPlayer();
        mMediaPlay.setOnCompletionListener(this);
        mMediaPlay.setOnErrorListener(this);
        mMediaPlay.setOnPreparedListener(this);

        mPlayTimer = new JTimer()
                .setInterval(1)
                .setTimeCallBack(this);
    };

    public static JAudioPlayer getInstance(){
        if(null == mInstance){
            mInstance = new JAudioPlayer();
        }
        return mInstance;
    }

    public static JAudioPlayer newInstance(){
        return new JAudioPlayer();
    }

    //TODO 设置播放监听
    public JAudioPlayer setListener(IAudioListener listener){
        mAudioCallBack = listener;
        return this;
    }

    //TODO 设置播放文件
    public JAudioPlayer setSource(String path){
        try {
            mMediaPlay.reset();
            mMediaPlay.setDataSource(path);
        } catch (IOException e) {
            JLogEx.w(e.getMessage());
            callBackError();
        }
        return this;
    }

    //TODO 设置播放位置
    public JAudioPlayer setSeekTo(int seekTo){
        mAudioSeekto = seekTo;
        mMediaPlay.seekTo(seekTo);
        return this;
    }

    //TODO 播放音频
    public JAudioPlayer play(){
        mMediaPlay.prepareAsync();
        return this;
    }

    //TODO 停止播放
    public void stop(){
        mMediaPlay.stop();
        callBackComplete();
    }

    //TODO 释放资源（播放完成会自动调用）
    public void release(){
        if(null != mMediaPlay){
            if(isPlaying()){
                stop();
            }
            mMediaPlay.release();
            mMediaPlay = null;
        }
    }

    //TODO 播放状态
    public boolean isPlaying(){
        return mMediaPlay.isPlaying();
    }

    //TODO 总时长
    public int getDuration(){
        return mMediaPlay.getDuration();
    }

    //TODO 当前播放位置（毫秒）
    public int getCurrentPosition(){
        return mMediaPlay.getCurrentPosition();
    }

    private void callBackError(){
        if(null != mAudioCallBack){
            mAudioCallBack.onError();
        }
        mPlayTimer.cancel();
    }

    private void callBackComplete(){
        if(null != mAudioCallBack){
            mAudioCallBack.onComplete();
        }
        mPlayTimer.cancel();
        release();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        callBackComplete();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        callBackError();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        mPlayTimer.start();//开始回调播放进度
    }

    @Override
    public void onTime(Object param) {
        if(null != mAudioCallBack){
            mAudioCallBack.onPosition(getCurrentPosition()/1000);//转成秒
        }
    }


    //播放状态监听
    public interface IAudioListener{
        public void onPosition(int position);
        public void onComplete();
        public void onError();
    }
}
