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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.broker.model
 * @Description: TODO
 * @Author: mumu
 * @CreateTime: 2024-12-14  11:37
 * @Version: 1.0
 */
public class QueueModel {
    private Integer id;
    private String fileName;
    private Integer offsetLimit;
    private AtomicInteger latestOffset;
    private Integer lastOffset;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getOffsetLimit() {
        return offsetLimit;
    }

    public void setOffsetLimit(Integer offsetLimit) {
        this.offsetLimit = offsetLimit;
    }

    public AtomicInteger getLatestOffset() {
        return latestOffset;
    }

    public void setLatestOffset(AtomicInteger latestOffset) {
        this.latestOffset = latestOffset;
    }

    public Integer getLastOffset() {
        return lastOffset;
    }

    public void setLastOffset(Integer lastOffset) {
        this.lastOffset = lastOffset;
    }

    @Override
    public String toString() {
        return "QueueModel{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", offsetLimit=" + offsetLimit +
                ", latestOffset=" + latestOffset +
                ", lastOffset=" + lastOffset +
                '}';
    }
}
