package dsim.rivers;

import java.util.PrimitiveIterator.OfInt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dsim.Contest;
import dsim.Die;

public class OngoingContest implements Contest {

	private static final Logger LOG = LoggerFactory.getLogger(OngoingContest.class);
	
	private final OpposedRoll simple;
	
	private final int numSamples = 1000000;
	
	double rolls = 0;

	public OngoingContest(OpposedRoll simple) {
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

		int pcRoll = 0;
		int oppRoll = 0;
		
		for (int i = 0; i < 3 && totalSuccesses <= totalLosses; i++)
		{
			pcRoll = random.nextInt();
			oppRoll = random.nextInt();

			totalSuccesses += simple.pc.countSuccesses(pcRoll);
			totalLosses += simple.opposition.countSuccesses(oppRoll);
			
			rolls += 1;
			
		}
		
		if (totalSuccesses == totalLosses && simple.useTieBreak)
		{
			if (pcRoll > oppRoll)
			{
				totalSuccesses++;
			}
			else if (pcRoll < oppRoll)
			{
				totalLosses++;
			}
		}
		
		return totalSuccesses > totalLosses;
	}
	

	
}
