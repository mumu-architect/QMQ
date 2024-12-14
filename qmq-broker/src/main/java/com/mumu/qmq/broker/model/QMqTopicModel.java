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

import java.util.List;

/**
 * mq的topic映射对象
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.model
 * @Description: TODO
 * @Author: mumu
 * @CreateTime: 2024-12-14  11:36
 * @Version: 1.0
 */
public class QMqTopicModel {
    private String topic;
    private CommitLogModel commitLogModel;
    private List<QueueModel> queueList;
    private Long createAt;
    private Long updateAt;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public CommitLogModel getCommitLogModel() {
        return commitLogModel;
    }

    public void setCommitLogModel(CommitLogModel commitLogModel) {
        this.commitLogModel = commitLogModel;
    }

    public List<QueueModel> getQueueList() {
        return queueList;
    }

    public void setQueueList(List<QueueModel> queueList) {
        this.queueList = queueList;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "QMqTopicModel{" +
                "topic='" + topic + '\'' +
                ", commitLogModel=" + commitLogModel +
                ", queueList=" + queueList +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
