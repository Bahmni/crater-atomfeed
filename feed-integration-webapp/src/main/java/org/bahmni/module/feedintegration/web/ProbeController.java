package org.bahmni.module.feedintegration.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ProbeController {
    @RequestMapping(value = "/health-check", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> taskStatus() {
        HashMap<String, String> message = new HashMap<String, String>() {{
            put("message", "Hello World!");
        }};
        return message;
    }

}
