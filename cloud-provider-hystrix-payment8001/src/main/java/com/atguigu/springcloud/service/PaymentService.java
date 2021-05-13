package com.atguigu.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    /**
     * 正常访问，肯定OK
     * @param id
     * @return
     */
    public String paymentInfo_OK(Integer id){
        return "线程池： "+Thread.currentThread().getName() + " paymentInfo_OK,id: "+id+"\t"+"O(∩_∩)O哈哈~";
    }

    /**访问超时*/
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler", commandProperties = {
            @HystrixProperty(name ="execution.isolation.thread.timeoutInMilliseconds",value = "5000")
    })
    public String paymentInfo_Timeout(Integer id){
//        int age = 10/0;
        int timeNumber = 3000;
        try{
            TimeUnit.MILLISECONDS.sleep(timeNumber);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return "线程池： "+Thread.currentThread().getName() + " paymentInfo_Timeout,id: "+id+"\t"+"O(∩_∩)O哈哈~" + "耗时（毫秒）：";
    }

    public String paymentInfo_TimeoutHandler(Integer id){
        return "线程池： "+Thread.currentThread().getName() + " 系统繁忙或运行报错，请稍后再试,id: "+id+"\t"+"/(ㄒoㄒ)/~~~";
    }

    //-----服务熔断
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"), //是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), //请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), //时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"), //失败率达到多少后跳闸
    })
    public String paymentCircuitBreaker(Integer id){
        if(id<0){
            throw new RuntimeException("****** id不能为负数");
        }
        String serialNumber = IdUtil.simpleUUID();
        return Thread.currentThread().getName() + "调用成功，流水号：" + serialNumber;
    }
    public String paymentCircuitBreaker_fallback(Integer id){
        return "id不能为负数，请稍后再试，/(ㄒoㄒ)/~~~  id: "+id;
    }

}
