package ngthu.com.Laptop_shop.service;

import ngthu.com.Laptop_shop.model.Category;

import java.util.List;

public interface CategoryService {

    public  Boolean existCategory(String name);

    public Category saveCategory(Category category);

    public List<Category> getAllCategory();

    public Boolean deleteCategory(int id);

    public Category getCategoryById(int id);

    public  List<Category> getAllActiveCategory();

}
