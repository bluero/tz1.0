package com.example.demo.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.PowerEntity;
import com.example.demo.service.PowerService;
import com.example.demo.common.utils.PageUtils;
import com.example.demo.common.utils.R;



/**
 * 
 *
 * @author lz
 * @email sunlightcs@gmail.com
 * @date 2021-11-18 17:02:46
 */
@RestController
@RequestMapping("/power")
public class PowerController {
    @Autowired
    private PowerService powerService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = powerService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{userId}")
    public R info(@PathVariable("userId") Long userId){
		PowerEntity power = powerService.getById(userId);

        return R.ok().put("power", power);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PowerEntity power){
		powerService.save(power);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PowerEntity power){
		powerService.updateById(power);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] userIds){
		powerService.removeByIds(Arrays.asList(userIds));
        return R.ok();
    }

}
