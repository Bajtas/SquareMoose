package pl.bajtas.squaremoose.api.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.core.MediaType;

/**
 * Created by Bajtas on 22.10.2016.
 */
@Controller
@RequestMapping("/dashboard")
public class LoginController {
    @RequestMapping(path={"/**"}, produces = {MediaType.TEXT_HTML})
    public String dashboard() {
        return "main";
    }
}
