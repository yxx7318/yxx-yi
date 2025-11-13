package com.yxx.web.controller.monitor;

import com.yxx.common.core.domain.R;
import com.yxx.common.core.utils.SerializationUtils;
import com.yxx.framework.web.domain.ServerDTO;
import com.yxx.framework.web.service.ServerService;
import com.yxx.framework.web.utils.ProcessInfoUtils;
import com.yxx.framework.utils.ServerUtils;
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
public class ServerController
{
    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping
    public R<Server> getInfo(ServerDTO serverDTO) throws Exception
    {
        if (serverDTO.getServerId() == null)
        {
            Server server = new Server();
            ServerUtils.copyTo(server);
            // 获取应用运行情况
            ProcessInfoUtils.setDefault(server.getProcessInfoList());
            return R.ok(server);
        }
        return R.ok(SerializationUtils.deserializeFromByteArray(serverService.getByteInfo(serverDTO).getData(), Server.class));
    }

    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping("/byte")
    public R<byte[]> getInfoByte() throws Exception
    {
        return serverService.getByteInfo();
    }
}
