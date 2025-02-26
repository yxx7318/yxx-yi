package com.yxx.web.controller.monitor;

import com.yxx.common.core.domain.R;
import com.yxx.common.yxx.utils.SerializationUtils;
import com.yxx.framework.web.domain.ServerDto;
import com.yxx.framework.web.service.ServerService;
import com.yxx.framework.web.utils.ProcessInfoUtils;
import com.yxx.framework.yxx.utils.ServerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yxx.framework.web.domain.Server;

/**
 * 服务器监控
 */
@RestController
@RequestMapping("/monitor/server")
@RequiredArgsConstructor
public class ServerController
{
    private final ServerService serverService;

    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    public R<Server> getInfo(ServerDto serverDto) throws Exception
    {
        if (serverDto.getServerId() == null) {
            Server server = new Server();
            ServerUtils.copyTo(server);
            // 获取应用运行情况
            ProcessInfoUtils.setDefault(server.getProcessInfoList());
            return R.ok(server);
        }
        return R.ok(SerializationUtils.deserializeFromByteArray(serverService.getByteInfo(serverDto).getData(), Server.class));
    }

    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping("/byte")
    public R<byte[]> getInfoByte() throws Exception
    {
        return serverService.getByteInfo();
    }
}
