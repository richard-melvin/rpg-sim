package dsim.rivers;

import dsim.Contest;
import dsim.Die;

public class Rivers implements Contest {

	final private Die d100 = new Die(100);

	final private Die d20 = new Die(20);

	final int maxNormal = 96;
	final int skill;

	final int resistance;

	public Rivers(int skill, int resistance) {
		super();
		this.skill = skill;
		this.resistance = resistance;
	}

	@Override
	public double calcWinRatio() {

		double totalwins = 0;

		for (int i : d100.allValues()) {

			for (int j : d100.allValues()) {
				if (isWin(i, j)) {
					totalwins++;
				}
			}
		}

		return totalwins / (d100.getSize() * d100.getSize());

	}

	public boolean isWin(int roll1, int roll2) {

		int c1 = roll1 <= effectiveSkill(skill) ? 1 : 0;
		int c2 = roll2 <= effectiveSkill(resistance) ? 1 : 0;

		if (c1 > c2) {
			return true;
		}

		if (c1 == c2) {
			return roll1 + tieBreakBonus(skill) > roll2 + tieBreakBonus(resistance);
		}
		return false;
	}

	@Override
	public String toString() {
		return "Rivers [skill=" + skill + ", resistance=" + resistance + "]";
	}

	public int effectiveSkill(int baseChance) {

		int baseEffective = Math.min(maxNormal, baseChance);

		int excess = baseChance - maxNormal;

		return baseEffective + Math.min(0, excess / 100);

	}

	public int tieBreakBonus(int baseChance) {

		return Math.max(0, baseChance - maxNormal);

	}

}
