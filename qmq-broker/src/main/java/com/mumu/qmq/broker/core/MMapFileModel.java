package com.mumu.qmq.broker.core;
//
//                       .::::.
//                     .::::::::.
//                    :::::::::::
//                 ..:::::::::::'
//              '::::::::::::'
//                .::::::::::
//           '::::::::::::::..
//                ..::::::::::::.
//              ``::::::::::::::::
//               ::::``:::::::::'        .:::.
//              ::::'   ':::::'       .::::::::.
//            .::::'      ::::     .:::::::'::::.
//           .:::'       :::::  .:::::::::' ':::::.
//          .::'        :::::.:::::::::'      ':::::.
//         .::'         ::::::::::::::'         ``::::.
//     ...:::           ::::::::::::'              ``::.
//    ```` ':.          ':::::::::'                  ::::..
//                       '.:::::'                    ':'````..
//
//
//
//                  年少太轻狂，误入码农行。
//                  白发森森立，两眼直茫茫。
//                  语言数十种，无一称擅长。
//                  三十而立时，无房单身郎。
//
//

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.core
 * @Description: TODO
 * @Author: mumu
 * @CreateTime: 2024-12-14  09:11
 * @Version: 1.0
 */
public class MMapFileModel {
    private File file;
    private MappedByteBuffer mappedByteBuffer;
    private FileChannel fileChannel;


    /**
     * 指定offset做文件的映射
     *
     * @param filePath    文件路径
     * @param startOffset 开始映射的offset
     * @param mappedSize  映射的体积 (byte)
     */
    public void loadFileInMMap(String filePath, int startOffset, int mappedSize) throws IOException {
        file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("filePath is " + filePath + " inValid");
        }
        fileChannel = new RandomAccessFile(file, "rw").getChannel();
        mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappedSize);
    }

    /**
     * 支持从文件的指定offset开始读取内容
     *
     * @param readOffset 读取起始位置
     * @param size 读取大小
     * @return byte[]
     */
    public byte[] readContent(int readOffset, int size) {
        return getBytes(readOffset, size, mappedByteBuffer);
    }

    public static byte[] getBytes(int readOffset, int size, MappedByteBuffer mappedByteBuffer) {
        mappedByteBuffer.position(readOffset);
        byte[] content = new byte[size];
        int j = 0;
        for (int i = 0; i < size; i++) {
            //这里是从内存空间读取数据
            byte b = mappedByteBuffer.get(readOffset + i);
            content[j++] = b;
        }
        return content;
    }


    /**
     * 更高性能的一种写入api
     *
     * @param content 消息内容
     */
    public void writeContent(byte[] content) {
        this.writeContent(content, false);
    }


    /**
     * 写入数据到磁盘当中
     * @param content 消息内容
     * @param force 强制刷盘
     */
    public void writeContent(byte[] content, boolean force) {
        //定位到最新的commitLog文件中，记录下当前文件是否已经写满，则创建新的文件，并且进行内存映射
        //如何当前文件没有写满，对content内容做一层封装，在判断写入是否会导致commitLog写满，如果不会则选择当前commitLog,如果会则创建新文件，并且做内存映射
        //定位到最小commitLog文件之后，写入
        //定义一个对象专门管理各个topic的最新写入的offset值，并定时刷新到磁盘中
        //写入数据，offset变更，如果高并发场景，offset是不是会被多个线程访问？
        //多线程，写入加锁

        //默认刷到page cache中，
        //如果需要强制刷盘，这里要兼容
        mappedByteBuffer.put(content);
        if (force) {
            //强制刷盘
            mappedByteBuffer.force();
        }
    }

    /**
     * 释放mmap内存占用
     */
    public void clean() {
        if (mappedByteBuffer == null || !mappedByteBuffer.isDirect() || mappedByteBuffer.capacity() == 0)
            return;
        invoke(invoke(viewed(mappedByteBuffer), "cleaner"), "clean");
    }

    /**
     *
     * @param target
     * @param methodName
     * @param args
     * @return Object
     */
    private Object invoke(final Object target, final String methodName, final Class<?>... args) {
        return AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                try {
                    Method method = method(target, methodName, args);
                    method.setAccessible(true);
                    return method.invoke(target);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        });
    }

    /**
     *
     * @param target
     * @param methodName
     * @param args
     * @return
     * @throws NoSuchMethodException
     */
    private Method method(Object target, String methodName, Class<?>[] args)
            throws NoSuchMethodException {
        try {
            return target.getClass().getMethod(methodName, args);
        } catch (NoSuchMethodException e) {
            return target.getClass().getDeclaredMethod(methodName, args);
        }
    }

    /**
     *
     * @param buffer
     * @return
     */
    private ByteBuffer viewed(ByteBuffer buffer) {
        String methodName = "viewedBuffer";
        Method[] methods = buffer.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("attachment")) {
                methodName = "attachment";
                break;
            }
        }

        ByteBuffer viewedBuffer = (ByteBuffer) invoke(buffer, methodName);
        if (viewedBuffer == null) {
            return buffer;
        }else {
            return viewed(viewedBuffer);
        }
    }


}
