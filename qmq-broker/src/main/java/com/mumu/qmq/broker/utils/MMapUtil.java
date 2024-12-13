package com.mumu.qmq.broker.utils;
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
 * @BelongsPackage: com.mumu.qmq.broker.utils
 * @Description: 1.支持基于javaMMap api访问文件能力（文件读写的能力）
 * 2.支持指定的offset的文件映射（结束映射的offset-开始映射的offset=映射的内存体积）=done!
 * 3.文件从指定的offset开始读取 =done!
 * 4.文件从指定的offset开始写入 =done!
 * 5.文件映射后的内存释放
 * @Author: mumu
 * @CreateTime: 2024-12-13  12:26
 * @Version: 1.0
 */
public class MMapUtil {
    private File file;
    private MappedByteBuffer mappedByteBuffer;
    private FileChannel fileChannel;

    /**
     * 指定offset做文件的映射
     * @param filePath  文件路径
     * @param startOffset 开始映射的offset
     * @param mappedSize 映射的体积
     * @throws IOException
     */
    public void loadFileInMMap(String filePath,int startOffset,int mappedSize) throws IOException {
        file=new File(filePath);
        if(!file.exists()) {
            throw new FileNotFoundException("filePath is " + filePath + " inValid");
        }
        fileChannel=new RandomAccessFile(file,"rw").getChannel();
        fileChannel.map(FileChannel.MapMode.READ_WRITE,startOffset,mappedSize);
    }

    /**
     * 支持从文件指定的offset开始读取内容
     * @param readOffset 开始读取位置
     * @param size 读取大小
     * @return 读取内容
     */
    public byte[] readContent(int readOffset,int size){
        mappedByteBuffer.position(readOffset);
        byte[] content =new byte[size];
        int j=0;
        for (int i=0;i<size;i++){
            byte b=mappedByteBuffer.get(readOffset+i);
            content[j++]=b;
        }
        return content;
    }

    /**
     * 更高性能的一种写入api
     * @param content 内容
     */
    public void writeContent(byte[] content){
        this.writeContent(content,false);
    }
    /**
     * 写入数据到磁盘
     * @param content  内容
     * @param force  是否强制刷盘true
     */
    public void writeContent(byte[] content,boolean force){
        //默认刷到page cache中
        //如果需强制刷盘，这里需要兼容
        mappedByteBuffer.put(content);
        if(force){
            //强制刷盘
            mappedByteBuffer.force();
        }
    }

    /**
     * 释放映射的内存空间
     */
    public void clean(){
        if(mappedByteBuffer==null||!mappedByteBuffer.isDirect()||mappedByteBuffer.capacity()==0){
            return;
        }
        invoke(invoke(viewed(mappedByteBuffer),"cleaner"),"clean");
    }

    private Object invoke(final Object target,final String methodName,final Class<?>...args){
        return AccessController.doPrivileged(new PrivilegedAction<Object>(){
            public Object run() {
                try {
                    Method method=method(target,methodName,args);
                    method.setAccessible(true);
                    return method.invoke(target);
                }catch (Exception e){
                    throw new IllegalStateException(e);
                }
            }
        });
    }

    private Method method(Object target,String methodName,Class<?>[] args) throws NoSuchMethodException {
        try{
            return target.getClass().getMethod(methodName,args);

        }catch (NoSuchMethodException e){
            return target.getClass().getDeclaredMethod(methodName,args);
        }
    }

    private ByteBuffer viewed(ByteBuffer buffer){
        String methodName="viewedBuffer";
        Method[] methods=buffer.getClass().getMethods();
        for(int i=0;i<methods.length;i++){
            if(methods[i].getName().equals("attachment")){
                methodName="attachment";
                break;
            }
        }
        ByteBuffer viewedBuffer=(ByteBuffer) invoke(buffer,methodName);
        if(viewedBuffer==null){
            return buffer;
        }else{
            return viewed(viewedBuffer);
        }
    }


}
