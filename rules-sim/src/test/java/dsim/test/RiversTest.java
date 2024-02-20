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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dsim.Contest;
import dsim.Die;
import dsim.rivers.ExtendedContest;
import dsim.rivers.OngoingContest;
import dsim.rivers.OpposedRoll;
import dsim.rivers.OpposedRoll.Status;

public class RiversTest {

	private static final Logger LOG = LoggerFactory.getLogger(RiversTest.class);

	@Test
	public void isWin() {
		assertThat(new OpposedRoll(50, 50).calcWinRatio()).isCloseTo(0.5, within(0.01));

		assertThat(new OpposedRoll(10, 80).calcWinRatio()).isCloseTo(0.04, within(0.01));

	}

	@RepeatedTest(100)
	public void stableCalcs() {
		Die d100 = new Die(100);

		Contest randomSills = new OpposedRoll(d100.randomValue(), d100.randomValue());

		assertThat(randomSills.calcWinRatio()).isCloseTo(randomSills.calcWinRatio(), within(0.003));
	}

	@RepeatedTest(100)
	public void sameSkillEvens() {
		Die d100 = new Die(300);

		final int skill = d100.randomValue();
		OpposedRoll sammeSkill = new OpposedRoll(skill, skill);

		assertThat(sammeSkill.calcWinRatio()).isCloseTo(0.495, within(0.005));
		
	}
	

	@RepeatedTest(100)
	public void sameSkillExtendedEvens() {
		Die d100 = new Die(300);

		final int skill = d100.randomValue();
		OpposedRoll sammeSkill = new OpposedRoll(skill, skill);		
		ExtendedContest ec = new ExtendedContest(sammeSkill);
		
		assertThat(ec.calcWinRatio()).describedAs("skill" + skill).isCloseTo(0.5, within(0.03));

	}
	
	
	@ParameterizedTest
	@EnumSource(Status.class)
	public void outputResultsTable(Status status) throws IOException {


		int tableSize = 250;
		int tableRes = 5;
		for (int i = tableRes; i <= tableSize; i += tableRes) {
			StringJoiner joiner = new StringJoiner(",");
			for (int j = tableRes; j <= tableSize; j += tableRes) {
				Contest iVersusJ = new OpposedRoll(i, status, j, Status.NONE, true);
				joiner.add(String.format("%.3f", iVersusJ.calcWinRatio()));
			}
			LOG.info("win ratio for skill {} is {}", i, joiner.toString());
		}

	}

	@ParameterizedTest
	@EnumSource(Status.class)
	public void writeResultsFilesDecisive(Status status) throws IOException {

		BiFunction<Integer, Integer, Double> f1 = (i, j) -> {
			Contest iVersusJ = new OpposedRoll(i, status, j, Status.NONE, true);
			return iVersusJ.calcWinRatio();
		};

		Contest.writeResultsAsCsv(Path.of("target", "riversSkillTableDecisive" + status + ".csv"), 250, 10, f1);

	}
	

	@ParameterizedTest
	@EnumSource(Status.class)
	public void writeResultsFilesDecisiveInactive(Status status) throws IOException {

		BiFunction<Integer, Integer, Double> f1 = (i, j) -> {
			Contest iVersusJ = new OpposedRoll(i, status, j, Status.INACTIVE, true);
			return iVersusJ.calcWinRatio();
		};

		Contest.writeResultsAsCsv(Path.of("target", "riversSkillTableDecisiveInactive" + status + ".csv"), 250, 10, f1);

	}



	@ParameterizedTest
	@EnumSource(Status.class)
	public void writeResultsFilesOpposed(Status status) throws IOException {

		BiFunction<Integer, Integer, Double> f1 = (i, j) -> {
			Contest iVersusJ = new OpposedRoll(i, status, j, Status.NONE, false);
			return iVersusJ.calcWinRatio();
		};

		Contest.writeResultsAsCsv(Path.of("target", "riversSkillTableOpposed" + status + ".csv"), 250, 10, f1);

	}

	


	@Test
	public void writeResultsFilesExtended() throws IOException {

		BiFunction<Integer, Integer, Double> f1 = (i, j) -> {
			OpposedRoll iVersusJ = new OpposedRoll(i, Status.NONE, j, Status.NONE, true);
			ExtendedContest ext = new ExtendedContest(iVersusJ);
			return ext.calcWinRatio();
		};

		Contest.writeResultsAsCsv(Path.of("target", "riversSkillTableExtended.csv"), 250, 10, f1);

	}
	


	@Test
	public void writeResultsFilesOngoing() throws IOException {

		BiFunction<Integer, Integer, Double> f1 = (i, j) -> {
			OpposedRoll iVersusJ = new OpposedRoll(i, Status.NONE, j, Status.NONE, true);
			Contest ext = new OngoingContest(iVersusJ);
			return ext.calcWinRatio();
		};

		Contest.writeResultsAsCsv(Path.of("target", "riversSkillTableOngoing.csv"), 250, 10, f1);

	}

}
