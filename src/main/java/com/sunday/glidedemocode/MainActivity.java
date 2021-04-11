package com.sunday.glidedemocode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;

public class MainActivity extends AppCompatActivity {

    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIv = findViewById(R.id.iv);
    }

    public void onLoadImageClick (View v) {
//        使用 HttpURLConnection 来加载网络中的图片
//        loadUrlImage("https://www.imooc.com/static/img/index/logo.png");
//        但是当网络图片过大时，会导致我们生成的bitmap所占用的内存过多，从而导致 OOM
//        loadUrlImage("http://res.lgdsunday.club/big_img.jpg");
//        同样，当我们在加载本地资源文件中的图片时，如果图片过大，一样会导致 OOM
//        loadResImage(R.mipmap.big_img);


//        使用 Glide 来去图片的时候，Glide则会帮助我们处理上面的问题
//        glideLoadImage("http://res.lgdsunday.club/big_img.jpg");
        glideLoadImage("https://img2.mukewang.com/5b037fb30001534202000199-140-140.jpg");
//        glideAppLoadImage("https://img2.mukewang.com/5b037fb30001534202000199-140-140.jpg");
    }

    /**
     * 加载网络图片
     * @param img 网络图片地址
     */
    private void loadUrlImage (final String img) {
//        开启子线程，用于进行网络请求
        new Thread(){
            @Override
            public void run() {
//                创建消息对象，用于通知handler
                Message message = new Message();
                try {
//                    根据传入的路径生成对应的URL
                    URL url = new URL(img);
//                    创建连接
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                    设置请求方式为GET
                    httpURLConnection.setRequestMethod("GET");
//                    获取返回码
                    int code = httpURLConnection.getResponseCode();
//                    当返回码为200时，表示请求成功
                    if (code == 200) {
//                        获取数据流
                        InputStream inputStream = httpURLConnection.getInputStream();
//                        利用位图工程根据数据流生成对应的位图对象
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        利用message对象将生成的bitmap携带到handler
                        message.obj = bitmap;
//                        成功的状态码
                        message.what = 200;
                    } else {
//                        失败的状态码
                        message.what = code;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    当出现异常的时候，状态码设置为 -1
                    message.what = -1;
                } finally {
//                    通知handler
                    handler.sendMessage(message);
                }
            }
        }.start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                当获取到成功的状态码时执行
                case 200:
//                    获取携带的bitmap
                    Bitmap bitmap = (Bitmap) msg.obj;
//                    imageView展示
                    mIv.setImageBitmap(bitmap);
                    break;
//                 当请求失败获取出现异常的时候回调
                default:
//                    展示加载失败的图片
                    mIv.setImageResource(R.mipmap.loader_error);
//                    打印失败的状态码
                    Toast.makeText(MainActivity.this, "code: " + msg.what , Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * 加载本地图片
     * @param resId 本地图片ID
     */
    private void loadResImage (int resId) {
        mIv.setImageResource(resId);
    }

    /**
     * 使用Glide加载网络图片
     * @param img 网络图片地址
     */
    private void glideLoadImage (String img) {
//      通过 RequestOptions 对象来设置Glide的配置
        RequestOptions options = new RequestOptions()
//                设置图片变换为圆角
                .circleCrop()
//                设置站位图
                .placeholder(R.mipmap.loading)
//                设置加载失败的错误图片
                .error(R.mipmap.loader_error);

//      Glide.with 会创建一个图片的实例，接收 Context、Activity、Fragment
        Glide.with(this)
//                指定需要加载的图片资源，接收 Drawable对象、网络图片地址、本地图片文件、资源文件、二进制流、Uri对象等等
                .load(img)
//                指定配置
                .apply(options)
//                用于展示图片的ImageView
                .into(mIv);
    }

    /**
     * 使用GlideApp进行图片加载
     * @param img
     */
    private void glideAppLoadImage (String img) {
        /**
         * 不想每次都通过 .apply(options) 的方式来进行配置的时候，可以使用GlideApp的方式来进行全局统一的配置
         * 需要注意以下规则：
         * 1、引入 repositories {mavenCentral()}  和 dependencies {annotationProcessor 'com.github.bumptech.glide:compiler:4.8.0'}
         * 2、集成 AppGlideModule 的类并且通过 @GlideModule 进行了注解
         * 3、有一个使用了 @GlideExtension 注解的类 MyGlideExtension，并实现private的构造函数
         * 4、在 MyGlideExtension 可以通过被 @GlideOption 注解了的静态方法来添加可以被GlideApp直接调用的方法，该方法默认接受第一个参数为：RequestOptions
         */
       GlideApp.with(this)
               .load(img)
//               调用在MyGlideExtension中实现的，被@GlideOption注解的方法，不需要传递 RequestOptions 对象
               .injectOptions()
               .into(mIv);
    }
}
