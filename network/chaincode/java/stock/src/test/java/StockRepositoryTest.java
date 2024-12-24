import com.owlike.genson.Genson;
import org.chaincode.Exchange;
import org.chaincode.Inventory;
import org.chaincode.StockRepository;
import org.chaincode.TransactionType;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockRepositoryTest {

    private StockRepository stockRepository;

    @Mock
    private Context mockContext;

    @Mock
    private ChaincodeStub mockStub;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stockRepository = new StockRepository();
        when(mockContext.getStub()).thenReturn(mockStub);
    }

    @Test
    void testInitLedger() {
        stockRepository.initLedger(mockContext);

        verify(mockStub, atLeastOnce()).putStringState(anyString(), anyString());
    }

    @Test
    void testInventoryExists() {
        when(mockStub.getStringState("inventory1")).thenReturn("value");
        assertTrue(stockRepository.inventoryExists(mockContext, "inventory1"));

        when(mockStub.getStringState("inventory2")).thenReturn("");
        assertFalse(stockRepository.inventoryExists(mockContext, "inventory2"));
    }

    @Test
    void testExchangeExists() {
        when(mockStub.getStringState("exchange1")).thenReturn("value");
        assertTrue(stockRepository.exchangeExists(mockContext, "exchange1"));

        when(mockStub.getStringState("exchange2")).thenReturn("");
        assertFalse(stockRepository.exchangeExists(mockContext, "exchange2"));
    }

    @Test
    void testCreateInventory_Invalid() {
        when(mockStub.getStringState("inventory1")).thenReturn("value");

        ChaincodeException exception = assertThrows(ChaincodeException.class, () ->
                stockRepository.createInventory(mockContext, "inventory1", "organization", Map.of("product", 100)));
        assertEquals("Inventory inventory1 already exists", exception.getMessage());
    }

    @Test
    void testCreateInventory_Valid() {
        when(mockStub.getStringState("inventory1")).thenReturn("");

        Inventory inventory = stockRepository.createInventory(mockContext, "inventory1", "organization", Map.of("product", 100));
        assertEquals("inventory1", inventory.getInventory_id());
        assertEquals("organization", inventory.getOrganization());
        assertEquals(Map.of("product", 100), inventory.getStock());

        verify(mockStub, times(1)).putStringState(eq("inventory1"), anyString());
    }

    @Test
    void testCreateExchange_Success() {
        Inventory inventory = new Inventory("inventory1", "organization", Map.of("product", 200));
        when(mockStub.getStringState("inventory1")).thenReturn(new Genson().serialize(inventory));

        Exchange exchange = stockRepository.createExchange(mockContext, "exchange1", "product", "inventory1", 100,
                "2023-10-10", TransactionType.SEND);

        assertEquals("exchange1", exchange.getExchange_id());
        assertEquals("product", exchange.getProduct_id());
        assertEquals("inventory1", exchange.getOrganization());
        assertEquals(100, exchange.getQuantity());
        assertEquals(TransactionType.SEND, exchange.getTransaction());
        verify(mockStub, atLeastOnce()).putStringState(anyString(), anyString());
    }

    @Test
    void testCreateExchange_InsufficientStock() {
        Inventory inventory = new Inventory("inventory1", "organization", new LinkedHashMap<>(Map.of("product", 50)));
        when(mockStub.getStringState("inventory1")).thenReturn(new Genson().serialize(inventory));

        ChaincodeException exception = assertThrows(ChaincodeException.class, () ->
                stockRepository.createExchange(mockContext, "exchange1", "product", "inventory1", 100,
                        "2023-10-10", TransactionType.SEND));
        assertEquals("Organization organization does not have enough stock for product product", exception.getMessage());
    }

    @Test
    void testGetInventory_Invalid() {
        when(mockStub.getStringState("inventory1")).thenReturn("");

        ChaincodeException exception = assertThrows(ChaincodeException.class, () ->
                stockRepository.getInventory(mockContext, "inventory1"));

        assertEquals("Inventory inventory1 does not exist", exception.getMessage());
    }

    @Test
    void testGetInventory_Valid() {
        Inventory inventory = new Inventory("inventory1", "organization", Map.of("product", 100));
        when(mockStub.getStringState("inventory1")).thenReturn(new Genson().serialize(inventory));

        Inventory result = stockRepository.getInventory(mockContext, "inventory1");

        assertNotNull(result);
        assertEquals("inventory1", result.getInventory_id());
        assertEquals("organization", result.getOrganization());
        assertEquals(Map.of("product", 100), result.getStock());
    }

    @Test
    void testTrade() {
        Inventory senderInventory = new Inventory("sender", "sender", Map.of("product", 150));
        Inventory receiverInventory = new Inventory("receiver", "receiver", Map.of("product", 50));

        when(mockStub.getStringState("sender")).thenReturn(new Genson().serialize(senderInventory));
        when(mockStub.getStringState("receiver")).thenReturn(new Genson().serialize(receiverInventory));

        System.out.println(" Inventory before trade");
        System.out.println("senderInventory: " + stockRepository.getInventory(mockContext, "sender"));
        System.out.println("receiverInventory: " + stockRepository.getInventory(mockContext, "receiver"));

        stockRepository.trade(mockContext, "sender", "receiver", "product", 100);

        System.out.println(" Inventory after trade");
        System.out.println("senderInventory: " + stockRepository.getInventory(mockContext, "sender"));
        System.out.println("receiverInventory: " + stockRepository.getInventory(mockContext, "receiver"));


        verify(mockStub, atLeastOnce()).putStringState(anyString(), anyString());
    }
}