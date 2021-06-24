package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.*;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: jiahao
 * @Date: 2019/11/27 10:06
 */
@Slf4j
public class InspectionCheckListItemValidate implements CustomizeValidator {
    private static final String ERR0010 = "GENERAL_ERR0006";
    private static final String SUBMIT_CHECK_LIST = "sumbit_check_list_pend_ins";
    private static final String MESSAGE_TAG_DRAFT = "Draft" ;
    public static final String NEXT_ACTION = "next";
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        setSubmitCheckList(request);
        fillUpVad(request,errMap);
        WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
        ParamUtil.setSessionAttr(request,SUBMIT_CHECK_LIST,null);
        return errMap;
    }

    private void setSubmitCheckList(HttpServletRequest request){
        String doSubmitAction = ParamUtil.getString(request,"doSubmitAction");
        if(!NEXT_ACTION.equalsIgnoreCase(doSubmitAction)){
            List<OrgUserDto> orgUserDtoUsers = (List<OrgUserDto>) ParamUtil.getSessionAttr(request,"inspectorsParticipant");
            if(!IaisCommonUtils.isEmpty(orgUserDtoUsers) && orgUserDtoUsers.size() > 1){
                ParamUtil.setSessionAttr(request,SUBMIT_CHECK_LIST,IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
            }
        }
    }

    private void fillUpVad(HttpServletRequest request, Map<String, String> errMap) {
        if(AppConsts.YES.equalsIgnoreCase((String) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE))){
            if(!specServiceFillUpVad(request, errMap)){
                errMap.put("fillchkl","UC_INSTA004_ERR009");
            }
        }
        if(!( serviceFillUpVad(request, errMap)& adhocFillUpVad(request, errMap)&commFillUpVad(request, errMap))){
            errMap.put("fillchkl","UC_INSTA004_ERR009");
        }
    }

    private boolean  specServiceFillUpVad(HttpServletRequest request,Map<String, String> errMap){
        List<InspectionSpecServiceDto> fDtosDtos =( List<InspectionSpecServiceDto>) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS);
        if(IaisCommonUtils.isNotEmpty( fDtosDtos)){
            boolean  noError = true;
            for(InspectionSpecServiceDto inspectionSpecServiceDto : fDtosDtos){
                if( !adhocSpecFillUpVad(request,errMap,inspectionSpecServiceDto) | !serviceSpecFillUpVad(request,errMap,inspectionSpecServiceDto)){
                    if(noError){
                        noError = false;
                        ParamUtil.setSessionAttr(request,"errorTab","ServiceInfo"+inspectionSpecServiceDto.getIdentify());
                    }
                }
            }
            return noError;
        }
        return true;
    }

    private boolean adhocSpecFillUpVad(HttpServletRequest request,Map<String, String> errMap,InspectionSpecServiceDto inspectionSpecServiceDto){
        if(inspectionSpecServiceDto.getAdchklDto() != null && IaisCommonUtils.isNotEmpty(inspectionSpecServiceDto.getAdchklDto().getAdItemList())){
            List<AdhocNcCheckItemDto> itemDtoList = inspectionSpecServiceDto.getAdchklDto().getAdItemList();
            if(itemDtoList!=null && !itemDtoList.isEmpty()){
                String draftTag =  getErrorMessageDraftStringTag(request);
                boolean isError = true;
                String identify = inspectionSpecServiceDto.getIdentify();
                for(AdhocNcCheckItemDto temp:itemDtoList){
                    String  prefix = temp.getId() + identify + draftTag;
                    if(StringUtil.isEmpty(temp.getAdAnswer())){
                        errMap.put(prefix+"adhoc",MessageUtil.replaceMessage(ERR0010,"Yes No N/A","field"));
                        if(isError)
                            isError = false;
                    } else if(!"Yes".equalsIgnoreCase(temp.getAdAnswer()) && StringUtil.isEmpty(temp.getRemark())){
                        errMap.put(prefix +"adhoc",MessageUtil.replaceMessage(ERR0010,"Remark","field"));
                        if(isError)
                            isError = false;
                    }else if(!"Yes".equalsIgnoreCase(temp.getAdAnswer()) && StringUtil.isEmpty(temp.getNcs())){
                        errMap.put(prefix +"adhoc",MessageUtil.replaceMessage(ERR0010,"Findings/NCs","field"));
                        if(isError)
                            isError = false;
                    }
                }
                return  isError;
            }
        }
        return true;
    }
    private boolean serviceSpecFillUpVad(HttpServletRequest request,Map<String, String> errMap,InspectionSpecServiceDto inspectionSpecServiceDto){
        if(!IaisCommonUtils.isEmpty(inspectionSpecServiceDto.getFdtoList())){
            int flagNum = 0;
            if(!IaisCommonUtils.isEmpty(inspectionSpecServiceDto.getFdtoList())){
                for(InspectionFillCheckListDto fDto:inspectionSpecServiceDto.getFdtoList()){
                    if(!fillServiceVad(request,fDto,errMap)){
                        flagNum++;
                    };
                }
            }
            if(flagNum>0){
                return false;
            }
        }
        return true;
    }

    private boolean serviceFillUpVad(HttpServletRequest request,Map<String, String> errMap) {
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,"serListDto");
        int flagNum = 0;
        if(serListDto!=null&&!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
            for(InspectionFillCheckListDto fDto:serListDto.getFdtoList()){
                if(!fillServiceVad(request,fDto,errMap)){
                    flagNum++;
                };
            }
        }
        if(flagNum>0){
            ParamUtil.setSessionAttr(request,"errorTab","ServiceInfo");
            return false;
        }
        return true;
    }

    private boolean commFillUpVad(HttpServletRequest request,Map<String, String> errMap) {
        InspectionFillCheckListDto icDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"commonDto");
        if (icDto != null) {
            boolean isError = true;
            List<InspectionCheckQuestionDto> cqDtoList = icDto.getCheckList();
            if(cqDtoList!=null && !cqDtoList.isEmpty()){
                for(InspectionCheckQuestionDto temp:cqDtoList){
                    temp = getTempByMap(request,temp);
                    String draftTag =  getErrorMessageDraftStringTag(request);
                    String prefix = StringUtil.getNonNull(temp.getSectionNameShow()) + temp.getItemId()+draftTag;
                    if( !(StringUtil.isEmpty(temp.getChkanswer()) && StringUtil.isEmpty(temp.getRemark()) && StringUtil.isEmpty(temp.getNcs()))){
                        if(StringUtil.isEmpty(temp.getChkanswer())){
                            if( !StringUtil.isEmpty(temp.getRemark()) || !StringUtil.isEmpty(temp.getNcs())){
                                errMap.put( prefix+"com",MessageUtil.replaceMessage(ERR0010,"Yes No N/A","field"));
                                if(isError){
                                    isError = false;
                                }
                            }
                        }else if(!"Yes".equalsIgnoreCase(temp.getChkanswer())){
                            if(StringUtil.isEmpty(temp.getRemark())){
                                errMap.put( prefix+draftTag+"comRemark",MessageUtil.replaceMessage(ERR0010,"Remarks","field"));
                                if(isError){
                                    isError = false;
                                }
                            }
                            if(StringUtil.isEmpty(temp.getNcs())){
                                errMap.put( prefix+draftTag+"comFindNcs",MessageUtil.replaceMessage(ERR0010,"Findings/NCs","field"));
                                if(isError){
                                    isError = false;
                                }
                            }
                        }
                    }else if(StringUtil.isEmpty(temp.getChkanswer())){
                        errMap.put( prefix+"com",MessageUtil.replaceMessage(ERR0010,"Yes No N/A","field"));
                        if(isError){
                            isError = false;
                        }
                    }
                }
                if( !isError){
                    ParamUtil.setSessionAttr(request,"errorTab","General");
                }
            }
            return isError;
        }
        return true;
    }

    public boolean fillServiceVad(HttpServletRequest request,InspectionFillCheckListDto fDto,Map<String, String> errMap){
        List<InspectionCheckQuestionDto> cqDtoList = fDto.getCheckList();
        if(!IaisCommonUtils.isEmpty(cqDtoList)){
            boolean isError = true;
            String draftTag =  getErrorMessageDraftStringTag(request);
            for(InspectionCheckQuestionDto temp:cqDtoList){
                temp = getTempByMap(request,temp);
                String prefix = fDto.getSubName() + StringUtil.getNonNull(temp.getSectionNameShow()) + temp.getItemId()+draftTag;
                if( !(StringUtil.isEmpty(temp.getChkanswer()) && StringUtil.isEmpty(temp.getRemark()) && StringUtil.isEmpty(temp.getNcs()))){
                    if(StringUtil.isEmpty(temp.getChkanswer())){
                        if( !StringUtil.isEmpty(temp.getRemark()) || !StringUtil.isEmpty(temp.getNcs())) {
                            errMap.put(prefix, MessageUtil.replaceMessage(ERR0010, "Yes No N/A", "field"));
                            if (isError){
                                isError = false;
                            }
                        }
                    }else if(!"Yes".equalsIgnoreCase(temp.getChkanswer())){
                        if(StringUtil.isEmpty(temp.getRemark())){
                            errMap.put(prefix+"Remark",MessageUtil.replaceMessage(ERR0010,"Remarks","field"));
                            if(isError){
                                isError = false;
                            }
                        }

                        if(StringUtil.isEmpty(temp.getNcs())){
                            errMap.put(prefix+"FindNcs",MessageUtil.replaceMessage(ERR0010,"Findings/NCs","field"));
                            if(isError){
                                isError = false;
                            }
                        }
                    }
                }else if(StringUtil.isEmpty(temp.getChkanswer())){
                    errMap.put(prefix+draftTag, MessageUtil.replaceMessage(ERR0010, "Yes No N/A", "field"));
                    if (isError){
                        isError = false;
                    }
                }
            }
            return  isError;
        }
        return true;
    }

    private  String getErrorMessageDraftStringTag(HttpServletRequest request){
        String submitUser = (String) ParamUtil.getSessionAttr(request,SUBMIT_CHECK_LIST);
        if( !StringUtil.isEmpty(submitUser)){
            return submitUser+MESSAGE_TAG_DRAFT;
        }
        return "";
    }

    private InspectionCheckQuestionDto getTempByMap(HttpServletRequest request,InspectionCheckQuestionDto temp){
        String submitUser = (String) ParamUtil.getSessionAttr(request,SUBMIT_CHECK_LIST);
        if( !StringUtil.isEmpty(submitUser)){
            Map<String, AnswerForDifDto> answerForDifDtoMaps = temp.getAnswerForDifDtoMaps();
            if(!IaisCommonUtils.isEmpty(answerForDifDtoMaps)){
                AnswerForDifDto answerForDifDto = answerForDifDtoMaps.get(submitUser);
                if(answerForDifDto != null){
                    String sectionNameShow =  temp.getSectionNameShow();
                    String itemId = temp.getItemId();
                    temp = MiscUtil.transferEntityDto(answerForDifDto,InspectionCheckQuestionDto.class);
                    temp.setChkanswer( answerForDifDto .getAnswer());
                    temp.setItemId(itemId);
                    temp.setSectionNameShow(sectionNameShow);
                }
            }
        }
        return temp;
    }
    private boolean adhocFillUpVad(HttpServletRequest request,Map<String, String> errMap) {
        AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,"adchklDto");
        if(showDto!=null){
            List<AdhocNcCheckItemDto> itemDtoList = showDto.getAdItemList();
            if(itemDtoList!=null && !itemDtoList.isEmpty()){
                boolean isError = true;
                String draftTag =  getErrorMessageDraftStringTag(request);
                for(AdhocNcCheckItemDto temp:itemDtoList){
                 String submitUser = (String) ParamUtil.getSessionAttr(request,SUBMIT_CHECK_LIST);
                 String prefix = temp.getId()+draftTag;
                 if( !StringUtil.isEmpty(submitUser)){
                     Map<String, AnswerForDifDto> answerForDifDtoMaps = temp.getAnswerForDifDtoMaps();
                     if(!IaisCommonUtils.isEmpty(answerForDifDtoMaps)){
                         AnswerForDifDto answerForDifDto = answerForDifDtoMaps.get(submitUser);
                         if(answerForDifDto != null){
                             String id = temp.getId();
                             temp = MiscUtil.transferEntityDto(answerForDifDto,AdhocNcCheckItemDto.class);
                             temp.setId(id);
                             temp.setAdAnswer( answerForDifDto .getAnswer());
                         }
                     }
                 }
                    if(!(StringUtil.isEmpty(temp.getAdAnswer()) && StringUtil.isEmpty(temp.getRemark()) && StringUtil.isEmpty(temp.getNcs()))){
                        if(StringUtil.isEmpty(temp.getAdAnswer())){
                            if( !StringUtil.isEmpty(temp.getRemark()) || !StringUtil.isEmpty(temp.getNcs())){
                                errMap.put(prefix+"adhoc",MessageUtil.replaceMessage(ERR0010,"Yes No N/A","field"));
                                if(isError){
                                    isError = false;
                                }
                            }

                        } else if(!"Yes".equalsIgnoreCase(temp.getAdAnswer())){
                            if(StringUtil.isEmpty(temp.getRemark())){
                                errMap.put(prefix+"adhocRemark",MessageUtil.replaceMessage(ERR0010,"Remarks","field"));
                                if(isError){
                                    isError = false;
                                }
                            }
                            if(StringUtil.isEmpty(temp.getNcs())){
                                errMap.put(prefix+"adhocFindNcs",MessageUtil.replaceMessage(ERR0010,"Findings/NCs","field"));
                                if(isError){
                                    isError = false;
                                }
                            }
                        }
                    }else if(StringUtil.isEmpty(temp.getAdAnswer())){
                        errMap.put(prefix+"adhoc",MessageUtil.replaceMessage(ERR0010,"Yes No N/A","field"));
                        if(isError){
                            isError = false;
                        }
                    }
                }
                if(!isError){
                    ParamUtil.setSessionAttr(request,"errorTab","ServiceInfo");
                }
                return  isError;
            }
        }
        return true;
    }


}
