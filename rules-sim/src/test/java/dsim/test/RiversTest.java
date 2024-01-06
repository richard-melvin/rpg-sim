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

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dsim.Contest;
import dsim.rivers.Rivers;

public class RiversTest {

	private static final Logger LOG = LoggerFactory.getLogger(RiversTest.class);




	@Test
	public void isWin() {
		assertThat(new Rivers(50, 50).calcWinRatio()).isCloseTo(0.5, within(0.01));

		
		assertThat(new Rivers(10, 80).calcWinRatio()).isCloseTo(0.25, within(0.01));

		
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
				joiner.add(Double.toString(iVersusJ.calcWinRatio()));
			}
			LOG.info("win ratio for skill {} is {}", i, joiner.toString());
			lj.add(joiner.toString());
		}


	}

	@Test
	public void writeResultsFiles() throws IOException {

		BiFunction<Integer, Integer, Double> f1 = (i, j) -> {
			Contest iVersusJ = new Rivers(i, j);
			return iVersusJ.calcWinRatio();
		};

		Contest.writeResultsAsCsv(Path.of("target", "riversSkillTable.csv"), 250, 10, f1);



	}

}
