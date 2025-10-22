package seedu.address.model.person.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.YearMonth;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.lesson.LessonList;
import seedu.address.model.payment.Payment;
import seedu.address.model.payment.PaymentList;
import seedu.address.model.payment.Status;
import seedu.address.model.payment.TotalAmount;
import seedu.address.model.payment.exceptions.PaymentException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.exceptions.PaymentStatusUpdateException;
import seedu.address.model.person.student.tag.Tag;
import seedu.address.model.util.DateTimeUtil;
import seedu.address.model.util.mapping.StatusMapper;

/**
 * Represents a Student in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Student extends Person {

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final LessonList lessons;
    private final PaymentList payments;

    private PaymentStatus paymentStatus;

    /**
     * Every field must be present and not null.
     */
    public Student(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email);
        requireAllNonNull(address, tags);
        this.address = address;
        this.tags.addAll(tags);
        this.lessons = new LessonList();
        this.payments = new PaymentList(new Payment(DateTimeUtil.currentYearMonth(), getTotalAmount()));
        this.paymentStatus = PaymentStatus.UNPAID;
        wireLessonListeners();
    }

    /**
     * Every field must be present and not null.
     */
    public Student(Name name, Phone phone, Email email, Address address, Set<Tag> tags, LessonList ll, PaymentList pl) {
        super(name, phone, email);
        requireAllNonNull(address, tags);
        this.address = address;
        this.tags.addAll(tags);
        this.lessons = new LessonList(ll.getLessons());
        this.payments = pl;
        this.paymentStatus = mapStatus(getPaymentListStatus());
        wireLessonListeners();
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an mutable ArrayList of Lessons.
     */
    public LessonList getLessonList() {
        return this.lessons;
    }

    /**
     * Returns a mutable ArrayList of Payments.
     */
    public PaymentList getPayments() {
        return this.payments;
    }

    /**
     * Returns the payment status of the student for the month
     */
    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    /**
     * Returns the status of the student from the payment list the month
     */
    public Status getPaymentListStatus() {
        return getPayments().getStatus();
    }

    /**
     * Checks whether payment list status corresponds to student status
     * A defensive check to ensure no difference in status.
     */
    public void checkIsStatusSame() {
        PaymentStatus plStatus = mapStatus(getPaymentListStatus());
        if (getPaymentStatus() != plStatus) {
            setPaymentStatus(plStatus);
        }
    }

    public TotalAmount getTotalAmount() {
        float f = lessons.getTotalAmountEarned(DateTimeUtil.currentYearMonth());
        return new TotalAmount(f);
    }

    /**
     * Changes payment status to {@link PaymentStatus}
     *
     * Changes to paid when pay command is used.
     * Changes to unpaid when a new month has started.
     * Changes to overdue when a new month has started and previous month is still unpaid.
     *
     * @param paymentStatus paid, unpaid, or overdue.
     */
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        requireNonNull(paymentStatus);
        this.paymentStatus = paymentStatus;
    }

    /**
     * Marks all the payments in the student's PaymentList as paid.
     *
     * @throws PaymentException
     */
    public void pay() throws PaymentException {
        payments.markAllPaid();
        setPaymentStatus(PaymentStatus.PAID);
    }

    public PaymentStatus mapStatus(Status status) {
        return StatusMapper.toPaymentStatus(status);
    }

    /**
     * Returns true if both students have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // compare Person identity
        if (!super.equals(other)) {
            return false;
        }

        // instanceof handles nulls
        if (!(other instanceof Student)) {
            return false;
        }

        Student otherStudent = (Student) other;
        return address.equals(otherStudent.address)
                && tags.equals(otherStudent.tags);
    }


    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(super.hashCode(), address, tags);
    }

    @Override
    public String toDisplayString() {
        final StringBuilder builder = new StringBuilder(super.toDisplayString());
        builder.append("; Address: ").append(address)
                .append("; Lessons: ").append(this.getLessonList().toString())
                .append("; Tags: ");
        this.getTags().forEach(builder::append);
        return builder.toString();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", getName())
                .add("phone", getPhone())
                .add("email", getEmail())
                .add("address", address)
                .add("lessons", lessons)
                .add("tags", tags)
                .toString();
    }

    /*
    Private methods
     */

    private void wireLessonListeners() {
        // Recompute whenever lessons change (add/remove/edit).
        this.lessons.addListener(change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved() || change.wasUpdated() || change.wasReplaced()) {
                    refreshCurrentMonthPayment();
                    break;
                }
            }
        });
    }

    /**
     * Recomputes the current month’s total from lessons and syncs PaymentList + Student status.
     **/
    private void refreshCurrentMonthPayment() throws PaymentStatusUpdateException {
        try {
            YearMonth ym = DateTimeUtil.currentYearMonth();
            float newTotal = lessons.getTotalAmountEarned(ym);
            payments.updateExistingPayment(ym, newTotal);

            System.out.println("New payment updated: new total: " + newTotal);

            setPaymentStatus(mapStatus(getPaymentListStatus()));
        } catch (PaymentException e) {
            throw new PaymentStatusUpdateException();
        }
    }

}
