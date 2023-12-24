package dsim.questworlds;

import java.io.IOException;
import java.nio.file.Path;
import java.util.PrimitiveIterator.OfInt;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dsim.Contest;
import dsim.Die;

public class ExtendedContest implements Contest {

	private static final Logger LOG = LoggerFactory.getLogger(ExtendedContest.class);
	
	private final SimpleContest simple;
	
	private final int numSamples = 1000000;
	
	private final int winTarget = 5;
	
	private final int lossTarget = 5;

	public ExtendedContest(SimpleContest simple) {
		super();
		this.simple = simple;
	}

	@Override
	public double calcWinRatio() {
		
		double numWins = 0.0;
		
		for (int i = 0; i < numSamples; i++)  {
			OfInt random = new Die(simple.dsize).randomValues().iterator();
			LOG.debug("checking for sample {} of {}", i, simple);
			if (isRandomWin(random)) {
				numWins++;
			}
		}
				
		return numWins/numSamples;
	}

	
	public boolean isRandomWin(OfInt random) {
		

		int rolls = 0;
		int totalSuccesses = 0;
		int totalLosses = 0;

		while (totalSuccesses < winTarget && totalLosses < lossTarget)
		{
			int netscore = simple.netSuccesses(random.nextInt(), random.nextInt());
			rolls += 2;
			
			if (netscore > 0)
			{
				totalSuccesses += netscore;
			}
			else
			{
				totalLosses -= netscore;

			}

			LOG.debug(" got {}/{} after {} rolls", totalSuccesses, totalLosses, rolls);
		}
		
		
		return totalSuccesses > totalLosses;
	}
	

	
}
