package com.company.project.service.impl;

import com.company.project.dao.OtherOpenidMapper;
import com.company.project.model.OtherOpenid;
import com.company.project.service.OtherOpenidService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2020/04/28.
 */
@Service
@Transactional
public class OtherOpenidServiceImpl extends AbstractService<OtherOpenid> implements OtherOpenidService {
    @Resource
    private OtherOpenidMapper tblOtherOpenidMapper;

}
