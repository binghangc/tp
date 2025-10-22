package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.student.Student;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        if (person instanceof Student) {
            Student tempStudent = (Student) person;
            return keywords.stream().anyMatch(keyword ->
                    StringUtil.containsWordIgnoreCase(tempStudent.getName().fullName, keyword)
                    || StringUtil.containsWordIgnoreCase(tempStudent.getEmail().toString(), keyword)
                    || StringUtil.containsWordIgnoreCase(tempStudent.getPhone().toString(), keyword)
                    || StringUtil.containsWordIgnoreCase(tempStudent.getAddress().toString(), keyword));
        }
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword)
                        || StringUtil.containsWordIgnoreCase(person.getEmail().toString(), keyword)
                        || StringUtil.containsWordIgnoreCase(person.getPhone().toString(), keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate)) {
            return false;
        }

        NameContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (NameContainsKeywordsPredicate) other;
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
