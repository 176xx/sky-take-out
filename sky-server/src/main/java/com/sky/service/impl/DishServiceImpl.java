package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    @Transactional      //事务要么全成功要么全失败
    public void insertDishAndFlavor(DishDTO dishDTO) {
        Dish dish = Dish.builder()
                .id(dishDTO.getId())
                .categoryId(dishDTO.getCategoryId())
                .name(dishDTO.getName())
                .image(dishDTO.getImage())
                .price(dishDTO.getPrice())
                .description(dishDTO.getDescription())
                .status(dishDTO.getStatus()).build();
        dishMapper.insertDish(dish);
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors!=null && flavors.size()>0){
            flavors.forEach(dishFlavor -> {dishFlavor.setDishId(dishId);});
            dishFlavorMapper.insert(flavors);
        }
    }

    @Override
    public PageResult selectDishPage(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.selectDishPage(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }
}
