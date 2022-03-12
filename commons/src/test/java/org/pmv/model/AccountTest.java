package org.pmv.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.pmv.model.exceptions.InsufficientBalanceException;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;


//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountTest {

    Account ericAccount;
    Account stanAccount;

    @BeforeEach
    void initMethodTest(){
        this.ericAccount = new Account("Eric Cartman",new BigDecimal("5000.00"));
        this.stanAccount = new Account("Stan", new BigDecimal("5000.00"));
    }

    @BeforeAll
     static void beforeAll() {
        System.out.println("Inicializando los tests...");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Ya se han ejecutado todos los tests...");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Test executed");
    }

    @Test
    void account_name_test() {
        String expected = "Eric Cartman";
        String actual = ericAccount.getPerson();
        assertEquals(expected,actual,
                () -> MessageFormat.format("Persons are NOT equals. Expected value is: {0}", expected));
    }

    @Test
    void account_name_constructor_test() {
        String expected = "Eric Cartman";
        String actual = ericAccount.getPerson();
        assertEquals(expected,actual);
    }

    @Test
    void account_balance_test() {
        assertEquals(5000.00, ericAccount.getBalance().doubleValue());
        assertFalse(ericAccount.getBalance().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(ericAccount.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void account_reference_test() {
        assertNotEquals(ericAccount,stanAccount);
    }

    @Test
    @DisplayName("Test: Cash withdrawal.")
    void account_debit_test() {
        stanAccount.debit(new BigDecimal("100"));
        assertNotNull(stanAccount.getBalance());
        assertEquals(4900,stanAccount.getBalance().intValue());
        assertEquals("4900.00", stanAccount.getBalance().toPlainString());
    }


    @Test
    @DisplayName("Test: Account deposit.")
    void account_credit_test() {
        stanAccount.credit(new BigDecimal("100"));
        assertNotNull(stanAccount.getBalance());
        assertEquals(5100,stanAccount.getBalance().intValue());
        assertEquals("5100.00", stanAccount.getBalance().toPlainString());
    }

    @Test
    void insufficient_balance_on_account_exception_test() {
        InsufficientBalanceException insufficientBalanceException =
                assertThrows(InsufficientBalanceException.class,
                        () -> stanAccount.debit(new BigDecimal("10000")));
        String actual = insufficientBalanceException.getMessage();
        String expected = "Insufficient balance in your account";
        assertEquals(expected, actual);
    }


    @Test
    void transfer_amount_between_accounts_test() {
        //Account stanAccount = new Account("Stan", new BigDecimal("5000.0"));
        //Account ericAccount = new Account("Eric", new BigDecimal("5000.0"));

        Bank cityBank = new Bank("City Bank");
        cityBank.transfer(stanAccount,ericAccount,new BigDecimal("100.0"));

        BigDecimal actualRootAccountExpectedAmount = new BigDecimal("4900.00");
        BigDecimal actualDestinationAccountExpectedAmount = new BigDecimal("5100.00");

        assertEquals(actualRootAccountExpectedAmount,stanAccount.getBalance());
        assertEquals(actualDestinationAccountExpectedAmount, ericAccount.getBalance());
    }

    @Test
    @Disabled
    void relation_bank_accounts_test() {
        Bank cityBank = new Bank("City Bank");
        cityBank.addAccount(stanAccount);
        cityBank.addAccount(ericAccount);

        cityBank.transfer(stanAccount,ericAccount,new BigDecimal("100.0"));

        BigDecimal actualRootAccountExpectedAmount = new BigDecimal("4900.00");
        BigDecimal actualDestinationAccountExpectedAmount = new BigDecimal("5100.00");

        assertEquals(actualRootAccountExpectedAmount,stanAccount.getBalance());
        assertEquals(actualDestinationAccountExpectedAmount, ericAccount.getBalance());

        assertEquals(2,cityBank.getAccounts().size());
        assertEquals("City Bank", stanAccount.getBank().getName());
        assertEquals("Stan",cityBank.getAccounts().stream()
                .filter(a -> a.getPerson().equals("Stan"))
                .findFirst()
                .get().getPerson());
        assertTrue(cityBank.getAccounts().stream()
                .anyMatch(a -> a.getPerson().equals("Eric Cartman")));

    }

    @Test
    void relation_bank_accounts_assertAll_test() {
        Bank cityBank = new Bank("City Bank");
        cityBank.addAccount(stanAccount);
        cityBank.addAccount(ericAccount);

        cityBank.transfer(stanAccount,ericAccount,new BigDecimal("100.0"));

        BigDecimal actualRootAccountExpectedAmount = new BigDecimal("4900.00");
        BigDecimal actualDestinationAccountExpectedAmount = new BigDecimal("5100.00");

        assertAll(() -> assertEquals(actualRootAccountExpectedAmount,stanAccount.getBalance()),
                  () -> assertEquals(actualDestinationAccountExpectedAmount, ericAccount.getBalance()),
                  () -> assertEquals(2,cityBank.getAccounts().size()),
                  () -> assertEquals("City Bank", stanAccount.getBank().getName()),
                  () -> assertEquals("Stan",cityBank.getAccounts().stream()
                          .filter(a -> a.getPerson().equals("Stan"))
                          .findFirst()
                          .get().getPerson()),
                  () -> assertTrue(cityBank.getAccounts().stream()
                          .anyMatch(a -> a.getPerson().equals("Eric Cartman")))
        );
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void only_windows_test() {
        assertTrue(true);
    }

    @Test
    @EnabledOnOs({OS.LINUX,OS.MAC})
    void not_windows_test() {assertTrue(true);}

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void not_in_windows_test() {}

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void only_in_jdk_8_test() {}

    @Test
    @EnabledOnJre(JRE.JAVA_17)
    void only_in_jdk_17_test() {}

    @Test
    void print_system_properties() {
        Properties properties = System.getProperties();
        properties.forEach((k,v) -> System.out.println(k + " : " + v + "\n"));
    }

    @Test
    @EnabledIfSystemProperty(named = "user.language", matches = "es")
    void saludo_test() {
        String saludo = "Hola";
        assertEquals("Hola", saludo);
    }

    @Test
    void print_environment_variables() {
        Map<String, String> env = System.getenv();
        env.forEach((k,v) -> System.out.println(k + " : " + v));
    }

    @Test
    @EnabledIfEnvironmentVariable(named="USERNAME", matches="caizo")
    void if_user_is_caizo() {
        String caizo = "caizo";
        assertEquals("caizo",caizo);
    }

    @Test
    void account_balance_dev_test() {
        boolean isDev = "dev".equals(System.getProperty("ENV"));
        assumeTrue(isDev);
        assertEquals(5000.00, ericAccount.getBalance().doubleValue());
        assertFalse(ericAccount.getBalance().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(ericAccount.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void account_balance_dev_test_2() {
        boolean isDev = "dev".equals(System.getProperty("ENV"));
        assumingThat(isDev,() -> {
            assertEquals(5000.00, ericAccount.getBalance().doubleValue());
            assertFalse(ericAccount.getBalance().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(ericAccount.getBalance().compareTo(BigDecimal.ZERO) > 0);
        });
    }

}