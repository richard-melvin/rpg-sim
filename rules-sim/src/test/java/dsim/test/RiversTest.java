/**
 * 
 */
package dsim.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.io.IOException;
import java.nio.file.Path;
import java.util.StringJoiner;
import java.util.function.BiFunction;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dsim.Contest;
import dsim.Die;
import dsim.rivers.Rivers;
import dsim.rivers.Rivers.Status;

public class RiversTest {

	private static final Logger LOG = LoggerFactory.getLogger(RiversTest.class);




	@Test
	public void isWin() {
		assertThat(new Rivers(50, 50).calcWinRatio()).isCloseTo(0.5, within(0.01));

		
		assertThat(new Rivers(10, 80).calcWinRatio()).isCloseTo(0.25, within(0.01));

		
		
	}


	@RepeatedTest(100)
	public void stableCalcs()
	{
		Die d100 = new Die(100);
		
		Contest randomSills = new Rivers(d100.randomValue(), d100.randomValue());

		assertThat(randomSills.calcWinRatio()).isCloseTo(randomSills.calcWinRatio(), within(0.003));
	}
	
	
	@Test
	public void outputResultsTable() throws IOException {

		StringJoiner lj = new StringJoiner(System.lineSeparator());

		int tableSize = 250;
		int tableRes = 5;
		for (int i = tableRes; i <= tableSize; i += tableRes) {
			StringJoiner joiner = new StringJoiner(",");
			for (int j = tableRes; j <= tableSize; j += tableRes) {
				Contest iVersusJ = new Rivers(i, j);
				joiner.add(String.format("%.3f", iVersusJ.calcWinRatio()));
			}
			LOG.info("win ratio for skill {} is {}", i, joiner.toString());
			lj.add(joiner.toString());
		}


	}

	@Test
	public void writeResultsFiles() throws IOException {

		for (Status status : Rivers.Status.values())
		{
			
			BiFunction<Integer, Integer, Double> f1 = (i, j) -> {
				Contest iVersusJ = new Rivers(i, status, j, Status.NONE);
				return iVersusJ.calcWinRatio();
			};
			
			Contest.writeResultsAsCsv(Path.of("target", "riversSkillTable" + status + ".csv"), 250, 10, f1);
		}
		



	}

}
