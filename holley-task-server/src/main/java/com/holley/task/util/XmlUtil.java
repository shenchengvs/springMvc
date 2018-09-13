package com.holley.task.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.holley.task.model.OrderVo;
import com.holley.task.model.PointVo;
import com.holley.task.server.ServerConfig;

/**
 * xml工具类
 * 
 * @author sc
 * @date 2018-09-10
 */
public class XmlUtil {

    private static Random random              = new Random();
    private static char   numbersAndLetters[] = "0123456789".toCharArray();

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
        if (StringUtils.isNotEmpty(node.getText())) {
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
    public static String createXML(PointVo pointVo) throws Exception {
        OrderVo order = getOrder(pointVo.getStep());
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("tasks");
        Element element_task = root.addElement("task").addAttribute("id", crateId()).addAttribute("mode", "showrawdata");
        element_task.addElement("city").addText(ServerConfig.city);
        element_task.addElement("cmd").addAttribute("cmdtype", "sys").addText(order.getCmd());
        element_task.addElement("termaddr").addText(pointVo.getRtuAddr());
        element_task.addElement("mpaddr").addText(pointVo.getPointOrder() + ":" + pointVo.getPointAddr());
        element_task.addElement("protocol").addText(ServerConfig.protocol);
        element_task.addElement("ttl").addText(ServerConfig.ttl);
        Element element_items = element_task.addElement("items").addAttribute("itemtype", "inner");
        element_items.addElement("item").addAttribute("id", order.getItemId()).addText(order.getItemText());
        // 以下参数待定
        Element element_aux = element_task.addElement("aux");
        element_aux.addElement("info").addAttribute("name", "CommPort").addText(pointVo.getCommPort());
        element_aux.addElement("info").addAttribute("name", "BaudRate").addText(ServerConfig.baudRate);
        element_aux.addElement("info").addAttribute("name", "CheckType").addText(ServerConfig.checkType);
        element_aux.addElement("info").addAttribute("name", "DataBit").addText(ServerConfig.dataBit);
        element_aux.addElement("info").addAttribute("name", "StopBit").addText(ServerConfig.stopBit);
        element_aux.addElement("info").addAttribute("name", "RelayTimeOut").addText(ServerConfig.relayTimeOut);
        if (pointVo.getStep() != 1) {
            if (pointVo.getStep() == 2) {
                // 首次设置出厂开关需要AA级密码
                element_aux.addElement("info").addAttribute("name", "AmmCtrlPasswd").addText(pointVo.getAmmCtrlPasswd());
            } else {
                element_aux.addElement("info").addAttribute("name", "AmmCtrlPasswd").addText(ServerConfig.ammCtrlPasswd);
            }
            element_aux.addElement("info").addAttribute("name", "OperCode").addText(ServerConfig.operCode);
        }

        // 写入到一个新的文件中
        return document.asXML();
    }

    private static OrderVo getOrder(int step) {
        OrderVo vo = new OrderVo();
        switch (step) {
            case 1:
                vo.setCmd("67");
                vo.setItemId("04000101");
                break;
            case 2:
                vo.setCmd("80");
                vo.setItemId("A6A6A6A6");
                vo.setItemText("00");
                break;
            case 3:
                vo.setCmd("81");
                vo.setItemId("04000302");
                vo.setItemText("07");
                break;
            case 4:
                vo.setCmd("82");
                vo.setItemId("04000301");
                vo.setItemText("07");
                break;
            case 5:
                vo.setCmd("83");
                vo.setItemId("04040106");
                vo.setItemText("0002010100");
                break;
            case 6:
                vo.setCmd("83");
                vo.setItemId("04040107");
                vo.setItemText("0000010000");
                break;
            case 7:
                vo.setCmd("80");
                vo.setItemId("A6A6A6A6");
                vo.setItemText("99");
                break;
            default:
                break;
        }
        return vo;
    }

    private static String crateId() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        return formater.format(new Date()) + randomNumber(3);
    }

    public static String randomNumber(int length) {
        if (length < 1) {
            return null;
        }

        char randBuffer[] = new char[length];
        for (int i = 0; i < randBuffer.length; i++)
            randBuffer[i] = numbersAndLetters[random.nextInt(numbersAndLetters.length)];

        return new String(randBuffer);
    }

    public static String createXMLtest() {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("tasks");
        Element element_task = root.addElement("task").addAttribute("id", "20180827001");
        element_task.addElement("cmd").addAttribute("cmdtype", "sys").addText("67");
        element_task.addElement("termaddr").addText("4414010FB24D");

        element_task.addElement("mpaddr").addText("2:091700408525");
        element_task.addElement("protocol").addText("nwsg-07");
        element_task.addElement("ttl").addText("30");
        Element element_items = element_task.addElement("items").addAttribute("itemtype", "sys");
        element_items.addElement("item").addAttribute("id", "04000101").addText("18090705");
        element_task.addElement("status").addText("0");
        element_task.addElement("message").addText("成功");
        return document.asXML();
    }

    public static void main(String[] args) throws DocumentException {
        String dateTime = "18090705";
        dateTime = dateTime.substring(dateTime.length() - 6, dateTime.length());
        String pointAddr = "091700201118";
        pointAddr = pointAddr.substring(pointAddr.length() - 6, pointAddr.length());

        int dateTimei = Integer.valueOf(dateTime);
        int pointAddri = Integer.valueOf(pointAddr);
        System.out.println(pointAddri ^ dateTimei);
    }
}
