package dsim.rivers;

import dsim.Contest;
import dsim.Die;

public class Rivers implements Contest {

	final private Die d100 = new Die(100);

	final private Die d20 = new Die(20);

	final static int maxNormal = 96;
	
	record Contestant (int base, int bonusDie) {
		
		public int effectiveSkill() {

			int baseEffective = Math.min(maxNormal, base);

			int excess = base - maxNormal;

			return baseEffective + Math.min(0, excess / 100);

		}
		

		public int tieBreakBonus() {

			return Math.max(0, base - maxNormal);

		}
	}
	
	final Contestant pc;

	final Contestant opposition;



	public Rivers(int skill, int skillBonus, int resistance, int resistanceBonus) {
		super();
		this.pc = new Contestant(skill, skillBonus);
		this.opposition = new Contestant(resistance, resistanceBonus);

	}

	public Rivers(int skill, int resistance) {
		this(skill, 0, resistance, 0);
	}
	@Override
	public double calcWinRatio() {

		double totalWins = 0;
		int totalsSamples = 0;

		for (int i : d100.allValues()) {

			for (int j : d100.allValues()) {
				totalWins += winRatioGiven(i, j);
				totalsSamples++;
			}
		}

		return totalWins / totalsSamples;

	}
	
	
	public double winRatioGiven(int roll1, int roll2) {
		int c1 = roll1 <= pc.effectiveSkill() ? 1 : 0;
		int c2 = roll2 <= opposition.effectiveSkill()  ? 1 : 0;

		if (c1 > c2) {
			return 1.0;
		}

		if (c1 == c2) {
			return tiebreakRule(roll1, roll2) ? 1.0 : 0.0;
		}
		return 0.0;
		
		
	}
	

	public boolean isWin(int roll1, int roll2) {

		int c1 = roll1 <= pc.effectiveSkill() ? 1 : 0;
		int c2 = roll2 <= opposition.effectiveSkill()  ? 1 : 0;
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
