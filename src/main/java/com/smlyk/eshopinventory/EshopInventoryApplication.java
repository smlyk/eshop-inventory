package com.smlyk.eshopinventory;

import com.smlyk.eshopinventory.thread.RequestProcessorThreadPool;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.smlyk.eshopinventory.mapper")
public class EshopInventoryApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(EshopInventoryApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //项目启动后执行
        //初始化工作线程和内存队列
        RequestProcessorThreadPool.init();
    }
}
