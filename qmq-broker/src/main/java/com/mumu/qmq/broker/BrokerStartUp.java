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
import com.mumu.qmq.broker.config.GlobalPropertiesLoader;
import com.mumu.qmq.broker.config.QMqTopicLoader;
import com.mumu.qmq.broker.constants.BrokerConstants;
import com.mumu.qmq.broker.core.CommitLogAppendHandler;
import com.mumu.qmq.broker.model.QMqTopicModel;

import java.io.IOException;
import java.util.List;

/**
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker
 * @Description: TODO
 * @Author: mumu
 * @CreateTime: 2024-12-14  11:54
 * @Version: 1.0
 */
public class BrokerStartUp {
    private static GlobalPropertiesLoader globalPropertiesLoader;
    private static QMqTopicLoader qMqTopicLoader;
    private static CommitLogAppendHandler messageAppendHandler;

    /**
     * 加载配置，映射mmap内存
     * @throws IOException 异常
     */
    private static void initProperties() throws IOException {
        globalPropertiesLoader= new GlobalPropertiesLoader();
        globalPropertiesLoader.loadProperties();
        qMqTopicLoader=new QMqTopicLoader();
        qMqTopicLoader.loadProperties();
        messageAppendHandler=new CommitLogAppendHandler();
        List<QMqTopicModel> qMqTopicModelList = CommonCache.getQMqTopicModelList();
        for(QMqTopicModel qMqTopicModel:qMqTopicModelList){
            String  topicName=qMqTopicModel.getTopic();
            String filePath=CommonCache.getGlobalProperties().getQMqHome()
                    +BrokerConstants.BASE_STORE_PAtH
                    +topicName
                    +"/00000000";
            messageAppendHandler.prepareMMapLoading(filePath,topicName);
        }

    }
//    public static void main(String[] args) throws IOException {
//        initProperties();
//        //模拟初始化文件映射
//        String topic="order_cancel_topic";
//        messageAppendHandler.appendMsg(topic,"this is a test content");
//        messageAppendHandler.readMsg(topic);
//    }
}
