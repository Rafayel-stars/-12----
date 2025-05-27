package org.example.bookmanagesystem01.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String no;
    private String name;
    private String password;
    private int age;
    private int sex; // 性别（0未知，1男，2女）
    private String phone;
    private int is_valid; // 是否有效（1有效，0无效）
    private String userType; // 用户类型（admin=管理员，user=普通用户）
    private Date registerTime; // 注册时间

    public User() {
    }

    // 包含注册时间的构造函数
    public User(String no, String name, String password, int age, int sex, String phone, int is_valid, String userType, Date registerTime) {
        this.no = no;
        this.name = name;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.phone = phone;
        this.is_valid = is_valid;
        this.userType = userType;
        this.registerTime = registerTime;
    }

    // 不含注册时间的构造函数（保留原有逻辑）
    public User(String no, String name, String password, int age, int sex, String phone, int is_valid, String userType) {
        this.no = no;
        this.name = name;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.phone = phone;
        this.is_valid = is_valid;
        this.userType = userType;
    }

    // getter 和 setter 方法
    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    // 其他 getter 和 setter 方法（保持不变）
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(int is_valid) {
        this.is_valid = is_valid;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", no='" + no + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", phone='" + phone + '\'' +
                ", is_valid=" + is_valid +
                ", userType='" + userType + '\'' +
                ", registerTime=" + registerTime +
                '}';
    }
}