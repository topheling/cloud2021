
package com.atguigu.springcloud.alibaba.service;

import com.atguigu.springcloud.alibaba.domain.Order;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @auther zzyy
 * @create 2019-12-11 16:48
 */
public interface OrderService {

    /**
     * 创建订单
     */
    void create(Order order);
}



