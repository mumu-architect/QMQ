package com.mumu.qmq.broker;
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
import com.mumu.qmq.broker.config.ConsumerQueueOffsetLoader;
import com.mumu.qmq.broker.config.GlobalPropertiesLoader;
import com.mumu.qmq.broker.config.QMqTopicLoader;
import com.mumu.qmq.broker.core.CommitLogAppendHandler;
import com.mumu.qmq.broker.core.ConsumerQueueAppendHandler;
import com.mumu.qmq.broker.model.QMqTopicModel;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Broker启动
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker
 * @Description: Broker启动
 * @Author: mumu
 * @CreateTime: 2024-12-14  11:54
 * @Version: 1.0
 */
public class BrokerStartUp {
    private static GlobalPropertiesLoader globalPropertiesLoader;
    private static QMqTopicLoader qMqTopicLoader;
    private static CommitLogAppendHandler commitLogAppendHandler;
    private static ConsumerQueueOffsetLoader consumerQueueOffsetLoader;
    private static ConsumerQueueAppendHandler consumerQueueAppendHandler;
    /**
     * 初始化配置逻辑
     * 加载配置，映射mmap内存
     * @throws IOException 异常
     */
    private static void initProperties() throws IOException {
        //全局配置
        globalPropertiesLoader= new GlobalPropertiesLoader();
        globalPropertiesLoader.loadProperties();
        //topic配置
        qMqTopicLoader=new QMqTopicLoader();
        qMqTopicLoader.loadProperties();
        qMqTopicLoader.startRefreshQMqTopicInfoTask();
        //消费者队列配置
        consumerQueueOffsetLoader=new ConsumerQueueOffsetLoader();
        consumerQueueOffsetLoader.loadProperties();
        //commitLogAppendHandler
        commitLogAppendHandler=new CommitLogAppendHandler();
        for(QMqTopicModel qMqTopicModel:CommonCache.getQMqTopicModelMap().values()){
            String  topicName=qMqTopicModel.getTopic();
            commitLogAppendHandler.prepareMMapLoading(topicName);
            consumerQueueAppendHandler.prepareConsumerQueue(topicName);
        }

    }
    public static void main(String[] args) throws IOException, InterruptedException {
        initProperties();
        //模拟初始化文件映射
        String topic="order_cancel_topic";
        for(int i=0;i<1000;i++){
            commitLogAppendHandler.appendMsg(topic,("this is a content"+i).getBytes());
            System.out.println("写入数据");
            TimeUnit.SECONDS.sleep(1);
        }
        //commitLogAppendHandler.readMsg(topic);
    }
}
