package com.ecquaria.cloud.moh.iais.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * SampleAjaxController
 *
 * @author Jinhua
 * @date 2019/10/10 9:15
 */
@Controller
@RequestMapping("/orgUser/*")
public class SampleAjaxController {
    @RequestMapping(value="doSomething.do")
    public @ResponseBody Map<String,Object> doSomething(HttpServletRequest request,
                                                  HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<String,Object>();

        map.put("aaa", "ccc");

        return map;
    }

}
