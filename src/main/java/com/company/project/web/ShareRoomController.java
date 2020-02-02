package com.company.project.web;
import com.company.project.annotation.AuthCheckAnnotation;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.ShareRoom;
import com.company.project.service.ShareRoomService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2019/09/22.
*/
@RestController
@RequestMapping("/share/room")
public class ShareRoomController {
    @Resource
    private ShareRoomService shareRoomService;

//    @PostMapping("/add")
//    public Result add(shareRoom shareRoom) {
//        shareRoomService.save(shareRoom);
//        return ResultGenerator.genSuccessResult();
//    }
//
//    @PostMapping("/delete")
//    public Result delete(@RequestParam Integer id) {
//        shareRoomService.deleteById(id);
//        return ResultGenerator.genSuccessResult();
//    }
//
//    @PostMapping("/update")
//    public Result update(shareRoom shareRoom) {
//        shareRoomService.update(shareRoom);
//        return ResultGenerator.genSuccessResult();
//    }
//
//    @PostMapping("/detail")
//    public Result detail(@RequestParam Integer id) {
//        shareRoom shareRoom = shareRoomService.findById(id);
//        return ResultGenerator.genSuccessResult(shareRoom);
//    }
//
//    @PostMapping("/list")
//    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
//        PageHelper.startPage(page, size);
//        List<shareRoom> list = shareRoomService.findAll();
//        PageInfo pageInfo = new PageInfo(list);
//        return ResultGenerator.genSuccessResult(pageInfo);
//    }
    @AuthCheckAnnotation(checkLogin = true,checkVerify = false)
    @PostMapping("/orgList")
    public Result orgList(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size
            ,@RequestParam(defaultValue = "0") String orgCode) {
        PageHelper.startPage(page, size);
        List<ShareRoom> list = shareRoomService.findByOrg(orgCode);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
