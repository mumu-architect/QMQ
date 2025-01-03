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
import java.util.Map;

/**
 * commitLog文件mmap模型管理，写入，读取
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.core
 * @Description: mmap文件模型管理，写入，读取
 * @Author: mumu
 * @CreateTime: 2024-12-14  09:12
 * @Version: 1.0
 */
public class MMapFileModelManager {
    private static Map<String,MMapFileModel> mMapFileModelMap=new HashMap<>();

    public void put(String topic,MMapFileModel mMapFileModel){
        mMapFileModelMap.put(topic,mMapFileModel);
    }

    public MMapFileModel get(String topic){
        return mMapFileModelMap.get(topic);
    }
}
