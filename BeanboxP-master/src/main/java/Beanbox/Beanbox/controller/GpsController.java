package Beanbox.Beanbox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GpsController {
    @GetMapping("/gps")
    public String Gps() {
        return "gps";
    }
    @GetMapping("/gps/word")
    public String getGpsWord(@RequestParam("search_word") String search_word, Model model) {
        model.addAttribute("search_word", search_word);
        return "gps";
    }
}
