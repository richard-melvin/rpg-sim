package dsim;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Die {

	final int dsize;

	public Die(int dsize) {
		this.dsize = dsize;
	}

	public final List<Integer> allValues() {

		return IntStream.range(1, dsize + 1).boxed().collect(Collectors.toList());
	}

	public final IntStream randomValues() {

		return IntStream.generate(() -> ThreadLocalRandom.current().nextInt(1, dsize + 1));
	}
}
