package com.jpmorgan.report.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Instruction {

    private Date acutalSettlementDate;

    private BuySell buySell;

    private String buySellString;

    private Currency currency;

    private String currencyString;

    private String entity;

    private BigDecimal exchangeRate;

    private Date instructionDate;

    private BigDecimal pricePerUnit;

    private Date settlementDate;

    private BigInteger units;

    private BigDecimal USDAmount;

    public Date getAcutalSettlementDate() {
	return this.acutalSettlementDate;
    }

    public BuySell getBuySell() {
	return this.buySell;
    }

    public String getBuySellString() {
	return this.buySellString;
    }

    public Currency getCurrency() {
	return this.currency;
    }

    public String getCurrencyString() {
	return this.currencyString;
    }

    private Date getDateFromString(final String string) {

	final DateFormat format = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
	try {
	    return format.parse(string);
	} catch (final ParseException e) {
	    return null;
	}
    }

    public String getEntity() {
	return this.entity;
    }

    public BigDecimal getExchangeRate() {
	return this.exchangeRate;
    }

    public Date getInstructionDate() {
	return this.instructionDate;
    }

    public BigDecimal getPricePerUnit() {
	return this.pricePerUnit;
    }

    public Date getSettlementDate() {
	return this.settlementDate;
    }

    public BigInteger getUnits() {
	return this.units;
    }

    public BigDecimal getUSDAmount() {
	return this.USDAmount;
    }

    public void setAcutalSettlementDate(final Date acutalSettlementDate) {
	this.acutalSettlementDate = acutalSettlementDate;
    }

    public void setBuySell(final BuySell buySell) {
	this.buySell = buySell;
    }

    public void setBuySellString(final String buySellString) {
	this.buySellString = buySellString;
    }

    public void setCurrency(final Currency currency) {
	this.currency = currency;
    }

    public void setCurrencyString(final String currencyString) {
	this.currencyString = currencyString;
    }

    public void setEntity(final String entity) {
	this.entity = entity;
    }

    public void setExchangeRate(final BigDecimal exchangeRate) {
	this.exchangeRate = exchangeRate;
    }

    public void setInstructionDate(final Date instructionDate) {
	this.instructionDate = instructionDate;
    }

    public void setInstructionDate(final String instructionDate) {
	this.instructionDate = this.getDateFromString(instructionDate);
    }

    public void setPricePerUnit(final BigDecimal pricePerUnit) {
	this.pricePerUnit = pricePerUnit;
    }

    public void setSettlementDate(final Date settlementDate) {
	this.settlementDate = settlementDate;
    }

    public void setSettlementDate(final String settlementDate) {
	this.settlementDate = this.getDateFromString(settlementDate);
    }

    public void setUnits(final BigInteger units) {
	this.units = units;
    }

    public void setUSDAmount(final BigDecimal uSDAmount) {
	this.USDAmount = uSDAmount;
    }
}
