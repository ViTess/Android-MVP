package vite.common;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * 提供Gson的序列化、反序列化等相关操作
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

    private static Singleton<Gson, Void> sDefault = new Singleton<Gson, Void>() {
        @Override
        protected Gson newInstance(Void... args) {
            return sGsonBuilder.create();
        }
    };

    public static Gson getInstance() {
        return sDefault.getInstance();
    }

    public static <T> T fromJson(String json, Class<T> tClass) {
        return getInstance().fromJson(json, tClass);
    }

    public static <T> String toJson(T t) {
        return getInstance().toJson(t);
    }

    public static <T> void registerTypeAdapter(Class<T> tClass, Object adapter) {
        sGsonBuilder.registerTypeAdapter(tClass, adapter);
        sDefault = new Singleton<Gson,Void>() {
            @Override
            protected Gson newInstance(Void... args) {
                return sGsonBuilder.create();
            }
        };
    }
}
