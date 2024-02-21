package ru.inno.market;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import ru.inno.market.core.Catalog;
import ru.inno.market.core.MarketService;
import ru.inno.market.model.Client;
import ru.inno.market.model.Item;

import ru.inno.market.model.PromoCodes;

import static org.junit.Assert.assertEquals;


public class MarketServiceTest {


    @Test
    @DisplayName("Создать 1 заказ для клиента")
    public void createOrderWithoutItem() {
        Client client1 = new Client(1, "John");
        MarketService market = new MarketService();
        market.createOrderFor(client1);
        assertEquals(1, market.getOrderInfo(1).getId());
    }


    @Test
    @DisplayName("Создать несколько разных заказов для одного клиента")
    public void createOrderForOneClient() {
        Client client1 = new Client(1, "John");
        MarketService market = new MarketService();
        int orderId1 = market.createOrderFor(client1);
        int orderId2 = market.createOrderFor(client1);
        int orderId3 = market.createOrderFor(client1);
        assertEquals(orderId1, market.getOrderInfo(1).getId());
        assertEquals(orderId2, market.getOrderInfo(2).getId());
        assertEquals(orderId3, market.getOrderInfo(3).getId());
    }

    @Test
    @DisplayName("Добавить товар в заказ")
    public void addItems() {
        Client client1 = new Client(1, "John");
        MarketService market = new MarketService();
        Catalog catalog = new Catalog();
        Item item1 = catalog.getItemById(1);
        int orderId = market.createOrderFor(client1);
        market.addItemToOrder(item1, orderId);
        var order = market.getOrderInfo(orderId);
        var items = order.getItems();
        assertEquals((Integer) 1, items.get(item1)); //добавлен товар в количестве 1 штуки
    }

    @Test
    @DisplayName("Добавить товар несколько раз в заказ")
    public void addItem() {
        Client client1 = new Client(1, "John");
        MarketService market = new MarketService();
        Catalog catalog = new Catalog();
        Item item1 = catalog.getItemById(1);
        int orderId = market.createOrderFor(client1);
        market.addItemToOrder(item1, orderId);
        market.addItemToOrder(item1, orderId);
        var order = market.getOrderInfo(orderId);
        var items = order.getItems();
        assertEquals((Integer) 2, items.get(item1)); //добавлен товар в количестве 2 штук
    }


    @Test
    @DisplayName("Применить скидку на заказ по валидному промокоду")
    public void applyValidDiscount() {
        Client client1 = new Client(1, "John");
        MarketService market = new MarketService();
        Catalog catalog = new Catalog();
        Item item1 = catalog.getItemById(1);
        int orderId = market.createOrderFor(client1);
        market.addItemToOrder(item1, orderId);
        double priceBeforeDiscount = market.getOrderInfo(orderId).getTotalPrice();
        market.applyDiscountForOrder(orderId, PromoCodes.HAPPY_HOUR);
        double priceAfterDiscount = market.getOrderInfo(orderId).getTotalPrice();
        assertEquals(priceAfterDiscount, priceBeforeDiscount * (1 - PromoCodes.HAPPY_HOUR.getDiscount()), 0); //скидка по валидному промокоду применилась корректно
    }


    @Test
    @DisplayName("Применить скидку только на 1 заказ клиента, на остальные не применять")
    public void applyValidDiscountForOneOrder() {
        Client client1 = new Client(1, "John");
        MarketService market = new MarketService();
        Catalog catalog = new Catalog();
        Item item1 = catalog.getItemById(1);
        Item item2 = catalog.getItemById(2);
        int orderId = market.createOrderFor(client1);
        int orderId2 = market.createOrderFor(client1);
        market.addItemToOrder(item1, orderId);
        market.addItemToOrder(item2, orderId2);
        double priceBeforeDiscountForOrder1 = market.getOrderInfo(orderId).getTotalPrice();
        double priceBeforeDiscountForOrder2 = market.getOrderInfo(orderId2).getTotalPrice();
        market.applyDiscountForOrder(orderId, PromoCodes.HAPPY_HOUR);
        double priceAfterDiscountForOrder1 = market.getOrderInfo(orderId).getTotalPrice();
        double priceAfterDiscountForOrder2 = market.getOrderInfo(orderId2).getTotalPrice();
        assertEquals(priceAfterDiscountForOrder1, priceBeforeDiscountForOrder1 * (1 - PromoCodes.HAPPY_HOUR.getDiscount()), 0); //скидка по валидному промокоду применилась корректно
        assertEquals(priceAfterDiscountForOrder2, priceBeforeDiscountForOrder2, 0); //для второго заказа клиента скидка не применилась
    }

    @Test
    @DisplayName("Применить один промокод на один заказ дважды")
    @Tag("negative")
    public void applySecondValidDiscount() {
        Client client1 = new Client(1, "John");
        MarketService market = new MarketService();
        Catalog catalog = new Catalog();
        Item item1 = catalog.getItemById(1);
        int orderId = market.createOrderFor(client1);
        market.addItemToOrder(item1, orderId);
        double priceBeforeFirstDiscount = market.getOrderInfo(orderId).getTotalPrice();
        market.applyDiscountForOrder(orderId, PromoCodes.HAPPY_HOUR);
        double priceAfterFirstDiscount = market.getOrderInfo(orderId).getTotalPrice();
        market.applyDiscountForOrder(orderId, PromoCodes.HAPPY_HOUR);
        double priceAfterSecondDiscount = market.getOrderInfo(orderId).getTotalPrice();
        assertEquals(priceAfterFirstDiscount, priceBeforeFirstDiscount * (1 - PromoCodes.HAPPY_HOUR.getDiscount()), 0); //скидка по валидному промокоду применилась корректно
        assertEquals(priceAfterFirstDiscount, priceAfterSecondDiscount, 0); //второй раз промокод нельзя применить к заказу
    }

    @Test
    @DisplayName("Применить разные промокоды на один заказ")
    @Tag("negative")
    public void applyOtherValidDiscount() {
        Client client1 = new Client(1, "John");
        MarketService market = new MarketService();
        Catalog catalog = new Catalog();
        Item item1 = catalog.getItemById(1);
        int orderId = market.createOrderFor(client1);
        market.addItemToOrder(item1, orderId);
        double priceBeforeFirstDiscount = market.getOrderInfo(orderId).getTotalPrice();
        market.applyDiscountForOrder(orderId, PromoCodes.HAPPY_HOUR);
        double priceAfterFirstDiscount = market.getOrderInfo(orderId).getTotalPrice();
        market.applyDiscountForOrder(orderId, PromoCodes.LOVE_DAY);
        double priceAfterSecondDiscount = market.getOrderInfo(orderId).getTotalPrice();
        assertEquals(priceAfterFirstDiscount, priceBeforeFirstDiscount * (1 - PromoCodes.HAPPY_HOUR.getDiscount()), 0); //скидка по валидному промокоду применилась корректно
        assertEquals(priceAfterFirstDiscount, priceAfterSecondDiscount, 0); // другой промокод нельзя применить к заказу если скидка уже была ранее
    }
}