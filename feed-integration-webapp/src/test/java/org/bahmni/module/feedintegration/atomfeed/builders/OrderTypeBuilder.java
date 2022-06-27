package org.bahmni.module.feedintegration.atomfeed.builders;

import org.bahmni.module.feedintegration.model.OrderType;

public class OrderTypeBuilder {
    private OrderType orderType;

    public OrderTypeBuilder() {
        orderType = new OrderType();
    }

    public OrderTypeBuilder withName(String orderTypeName) {
        orderType.setName(orderTypeName);
        return this;
    }

    public OrderType build() {
        return orderType;
    }
}
