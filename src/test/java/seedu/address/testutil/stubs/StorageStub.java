package seedu.address.testutil.stubs;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.storage.Storage;

/**
 * A base Storage stub that throws AssertionError for all methods.
 */
public class StorageStub implements Storage {

    @Override
    public Path getUserPrefsFilePath() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Path getAddressBookFilePath() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataLoadingException {
        throw new AssertionError("This method should not be called.");
    }


    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
        throw new AssertionError("This method should not be called.");
    }
}
