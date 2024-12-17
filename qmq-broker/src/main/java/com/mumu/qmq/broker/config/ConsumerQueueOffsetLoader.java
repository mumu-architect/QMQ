package com.mumu.qmq.broker.config;
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

import com.alibaba.fastjson2.JSON;
import com.mumu.qmq.broker.cache.CommonCache;
import com.mumu.qmq.broker.constants.BrokerConstants;
import com.mumu.qmq.broker.model.ConsumerQueueOffsetModel;
import com.mumu.qmq.broker.model.QMqTopicModel;
import com.mumu.qmq.broker.utils.FileContentUtil;
import io.netty.util.internal.StringUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.config
 * @Description: TODO
 * @Author: mumu
 * @CreateTime: 2024-12-16  15:18
 * @Version: 1.0
 */
public class ConsumerQueueOffsetLoader {

    private String filePath;
    /**
     * 加载全局属性，加载消费队列配置文件信息，最后consumer-queue-offset配置信息存入内存
     */
    public void loadProperties(){
        GlobalProperties globalProperties= CommonCache.getGlobalProperties();
        String basePath=globalProperties.getQMqHome();
        if(StringUtil.isNullOrEmpty(basePath)){
            throw new IllegalStateException("Q_MQ_HOME is invalid!");
        }
        filePath=basePath+"/config/consumer-queue-offset.json";
        String fileContent = FileContentUtil.readFromFile(filePath);
        ConsumerQueueOffsetModel consumerQueueOffsetModel= JSON.parseObject(fileContent, ConsumerQueueOffsetModel.class);
        CommonCache.setConsumerQueueOffsetModel(consumerQueueOffsetModel);
    }

    /**
     * 开启一个刷新内存到磁盘的任务
     */
    public void startRefreshConsumerQueueOffsetTask(){
        //异步线程
        //每个15秒将内存中的主题信息刷新到磁盘
        CommonThreadPoolConfig.refreshConsumerQueueOffsetExecutor.execute(new Runnable() {
            @Override
            public void run() {
                do{
                    try {
                        TimeUnit.SECONDS.sleep(BrokerConstants.DEFAULT_REFRESH_CONSUMER_QUEUE_OFFSET_TIME_STEP);
                        System.out.println("刷新ConsumerQueueOffset到磁盘");
                        ConsumerQueueOffsetModel consumerQueueOffsetModel=CommonCache.getConsumerQueueOffsetModel();
                        FileContentUtil.overWriteToFile(filePath,JSON.toJSONString(consumerQueueOffsetModel));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }while(true);
            }
        });
    }
}
