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

/**
 * 读取环境变量中配置的mq存储绝对路径地址
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.config
 * @Description: TODO
 * @Author: mumu
 * @CreateTime: 2024-12-14  10:30
 * @Version: 1.0
 */
public class GlobalProperties {
    private String qMqHome;

    public String getQMqHome() {
        return qMqHome;
    }

    public void setQMqHome(String qMqHome) {
        this.qMqHome = qMqHome;
    }
}
