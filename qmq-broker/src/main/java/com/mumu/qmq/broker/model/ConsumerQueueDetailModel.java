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
 * 消费者queue模型
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.model
 * @Description: consumerQueue数据结构存储的最小单元对象
 * @Author: mumu
 * @CreateTime: 2024-12-16  13:56
 * @Version: 1.0
 */
public class ConsumerQueueDetailModel {
    private int commitLogFilename;//4byte
    private int msgIndex;//4byte
    private int msgLength;//消息长度 4byte

    public int getCommitLogFilename() {
        return commitLogFilename;
    }

    public void setCommitLogFilename(int commitLogFilename) {
        this.commitLogFilename = commitLogFilename;
    }

    public int getMsgIndex() {
        return msgIndex;
    }

    public void setMsgIndex(int msgIndex) {
        this.msgIndex = msgIndex;
    }

    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }
    public byte[] convertToBytes(){
        byte[] commitLogFilenameBytes = ByteConvertUtil.intToBytes(commitLogFilename);
        byte[] msgIndexBytes = ByteConvertUtil.intToBytes(msgIndex);
        byte[] msgLengthBytes = ByteConvertUtil.intToBytes(msgLength);
        byte[] finalBytes=new byte[12];
        int p=0;
        for(int i=0;i<4;i++){
            finalBytes[p++]=commitLogFilenameBytes[i];
        }
        for(int i=0;i<4;i++){
            finalBytes[p++]=msgIndexBytes[i];
        }
        for(int i=0;i<4;i++){
            finalBytes[p++]=msgLengthBytes[i];
        }
        return finalBytes;
    }
}
