package com.jpmorgan.report.service.model

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

import com.jpmorgan.report.model.Currency
import com.jpmorgan.report.model.EntityRanking
import com.jpmorgan.report.model.Instruction
import com.jpmorgan.report.model.Report

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

@Title("Tests for report")
@Subject(Report)
class ReportSpec extends Specification {

    private List<Instruction> instructions;

    def setup() {

	instructions = new ArrayList<>();

	final Instruction instruction1 = new Instruction();
	instruction1.setEntity("foo");
	instruction1.setBuySellString("B");
	instruction1.setExchangeRate(new BigDecimal("9.50"));
	instruction1.setCurrencyString("SAR");
	instruction1.setInstructionDate("01 Jan 2016");
	instruction1.setSettlementDate("08 Sep 2018");
	instruction1.setUnits(new BigInteger("200"));
	instruction1.setPricePerUnit(new BigDecimal("70200000000.25"));
	instructions.add(instruction1);

	final Instruction instruction2 = new Instruction();
	instruction2.setEntity("bar");
	instruction2.setBuySellString("B");
	instruction2.setExchangeRate(new BigDecimal("2.50"));
	instruction2.setCurrencyString("SAR");
	instruction2.setInstructionDate("01 Jan 2016");
	instruction2.setSettlementDate("15 Sep 2018");
	instruction2.setUnits(new BigInteger("2000"));
	instruction2.setPricePerUnit(new BigDecimal("59900000000000.25"));
	instructions.add(instruction2);

	final Instruction instruction3 = new Instruction();
	instruction3.setEntity("baz");
	instruction3.setBuySellString("B");
	instruction3.setExchangeRate(new BigDecimal("0.50"));
	instruction3.setCurrencyString("SAR");
	instruction3.setInstructionDate("01 Jan 2016");
	instruction3.setSettlementDate("08 Sep 2018");
	instruction3.setUnits(new BigInteger("2000"));
	instruction3.setPricePerUnit(new BigDecimal("10000.0000121"));
	instructions.add(instruction3);

	final Instruction instruction4 = new Instruction();
	instruction4.setEntity("tim");
	instruction4.setBuySellString("B");
	instruction4.setExchangeRate(new BigDecimal("0.55"));
	instruction4.setCurrencyString("SGP");
	instruction4.setInstructionDate("21 Jan 2018");
	instruction4.setSettlementDate("21 Jan 2018");
	instruction4.setUnits(new BigInteger("2000"));
	instruction4.setPricePerUnit(new BigDecimal("920.25"));
	instructions.add(instruction4);

	final Instruction instruction5 = new Instruction();
	instruction5.setEntity("bar");
	instruction5.setBuySellString("S");
	instruction5.setExchangeRate(new BigDecimal("0.50"));
	instruction5.setCurrencyString("SGP");
	instruction5.setInstructionDate("01 Jan 2016");
	instruction5.setSettlementDate("13 Sep 2018");
	instruction5.setUnits(new BigInteger("2000"));
	instruction5.setPricePerUnit(new BigDecimal("0.25"));
	instructions.add(instruction5);

	final Instruction instruction6 = new Instruction();
	instruction6.setEntity("baz");
	instruction6.setBuySellString("S");
	instruction6.setExchangeRate(new BigDecimal("0.50"));
	instruction6.setCurrencyString("SGP");
	instruction6.setInstructionDate("01 Jan 2016");
	instruction6.setSettlementDate("08 Sep 2018");
	instruction6.setUnits(new BigInteger("2000"));
	instruction6.setPricePerUnit(new BigDecimal("0.222325"));
	instructions.add(instruction6);

	final Instruction instruction7 = new Instruction();
	instruction7.setEntity("foo");
	instruction7.setBuySellString("S");
	instruction7.setExchangeRate(new BigDecimal("0.50"));
	instruction7.setCurrencyString("SGP");
	instruction7.setInstructionDate("21 Jan 2016");
	instruction7.setSettlementDate("21 Sep 2018");
	instruction7.setUnits(new BigInteger("2000"));
	instruction7.setPricePerUnit(new BigDecimal("0.253232"));
	instructions.add(instruction7);

	final Instruction instruction8 = new Instruction();
	instruction8.setEntity("bar");
	instruction8.setBuySellString("S");
	instruction8.setExchangeRate(new BigDecimal("6.40"));
	instruction8.setCurrencyString("SGP");
	instruction8.setInstructionDate("01 Jan 2016");
	instruction8.setSettlementDate("19 Sep 2018");
	instruction8.setUnits(new BigInteger("2430"));
	instruction8.setPricePerUnit(new BigDecimal("800.25"));
	instructions.add(instruction8);

	final Instruction instruction9 = new Instruction();
	instruction9.setEntity("der");
	instruction9.setBuySellString("S");
	instruction9.setExchangeRate(new BigDecimal("0.50"));
	instruction9.setCurrencyString("SGP");
	instruction9.setInstructionDate("01 Jan 2016");
	instruction9.setSettlementDate("08 Sep 2018");
	instruction9.setUnits(new BigInteger("2000"));
	instruction9.setPricePerUnit(new BigDecimal("0.25"));
	instructions.add(instruction9);

	/*
	 * We will make this instruction invalid by having an invalid Currency
	 * value...
	 */
	final Instruction instruction0 = new Instruction();
	instruction0.setEntity("zero");
	instruction0.setBuySellString("S");
	instruction0.setExchangeRate(new BigDecimal("0.50"));
	instruction0.setCurrencyString("LOD");
	instruction0.setInstructionDate("01 Jan 2016");
	instruction0.setSettlementDate("08 Sep 2018");
	instruction0.setUnits(new BigInteger("2000"));
	instruction0.setPricePerUnit(new BigDecimal("0.25"));
	instructions.add(instruction0);
    }

