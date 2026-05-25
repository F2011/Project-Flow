package dhbw.swe.entities;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dhbw.swe.valueobjects.Money;
import org.junit.jupiter.api.Test;

class MoneyTest {

    // For our use case, money must never be negative!
    @Test
    void subtract_largerFromSmaller_throwsIllegalArgumentException() {
        Money ten = Money.euro(10);
        Money twenty = Money.euro(20);

        assertThrows(IllegalArgumentException.class, () -> ten.subtract(twenty));
    }

    @Test
    void subtract_equal_amounts_yields_zero() {
        Money result = Money.euro(10).subtract(Money.euro(10));
        assertTrue(result.compareTo(Money.euro(0)) == 0);
    }

    @Test
    void subtract_smallerFromLarger_yieldsPositiveResult() {
        Money result = Money.euro(20).subtract(Money.euro(10));
        assertTrue(result.compareTo(Money.euro(10)) == 0);
    }
}
