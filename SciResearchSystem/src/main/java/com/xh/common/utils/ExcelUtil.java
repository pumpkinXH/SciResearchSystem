package com.xh.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.xh.sys.entity.Project;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class ExcelUtil {
    public static void writeExcel(String filename, HttpServletResponse response,  List<?> list, Class<?> headClazz) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(filename, "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename = " + fileName + ".xlsx");
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
        WriteSheet sheet = EasyExcel.writerSheet(0,"sheet")
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())  // 列宽自适应
                .head(headClazz).build();
        excelWriter.write(list, sheet);
        excelWriter.finish();
    }
}
