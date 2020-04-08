package com.golink.ecommerceb2bvendor.Products;

import java.util.ArrayList;
import java.util.List;

public class CategoryItems {

    private String id;
    private String name;
    private List <ProductItems> productItemsList = new ArrayList<>();

    public CategoryItems(String id, String name, List<ProductItems> productItemsList) {
        this.id = id;
        this.name = name;
        this.productItemsList = productItemsList;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ProductItems> getProductItemsList() {
        return productItemsList;
    }
}
