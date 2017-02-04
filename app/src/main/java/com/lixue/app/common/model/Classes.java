package com.lixue.app.common.model;

import java.io.Serializable;

/**
 * Created by enlong on 2017/1/23.
 */

public class Classes implements Serializable{
    public String className;
    public String classID;
    public int grade;
    public Teacher[] teachers;
    public Teacher manager;
}
