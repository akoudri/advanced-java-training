package com.akfc.training.ioc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:hello.properties")
@ComponentScan(basePackages = "com.akfc.training.ioc")
@EnableAspectJAutoProxy
public class MainConfig {}
