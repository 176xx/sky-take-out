package com.sky.controller.admin;

import com.sky.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminshopController")
@RequestMapping("/admin/shop")
public class ShopController {

    public final static String KEY="SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    public Result setStatus(@PathVariable Integer status){
        redisTemplate.opsForValue().set(KEY,status);
        return Result.success();
    }

    @GetMapping("/status")
    public Result<Integer> getStatus(){
        Integer value = (Integer) redisTemplate.opsForValue().get(KEY);
        return Result.success(value);
    }
}
