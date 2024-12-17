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
import com.mumu.qmq.broker.model.QMqTopicModel;
import com.mumu.qmq.broker.utils.FileContentUtil;
import io.netty.util.internal.StringUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 消息主题信息对象加载
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.config
 * @Description: 消息主题信息对象加载
 * @Author: mumu
 * @CreateTime: 2024-12-14  10:49
 * @Version: 1.0
 */
public class QMqTopicLoader {
    private String filePath;
    /**
     * 加载全局属性，加载topic配置文件信息，最后topic配置信息存入内存
     */
    public void loadProperties(){
        GlobalProperties globalProperties= CommonCache.getGlobalProperties();
        String basePath=globalProperties.getQMqHome();
        if(StringUtil.isNullOrEmpty(basePath)){
            throw new IllegalStateException("Q_MQ_HOME is invalid!");
        }
        filePath=basePath+"/config/qmq-topic.json";
        String fileContent = FileContentUtil.readFromFile(filePath);
        List<QMqTopicModel> qMqTopicModelList = JSON.parseArray(fileContent, QMqTopicModel.class);
        CommonCache.setQMqTopicModelMapList(qMqTopicModelList);
    }

    /**
     * 开启一个刷新内存到磁盘的任务
     */
    public void startRefreshQMqTopicInfoTask(){
        //异步线程
        //每个15秒将内存中的主题信息刷新到磁盘
        CommonThreadPoolConfig.refreshQMqTopicExecutor.execute(new Runnable() {
            @Override
            public void run() {
                do{
                    try {
                        TimeUnit.SECONDS.sleep(BrokerConstants.DEFAULT_REFRESH_MQ_TOPIC_TIME_STEP);
                        System.out.println("属性topic到磁盘");
                        List<QMqTopicModel> qMqTopicModelList=CommonCache.getqMqTopicModelMapList();
                        FileContentUtil.overWriteToFile(filePath,JSON.toJSONString(qMqTopicModelList));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }while(true);
            }
        });
    }
}
