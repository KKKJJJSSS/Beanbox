package Beanbox.Beanbox.service;

import Beanbox.Beanbox.dto.RecipeDto;
import Beanbox.Beanbox.model.RecipeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    private final RecipeMapper recipeMapper;

    public RecipeService(RecipeMapper recipeMapper) {
        this.recipeMapper = recipeMapper;
    }

    public List<RecipeDto> getRecipeList() {
        return recipeMapper.getRecipeList();
    }

    public int deleteRecipe(int recipeId) {
        return recipeMapper.deleteRecipe(recipeId);
    }
}