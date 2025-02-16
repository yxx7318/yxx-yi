package com.yxx.common.yxx.utils;

import java.io.*;

public class SerializationUtils {

    /**
     * 将对象序列化为 byte[]
     *
     * @param obj 要序列化的对象
     * @return 序列化后的字节数组
     * @throws IOException 如果序列化失败
     */
    public static byte[] serializeToByteArray(Object obj) throws IOException {
        if (obj == null) {
            throw new IllegalArgumentException("无法序列化 null 对象");
        }

        try (
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
        ) {

            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();

            return byteArrayOutputStream.toByteArray();
        }
    }

    /**
     * 从 byte[] 反序列化为指定类型的对象
     *
     * @param bytes 字节数组
     * @param clazz 目标对象的类类型
     * @param <T>   泛型类型
     * @return 反序列化后的对象
     * @throws IOException            如果反序列化失败
     * @throws ClassNotFoundException 如果找不到对应的类
     */
    public static <T> T deserializeFromByteArray(byte[] bytes, Class<T> clazz)
            throws IOException, ClassNotFoundException {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("无法反序列化空的字节数组");
        }

        try (
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)
        ) {

            Object obj = objectInputStream.readObject();

            // 检查反序列化后的对象是否是目标类的实例
            if (!clazz.isInstance(obj)) {
                throw new ClassCastException("反序列化后的对象不是 " + clazz.getName() + " 类型");
            }

            return clazz.cast(obj);
        }
    }
}