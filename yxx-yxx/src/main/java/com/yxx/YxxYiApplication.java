package com.yxx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author yxx
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class YxxYiApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(YxxYiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  YXX启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " __   __ __  __  __  __  \n" +
                " \\ \\ / / \\ \\/ /  \\ \\/ /  \n" +
                "  \\ V /   >  <    >  <   \n" +
                "  _|_|_  /_/\\_\\  /_/\\_\\  \n" +
                "_| \"\"\" |_|\"\"\"\"\"|_|\"\"\"\"\"| \n" +
                "\"`-0-0-'\"`-0-0-'\"`-0-0-' \n");
    }
}
