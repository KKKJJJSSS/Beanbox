package Beanbox.Beanbox.controller;

import Beanbox.Beanbox.dto.CartDto;
import Beanbox.Beanbox.dto.RecipeDto;
import Beanbox.Beanbox.dto.UserDto;
import Beanbox.Beanbox.model.CartMapper;
import Beanbox.Beanbox.model.RecipeMapper;
import Beanbox.Beanbox.service.CartService;
import Beanbox.Beanbox.service.RecipeService;
import Beanbox.Beanbox.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class UserController {
    private final CartMapper cartMapper;

    private final RecipeMapper recipeMapper;

    private final UserService userService;

    private final RecipeService recipeService;

    private final CartService cartService;


    public UserController(CartMapper cartMapper, RecipeMapper recipeMapper, UserService userService, RecipeService recipeService, CartService cartService) {
        this.cartMapper = cartMapper;
        this.recipeMapper = recipeMapper;
        this.userService = userService;
        this.recipeService = recipeService;
        this.cartService = cartService;
    }

    @PostMapping("/register")
    @ResponseBody
    public Map<String, String> signup(@RequestBody UserDto user) {
        return userService.signup(user);
    }

    @PostMapping("/login")
    @ResponseBody
    public Map<String, String> signin(@RequestBody UserDto user) {
        return userService.signin(user);
    }

    @GetMapping("/cart")
    public String cart(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "login";
        } else {
            List<CartDto> cartList = cartService.getCartList();
            List<RecipeDto> recipeList = recipeService.getRecipeList();

            model.addAttribute("cartList", cartList);
            model.addAttribute("recipeList", recipeList);
            model.addAttribute("username", username);
        }

        return "cart";
    }

    @GetMapping("/menu")
    public String checkMenu(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "login";
        }

        List<RecipeDto> recipeList = recipeService.getRecipeList();

        model.addAttribute("recipeList", recipeList);
        model.addAttribute("username", username);

        return "menu";
    }


    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addToCart(HttpSession session, @RequestBody String coffeeName) {
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        ResponseEntity<String> response;

        if (cartService.isCoffeeAlreadyAdded(username, coffeeName)) {
            response = new ResponseEntity<>("이미 추가된 상품입니다.", HttpStatus.CONFLICT);
        } else {
            cartService.addToCart(coffeeName, username);
            response = new ResponseEntity<>("상품이 성공적으로 장바구니에 추가되었습니다.", HttpStatus.OK);
        }

        return response;
    }


    @RequestMapping(value = "remove-from-cart", method = RequestMethod.POST)
    public ResponseEntity<String> deleteCoffeeBean(@RequestParam("cart_number") int cartNumber) {
        int deletedRowCount = cartService.deleteCoffeeBeanByCartNumber(cartNumber);

        if (deletedRowCount > 0) {
            return ResponseEntity.ok("삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 항목을 찾을 수 없습니다.");
        }
    }

    @GetMapping("/cartrecipe")
    public String getRecipeList(@RequestParam("img_number") String imgNumber, Model model) {
        List<RecipeDto> recipeList = recipeService.getRecipeList();

        model.addAttribute("recipeList", recipeList);
        model.addAttribute("img_number", imgNumber);
        return "cartrecipe";
    }
}
