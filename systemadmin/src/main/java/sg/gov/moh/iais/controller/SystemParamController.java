package sg.gov.moh.iais.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.gov.moh.iais.service.SystemParamService;

/*
    @author yichen_guo@ecquaria.com

 */

public class SystemParamController {

    @Autowired
    private SystemParamService service;

    /*@ApiOperation(value = "list params")
    @GetMapping(value = "params")
    @LogInfo(action = "view")
    public ResponseEntity<List<Map<String, String>>> view(){
        List<Map<String, String>> list = new ArrayList<>();
        service.listSystemParam().forEach(i -> {
            Map<String, String> map = new HashMap<>();
            map.put("id", i.getId());
            map.put("value", i.getValue());
            map.put("description", i.getDescription());
            list.add(map);
        });

        return ResponseEntity.accepted().body(list);
    }

    @ApiOperation(value = "update to System param value by pk id")
    @PostMapping(value = "params/{param}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateParamByPkId(@RequestBody @Required SystemParamDTO param){
        service.updateParam(param.getId(), param.getValue());
    }

    @ApiOperation(value = "insert to new record for system param")
    @PostMapping(value = "param", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void insertRecord(@RequestBody @Required SystemParam sys){
        service.insertRecord(sys);
    }*/
}
