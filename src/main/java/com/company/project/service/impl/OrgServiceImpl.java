package com.company.project.service.impl;

import com.company.project.dao.OrgMapper;
import com.company.project.model.Org;
import com.company.project.service.OrgService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2019/09/23.
 */
@Service
@Transactional
public class OrgServiceImpl extends AbstractService<Org> implements OrgService {
    @Resource
    private OrgMapper tOrgMapper;

    @Override
    public String findOrgCodeByUserId(Long userId) {

        return tOrgMapper.findOrgCodeByUserId(userId);
    }
}
