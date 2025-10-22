package seedu.address.model.lesson;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.student.Address;
import seedu.address.model.person.student.Student;

/**
 * Represents a Lesson.
 * Does not guarantee: details are present and not null, field values are validated, immutable.
 * Fields student and address may be null, can only be fixed when the student class has been created.
 */
public class Lesson {
    private Student student;
    private Subject subject;
    private Level level;
    private Day day;
    private LessonTime lessonTime;
    private Rate rate;
    private Address address;

    /**
     * Every field must be present and not null.
     */
    public Lesson(Subject subject, Level level, Day day, LessonTime lessonTime, Rate rate) {
        requireAllNonNull(subject, level, day, lessonTime, rate);
        this.subject = subject;
        this.level = level;
        this.day = day;
        this.lessonTime = lessonTime;
        this.rate = rate;
    }

    public Subject getSubject() {
        return subject;
    }

    public Level getLevel() {
        return level;
    }

    public Day getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return lessonTime.getStart();
    }

    public LocalTime getEndTime() {
        return lessonTime.getEnd();
    }

    public LessonTime getLessonTime() {
        return lessonTime;
    }

    public Rate getRate() {
        return rate;
    }

    public Address getAddress() {
        return address;
    }

    public Duration getDuration() {
        return lessonTime.getDuration();
    }

    /**
     * Returns the Duration of the class in hours
     * @return a long representing number of hours
     */
    public long getDurationLong() {
        return lessonTime.getDurationLong();
    }

    public Student getStudent() {
        return student;
    }

    /**
     * Returns the amount earned in $ from one lesson per week.
     * This is the product of the hourly rate and the duration of the class in hours.
     *
     * @return product of the hourly rate and the duration of the class in hours as a float
     */
    public float getAmountEarned() {
        return getDurationLong() * rate.getRate();
    }

    /**
     * Adds a student to the student field and adds the address of the student in the address field
     * @param student The student to be added.
     */
    public void addStudent(Student student) {
        requireAllNonNull(student);
        this.student = student;
        this.address = student.getAddress();
    }

    /**
     * Returns true if both lessons have the same day and their lesson times clash.
     * This defines a stronger notion of equality between two lessons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Lesson)) {
            return false;
        }

        Lesson otherLesson = (Lesson) other;

        return day.getDayOfWeek().equals(otherLesson.day.getDayOfWeek())
                && lessonTime.hasTimeClash(otherLesson.lessonTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, subject, level, day, lessonTime, rate, address);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("subject", subject)
                .add("level", level)
                .add("day", day)
                .add("time", lessonTime)
                .add("rate", rate)
                .add("address", address)
                .toString();
    }

}