    def "A Report will return the correct amount of valid received instructions"() {

	when: "A report has been generated"
	Report report = new Report(instructions);

	then: "The report will contain the correct amount of valid received instructions"
	report.getValidInstructions().size() == 9;
    }

    def "A Report will return the correct amount of valid received incoming instructions"() {

	when: "A report has been generated"
	Report report = new Report(instructions);

	then: "The report will contain the correct amount of valid received incoming instructions"
	report.getIncomingInstructions().size() == 5;
    }

    def "A Report will return the correct amount of valid received outgoing instructions"() {

	when: "A report has been generated"
	Report report = new Report(instructions);

	then: "The report will contain the correct amount of valid received outgoing instructions"
	report.getOutgoingInstructions().size() == 4;
    }

    def "A Report will return the correct ranking of entities based on incoming amount"() {

	when: "A report has been generated"
	Report report = new Report(instructions);

	List<EntityRanking> entityRankingList = report.getIncomingEntityRankingList();
	EntityRanking entityRanking0 = entityRankingList.get(0);
	EntityRanking entityRanking1 = entityRankingList.get(1);
	EntityRanking entityRanking2 = entityRankingList.get(2);
	EntityRanking entityRanking3 = entityRankingList.get(3);

	then: "The report will return the correct ranking of entities based on incoming amount"
	entityRanking0.entity == "bar"
	entityRanking0.highestUSDAmount == 12445488.0000
	entityRanking0.rank == 1
	entityRanking1.entity == "foo"
	entityRanking1.highestUSDAmount == 253.23200000
	entityRanking1.rank == 2
	entityRanking2.entity == "der"
	entityRanking2.highestUSDAmount == 250.0000
	entityRanking2.rank == 3
	entityRanking3.entity == "baz"
	entityRanking3.highestUSDAmount == 222.32500000
	entityRanking3.rank == 4
    }

