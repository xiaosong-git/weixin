package com.company.project.service.impl;

import com.company.project.dao.OtherWxMapper;
import com.company.project.model.otherWx;
import com.company.project.service.OtherWxService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2020/04/28.
 */
@Service
@Transactional
public class OtherWxServiceImpl extends AbstractService<otherWx> implements OtherWxService {
    @Resource
    private OtherWxMapper tblOtherWxMapper;

}
