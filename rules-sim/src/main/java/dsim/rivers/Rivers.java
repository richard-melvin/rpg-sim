package dsim.rivers;

import dsim.Contest;
import dsim.Die;

public class Rivers implements Contest {

	final private static Die d100 = new Die(100);

	final private static Die d20 = new Die(20);

	final static int maxNormal = 96;

	public enum Status {

		NONE, ADVANTAGE, DISADVANTAGE, INSPIRATION, INACTIVE
	}

	record Contestant(int base, Status status) {

		public int effectiveSkill() {

			int baseEffective = Math.min(maxNormal, base);

			int excess = base - maxNormal;

			return baseEffective + Math.min(0, excess / 100);

		}

		public int tieBreakBonus() {

			return Math.max(0, base - maxNormal);

		}

		public int successLevel() {
			int bonusDie = switch (status) {
				case ADVANTAGE, INSPIRATION -> Math.max(d20.randomValue(), d20.randomValue());
				case DISADVANTAGE -> Math.min(d20.randomValue(), d20.randomValue());
				case NONE -> d20.randomValue();
				case INACTIVE -> 10;

			};

			return switch (bonusDie) {
				case 20 -> 3;
				case 17, 18, 19 -> 2;
				default -> 1;
			};

		}
		public int failureLevel() {
			int bonusDie = switch (status) {
				case ADVANTAGE -> Math.max(d20.randomValue(), d20.randomValue());
				case DISADVANTAGE, INSPIRATION  -> Math.min(d20.randomValue(), d20.randomValue());
				case NONE -> d20.randomValue();
				case INACTIVE -> 10;

			};

			return switch (bonusDie) {
				case 1 -> -1;
				default -> 0;
			};

		}
	}

	final Contestant pc;

	final Contestant opposition;

	public Rivers(int skill, Status skillBonus, int resistance, Status resistanceBonus) {
		super();
		this.pc = new Contestant(skill, skillBonus);
		this.opposition = new Contestant(resistance, resistanceBonus);

	}

	public Rivers(int skill, int resistance) {
		this(skill, Status.INACTIVE, resistance, Status.INACTIVE);
	}

	@Override
	public double calcWinRatio() {

		double totalWins = 0;
		long totalsSamples = 5_000_000;

		for (int count = 0; count < totalsSamples; count++) {

			if (isWin(d100.randomValue(), d100.randomValue()))
			{
				totalWins++;
			}
		}

		return totalWins / totalsSamples;

	}


	public boolean isWin(int roll1, int roll2) {

		int c1 = roll1 <= pc.effectiveSkill() ? pc.successLevel() : pc.failureLevel();
		int c2 = roll2 <= opposition.effectiveSkill() ? opposition.successLevel() : opposition.failureLevel();
		if (c1 > c2) {
			return true;
		}

		if (c1 == c2) {
			return tiebreakRule(roll1, roll2);
		}
		return false;
	}

	private boolean tiebreakRule(int roll1, int roll2) {
		return roll1 - pc.tieBreakBonus() < roll2 - opposition.tieBreakBonus();
	}

}
