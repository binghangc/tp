package seedu.address.model.payment;

import org.junit.jupiter.api.Test;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.testutil.stubs.ModelStub;

import static seedu.address.testutil.Assert.assertThrows;



public class StartupRolloverHandlerTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new StartupRolloverHandler(null, null));
    }

    private static class EmptyModel extends ModelStub {
        private final AddressBook addressBook = new AddressBook();

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return addressBook;
        }
    }


}
