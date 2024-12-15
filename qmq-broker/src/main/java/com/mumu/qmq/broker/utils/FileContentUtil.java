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

import java.io.BufferedReader;
import java.io.FileReader;


/**
 * 简化版本的文件读取工具类
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.utils
 * @Description: 简化版本的文件读取工具类
 * @Author: mumu
 * @CreateTime: 2024-12-14  11:26
 * @Version: 1.0
 */
public class FileContentReaderUtil {
    public  static String readFromFile(String path){
        try(BufferedReader in = new BufferedReader(new FileReader(path))){
            StringBuffer stringBuffer=new StringBuffer();
            while (in.ready()){
                stringBuffer.append(in.readLine());
            }
            return stringBuffer.toString();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) {
//        String content = FileContentUtils.readFromFile("broker/config/qmq-topic.json");
//        System.out.println(content);
//        List<QMqTopicModel> qMqTopicModelList =JSON.parseArray(content, QMqTopicModel.class);
//        System.out.println(qMqTopicModelList);
//    }
}
