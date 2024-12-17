package com.mumu.qmq.broker.cache;
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

import com.mumu.qmq.broker.config.GlobalProperties;
import com.mumu.qmq.broker.core.ConsumerQueueMMapFileModelManager;
import com.mumu.qmq.broker.model.ConsumerQueueOffsetModel;
import com.mumu.qmq.broker.model.QMqTopicModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统一缓存配置文件内容类
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.cache
 * @Description: 统一缓存配置文件内容类
 * @Author: mumu
 * @CreateTime: 2024-12-14  10:57
 * @Version: 1.0
 */
public class CommonCache {
    public static GlobalProperties globalProperties = new GlobalProperties();
    public static List<QMqTopicModel> qMqTopicModelMapList=new ArrayList<>();
    public static ConsumerQueueOffsetModel consumerQueueOffsetModel=new ConsumerQueueOffsetModel();
    public static ConsumerQueueMMapFileModelManager consumerQueueMMapFileModelManager=new ConsumerQueueMMapFileModelManager();

    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }

    public static Map<String,QMqTopicModel> getQMqTopicModelMap() {
        return qMqTopicModelMapList.stream().collect(Collectors.toMap(QMqTopicModel::getTopic,item->item));
    }

    public static List<QMqTopicModel> getqMqTopicModelMapList() {
        return qMqTopicModelMapList;
    }

    public static void setQMqTopicModelMapList(List<QMqTopicModel> qMqTopicModelMap) {
        CommonCache.qMqTopicModelMapList = qMqTopicModelMap;
    }

    public static ConsumerQueueOffsetModel getConsumerQueueOffsetModel() {
        return consumerQueueOffsetModel;
    }

    public static void setConsumerQueueOffsetModel(ConsumerQueueOffsetModel consumerQueueOffsetModel) {
        CommonCache.consumerQueueOffsetModel = consumerQueueOffsetModel;
    }

    public static ConsumerQueueMMapFileModelManager getConsumerQueueMMapFileModelManager() {
        return consumerQueueMMapFileModelManager;
    }

    public static void setConsumerQueueMMapFileModelManager(ConsumerQueueMMapFileModelManager consumerQueueMMapFileModelManager) {
        CommonCache.consumerQueueMMapFileModelManager = consumerQueueMMapFileModelManager;
    }
}
