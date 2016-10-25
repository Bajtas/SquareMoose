package pl.bajtas.squaremoose.api.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Bajtas on 22.10.2016.
 */
@Controller
public class LoginController {

    @RequestMapping("/dashboard/**")
    public String dashboard(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "main";
    }

//    @RequestMapping("/dashboard/login")
//    public String loginForm() {
//        return "main";
//    }

//    @RequestMapping("/dashboard/app")
//    public String app() {
//        return "main";
//    }
}
