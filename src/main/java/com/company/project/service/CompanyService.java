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
    /**
     * 模糊查询公司，后期可以根据公司热度作预处理
     * @param companyName 公司名称
     * @return java.util.List<com.company.project.model.Company>
     * @author cwf
     * @date 2020/5/14 17:40
     */
    List<Company> findCompany(String companyName);

    List<Company> findCompanyAddr(String addr);
}
