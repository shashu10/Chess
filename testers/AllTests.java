package testers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs all tests for classes in the <code>contactInfo</code> package
 * 
 * @author Jerome Bell
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ BoardTests.class, GameTests.class, PieceTests.class,
		PlayerTests.class, PositionTests.class })
public class AllTests {
}
