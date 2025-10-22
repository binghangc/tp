package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.testutil.TypicalLessons.Y1_ENGLISH;
import static seedu.address.testutil.TypicalLessons.Y1_PHYSICS;
import static seedu.address.testutil.TypicalLessons.Y3_GEOGRAPHY;
import static seedu.address.testutil.TypicalLessons.Y3_HISTORY;
import static seedu.address.testutil.TypicalLessons.Y3_MATH;
import static seedu.address.testutil.TypicalLessons.Y4_ENGLISH;
import static seedu.address.testutil.TypicalLessons.getTypicalLessons;
import static seedu.address.testutil.TypicalPayments.currentYmUnpaid0;
import static seedu.address.testutil.TypicalPayments.feb25Paid;
import static seedu.address.testutil.TypicalPayments.feb25Unpaid;
import static seedu.address.testutil.TypicalPayments.jan25Paid;
import static seedu.address.testutil.TypicalPayments.sampleArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.lesson.LessonList;
import seedu.address.model.payment.PaymentList;
import seedu.address.model.person.Person;
import seedu.address.model.person.student.Student;

/**
 * A utility class containing a list of {@link Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Student ALICE = new StudentBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253")
            .withLessonList(new LessonList().addLesson(Y3_MATH))
            .withPaymentList(new PaymentList(sampleArrayList())).build();
    public static final Student BENSON = new StudentBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("johnd@example.com").withPhone("98765432")
            .withLessonList(new LessonList().addLesson(Y1_PHYSICS))
            .withPaymentList(new PaymentList(new ArrayList<>(List.of(jan25Paid(), feb25Unpaid())))).build();
    public static final Student CARL = new StudentBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withAddress("wall street")
            .withLessonList(new LessonList().addLesson(Y4_ENGLISH))
            .withPaymentList(new PaymentList(new ArrayList<>(List.of(jan25Paid(), feb25Paid())))).build();
    public static final Student DANIEL = new StudentBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withAddress("10th street")
            .withLessonList(new LessonList())
            .withPaymentList(new PaymentList(new ArrayList<>(List.of(currentYmUnpaid0())))).build();
    public static final Student ELLE = new StudentBuilder().withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@example.com").withAddress("michegan ave")
            .withLessonList(new LessonList().addLesson(Y1_ENGLISH))
            .withPaymentList(new PaymentList(feb25Unpaid())).build();
    public static final Student FIONA = new StudentBuilder().withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@example.com").withAddress("little tokyo")
            .withLessonList(new LessonList().addLesson(Y3_HISTORY))
            .withPaymentList(new PaymentList(sampleArrayList())).build();
    public static final Student GEORGE = new StudentBuilder().withName("George Best").withPhone("9482442")
            .withEmail("anna@example.com").withAddress("4th street")
            .withLessonList(new LessonList().addLesson(Y3_GEOGRAPHY))
            .withPaymentList(new PaymentList()).build();
    public static final Student HANNAH = new StudentBuilder().withName("HANNAH MONTANA").withPhone("94823224")
            .withEmail("mileycyrus@example.com").withAddress("usa ave")
            .withLessonList(new LessonList().addLesson(Y1_ENGLISH))
            .withPaymentList(new PaymentList(jan25Paid())).build();


    // Manually added
    public static final Student HOON = new StudentBuilder().withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com").withAddress("little india").build();
    public static final Student IDA = new StudentBuilder().withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").withAddress("chicago ave").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Student AMY = new StudentBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).build();
    public static final Student BOB = new StudentBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@link AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        ab.setLessons(getTypicalLessons());
        return ab;
    }

    public static List<? extends Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
