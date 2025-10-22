package seedu.address.testutil;

import java.util.ArrayList;
import java.util.List;

import seedu.address.model.payment.Payment;
import seedu.address.model.util.DateTimeUtil;

/**
 * A utility class containing a list of {@code Payment} factory methods to be used in tests.
 */
public class TypicalPayments {

    private TypicalPayments() {} // prevents instantiation

    /**
     * Creates a Payment object in January 2025 with total amount 600
     * and payment status Paid.
     *
     * @return Payment object
     */
    public static Payment jan25Paid() {
        return new PaymentBuilder()
                .withYearMonth("2025-01")
                .withTotalAmount(600f)
                .withIsPaid(true)
                .build();
    }

    /**
     * Creates a Payment object in February 2025 with total amount 600
     * and payment status Unpaid.
     *
     * @return Payment object
     */
    public static Payment feb25Unpaid() {
        return new PaymentBuilder()
                .withYearMonth("2025-02")
                .withTotalAmount(600f)
                .withIsPaid(false)
                .build();
    }

    /**
     * Creates a Payment object in February 2025 with total amount 600
     * and payment status Paid.
     *
     * @return Payment object
     */
    public static Payment feb25Paid() {
        return new PaymentBuilder()
                .withYearMonth("2025-02")
                .withTotalAmount(600f)
                .withIsPaid(true)
                .build();
    }

    /**
     * Creates a Payment object in March 2025 with total amount 600
     * and payment status Unpaid.
     *
     * @return Payment object
     */
    public static Payment mar25Unpaid() {
        return new PaymentBuilder()
                .withYearMonth("2025-03")
                .withTotalAmount(600f)
                .withIsPaid(false)
                .build();
    }

    /**
     * Creates a Payment object in September 2025 with total amount 600
     * and payment status Unpaid.
     *
     * @return Payment object
     */
    public static Payment sep25Unpaid() {
        return new PaymentBuilder()
                .withYearMonth("2025-09")
                .withTotalAmount(600f)
                .withIsPaid(false)
                .build();
    }

    /**
     * Creates a Payment object in January 2024 with total amount 600
     * and payment status Paid.
     *
     * @return Payment object
     */
    public static Payment jan24Paid600() {
        return new PaymentBuilder()
                .withYearMonth("2024-01")
                .withTotalAmount(600f)
                .withIsPaid(true)
                .build();
    }

    /**
     * Creates a Payment object in January 2025 with total amount 800
     * and payment status Unpaid.
     *
     * @return Payment object
     */
    public static Payment jan24Unpaid800() {
        return new PaymentBuilder()
                .withYearMonth("2024-01")
                .withTotalAmount(800f)
                .withIsPaid(false)
                .build();
    }

    /**
     * Creates a Payment object of current year month with total amount 800
     * and payment status Unpaid.
     *
     * @return Payment object
     */
    public static Payment currentYmUnpaid0() {
        return new PaymentBuilder()
                .withYearMonth(DateTimeUtil.currentYearMonth().toString())
                .withTotalAmount(0f)
                .withIsPaid(false)
                .build();
    }

    /**
     * Creates an ArrayList of Payment objects.
     * These objects were chosen to represent a typical chronology of the month,
     * and represents an overdue payment list.
     *
     * @return an ArrayList of Payment objects.
     */
    public static ArrayList<Payment> sampleArrayList() {
        return new ArrayList<>(List.of(jan25Paid(), feb25Unpaid(), mar25Unpaid()));
    }
}
