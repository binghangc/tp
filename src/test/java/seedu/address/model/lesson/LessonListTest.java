package seedu.address.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalLessons.Y1_ENGLISH;
import static seedu.address.testutil.TypicalLessons.Y1_PHYSICS;
import static seedu.address.testutil.TypicalLessons.Y3_GEOGRAPHY;
import static seedu.address.testutil.TypicalLessons.Y3_HISTORY;
import static seedu.address.testutil.TypicalLessons.Y3_MATH;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.collections.ListChangeListener;
import seedu.address.model.lesson.exceptions.LessonException;
import seedu.address.model.util.DateTimeUtil;
import seedu.address.testutil.LessonBuilder;

public class LessonListTest {

    @Test
    @DisplayName("Default constructor creates empty observable list")
    public void defaultConstructor_createsEmptyObservableList() {
        LessonList list = new LessonList();

        assertNotNull(list, "LessonList instance should not be null");
        assertEquals(0, list.getSize(), "New list should be empty");

        // Should be able to add a lesson without exceptions
        list.addLesson(Y1_PHYSICS);
        assertEquals(1, list.getSize(), "LessonList should support mutation after construction");
    }

    @Test
    @DisplayName("Copy constructor copies elements but not reference to source collection")
    public void copyConstructor_emptyConstructor_copiesContentsSafely() {
        ArrayList<Lesson> base = new ArrayList<>();
        base.add(Y3_MATH);

        LessonList list = new LessonList(base);

        assertEquals(1, list.getSize(), "Copied list should contain same number of lessons as source");

        // Modify source -> should not affect copied LessonList
        base.add(Y3_GEOGRAPHY);
        assertEquals(1, list.getSize(), "Internal list should not share reference with input collection");
    }

