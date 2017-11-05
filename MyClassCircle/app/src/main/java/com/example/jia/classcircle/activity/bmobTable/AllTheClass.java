package com.example.jia.classcircle.activity.bmobTable;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by jia on 2017/9/21.
 */

public class AllTheClass extends BmobObject{//展示所有班级，由管理员创建班级,学生加入某班的数组
    private String className;
    private String classMateNum;
    private String ClassNum;

    private List<APPUser> stu;//学生加入,管理员也放入里面

    public List<APPUser> getStu() {
        return stu;
    }

    public void setStu(List<APPUser> stu) {
        this.stu = stu;
    }




    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassMateNum() {
        return classMateNum;
    }

    public void setClassMateNum(String classMateNum) {
        this.classMateNum = classMateNum;
    }

    public String getClassNum() {
        return ClassNum;
    }

    public void setClassNum(String classNum) {
        ClassNum = classNum;
    }



}
