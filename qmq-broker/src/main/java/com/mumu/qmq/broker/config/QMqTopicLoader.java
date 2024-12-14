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
import com.mumu.qmq.broker.model.QMqTopicModel;
import com.mumu.qmq.broker.utils.FileContentUtils;
import io.netty.util.internal.StringUtil;

import java.util.List;

/**
 * 消息主题信息对象加载
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.config
 * @Description: TODO
 * @Author: mumu
 * @CreateTime: 2024-12-14  10:49
 * @Version: 1.0
 */
public class QMqTopicLoader {
    private QMqTopicModel qMqTopicModel;
    public void loadProperties(){
        GlobalProperties globalProperties= CommonCache.getGlobalProperties();
        String basePath=globalProperties.getQMqHome();
        if(StringUtil.isNullOrEmpty(basePath)){
            throw new IllegalStateException("Q_MQ_HOME is invalid!");
        }
        String topicJsonFilePath=basePath+"/config/qmq-topic.json";
        String fileContent = FileContentUtils.readFromFile(topicJsonFilePath);
        List<QMqTopicModel> qMqTopicModelList = JSON.parseArray(fileContent, QMqTopicModel.class);
        CommonCache.setQMqTopicModelList(qMqTopicModelList);
    }
}
