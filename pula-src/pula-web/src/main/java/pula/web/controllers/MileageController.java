/**
 * 
 */
package pula.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Liangfei
 *
 */
@Controller
public class MileageController {

    @RequestMapping
    public ModelAndView entry() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("mileage");
        return mav;
    }
}
