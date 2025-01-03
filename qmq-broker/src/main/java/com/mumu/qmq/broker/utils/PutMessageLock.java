package com.mumu.qmq.broker.utils;
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
 * 修改消息的锁接口类
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.utils
 * @Description: 修改消息的锁接口类
 * @Author: mumu
 * @CreateTime: 2024-12-15  17:08
 * @Version: 1.0
 */
public interface PutMessageLock {
    /**
     * 枷锁
     */
    void lock();
    //解锁
    void unlock();
}
