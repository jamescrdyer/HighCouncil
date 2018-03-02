package highcouncil.domain.enumeration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The Action enumeration.
 */
public enum Action {
    Piety,  Popularity,  Favour,  Military,  Wealth;

    private static final List<Action> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Action randomAction()  {
    	return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
