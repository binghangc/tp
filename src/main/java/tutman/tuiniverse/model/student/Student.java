package tutman.tuiniverse.model.student;

import static java.util.Objects.requireNonNull;
import static tutman.tuiniverse.commons.util.CollectionUtil.requireAllNonNull;

import java.time.YearMonth;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javafx.collections.ListChangeListener;
import tutman.tuiniverse.commons.util.ToStringBuilder;
import tutman.tuiniverse.model.lesson.Lesson;
import tutman.tuiniverse.model.lesson.LessonList;
import tutman.tuiniverse.model.payment.PaymentList;
import tutman.tuiniverse.model.payment.Status;
import tutman.tuiniverse.model.payment.TotalAmount;
import tutman.tuiniverse.model.payment.UnpaidAmount;
import tutman.tuiniverse.model.payment.exceptions.PaymentException;
import tutman.tuiniverse.model.student.exceptions.PaymentStatusUpdateException;
import tutman.tuiniverse.model.student.tag.Tag;
import tutman.tuiniverse.model.util.DateTimeUtil;
import tutman.tuiniverse.model.util.mapping.StatusMapper;

/**
 * Represents a Person in the address book.
 * A Person stores basic identity details such as name, phone number, and email address.
 * Subclasses (e.g., Student, Parent) may include additional fields specific to their roles.
 */
public class Student {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final LessonList lessons;
    private final PaymentList payments;

    private PaymentStatus paymentStatus;
    private boolean autoRefreshEnabled = true;

    /**
     * Every field must be present and not null.
     */
    public Student(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.lessons = new LessonList();
        this.payments = new PaymentList();
        this.paymentStatus = PaymentStatus.PAID;
        wireLessonListeners();
    }

    /**
     * Every field must be present and not null.
     */
    public Student(Name name, Phone phone, Email email, Address address, Set<Tag> tags, LessonList ll, PaymentList pl) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.lessons = new LessonList(ll.getLessons());
        this.payments = pl;
        this.paymentStatus = mapStatus(getPaymentListStatus());
        wireLessonListeners();
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
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

    public boolean hasLesson(Lesson lesson) {
        return this.getLessonList().hasLesson(lesson);
    }

    /**
     * Returns a mutable TreeMap of Payments.
     */
    public PaymentList getPayments() {
        return this.payments;
    }

    /**
     * Replaces the student's current PaymentList with a new one.
     * This should be used when restoring or carrying over payment data during edits.
     */
    public void setPayments(PaymentList newPayments) {
        requireNonNull(newPayments);
        this.payments.getPayments().clear();
        this.payments.getPayments().addAll(newPayments.getPayments());
        setPaymentStatus(mapStatus(newPayments.getStatus()));
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

    public float getTotalAmountFloat() {
        return lessons.getTotalAmountEarned(DateTimeUtil.currentYearMonth());
    }

    /**
     * Returns the total amount unpaid by the student.
     *
     * @return a UnpaidAmount object with amount equivalent to the total of unpaid payments.
     */
    public UnpaidAmount getAmountDue() {
        return payments.calculateUnpaidAmount();
    }

    public TotalAmount getTotalAmountByMonth(YearMonth yearMonth) {
        float f = lessons.getTotalAmountEarned(yearMonth);
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
     * Checks if this person is equal to another object.
     * Two persons are considered equal if they have the same name, phone, email, address, and tags.
     *
     * @param other the object to compare with.
     * @return true if both objects represent the same person with identical identity fields.
     */
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // might need to change
        if (other == null || !(other instanceof Student)) {
            return false;
        }

        Student that = (Student) other;
        return name.equals(that.name)
                && phone.equals(that.phone)
                && email.equals(that.email)
                && address.equals(that.address)
                && tags.equals(that.tags);
    }

    /**
     * Returns true if both person have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Student otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName())
                && otherPerson.getEmail().equals(getEmail())
                && otherPerson.getPhone().equals(getPhone());
    }

    public int hashCode() {
        return Objects.hash(name, phone, email, address, tags);
    }


    /**
     * Returns a user-facing string for display.
     * Subclasses may override this method to append their own fields.
     */
    public String toDisplayString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Name: ").append(name)
                .append("; Phone: ").append(phone)
                .append("; Email: ").append(email)
                .append("; Address: ").append(address)
                .append("; Lessons: ").append(this.getLessonList().toString())
                .append("; Tags: ");
        this.getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Returns a string representation of this {@code Person} for debugging purposes.
     * The string includes the person's {@code Name}, {@code Phone}, and {@code Email}, and is mainly intended
     * for logging or internal inspection rather than user display.
     *
     * @return a string representation of this person with their core identity fields.
     */
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
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
            if (!autoRefreshEnabled) {
                return;
            }
            while (change.next()) {
                if (hasStructuralChange(change)) {
                    refreshCurrentMonthPayment();
                    break;
                }
            }
        });
    }

    private boolean hasStructuralChange(ListChangeListener.Change<?> change) {
        return change.wasAdded() || change.wasRemoved() || change.wasUpdated() || change.wasReplaced();
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

    /**
     * Disables automatic payment refreshes triggered by changes in the {@code LessonList}.
     */
    public void disableAutoPaymentRefresh() {
        this.autoRefreshEnabled = false;
    }

    /**
     * Re-enables automatic payment refreshes triggered by changes in the {@code LessonList}.
     */
    public void enableAutoPaymentRefresh() {
        this.autoRefreshEnabled = true;
    }
}

