package com.golink.ecommerceb2bvendor;

import java.util.ArrayList;
import java.util.List;

public class CategoryItems {

    private String id;
    private String name;
    private String vendor;
    private List <ProductItems> productItemsList = new ArrayList<>();

    public CategoryItems(String id, String name, String vendor, List<ProductItems> productItemsList) {
        this.id = id;
        this.name = name;
        this.vendor = vendor;
        this.productItemsList = productItemsList;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVendor() {
        return vendor;
    }

    public List<ProductItems> getProductItemsList() {
        return productItemsList;
    }
}
