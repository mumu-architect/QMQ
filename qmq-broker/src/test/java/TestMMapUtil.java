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

import com.mumu.qmq.broker.utils.MMapUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @BelongsProject: QMQ
 * @BelongsPackage: PACKAGE_NAME
 * @Description: TODO
 * @Author: mumu
 * @CreateTime: 2024-12-13  13:47
 * @Version: 1.0
 */
public class TestMMapUtil {
    private MMapUtil mMapUtil;
    private static final  String filePath="broker/commitlog/order_cancel_topic/00000000";
    @Before
    public void setUp(){
        mMapUtil=new MMapUtil();
    }

    /**
     * 内存映射
     * @throws IOException
     */
    @Test
    public void testLoadFile() throws IOException {
        mMapUtil.loadFileInMMap(filePath,0,1*1024*10);
    }

    /**
     * 文件写入文件读取
     */
    @Test
    public void testWriteAndReadFile(){
        String str="this is a test content";
        byte[] content=str.getBytes();
        mMapUtil.writeContent(content);
        //consumeQueue
        byte[] readContent= mMapUtil.readContent(0,content.length+1);
        System.out.println(new String(readContent));
    }
}
