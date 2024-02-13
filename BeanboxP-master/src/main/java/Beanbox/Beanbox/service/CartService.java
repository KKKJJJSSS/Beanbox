package Beanbox.Beanbox.service;

import Beanbox.Beanbox.dto.CartDto;
import Beanbox.Beanbox.dto.RecipeDto;
import Beanbox.Beanbox.model.CartMapper;
import Beanbox.Beanbox.model.RecipeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final CartMapper cartMapper;

    public CartService(CartMapper cartMapper, RecipeMapper recipeMapper) {
        this.cartMapper = cartMapper;
    }

    public List<CartDto> getCartList() {
        return cartMapper.getCartList();
    }

    public boolean isCoffeeAlreadyAdded(String username, String coffeeName) {
        List<String> allCoffeeNames = cartMapper.getAllCoffeeNamesForUser(username);
        return allCoffeeNames.contains(coffeeName);
    }

    public void addToCart(String coffeeName, String username) {
        cartMapper.saveToCart(coffeeName, username);
    }

    public int deleteCoffeeBeanByCartNumber(int cartNumber) {
        return cartMapper.deleteCoffeeBeanByCartNumber(cartNumber);
    }

    public int deleteCoffeeBeanByRecipeId(int recipeId) {
        return cartMapper.deleteCoffeeBeanByRecipeId(recipeId);
    }
}

