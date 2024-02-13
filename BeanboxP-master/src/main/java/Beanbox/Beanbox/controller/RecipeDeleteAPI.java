package Beanbox.Beanbox.controller;

import Beanbox.Beanbox.service.BeanService;
import Beanbox.Beanbox.service.CartService;
import Beanbox.Beanbox.service.RecipeService;
import Beanbox.Beanbox.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class RecipeDeleteAPI {
    private final RecipeService recipeService;
    private final CartService cartService;
    private final BeanService beanService;
    private final UserService userService;
    private final HttpSession session;


    public RecipeDeleteAPI(RecipeService recipeService, CartService cartService, BeanService beanService, UserService userService, HttpSession session) {
        this.recipeService = recipeService;
        this.cartService = cartService;
        this.beanService = beanService;
        this.userService = userService;
        this.session = session;
    }

    @RequestMapping(value = "remove-from-recipe", method = RequestMethod.POST)
    public ResponseEntity<String> deleteRecipe(@RequestParam("recipe_id") int recipeId, @RequestParam("bean_name") String beanName) {
        // 레시피 삭제
        int deletedRowCount = recipeService.deleteRecipe(recipeId);

        // 장바구니에 담겨 있는 레시피 삭제
        cartService.deleteCoffeeBeanByRecipeId(recipeId);

        // 마지막 원두면 원두 데이터도 삭제
        int countBean = beanService.findByLastBean(beanName);
        if (countBean == 1) {
            beanService.deleteLastBean(beanName);
        }

        if (deletedRowCount > 0) {
            return ResponseEntity.ok("삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 항목을 찾을 수 없습니다.");
        }
    }
}
