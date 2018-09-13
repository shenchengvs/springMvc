package com.holley.mvc.extend;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.holley.mvc.model.def.ExcelBean;
import com.holley.mvc.model.def.MyGlobals;

public class ExcelView extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!model.containsKey(MyGlobals.EXCEL_BEAN_KEY)) {
            return;
        }
        ExcelBean excelBean = (ExcelBean) model.get(MyGlobals.EXCEL_BEAN_KEY);

        String[] titles = excelBean.getTitles();
        String[] properies = excelBean.getProperies();
        String excelName = excelBean.getExcelName();
        List list = excelBean.getList();
        excelName = excelName == null ? System.currentTimeMillis() + "" : excelName;
        // 创建工作表
        HSSFSheet sheet = workbook.createSheet(excelName);
        // 设置Excel的头标题行
        HSSFRow row1 = sheet.createRow(0);

        for (int i = 0; i < titles.length; i++) {
            if (StringUtils.isNotEmpty(titles[i])) {
                HSSFCell cell = row1.createCell(i);
                cell.setCellValue(titles[i]);
            }

        }
        for (int j = 0; j < list.size(); j++) {
            HSSFRow row = sheet.createRow(j + 1);
            Object obj = list.get(j);
            for (int i = 0; i < properies.length; i++) {
                if (StringUtils.isNotEmpty(properies[i])) {
                    row.createCell(i).setCellValue(BeanUtils.getProperty(obj, properies[i]));
                }
            }
        }
        // 设置响应中 下载文件的名称
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((excelName + ".xls").getBytes(), "ISO-8859-1"));
    }

}
