package com.sbm.live_order_board;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sbm.live_order_board.model.Order;
import com.sbm.live_order_board.model.OrderType;
import com.sbm.live_order_board.model.SummaryEntry;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.sbm.live_order_board.model.OrderType.BUY;
import static com.sbm.live_order_board.model.OrderType.SELL;
import static org.assertj.core.api.Assertions.assertThat;

public class LiveOrderBoardTest {

    private static final int USER_ID = 1;
    private static final int QUANTITY = 20;
    private static final int PRICE = 300;

    private LiveOrderBoard board = new LiveOrderBoard();
    private static final Logger logger = LoggerFactory.getLogger(LiveOrderBoardTest.class);	
    
    
        
    /**
     * Method to RegisterOrder
     *  @param USER_ID
     *  @param SELL
     *  @param PRICE
     *  @param QUANTITY
     */
    
    @Test
    public void shouldRegisterOrder() {
    	
    	
        Order order = anOrder(USER_ID, SELL, PRICE, QUANTITY);

        board.registerOrder(order);

        assertThat(board.getSummary()).containsOnly(aSummaryEntry(SELL, PRICE, QUANTITY));
    }

    
    /**
     * Method to CancelOrder
     */
    @Test
    public void shouldCancelOrder() {
        Order order1 = anOrder(1, BUY, PRICE, 10);
        Order order2 = anOrder(2, BUY, PRICE, 20);
        UUID orderId1 = board.registerOrder(order1);
        UUID orderId2 = board.registerOrder(order2);

        board.cancelOrder(orderId1);

        assertThat(board.getSummary()).containsOnly(aSummaryEntry(BUY, PRICE, order2.getQuantity()));
    }

    
    /**
     * Method to CancelOrder
     */
    @Test(expected = NoSuchElementException.class)
    public void shouldFailCancellingNotExistingOrder() {
        board.cancelOrder(UUID.randomUUID());
    }

    
    /**
     * Method to Merge Same Price Of SameType InSummary
     */
    
    @Test
    public void shouldMergeSamePriceOfSameTypeInSummary() {
        board.registerOrder(anOrder(1, SELL, 100, 10));
        board.registerOrder(anOrder(2, SELL, 200, 20));
        board.registerOrder(anOrder(3, SELL, 100, 30));

        List<SummaryEntry> summary = board.getSummary();

        assertThat(summary).containsOnly(
                aSummaryEntry(SELL, 100, 40),
                aSummaryEntry(SELL, 200, 20));
    }
    
    
    /**
     * Method to Not Merge SamePrice Of DifferentTypes InSummary
     */

    @Test
    public void shouldNotMergeSamePriceOfDifferentTypesInSummary() {
        board.registerOrder(anOrder(USER_ID, BUY, 300, 10));
        board.registerOrder(anOrder(USER_ID, SELL, 300, 20));

        List<SummaryEntry> summary = board.getSummary();

        assertThat(summary).containsOnly(
                aSummaryEntry(BUY, 300, 10),
                aSummaryEntry(SELL, 300, 20));
    }

    
    /**
     * Method to Return Sells Summary Ascending
     */
    
    @Test
    public void shouldReturnSellsSummaryAscending() {
        board.registerOrder(anOrder(USER_ID, SELL, 100, 10));
        board.registerOrder(anOrder(USER_ID, SELL, 300, 30));
        board.registerOrder(anOrder(USER_ID, SELL, 200, 20));

        List<SummaryEntry> summary = board.getSummary();

        assertThat(summary).containsExactly(
                aSummaryEntry(SELL, 100, 10),
                aSummaryEntry(SELL, 200, 20),
                aSummaryEntry(SELL, 300, 30));
    }

    
    /**
     * Method to Return Buys Summary Descending
     */
    @Test
    public void shouldReturnBuysSummaryDescending() {
        board.registerOrder(anOrder(USER_ID, BUY, 100, 10));
        board.registerOrder(anOrder(USER_ID, BUY, 300, 30));
        board.registerOrder(anOrder(USER_ID, BUY, 200, 20));

        List<SummaryEntry> summary = board.getSummary();

        assertThat(summary).containsExactly(
                aSummaryEntry(BUY, 300, 30),
                aSummaryEntry(BUY, 200, 20),
                aSummaryEntry(BUY, 100, 10));
    }

    /**
     * Method to Sells Go Before Buys InSummary
     */
    
    @Test
    public void shouldSellsGoBeforeBuysInSummary() {
        board.registerOrder(anOrder(USER_ID, BUY, PRICE, QUANTITY));
        board.registerOrder(anOrder(USER_ID, SELL, PRICE, QUANTITY));

        List<SummaryEntry> summary = board.getSummary();

        assertThat(summary).containsExactly(
                aSummaryEntry(SELL, PRICE, QUANTITY),
                aSummaryEntry(BUY, PRICE, QUANTITY));
    }

    
    /**
     * Method to create Order
     */
    private static Order anOrder(int userId, OrderType type, int price, int quantity) {
        return new Order(userId, type, price, quantity);
    }

    
    /**
     * Method to create SummaryEntry
     */
    
    private static SummaryEntry aSummaryEntry(OrderType type, int price, int quantity) {
        return new SummaryEntry(type, price, quantity);
    }
}
