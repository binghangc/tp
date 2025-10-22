package seedu.address.model.person.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLessons.Y1_ENGLISH;
import static seedu.address.testutil.TypicalLessons.Y2_CHEMISTRY;
import static seedu.address.testutil.TypicalPayments.feb25Unpaid;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.HANNAH;

import java.time.YearMonth;

import org.junit.jupiter.api.Test;

import seedu.address.model.lesson.LessonList;
import seedu.address.model.payment.Payment;
import seedu.address.model.payment.PaymentList;
import seedu.address.model.payment.Status;
import seedu.address.model.payment.exceptions.PaymentException;
import seedu.address.model.person.Person;
import seedu.address.model.util.DateTimeUtil;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.StudentBuilder;

public class StudentTest {

    @Test
    void constructor_minimalFields_initializesDefaultFields() {
        assertNotNull(BOB.getLessonList());
        assertNotNull(BOB.getPayments());
        assertEquals(Status.UNPAID, BOB.getPaymentListStatus());
        assertEquals(PaymentStatus.UNPAID, BOB.getPaymentStatus());
    }

    @Test
    void constructor_allFields_correctPaymentStatus() {
        // correctly identifies overdue
        assertEquals(Status.OVERDUE, FIONA.getPaymentListStatus());
        assertEquals(PaymentStatus.OVERDUE, FIONA.getPaymentStatus());

        // correctly identifies unpaid
        assertEquals(Status.UNPAID, BENSON.getPaymentListStatus());
        assertEquals(PaymentStatus.UNPAID, BENSON.getPaymentStatus());

        // correctly identifies paid
        assertEquals(Status.PAID, CARL.getPaymentListStatus());
        assertEquals(PaymentStatus.PAID, CARL.getPaymentStatus());

        // initialises payment list for constructor with one payment object and identifies unpaid
        assertNotNull(BOB.getLessonList());
        assertNotNull(BOB.getPayments());
        assertEquals(Status.UNPAID, ELLE.getPaymentListStatus());
        assertEquals(PaymentStatus.UNPAID, ELLE.getPaymentStatus());

    }

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Student student = new StudentBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> student.getTags().remove(0));
    }

    @Test
    public void checkIsStatusSame_whenAlreadySame_noChange() {
        // Sanity before
        assertEquals(Status.UNPAID, ELLE.getPaymentListStatus());
        assertEquals(PaymentStatus.UNPAID, ELLE.getPaymentStatus());

        // Should be a no-op
        ELLE.checkIsStatusSame();

        assertEquals(PaymentStatus.UNPAID, ELLE.getPaymentStatus());
    }

    @Test
    public void checkIsStatusSame_whenStudentDiffers_updatesToPaymentList() {
        // Force mismatch on Paid Student
        HANNAH.setPaymentStatus(PaymentStatus.UNPAID);

        // Sync should update Student to PAID
        HANNAH.checkIsStatusSame();

        assertEquals(PaymentStatus.PAID, HANNAH.getPaymentStatus());
    }

    @Test
    void checkIsStatusSame_afterPaymentListMutates_syncsToNewStatus() throws PaymentException {
        Student elle = new StudentBuilder().withName("Elle Meyer").withPhone("9482224")
                .withEmail("werner@example.com").withAddress("michegan ave")
                .withLessonList(new LessonList().addLesson(Y1_ENGLISH))
                .withPaymentList(new PaymentList(feb25Unpaid())).build();

        // Sanity before
        assertEquals(PaymentStatus.UNPAID, elle.getPaymentStatus());

        // Mutate underlying PaymentList: mark all paid
        elle.getPayments().markAllPaid(); // PaymentList becomes PAID

        // Sync should update Student to PAID
        elle.checkIsStatusSame();
        assertEquals(PaymentStatus.PAID, elle.getPaymentStatus());
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns true
        Person editedAlice = new StudentBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new StudentBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new StudentBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new StudentBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new StudentBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different subclass (Person vs Student) -> returns false
        Person dummyPerson = new PersonBuilder()
                .withName(ALICE.getName().fullName)
                .withPhone(ALICE.getPhone().value)
                .withEmail(ALICE.getEmail().value)
                .build();
        assertFalse(ALICE.equals(dummyPerson));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new StudentBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new StudentBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new StudentBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new StudentBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new StudentBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = ALICE.getClass().getCanonicalName()
                + "{name=" + ALICE.getName()
                + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail()
                + ", address=" + ALICE.getAddress()
                + ", lessons=" + ALICE.getLessonList()
                + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }

    @Test
    public void toDisplayString() {
        Student amy = new StudentBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_FRIEND)
                .build();
        String expectedMessage = "Name: " + VALID_NAME_AMY
                + "; Phone: " + VALID_PHONE_AMY
                + "; Email: " + VALID_EMAIL_AMY
                + "; Address: " + VALID_ADDRESS_AMY
                + "; Lessons: " + amy.getLessonList().toString()
                + "; Tags: [" + VALID_TAG_FRIEND + "]";
        assertEquals(expectedMessage, amy.toDisplayString());

    }

    @Test
    void refreshesPaymentTotal_addAndRemoveLesson_success() throws PaymentException {
        // setup a blank student with empty payments
        Student s = DANIEL;

        PaymentList payments = s.getPayments();
        YearMonth ym = DateTimeUtil.currentYearMonth();

        // before adding any lesson
        Payment before = payments.getPaymentByMonth(ym);
        float beforeAmount = before.getTotalAmountFloat();
        assertEquals(0f, beforeAmount, 1e-6);

        // adding lesson
        // should fire listener -> refreshCurrentMonthPayment()
        s.getLessonList().addLesson(Y2_CHEMISTRY);

        // then payment total should have increased
        Payment after = payments.getPaymentByMonth(ym);
        float afterAmount = after.getTotalAmountFloat();

        assertTrue(afterAmount > beforeAmount,
                "Payment total should increase after adding lesson");

        assertEquals(s.mapStatus(payments.getStatus()), s.getPaymentStatus());

        // removing lesson decreases total
        float beforeRemove = payments
                .getPaymentByMonth(ym)
                .getTotalAmountFloat();

        s.getLessonList().deleteLesson(Y2_CHEMISTRY);

        float afterRemove = s.getPayments()
                .getPaymentByMonth(ym)
                .getTotalAmountFloat();

        assertTrue(afterRemove < beforeRemove,
                "Payment total should decrease after deleting lesson");
    }

}
