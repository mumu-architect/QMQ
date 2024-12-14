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
 * @CreateTime: 2024-12-14  11:42
 * @Version: 1.0
 */
public class CommitLogModel {
    /**
     * 最新commitLog文件的名称
     */
    private String fileName;
    /**
     * commitLog文件写入的最大上限体积
     */
    private Long offsetLimit;

    /**
     * 最新commitLog文件写入数据的地址
     */
    private AtomicInteger offset;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getOffsetLimit() {
        return offsetLimit;
    }

    public void setOffsetLimit(Long offsetLimit) {
        this.offsetLimit = offsetLimit;
    }

    public AtomicInteger getOffset() {
        return offset;
    }

    public void setOffset(AtomicInteger offset) {
        this.offset = offset;
    }
    public Long countDiff() {
        return this.offsetLimit - this.offset.get();
    }

    @Override
    public String toString() {
        return "CommitLogModel{" +
                "fileName='" + fileName + '\'' +
                ", offsetLimit=" + offsetLimit +
                ", offset=" + offset +
                '}';
    }

}
