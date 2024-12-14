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
import com.mumu.qmq.broker.model.QMqTopicModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 统一缓存对象
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.cache
 * @Description: TODO
 * @Author: mumu
 * @CreateTime: 2024-12-14  10:57
 * @Version: 1.0
 */
public class CommonCache {
    public static GlobalProperties globalProperties = new GlobalProperties();
    public static List<QMqTopicModel> qMqTopicModelList = new ArrayList<>();

    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }

    public static List<QMqTopicModel> getQMqTopicModelList() {
        return qMqTopicModelList;
    }

    public static void setQMqTopicModelList(List<QMqTopicModel> qMqTopicModelList) {
        CommonCache.qMqTopicModelList = qMqTopicModelList;
    }
}
