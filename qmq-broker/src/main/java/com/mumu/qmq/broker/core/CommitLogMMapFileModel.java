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

import com.alibaba.fastjson.JSON;
import com.mumu.qmq.broker.cache.CommonCache;
import com.mumu.qmq.broker.constants.BrokerConstants;
import com.mumu.qmq.broker.model.*;
import com.mumu.qmq.broker.utils.LogFileNameUtil;
import com.mumu.qmq.broker.utils.PutMessageLock;
import com.mumu.qmq.broker.utils.UnfailReentrantLock;

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
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * mmap文件模型类
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.core
 * @Description: mmap文件模型类
 * @Author: mumu
 * @CreateTime: 2024-12-14  09:11
 * @Version: 1.0
 */
public class MMapFileModel {
    private File file;
    private MappedByteBuffer mappedByteBuffer;
    private FileChannel fileChannel;
    private String topic;
    private PutMessageLock putMessageLock;


    /**
     * 指定offset做文件的映射
     *
     * @param topicName    文件主题生成内存映射路径
     * @param startOffset 开始映射的offset
     * @param mappedSize  映射的体积 (byte)
     */
    public void loadFileInMMap(String topicName, int startOffset, int mappedSize) throws IOException {
        this.topic=topicName;
        String filePath=  getLatestCommitLogFile(topicName);
        this.doMMap(filePath,startOffset,mappedSize);
        //默认非公平
        putMessageLock=new UnfailReentrantLock();
    }

