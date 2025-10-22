package seedu.address.model.payment;

import static java.util.Objects.requireNonNull;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;

import seedu.address.model.payment.exceptions.PaymentException;

/**
 * Represents a list of payments.
 * A PaymentList represents the payments one student takes under one tutor.
 * It supports adding, retrieving, deleting, and printing payments.
 */
public class PaymentList {
    private final ArrayList<Payment> payments;
    private YearMonth earliestUnpaidYearmonth;

    private Status status;

    /**
     * Constructs a new payment list by creating an empty array list.
     */
    public PaymentList() {
        this.payments = new ArrayList<>();

        earliestUnpaidYearmonth = null;
        setPaymentStatus(Status.PAID);
    }

    /**
     * Constructs a new payment list by adding a payment.
     * Assumes added payment is unpaid.
     */
    public PaymentList(Payment payment) {
        this.payments = new ArrayList<>();
        this.payments.add(payment);

        updateStatus();
        findAndSetEarliestUnpaidYearMonth();
    }

    /**
     * Constructs a new payment list by adding an arraylist of payments.
     */
    public PaymentList(ArrayList<Payment> payments) {
        requireNonNull(payments);
        this.payments = new ArrayList<>(payments);
        sortByYearMonth();

        updateStatus();
        findAndSetEarliestUnpaidYearMonth();
    }

    public int getSize() {
        return payments.size();
    }

    public boolean isEmpty() {
        return this.getSize() == 0;
    }

    /**
     * Returns payment by index.
     *
     * @param indexOneBased index of payment object, one based.
     * @return a Payment object
     * @throws PaymentException when no payment corresponding to yearmonth found.
     */
    public Payment getPaymentByIndex(int indexOneBased) throws PaymentException {
        int idx = indexOneBased - 1;
        if (idx < 0 || idx >= payments.size()) {
            throw new PaymentException("No such payment: " + indexOneBased);
        }
        return payments.get(idx);
    }

    public ArrayList<Payment> getPayments() {
        return this.payments;
    }

    public Status getStatus() {
        return this.status;
    }

    public YearMonth getEarliestUnpaidYearmonth() {
        return this.earliestUnpaidYearmonth;
    }

    /**
     * Returns payment by month.
     *
     * @param month a YearMonth object
     * @return a Payment object corresponding to YearMonth
     * @throws PaymentException when no payment corresponding to yearmonth found.
     */
    public Payment getPaymentByMonth(YearMonth month) throws PaymentException {
        requireNonNull(month, "Month must not be null.");

        for (Payment p : payments) {
            if (month.equals(p.getYearMonth())) {
                return p;
            }
        }

        throw new PaymentException("No payment found for " + month);
    }

