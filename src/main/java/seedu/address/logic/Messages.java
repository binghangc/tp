package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {
    public static final String MESSAGE_INVALID_DISPLAYED_PERSON = "Invalid Person displayed";
    public static final String MESSAGE_INVALID_DISPLAYED_LESSON = "The lesson index provided is invalid";
    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
            "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        return person.toDisplayString();
    }

    /**
     * Formats the {@code lesson} for display to the user.
     */
    public static String formatLesson(Lesson lesson) {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Subject: ")
                .append(lesson.getSubject())
                .append("; Secondary: ")
                .append(lesson.getLevel())
                .append("; Day: ")
                .append(lesson.getDay())
                .append("; Start: ")
                .append(lesson.getStartTime())
                .append("; End: ")
                .append(lesson.getEndTime())
                .append("; Address: ")
                .append(lesson.getAddress())
                .append("; Rate: $")
                .append(lesson.getRate());
        return builder.toString();
    }

}
