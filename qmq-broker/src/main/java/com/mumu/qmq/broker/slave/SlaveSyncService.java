package org.idea.eaglemq.broker.slave;

import com.alibaba.fastjson.JSON;
import org.idea.eaglemq.common.coder.TcpMsg;
import org.idea.eaglemq.common.dto.StartSyncReqDTO;
import org.idea.eaglemq.common.enums.BrokerEventCode;
import org.idea.eaglemq.common.event.EventBus;
import org.idea.eaglemq.common.remote.BrokerNettyRemoteClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @Author idea
 * @Date: Created at 2024/7/10
 * @Description 从节点同步服务
 */
public class SlaveSyncService {

    private BrokerNettyRemoteClient brokerNettyRemoteClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(SlaveSyncService.class);

    public boolean connectMasterBrokerNode(String address) {
        String addressAddr[] = address.split(":");
        String ip = addressAddr[0];
        Integer port = Integer.valueOf(addressAddr[1]);
        try {
            brokerNettyRemoteClient = new BrokerNettyRemoteClient(ip,port);
            brokerNettyRemoteClient.buildConnection(new SlaveSyncServerHandler(new EventBus("slave-sync-eventbus")));
            return true;
        }catch (Exception e) {
            LOGGER.error("error connect master broker", e);
        }
        return false;
    }

    public void sendStartSyncMsg() {
        StartSyncReqDTO startSyncReqDTO = new StartSyncReqDTO();
        startSyncReqDTO.setMsgId(UUID.randomUUID().toString());
        TcpMsg tcpMsg = new TcpMsg(BrokerEventCode.START_SYNC_MSG.getCode(), JSON.toJSONBytes(startSyncReqDTO));
        TcpMsg startSyncMsgResp = brokerNettyRemoteClient.sendSyncMsg(tcpMsg,startSyncReqDTO.getMsgId());
        LOGGER.info("startSyncMsgResp is:{}",JSON.toJSONString(startSyncMsgResp));
    }
}
