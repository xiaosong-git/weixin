package com.company.project.web;
import com.company.project.annotation.AuthCheckAnnotation;
import com.company.project.configurer.MvcConfig;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.VisitRecord;
import com.company.project.service.visitRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2019/09/22.
*/
@RestController
@RequestMapping("/visit/record")
public class VisitRecordController {
    @Resource
    private visitRecordService visitRecordService;

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(MvcConfig.class);
    @PostMapping("/add")
    public Result add(VisitRecord visitRecord) {
        visitRecordService.save(visitRecord);
        return ResultGenerator.genSuccessResult();
    }
//
//    @PostMapping("/delete")
//    public Result delete(@RequestParam Integer id) {
//        visitRecordService.deleteById(id);
//        return ResultGenerator.genSuccessResult();
//    }
//
//    @PostMapping("/update")
//    public Result update(visitRecord visitRecord) {
//        visitRecordService.update(visitRecord);
//        return ResultGenerator.genSuccessResult();
//    }
    @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        VisitRecord visitRecord = visitRecordService.findById(id);
        return ResultGenerator.genSuccessResult(visitRecord);
    }
    @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<VisitRecord> list = visitRecordService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    /**
     * @param userId 用户id
     * @param visitorId 访问id
     * @param reason 访问原因
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return result
     * @throws Exception
     * @author cwf
     * @date 2019/10/8 16:11
     */
    @AuthCheckAnnotation(checkLogin = true,checkVerify = true)
     @PostMapping("/visitRequest")
    public Result visitRequest(@RequestParam(required = true) String userId, @RequestParam(required = true) String visitorId,
//                               @RequestParam (defaultValue = "0")long companyId,
                                @RequestParam(defaultValue = "无") String reason,
                               @RequestParam(required = true) String startDate, @RequestParam(required = true) String endDate)
//                               @RequestParam(defaultValue = "0") String orgCode)
             throws Exception {
         String cstatus = "applyConfirm";

        return visitRecordService.visitRequest( Long.valueOf(userId), Long.valueOf(visitorId), reason, startDate, endDate);
    }


}