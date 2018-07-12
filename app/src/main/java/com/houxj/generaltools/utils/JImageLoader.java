package com.houxj.generaltools.utils;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedVignetteBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by 侯晓戬 on 2018/6/28.
 * 图像加载工具类，
 * 使用的是 nostra13 的 ImageLoader 图像加载架构
 */

public class JImageLoader {
    private static JImageLoader mInstance;
    private DisplayImageOptions.Builder mOptionBuidler = null;
    private ImageLoadingListener mLoadListener = null;

    private JImageLoader(){
        mOptionBuidler = createDisplayOptions();
    }
    public static JImageLoader getInstance(){
        if(null == mInstance){
            mInstance = new JImageLoader();
        }
        return mInstance;
    }
    //TODO 创建一个加载者
    public static JImageLoader createLoader(){
        return new JImageLoader();
    }
    //TODO 初始化
    public static void initLoader(Context context){
        File cacheDir= StorageUtils.getCacheDirectory(context);
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .tasksProcessingOrder(QueueProcessingType.FIFO) //设置图片下载和显示的工作队列排序
                .memoryCacheExtraOptions(480, 800)
                // CompressFormat.PNG类型，70质量（0-100）
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(2 * 1024 * 1024) // 缓存到内存的最大数据
                // 缓存在文件的图片的宽和高度
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheExtraOptions(960, 1280, null)
                .diskCacheSize(50 * 1024 * 1024) // 缓存到文件的最大数据
                .diskCacheFileCount(1000) // 文件数量
                .defaultDisplayImageOptions(displayImageOptions). // 上面的options对象，一些属性配置
                build();
        ImageLoader.getInstance().init(config); // 初始化
    }
    public static void cleanCache(Context context){
        File cacheDir= StorageUtils.getCacheDirectory(context);
        JAppCache.cleanCustomCache(cacheDir);
    }
    //TODO 将图像url显示到图像视图中
    public <T extends ImageView> void displayImage(String url, T view){
        DisplayImageOptions options = mOptionBuidler.build();
        ImageLoader.getInstance().displayImage(url,view,options,mLoadListener);
    }
    //TODO 将一个本地图片能使用特殊效果显示在视图上
    public <T extends ImageView> void displayImage(int ResId, T view){
        String url = "drawable://" + ResId;
        DisplayImageOptions options = mOptionBuidler.build();
        ImageLoader.getInstance().displayImage(url,view,options);
    }
    //TODO 设置加载回调
    public JImageLoader setListener(ImageLoadingListener listener){
        mLoadListener = listener;
        return this;
    }
    //TODO 设置参数 空地址时 加载失败时 加载中显示图片
    public JImageLoader setDisplayOptions(int resEmpty, int resFail, int resLoading){
        mOptionBuidler.showImageForEmptyUri(resEmpty)
                .showImageOnFail(resFail)
                .showImageOnLoading(resLoading);
        return this;
    }
    //TODO 建立圆形显示效果配置
    public JImageLoader setCircleOptions(){
        mOptionBuidler.displayer(new CircleBitmapDisplayer());
        return this;
    }
    //TODO 建立圆角显示效果配置
    // radius 圆角半径
    public JImageLoader setRoundOptions(int radius){
        mOptionBuidler.displayer(new RoundedBitmapDisplayer(radius));
        return this;
    }
    //TODO 建立圆角小插图显示效果配置
    // radius 圆角半径 marginPixels 偏移
    public JImageLoader setRoundVignetteOptions(int radius,int marginPixels){
        mOptionBuidler.displayer(new RoundedVignetteBitmapDisplayer(radius, marginPixels));
        return this;
    }
    //TODO 建立淡入显示效果配置
    public JImageLoader setFedeInOptions(int durationMillis){
        mOptionBuidler.displayer(new FadeInBitmapDisplayer(durationMillis));
        return this;
    }

    ///////////////////////////////////////////
    //
    private DisplayImageOptions.Builder createDisplayOptions(){
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true); // 设置下载的图片是否缓存在SD卡中
    }
}
