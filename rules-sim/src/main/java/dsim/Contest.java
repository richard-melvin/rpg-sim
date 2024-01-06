package dsim;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringJoiner;
import java.util.function.BiFunction;

public interface Contest {

	double calcWinRatio();

	static void writeResultsAsCsv(Path p, int tableSize, int tableRes, BiFunction<Integer, Integer, Double> cellFunc) throws IOException {
		
		StringJoiner lj = new StringJoiner(System.lineSeparator());
	
		StringJoiner header = new StringJoiner(",");
		header.add("");
		for (int j = 0; j <= tableSize; j += tableRes) {
			header.add("" + j);
		}
		lj.add(header.toString());

		for (int i = tableRes; i <= tableSize; i += tableRes) {
			StringJoiner joiner = new StringJoiner(",");
			joiner.add("" + i);
			for (int j = 0; j <= tableSize; j += tableRes) {
				joiner.add(Double.toString(cellFunc.apply(i, j)));
			}
			lj.add(joiner.toString());
		}
		
	    Files.write(p, lj.toString().getBytes(StandardCharsets.UTF_8));
	
	}
	
	
	
	default public boolean tieBreak(int roll1, int roll2) {
		return roll1 > roll2;
	}


}