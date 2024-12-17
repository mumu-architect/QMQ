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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自旋锁，原地等待的方式
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.utils
 * @Description: TODO
 * @Author: mumu
 * @CreateTime: 2024-12-15  17:16
 * @Version: 1.0
 */
public class SpinLock implements PutMessageLock{
    AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public void lock() {
        do {
            //将当前值返回后在加1
            int result = atomicInteger.getAndIncrement();
            System.out.println(result);
            if (result == 1) {
                return;
            }
        } while (true);
    }

    @Override
    public void unlock() {
        //将当前值减1后返回
        atomicInteger.decrementAndGet();
    }






}
