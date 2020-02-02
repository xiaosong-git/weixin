package com.company.project.service;
import com.company.project.model.Org;
import com.company.project.core.Service;


/**
 * Created by CodeGenerator on 2019/09/23.
 */
public interface OrgService extends Service<Org> {

    String findOrgCodeByUserId(Long id);
}
