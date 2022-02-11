
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)     // if this is added we can remove static keyword from
class ContactManagerTest {                          // @BeforeALl and @AfterAll methods (public static void)

    ContactManager contactManager;

    @BeforeAll
    public void setupAll() {     // must be static,otherwise Junit won't be able to execute it
        System.out.println("Should print before all tests");
    }

    @BeforeEach
    public void setup(){
        contactManager = new ContactManager();
    }

    @Test
    public void shouldCreateContact() {
        contactManager.addContact("Jane", "Robinson", "0123456789");
        Assertions.assertFalse(contactManager.getAllContacts().isEmpty());
        Assertions.assertEquals(1, contactManager.getAllContacts().size());
        Assertions.assertTrue(contactManager.getAllContacts().stream()
        .filter(contact -> contact.getFirstName().equals("Jane") &&
                contact.getLastName().equals("Robinson") &&
                contact.getPhoneNumber().equals("0123456789"))
        .findAny()
        .isPresent());
    }


    // testing exceptions using assertThrows()
    @Test
    @DisplayName("Should not create contact when first name is null")
    public void shouldThrowRuntimeExceptionWhenFirstNameIsNull(){
//        ContactManager contactManager = new ContactManager();
        Assertions.assertThrows(RuntimeException.class, () -> {
            contactManager.addContact(null, "Robinson", "0123456789");
        });
    }

    @Test
    @DisplayName("Should not create contact when last name is null")
    public void shouldThrowRuntimeExceptionWhenLastNameIsNull(){
//        ContactManager contactManager = new ContactManager();
        Assertions.assertThrows(RuntimeException.class, () -> {
            contactManager.addContact("John", null, "0123456789");
        });
    }

    @Test
    @DisplayName("Should not create contact when last name is null")
    public void shouldThrowRuntimeExceptionWhenPhoneNumberIsNull(){
//        ContactManager contactManager = new ContactManager();
        Assertions.assertThrows(RuntimeException.class, () -> {
            contactManager.addContact("John", "Jason", null);
        });
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Should execute after each test");
    }

    @AfterAll
    public void tearDownAll() {
        System.out.println("Should be executed at the end of the test");
    }


    // CONDITIONAL EXECUTIONS

    // running test only for mac
    @Test
    @DisplayName("Should create contact only on Mac OS")
    @EnabledOnOs(value = OS.MAC, disabledReason = "Enabled only on Mac OS")
    public void shouldCreateContactOnMacOs() {
        contactManager.addContact("Jane", "Robinson", "0123456789");
        Assertions.assertFalse(contactManager.getAllContacts().isEmpty());
        Assertions.assertEquals(1, contactManager.getAllContacts().size());
        Assertions.assertTrue(contactManager.getAllContacts().stream()
                .filter(contact -> contact.getFirstName().equals("Jane") &&
                        contact.getLastName().equals("Robinson") &&
                        contact.getPhoneNumber().equals("0123456789"))
                .findAny()
                .isPresent());
    }

    // not running test on windows
    @Test
    @DisplayName("Should not create contact on Windows OS")
    @DisabledOnOs(value = OS.WINDOWS, disabledReason = "Disabled on Windows OS")
    public void shouldNotCreateContactOnWindows() {
        contactManager.addContact("Jane", "Robinson", "0123456789");
        Assertions.assertFalse(contactManager.getAllContacts().isEmpty());
        Assertions.assertEquals(1, contactManager.getAllContacts().size());
        Assertions.assertTrue(contactManager.getAllContacts().stream()
                .filter(contact -> contact.getFirstName().equals("Jane") &&
                        contact.getLastName().equals("Robinson") &&
                        contact.getPhoneNumber().equals("0123456789"))
                .findAny()
                .isPresent());
    }

    // In Junit 5 Assumptions are collection of utility methods that support conditional test execution based on
    // assumptions. Failed assumptions do not result in a test failure; rather, a failed assumption results in a test
    // being aborted. Assumption basically means “don't run this test if these conditions don't apply”
    // for some reson DEV and ENV is not working for me
    @Test
    @DisplayName("Should test contact creation on dev machine")
    public void shouldTestContactCreationOnDEV() {
        Assumptions.assumeTrue("DEV".equals(System.getProperty("ENV")));    // edit configurations for test class
        // at build tab (between hammer and play button. Write "-ea -DENV=DEV" in VM
        contactManager.addContact("Jane", "Robinson", "0123456789");
        Assertions.assertFalse(contactManager.getAllContacts().isEmpty());
        Assertions.assertEquals(1, contactManager.getAllContacts().size());
    }

//    // REPEATED TESTS - used when there is functionality with randomness. @RepeatedTest substitutes @Test
//    @DisplayName("Repeat contact creation test 5 times")
//    @RepeatedTest(value = 5, name = "Repeating contact creation test {currentRepetition} of {totalRepetitions}")
//    public void shouldTestContactCreationRepeatedly() {
//        contactManager.addContact("Jane", "Robinson", "0123456789");
//        Assertions.assertFalse(contactManager.getAllContacts().isEmpty());
//        Assertions.assertEquals(1, contactManager.getAllContacts().size());
//    }
//
//    // PARAMETERIZED TESTS are executed with different set of input. Substitutes @Test.
//    // Data is provided using different annotations, e.g. @ValueSource
//    @DisplayName("Parameterized test using value source")
//    @ParameterizedTest
//    @ValueSource(strings = {"0123456789", "0123", "01234aa789", "+01234556789"})
//    public void shouldTestContactCreationUsingValueSource(String phoneNumber) {
//        contactManager.addContact("Jane", "Robinson", phoneNumber);
//        Assertions.assertFalse(contactManager.getAllContacts().isEmpty());
//        Assertions.assertEquals(1, contactManager.getAllContacts().size());
//    }

