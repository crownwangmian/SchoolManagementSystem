package com.itheima.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)  // 什么时候生效
@Target(ElementType.METHOD) // 在哪里生效，可用在方法上
public @interface Log {


}
