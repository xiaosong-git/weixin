package com.company.project.service;
import com.company.project.model.Param;
import com.company.project.core.Service;


/**
 * Created by CodeGenerator on 2019/10/11.
 */
public interface ParamService extends Service<Param> {

    String findValueByName(String paramName);
}
