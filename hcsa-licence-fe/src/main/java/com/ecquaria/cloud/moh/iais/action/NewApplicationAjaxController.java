package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2019/12/5 15:51
 */
@Controller
public class NewApplicationAjaxController {
    @RequestMapping(value = "/request-check-error",method = RequestMethod.POST)
    public @ResponseBody Map<String,String> doVail(HttpServletRequest request, HttpServletResponse response){
        List<String> subList=new ArrayList<>();
        List<String> sumList=new ArrayList<>();
        List<String> idList=new ArrayList<>();
        List<String> parentId=new ArrayList<>();
        Map<String,String> map=new HashMap<>();
        String[] parameterValues = request.getParameterValues("control--runtime--1");
        for(String every:parameterValues){
            if(every.contains("true")){
                subList.add(every);
            }else if(every.contains("false")){
                sumList.add(every);
            }
        }
        for(String every:sumList){
            String[] split = every.split(";");
            if(split.length==3){
                idList.add(split[0]);
            }else if(split.length==4){
                parentId.add(every);
            }
        }
        if(idList.isEmpty()&&!parentId.isEmpty()){
            map.put("errorM","error massage!");
            return map;
        }
        if(!subList.isEmpty()){
            boolean recursion1 = recursion(subList);
            if(!recursion1){
                map.put("errorM","error massage!");
                return map;
            }
        }
        boolean recursion = recursion(parentId);
        if(!recursion){
            map.put("errorM","error massage!");
            return map;
        }
        return map;
    }

    private boolean recursion(List<String> parentId){
        boolean flag=false;
        for(String every:parentId){
            for(String index:parentId){
                if( every.split(";")[3].equals(index.split(";")[0]) ){
                    flag=true;
                }
            }
        }
        return flag;
    }

    @GetMapping(value = "/sg-number-validator")
    @ResponseBody
    public Map<String,String> sgNoValidator(@RequestParam("idType") String idType,@RequestParam("idNumber") String idNumber){
        Map<String,String> map=new HashMap<>();
        if(idNumber==null||"".equals(idNumber)){
            map.put("errorM","cannot be blank!");
        }else {
            boolean aBoolean = false;
            if(idType.equals("fin")){
                aBoolean= SgNoValidator.validateFin(idNumber);
            }else  if(idType.equals("nric")){
              aBoolean = SgNoValidator.validateNric(idNumber);
            }
            if(!aBoolean){
                map.put("errorM","Please key in a valid NRIC/FIN!");
            }

        }

        return map;
    };
}
