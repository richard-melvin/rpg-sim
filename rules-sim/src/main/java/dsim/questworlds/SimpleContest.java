package dsim.questworlds;

import dsim.Contest;
import dsim.Die;

public class SimpleContest implements Contest {

	final int dsize = 20;

	final int skill;

	final int resistance;
	
	final boolean applyTieBreakRule;
	

	public SimpleContest(int skill, int resistance) {
		this(skill, resistance, true);
	}
	
	
	public SimpleContest(int skill, int resistance, boolean applyTieBreakRule) {
		super();
		this.skill = skill;
		this.resistance = resistance;
		this.applyTieBreakRule = applyTieBreakRule;
	}


	@Override
	public double calcWinRatio() {

		double totalwins = 0;
		Die d1 = new Die(dsize);
		Die d2 = new Die(dsize);
				
		for (int i : d1.allValues()) {

			for (int j : d2.allValues()) {
				if (isWin(i, j)) {
					totalwins++;
				}
			}
		}
		
		return totalwins / (dsize * dsize);
		
	}
	

	public boolean isWin(int roll1, int roll2) {

		int c1 = countSuccesses(roll1, skill);
		int c2 = countSuccesses(roll2, resistance);
		
		return c1 > c2 || (c1 == c2 && applyTieBreakRule && tieBreak(roll1, roll2));
	}
	
	public int netSuccesses(int roll1, int roll2) {

		int c1 = countSuccesses(roll1, skill);
		int c2 = countSuccesses(roll2, resistance);
		
		if (c1 > c2) {
			return c1 - c2 + 1; 
		}
		if (c1 < c2) {
			return c1 - c2 - 1; 
		}
		
		if (applyTieBreakRule && roll1 != roll2) {
			return  tieBreak(roll1, roll2) ? 1 : -1;
		}
			
		return 0;
	}
	

	public int countSuccesses(int roll, int skill) {
		
		assert roll <= dsize;
		assert roll > 0;
		
		int target = skill == dsize ? dsize : skill % dsize;
		
		int masteries = skill == dsize ? 0 : skill / dsize;
		
		if (roll == target) {
			return 2 + masteries;
		}
		
		if (roll < target) {
			return 1 + masteries;
		}
		
		return masteries;
	}


	@Override
	public String toString() {
		return "SimpleContest [skill=" + skill + ", resistance=" + resistance + ", applyTieBreakRule="
				+ applyTieBreakRule + "]";
	}
	
	
}
