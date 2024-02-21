package ru.inno.market.core;

import ru.inno.market.model.Category;
import ru.inno.market.model.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Catalog {
    private Map<Item, Integer> storage;

    public Catalog() {
        storage = new HashMap<>();
        storage.put(
                new Item(1, "Apple iPhone SE", Category.SMARTPHONES, 97990), // товар
                10 // количество товара на складе
        );
        storage.put(
                new Item(2, "Xiaomi POCO M5s", Category.SMARTPHONES, 19490), // товар
                8 // количество товара на складе
        );
        storage.put(
                new Item(3, "Samsung Galaxy S23+", Category.SMARTPHONES, 72490), // товар
                15 // количество товара на складе
        );
        storage.put(
                new Item(4, "Samsung Galaxy S20 Ultra", Category.SMARTPHONES, 48890), // товар
                15 // количество товара на складе
        );
        storage.put(
                new Item(5, "Apple MacBook Air 13 2020", Category.LAPTOPS, 89990), // товар
                5 // количество товара на складе
        );
        storage.put(
                new Item(6, "MSI GP66 11UG-699XRU Leopard 9S7-154322-699", Category.LAPTOPS, 130000), // товар
                4 // количество товара на складе
        );
        storage.put(
                new Item(7, "HUAWEI MateBook E", Category.LAPTOPS, 89999),
                2 // количество товара на складе
        );
        storage.put(
                new Item(8, "Microsoft Windows 11 Pro", Category.SOFTWARE, 5200),
                100 // количество товара на складе
        );
        storage.put(
                new Item(9, "Microsoft 365 Персональный, электронный ключ", Category.SOFTWARE, 3790),
                90 // количество товара на складе
        );
        storage.put(
                new Item(10, "Яндекс Плюс на 12 месяцев", Category.SOFTWARE, 1990),
                1500 // количество товара на складе
        );
        storage.put(
                new Item(11, "1С: Бухгалтерия 8", Category.SOFTWARE, 2530),
                50 // количество товара на складе
        );
        storage.put(
                new Item(12, "Лаборатория Касперского Kaspersky Premium", Category.SOFTWARE, 2039),
                70 // количество товара на складе
        );
    }

    public Item getItemById(int id) {
        Item item = storage.keySet().stream().filter(x -> x.getId() == id).findFirst().orElseThrow();
        int count = storage.get(item);
        if (count > 0) {
            count -= 1;
            storage.put(item, count);
            return item;
        }
        throw new NoSuchElementException("Товар закончился");

    }

    public int getCountForItem(Item i) {
        return storage.get(i);
    }
}