    @Test
    @DisplayName("Copy constructor builds observable list (listener fires)")
    public void copyConstructor_nonEmptyConstructor_observableBehavior() {
        ArrayList<Lesson> base = new ArrayList<>(List.of(Y3_MATH));
        LessonList list = new LessonList(base);

        boolean[] fired = { false };
        list.addListener(change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    fired[0] = true;
                    break;
                }
            }
        });

        list.addLesson(Y3_GEOGRAPHY);
        assertTrue(fired[0], "Listener should fire on add after copy construction");
    }

    @Test
    public void addRemoveListener_changeListener_stopsFiringAfterRemoval() {
        LessonList list = new LessonList();
        final boolean[] fired = { false };

        ListChangeListener<Lesson> listener = change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    fired[0] = true;
                    break;
                }
            }
        };

        list.addListener(listener);
        list.addLesson(Y3_HISTORY);
        assertTrue(fired[0]);

        fired[0] = false;
        list.removeListener(listener);
        list.addLesson(Y3_MATH);
        assertFalse(fired[0], "Listener should not fire after removal");
    }

    @Test
    public void getLesson_index_successAndBounds() throws Exception {
        LessonList list = new LessonList();
        Lesson a = Y1_ENGLISH;
        list.addLesson(a);

        // success at 1
        assertSame(a, list.getLesson(1));

        // out-of-bounds
        assertThrows(LessonException.class, () -> list.getLesson(0));
        assertThrows(LessonException.class, () -> list.getLesson(2));
    }

    @Test
    @DisplayName("addLesson(parts) adds exactly one lesson")
    public void addLesson_addLessonByParts_addsOneLesson() throws LessonException {
        LessonList list = new LessonList();
        assertEquals(0, list.getSize());

        list.addLesson(Y1_ENGLISH);

        assertEquals(1, list.getSize());

        Lesson got = list.getLesson(1);
        assertTrue(list.hasLesson(got));
    }

    @Test
    @DisplayName("addLesson(Lesson) is idempotent: no duplicates when equal()")
    public void addLesson_addLessonAsWhole_noDuplicateEqualLessons() {
        LessonList list = new LessonList();
        Lesson a = Y3_MATH;

        list.addLesson(a);
        list.addLesson(a); // same object
        assertEquals(1, list.getSize(), "Should not add duplicate same-instance");

        // equal() but different instance
        Lesson a2 = new LessonBuilder().withSubject("Math").withLevel("3").withDay("3")
                .withLessonTime("09:00", "11:00").build();
        list.addLesson(a2);
        assertEquals(1, list.getSize(), "Should not add duplicate equal() instance");
    }

    @Test
    @DisplayName("addLesson adds distinct lessons")
    public void addLesson_distinctLessons_added() {
        LessonList list = new LessonList();

        list.addLesson(Y3_MATH);
        list.addLesson(Y3_HISTORY);
        assertEquals(2, list.getSize());
        assertTrue(list.hasLesson(Y3_MATH));
        assertTrue(list.hasLesson(Y3_HISTORY));
    }


    @Test
    @DisplayName("deleteLesson removes when present")
    public void deleteLesson_lessonInList_removesExistingLesson() {
        LessonList list = new LessonList();

        list.addLesson(Y3_GEOGRAPHY);
        assertEquals(1, list.getSize());

        list.deleteLesson(Y3_GEOGRAPHY);
        assertEquals(0, list.getSize());
        assertFalse(list.hasLesson(Y3_GEOGRAPHY));
    }

    @Test
    public void deleteLesson_lessonNotInList_noOperation() {
        LessonList list = new LessonList();

        list.addLesson(Y3_HISTORY);
        assertEquals(1, list.getSize());

        list.deleteLesson(Y3_GEOGRAPHY);
        assertEquals(1, list.getSize(), "Deleting non-existent lesson should not change size");
        assertTrue(list.hasLesson(Y3_HISTORY));
    }

    @Test
    @DisplayName("hasLesson returns false for empty list")
    public void hasLesson_emptyList_false() {
        LessonList list = new LessonList();
        assertFalse(list.hasLesson(Y1_ENGLISH));
    }

    @Test
    @DisplayName("hasLesson returns true when exact lesson present")
    public void hasLesson_lessonPresent_true() {
        LessonList list = new LessonList();
        list.addLesson(Y3_MATH);
        assertTrue(list.hasLesson(Y3_MATH));
    }

    @Test
    @DisplayName("hasLesson returns true for equal() but different instance")
    public void hasLesson_equalInstance_true() {
        LessonList list = new LessonList();
        Lesson a1 = Y3_MATH;
        Lesson a2 = new LessonBuilder().withSubject("Math").withLevel("3").withDay("3")
                .withLessonTime("09:00", "11:00").build();
        list.addLesson(a1);
        assertTrue(list.hasLesson(a2));
    }

    @Test
    @DisplayName("hasLesson returns false for distinct lesson")
    public void hasLesson_distinctSubjects_false() {
        LessonList list = new LessonList();
        list.addLesson(Y3_GEOGRAPHY);
        assertFalse(list.hasLesson(Y3_HISTORY));
    }

    @Test
    @DisplayName("getTotalAmountEarned = 0 for no lessons")
    public void getTotalAmount_noLessons_zero() {
        LessonList list = new LessonList();
        float total = list.getTotalAmountEarned(YearMonth.of(2025, 10));
        assertEquals(0f, total, 1e-6);
    }

    @Test
    @DisplayName("getTotalAmountEarned for single lesson matches daysInMonth * perLessonAmount")
    public void getTotalAmount_singleLesson_correct() {
        LessonList list = new LessonList();
        list.addLesson(Y1_PHYSICS);

        YearMonth ym = YearMonth.of(2025, 10);
        int days = DateTimeUtil.countDaysOfWeekInMonth(ym, Y1_PHYSICS.getDay());
        float expected = days * Y1_PHYSICS.getAmountEarned(); // use model’s own per-lesson amount
        assertEquals(expected, list.getTotalAmountEarned(ym), 1e-5);
    }

    @Test
    @DisplayName("getTotalAmountEarned sums across multiple lessons on different days/rates")
    public void getTotalAmount_multipleLessons_sumOfAmounts() {
        LessonList list = new LessonList();

        Lesson mon = Y1_PHYSICS;
        Lesson wed = Y3_MATH;
        list.addLesson(mon);
        list.addLesson(wed);

        YearMonth ym = YearMonth.of(2025, 10);

        int monCount = DateTimeUtil.countDaysOfWeekInMonth(ym, mon.getDay());
        int wedCount = DateTimeUtil.countDaysOfWeekInMonth(ym, wed.getDay());

        float expected = monCount * mon.getAmountEarned()
                + wedCount * wed.getAmountEarned();

        assertEquals(expected, list.getTotalAmountEarned(ym), 1e-5);
    }
}