    // using @MethodSource - other tests moved to nested classes
    @DisplayName("Method source case - phone number should match the required format")
    @ParameterizedTest
    @MethodSource("phoneNumberList")
    public void shouldTestContactCreationUsingMethodSource(String phoneNumber) {
        contactManager.addContact("Jane", "Robinson", phoneNumber);
        Assertions.assertFalse(contactManager.getAllContacts().isEmpty());
        Assertions.assertEquals(1, contactManager.getAllContacts().size());
    }

    private static List<String> phoneNumberList() {
        return Arrays.asList("0123456798", "0876543210", "5432167890");
    }

//    // using @CsvSource (CSV - Comma Separated Values)
//    @DisplayName("CSV Source case - phone number should match required format")
//    @ParameterizedTest
//    @CsvSource({"0123456789", "0987654321", "0987612345"})
//    public void shouldTestContactCreationUsingCsvSource(String phoneNumber) {
//        contactManager.addContact("Jane", "Robinson", phoneNumber);
//        Assertions.assertFalse(contactManager.getAllContacts().isEmpty());
//        Assertions.assertEquals(1, contactManager.getAllContacts().size());
//    }
//
//    // using @CsvSource (CSV - Comma Separated Values)
//    @DisplayName("CSV File Source case - phone number should match required format")
//    @ParameterizedTest
//    @CsvFileSource(resources = "/phone numbers junit tutorial.csv")
//    public void shouldTestContactCreationUsingCsvFileSource(String phoneNumber) {
//        contactManager.addContact("Jane", "Robinson", phoneNumber);
//        Assertions.assertFalse(contactManager.getAllContacts().isEmpty());
//        Assertions.assertEquals(1, contactManager.getAllContacts().size());
//    }

    // NESTED TESTS - can only use beforeEach and afterEach, beforeAll and afterAll is not allowed. Java won't let
    // use static methods in nested classes - InnerClass cannot have static members because it belongs to an instance
    // (of OuterClass ). If you declare InnerClass as static to detach it from the instance, your code will compile.
    // You can use afterAll and beforeAll if you add @TestInstance(TestInstance.Lifecycle.PER_CLASS).
    @Nested     // moved the commented out tests from above to here
    class RepeatedNestedTest {
        @DisplayName("Repeat contact creation test 5 times")
        @RepeatedTest(value = 5, name = "Repeating contact creation test {currentRepetition} of {totalRepetitions}")
        public void shouldTestContactCreationRepeatedly() {
            contactManager.addContact("Jane", "Robinson", "0123456789");
            Assertions.assertFalse(contactManager.getAllContacts().isEmpty());
            Assertions.assertEquals(1, contactManager.getAllContacts().size());
        }

    }

    @Nested     // moved the commented out tests from above to here
    class ParameterizedNestedTest {
        @DisplayName("Parameterized test using value source")
        @ParameterizedTest
        @ValueSource(strings = {"0123456789", "0123", "01234aa789", "+01234556789"})
        public void shouldTestContactCreationUsingValueSource(String phoneNumber) {
            contactManager.addContact("Jane", "Robinson", phoneNumber);
            Assertions.assertFalse(contactManager.getAllContacts().isEmpty());
            Assertions.assertEquals(1, contactManager.getAllContacts().size());
        }

        // using @CsvSource (CSV - Comma Separated Values)
        @DisplayName("CSV Source case - phone number should match required format")
        @ParameterizedTest
        @CsvSource({"0123456789", "0987654321", "0987612345"})
        public void shouldTestContactCreationUsingCsvSource(String phoneNumber) {
            contactManager.addContact("Jane", "Robinson", phoneNumber);
            Assertions.assertFalse(contactManager.getAllContacts().isEmpty());
            Assertions.assertEquals(1, contactManager.getAllContacts().size());
        }

        // using @CsvSource (CSV - Comma Separated Values)
        @DisplayName("CSV File Source case - phone number should match required format")
        @ParameterizedTest
        @CsvFileSource(resources = "/phone numbers junit tutorial.csv")
        public void shouldTestContactCreationUsingCsvFileSource(String phoneNumber) {
            contactManager.addContact("Jane", "Robinson", phoneNumber);
            Assertions.assertFalse(contactManager.getAllContacts().isEmpty());
            Assertions.assertEquals(1, contactManager.getAllContacts().size());
        }
    }

    // @Disabled - not encouraged, disables a test, but you might forget to enable it again

}