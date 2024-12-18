package com.mumu.qmq.nameserver.coder;
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

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * TCP消息编码器类
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.nameserver.coder
 * @Description: TCP消息编码器类
 * @Author: mumu
 * @CreateTime: 2024-12-18  14:27
 * @Version: 1.0
 */
public class TcpMsgEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf out) throws Exception {
        TcpMsg tcpMsg=(TcpMsg) msg;
        out.writeShort(tcpMsg.getMagic());
        out.writeInt(tcpMsg.getCode());
        out.writeInt(tcpMsg.getLen());
        out.writeBytes(tcpMsg.getBody());
    }
}
