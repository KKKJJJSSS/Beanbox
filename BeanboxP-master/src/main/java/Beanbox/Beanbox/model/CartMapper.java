package Beanbox.Beanbox.model;

import Beanbox.Beanbox.dto.CartDto;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CartMapper {
    @Select("SELECT * FROM shopping_cart")
    List<CartDto> getCartList();

    @Select("SELECT coffee_name FROM shopping_cart WHERE username = #{username}")
    List<String> getAllCoffeeNamesForUser(@Param("username") String username);

    @Insert("INSERT INTO shopping_cart(coffee_name, username) VALUES(#{coffeeName}, #{username})")
    void saveToCart(@Param("coffeeName") String coffeeName, @Param("username") String username);

    @Delete("DELETE FROM shopping_cart WHERE cart_number = #{cart_number}")
    Integer deleteCoffeeBeanByCartNumber(@Param("cart_number") int cartNumber);

    @Delete("DELETE FROM shopping_cart WHERE recipe_id = #{recipe_id}")
    Integer deleteCoffeeBeanByRecipeId(@Param("recipe_id") int recipeId);
}
