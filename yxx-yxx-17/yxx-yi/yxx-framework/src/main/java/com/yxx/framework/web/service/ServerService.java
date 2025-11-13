package com.yxx.framework.web.service;

import com.yxx.common.core.domain.R;
import com.yxx.common.core.utils.SerializationUtils;
import com.yxx.framework.web.domain.Server;
import com.yxx.framework.web.domain.ServerDTO;
import com.yxx.framework.web.utils.ProcessInfoUtils;
import com.yxx.framework.utils.ServerUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ServerService {

    /**
     * 获取序列化内容，依据条件获取
     */
    public R<byte[]> getByteInfo(ServerDTO serverDTO) throws IOException {
        return getByteInfo();
    }

    /**
     * 获取序列化内容
     */
    public R<byte[]> getByteInfo() throws IOException {
        Server server = new Server();
        ServerUtils.copyTo(server);
        // 获取应用运行情况
        ProcessInfoUtils.setDefault(server.getProcessInfoList());
        return R.ok(SerializationUtils.serializeToByteArray(server));
    }
}
