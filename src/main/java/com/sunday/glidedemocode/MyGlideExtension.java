package com.sunday.glidedemocode;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.request.RequestOptions;

@GlideExtension
public class MyGlideExtension{

    /**
     * 实现private的构造函数
     */
    private MyGlideExtension() {
    }

    @GlideOption
    public static void injectOptions (RequestOptions options) {
        options
//                设置图片变换为圆角
                .circleCrop()
//                设置站位图
                .placeholder(R.mipmap.loading)
//                设置加载失败的错误图片
                .error(R.mipmap.loader_error);

    }
}
