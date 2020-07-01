package com.lessons.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("com.lessons.controllers.WelcomeController")
public class WelcomeController {

    private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);


    /**********************************************************************
     * showDefaultPage()
     *
     * If the user connects to http://localhost:8080/app1 or http://localhost:8080/app1/page/...
     * Then take the user to the main landing page
     *
     * This is needed to ensure that page refreshes keep the user in the single-page-app
     ***********************************************************************/
    @RequestMapping(value = {"/", "/page/**"}, method = RequestMethod.GET, produces = "text/plain")
    public String showDefaultPage()
    {
        logger.debug("showDefaultPage() started");

        // Forward the user to the main page
        return "forward:/app.html";
    }

    /**********************************************************************
     * simulateError()
     *
     * The user browsed to the /error page
     * -- Throw a RuntimeException
     ***********************************************************************/
    @RequestMapping("/error2")
    public ModelAndView simulateError(Model aModel )
    {
        logger.debug("simulateError() started");

        // Create a modelAndView object
        ModelAndView mav = new ModelAndView();

        // Show the welcome.jsp page
        mav.setViewName("welcome.jsp");

        int x=5;

        if (x==5)
        {
            // Throw a runtime exception
            throw new RuntimeException("There's a snake in my boot");
        }


        logger.debug("simulateError() finished");
        return mav;
    }


}
