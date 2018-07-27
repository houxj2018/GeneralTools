package com.houxj.generaltools.tools;

import android.Manifest;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Build;

import com.houxj.generaltools.permit.JRunTimePermissions;
import com.houxj.generaltools.utils.JDateTimeUtils;
import com.houxj.generaltools.utils.JFileUtils;
import com.houxj.generaltools.utils.JLogEx;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by 侯晓戬 on 2018/7/24.
 * 录音工具类
 */

public class JAudioRecorder implements MediaRecorder.OnInfoListener,
        MediaRecorder.OnErrorListener {
    public static final String DIRECTORY_RECORD_CACHE = "recordx";
    public static final int ERROR_ID_NO_PERMISSION = -1;//无权限的错误
    public static final int ERROR_ID_START_FAIL = -2;//启动失败

    private MediaRecorder mRecorder = null;
    private File mSaveFile;
    private long mStartTime = 0;//开始录制的时间。用来计算录制时间
    private long mStopTime = 0;//停止的时间。用来计算录制时间
    private boolean mRecording = false;//录制状态
    private IAudioRecordListener mListener = null;

    private final Context mContext;

    public static JAudioRecorder newInstance(Context context){
        return new JAudioRecorder(context);
    }

    public JAudioRecorder(Context context){
        mContext = context;
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//录制麦克的数据
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setOnErrorListener(this);
        mRecorder.setOnInfoListener(this);
    };

    //TODO 设置输入文件路径
    public JAudioRecorder setOutputFile(String patch){
        setOutputFile(new File(patch));
        return this;
    }

    public JAudioRecorder setOutputFile(File outputFile){
        mSaveFile = outputFile;
        try {
            if(!mSaveFile.exists()){
                if(!mSaveFile.getParentFile().exists()){
                    mSaveFile.getParentFile().mkdirs();
                }
                mSaveFile.createNewFile();
            }
        }catch (IOException e){
            JLogEx.w(e.getMessage());
        }
        JLogEx.d("outputFile %s", outputFile.getAbsolutePath());
        return this;
    }

    //TODO 设置最大时间
    public JAudioRecorder setMaxDuration(int max_duration_sec){
        mRecorder.setMaxDuration(max_duration_sec * 1000);//转毫秒
        return this;
    }

    //TODO 设置监听回调
    public JAudioRecorder setListener(IAudioRecordListener listener){
        mListener = listener;
        return this;
    }

    //TODO 开始录制
    public JAudioRecorder start(){
        if(checkPermission()){
            if(!mRecording) {
                try {
                    if (null == mSaveFile) {
                        setOutputFile(createCacheAudioFile());
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        mRecorder.setOutputFile(mSaveFile);
                    }else{
                        mRecorder.setOutputFile(mSaveFile.getAbsolutePath());
                    }
//                    mRecorder.reset();
                    mRecorder.prepare();
                    mRecorder.start();
                    mStartTime = System.currentTimeMillis();
                    mStopTime = 0;
                    mRecording = true;
                } catch (IOException e) {
                    JLogEx.w(e.getMessage());
                    callBackError(ERROR_ID_START_FAIL);
                }
            }
        }else{//显示错误
            callBackError(ERROR_ID_NO_PERMISSION);
        }
        return this;
    }

    //TODO 开启异步录制（新开线程录制）
    public JAudioRecorder startAsync(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }).start();
        return this;
    }

    //TODO 停止录制
    public void stop(){
        mRecorder.stop();
        mRecorder.release();
        mStopTime = System.currentTimeMillis();
        mRecording = false;
        callBackComplete();
    }

    //TODO 获取录制时间长度(秒)
    public int getCurrentPosition(){
        if(mRecording) {
            return (int) ((System.currentTimeMillis() - mStartTime) / 1000);
        }else{
            if(mStopTime > mStartTime){
                return (int) ((mStopTime - mStartTime)/ 1000);
            }
        }
        return 0;
    }

    //TODO 创建临时录音文件
    private String createCacheAudioFile(){
        StringBuilder builder = new StringBuilder();
//        Environment.getExternalStorageDirectory().getAbsolutePath()
        builder.append(JFileUtils.getCachePath(mContext));
        builder.append(File.separator);
        builder.append(DIRECTORY_RECORD_CACHE);
        builder.append(File.separator);
        builder.append("rc");
        builder.append(JDateTimeUtils.getFormatString(new Date(),"yyyyMMdd_HHmmss"));
        builder.append(".aac");
        return builder.toString();
    }

    //TODO 检查权限
    private boolean checkPermission(){
        String[] permission = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO};
        return JRunTimePermissions.checkPermissionsInManifest(mContext, permission);
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
            //完成指定长度的录制
            callBackComplete();
        }
        JLogEx.d("what %d extra %d", what, extra);
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        JLogEx.d("what %d extra %d", what, extra);
        callBackError(what);
    }

    private void callBackError(int id){
        if(null != mListener){
            mListener.onError(id);
        }
    }

    private void callBackComplete(){
        if(null != mListener){
            mListener.onComplete();
        }
    }

    public interface IAudioRecordListener{
        public void onComplete();
        public void onError(int id);
    }
}
