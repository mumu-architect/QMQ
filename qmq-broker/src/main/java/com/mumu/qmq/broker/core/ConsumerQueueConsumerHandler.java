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

/**
 * 消费者队列消费助手类
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.core
 * @Description: 消费者队列消费助手类
 * @Author: mumu
 * @CreateTime: 2024-12-16  18:05
 * @Version: 1.0
 */
public class ConsumerQueueConsumerHandler {
    /**
     * 读取当前最新一条consumerQueue的消息内容
     * @return
     */
    public byte[] consumer(String topic,String consumerQueue,Integer queueId){
        //1.检查参数合法性
        //2.获取当前匹配的队列的最新的consumerQueue的offset是多少
        //3.获取当前匹配的队列存储文件的mmap对象，然后读取offset地址的数据
        CommonCache.getConsumerQueueOffsetModel();
        return null;
    }

    /**
     * 更新consumerQueue-offset的值
     * @return
     */
    public boolean ack(){
        return false;
    }
}
