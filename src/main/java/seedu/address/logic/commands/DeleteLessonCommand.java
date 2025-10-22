package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_INDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.LessonList;
import seedu.address.model.lesson.exceptions.LessonException;
import seedu.address.model.person.Person;
import seedu.address.model.person.student.Student;

/**
 * Represents a command to delete a lesson from a student's lesson list.
 * This command deletes the lesson identified by the index number
 * used in the displayed lesson list for the student identified by the index number
 * used in the displayed student list.
 * Usage example:
 *     delete.lesson s/1 l/2
 */
public class DeleteLessonCommand extends Command {

    public static final String COMMAND_WORD = "delete.lesson";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the lesson identified by the index number used in the displayed lesson list"
            + " for the student identified by the index number used in the displayed student list.\n"
            + "Parameters: STUDENT_INDEX (must be a positive integer)"
            + "LESSON_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_STUDENT_INDEX + " 1 "
            + PREFIX_LESSON_INDEX + "2";

    public static final String MESSAGE_DELETE_LESSON_SUCCESS = "Deleted Lesson: %1$s";
    public static final String MESSAGE_INVALID_DISPLAYED = "The lesson index provided is invalid";
    private final Index studentIndex;
    private final Index lessonIndex;

    /**
     * Constructs a DeleteLessonCommand with given student and lesson indexes.
     *
     * @param sI Index of the student in the displayed list.
     * @param lI Index of the lesson in the student's lesson list.
     */
    public DeleteLessonCommand(Index sI, Index lI) {
        this.studentIndex = sI;
        this.lessonIndex = lI;
    }

    /**
     * Executes the DeleteLessonCommand.
     * Attempts to delete the lesson from the specified student's lesson list.
     *
     * @param model the {@link Model} which the command should operate on
     * @return the result of command execution
     * @throws CommandException if the provided indexes are invalid,
     *         or if the lesson deletion fails
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> studentList = model.getFilteredPersonList();
        Student currStudent;

        if (studentIndex.getZeroBased() >= studentList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person student = studentList.get(studentIndex.getZeroBased());

        if (student instanceof Student) {
            currStudent = (Student) student;
        } else {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        LessonList lls = currStudent.getLessonList();

        if (lessonIndex.getOneBased() > lls.getSize()) {
            throw new CommandException(MESSAGE_INVALID_DISPLAYED);
        }

        try {
            Lesson deleteLesson = lls.getLesson(lessonIndex.getOneBased());
            model.deleteLesson(currStudent, deleteLesson);
            return new CommandResult(String.format(MESSAGE_DELETE_LESSON_SUCCESS, Messages.formatLesson(deleteLesson)));
        } catch (LessonException e) {
            throw new CommandException(MESSAGE_INVALID_DISPLAYED);
        }

    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteLessonCommand)) {
            return false;
        }

        DeleteLessonCommand otherDeleteLessonCommand = (DeleteLessonCommand) other;
        return studentIndex.equals(otherDeleteLessonCommand.studentIndex)
                && lessonIndex.equals(otherDeleteLessonCommand.lessonIndex);
    }


}
