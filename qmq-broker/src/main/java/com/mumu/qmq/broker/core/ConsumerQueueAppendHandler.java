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
import com.mumu.qmq.broker.model.ConsumerQueueDetailModel;
import com.mumu.qmq.broker.model.QMqTopicModel;
import com.mumu.qmq.broker.model.QueueModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 预处理consumerQueue的mmap空间映射
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.core
 * @Description: TODO
 * @Author: mumu
 * @CreateTime: 2024-12-16  16:14
 * @Version: 1.0
 */
public class ConsumerQueueAppendHandler {
    private ConsumerQueueMMapFileModelManager consumerQueueMMapFileModelManager=new ConsumerQueueMMapFileModelManager();

    public void prepareConsumerQueue(String topicName) throws IOException {
        QMqTopicModel qMqTopicModel = CommonCache.getQMqTopicModelMap().get(topicName);
        List<QueueModel> queueModelList=qMqTopicModel.getQueueList();
        List<ConsumerQueueMMapFileModel> consumerQueueDetailModels=new ArrayList<>();
        //循环遍历，mmap的初始化
        for (QueueModel queueModel:queueModelList){
            ConsumerQueueMMapFileModel consumerQueueMMapFileModel=new ConsumerQueueMMapFileModel();
            consumerQueueMMapFileModel.loadFileInMMap(
                    topicName,
                    queueModel.getId(),
                    queueModel.getLatestOffset(),
                    queueModel.getOffsetLimit()
                    );
            consumerQueueDetailModels.add(consumerQueueMMapFileModel);
        }
        CommonCache.getConsumerQueueMMapFileModelManager().put(topicName,consumerQueueDetailModels);
    }
}
