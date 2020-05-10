package ch.zhaw.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Forward not already mapped requests to the SPA.
 * So no "Whitelabel Error Page" should be served anymore.
 */
@Controller
public class SpaErrorController implements ErrorController {

    @RequestMapping("/error")
    public Object handleError(HttpServletRequest request, HttpServletResponse response) {
        if (request.getMethod().equalsIgnoreCase(HttpMethod.GET.name())) {
            String forwardUrl = "forward:/index.html";
            response.setStatus(HttpStatus.OK.value());
            return forwardUrl;
        } else {
            return ResponseEntity.notFound().build(); // REST HTTP ERROR 404
        }
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
