package com.ecquaria.cloud.moh.iais.validate.declarationsValidate;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/5/15 14:02
 */
public class DeclarationsUtil {
    public static void declarationsValidate(Collection<Declarations> collection, Map<String,String> map, AppDeclarationMessageDto appDeclarationMessageDto){
        if (collection==null || collection.isEmpty()){
            return;
        }
        Iterator<Declarations> iterator = collection.iterator();
        iterator.forEachRemaining((v)->{
            v.validateDeclarations(map,appDeclarationMessageDto);
        });
    }
}
