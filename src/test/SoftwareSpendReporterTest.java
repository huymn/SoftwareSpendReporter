import com.servicenow.softwarespendreporter.SoftwareSpendReporter;
import com.servicenow.softwarespendreporter.Transaction;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SoftwareSpendReporterTest {
    SoftwareSpendReporter SSR;
    Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
    String SPath = path.toString() + "\\src\\resources\\test1.csv";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeAll
    public void setUpAll() {
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    public void setUp() {
        SSR = new SoftwareSpendReporter();
    }

    @Test
    public void shouldPrintFromFile() {
        SSR.printReport(SPath);
        Assertions.assertEquals("Adobe $20,000\r\n  Premier Pro $20,000\r\nSalesforce $1,000\r\n  Random Product $1,000", outContent.toString().trim());
    }

    @Test
    public void shouldGetTransactions() {
        Assertions.assertThrows(NullPointerException.class, () -> SSR.getTransactions(null));
        Assertions.assertFalse(SSR.getTransactions(SPath).isEmpty());
        Assertions.assertEquals(SSR.getTransactions(SPath).size(), 2);
    }

    @Test
    public void shouldGetReport() {
        Transaction t1 = new Transaction("1/20/2021", "Microsoft", "Azure", 3000);
        Transaction t2 = new Transaction("1/20/2021", "Microsoft", "Words", 4000);
        Transaction t3 = new Transaction("1/20/2021", "Microsoft", "Excel", 5000);
        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(t1);
        transactions.add(t2);
        transactions.add(t3);
        Assertions.assertFalse(SSR.getReport(transactions).isEmpty());
        assertEquals(SSR.getReport(transactions).size(), 1);
    }

    @Test
    public void shouldGetTotal() {
        TreeMap<String, Integer> products = new TreeMap<>();
        products.put("product1", 3000);
        products.put("product2", 4000);
        products.put("product3", 5000);
        Assertions.assertEquals(SSR.getTotal(products), 12000);
    }

    @AfterAll
    public void tearDown() {
        System.setOut(originalOut);
    }
}