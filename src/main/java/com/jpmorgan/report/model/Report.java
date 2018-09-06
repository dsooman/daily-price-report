package com.jpmorgan.report.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Report {

    /**
     * List of Incoming Entity rankings.
     */
    private final List<EntityRanking> incomingEntityRankingList;

    /**
     * Map of valid Instructions, grouped by Buy or Sell (i.e B is outgoing, S
     * is incoming).
     */
    private final Map<BuySell, List<Instruction>> instructionsByDirectionMap;

    /**
     * Map where key is Boolean of validity.
     */
    private final Map<Boolean, List<Instruction>> instructionsValidityMap;

    /**
     * List of Outgoing Entity rankings.
     */
    private final List<EntityRanking> outgoingEntityRankingList;

    /**
     * Constructor for the Report.
     *
     * @param instructions
     *            constructs a Report from a <code>List</code> of
     *            <code>Instruction</code> objects
     */
    public Report(final List<Instruction> instructions) {

	Objects.requireNonNull(instructions);

	/*
	 * Partition the Instructions according to whether they are valid or
	 * not...
	 */
	this.instructionsValidityMap = Collections
		.unmodifiableMap(instructions.stream().collect(Collectors.partitioningBy(this::isValidInstruction)));

	/*
	 * Only valid Instructions will be processed...
	 */
	final List<Instruction> validInstructions = this.getValidInstructions();

	/*
	 * Apply the supplied business logic to the instructions...
	 */
	validInstructions.stream().forEach(instruction -> {

	    /*
	     * Our Instructions have passed validation, therefore we can safely
	     * set our Enum values to the supplied String values.
	     */
	    instruction.setBuySell(this.getEnumFromString(BuySell.class, instruction.getBuySellString()));
	    instruction.setCurrency(this.getEnumFromString(Currency.class, instruction.getCurrencyString()));

	    /*
	     * Now we shall set the USD amount using the formula supplied.
	     */
	    instruction.setUSDAmount(instruction.getPricePerUnit()
		    .multiply(new BigDecimal(instruction.getUnits()).multiply(instruction.getExchangeRate())));

	    /*
	     * Initially, we shall set the acutalSettlementDate to be the same
	     * value as the settlementDate value.
	     */
	    instruction.setAcutalSettlementDate(instruction.getSettlementDate());

	    /*
	     * For now, we shall hard code the Instructions for AED or SAR
	     * currencies to have working week Sunday to Thursday. In a real
	     * application, I would seek to use Spring to provide access to a
	     * properties file and have this List of currencies set there. This
	     * List or Set would be injected into this class (which would be a
	     * Spring Bean). I would NOT want the following to be in code I
	     * actually developed professionally. The following is merely to
	     * keep the number of dependencies down.
	     */

	    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
	    final String dayOfWeek = simpleDateFormat.format(instruction.getAcutalSettlementDate()).toUpperCase();

	    /*
	     * A work week starts Monday and ends Friday, unless the currency of
	     * the trade is AED or SAR, where the work week starts Sunday and
	     * ends Thursday. No other holidays to be taken into account....
	     */
	    if (instruction.getCurrency() == Currency.AED || instruction.getCurrency() == Currency.SAR) {

		if (dayOfWeek.equals("FRIDAY")) {

		    /*
		     * I could have refactored this into a private method, but I
		     * want not want to keep this code in a professionally
		     * delivered application. I will leave it in this form for
		     * illustration...
		     */
		    final Calendar cal = Calendar.getInstance();
		    cal.setTime(instruction.getAcutalSettlementDate());
		    cal.add(Calendar.DATE, 2);
		    instruction.setAcutalSettlementDate(cal.getTime());

		}

		/*
		 * I could have refactored this into a private method, but I
		 * want not want to keep this code in a professionally delivered
		 * application. I will leave it in this form for illustration...
		 */
		if (dayOfWeek.equals("SATURDAY")) {

		    final Calendar cal = Calendar.getInstance();
		    cal.setTime(instruction.getAcutalSettlementDate());
		    cal.add(Calendar.DATE, 1);
		    instruction.setAcutalSettlementDate(cal.getTime());

		}
	    } else {

		if (dayOfWeek.equals("SATURDAY")) {

		    /*
		     * I could have refactored this into a private method, but I
		     * want not want to keep this code in a professionally
		     * delivered application. I will leave it in this form for
		     * illustration...
		     */
		    final Calendar cal = Calendar.getInstance();
		    cal.setTime(instruction.getAcutalSettlementDate());
		    cal.add(Calendar.DATE, 2);
		    instruction.setAcutalSettlementDate(cal.getTime());

		}

		/*
		 * I could have refactored this into a private method, but I
		 * want not want to keep this code in a professionally delivered
		 * application. I will leave it in this form for illustration...
		 */
		if (dayOfWeek.equals("SUNDAY")) {

		    final Calendar cal = Calendar.getInstance();
		    cal.setTime(instruction.getAcutalSettlementDate());
		    cal.add(Calendar.DATE, 1);
		    instruction.setAcutalSettlementDate(cal.getTime());

		}
	    }

	});

	this.instructionsByDirectionMap = this.getValidInstructions().stream()
		.collect(Collectors.groupingBy(Instruction::getBuySell));

	this.incomingEntityRankingList = this.getEntityRankingList(this.getIncomingInstructions());
	this.outgoingEntityRankingList = this.getEntityRankingList(this.getOutgoingInstructions());

    }

    /**
     * Generates an <code>EntityRanking List</code> based on a <code>List</code>
     * of <code>Instructions</code>.
     *
     * @param instructions
     *            the <code>List</code> of <code>Instructions</code> to generate
     *            the <code>EntityRanking Set</code> from
     * @return the <code>EntityRanking List</code>
     */
    private List<EntityRanking> getEntityRankingList(final List<Instruction> instructions) {

	Objects.requireNonNull(instructions);

	final Set<EntityRanking> entityRankingSet = new HashSet<>();

	for (final String entity : instructions.stream().map(Instruction::getEntity).collect(Collectors.toSet())) {

	    final Instruction highestUSDAmountInstruction = instructions.stream()
		    .filter(instruction -> instruction.getEntity().equals(entity))
		    .max(Comparator.comparing(Instruction::getUSDAmount)).get();

	    final EntityRanking entityRanking = new EntityRanking();
	    entityRanking.setEntity(entity);
	    entityRanking.setHighestUSDAmount(highestUSDAmountInstruction.getUSDAmount());

	    entityRankingSet.add(entityRanking);

	}

	List<EntityRanking> entityRankingList = new ArrayList<>();
	entityRankingList.addAll(entityRankingSet);

	entityRankingList = entityRankingList.stream()
		.sorted((x, y) -> x.getHighestUSDAmount().compareTo(y.getHighestUSDAmount()))
		.collect(Collectors.toList());

	Collections.reverse(entityRankingList);

	Integer rank = new Integer(1);

	for (final EntityRanking entityRanking : entityRankingList) {
	    entityRanking.setRank(rank);
	    rank++;
	}

	return entityRankingList;
    }

    /**
     * A common method for all enums since they can't have another base class
     *
     * @param <T>
     *            Enum type
     * @param c
     *            enum type. All enums must be all caps.
     * @param string
     *            case insensitive
     * @return corresponding enum, or null
     */
    public <T extends Enum<T>> T getEnumFromString(final Class<T> c, final String string) {
	if (c != null && string != null) {
	    try {
		return Enum.valueOf(c, string.trim().toUpperCase());
	    } catch (final IllegalArgumentException ex) {
	    }
	}
	return null;
    }

    public List<EntityRanking> getIncomingEntityRankingList() {
	return this.incomingEntityRankingList;
    }

    public List<Instruction> getIncomingInstructions() {

	if (this.instructionsByDirectionMap.get(BuySell.S) != null) {
	    return this.instructionsByDirectionMap.get(BuySell.S);
	}

	return new ArrayList<>();
    }

    public List<Instruction> getInvalidInstructions() {
	return this.instructionsValidityMap.get(false);
    }

    /**
     * Returns a <code>Map</code> where the key is the Actual Date of settlement
     * and the value is the total amount in USD settled incoming that day.
     *
     * @return <code>Map</code> of results.
     */
    public Map<Date, BigDecimal> getMapOfTotalUSDAmountSettledIncomingPerDay() {

	final Map<Date, List<Instruction>> map = this.getIncomingInstructions().stream()
		.collect(Collectors.groupingBy(Instruction::getAcutalSettlementDate));

	final Map<Date, BigDecimal> totalsMap = new HashMap<>();

	map.keySet().forEach(date -> {

	    final List<Instruction> instructionsPerSettlementDate = map.get(date);

	    BigDecimal total = new BigDecimal("0");

	    for (final Instruction instruction : instructionsPerSettlementDate) {
		total = total.add(instruction.getUSDAmount());
	    }

	    totalsMap.put(date, total);
	});

	return totalsMap;
    }

    /**
     * Returns a <code>Map</code> where the key is the Actual Date of settlement
     * and the value is the total amount in USD settled outgoing that day.
     *
     * @return <code>Map</code> of results.
     */
    public Map<Date, BigDecimal> getMapOfTotalUSDAmountSettledOutgoingPerDay() {

	final Map<Date, List<Instruction>> map = this.getOutgoingInstructions().stream()
		.collect(Collectors.groupingBy(Instruction::getAcutalSettlementDate));

	final Map<Date, BigDecimal> totalsMap = new HashMap<>();

	map.keySet().forEach(date -> {

	    final List<Instruction> instructionsPerSettlementDate = map.get(date);

	    BigDecimal total = new BigDecimal("0");

	    for (final Instruction instruction : instructionsPerSettlementDate) {
		total = total.add(instruction.getUSDAmount());
	    }

	    totalsMap.put(date, total);
	});

	return totalsMap;
    }

    public List<EntityRanking> getOutgoingEntityRankingList() {
	return this.outgoingEntityRankingList;
    }

    public List<Instruction> getOutgoingInstructions() {
	if (this.instructionsByDirectionMap.get(BuySell.B) != null) {
	    return this.instructionsByDirectionMap.get(BuySell.B);
	}

	return new ArrayList<>();
    }

    /**
     * Gets the total amount in USD settled incoming.
     *
     * @return the total amount in USD settled incoming
     */
    public BigDecimal getTotalUSDAmountSettledIncoming() {

	BigDecimal total = new BigDecimal("0");

	for (final Instruction instruction : this.getIncomingInstructions()) {
	    total = total.add(instruction.getUSDAmount());
	}

	return total;
    }

    /**
     * Gets the total amount in USD settled outgoing.
     *
     * @return the total amount in USD settled outgoing everyday
     */
    public BigDecimal getTotalUSDAmountSettledOutgoing() {

	BigDecimal total = new BigDecimal("0");

	for (final Instruction instruction : this.getOutgoingInstructions()) {
	    total = total.add(instruction.getUSDAmount());
	}

	return total;

    }

    public List<Instruction> getValidInstructions() {
	return this.instructionsValidityMap.get(true);
    }

    /**
     * This method will determine if the Instruction is valid. For example, the
     * Instruction must be for a Buy or a Sell, therefore it might be a value of
     * the BuySell Enum, etc...
     *
     * @param instruction
     *            Instruction to determine validity for
     * @return <code>true</code> if instruction is valid, otherwise
     *         <code>false</code>
     */
    private Boolean isValidInstruction(final Instruction instruction) {

	Objects.requireNonNull(instruction);

	/**
	 * This code could be a lot more developed. The business could provide
	 * more concise rules that determine <code>Instruction</code> validity.
	 * In this case, we are just going to check that supplied
	 * <code>String</code> values correspond to <code>Enum</code> values.
	 * But we could do a lot more here to reject bad data...
	 */

	if (this.getEnumFromString(BuySell.class, instruction.getBuySellString()) == null) {
	    return false;
	}

	if (this.getEnumFromString(Currency.class, instruction.getCurrencyString()) == null) {
	    return false;
	}

	return true;

    }
}
