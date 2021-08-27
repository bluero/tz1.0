package com.example.demo.controller;

import java.util.Arrays;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.DrawingEntity;
import com.example.demo.service.DrawingService;
import com.example.demo.common.utils.PageUtils;
import com.example.demo.common.utils.R;



/**
 * 
 *
 * @author lz
 * @email sunlightcs@gmail.com
 * @date 2021-08-26 16:51:41
 */
@RestController
@RequestMapping("/drawing")
public class DrawingController {
    @Autowired
    private DrawingService drawingService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions(":drawing:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = drawingService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{drawingId}")
//    @RequiresPermissions(":drawing:info")
    public R info(@PathVariable("drawingId") Integer drawingId){
		DrawingEntity drawing = drawingService.getById(drawingId);

        return R.ok().put("drawing", drawing);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions(":drawing:save")
    public R save(@RequestBody DrawingEntity drawing){
		drawingService.save(drawing);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions(":drawing:update")
    public R update(@RequestBody DrawingEntity drawing){
		drawingService.updateById(drawing);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions(":drawing:delete")
    public R delete(@RequestBody Integer[] drawingIds){
		drawingService.removeByIds(Arrays.asList(drawingIds));

        return R.ok();
    }

    @RequestMapping("contrastDrawing")
    @ResponseBody
    public void contrastDrawing(){

    }

}
