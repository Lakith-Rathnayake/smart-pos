package lk.ijse.dep11.pos.db;

import lk.ijse.dep11.pos.tm.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDataAccessTest {


    @Test
    void sqlSyntax() {
        assertDoesNotThrow(()->Class.forName("lk.ijse.dep11.pos.db.CustomerDataAccess"));
    }
    @BeforeEach
    void setUp() throws SQLException {
        SingleConnectionDataSource.getInstance().getConnection().setAutoCommit(false);
    }

    @AfterEach
    void tearDown() throws SQLException {
        SingleConnectionDataSource.getInstance().getConnection().rollback();
        SingleConnectionDataSource.getInstance().getConnection().setAutoCommit(true);
    }
    @Test
    void saveCustomer() {
        assertDoesNotThrow(() -> {
            CustomerDataAccess.saveCustomer(new Customer("C001", "Kasun", "Galle"));
            CustomerDataAccess.saveCustomer(new Customer("C002", "Ruwan", "Panadura"));
            assertThrows(SQLException.class, () -> {
                CustomerDataAccess.saveCustomer(new Customer("C001", "Kasun", "Galle"));
            });
        });
    }

    @Test
    void getAllCustomers() throws SQLException {
        CustomerDataAccess.saveCustomer(new Customer("C001", "Kasun", "Galle"));
        CustomerDataAccess.saveCustomer(new Customer("C002", "Ruwan", "Panadura"));
        assertDoesNotThrow(() -> {
            List<Customer> customerList = CustomerDataAccess.getAllCustomers();
            assertTrue(customerList.size() >= 2);
        });
    }


    @Test
    void updateCustomer() throws SQLException {
        CustomerDataAccess.saveCustomer(new Customer("C001", "Kasun", "Galle"));
        CustomerDataAccess.saveCustomer(new Customer("C002", "Ruwan", "Panadura"));
        assertDoesNotThrow(()-> {
           CustomerDataAccess.updateCustomer(new Customer("C001", "Lakith", "Colombo"));
        });
    }

    @Test
    void deleteCustomer() throws SQLException {
        CustomerDataAccess.saveCustomer(new Customer("C001", "Kasun", "Galle"));
        CustomerDataAccess.saveCustomer(new Customer("C002", "Ruwan", "Panadura"));
        int size = CustomerDataAccess.getAllCustomers().size();
        assertDoesNotThrow(() -> {
           CustomerDataAccess.deleteCustomer("C001");
           assertEquals(size - 1, CustomerDataAccess.getAllCustomers().size());
        });
    }

    @Test
    void getLastCustomerID() throws SQLException {
        String lastCustomerID = CustomerDataAccess.getLastCustomerID();
        if(CustomerDataAccess.getAllCustomers().isEmpty()) {
            assertNull(lastCustomerID);
        } else {
            CustomerDataAccess.saveCustomer(new Customer("C001", "Kasun", "Galle"));
            lastCustomerID = CustomerDataAccess.getLastCustomerID();
            assertNotNull(lastCustomerID);
        }
    }
}