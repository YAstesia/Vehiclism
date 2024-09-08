package com.seu.vehiclism;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.seu.vehiclism.*.mapper")
@ComponentScan(basePackages = "com.seu.vehiclism")
public class VehiclismApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehiclismApplication.class, args);
    }

}
