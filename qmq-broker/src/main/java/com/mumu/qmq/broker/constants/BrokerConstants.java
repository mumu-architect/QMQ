package com.mumu.qmq.broker.constants;
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

/**
 * 常量定义
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.constants
 * @Description: 常量定义
 * @Author: mumu
 * @CreateTime: 2024-12-14  10:39
 * @Version: 1.0
 */
public class BrokerConstants {
    public static final String Q_MQ_HOME="Q_MQ_HOME";
    public static final String BASE_COMMIT_LOG_PATH="/commitlog/";
    public static final String BASE_CONSUMER_QUEUE_PATH="/consumequeue/";
    public static final String SPLIT="/";
    public static final Integer COMMIT_LOG_DEFAULT_MMAP_SIZE=1*1024;//1kb
    public static final Integer DEFAULT_REFRESH_MQ_TOPIC_TIME_STEP=3;
    public static final Integer DEFAULT_REFRESH_CONSUMER_QUEUE_OFFSET_TIME_STEP=1;
}
