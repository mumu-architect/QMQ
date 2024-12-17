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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置对象
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.config
 * @Description: 通用的线程池配置
 * @Author: mumu
 * @CreateTime: 2024-12-15  15:28
 * @Version: 1.0
 */
public class CommonThreadPoolConfig {
    //专门用于将topic配置异步刷盘使用
    public static ThreadPoolExecutor  refreshQMqTopicExecutor=new ThreadPoolExecutor(
            1,
            1,
            30,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            r->{
                Thread thread=new Thread(r);
                thread.setName("refresh-q-mq-topic-config");
                return thread;
            });
    //专门用于将consumer-queue配置进度信息异步刷盘使用
    public static ThreadPoolExecutor  refreshConsumerQueueOffsetExecutor=new ThreadPoolExecutor(
            1,
            1,
            30,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            r->{
                Thread thread=new Thread(r);
                thread.setName("refresh-q-mq-topic-config");
                return thread;
            });

}
