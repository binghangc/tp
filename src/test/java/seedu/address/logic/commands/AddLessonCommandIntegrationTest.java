package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.student.Student;
import seedu.address.testutil.LessonBuilder;
import seedu.address.testutil.StudentBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddLessonCommandIntegrationTest {
    private Model model;
    private Lesson newLesson;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        newLesson = new LessonBuilder()
                .withSubject("Math")
                .withLevel("4")
                .withDay("6")
                .withLessonTime("12:00", "14:00")
                .build();
    }

    @Test
    public void execute_newLesson_success() {
        // Assume the first person in list is a Student
        Student student = (Student) model.getFilteredPersonList().get(0);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Student expectedStudent = new StudentBuilder(student).build();
        expectedModel.setPerson(student, expectedStudent);

        AddLessonCommand addLessonCommand = new AddLessonCommand(Index.fromZeroBased(0), newLesson);
        newLesson.addStudent(expectedStudent);
        String expectedMessage = String.format(AddLessonCommand.MESSAGE_SUCCESS, Messages.formatLesson(newLesson));
        assertCommandSuccess(addLessonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateLesson_throwsCommandException() {
        Student student = (Student) model.getFilteredPersonList().get(0);
        Lesson lesson = student.getLessonList().getLessons().get(0);
        AddLessonCommand addLessonCommand = new AddLessonCommand(Index.fromZeroBased(0), lesson);
        assertCommandFailure(addLessonCommand, model, AddLessonCommand.MESSAGE_DUPLICATE_LESSON);
    }

    @Test
    public void execute_invalidStudentIndex_throwsCommandException() {
        // Index greater than the typicalPersons list size
        Index invalidIndex = Index.fromZeroBased(7);

        AddLessonCommand addLessonCommand = new AddLessonCommand(invalidIndex, newLesson);

        assertCommandFailure(addLessonCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
}
