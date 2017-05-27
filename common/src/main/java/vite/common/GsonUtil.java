package vite.common;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * Created by trs on 16-11-17.
 */

public class GsonUtil {
    //@Expose(serialize = false, deserialize = false)//注解：gson不序列化和反序列化
    private static final GsonBuilder sGsonBuilder = new GsonBuilder()
            .addSerializationExclusionStrategy(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    final Expose expose = f.getAnnotation(Expose.class);
                    return expose != null && !expose.serialize();
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .addDeserializationExclusionStrategy(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    final Expose expose = f.getAnnotation(Expose.class);
                    return expose != null && !expose.deserialize();
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            });

    private static volatile Gson sGson = null;

    private static Gson getInstance() {
        if (sGson == null) {
            synchronized (GsonUtil.class) {
                if (sGson == null)
                    sGson = sGsonBuilder.create();
            }
        }
        return sGson;
    }

    public static <T> T fromJson(String json, Class<T> tClass) {
        return getInstance().fromJson(json, tClass);
    }

    public static <T> String toJson(T t) {
        return getInstance().toJson(t);
    }

    public static <T> void registerTypeAdapter(Class<T> tClass, Object adapter) {
        sGsonBuilder.registerTypeAdapter(tClass, adapter);
        sGson = sGsonBuilder.create();
    }
}
