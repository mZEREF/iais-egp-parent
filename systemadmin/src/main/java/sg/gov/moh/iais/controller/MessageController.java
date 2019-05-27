package sg.gov.moh.iais.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    @author yichen_guo@ecquaria.com

 */

public class MessageController {
//
//    @Autowired
//    private MessageService messageService;
//
//
//    //@ApiImplicitParam(name  = "SearchConditionDTO", value = "Params Name: type, messagetype, module", required = true)
//    @RequestMapping(value = "message", consumes = MediaType.APPLICATION_JSON_VALUE, method={RequestMethod.POST})
//    public static void listMessageByParams(@RequestBody @Required SearchConditionDTO searchCondition){
//        /*if (searchCondition == null)
//            return ResponseEntity.badRequest().build();
//
//
//        return ResponseEntity.ok(messageService.listMessageBySearchCondition(searchCondition));*/
//    }
//
//    @RequestMapping(value = "description", consumes = MediaType.APPLICATION_JSON_VALUE, method={RequestMethod.POST})
//
//    public ResponseEntity<String> updateDescriptionByCodeId(@RequestBody @Required String requestBody){
//
//        JSONObject json = JSONObject.parseObject(requestBody);
//        String codeId = String.valueOf(json.get("codeId"));
//        String description = String.valueOf(json.get("description"));
//
//        if (StringUtils.isEmpty(codeId))
//            return ResponseEntity.badRequest().build();
//
//        service.updateMessageByCodeId(codeId, description);
//
//        return  ResponseEntity.ok().build();*/
//    }
}
