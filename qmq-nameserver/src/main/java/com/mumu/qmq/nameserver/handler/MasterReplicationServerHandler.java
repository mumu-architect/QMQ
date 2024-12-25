package com.mumu.qmq.nameserver.handler;
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

import com.alibaba.fastjson2.JSON;
import com.mumu.qmq.common.coder.TcpMsg;
import com.mumu.qmq.common.enums.NameServerEventCode;
import com.mumu.qmq.nameserver.event.EventBus;
import com.mumu.qmq.nameserver.event.model.Event;
import com.mumu.qmq.nameserver.event.model.StartReplicationEvent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 主从复制服务助手类
 * @BelongsProject: QMQ
 * @BelongsPackage: com.mumu.qmq.nameserver.handler
 * @Description: 主从架构下的复制handler
 * @Author: mumu
 * @CreateTime: 2024-12-24  14:09
 * @Version: 1.0
 */
@ChannelHandler.Sharable
public class MasterSlaveReplicationServerHandler extends SimpleChannelInboundHandler {

    private EventBus eventBus;

    public MasterSlaveReplicationServerHandler(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.init();
    }

    //1.网络请求的接收(netty完成)
    //2.事件发布器的实现（EventBus-》event）Spring的事件，Google Guaua
    //3.事件处理器的实现（Listener-》处理event）
    //4.数据存储（基于Map本地内存的方式存储）
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        TcpMsg tcpMsg = (TcpMsg) msg;
        int code = tcpMsg.getCode();
        byte[] body = tcpMsg.getBody();
        //从节点发起链接，在master端通过密码验证，建立链接
        Event event = null;
        if (NameServerEventCode.START_REPLICATION.getCode() == code) {
            event = JSON.parseObject(body, StartReplicationEvent.class);
        }
        //channelHandlerContext -》 map
        //链接建立完成后，master收到的数据，同步发送给slave节点
        //channelHandlerContext.writeAndFlush();
        event.setChannelHandlerContext(channelHandlerContext);
        eventBus.publish(event);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
