package ru.inno.market;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import ru.inno.market.core.Catalog;
import ru.inno.market.model.Client;
import ru.inno.market.model.Item;
import ru.inno.market.model.Order;

import java.util.Map;

import static org.junit.Assert.*;

public class OrderTest {

    @Test
    @DisplayName("Создать 1 заказ без товаров")
    public void createOrderWithoutItem() {
        Client client1 = new Client(1, "John");
        Order order1 = new Order(1, client1);
        Map<Item, Integer> cart = order1.getCart();
        assertEquals(0, cart.size());
        assertEquals(0, order1.getTotalPrice(), 0);
        assertFalse(order1.isDiscountApplied());
    }

    @Test
    @DisplayName("Создать несколько заказов для разных клиентов без товаров")
    public void createSomeOtherOrders() {
        Client client1 = new Client(1, "John");
        Client client2 = new Client(2, "Lisa");
        Client client3 = new Client(3, "Tonny");
        Order order1 = new Order(1, client1);
        Order order2 = new Order(2, client2);
        Order order3 = new Order(3, client3);
        Map<Item, Integer> cart1 = order1.getCart();
        Map<Item, Integer> cart2 = order2.getCart();
        Map<Item, Integer> cart3 = order3.getCart();
        assertEquals(0, cart1.size());
        assertEquals(0, cart2.size());
        assertEquals(0, cart3.size());
        assertEquals(0, order1.getTotalPrice(), 0);
        assertEquals(0, order2.getTotalPrice(), 0);
        assertEquals(0, order3.getTotalPrice(), 0);
        assertFalse(order1.isDiscountApplied());
        assertFalse(order2.isDiscountApplied());
        assertFalse(order3.isDiscountApplied());
    }


    @Test
    @DisplayName("Добавить в заказ 1 товар из каталога")
    public void addOneItem() {
        Client client1 = new Client(1, "John");
        Order order1 = new Order(1, client1);
        Catalog catalog = new Catalog();
        Item item1 = catalog.getItemById(1);
        order1.addItem(item1);
        Map<Item, Integer> cart = order1.getCart();
        assertEquals(1, cart.size());       //товар добавлен в заказ
        assertEquals((Integer) 1, cart.get(item1));  //кол-во товара
        assertEquals(item1, cart.keySet().stream().findFirst().get()); //в заказе именно товар из каталога
        assertFalse(order1.isDiscountApplied()); //скидка не применялась
    }

    @Test
    @DisplayName("Добавить в заказ несколько товаров из каталога")
    public void addSomeItems() {
        Client client1 = new Client(1, "John");
        Order order1 = new Order(1, client1);
        Catalog catalog = new Catalog();
        Item item1 = catalog.getItemById(1);
        Item item2 = catalog.getItemById(2);
        Item item3 = catalog.getItemById(3);
        order1.addItem(item1);
        order1.addItem(item2);
        order1.addItem(item3);
        Map<Item, Integer> cart = order1.getCart();
        assertEquals(3, cart.size());       //товары добавлены в заказ
        double sumPrice = item1.getPrice() + item2.getPrice() + item3.getPrice();
        assertEquals(sumPrice, order1.getTotalPrice(), 0); //сумма выбранных из каталога товаров совпадает сумме добавленных в заказ
    }


    @Test
    @DisplayName("Добавить в заказ 1 товар из каталога в количестве 3 штук")
    public void addItem() {
        Client client1 = new Client(1, "John");
        Order order1 = new Order(1, client1);
        Catalog catalog = new Catalog();
        Item item1 = catalog.getItemById(1);
        order1.addItem(item1);
        order1.addItem(item1);
        order1.addItem(item1);
        Map<Item, Integer> cart = order1.getCart();
        assertEquals(1, cart.size());       //товар добавлен в заказ
        assertEquals((Integer) 3, cart.get(item1));  //кол-во товара
        assertFalse(order1.isDiscountApplied()); //скидка не применялась
        double sumPrice = item1.getPrice() * 3;
        assertEquals(sumPrice, order1.getTotalPrice(), 0);
    }

    @Test
    @DisplayName("Применить скидку на стоимость заказа, где ранее скидка не применялась")
    public void applyFirstDiscount() {
        Client client1 = new Client(1, "John");
        Order order1 = new Order(1, client1);
        Catalog catalog = new Catalog();
        Item item1 = catalog.getItemById(1);
        order1.addItem(item1);
        boolean hasDiscountBefore = order1.isDiscountApplied();
        double priceBeforeDiscount = order1.getTotalPrice();
        double expectedPrice = priceBeforeDiscount * (1 - 0.2);
        order1.applyDiscount(0.2);
        assertFalse(hasDiscountBefore); //до этого не было скидок
        assertTrue(order1.isDiscountApplied());    //скидка применилась
        assertEquals(expectedPrice, order1.getTotalPrice(), 0); //рассчет стоимости после скидки ок
    }

    @Test
    @DisplayName("Применить скидку на стоимость заказа, где ранее уже применялась скидка")
    @Tag("negative")
    public void applySecondDiscount() {
        Client client1 = new Client(1, "John");
        Order order1 = new Order(1, client1);
        Catalog catalog = new Catalog();
        Item item1 = catalog.getItemById(1);
        order1.addItem(item1);
        order1.applyDiscount(0.2);
        boolean hasDiscount = order1.isDiscountApplied();
        double priceAfterFirstDiscount = order1.getTotalPrice();
        order1.applyDiscount(0.2); //снова применяем скидку
        double priceAfterSecondDiscount = order1.getTotalPrice();
        assertTrue(hasDiscount);   //скидка уже применялась ранее
        assertEquals(priceAfterFirstDiscount, priceAfterSecondDiscount, 0); //вторая скидка не применилась т.к. discountApplied = true;
    }
}