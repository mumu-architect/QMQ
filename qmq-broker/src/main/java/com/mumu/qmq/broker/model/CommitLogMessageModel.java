package com.mumu.qmq.broker.model;
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

import com.mumu.qmq.broker.utils.ByteConvertUtil;

/**
 * 消息真实数据存储对象模型
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.model
 * @Description: 消息真实数据存储对象模型
 * @Author: mumu
 * @CreateTime: 2024-12-15  10:21
 * @Version: 1.0
 */
public class CommitLogMessageModel {
    /**
     * 消息体积大小，单位是字节
     */
//    private int size;
    /**
     * 消息内容
     */
    private byte[] content;
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
     * 消息总大小包括消息长度+消息内容
     */
//    public int getTotalSize() {
//        byte[] sizeByte= ByteConvertUtil.intToBytes(content.length);
//        return sizeByte.length+content.length;
//    }



    /**
     * 消息大小和内容转字节然后拼接成真正的消息体
     * @return 真正的消息体
     */
    public byte[] convertToBytes(){
        return  this.getContent();
    }
}
