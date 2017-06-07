package vite.common;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

/**
 * glide配置
 * Created by trs on 17-5-27.
 */

public class GlideConfig implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
//        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
//        builder.setBitmapPool(new LruBitmapPool(200 * 1024 * 1024));
//        builder.setMemoryCache(new LruResourceCache(100 * 1024 * 1024));

        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
        int diskCacheBytes = 104857600;  //100M

//        String downloadDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String downloadDirectoryPath = context.getExternalCacheDir().getAbsolutePath();
        builder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath, diskCacheBytes));
        LogUtil.v("GlideConfig", "applyOptions");
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.setMemoryCategory(MemoryCategory.LOW);
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
        LogUtil.v("GlideConfig", "registerComponents");
    }
}