    /**
     * 执行mmap步骤,指定offset做文件的映射
     * @param filePath
     * @param startOffset
     * @param mappedSize
     * @throws IOException
     */
    private void doMMap(String filePath,int startOffset, int mappedSize) throws IOException {
        file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("filePath is " + filePath + " inValid");
        }
        this.fileChannel = new RandomAccessFile(file, "rw").getChannel();
        this.mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappedSize);
    }

    /**
     * 获取最新commitLog文件路径
     * @param topicName 主题名称
     * @return 最新commitLog文件路径
     */
    private String getLatestCommitLogFile(String topicName){
        QMqTopicModel qMqTopicModel = CommonCache.getQMqTopicModelMap().get(topicName);
        if (qMqTopicModel == null) {
            throw new IllegalArgumentException("topic is inValid! topicName is " + topicName);
        }
        CommitLogModel commitLogModel = qMqTopicModel.getCommitLogModel();
        long diff = commitLogModel.countDiff();
        String filePath = null;
        if (diff == 0) {
            //已经写满了
            CommitLogFilePath commitLogFilePath = this.createNewCommitLogFile(topicName, commitLogModel);
            filePath = commitLogFilePath.getFilePath();
        } else if (diff > 0) {
            //还有机会写入
            filePath = LogFileNameUtil.buildCommitLogFilePath(topicName, commitLogModel.getFileName());
        }
        return filePath;
    }

    class CommitLogFilePath{
        private String fileName;
        private String filePath;

        public CommitLogFilePath(String fileName, String filePath) {
            this.fileName = fileName;
            this.filePath = filePath;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }

    /**
     * 创建新的commitLog文件
     * @param topicName 主题名称
     * @param commitLogModel commitLog数据模型
     * @return 返回创建新的commitLog文件路径
     */
    private CommitLogFilePath createNewCommitLogFile(String topicName,CommitLogModel commitLogModel){
        String newFileName= LogFileNameUtil.increaseCommitLogFileName(commitLogModel.getFileName());
        String newFilePath = LogFileNameUtil.buildCommitLogFilePath(topicName,newFileName);
        File newCommitLogFile = new File(newFilePath);
        try{
            //新的commitLog文件创建
            newCommitLogFile.createNewFile();
            System.out.println("创建了新的commitlog文件");
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return new CommitLogFilePath(newFileName,newFilePath);
    }

    /**
     * 支持从文件的指定offset开始读取内容
     *
     * @param readOffset 读取起始位置
     * @param size 读取大小
     * @return byte[]
     */
    public byte[] readContent(int readOffset, int size) {
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
     * @param commitLogMessageModel 消息模型
     */
    public void writeContent(CommitLogMessageModel commitLogMessageModel) throws IOException {
        this.writeContent(commitLogMessageModel, false);
    }


    /**
     * 写入数据到磁盘当中
     * @param commitLogMessageModel 消息模型
     * @param force 强制刷盘
     */
    public void writeContent(CommitLogMessageModel commitLogMessageModel, boolean force) throws IOException {
//定位到最新的commitLog文件中，记录下当前文件是否已经写满，如果写满，则创建新的文件，并且做新的mmap映射 done
        //如果当前文件没有写满，对content内容做一层封装 done
        //再判断写入是否会导致commitLog写满，如果不会，则选择当前commitLog，如果会则创建新文件，并且做mmap映射 done
        //定位到最新的commitLog文件之后，写入 done

        //定义一个对象专门管理各个topic的最新写入offset值，并且定时刷新到磁盘中（缺少了同步到磁盘的机制）
        //写入数据，offset变更，如果是高并发场景，offset是不是会被多个线程访问？

        //offset会用一个原子类AtomicLong去管理
        //线程安全问题：线程1：111，线程2：122
        //加锁机制 （锁的选择非常重要）
        QMqTopicModel qMqTopicModel = CommonCache.getQMqTopicModelMap().get(topic);
        if (qMqTopicModel == null) {
            throw new IllegalArgumentException("eagleMqTopicModel is null");
        }
        CommitLogModel commitLogModel = qMqTopicModel.getCommitLogModel();
        if (commitLogModel == null) {
            throw new IllegalArgumentException("commitLogModel is null");
        }
        //默认刷到page cache中，
        //如果需要强制刷盘，这里要兼容
        putMessageLock.lock();
        this.checkCommitLogHasEnableSpace(commitLogMessageModel);
        byte[] writeContent = commitLogMessageModel.convertToBytes();
        mappedByteBuffer.put(writeContent);
        AtomicInteger currentLatestMsgOffset = commitLogModel.getOffset();
        this.dispatcher(writeContent,currentLatestMsgOffset.get());
        currentLatestMsgOffset.addAndGet(writeContent.length);
        if (force) {
            //强制刷盘
            mappedByteBuffer.force();
        }

        putMessageLock.unlock();

    }

    /**
     * 将ConsumerQueue文件写入
     * @param writeContent 写入文件的字节，长度+内容
     * @param megIndex 写入内容的起始位置索引
     */
    private void dispatcher(byte[] writeContent,int megIndex) {
        QMqTopicModel qMqTopicModel=CommonCache.getQMqTopicModelMap().get(topic);
        if(qMqTopicModel==null){
            throw new RuntimeException("topic is undefined");
        }
        //TODO
        int queueId=0;
        ConsumerQueueDetailModel consumerQueueDetailModel=new ConsumerQueueDetailModel();
        consumerQueueDetailModel.setCommitLogFilename(Integer.parseInt(qMqTopicModel.getCommitLogModel().getFileName()));
        consumerQueueDetailModel.setMsgIndex(megIndex);
        consumerQueueDetailModel.setMsgLength(writeContent.length);
        System.out.println("写入consumerQueue内容："+ JSON.toJSONString(consumerQueueDetailModel));
        byte[] content = consumerQueueDetailModel.convertToBytes();
        consumerQueueDetailModel.buildFromBytes(content);
        System.out.println("byte convert is ："+ JSON.toJSONString(consumerQueueDetailModel));
        List<ConsumerQueueMMapFileModel> queueModelList=CommonCache.getConsumerQueueMMapFileModelManager().get(topic);
        ConsumerQueueMMapFileModel consumerQueueMMapFileModel=queueModelList.stream().filter(queueModel->queueModel.getQueueId().equals(queueId)).findFirst().orElse(null);
        consumerQueueMMapFileModel.writeContent(content);
        //刷新offset到磁盘配置文件
        QueueModel queueModel=qMqTopicModel.getQueueList().get(queueId);
        queueModel.getLatestOffset().addAndGet(content.length);
    }

    /**
     * 检查写入内容是否会导致commitLog写满，写超
     * @param commitLogMessageModel
     */
    private void checkCommitLogHasEnableSpace(CommitLogMessageModel commitLogMessageModel) throws IOException {
        QMqTopicModel eagleMqTopicModel = CommonCache.getQMqTopicModelMap().get(this.topic);
        CommitLogModel commitLogModel = eagleMqTopicModel.getCommitLogModel();
        long writeAbleOffsetNum = commitLogModel.countDiff();
        //空间不足，需要创建新的commitLog文件并且做映射
        if (!(writeAbleOffsetNum >= commitLogMessageModel.convertToBytes().length)) {
            //00000000文件 -》00000001文件
            //commitLog剩余150byte大小的空间，最新的消息体积是151byte
            CommitLogFilePath commitLogFilePath = this.createNewCommitLogFile(topic, commitLogModel);
            commitLogModel.setOffsetLimit(Long.valueOf(BrokerConstants.COMMIT_LOG_DEFAULT_MMAP_SIZE));
            commitLogModel.setOffset(new AtomicInteger(0));
            commitLogModel.setFileName(commitLogFilePath.getFileName());
            //新文件路径映射进来
            this.doMMap(commitLogFilePath.getFilePath(), 0, BrokerConstants.COMMIT_LOG_DEFAULT_MMAP_SIZE);
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
