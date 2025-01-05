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

import com.mumu.qmq.broker.cache.CommonCache;
import com.mumu.qmq.common.constants.BrokerConstants;
import com.mumu.qmq.broker.model.ConsumerQueueDetailModel;
import com.mumu.qmq.broker.model.ConsumerQueueOffsetModel;
import com.mumu.qmq.broker.model.QMqTopicModel;
import com.mumu.qmq.broker.model.QueueModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消费者队列消费助手类
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.core
 * @Description: 消费者队列消费助手类
 * @Author: mumu
 * @CreateTime: 2024-12-16  18:05
 * @Version: 1.0
 */
public class ConsumeQueueConsumeHandler {

    /**
     * 读取当前最新一条consumerQueue的消息内容
     * @return
     */
    public byte[] consumer(String topic,String consumerGroup,Integer queueId){
        //1.检查参数合法性
        //2.获取当前匹配的队列的最新的consumerQueue的offset是多少
        //3.获取当前匹配的队列存储文件的mmap对象，然后读取offset地址的数据
        QMqTopicModel qMqTopicModel = CommonCache.getQMqTopicModelMap().get(topic);
        if(qMqTopicModel==null){
            throw new RuntimeException("topic "+topic+" not exist!");
        }
        ConsumerQueueOffsetModel.OffsetTable offsetTable = CommonCache.getConsumerQueueOffsetModel().getOffsetTable();
        Map<String, ConsumerQueueOffsetModel.ConsumerGroupDetail> consumerGroupDetailMap = offsetTable.getTopicConsumerGroupDetail();
        ConsumerQueueOffsetModel.ConsumerGroupDetail consumerGroupDetail = consumerGroupDetailMap.get(topic);
        //如果是首次消费
        if(consumerGroupDetail==null){
            consumerGroupDetail=new ConsumerQueueOffsetModel.ConsumerGroupDetail();
            consumerGroupDetailMap.put(topic,consumerGroupDetail);
        }
        Map<String,Map<String,String>> consumerGroupOffsetMap=consumerGroupDetail.getConsumerGroupDetailMap();
        Map<String, String> queueOffsetDetailMap = consumerGroupOffsetMap.get(consumerGroup);
        List<QueueModel> queueList = qMqTopicModel.getQueueList();
        if(queueOffsetDetailMap==null){
            queueOffsetDetailMap=new HashMap<>();
            for (QueueModel queueModel:queueList){
                queueOffsetDetailMap.put(String.valueOf(queueModel.getId()),"00000000");
            }
            consumerGroupOffsetMap.put(consumerGroup,queueOffsetDetailMap);
        }
        String offsetStringInfo = queueOffsetDetailMap.get(String.valueOf(queueId));
        String[] offsetStringArr = offsetStringInfo.split("#");
        Integer consumerQueueOffset = Integer.valueOf(offsetStringArr[1]);
        QueueModel queueModel = queueList.get(queueId);
        //消费到尽头了
        if(queueModel.getLatestOffset().get()<=consumerQueueOffset){
            return null;
        }
        List<ConsumerQueueMMapFileModel> consumerQueueMMapFileModels = CommonCache.getConsumerQueueMMapFileModelManager().get(topic);
        ConsumerQueueMMapFileModel consumerQueueMMapFileModel = consumerQueueMMapFileModels.get(queueId);
        byte[] content = consumerQueueMMapFileModel.readContent(consumerQueueOffset);
        ConsumerQueueDetailModel consumerQueueDetailModel = new ConsumerQueueDetailModel();
        consumerQueueDetailModel.buildFromBytes(content);
        CommitLogMMapFileModel commitLogMMapFileModel = CommonCache.getCommitLogMMapFileModelManager().get(topic);
        return commitLogMMapFileModel.readContent(consumerQueueDetailModel.getMsgIndex(),consumerQueueDetailModel.getMsgLength());
    }

    /**
     * 更新consumerQueue-offset的值
     * @return
     */
    public boolean ack(String topic,String consumerGroup,Integer queueId){
        try {
            ConsumerQueueOffsetModel.OffsetTable offsetTable = CommonCache.getConsumerQueueOffsetModel().getOffsetTable();
            Map<String, ConsumerQueueOffsetModel.ConsumerGroupDetail> consumerGroupDetailMap = offsetTable.getTopicConsumerGroupDetail();
            ConsumerQueueOffsetModel.ConsumerGroupDetail consumerGroupDetail = consumerGroupDetailMap.get(topic);
            Map<String, String> consumeQueueOffsetDetailMap = consumerGroupDetail.getConsumerGroupDetailMap().get(consumerGroup);
            String offsetStrInfo = consumeQueueOffsetDetailMap.get(String.valueOf(queueId));
            String[] offsetStrArr = offsetStrInfo.split("#");
            String fileName = offsetStrArr[0];
            Integer currentOffset = Integer.valueOf(offsetStrArr[1]);
            currentOffset += BrokerConstants.CONSUMER_QUEUE_EACH_MSG_SIZE;
            consumeQueueOffsetDetailMap.put(String.valueOf(queueId), fileName + "#" + currentOffset);
        } catch (Exception e) {
            System.err.println("ack操作异常");
            e.printStackTrace();
        } finally {
        }
        return true;
    }
}
