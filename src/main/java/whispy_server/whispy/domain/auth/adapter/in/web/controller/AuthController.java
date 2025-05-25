package whispy_server.whispy.domain.auth.adapter.in.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final String OAUTH2_AUTHORIZATION_BASE_URI = "/oauth2/authorization/";

    @GetMapping("/oauth2/{provider}")
    public RedirectView oauth2Login(@PathVariable String provider) {
        return new RedirectView(OAUTH2_AUTHORIZATION_BASE_URI + provider);
    }




}
