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

import com.mumu.qmq.broker.cache.CommonCache;
import com.mumu.qmq.broker.model.QMqTopicModel;
import com.mumu.qmq.broker.model.QueueModel;
import com.mumu.qmq.broker.utils.LogFileNameUtil;
import com.mumu.qmq.broker.utils.PutMessageLock;
import com.mumu.qmq.broker.utils.UnfailReentrantLock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * 消费队列mmap文件映射内存模型
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.core
 * @Description: 消费队列mmap文件映射内存模型
 * @Author: mumu
 * @CreateTime: 2024-12-16  16:00
 * @Version: 1.0
 */
public class ConsumerQueueMMapFileModel {
    private File file;
    private MappedByteBuffer mappedByteBuffer;
    private FileChannel fileChannel;
    private String topic;
    private Integer queueId;
    private String consumerQueueFileName;
    private PutMessageLock putMessageLock;
    /**
     * 指定offset做文件的映射
     *
     * @param topicName    文件主题生成内存映射路径
     * @param startOffset 开始映射的offset
     * @param mappedSize  映射的体积 (byte)
     */
    public void loadFileInMMap(String topicName,Integer queueId, int startOffset, int mappedSize) throws IOException {
        this.topic=topicName;
        this.queueId=queueId;
        String filePath=  getLatestConsumerQueueFile();
        this.doMMap(filePath,startOffset,mappedSize);
        //默认非公平
        putMessageLock=new UnfailReentrantLock();
    }
    public void writeContent(byte[] content,boolean force){
        try {
            putMessageLock.lock();
            mappedByteBuffer.put(content);
            //强刷落磁盘
            if(force){
                mappedByteBuffer.force();
            }
        }finally {
            putMessageLock.unlock();
        }
    }
    /**
     * 写入consumerQueue
     * @param content
     */
    public void writeContent(byte[] content){
        writeContent(content,false);
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
     * @return 最新commitLog文件路径
     */
    private String getLatestConsumerQueueFile(){
        QMqTopicModel qMqTopicModel = CommonCache.getQMqTopicModelMap().get(topic);
        if (qMqTopicModel == null) {
            throw new IllegalArgumentException("topic is inValid! topicName is " + topic);
        }
        List<QueueModel> queueModelList=qMqTopicModel.getQueueList();
        QueueModel queueModel = queueModelList.get(queueId);
        if(queueModel==null){
            throw  new IllegalArgumentException("queueId is inValid queueId is "+queueId);
        }
        long diff = queueModel.countDiff();
        String filePath = null;
        if (diff == 0) {
            //已经写满了
            filePath = this.createNewConsumerQueueFile(queueModel.getFileName());
        } else if (diff > 0) {
            //还有机会写入
            filePath = LogFileNameUtil.buildConsumerQueueFilePath(topic, queueId,queueModel.getFileName());
        }
        return filePath;
    }

    /**
     * 创建新的ConsumerQueue文件
     * @param fileName 消费者队列文件名称
     * @return 返回创建新的ConsumerQueue文件路径
     */
    private String createNewConsumerQueueFile(String fileName){
        String newFileName= LogFileNameUtil.increaseConsumerQueueFileName(fileName);
        String newFilePath = LogFileNameUtil.buildConsumerQueueFilePath(topic,queueId,newFileName);
        File newConsumertQueueFile = new File(newFilePath);
        try{
            //新的commitLog文件创建
            newConsumertQueueFile.createNewFile();
            System.out.println("创建了新的newConsumertQueueFile文件");
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return newFilePath;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getQueueId() {
        return queueId;
    }

    public void setQueueId(Integer queueId) {
        this.queueId = queueId;
    }
}
