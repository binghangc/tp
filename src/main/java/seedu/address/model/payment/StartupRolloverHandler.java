package seedu.address.model.payment;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.time.YearMonth;
import java.util.logging.Logger;

import seedu.address.commons.util.StringUtil;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.util.DateTimeUtil;
import seedu.address.storage.Storage;


/**
 * Handles monthly rollover and first-time initialization logic during app startup.
 * <p>
 * This class is responsible for:
 * <ul>
 *   <li>Detecting first-time launches and initializing {@code lastOpened} in {@link UserPrefs}.</li>
 *   <li>Running {@link MonthlyRollover} to generate new payment records for months since the last launch.</li>
 *   <li>Safely persisting {@link seedu.address.model.AddressBook} and {@link UserPrefs} updates to storage.</li>
 * </ul>
 */
public class StartupRolloverHandler {
    private static final Logger logger = Logger.getLogger(StartupRolloverHandler.class.getName());
    private final Model model;
    private final Storage storage;

    /**
     * Constructs a {@code StartupRolloverHandler} with the given model and storage.
     *
     * @param model the application's model containing the address book and payment data
     * @param storage the storage interface for saving address book and user preferences
     */
    public StartupRolloverHandler(Model model, Storage storage) {
        requireNonNull(model);
        requireNonNull(storage);
        this.model = model;
        this.storage = storage;
    }

    /**
     * Performs the monthly rollover process during application startup.
     * <p>
     * This method checks the {@code lastOpened} month stored in {@code UserPrefs} and compares it to the current month.
     * If {@code lastOpened} is null, it initializes it to the current month and saves the preferences.
     * If the application has already been opened this month, it skips rollover.
     * Otherwise, it runs the monthly rollover for all missing months between {@code lastOpened} and now,
     * updates {@code lastOpened} to the current month, and persists all changes.
     *
     * @param userPrefs the user preferences containing the last opened month to update and persist
     */
    public void perform(UserPrefs userPrefs) {
        YearMonth now = DateTimeUtil.currentYearMonth();
        YearMonth lastOpened = userPrefs.getLastOpened();

        if (lastOpened == null) {
            logger.info("[Startup] First-time launch detected - initializing lastOpened to " + now);
            userPrefs.setLastOpened(now);
            saveUserPrefs(userPrefs, "first-time initialization");
            return;
        }

        if (!lastOpened.isBefore(now)) {
            logger.info("[Startup] Skipping rollover - app already opened this month (" + lastOpened + ")");
            return;
        }

        // Do rollover for missing months
        new MonthlyRollover(model).compute(lastOpened, now);

        // Save changes
        saveAddressBook("after rollover");
        userPrefs.setLastOpened(now);
        saveUserPrefs(userPrefs, "after rollover");
    }

    /**
     * Saves the current address book state to storage.
     *
     * @param context a descriptive context string for logging purposes
     */
    private void saveAddressBook(String context) {
        try {
            storage.saveAddressBook(model.getAddressBook());
        } catch (IOException e) {
            logger.warning("[Startup] Failed to save AddressBook " + context + ": " + StringUtil.getDetails(e));
        }
    }

    /**
     * Saves the given user preferences to storage.
     *
     * @param prefs the user preferences to save
     * @param context a descriptive context string for logging purposes
     */
    private void saveUserPrefs(UserPrefs prefs, String context) {
        try {
            storage.saveUserPrefs(prefs);
        } catch (IOException e) {
            logger.warning("[Startup] Failed to save UserPrefs " + context + ": " + StringUtil.getDetails(e));
        }
    }
}
