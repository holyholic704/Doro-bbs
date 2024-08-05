package com.doro.common.utils;

import cn.hutool.json.JSONUtil;
import com.doro.common.bean.response.Response;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RenderUtil {

    public static void renderJson(HttpServletResponse response, Response re) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        ServletOutputStream out = response.getOutputStream();
        String str = JSONUtil.toJsonStr(re);
        out.write(str.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }

}
