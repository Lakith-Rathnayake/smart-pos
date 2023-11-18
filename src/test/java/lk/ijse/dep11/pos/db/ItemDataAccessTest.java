package lk.ijse.dep11.pos.db;

import lk.ijse.dep11.pos.tm.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ItemDataAccessTest {

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
    void getAllItems() throws SQLException {
        ItemDataAccess.saveItem(new Item("1234", "Mouse", 20, new BigDecimal("450.50")));
        ItemDataAccess.saveItem(new Item("5783", "Keyboard", 30, new BigDecimal("930.50")));
        assertTrue(ItemDataAccess.getAllItems().size() >= 2);
    }

    @Test
    void saveItem() throws SQLException {
        assertDoesNotThrow(() -> {
            ItemDataAccess.saveItem(new Item("1234", "Mouse", 20, new BigDecimal("450.50")));
            ItemDataAccess.saveItem(new Item("5783", "Keyboard", 30, new BigDecimal("930.50")));
        });
        assertThrows(SQLException.class, () -> {
            ItemDataAccess.saveItem(new Item("1234", "Mouse", 20, new BigDecimal("450.50")));
        });
    }

    @Test
    void updateItem() throws SQLException {
        ItemDataAccess.saveItem(new Item("1234", "Mouse", 20, new BigDecimal("450.50")));
        assertDoesNotThrow(()-> ItemDataAccess.updateItem(new Item("1234", "Headphone", 45, new BigDecimal("1200.00"))));
    }

    @Test
    void deleteItem() throws SQLException {
        ItemDataAccess.saveItem(new Item("1234", "Mouse", 20, new BigDecimal("450.50")));
        ItemDataAccess.saveItem(new Item("5783", "Keyboard", 30, new BigDecimal("930.50")));
        int size = ItemDataAccess.getAllItems().size();
        assertDoesNotThrow(()-> ItemDataAccess.deleteItem("1234"));
        assertTrue(ItemDataAccess.getAllItems().size() == 1);
    }

    @Test
    void existsItem() throws SQLException {
        ItemDataAccess.saveItem(new Item("1234", "Mouse", 20, new BigDecimal("450.50")));
        ItemDataAccess.saveItem(new Item("5783", "Keyboard", 30, new BigDecimal("930.50")));
        assertTrue(ItemDataAccess.existsItem("1234"));
    }
}