package dsim.runequest;

import dsim.Contest;
import dsim.Die;

public class Runequest implements Contest {

	final int dsize = 100;

	final int skill;

	final int resistance;
	
	
	public Runequest(int skill, int resistance) {
		super();
		this.skill = skill;
		this.resistance = resistance;
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
		
		return c1 > c2;
	}
	
	
	public int overHundredRule(int baseChance, int opposition)
	{
		int highest = Math.max(baseChance, opposition);
		int lowest = Math.min(baseChance, opposition);

		if (highest <= 100)
		{
			return baseChance;
		}
		
		int delta = Math.clamp(highest - 100, 0, lowest);
		

		return baseChance - delta;
		
	}


	
	/**
	 * Count successes.
	 * 
	 * see RQ:G p143.
	 *
	 * @param roll the roll
	 * @param skill the skill
	 * @return number in range [-1, 3]
	 */
	public int countSuccesses(int roll, int skill) {
		
		assert roll <= dsize;
		assert roll > 0;
		
		int successes = 0;
		
		// normal success
		if (roll <= Math.clamp(skill, 5, 95))
		{
			successes++;
		}
		// special success
		if (roll <= Math.round(skill/5.0))
		{
			successes++;
		}
		// critical
		long critThreshold = Math.max(Math.round(skill/20.0), 1);
		if (roll <= critThreshold)
		{
			successes++;
		}
		
		// fumble
		long fumbleThreshold = Math.max(Math.round((dsize - skill)/20.0), 1);
		if (roll >= 101 - fumbleThreshold)
		{
			successes--;
		}
		
		return successes;
	}


	@Override
	public String toString() {
		return "RQ:G [skill=" + skill + ", resistance=" + resistance + "]";
	}
	
	
}
