package dsim.rivers;

import dsim.Contest;
import dsim.Die;

public class OpposedRoll implements Contest {

	final static Die d100 = new Die(100);

	final static Die d20 = new Die(20);

	final static int maxNormal = 96;

	public enum Status {

		NONE, ADVANTAGE, DISADVANTAGE, INSPIRATION, INACTIVE
	}

	record Contestant(int base, Status status) {

		public int effectiveSkill() {

			int baseEffective = Math.min(maxNormal, base);

			return baseEffective + Math.min(0, excessSkill() / 100);

		}

		public int excessSkill() {

			return Math.max(0, base - 100);

		}

		public int successLevel() {
			int bonusDie = switch (status) {
				case ADVANTAGE, INSPIRATION -> Math.max(d20.randomValue(), d20.randomValue());
				case DISADVANTAGE -> Math.min(d20.randomValue(), d20.randomValue());
				case NONE -> d20.randomValue();
				case INACTIVE -> 1;
			};

			if (bonusDie + (excessSkill() / 50) >= 20) {
				return 3;
			}
			
			if (bonusDie > 1 && bonusDie + (excessSkill() / 10) >= 17) {
				return 2;
			}
			
			return 1;
		}

		public int failureLevel() {
			int bonusDie = switch (status) {
				case ADVANTAGE -> Math.max(d20.randomValue(), d20.randomValue());
				case DISADVANTAGE, INSPIRATION -> Math.min(d20.randomValue(), d20.randomValue());
				case NONE -> d20.randomValue();
				case INACTIVE -> 2;

			};

			return switch (bonusDie) {
			case 1 -> -1;
			default -> 0;
			};

		}
		
		
		public int countSuccesses (int diceRoll)
		{
			return diceRoll <= effectiveSkill() ? successLevel() : failureLevel();
		}
	}

	final Contestant pc;

	final Contestant opposition;

	final boolean useTieBreak;

	public OpposedRoll(int skill, Status skillBonus, int resistance, Status resistanceBonus, boolean useTieBreak) {
		super();
		this.pc = new Contestant(skill, skillBonus);
		this.opposition = new Contestant(resistance, resistanceBonus);
		this.useTieBreak = useTieBreak;

	}

	public OpposedRoll(int skill, int resistance) {
		this(skill, Status.INACTIVE, resistance, Status.INACTIVE, true);
	}

	@Override
	public double calcWinRatio() {

		double totalWins = 0;
		long totalsSamples = 1_000_000;

		for (int count = 0; count < totalsSamples; count++) {

			if (isWin(d100.randomValue(), d100.randomValue())) {
				totalWins++;
			}
		}

		return totalWins / totalsSamples;

	}

	public boolean isWin(int roll1, int roll2) {

		int c1 = pc.countSuccesses(roll1);
		int c2 = opposition.countSuccesses(roll2);
		if (c1 > c2) {
			return true;
		}

		if (c1 == c2) {
			return tiebreakRule(roll1, roll2);
		}
		return false;
	}

	private boolean tiebreakRule(int roll1, int roll2) {
		if (useTieBreak)
		{
			return roll1 > roll2;			
		}
		
		return false;
		
	}

}
