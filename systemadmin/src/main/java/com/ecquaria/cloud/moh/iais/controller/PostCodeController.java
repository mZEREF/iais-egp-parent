package com.ecquaria.cloud.moh.iais.controller;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.entity.PostCode;
import com.ecquaria.cloud.moh.iais.service.PostCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Delegator
public class PostCodeController {
    private   String postCodePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "file/POSTCODE.TXT" ;
    private   String streetsPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "file/STREETS.TXT";
    private   String buildingPath= Thread.currentThread().getContextClassLoader().getResource("").getPath() + "file/BUILDING.TXT";

    @Autowired
    private PostCodeService postCodeService;

    public void importPostCode() throws IOException {
        Map<String,String> streetMap = initstreetMap();
        Map<String,String> buildingMap =initbuildingMap();
        List<PostCode> list = convert(streetMap,buildingMap);
        if(!list.isEmpty()){
            postCodeService.clean();
            postCodeService.createAll(list);
        }
    }

    private Map<String,String> initstreetMap() throws IOException {
        BufferedReader br = null;
        Map<String,String> streetMap = new HashMap<>();
        try {
        String line = null;
        String key = null;
        String value = null;
        //street
        br = new BufferedReader(new InputStreamReader(new FileInputStream(streetsPath)));
        while ((line = br.readLine()) != null){
            if(!StringUtils.isEmpty(line) && line.trim().length() > 0){
                key = line.substring(0,7).trim();
                value = line.substring(7,39).trim();
                streetMap.put(key, value);
            }else{
                break;
            }
        }
        }finally {
            if(br != null){
                br.close();
            }
        }
        return streetMap;
    }
    private Map<String,String> initbuildingMap() throws IOException {
        BufferedReader br = null;
        Map<String,String> buildingMap = new HashMap<>();
        try {
            String line = null;
            String key = null;
            String value = null;
            //building
            br = new BufferedReader(new InputStreamReader(new FileInputStream(buildingPath)));
            while ((line = br.readLine()) != null){
                if(!StringUtils.isEmpty(line) && line.trim().length() > 0){
                    key = line.substring(0,6).trim();
                    value = line.substring(6,51).trim();
                    buildingMap.put(key, value);
                }else{
                    break;
                }
            }
        }finally {
            if(br != null){
                br.close();
            }
        }
        return buildingMap;
    }


    /**
     * read file, use inited Map convert data to SingpostAddress
     */
    private  List<PostCode> convert(Map<String,String> streetMap,Map<String,String> buildingMap) throws IOException {
        BufferedReader br = null;
        List<PostCode> list = new ArrayList<>();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(postCodePath)));
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
                    PostCode sa = new PostCode();
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
        } finally {
            if (br != null) {
                br.close();
            }
        }
         return list;
    }
}
