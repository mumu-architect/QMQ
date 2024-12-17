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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消费队列文件mmap模型管理，写入，读取
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.core
 * @Description: TODO
 * @Author: mumu
 * @CreateTime: 2024-12-16  16:05
 * @Version: 1.0
 */
public class ConsumerQueueMMapFileModelManager {
    private static Map<String, List<ConsumerQueueMMapFileModel>> consumerQueueMMapFileModel=new HashMap<>();

    public void put(String topic,List<ConsumerQueueMMapFileModel> consumerQueueMMapFileModels){
        consumerQueueMMapFileModel.put(topic,consumerQueueMMapFileModels);
    }

    public List<ConsumerQueueMMapFileModel> get(String topic){
        return consumerQueueMMapFileModel.get(topic);
    }

}
