package com.jpmorgan.report.launcher;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.jpmorgan.report.model.Instruction;
import com.jpmorgan.report.model.Report;

/**
 * Demos the Application using sample Data.
 */
public class Launcher {

    private static Report getSampleReport() {

	final List<Instruction> instructions = new ArrayList<>();

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

	return new Report(instructions);

    }

    /**
     * @param args
     */
    public static void main(final String[] args) {

	final Report report = Launcher.getSampleReport();

	System.out.println("Total Number of Valid Received Instructions: " + report.getValidInstructions().size());

	System.out.println(
		"Total Number of Valid Received Incoming Instructions: " + report.getIncomingInstructions().size());

	System.out.println(
		"Total Number of Valid Received Outgoing Instructions: " + report.getOutgoingInstructions().size());

	System.out.print("\n");

	System.out.println("Valid Incoming Instructions as follows: \n");
	report.getIncomingInstructions().forEach(x -> {
	    Launcher.printInstruction(x);
	    System.out.print("\n");
	});

	System.out.println("Valid Outgoing Instructions as follows: \n");
	report.getOutgoingInstructions().forEach(x -> {
	    Launcher.printInstruction(x);
	    System.out.print("\n");
	});

	System.out.println("Ranking of entities based on incoming amount:");
	report.getIncomingEntityRankingList().forEach(x -> {
	    System.out.println(x.getRank() + "\t" + x.getEntity() + "\t" + x.getHighestUSDAmount());

	});

	System.out.print("\n");

	System.out.println("Ranking of entities based on outgoing amount:");
	report.getOutgoingEntityRankingList().forEach(x -> {
	    System.out.println(x.getRank() + "\t" + x.getEntity() + "\t" + x.getHighestUSDAmount());

	});

	System.out.print("\n");

	System.out.print("Total amount in USD settled incoming everyday\n");
	Launcher.printTotalUSDAmountSettledPerDay(report.getMapOfTotalUSDAmountSettledIncomingPerDay());
	System.out.print("\n");
	System.out.print("Total amount in USD settled outgoing everyday\n");
	Launcher.printTotalUSDAmountSettledPerDay(report.getMapOfTotalUSDAmountSettledOutgoingPerDay());
    }

    private static void printInstruction(final Instruction instruction) {

	Objects.requireNonNull(instruction);

	System.out.println(instruction.getEntity());
	System.out.println(instruction.getBuySell());
	System.out.println(instruction.getExchangeRate());
	System.out.println(instruction.getCurrency());
	System.out.println(instruction.getInstructionDate());
	System.out.println(instruction.getSettlementDate());
	System.out.println(instruction.getAcutalSettlementDate());
	System.out.println(instruction.getUnits());
	System.out.println(instruction.getPricePerUnit());
	System.out.println(instruction.getUSDAmount());
    }

    private static void printTotalUSDAmountSettledPerDay(final Map<Date, BigDecimal> settlementTotalPerDayMap) {

	Objects.requireNonNull(settlementTotalPerDayMap);
	final Iterator<Entry<Date, BigDecimal>> it = settlementTotalPerDayMap.entrySet().iterator();

	while (it.hasNext()) {
	    final Entry<Date, BigDecimal> pair = it.next();
	    System.out.println("Date:\t" + pair.getKey() + "\t Total:\t" + pair.getValue());
	}
    }

}
