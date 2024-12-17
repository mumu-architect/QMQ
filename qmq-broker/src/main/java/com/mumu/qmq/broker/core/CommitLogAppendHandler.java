package com.mumu.qmq.broker.core;
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

import com.mumu.qmq.broker.constants.BrokerConstants;
import com.mumu.qmq.broker.model.CommitLogMessageModel;

import java.io.IOException;

/**
 * 预处理mmap空间映射
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.core
 * @Description: 1.预处理mmap空间映射
 * 2.消息写入
 * 3.消息读取
 * @Author: mumu
 * @CreateTime: 2024-12-14  09:25
 * @Version: 1.0
 */
public class CommitLogAppendHandler {
    private MMapFileModelManager mMapFileModelManager=new MMapFileModelManager();
//    private String filPath="broker/commitlog/order_cancel_topic/00000000";
//    private String topicName="order_cancel_topic";

    /**
     * 预处理mmap空间映射
     * @throws IOException
     */
    public void prepareMMapLoading(String topicName) throws IOException{
        MMapFileModel mMapFileModel=new MMapFileModel();
        mMapFileModel.loadFileInMMap(topicName,0, BrokerConstants.COMMIT_LOG_DEFAULT_MMAP_SIZE);
        mMapFileModelManager.put(topicName,mMapFileModel);
    }

    /**
     * 消息写入
     * @param topic
     * @param content
     */
    public void appendMsg(String topic, byte[] content) throws IOException {
        MMapFileModel mMapFileModel=mMapFileModelManager.get(topic);
        if(mMapFileModel==null){
            throw new RuntimeException("topic is invalid!");
        }
        CommitLogMessageModel commitLogMessageModel=new CommitLogMessageModel();
        commitLogMessageModel.setContent(content);
        mMapFileModel.writeContent(commitLogMessageModel);
    }


    /**
     * 消息读取
     * @param topic
     */
    public void readMsg(String topic){
        MMapFileModel mMapFileModel=mMapFileModelManager.get(topic);
        if(mMapFileModel==null){
            throw new RuntimeException("topic is invalid!");
        }
        byte[] content = mMapFileModel.readContent(0,1000);
        System.out.println(new String(content));
    }

}
