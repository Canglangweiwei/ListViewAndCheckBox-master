package com.mrwu.demo.bean;

public class DemoBean {

    private String name;
    private double fee;

    /**
     * 标识是否可以删除
     */
    private boolean canRemove = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public boolean isCanRemove() {
        return canRemove;
    }

    public void setCanRemove(boolean canRemove) {
        this.canRemove = canRemove;
    }

    public DemoBean(String name, double fee, boolean canRemove) {
        this.name = name;
        this.fee = fee;
        this.canRemove = canRemove;
    }

    public DemoBean() {
        super();
    }

    @Override
    public String toString() {
        return "DemoBean{" +
                "name='" + name + '\'' +
                ", fee=" + fee +
                ", canRemove=" + canRemove +
                '}';
    }
}
