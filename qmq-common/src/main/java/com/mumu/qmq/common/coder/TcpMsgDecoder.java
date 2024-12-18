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
import io.netty.handler.codec.ByteToMessageDecoder;
import com.mumu.qmq.common.constants.BrokerConstants;

import java.util.List;

/**
 * TCP消息解码器类
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.nameserver.coder
 * @Description: TCP消息解码器类
 * @Author: mumu
 * @CreateTime: 2024-12-18  14:32
 * @Version: 1.0
 */
public class TcpMsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> in) throws Exception {
        if(byteBuf.readableBytes()>2+4+4){
            if(byteBuf.readShort()!=BrokerConstants.DEFAULT_MAGIC_NUM){
                ctx.close();
                return;
            }
            int code=byteBuf.readInt();
            int len=byteBuf.readInt();
            if(byteBuf.readableBytes()<len){
                ctx.close();
                return;
            }
            byte[] body=new byte[len];
            byteBuf.readBytes(body);
            TcpMsg tcpMsg = new TcpMsg();
            tcpMsg.setMagic(BrokerConstants.DEFAULT_MAGIC_NUM);
            tcpMsg.setCode(code);
            tcpMsg.setLen(len);
            tcpMsg.setBody(body);
            in.add(tcpMsg);
        }
    }
}
