package com.sbm.live_order_board.model;

public class Order {

    private final int userId;
    private final OrderType type;
    private final int price;
    private final int quantity;

    /**
     * Method to set and get Order
     *  @param userId
     *  @param type
     *  @param price
     *  @param quantity
     */
    
    public Order(int userId, OrderType type, int price, int quantity) {
        this.type = type;
        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderType getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
