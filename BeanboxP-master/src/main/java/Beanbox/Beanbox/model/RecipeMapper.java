package Beanbox.Beanbox.model;

import Beanbox.Beanbox.dto.RecipeDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RecipeMapper {
    @Select("SELECT * FROM recipe")
    List<RecipeDto> getRecipeList();

    @Delete("DELETE FROM recipe WHERE recipe_id = #{recipe_id}")
    Integer deleteRecipe(@Param("recipe_id") int recipeId);
}
