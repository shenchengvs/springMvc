package com.holley.mvc.biz.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.holley.mvc.mapper.ObjEnterpriseMapper;

/**
 * 反射工具类
 * 
 * @author sc
 */
public class ReflectUtil {

    /**
     * 获取接口泛型参数类型
     * 
     * @param clazz
     * @return
     */
    public static List<Class<?>> getInterfaceGenericParameterType(Class clazz) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        int len = genericInterfaces.length;

        List<Class<?>> list = new ArrayList<Class<?>>();
        for (int i = 0; i < len; i++) {
            Type genericInterface = genericInterfaces[i];

            // 判断接口是否有泛型
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericInterface;

                // 得到所有的泛型【Type类型的数组】
                Type[] interfaceTypes = pt.getActualTypeArguments();

                int length = interfaceTypes.length;

                for (int j = 0; j < length; j++) {
                    // 获取对应的泛型【Type类型】
                    Type interfaceType = interfaceTypes[j];
                    // 转换为Class类型
                    Class<?> interfaceClass = (Class<?>) interfaceType;
                    list.add(interfaceClass);
                }

            }

        }

        return list;
    }

    /**
     * 获取父类泛型参数类型
     * 
     * @param clazz
     * @return
     */
    public static Class<?> getSuperGenericParameterType(Class<?> clazz) {
        Type genericSuperClass = clazz.getGenericSuperclass();

        Class<?> superClassGenericParameterizedType = null;

        // 判断父类是否有泛型
        if (genericSuperClass instanceof ParameterizedType) {
            // 向下转型，以便调用方法
            ParameterizedType pt = (ParameterizedType) genericSuperClass;
            // 只取第一个，因为一个类只能继承一个父类
            Type superClazz = pt.getActualTypeArguments()[0];
            // 转换为Class类型
            superClassGenericParameterizedType = (Class<?>) superClazz;
        }

        return superClassGenericParameterizedType;
    }

    /**
     * 获取obj对象fieldName的Field
     * 
     * @param obj
     * @param fieldName
     * @return
     */
    public static Field getFieldByFieldName(Object obj, String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
            }
        }
        return null;
    }

    /**
     * 获取obj对象fieldName的属性值
     * 
     * @param obj
     * @param fieldName
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getValueByFieldName(Object obj, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;
        if (field != null) {
            if (field.isAccessible()) {
                value = field.get(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }
        return value;
    }

    /**
     * 设置obj对象fieldName的属性值
     * 
     * @param obj
     * @param fieldName
     * @param value
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setValueByFieldName(Object obj, String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException,
                                                                                      IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        if (field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }

    public static List<String> getAllFileName(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<String> list = new ArrayList<String>();
        if (fields != null && fields.length > 0) {
            for (Field f : fields) {
                list.add(f.getName());
            }
        }
        return list;
    }

    public static Object executeMethod(Object obj, Method m, Object... params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return m.invoke(obj, params);
    }

    public static Method getMethodByName(String name, Class clazz, Class... parameterTypes) throws NoSuchMethodException, SecurityException {
        return clazz.getDeclaredMethod(name, parameterTypes);
    }

    public static Method getMethodByName(String name, Class clazz) throws NoSuchMethodException, SecurityException {
        Method[] ms = ObjEnterpriseMapper.class.getDeclaredMethods();
        for (Method m : ms) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }

}
