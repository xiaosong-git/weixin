package com.company.project.service;
import com.company.project.model.CompanyUser;
import com.company.project.core.Service;

import java.util.List;


/**
 * Created by CodeGenerator on 2019/10/09.
 */
public interface CompanyUserService extends Service<CompanyUser> {

    List<CompanyUser> findByUserId(long userId);
}
