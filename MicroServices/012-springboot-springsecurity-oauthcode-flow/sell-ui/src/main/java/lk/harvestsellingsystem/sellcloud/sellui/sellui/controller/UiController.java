package lk.harvestsellingsystem.sellcloud.sellui.sellui.controller;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableOAuth2Sso
public class UiController {

    @RequestMapping(value = "/")
    public String loadUi() {
        return "home";
    }

    @RequestMapping(value = "/secure")
    public String loadSecuredUi() {
        return "secure";
    }
}
