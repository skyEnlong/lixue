package com.lixue.app.common.model;

/**
 * Created by enlong on 2017/1/23.
 */

public class Teacher extends Person{
    public String teach;  // 教学内容
    public Classes[] classes;  //班级
    public Classes holdClass;  // 负责主持的班级（班主任）
    public School school;
    public String id;

}
