package com.sbm.live_order_board;

public class SummaryEntry {

    private final OrderType type;
    private final int price;
    private final int quantity;

    public SummaryEntry(OrderType type, int price, int quantity) {
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    }

    // Default Intellij implementations

    @Override
    public String toString() {
        return "SummaryEntry{" +
                "type=" + type +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SummaryEntry that = (SummaryEntry) o;

        if (price != that.price) return false;
        if (quantity != that.quantity) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + price;
        result = 31 * result + quantity;
        return result;
    }
}
