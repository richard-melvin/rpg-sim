/**
 * 
 */
package dsim.questworlds.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import dsim.Contest;
import dsim.questworlds.ExtendedContest;
import dsim.questworlds.SimpleContest;


@Disabled
public class ExtendedContestTest {

	
	private ExtendedContest sameSkill = new ExtendedContest(new SimpleContest(10, 10));

	
	
	@RepeatedTest(5)
	public void winRatioForSameSkill() {

		assertThat(sameSkill.calcWinRatio()).isEqualTo(0.5, within(0.01));

	}

	
	
	
	@Test
	public void writeResultsFilestb() throws IOException {

		BiFunction<Integer, Integer, Double> f1 = (i, j) -> {
			SimpleContest iVersusJ = new SimpleContest(i, j, true);
			return new ExtendedContest(iVersusJ).calcWinRatio();
		};

		Contest.writeResultsAsCsv(Path.of("target", "extendedSkillTable.csv"), 50, 1, f1);


	}
	
	
	
	
	@Test
	public void writeResultsFilesnotb() throws IOException {


		BiFunction<Integer, Integer, Double> f2 = (i, j) -> {
			SimpleContest iVersusJ = new SimpleContest(i, j, false);
			return new ExtendedContest(iVersusJ).calcWinRatio();
		};

		Contest.writeResultsAsCsv(Path.of("target", "extendedSkillTableNoTieBreak.csv"), 50, 1, f2);

	}
	
}
