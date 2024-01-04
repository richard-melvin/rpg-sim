/**
 * 
 */
package dsim.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.StringJoiner;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dsim.Contest;
import dsim.runequest.Runequest;

public class RuneQuestTest {

	private static final Logger LOG = LoggerFactory.getLogger(RuneQuestTest.class);




	/**
	 * Test method for {@link dsim.questworlds.SimpleContest#countSuccesses(int, int)}.
	 */
	@Test
	public void countSuccesses() {
	    Runequest contest = new Runequest(50, 50);

		assertThat(contest.countSuccesses(1, 10)).isEqualTo(3);
		assertThat(contest.countSuccesses(2, 10)).isEqualTo(2);
		assertThat(contest.countSuccesses(3, 10)).isEqualTo(1);
		assertThat(contest.countSuccesses(5, 10)).isEqualTo(1);
		assertThat(contest.countSuccesses(10, 10)).isEqualTo(1);
		assertThat(contest.countSuccesses(15, 10)).isEqualTo(0);
		assertThat(contest.countSuccesses(20, 10)).isEqualTo(0);
		assertThat(contest.countSuccesses(95, 10)).isEqualTo(0);
		assertThat(contest.countSuccesses(96, 10)).isEqualTo(-1);
		assertThat(contest.countSuccesses(100, 10)).isEqualTo(-1);

		assertThat(contest.countSuccesses(1, 20)).isEqualTo(3);
		assertThat(contest.countSuccesses(2, 20)).isEqualTo(2);
		assertThat(contest.countSuccesses(3, 20)).isEqualTo(2);
		assertThat(contest.countSuccesses(5, 20)).isEqualTo(1);
		assertThat(contest.countSuccesses(10, 20)).isEqualTo(1);
		assertThat(contest.countSuccesses(15, 20)).isEqualTo(1);
		assertThat(contest.countSuccesses(20, 20)).isEqualTo(1);
		assertThat(contest.countSuccesses(95, 20)).isEqualTo(0);
		assertThat(contest.countSuccesses(96, 20)).isEqualTo(0);
		assertThat(contest.countSuccesses(100, 10)).isEqualTo(-1);
		
		
		assertThat(contest.countSuccesses(99, 69)).isEqualTo(-1);
		assertThat(contest.countSuccesses(99, 70)).isEqualTo(-1);
		assertThat(contest.countSuccesses(99, 71)).isEqualTo(0);

		
	}


	@Test
	public void isWin() {
		Runequest contest = new Runequest(50, 50);

		assertThat(contest.isWin(1, 11)).isTrue();
		assertThat(contest.isWin(10, 11)).isTrue();
		assertThat(contest.isWin(9, 8)).isFalse();

		assertThat(contest.isWin(11, 11)).isFalse();
		assertThat(contest.isWin(11, 1)).isFalse();
		assertThat(contest.isWin(8, 9)).isFalse();
	}

	@Test
	public void skillOverOneHundred() {

		Runequest contest = new Runequest(150, 50);
		assertThat(contest.isWin(1, 1)).isFalse();
		assertThat(contest.isWin(3, 2)).isTrue();
		assertThat(contest.isWin(5, 5)).isTrue();
		assertThat(contest.isWin(6, 6)).isTrue();
		
		assertThat(contest.isWin(95, 2)).isFalse();
		assertThat(contest.isWin(95, 6)).isTrue();
		assertThat(contest.isWin(96, 6)).isFalse();

		assertThat(contest.isWin(99, 2)).isFalse();

		assertThat(contest.isWin(100, 100)).isFalse();

	}
	

	@Test
	public void skillSlightlyOverOneHundred() {

		Runequest contest = new Runequest(120, 80);
		assertThat(contest.isWin(1, 1)).isFalse();
		assertThat(contest.isWin(3, 2)).isFalse();
		assertThat(contest.isWin(3, 3)).isFalse();
		assertThat(contest.isWin(4, 3)).isFalse();
		assertThat(contest.isWin(4, 4)).isTrue();

		assertThat(contest.isWin(5, 5)).isTrue();
		assertThat(contest.isWin(6, 6)).isFalse();
		
		assertThat(contest.isWin(95, 2)).isFalse();
		assertThat(contest.isWin(95, 6)).isFalse();
		assertThat(contest.isWin(95, 60)).isFalse();
		assertThat(contest.isWin(95, 61)).isTrue();

		assertThat(contest.isWin(96, 60)).isFalse();

		assertThat(contest.isWin(99, 2)).isFalse();

		assertThat(contest.isWin(100, 100)).isFalse();

	}
	

	@Test
	public void resitanceSlightlyOverOneHundred() {

		Runequest contest = new Runequest(80, 130);
		assertThat(contest.isWin(1, 1)).isFalse();

		assertThat(contest.isWin(50, 95)).isFalse();
		assertThat(contest.isWin(50, 96)).isTrue();
		assertThat(contest.isWin(51, 96)).isFalse();

		assertThat(contest.isWin(100, 100)).isFalse();

	}
	
	
	@Test
	public void outputResultsTable() throws IOException {

		StringJoiner lj = new StringJoiner(System.lineSeparator());

		int tableSize = 250;
		int tableRes = 5;
		for (int i = tableRes; i <= tableSize; i += tableRes) {
			StringJoiner joiner = new StringJoiner(",");
			for (int j = tableRes; j <= tableSize; j += tableRes) {
				Contest iVersusJ = new Runequest(i, j);
				joiner.add(Double.toString(iVersusJ.calcWinRatio()));
			}
			LOG.info("win ratio for skill {} is {}", i, joiner.toString());
			lj.add(joiner.toString());
		}


	}

	@Test
	public void writeResultsFiles() throws IOException {

		BiFunction<Integer, Integer, Double> f1 = (i, j) -> {
			Contest iVersusJ = new Runequest(i, j);
			return iVersusJ.calcWinRatio();
		};

		Contest.writeResultsAsCsv(Path.of("target", "rqSkillTable.csv"), 250, 10, f1);



	}

}
