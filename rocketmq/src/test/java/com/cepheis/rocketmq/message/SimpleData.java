package com.cepheis.rocketmq.message;

/**
 * Created by diwayou on 2015/10/29.
 */
public class SimpleData {

    private String name;

    private String password;

    // 一定要写默认构造函数，因为反序列化需要
    public SimpleData() {
    }

    public SimpleData(String name, String password) {
        this.name = name;
        this.password = password;
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

    @Override
    public String toString() {
        return "SimpleData{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
