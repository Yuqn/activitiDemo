package com.yuqn.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: yuqn
 * @Date: 2024/5/12 0:49
 * @description:
 * 出差申请中的流程变量
 * @version: 1.0
 */
public class Evection implements Serializable {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 出差单名字
     */
    private String evectionName;
    /**
    * 出差天数
    */
    private Double num;
    /**
     * 出差开始时间
     */
    private Date beginDate;
    /**
     * 出差结束时间
     */
    private Date endDate;
    /**
     * 出差目的地
     */
    private String destination;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEvectionName() {
        return evectionName;
    }

    public void setEvectionName(String evectionName) {
        this.evectionName = evectionName;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Evection{" +
                "id=" + id +
                ", evectionName='" + evectionName + '\'' +
                ", num=" + num +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", destination='" + destination + '\'' +
                '}';
    }

    public Evection(Long id, String evectionName, Double num, Date beginDate, Date endDate, String destination) {
        this.id = id;
        this.evectionName = evectionName;
        this.num = num;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.destination = destination;
    }

    public Evection() {
    }
}
