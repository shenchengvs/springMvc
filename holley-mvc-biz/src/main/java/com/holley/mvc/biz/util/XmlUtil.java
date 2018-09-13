package com.holley.mvc.biz.util;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.holley.platform.common.util.StringUtil;

/**
 * xml工具类
 * 
 * @author sleep
 * @date 2016-09-13
 */
public class XmlUtil {

    /**
     * String 转 org.dom4j.Document
     * 
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static Document strToDocument(String xml) throws DocumentException {
        return DocumentHelper.parseText(xml);
    }

    /**
     * org.dom4j.Document 转 com.alibaba.fastjson.JSONObject
     * 
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static JSONObject documentToJSONObject(String xml) throws DocumentException {
        return elementToJSONObject(strToDocument(xml).getRootElement());
    }

    /**
     * org.dom4j.Element 转 com.alibaba.fastjson.JSONObject
     * 
     * @param node
     * @return
     */
    public static JSONObject elementToJSONObject(Element node) {
        JSONObject result = new JSONObject();
        // 当前节点的名称、文本内容和属性
        List<Attribute> listAttr = node.attributes();// 当前节点的所有属性的list
        for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
            result.put(attr.getName(), attr.getValue());
        }
        if (StringUtil.isNotEmpty(node.getText())) {
            result.put("text", node.getText());
        }
        // 递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();// 所有一级子节点的list
        if (!listElement.isEmpty()) {
            for (Element e : listElement) {// 遍历所有一级子节点
                if (e.attributes().isEmpty() && e.elements().isEmpty()) // 判断一级节点是否有属性和子节点
                result.put(e.getName(), e.getTextTrim());// 沒有则将当前节点作为上级节点的属性对待
                else {
                    if (!result.containsKey(e.getName())) // 判断父节点是否存在该一级节点名称的属性
                    result.put(e.getName(), new JSONArray());// 没有则创建
                    ((JSONArray) result.get(e.getName())).add(elementToJSONObject(e));// 将该一级节点放入该节点名称的属性对应的值中
                }
            }
        }
        return result;
    }

    // 电设置每屏循显时间
    public static String createXML() throws Exception {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("tasks");
        Element element_task = root.addElement("task").addAttribute("id", "20131205001").addAttribute("mode", "showrawdata");
        element_task.addElement("city").addText("19");
        element_task.addElement("cmd").addAttribute("cmdtype", "sys").addText("74");
        element_task.addElement("termaddr").addText("4414010FB24D");
        element_task.addElement("mpaddr").addText("2:091700408525");
        element_task.addElement("protocol").addText("nwsg-07");
        element_task.addElement("ttl").addText("60");
        Element element_items = element_task.addElement("items").addAttribute("itemtype", "inner");
        element_items.addElement("item").addAttribute("id", "04000302").addText("07");
        Element element_aux = element_task.addElement("aux");
        element_aux.addElement("info").addAttribute("name", "CommPort").addText("31");
        element_aux.addElement("info").addAttribute("name", "BaudRate").addText("8");
        element_aux.addElement("info").addAttribute("name", "CheckType").addText("1");
        element_aux.addElement("info").addAttribute("name", "DataBit").addText("8");
        element_aux.addElement("info").addAttribute("name", "StopBit").addText("0");
        element_aux.addElement("info").addAttribute("name", "RelayTimeOut").addText("9");
        element_aux.addElement("info").addAttribute("name", "AmmCtrlPasswd").addText("02000000");
        element_aux.addElement("info").addAttribute("name", "OperCode").addText("0000");
        // 写入到一个新的文件中
        return document.asXML();
    }

    public static void main(String[] args) {
        try {
            System.out.println(createXML());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
