package seedu.address.model.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPayments.feb25Paid;
import static seedu.address.testutil.TypicalPayments.feb25Unpaid;
import static seedu.address.testutil.TypicalPayments.jan25Paid;
import static seedu.address.testutil.TypicalPayments.mar25Unpaid;
import static seedu.address.testutil.TypicalPayments.sampleArrayList;
import static seedu.address.testutil.TypicalPayments.sep25Unpaid;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.payment.exceptions.PaymentException;
import seedu.address.testutil.PaymentBuilder;

public class PaymentListTest {

    /*
    Testing the payment constructor
     */
    @Test
    public void constructor_noArguments_success() {
        PaymentList pl = new PaymentList();
        assertEquals(0, pl.getSize());
        assertTrue(pl.isEmpty());
        assertNull(pl.findAndSetEarliestUnpaidYearMonth());
    }

    @Test
    public void constructor_oneValidUnpaidLessonArgument_success() {
        PaymentList pl = new PaymentList(new PaymentBuilder()
                .withYearMonth("2025-03").withTotalAmount(600f)
                .withIsPaid(false).build());
        assertEquals(1, pl.getSize());
        assertNotNull(pl.findAndSetEarliestUnpaidYearMonth());
        assertEquals(Status.UNPAID, pl.getStatus());
    }

    @Test
    public void constructor_oneValidPaidLessonArgument_success() {
        PaymentList pl = new PaymentList(jan25Paid());
        assertEquals(1, pl.getSize());
        assertNull(pl.getEarliestUnpaidYearmonth());
        assertEquals(Status.PAID, pl.getStatus());
    }

