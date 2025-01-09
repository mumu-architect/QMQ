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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSONWriter;
import com.mumu.qmq.broker.cache.CommonCache;
import com.mumu.qmq.common.constants.BrokerConstants;
import com.mumu.qmq.broker.model.ConsumeQueueOffsetModel;
import com.mumu.qmq.broker.utils.FileContentUtil;
import io.netty.util.internal.StringUtil;

import java.util.concurrent.TimeUnit;

/**
 * 加载全局属性，加载消费队列配置文件信息
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.config
 * @Description: 加载全局属性，加载消费队列配置文件信息
 * @Author: mumu
 * @CreateTime: 2024-12-16  15:18
 * @Version: 1.0
 */
public class ConsumeQueueOffsetLoader {

    private String filePath;

    public void loadProperties() {
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getQMqHome();
        if(StringUtil.isNullOrEmpty(basePath)){
            throw new IllegalArgumentException("Q_MQ_HOME is invalid!");
        }
        filePath = basePath + "/config/consume-queue-offset.json";
        String fileContent = FileContentUtil.readFromFile(filePath);
        ConsumeQueueOffsetModel consumeQueueOffsetModels = com.alibaba.fastjson.JSON.parseObject(fileContent, ConsumeQueueOffsetModel.class);
        CommonCache.setConsumeQueueOffsetModel(consumeQueueOffsetModels);
    }


    /**
     * 开启一个刷新内存到磁盘的任务
     */
    public void startRefreshConsumeQueueOffsetTask() {
        //异步线程
        //每3秒将内存中的配置刷新到磁盘里面
        CommonThreadPoolConfig.refreshConsumeQueueOffsetExecutor.execute(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        TimeUnit.SECONDS.sleep(BrokerConstants.DEFAULT_REFRESH_CONSUME_QUEUE_OFFSET_TIME_STEP);
                        ConsumeQueueOffsetModel consumeQueueOffsetModel = CommonCache.getConsumeQueueOffsetModel();
                        FileContentUtil.overWriteToFile(filePath, JSON.toJSONString(consumeQueueOffsetModel, SerializerFeature.PrettyFormat));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }while (true);
            }
        });

    }
}
