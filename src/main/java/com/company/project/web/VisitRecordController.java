package com.company.project.web;

import com.company.project.configurer.MvcConfig;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.VisitRecord;
import com.company.project.service.visitRecordService;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
* Created by CodeGenerator on 2019/09/22.
*/
@RestController
@RequestMapping("/visit/record")
public class VisitRecordController {
    @Resource
    private visitRecordService visitRecordService;

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(MvcConfig.class);
    /**
     * @param hour 结束时间
     * @return result
     * @throws Exception
     * @author cwf
     * @date 2019/10/8 16:11
     */
     @PostMapping("/visitRequest")
    public Result visitRequest(VisitRecord visitRecord, @RequestParam() String hour)
             throws Exception {
         try {
             return visitRecordService.visitRequest( visitRecord, hour);
         } catch (Exception e) {
             e.printStackTrace();
         }

         return ResultGenerator.genFailResult("系统异常","");

    }

    @PostMapping("/inviteRequest")
    public Result inviteRequest(VisitRecord visitRecord, @RequestParam() String hour)
            throws Exception {
        try {
            return visitRecordService.inviteRequest( visitRecord, hour);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统异常","");
    }

    /**
     *
     * @param userId 用户id
     * @return
     * @throws Exception
     */
    @PostMapping("/record")
    public Result record(@RequestParam() Long userId,@RequestParam() int pages,@RequestParam() int sizes)
            throws Exception {
        try {
            return visitRecordService.record( userId,pages,sizes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统异常","");
    }
    /**
     *
     * @param loginId 用户id
     * @return
     * @throws Exception
     */
    @PostMapping("/recordDetail")
    public Result recordDetail(@RequestParam() Long visitorId,@RequestParam() Long loginId,@RequestParam() int pages,@RequestParam() int sizes)
            throws Exception {
        try {
            return visitRecordService.recordDetail(visitorId,loginId,pages,sizes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统异常","");

    }

    /**
     * 审核访问或邀约
     * @param visitRecord 访问记录
     * @param loginId 登入人id
     * @return
     * @throws Exception
     */
    @PostMapping("/recordReply")
    public Result recordReply(VisitRecord visitRecord,
                              @RequestParam() Long loginId)
            throws Exception {
        try {
            return visitRecordService.recordReply(visitRecord,loginId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统异常","");
    }
}
