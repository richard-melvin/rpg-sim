package dsim;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringJoiner;
import java.util.function.BiFunction;

public interface Contest {

	double calcWinRatio();

	static void writeResultsAsCsv(Path p, int tableSize, BiFunction<Integer, Integer, Double> cellFunc) throws IOException {
		
		StringJoiner lj = new StringJoiner(System.lineSeparator());
	
	
		
		
		for (int i = 1; i <= tableSize; i++) {
			StringJoiner joiner = new StringJoiner(",");
			for (int j = 1; j <= tableSize; j++) {
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