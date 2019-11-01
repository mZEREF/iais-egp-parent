package com.ecquaria.cloud.moh.iais.controller;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.PostCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Delegator
@Slf4j
public class PostCodeDelegator {
    private   String postCodePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "file/POSTCODE.TXT" ;
    private   String streetsPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "file/STREETS.TXT";
    private   String buildingPath= Thread.currentThread().getContextClassLoader().getResource("").getPath() + "file/BUILDING.TXT";

    @Autowired
    private PostCodeService postCodeService;

    public void importPostCode(BaseProcessClass bpc) throws IOException {
        Map<String,String> streetMap = initstreetMap();
        Map<String,String> buildingMap =initbuildingMap();
        List<PostCodeDto> list = convert(streetMap,buildingMap);
        if(!list.isEmpty()){
            postCodeService.createAll(list);
        }
        bpc.request.setAttribute("success","success");
    }

    private Map<String,String> initstreetMap() throws IOException {
        Map<String,String> streetMap = new HashMap<>();
        try(BufferedReader  br = new BufferedReader(new FileReader(streetsPath));){
            String line = null;
            String key = null;
            String value = null;
            while ((line = br.readLine()) != null){
                if(!StringUtils.isEmpty(line) && line.trim().length() > 0){
                    key = line.substring(0,7).trim();
                    value = line.substring(7,39).trim();
                    streetMap.put(key, value);
                }else{
                    break;
                }
            }
        }catch (Exception e){
          log.error(StringUtil.changeForLog(e.getMessage()),e);
        }
        return streetMap;
    }
    private Map<String,String> initbuildingMap() throws IOException {
        Map<String,String> buildingMap = new HashMap<>();
        try (BufferedReader  br = new BufferedReader(new FileReader(buildingPath)); ){
            String line = null;
            String key = null;
            String value = null;
            //building
            while ((line = br.readLine()) != null){
                if(!StringUtils.isEmpty(line) && line.trim().length() > 0){
                    key = line.substring(0,6).trim();
                    value = line.substring(6,51).trim();
                    buildingMap.put(key, value);
                }else{
                    break;
                }
            }
        }catch (Exception e){
            log.error(StringUtil.changeForLog(e.getMessage()),e);
        }
        return buildingMap;
    }


    /**
     * read file, use inited Map convert data to SingpostAddress
     */
    private  List<PostCodeDto> convert(Map<String,String> streetMap,Map<String,String> buildingMap) throws IOException {
        List<PostCodeDto> list = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(postCodePath));){
            String line = null;
            String postalCode = null;
            String addressType = null;
            String blkHseNo = null;
            String streetKey = null;
            String buildingKey = null;

            while ((line = br.readLine()) != null) {
                if (!StringUtils.isEmpty(line) && line.trim().length() > 0) {
                    postalCode = line.substring(0, 6).trim();
                    addressType = line.substring(6, 7).trim();
                    blkHseNo = line.substring(7, 14).trim();
                    streetKey = line.substring(14, 21).trim();
                    buildingKey = line.substring(21).trim();
                    PostCodeDto sa = new PostCodeDto();
                    sa.setPostalCode(postalCode);
                    sa.setAddressType(addressType);
                    sa.setBlkHseNo(blkHseNo);
                    sa.setStreetName(streetMap.get(streetKey));
                    sa.setBuildingName(buildingMap.get(buildingKey));
                    list.add(sa);
                } else {
                    break;
                }
            }
        } catch (Exception e){
            log.error(StringUtil.changeForLog(e.getMessage()),e);
        }
         return list;
    }
}
