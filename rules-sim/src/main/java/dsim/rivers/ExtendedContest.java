package dsim.rivers;

import java.util.PrimitiveIterator.OfInt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dsim.Contest;
import dsim.Die;

public class ExtendedContest implements Contest {

	private static final Logger LOG = LoggerFactory.getLogger(ExtendedContest.class);
	
	private final OpposedRoll simple;
	
	private final int numSamples = 1000000;
	
	private final int winTarget = 5;
	
	private final int lossTarget = 5;
	double rolls = 0;

	public ExtendedContest(OpposedRoll simple) {
		super();
		this.simple = simple;
	}

	@Override
	public double calcWinRatio() {
		
		double numWins = 0.0;
		OfInt random = new Die(OpposedRoll.d100.getSize()).randomValues().iterator();
		rolls = 0;
		for (int i = 0; i < numSamples; i++)  {
			// LOG.debug("checking for sample {} of {}", i, simple);
			if (isRandomWin(random)) {
				numWins++;
			}
		}
		LOG.debug(" got {} for {}/{} after average {} rolls", numWins/numSamples, simple.pc.base(), simple.opposition.base(),
				rolls/numSamples);
				
		return numWins/numSamples;
	}

	
	public boolean isRandomWin(OfInt random) {
		

		int totalSuccesses = 0;
		int totalLosses = 0;

		while ((totalSuccesses < winTarget && totalLosses < lossTarget) || totalSuccesses == totalLosses)
		{
			final int pcRoll = random.nextInt();
			final int oppRoll = random.nextInt();

			int pcScore = simple.pc.countSuccesses(pcRoll);
			int oppScore = simple.opposition.countSuccesses(oppRoll);

			if (pcScore == oppScore && simple.useTieBreak)
			{
				if (pcRoll > oppRoll)
				{
					pcScore++;
				}
				else if (pcRoll < oppRoll)
				{
					oppScore++;
				}
			}
			totalSuccesses += pcScore;
			totalLosses += oppScore;
			
			rolls += 1;

		}
		
		
		return totalSuccesses > totalLosses;
	}
	

	
}
