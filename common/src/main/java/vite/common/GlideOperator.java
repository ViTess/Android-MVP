package vite.common;

//import com.bumptech.glide.RequestBuilder;
//import com.bumptech.glide.annotation.GlideExtension;
//import com.bumptech.glide.annotation.GlideOption;
//import com.bumptech.glide.annotation.GlideType;
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
//import com.bumptech.glide.load.resource.gif.GifDrawable;
//import com.bumptech.glide.request.RequestOptions;
//
//import static com.bumptech.glide.request.RequestOptions.decodeTypeOf;
//
///**
// * Glide的额外操作，像kotlin一样可以增加Extension，在这里定义好的操作可以直接在Glide里调用
// * 目前的4.0.0-RC版本有问题，该功能等修复后再使用
// * Created by trs on 17-6-9.
// */
//@GlideExtension
//public class GlideOperator {
//    private GlideOperator(){}
//
//    private static final RequestOptions DECODE_TYPE_GIF = decodeTypeOf(GifDrawable.class).lock();
//
//    /**
//     * e.g
//     *
//     * GlideApp.with(fragment)
//     * .load(url)
//     * .miniThumb(thumbnailSize)
//     * .into(imageView);
//     *
//     * @param options
//     * @param size
//     */
//    @GlideOption
//    public static void miniThumb(RequestOptions options, int size) {
//        options.fitCenter()
//                .override(size);
//    }
//
//    /**
//     * e.g
//     *
//     * GlideApp.with(fragment)
//     * .asGif()
//     * .load(url)
//     * .into(imageView);
//     *
//     * @param requestBuilder
//     */
//    @GlideType(GifDrawable.class)
//    public static void asGif(RequestBuilder<GifDrawable> requestBuilder) {
//        requestBuilder.transition(new DrawableTransitionOptions())
//                .apply(DECODE_TYPE_GIF);
//    }
//}
