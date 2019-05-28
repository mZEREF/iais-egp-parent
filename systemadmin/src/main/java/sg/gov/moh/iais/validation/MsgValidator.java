package sg.gov.moh.iais.validation;

import sg.gov.moh.iais.constant.MessageConstant;

import java.util.Arrays;
import java.util.List;

public class MsgValidator {

    public boolean validCodeId(Integer id){

        return true;
    }

    public boolean validModule(String module){
        if(module.length()< 0 || module.length()> 50) return false;

        List<String> str = Arrays.asList("New", "Renewal", "Cessation", "Amendment", "Reinstate", "Audit", "Common", "Others");
        for (int i = 0; i < str.size(); i++){
            if(str.get(i).equals(i)){
                return true;
            }
        }
        return false;
    }

    public static boolean validType(String type){
        if (type.length() < 0 || type.length() > 5){
            return false;
        }

        boolean is;
        switch (type){
            case MessageConstant
                    .TYPE_INTER :
                is = true;
                break;
            case MessageConstant
                    .TYPE_INTRA :
                is = true;
                break;
            default:
                    is = false;
        }
        return is;
    }

    public boolean validMessageType(String msg){
        if (msg.length() < 0 || msg.length() > 25){
            return false;
        }

        boolean is;
        switch (msg){
            case MessageConstant.MESSAGETYPE_ACKNOWLEDGEMENT :
                is = true;
                break;
            case MessageConstant.MESSAGETYPE_ERROR :
                is = true;
                break;
            default:
                is = false;
        }
        return is;
    }

    public boolean validStatus(Character status){
        boolean is;
        switch (status){
            case MessageConstant.STATUS_ACTIVE :
                is = true;
                break;
            case MessageConstant.STATUS_DEACTIVATED:
                is = true;
                break;
            default:
                is = false;
        }
        return is;
    }
}
