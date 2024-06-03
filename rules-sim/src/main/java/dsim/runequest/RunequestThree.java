package dsim.runequest;

/**
 * RQ3 rules, at least as per 
 * https://www.maranci.net/basicrq97.htm
 */
public class RunequestThree extends Runequest {

	public RunequestThree(int skill, int resistance) {
		super(skill, resistance);
	}

	
	/**
	 * RQ3 does nto treat skills over 100% specially.
	 */
	@Override
	public int overHundredRule(int baseChance, int opposition) {
		return baseChance;
	}
	
	
	

}
