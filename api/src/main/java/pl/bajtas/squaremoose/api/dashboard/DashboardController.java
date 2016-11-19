package pl.bajtas.squaremoose.api.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.core.MediaType;

/**
 * Created by Bajtas on 22.10.2016.
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @RequestMapping(path = {"/**"}, produces = {MediaType.TEXT_HTML})
    public String dashboard() {
        return "main";
    }
}
