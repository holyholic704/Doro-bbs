package com.doro.core.utils;

import cn.hutool.json.JSONUtil;
import com.doro.common.model.res.ResponseResult;
import com.doro.common.constant.Separator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 前端渲染
 */
public class RenderUtil {

    public static void renderJson(HttpServletResponse httpServletResponse, ResponseResult<?> responseResult) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", Separator.STAR);
        ServletOutputStream out = httpServletResponse.getOutputStream();
        String str = JSONUtil.toJsonStr(responseResult);
        out.write(str.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }

}
