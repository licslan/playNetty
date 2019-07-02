package com.licslan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@ComponentScan(basePackages= {"com.licslan.dbdao","com.licslan.sb2netty"})
public class App
{
        public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
