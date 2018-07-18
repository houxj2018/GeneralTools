package com.houxj.generaltools.debug;

import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.houxj.generaltools.R;
import com.houxj.generaltools.permit.IPermissionCallBack;
import com.houxj.generaltools.permit.JRunTimePermissions;
import com.houxj.generaltools.utils.JFileUtils;
import com.houxj.generaltools.utils.JPinYinUtils;
import com.houxj.generaltools.utils.JAppCache;
import com.houxj.generaltools.utils.JImageLoader;
import com.houxj.generaltools.utils.JLogEx;
import com.houxj.generaltools.utils.JMd5Tools;
import com.houxj.generaltools.utils.JSharedP;
import com.houxj.generaltools.utils.JDateTimeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IPermissionCallBack {

    private ImageView mImageView;
    private TextView mtvInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JLogEx.setDebugEnable(true);
        JImageLoader.initLoader(this);
        setContentView(R.layout.activity_main);
        JRunTimePermissions.with(this)
                .check(this);
        initView();
    }

    private void initView(){
        mImageView = (ImageView)findViewById(R.id.image_test);
        mtvInfo = (TextView) findViewById(R.id.text_view_info);
        mtvInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void updateTextInfo(String msg){
        if(null == msg){
            msg = "null";
        }
        mtvInfo.append("\r\n");
        mtvInfo.append(msg);
        JLogEx.d(msg);
        int offset=mtvInfo.getLineCount()*mtvInfo.getLineHeight();
        if(offset>(mtvInfo.getHeight()-mtvInfo.getLineHeight()-20)){
            mtvInfo.scrollTo(0,offset-mtvInfo.getHeight()+mtvInfo.getLineHeight()+20);
        }
    }

    public void onClick(View view){
        int id = view.getId();
        if(id == R.id.but_local_pic){
            JImageLoader.createLoader()
                    .setCircleOptions()
                    .displayImage(R.mipmap.mypic,mImageView);
        }else if(id == R.id.but_net_pic){
            loadNetPic();
        }else if(id == R.id.but_cache){
            listAppDir();
            getCacheSize();
            JLogEx.d("清理");
            JAppCache.cleanAppCacheData(this);
            JImageLoader.cleanCache(this);
            getCacheSize();
        }else if(id == R.id.but_perm){
            readUsePerm();
        }else if(id == R.id.but_md5){
            testMd5();
        }else if(id == R.id.but_sp_list){
            testSpList();
        }else if(id == R.id.but_test_date){
            testDate();
        }else if(R.id.but_pinyin == id){
            testPinYin();
        }else if(R.id.but_file == id){
            listSystemUri();
            testFileUtils();
        }
    }

    private void listSystemUri(){
        Uri content = MediaStore.Files.getContentUri("external");
        updateTextInfo(MediaStore.Audio.Media.INTERNAL_CONTENT_URI.toString());
        updateTextInfo(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString());
        updateTextInfo(content.toString());
        updateTextInfo(content.getAuthority());
        updateTextInfo(content.getHost());
        updateTextInfo(content.getPath());

        Uri uri = JFileUtils.getFileUri(this, "");
        updateTextInfo(uri.getPath());
        updateTextInfo(uri.getHost());
        updateTextInfo(uri.getAuthority());
        updateTextInfo(uri.getEncodedAuthority());
        updateTextInfo(uri.getEncodedFragment());
        updateTextInfo(uri.getEncodedPath());
        updateTextInfo(uri.getEncodedQuery());
        updateTextInfo(uri.getEncodedSchemeSpecificPart());
        updateTextInfo(uri.getEncodedUserInfo());
        updateTextInfo(uri.getFragment());
        updateTextInfo(uri.getLastPathSegment());
        updateTextInfo(uri.getQuery());
        updateTextInfo(uri.getScheme());
    }

    private void testFileInfo(){
        //external MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        Uri content = MediaStore.Files.getContentUri("external");//外部所有文件
        Cursor mycr = JFileUtils.queryFileFromProvider(getContentResolver(),content,"");
        if(null != mycr && mycr.moveToFirst()){
            int count = mycr.getCount();
            updateTextInfo("count = " + count);
            int len = count>=100?100:count;
            if(count > 17000){
                mycr.move(17000);
            }
            int _id_ind = mycr.getColumnIndex(MediaStore.MediaColumns._ID);
            int _data_ind = mycr.getColumnIndex(MediaStore.MediaColumns.DATA);
            for (int i= 0; i< len; i++){
                updateTextInfo("id = "  + mycr.getInt(_id_ind) + " data=" +mycr.getString(_data_ind));
                if(!mycr.moveToNext()){
                    break;
                }
            }
            mycr.close();
        }
    }

    private void testFileUtils(){
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath();
        //大壮-我们不一样.mp3
        file_path += File.separator + Environment.DIRECTORY_MUSIC + File.separator + "费玉清-夏之旅.wav";

        updateTextInfo(Uri.decode(file_path));
        updateTextInfo(file_path);
        updateTextInfo(Uri.fromFile(new File(file_path)).toString());
        Uri uri = JFileUtils.getFileUri(this, file_path);
        updateTextInfo(uri.toString());

        updateTextInfo(JFileUtils.getRealFilePath(this, uri));
        updateTextInfo(JFileUtils.getRealFilePath(this, Uri.EMPTY));
        updateTextInfo(JFileUtils.getRealFilePath(this, Uri.fromFile(new File(file_path))));
    }


    private void testPinYin(){
        updateTextInfo(JPinYinUtils.getFirstPinYin("侯晓戬"));
        updateTextInfo(JPinYinUtils.getFullPinYin("侯晓戬"));
        List<String> mingdan = new ArrayList<>();
        mingdan.add("侯晓戬");
        mingdan.add("郝书华");
        mingdan.add("王晔垌");
        mingdan.add("刘劲松");
        mingdan.add("潘锦安");
        mingdan.add("谢永许");
        mingdan.add("侯保国");
        updateTextInfo(JLogEx.objString(mingdan.toArray()));
        JPinYinUtils.sortByFirstPinYin(mingdan);
        updateTextInfo(JLogEx.objString(mingdan.toArray()));
    }

    private void testDate(){
        updateTextInfo(JDateTimeUtils.getDay());
        updateTextInfo(JDateTimeUtils.getDateTime());
        updateTextInfo(JDateTimeUtils.getTime());
        updateTextInfo(JDateTimeUtils.getDateTime(JDateTimeUtils.getWeekMondayForDate(new Date())));
        updateTextInfo(JDateTimeUtils.getDateTime(JDateTimeUtils.getWeekSundayForDate(new Date())));
        Date mydate = JDateTimeUtils.getDateForString("2018-11-12 10:12:21");
        updateTextInfo(JDateTimeUtils.getDateTime(mydate));
        mydate = JDateTimeUtils.getGMTString("/Date(1541988741000)/");
        updateTextInfo(JDateTimeUtils.getDateTime(mydate));
        mydate = JDateTimeUtils.getGMTString("1541988741000");
        updateTextInfo(JDateTimeUtils.getDateTime(mydate));
        mydate = JDateTimeUtils.getGMTString("1541988741");
        updateTextInfo(JDateTimeUtils.getDateTime(mydate));

    }

    private void testSpList(){
        List<String> lst = new ArrayList<>();
        lst.add("今天的天气不错");
        lst.add("hello world");
        lst.add("明天不会下雨的");
        updateTextInfo(JLogEx.objString(lst.toArray()));
        JSharedP.setListValue(getApplicationContext(),"key", lst);
        List<String> values = JSharedP.getListValue(getApplicationContext(),"key",String.class);
        updateTextInfo(JLogEx.objString(values.toArray()));

        Stu mystu = new Stu();
        mystu.id = 1;
        mystu.name ="张三";
        mystu.age = 12;
        mystu.grade =98.5f;

        Stu mystu2 = new Stu();
        mystu2.id = 2;
        mystu2.name ="李国友";
        mystu2.age = 14;
        mystu2.grade =88.2f;

        List<Stu> stu_lst = new ArrayList<>();
        stu_lst.add(mystu);
        stu_lst.add(mystu2);
        updateTextInfo(JLogEx.objString(stu_lst.toArray()));
        JSharedP.setListValue(getApplicationContext(),"STU", stu_lst);
        List<Stu> val_stu = JSharedP.getListValue(getApplicationContext(),"STU1", Stu.class);
        if(null != val_stu) {
            updateTextInfo(JLogEx.objString(val_stu.toArray()));
        }

        JSharedP.setValue(getApplicationContext(),"xxx", mystu2);
        updateTextInfo(JLogEx.objString(mystu2));
        Stu mystu3 = JSharedP.getValue(getApplicationContext(),"xxx1", Stu.class);
        if(null != mystu3) {
            updateTextInfo(JLogEx.objString(mystu3));
        }
    }

    class Stu{
        int id;
        String name;
        int age;
        float grade;
        private int code;
    }

    private void testMd5(){
        String src = "hou13510901281inszxxx";
        updateTextInfo(JMd5Tools.getMd5(src));
        JLogEx.d("测试一下");

        Stu mystu = new Stu();
        mystu.id = 1;
        mystu.name ="张三";
        mystu.age = 12;
        mystu.grade =98.5f;
        updateTextInfo(JLogEx.objString(mystu));
        JLogEx.d(1);
        int xx = 2;
        JLogEx.d(xx);
        JLogEx.d(false);
        JLogEx.d(10.2f);
        JLogEx.d(10.222);
        JLogEx.d('c');
        JLogEx.d((byte)0x02);
    }

    private void readUsePerm(){
        JRunTimePermissions.goSystemPermissionSetting(this);
    }

    private void loadNetPic(){
        String url = "https://jiuye-res.jikexueyuan.com/zhiye/" +
                "showcase/attach-/20161208/0409c25c-77a2-498f-b583-066bd97a8ff7.jpg";
        JImageLoader.createLoader()
//                    .setRoundOptions(10)
                .setFedeInOptions(800)
                .displayImage(url, mImageView);
    }

    private void listAppDir(){
        updateTextInfo("APP:");
        updateTextInfo(getFilesDir().getParentFile().getAbsolutePath());
        File[] files = getFilesDir().getParentFile().listFiles();
        for (File file : files){
            updateTextInfo(file.getPath());
        }
        updateTextInfo("外部:");
        updateTextInfo(getExternalCacheDir().getAbsolutePath());
        files = getExternalCacheDir().getParentFile().listFiles();
        for (File file : files){
            updateTextInfo(file.getPath());
        }
        updateTextInfo(Environment.getDataDirectory().getAbsolutePath());
        updateTextInfo(Environment.getExternalStorageDirectory().getAbsolutePath());

        updateTextInfo(JFileUtils.getTempPath(this));
    }

    //获取缓存大小
    private void getCacheSize(){
        updateTextInfo(JAppCache.fomatFileSize(JAppCache.getAppCacheSize(this)));
    }

    @Override
    public void onResult(int result) {
        JLogEx.d("%d", result);
    }
}
