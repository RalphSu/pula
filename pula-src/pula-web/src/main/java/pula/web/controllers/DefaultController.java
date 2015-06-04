package pula.web.controllers;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import puerta.support.service.SessionBox;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;

@RequestMapping
@Controller("defaultConsole")
public class DefaultController {

    @Resource
    private SessionService sessionService;
    @Resource
    ParameterKeeper parameterKeeper;

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView m = new ModelAndView();
        SessionBox sb = sessionService.get();
        if (sb != null) {
            m.addObject("sessionUser", sb);
        }
        m.setViewName("index");
        return m;
    }

}