    def "A Report will return the correct ranking of entities based on outgoing amount"() {

	when: "A report has been generated"
	Report report = new Report(instructions);

	List<EntityRanking> entityRankingList = report.getOutgoingEntityRankingList();
	EntityRanking entityRanking0 = entityRankingList.get(0);
	EntityRanking entityRanking1 = entityRankingList.get(1);
	EntityRanking entityRanking2 = entityRankingList.get(2);
	EntityRanking entityRanking3 = entityRankingList.get(3);

	then: "The report will return the correct ranking of entities based on outgoing amount"
	entityRanking0.entity == "bar"
	entityRanking0.highestUSDAmount == 299500000000001250.0000
	entityRanking0.rank == 1
	entityRanking1.entity == "foo"
	entityRanking1.highestUSDAmount == 133380000000475.0000
	entityRanking1.rank == 2
	entityRanking2.entity == "baz"
	entityRanking2.highestUSDAmount == 10000000.012100000
	entityRanking2.rank == 3
	entityRanking3.entity == "tim"
	entityRanking3.highestUSDAmount == 1012275.0000
	entityRanking3.rank == 4
    }

    def "A Report will correctly set the Actual Settlement Date to be a working day for incoming Instructions"() {

	when: "A report has been generated"
	Report report = new Report(instructions);
	Instruction instruction = report.getIncomingInstructions().get(1);

	then: "The actual settlement Date of the Instruction will be a working day with respect to a specific Currency"
	instruction.currency == Currency.SGP
	instruction.settlementDate.toString() == "Sat Sep 08 00:00:00 BST 2018"
	instruction.acutalSettlementDate.toString() == "Mon Sep 10 00:00:00 BST 2018"
    }

    def "A Report will correctly set the Actual Settlement Date to be a working day for outgoing Instructions"() {

	when: "A report has been generated"
	Report report = new Report(instructions);
	Instruction instruction = report.getOutgoingInstructions().get(1);

	then: "The actual settlement Date of the Instruction will be a working day with respect to a specific Currency"
	instruction.currency == Currency.SAR
	instruction.settlementDate.toString() == "Sat Sep 15 00:00:00 BST 2018"
	instruction.acutalSettlementDate.toString() == "Sun Sep 16 00:00:00 BST 2018"
    }

    def "A Report will correctly return a Map of the total USD amount settled incoming per day"() {

	when: "A report has been generated"
	Report report = new Report(instructions);
	Map<Date, BigDecimal> mapOfTotalUSDAmountSettledPerDay = report.getMapOfTotalUSDAmountSettledIncomingPerDay();

	then: "The returned Map will contain the correct total USD amounts per day"
	mapOfTotalUSDAmountSettledPerDay.get(getDateFromString("21 Sep 2018")) == 253.23200000
	mapOfTotalUSDAmountSettledPerDay.get(getDateFromString("10 Sep 2018")) == 472.32500000
	mapOfTotalUSDAmountSettledPerDay.get(getDateFromString("13 Sep 2018")) == 250.0000
	mapOfTotalUSDAmountSettledPerDay.get(getDateFromString("19 Sep 2018")) == 12445488.0000
    }

    def "A Report will correctly return a Map of the total USD amount settled outgoing per day"() {

	when: "A report has been generated"
	Report report = new Report(instructions);
	Map<Date, BigDecimal> mapOfTotalUSDAmountSettledPerDay = report.getMapOfTotalUSDAmountSettledOutgoingPerDay();

	then: "The returned Map will contain the correct total USD amounts per day"
	mapOfTotalUSDAmountSettledPerDay.get(getDateFromString("16 Sep 2018")) == 299500000000001250.0000
	mapOfTotalUSDAmountSettledPerDay.get(getDateFromString("22 Jan 2018")) == 1012275.0000
	mapOfTotalUSDAmountSettledPerDay.get(getDateFromString("09 Sep 2018")) == 133380010000475.012100000
    }

    private Date getDateFromString(final String string) {

	final DateFormat format = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
	try {
	    return format.parse(string);
	} catch (final ParseException e) {
	    return null;
	}
    }
}
