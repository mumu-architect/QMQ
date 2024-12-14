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

import com.mumu.qmq.broker.cache.CommonCache;
import com.mumu.qmq.broker.constants.BrokerConstants;
import io.netty.util.internal.StringUtil;

/**
 * 加载读取环境变量中配置的mq存储绝对路径地址的对象
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.config
 * @Description: TODO
 * @Author: mumu
 * @CreateTime: 2024-12-14  10:34
 * @Version: 1.0
 */
public class GlobalPropertiesLoader {
    public void loadProperties(){
        GlobalProperties globalProperties=new GlobalProperties();
        String qMqHome=System.getenv(BrokerConstants.Q_MQ_HOME);
        if(StringUtil.isNullOrEmpty(qMqHome)){
            throw new IllegalStateException("Q_MQ_HOME is null");
        }
        globalProperties.setQMqHome(qMqHome);
        CommonCache.setGlobalProperties(globalProperties);
    }
}
