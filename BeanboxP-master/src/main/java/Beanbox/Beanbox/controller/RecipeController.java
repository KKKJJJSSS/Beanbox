package Beanbox.Beanbox.controller;

import Beanbox.Beanbox.dto.RecipeDto;
import Beanbox.Beanbox.model.RecipeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller // 해당 클래스가 Spring의 Controller임을 선언
public class RecipeController {
    public final RecipeMapper recipeMapper;


    @Autowired
    public RecipeController(RecipeMapper recipeMapper ) {
        this.recipeMapper = recipeMapper;
    }

    @GetMapping("/recipe")
    public String getRecipeList(@RequestParam("filename") String filename, @RequestParam("beanName") String beanName, Model model) {
        List<RecipeDto> recipeList = recipeMapper.getRecipeList();
        String img_number = beanName + filename;

        model.addAttribute("recipeList", recipeList);
        model.addAttribute("filename", filename);
        model.addAttribute("beanName", beanName);
        model.addAttribute("img_number", img_number);
        return "recipe";
    }
}