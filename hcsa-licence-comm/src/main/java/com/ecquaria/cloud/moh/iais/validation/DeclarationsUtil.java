package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.validation.decaration.DeclarationOnBankruptcy;
import com.ecquaria.cloud.moh.iais.validation.decaration.DeclarationOnCompetencies;
import com.ecquaria.cloud.moh.iais.validation.decaration.DeclarationOnCriminalRecords;
import com.ecquaria.cloud.moh.iais.validation.decaration.GeneralAccuracyDeclaration;
import com.ecquaria.cloud.moh.iais.validation.decaration.PreliminaryQuestion;
import com.ecquaria.cloud.moh.iais.validation.decaration.Statements;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/5/15 14:02
 */
public class DeclarationsUtil {
    private final static List<Declarations> RFC_LIST=new ArrayList<>(2);
    private final static List<Declarations> RENEW_LIST=new ArrayList<>(5);
    private final static List<Declarations> NEW_LIST=new ArrayList<>(5);
    static {
        RFC_LIST.add(new PreliminaryQuestion());
        RFC_LIST.add(new Statements());
        /*---------------------*/
        RENEW_LIST.add(new PreliminaryQuestion());
        RENEW_LIST.add(new DeclarationOnCriminalRecords());
        RENEW_LIST.add(new DeclarationOnCompetencies());
        RENEW_LIST.add(new DeclarationOnBankruptcy());
        RENEW_LIST.add(new GeneralAccuracyDeclaration());
        /*-----------------------*/
        NEW_LIST.add(new PreliminaryQuestion());
        NEW_LIST.add(new DeclarationOnCriminalRecords());
        NEW_LIST.add(new DeclarationOnCompetencies());
        NEW_LIST.add(new DeclarationOnBankruptcy());
        NEW_LIST.add(new GeneralAccuracyDeclaration());
    }
    public static void declarationsValidate(Collection<Declarations> collection, Map<String,String> map, AppDeclarationMessageDto appDeclarationMessageDto){
        if (collection==null || collection.isEmpty()){
            return;
        }
        Iterator<Declarations> iterator = collection.iterator();
        iterator.forEachRemaining((v)->{
            v.validateDeclarations(map,appDeclarationMessageDto);
        });
    }
    public static void declarationsValidate(Map<String,String> map, AppDeclarationMessageDto appDeclarationMessageDto,String type){
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(type)){
            declarationsValidate(RFC_LIST,map,appDeclarationMessageDto);
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(type)){
            declarationsValidate(RENEW_LIST,map,appDeclarationMessageDto);
        }else if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(type)){
            declarationsValidate(NEW_LIST,map,appDeclarationMessageDto);
        }
    }
}
