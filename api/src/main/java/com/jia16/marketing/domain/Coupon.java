package com.jia16.marketing.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("serial")
public class Coupon implements Serializable {

	private long id;
	
	private Long accountId;
	
	private int activityCategoryId;
	
	private BigDecimal amount;
	
	private String status;
	
	private Date createAt;

	private Date updateAt;

	private String createUser;

	private String updateUser;

	private Date startDate;

	private int activityId;

	private BigDecimal miniAmount;

	private Date expireDate;

    private long invReqId;

    private String subjectType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public int getActivityCategoryId() {
        return activityCategoryId;
    }

    public void setActivityCategoryId(int activityCategoryId) {
        this.activityCategoryId = activityCategoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public BigDecimal getMiniAmount() {
        return miniAmount;
    }

    public void setMiniAmount(BigDecimal miniAmount) {
        this.miniAmount = miniAmount;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public long getInvReqId() {
        return invReqId;
    }

    public void setInvReqId(long invReqId) {
        this.invReqId = invReqId;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }
}