    /**
     * Returns the index of the payment with the given YearMonth, or -1 if absent.
     */
    public int indexOfMonth(YearMonth month) {
        requireNonNull(month, "Month must not be null.");
        for (int i = 0; i < payments.size(); i++) {
            if (month.equals(payments.get(i).getYearMonth())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns true if there exists a payment for the given YearMonth.
     */
    public boolean containsMonth(YearMonth month) {
        return indexOfMonth(month) >= 0;
    }

    /**
     * Adds a new payment to the payment list.
     */
    public void addPayment(Payment payment) {
        requireNonNull(payment);
        payments.add(payment);

        if (status == Status.PAID && !payment.isPaid()) {
            setPaymentStatus(Status.UNPAID);
            setEarliestUnpaidYearmonth(payment.getYearMonth());
        } else if (status == Status.UNPAID && !payment.isPaid()) {
            setPaymentStatus(Status.OVERDUE);
        }
        sortByYearMonth();
        updateStatus();
        setEarliestUnpaidYearmonth(findAndSetEarliestUnpaidYearMonth());
    }

    /**
     * Adds the payment only if there is no existing entry for the same YearMonth.
     * Keeps the list sorted and updates status/earliest unpaid when added.
     *
     * @return true if added, false if a payment for the same YearMonth already exists.
     */
    public boolean addPaymentIfAbsent(Payment payment) {
        requireNonNull(payment);
        if (containsMonth(payment.getYearMonth())) {
            return false;
        }
        payments.add(payment);
        sortByYearMonth();
        updateStatus();
        setEarliestUnpaidYearmonth(findAndSetEarliestUnpaidYearMonth());
        return true;
    }

    /**
     * Inserts or replaces a payment by YearMonth (upsert semantics).
     * If an entry for the same YearMonth exists, it is overwritten.
     *
     * @return the previously stored Payment for that YearMonth, or null if none existed.
     */
    public Payment putPaymentForMonth(Payment payment) {
        requireNonNull(payment);
        YearMonth month = payment.getYearMonth();
        int idx = indexOfMonth(month);
        Payment replaced = null;
        if (idx >= 0) {
            replaced = payments.set(idx, payment);
        } else {
            payments.add(payment);
        }
        sortByYearMonth();
        updateStatus();
        setEarliestUnpaidYearmonth(findAndSetEarliestUnpaidYearMonth());
        return replaced;
    }

    /**
     * Find unpaid payments in a payment list.
     * @return ArrayList<Payment> of unpaid payments</Payment>.
     */
    public ArrayList<Payment> findUnpaids() {
        ArrayList<Payment> unpaidList = new ArrayList<>();
        for (Payment p : payments) {
            if (!p.isPaid()) {
                unpaidList.add(p);
            }
        }

        return unpaidList;
    }

    /**
     * Updates payment status according to the size of unpaid list.
     */
    private void updateStatus() {
        ArrayList<Payment> unpaidList = findUnpaids();
        if (unpaidList.isEmpty()) {
            setPaymentStatus(Status.PAID);
        } else if (unpaidList.size() == 1) {
            setPaymentStatus(Status.UNPAID);
        } else {
            setPaymentStatus(Status.OVERDUE);
        }
    }

    /**
     * Returns the earlier (max YearMonth) unpaid payment's month, or null if none.
     * Manual method.
     */
    public YearMonth findAndSetEarliestUnpaidYearMonth() {
        YearMonth earliest = null;
        for (Payment p : payments) {
            if (!p.isPaid()) {
                YearMonth ym = p.getYearMonth();
                if (earliest == null || ym.isBefore(earliest)) {
                    earliest = ym;
                }
            }
        }
        if (earliest != earliestUnpaidYearmonth) {
            setEarliestUnpaidYearmonth(earliest);
        }
        return earliest;
    }

    /**
     * Updates existing payment corresponding to yearmonth with new totalAmount.
     *
     * @param month the YearMonth corresponding to payment.
     * @param totalAmount float representing the new total amount.
     * @throws PaymentException
     */
    public void updateExistingPayment(YearMonth month, float totalAmount) throws PaymentException {
        try {
            Payment p = getPaymentByMonth(month);
            p.setTotalAmount(totalAmount);
            p.setPaid(false);

            // reset status
            updateStatus();
            findAndSetEarliestUnpaidYearMonth();
        } catch (PaymentException e) {
            throw e;
        }
    }

    /**
     * Mark all outstanding payments as paid by iterating through
     * list of unpaid payments and marking them as paid.
     */
    public void markAllPaid() throws PaymentException {
        ArrayList<Payment> unpaidList = findUnpaids();
        if (unpaidList.isEmpty()) {
            throw new PaymentException("All lessons paid for already.");
        }

        for (Payment p : unpaidList) {
            p.markPaid();
        }

        setPaymentStatus(Status.PAID);
        setEarliestUnpaidYearmonth(null);

    }

    /**
     * Returns a string representation of the payment list, with
     * each lesson prefixed by its index.
     *
     * @return the formatted string representation of the payment list.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < payments.size(); i++) {
            sb.append(i + 1).append(". ").append(payments.get(i)).append("\n");
        }
        return sb.toString();
    }

    private void sortByYearMonth() {
        payments.sort(Comparator.comparing(Payment::getYearMonth));
    }

    private void setPaymentStatus(Status status) {
        this.status = status;
    }

    private void setEarliestUnpaidYearmonth(YearMonth month) {
        this.earliestUnpaidYearmonth = month;
    }

    /**
     * Returns a defensive deep copy of this PaymentList.
     * Each Payment is also copied to prevent external mutation.
     */
    public PaymentList copy() {
        ArrayList<Payment> copiedPayments = new ArrayList<>();
        for (Payment p : payments) {
            copiedPayments.add(new Payment(p));
        }
        return new PaymentList(copiedPayments);
    }
}
