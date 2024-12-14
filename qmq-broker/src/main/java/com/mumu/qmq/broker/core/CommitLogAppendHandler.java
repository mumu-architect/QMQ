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

import java.io.IOException;

/**
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.core
 * @Description: TODO
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
    public void prepareMMapLoading(String filPath,String topicName) throws IOException{
        MMapFileModel mMapFileModel=new MMapFileModel();
        mMapFileModel.loadFileInMMap(filPath,0,1*1024*1024);
        mMapFileModelManager.put(topicName,mMapFileModel);
    }

    public void appendMsg(String topic,String content){
        MMapFileModel mMapFileModel=mMapFileModelManager.get(topic);
        if(mMapFileModel==null){
            throw new RuntimeException("topic is invalid!");
        }
        mMapFileModel.writeContent(content.getBytes());
    }


    public void readMsg(String topic){
        MMapFileModel mMapFileModel=mMapFileModelManager.get(topic);
        if(mMapFileModel==null){
            throw new RuntimeException("topic is invalid!");
        }
        byte[] content = mMapFileModel.readContent(0,10);
        System.out.println(new String(content));
    }

}
