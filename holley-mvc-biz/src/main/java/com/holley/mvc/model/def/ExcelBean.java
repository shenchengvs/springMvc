package com.holley.mvc.model.def;

import java.util.Arrays;
import java.util.List;

public class ExcelBean {

    private int      curreentCount = 10;
    private int      stepCount     = 10;
    private String   excelName;
    private String[] titles        = new String[curreentCount];
    private String[] properies     = new String[curreentCount];
    private List     list;
    private int      count         = 0;

    public ExcelBean(String excelName, List list) {
        this.excelName = excelName;
        this.list = list;
    }

    public String getExcelName() {
        return excelName;
    }

    public String[] getTitles() {
        return titles;
    }

    public String[] getProperies() {
        return properies;
    }

    public List getList() {
        return list;
    }

    public ExcelBean setTitleProperie(String title, String properie) {
        if (count == curreentCount) {
            curreentCount = curreentCount + stepCount;
            titles = Arrays.copyOf(titles, curreentCount);
            properies = Arrays.copyOf(properies, curreentCount);
        }
        titles[count] = title;
        properies[count] = properie;
        count++;
        return this;
    }
}
