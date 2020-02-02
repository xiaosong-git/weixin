package com.company.project.service;
import com.company.project.model.Key;
import com.company.project.core.Service;


/**
 * Created by CodeGenerator on 2019/10/14.
 */
public interface KeyService extends Service<Key> {

    String findKeyByStatus(String cstatus) throws  Exception;
}
