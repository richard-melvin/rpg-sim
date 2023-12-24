/**
 * 
 */
package dsim.questworlds.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringJoiner;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dsim.Contest;
import dsim.questworlds.SimpleContest;

public class SimpleContestTest {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleContestTest.class);

	private SimpleContest sameSkill = new SimpleContest(10, 10);

	@Test
	public void winRatioForSameSkill() {

		assertThat(sameSkill.calcWinRatio()).isEqualTo(0.475, within(0.001));

	}

	/**
	 * Test method for {@link dsim.questworlds.SimpleContest#countSuccesses(int, int)}.
	 */
	@Test
	public void countSuccesses() {

		assertThat(sameSkill.countSuccesses(1, 10)).isEqualTo(1);
		assertThat(sameSkill.countSuccesses(5, 10)).isEqualTo(1);
		assertThat(sameSkill.countSuccesses(10, 10)).isEqualTo(2);
		assertThat(sameSkill.countSuccesses(15, 10)).isEqualTo(0);
		assertThat(sameSkill.countSuccesses(20, 10)).isEqualTo(0);

		assertThat(sameSkill.countSuccesses(1, 20)).isEqualTo(1);
		assertThat(sameSkill.countSuccesses(5, 20)).isEqualTo(1);
		assertThat(sameSkill.countSuccesses(10, 20)).isEqualTo(1);
		assertThat(sameSkill.countSuccesses(15, 20)).isEqualTo(1);
		assertThat(sameSkill.countSuccesses(20, 20)).isEqualTo(2);

		assertThat(sameSkill.countSuccesses(1, 30)).isEqualTo(2);
		assertThat(sameSkill.countSuccesses(5, 30)).isEqualTo(2);
		assertThat(sameSkill.countSuccesses(10, 30)).isEqualTo(3);
		assertThat(sameSkill.countSuccesses(15, 30)).isEqualTo(1);
		assertThat(sameSkill.countSuccesses(20, 30)).isEqualTo(1);

	}

	/**
	 * Test method for {@link dsim.questworlds.SimpleContest#isWin(int, int)}.
	 */
	@Test
	public void isWin() {

		assertThat(sameSkill.isWin(1, 11)).isTrue();
		assertThat(sameSkill.isWin(10, 11)).isTrue();
		assertThat(sameSkill.isWin(9, 8)).isTrue();

		assertThat(sameSkill.isWin(11, 11)).isFalse();
		assertThat(sameSkill.isWin(11, 1)).isFalse();
		assertThat(sameSkill.isWin(8, 9)).isFalse();
	}

	@Test
	public void outputResultsTable() throws IOException {

		StringJoiner lj = new StringJoiner(System.lineSeparator());

		int tableSize = 50;
		for (int i = 1; i <= tableSize; i++) {
			StringJoiner joiner = new StringJoiner(",");
			for (int j = 1; j <= tableSize; j++) {
				Contest iVersusJ = new SimpleContest(i, j);
				joiner.add(Double.toString(iVersusJ.calcWinRatio()));
			}
			LOG.info("win ratio for skill {} is {}", i, joiner.toString());
			lj.add(joiner.toString());
		}


	}

	@Test
	public void writeResultsFiles() throws IOException {

		BiFunction<Integer, Integer, Double> f1 = (i, j) -> {
			SimpleContest iVersusJ = new SimpleContest(i, j, true);
			return iVersusJ.calcWinRatio();
		};

		Contest.writeResultsAsCsv(Path.of("target", "skillTable.csv"), 50, f1);

		BiFunction<Integer, Integer, Double> f2 = (i, j) -> {
			SimpleContest iVersusJ = new SimpleContest(i, j, false);
			return iVersusJ.calcWinRatio();
		};

		Contest.writeResultsAsCsv(Path.of("target", "skillTableNoTieBreak.csv"), 50, f2);

	}

}
