package org.lab5.lab5;

import java.util.*;
import java.io.*;

public class ToysCatalog {
    private static ToysCatalog instance;
    private Map<String, Integer> toyPrices;
    private static final String CATALOG_FILE = "catalog.dat";

    private ToysCatalog() {
        toyPrices = new HashMap<>();
        initializeDefaultCatalog();
        loadCatalog();
    }

    public static ToysCatalog getInstance() {
        if (instance == null) {
            instance = new ToysCatalog();
        }
        return instance;
    }

    private void initializeDefaultCatalog() {
        toyPrices.put("car", 150);
        toyPrices.put("lego", 300);
        toyPrices.put("soft-toy", 200);
    }

    public Toys createToy(String size, String type) {
        if (toyPrices.containsKey(type)) {
            return new Toys(size, type, toyPrices.get(type));
        }
        System.out.println("Тип игрушки '" + type + "' не найден в каталоге.");
        return null;
    }

    public void addToyType(String type, int basePrice) {
        toyPrices.put(type, basePrice);
        saveCatalog();
    }

    public boolean modifyToyPrice(String type, int newPrice) {
        if (toyPrices.containsKey(type)) {
            toyPrices.put(type, newPrice);
            saveCatalog();
            return true;
        }
        return false;
    }

    public boolean removeToyType(String type) {
        Integer removed = toyPrices.remove(type);
        if (removed != null) {
            saveCatalog();
            return true;
        }
        return false;
    }

    public void showCatalog() {
        System.out.println("=== КАТАЛОГ ИГРУШЕК ===");
        for (Map.Entry<String, Integer> entry : toyPrices.entrySet()) {
            System.out.println("Тип: " + entry.getKey() + " | Базовая цена: " + entry.getValue());
        }
    }

    public Map<String, Integer> getAllToyTypes() {
        return new HashMap<>(toyPrices);
    }

    private void saveCatalog() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CATALOG_FILE))) {
            oos.writeObject(toyPrices);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения каталога: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadCatalog() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CATALOG_FILE))) {
            Map<String, Integer> loaded = (Map<String, Integer>) ois.readObject();
            toyPrices.putAll(loaded);
        } catch (IOException | ClassNotFoundException e) {
            // Файл не существует или поврежден - используем дефолтный каталог
            saveCatalog();
        }
    }
}