    @Test
    public void constructor_nullLesson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PaymentList((Payment) null));
    }

    @Test
    public void constructor_validUnpaidLessonList_success() {
        ArrayList<Payment> al = new ArrayList<>(List.of(jan25Paid(), feb25Unpaid()));
        PaymentList pl = new PaymentList(al);
        assertEquals(2, pl.getSize());
        assertEquals(YearMonth.of(2025, 2), pl.getEarliestUnpaidYearmonth());
        assertEquals(Status.UNPAID, pl.getStatus());
    }

    @Test
    public void constructor_validOverdueLessonList_success() {
        PaymentList pl = new PaymentList(sampleArrayList());
        assertEquals(3, pl.getSize());
        assertEquals(YearMonth.of(2025, 2), pl.getEarliestUnpaidYearmonth());
        assertEquals(Status.OVERDUE, pl.getStatus());
    }

    @Test
    public void getPaymentByIndex_validIndex_success() throws PaymentException {
        PaymentList pl = new PaymentList(sampleArrayList());
        System.out.println("Payments size: " + pl.getSize());
        System.out.println("Payments list: " + pl);
        assertEquals(jan25Paid(), pl.getPaymentByIndex(1));
    }

    @Test
    public void getPaymentByMonth_validIndex_success() throws PaymentException {
        PaymentList pl = new PaymentList(sampleArrayList());
        assertEquals(jan25Paid(), pl.getPaymentByMonth(YearMonth.of(2025, 1)));
    }

    @Test
    public void addPayment_validPayment_success() {
        PaymentList pl = new PaymentList(sampleArrayList());
        pl.addPayment(sep25Unpaid());
        assertEquals(4, pl.getSize());
        assertEquals(YearMonth.of(2025, 2), pl.getEarliestUnpaidYearmonth());
        assertEquals(Status.OVERDUE, pl.getStatus());
    }

    @Test
    public void addPayment_validPayment_successAndStatusChangesFromUnpaidToOverdue() {
        ArrayList<Payment> al = new ArrayList<>(List.of(jan25Paid(), feb25Unpaid()));
        PaymentList pl = new PaymentList(al);
        pl.addPayment(mar25Unpaid());
        assertEquals(3, pl.getSize());
        assertEquals(YearMonth.of(2025, 2), pl.getEarliestUnpaidYearmonth());
        assertEquals(Status.OVERDUE, pl.getStatus());
    }

    @Test
    public void addPayment_validPayment_successAndStatusChangesFromPaidToUnpaid() {
        ArrayList<Payment> al = new ArrayList<>(List.of(jan25Paid(), feb25Paid()));
        PaymentList pl = new PaymentList(al);
        pl.addPayment(mar25Unpaid());
        assertEquals(3, pl.getSize());
        assertEquals(YearMonth.of(2025, 3), pl.getEarliestUnpaidYearmonth());
        assertEquals(Status.UNPAID, pl.getStatus());
    }

    @Test
    public void findUnpaids_returnNull() {
        ArrayList<Payment> al = new ArrayList<>(List.of(jan25Paid(), feb25Paid()));
        PaymentList pl = new PaymentList(al);
        ArrayList<Payment> unpaids = pl.findUnpaids();
        assertEquals(0, unpaids.size());
        assertEquals(Status.PAID, pl.getStatus());
    }

    @Test
    public void findUnpaids_returnArrayList() {
        PaymentList pl = new PaymentList(sampleArrayList());
        ArrayList<Payment> unpaids = pl.findUnpaids();

        ArrayList<Payment> expectedUnpaids = new ArrayList<>(List.of(feb25Unpaid(), mar25Unpaid()));
        assertEquals(2, unpaids.size());
        assertEquals(expectedUnpaids, unpaids);
        assertEquals(Status.OVERDUE, pl.getStatus());
    }

    @Test
    public void updateExistingAmount_amountUpdatedAndMarkedUnpaid() throws Exception {
        YearMonth ym = YearMonth.of(2025, 10);
        Payment p = new PaymentBuilder().withYearMonth(ym.toString()).withTotalAmount(0f).build();
        PaymentList pl = new PaymentList(p);

        pl.updateExistingPayment(ym, 2600.00f);

        Payment pp = pl.getPaymentByMonth(ym);
        assertEquals(2600.00f, pp.getTotalAmountFloat(), 1e-4, "totalAmount should update");
        assertFalse(p.isPaid(), "payment must be marked unpaid after updateExistingPayment");
    }

    @Test
    public void updateExistingAmount_recomputesAggregateStatus() throws Exception {
        YearMonth ym = YearMonth.of(2025, 10);
        Payment p = new PaymentBuilder().withYearMonth(ym.toString()).withTotalAmount(50f).build();
        p.setPaid(true);
        PaymentList pl = new PaymentList(p);

        assertEquals(Status.PAID, pl.getStatus(), "precondition: list status starts as PAID");

        pl.updateExistingPayment(ym, 100f);

        assertEquals(Status.UNPAID, pl.getStatus(),
                "after making target month unpaid, aggregate status should be UNPAID");
    }

    @Test
    public void updateExistingAmount_doesNotAffectOtherMonths() throws Exception {
        YearMonth oct = YearMonth.of(2025, 10);
        YearMonth nov = YearMonth.of(2025, 11);

        Payment pOct = new PaymentBuilder().withYearMonth(oct.toString()).withTotalAmount(500f).build();
        Payment pNov = new PaymentBuilder().withYearMonth(nov.toString()).withTotalAmount(750f).build();

        PaymentList pl = new PaymentList(new ArrayList<>(List.of(pOct, pNov)));

        pl.updateExistingPayment(oct, 600f);

        Payment paymentOct = pl.getPaymentByMonth(oct);
        Payment paymentNov = pl.getPaymentByMonth(nov);

        assertEquals(600f, paymentOct.getTotalAmountFloat(), 1e-4);
        assertFalse(pOct.isPaid(), "Target month should be set to unpaid");

        assertEquals(750f, paymentNov.getTotalAmountFloat(), 1e-4,
                "Other months' amounts must not change");
    }

    @Test
    public void updateExistingAmount_invalidMonth_throwsWhenMonthMissing() {
        YearMonth ym = YearMonth.of(2025, 10);
        YearMonth nov = YearMonth.of(2025, 11);
        Payment p = new PaymentBuilder().withYearMonth(nov.toString()).withTotalAmount(500f).build();
        PaymentList pl = new PaymentList(p);

        assertThrows(PaymentException.class, () ->
                        pl.updateExistingPayment(ym, 123f),
                "should throw when target month is absent");
    }

    @Test
    public void markAllPaid_success() throws PaymentException {
        ArrayList<Payment> al = new ArrayList<>(List.of(jan25Paid(), feb25Unpaid(), mar25Unpaid()));
        PaymentList pl = new PaymentList(al);
        pl.markAllPaid();

        assertEquals(Status.PAID, pl.getStatus());
        assertNull(pl.getEarliestUnpaidYearmonth());
    }

    @Test
    public void markAllPaid_allPaid_throwsPaymentException() throws PaymentException {
        ArrayList<Payment> al = new ArrayList<>(List.of(jan25Paid(), feb25Paid()));
        PaymentList pl = new PaymentList(al);

        assertThrows(PaymentException.class, () -> pl.markAllPaid());
    }

    /*
    Testing toString
     */
    @Test
    void toString_numberedAndIncludesPaymentStrings() throws PaymentException {
        PaymentList pl = new PaymentList(sampleArrayList());

        String s = pl.toString();
        String[] lines = s.split("\\R");

        assertTrue(lines[0].startsWith("1. "));
        assertTrue(lines[1].startsWith("2. "));
        assertTrue(lines[2].startsWith("3. "));

        assertTrue(lines[0].contains("2025-01"));
        assertTrue(lines[1].contains("2025-02"));
        assertTrue(lines[2].contains("2025-03"));
    }

    @Test
    void toString_numberedAndIncludesPaymentStringsAndSorts() throws PaymentException {
        ArrayList<Payment> al = new ArrayList<>(List.of(mar25Unpaid(), jan25Paid(), feb25Unpaid()));
        PaymentList pl = new PaymentList(al);

        String s = pl.toString();
        String[] lines = s.split("\\R");

        assertTrue(lines[0].startsWith("1. "));
        assertTrue(lines[1].startsWith("2. "));
        assertTrue(lines[2].startsWith("3. "));

        assertTrue(lines[0].contains("2025-01"));
        assertTrue(lines[1].contains("2025-02"));
        assertTrue(lines[2].contains("2025-03"));
    }

}
