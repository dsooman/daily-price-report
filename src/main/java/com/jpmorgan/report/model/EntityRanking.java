package com.jpmorgan.report.model;

import java.math.BigDecimal;

public class EntityRanking {

    private String entity;

    private BigDecimal highestUSDAmount;

    private Integer rank;

    public String getEntity() {
	return this.entity;
    }

    public BigDecimal getHighestUSDAmount() {
	return this.highestUSDAmount;
    }

    public Integer getRank() {
	return this.rank;
    }

    public void setEntity(final String entity) {
	this.entity = entity;
    }

    public void setHighestUSDAmount(final BigDecimal highestUSDAmount) {
	this.highestUSDAmount = highestUSDAmount;
    }

    public void setRank(final Integer rank) {
	this.rank = rank;
    }
}
