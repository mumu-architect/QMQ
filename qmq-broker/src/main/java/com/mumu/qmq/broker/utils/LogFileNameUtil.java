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

import com.mumu.qmq.broker.cache.CommonCache;
import com.mumu.qmq.broker.constants.BrokerConstants;

/**
 * commitLog文件名生成工具类
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.utils
 * @Description: commitLog文件名生成工具类
 * @Author: mumu
 * @CreateTime: 2024-12-15  09:33
 * @Version: 1.0
 */
public class CommitLogFileNameUtil {
    /**
     * 构建第一份commitLog文件名称
     * @return
     */
    public static String buildFirstCommitLogName(){
        return "00000000";
    }

    /**
     * 构建新的commitLog文件路径
     * @param topicName topic主题名
     * @param commitLogFileName 新文件名
     * @return  新的commitLog文件路径
     */
    public static String buildCommitLogFilePath(String topicName,String commitLogFileName){
        return CommonCache.getGlobalProperties().getQMqHome()
                + BrokerConstants.BASE_STORE_PAtH
                +topicName
                +"/"+commitLogFileName;
    }
    /**
     * 根据老的commitLog文件名，生成新的commitLog文件名
     * @param oldFileName 老的commitLog文件名
     * @return 新的文件名
     */
    public static String increaseCommitLogFileName(String oldFileName){
        if(oldFileName.length()!= 8){
            throw new IllegalArgumentException("fileName must has 8 chars");
        }
        Long fileIndex=Long.valueOf(oldFileName);
        fileIndex++;
        String newFileName=String.valueOf(fileIndex);
        int newFileNameLen = newFileName.length();
        int needFullLen=8-newFileNameLen;
        if(needFullLen<0){
            throw new RuntimeException("unKnow fileName error");
        }
        StringBuffer stringBuffer=new StringBuffer();
        for(int i=0;i<needFullLen;i++){
            stringBuffer.append("0");
        }
        stringBuffer.append(newFileName);
        return stringBuffer.toString();
    }

//    public static void main(String[] args) {
//        String newFileName = CommitLogFileNameUtil.increaseCommitLogFileName("00000011");
//        System.out.println(newFileName);
//    }
}
