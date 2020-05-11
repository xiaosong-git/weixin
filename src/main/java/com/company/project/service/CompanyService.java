package com.company.project.service;
import com.company.project.model.Company;
import com.company.project.core.Service;

import java.util.List;


/**
 * Created by CodeGenerator on 2019/10/09.
 */
public interface CompanyService extends Service<Company> {

    List<Company> findByUserId(long userId);

    List<Company> findNotCompanyUser(long userId);

    List<Company> findCompany(String companyName);

    List<Company> findCompanyAddr(String addr);
}
