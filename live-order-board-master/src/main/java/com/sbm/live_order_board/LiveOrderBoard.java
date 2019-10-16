package com.sbm.live_order_board;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.UUID;

import static com.sbm.live_order_board.OrderType.BUY;
import static com.sbm.live_order_board.OrderType.SELL;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

public class LiveOrderBoard {

    private final Map<UUID, Order> orders = new HashMap<>();

    public UUID registerOrder(Order order) {
        UUID id = UUID.randomUUID();
        orders.put(id, order);

        return id;
    }

    public void cancelOrder(UUID id) {
        if (orders.remove(id) == null) {
            throw new NoSuchElementException("No Order with id " + id);
        }
    }

    public List<SummaryEntry> getSummary() {
        List<SummaryEntry> result = new ArrayList<>();

        result.addAll(createSummarySection(SELL, naturalOrder()));
        result.addAll(createSummarySection(BUY, reverseOrder()));

        return result;
    }

    private List<SummaryEntry> createSummarySection(OrderType type, Comparator<Integer> orderingComparator) {
        return orders.values().stream()
                .filter(o -> o.getType() == type)
                .collect(groupingBy(
                        Order::getPrice,
                        () -> new TreeMap<>(orderingComparator),
                        summingInt(Order::getQuantity)))
                .entrySet().stream()
                .map(e -> new SummaryEntry(type, e.getKey(), e.getValue()))
                .collect(toList());
    }
}
