package com.holley.mvc.model.dto;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ObjEnterpriseDTO {

    private String  name;
    private Integer age;
    private Date    time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
