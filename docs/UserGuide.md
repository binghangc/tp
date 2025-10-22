---
layout: page
title: User Guide
---

Tuiniverse is a **desktop app for private freelance one-on-one tuition tutors to manage the contacts of their students. It keeps track of the student contact details, lesson details, and payment statuses per student and bills outstanding per month.
Tuiniverse is optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, Tuiniverse can get your contact management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a student: `add`

Adds a student to the address book.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A student can have any number of tags (including 0)
</div>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal`

### Adding a lesson: `add.lesson`

Adds a lesson to the specific student.

Format: `add.lesson i/STUDENT_INDEX s/SUBJECT l/LEVEL d/DAY s/START_TIME e/END_TIME r/HOURLY RATE`

Examples:
* `add.lesson i/4 s/Math l/3 d/Tuesday s/13:00 e/15:00 r/40`

### Making payment: `pay`
Tracks that a student has made payment for that month. 

Format: `pay INDEX`

* Marks payment for the student at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* When you mark an **unpaid** or **overdue** student as paid, their status becomes **paid**.
* A **paid** student cannot make payment until the next month.

### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`

### Listing all payments: `list.paid`
Lists all students that have **paid** their fees for the month.

Format: `list.paid`

### Listing all unpaid fees: `list.unpaid`
Lists all students that have **unpaid** fees for the month.

Format: `list.unpaid`

### Listing all overdue fees: `list.overdue` 
Lists all students that have **overdue** fees from previous months.

Format: `list.overdue`

### Editing a person : `edit`

Edits an existing student in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Locating students by name: `find`

Finds students whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* The name, address, email and phone number can be searched.
* Any substring of a word will be matched e.g. `Han` will match `Hans`
* Students matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex 91031282` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Deleting a person : `delete`

Deletes the specified student from the address book.

Format: `delete INDEX`

* Deletes the students at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd student in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st student in the results of the `find` command.

### Deleting a Lesson: `delete.lesson`

Deletes an existing lesson from a specific student.

Format:`delete.lesson i/STUDENT_INDEX c/LESSON_INDEX…​`

Examples:
* `delete.lesson i/2 c/1`

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

Tuiniverse data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

Tuiniverse data are saved automatically as a JSON file `[JAR file location]/data/Tuiniverse.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, Tuiniverse will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the Tuiniverse to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous Tuiniverse home folder.

--------------------------------------------------------------------------------------------------------------------

## Glossary

* **Person**: A person can either be a student or a parent
    * Student - A student studies a subject under a tutor for an hourly rate. 
    * Parent - The parent of the student.
* **Payment Status**: Each student has a payment status which updates every month.
    * Paid - The student has paid within the month
    * Unpaid - The student has not paid within the month
    * Overdue - The student has outstanding bills from previous months
* **Bill**: The payment amount owed by a student
* **Subjects**: Math, English, Physics, Chemistry, Biology, Geography, History, Mother tongue, Social Studies, Literature
* **Note**: A comment located in a student's information
* **Schedule**: A timetable for classes containing the time, location, subject of the class and the student taking the class

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
**Add Lesson** | `add.lesson i/STUDENT_INDEX s/SUBJECT l/LEVEL d/DAY s/START_TIME e/END_TIME r/HOURLY RATE` <br> e.g., `add.lesson i/4 s/Math l/3 d/Tuesday s/13:00 e/15:00 r/40`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Delete Lesson** | `delete.lesson i/STUDENT_INDEX c/LESSON_INDEX…​`<br> e.g.,`delete.lesson i/2 c/1`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List** | `list`
**List Paid** | `list.paid`
**List Unpaid** | `list.unpaid`
**List Overdue** | `list.overdue`
**Pay** | `pay INDEX`<br> e.g., `pay 3`
**Help** | `help`
