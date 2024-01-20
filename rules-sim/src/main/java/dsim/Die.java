package dsim;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Die {

	final int size;
	private static ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

	public Die(int dsize) {
		this.size = dsize;
	}

	public final List<Integer> allValues() {

		return IntStream.range(1, size + 1).boxed().collect(Collectors.toList());
	}

	public final IntStream randomValues() {

		return IntStream.generate(this::randomValue);
	}

	public final int randomValue()
	{
		return threadLocalRandom.nextInt(1, size + 1);
	}
	
	public int getSize() {
		return size;
	}
	
	
	
}
