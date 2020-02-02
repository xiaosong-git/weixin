package com.company.project.util;

import com.alibaba.fastjson.JSONObject;
import com.company.project.core.ResultGenerator;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: weixin
 * @description: 自己封装的回应类
 * @author: cwf
 * @create: 2019-10-13 16:02
 **/
public class ResponseUtil {

    public static void response(HttpServletResponse response, String json) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.append(json);
        writer.flush();
        writer.close();
    }
}
