<input type="hidden" name="crud_action_value_ar_stage" id="crud_action_value_ar_stage" value="-2"/>
<input type="hidden" name="crud_action_value_valiate_donor" id="crud_action_value_valiate_donor" value="-1"/>
<input type="hidden" name="crud_action_value_action_age" id="crud_action_value_action_age" value="-1"/>
<div class="panel panel-default usedDonorOocyteControlClass">
     <div class="panel-heading">
         <h4  class="panel-title" >
        <a href="#donorDtoDetails" data-toggle="collapse" >
            Details of Donor(s)
          </a>
       </h4>
    </div>
     <div id="donorDtoDetails" class="panel-collapse collapse in">
         <div class="panel-body">
             <input type="hidden" name="crud_action_value_donor_size" id="crud_action_value_donor_size" value="${donorDtos.size()}"/>
         <c:forEach items="${donorDtos}" var="donorDto">
             <c:set var="arDonorIndex" value="${donorDto.arDonorIndex}"/>
             <div class="panel-main-content form-horizontal">

                 <iais:row >
                     <iais:field width="6" cssClass="col-md-6" value="Donor ${donorDto.arDonorIndex+1}" />
                     <iais:value width="5" cssClass="col-md-5" display="true"/>
                     <iais:value width="2" cssClass="col-md-2" display="true">
                         <c:if test="${donorDto.arDonorIndex >0}">
                             <h4 class="text-danger">
                                 <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer" class="deleteDonor" id="deleteDonor${arDonorIndex}" onclick="deleteDonor('${arDonorIndex}')"></em>
                             </h4>
                         </c:if>
                     </iais:value>
                 </iais:row>

                 <iais:row>
                     <iais:field width="6" cssClass="col-md-6" value="Is this a Directed Donation?" mandatory="true"/>
                     <div class="col-md-6">
                     <iais:value width="3" cssClass="col-md-6">
                         <div class="form-check" style="padding-left: 0px;" onchange="showDonor('${arDonorIndex}')">
                             <input class="form-check-input"
                                    type="radio"
                                    name="directedDonation${arDonorIndex}"
                                    value="1"
                                    id="directedDonation${arDonorIndex}RadioYes"
                                    <c:if test="${donorDto.directedDonation}">checked</c:if>
                                    aria-invalid="false">
                             <label class="form-check-label"
                                    for="directedDonation${arDonorIndex}RadioYes"><span
                                     class="check-circle"></span>Yes</label>
                         </div>
                     </iais:value>
                     <iais:value width="3" cssClass="col-md-6">
                         <div class="form-check" style="padding-left: 0px;" onchange="hideDonor('${arDonorIndex}')">
                             <input class="form-check-input" type="radio"
                                    name="directedDonation${arDonorIndex}"
                                    value="0"
                                    id="directedDonation${arDonorIndex}RadioNo"
                                    <c:if test="${donorDto.directedDonation eq false}">checked</c:if>
                                    aria-invalid="false">
                             <label class="form-check-label"
                                    for="directedDonation${arDonorIndex}RadioNo" ><span
                                     class="check-circle"></span>No</label>
                         </div>
                     </iais:value>
                     <span id="error_directedDonation${arDonorIndex}" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                 </iais:row>
                 <iais:row id="idNo${arDonorIndex}Row"  style="${donorDto.directedDonation eq true ? '' : 'display: none;'}">
                     <iais:field width="6" cssClass="col-md-6" value="ID No." mandatory="true"/>
                     <iais:value width="2" cssClass="col-md-2">
                         <iais:select name="idType${arDonorIndex}" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE_DTV" value="${donorDto.idType}"
                                      cssClass="idTypeSel${arDonorIndex}" onchange="removeAges('${arDonorIndex}')"/>
                     </iais:value>
                     <iais:value width="3" cssClass="col-md-3">
                         <iais:input maxLength="20" type="text" name="idNumber${arDonorIndex}" value="${donorDto.idNumber}" onchange="removeAges('${arDonorIndex}')" />
                     </iais:value>
                     <iais:value width="2" cssClass="col-md-3" display="true">
                         <a class="validateDonor" onclick="validateDonor('${arDonorIndex}')">
                             Validate Donor
                         </a>
                          <div>
                         <span id="error_validateDonorYes${arDonorIndex}" name="iaisErrorMsg" class="error-msg"></span>
                          </div>
                     </iais:value>
                 </iais:row>

                 <iais:row id="donorSampleCode${arDonorIndex}Row"   style="${donorDto.directedDonation eq null || donorDto.directedDonation eq true ? 'display: none;' : ''}">
                     <iais:field width="6" cssClass="col-md-6" value="Donor Sample Code / ID" mandatory="true"/>
                     <iais:value width="2" cssClass="col-md-2">
                         <iais:select name="idTypeSample${arDonorIndex}" firstOption="Please Select" options="donorSampleDropDown" value="${donorDto.idType}"
                                      cssClass="idSampleTypeSel${arDonorIndex}" onchange="removeAges('${arDonorIndex}')"/>
                     </iais:value>
                     <iais:value width="3" cssClass="col-md-3">
                         <iais:input maxLength="20" type="text" name="donorSampleCode${arDonorIndex}" id="donorSampleCode${arDonorIndex}" value="${donorDto.donorSampleCode}" onchange="removeAges('${arDonorIndex}')" />
                     </iais:value>
                     <iais:value width="2" cssClass="col-md-3" display="true">
                         <a class="validateDonor" onclick="validateDonor('${arDonorIndex}')">
                             Validate Donor
                         </a>
                         <div>
                         <span id="error_validateDonorNo${arDonorIndex}" name="iaisErrorMsg" class="error-msg"></span>
                         </div>
                     </iais:value>
                 </iais:row>

                 <iais:row id="source${arDonorIndex}Row" style="${(!donorDto.directedDonation && donorDto.donorSampleKey != null) ? '': 'display: none;' }">
                     <iais:field width="6" cssClass="col-md-6" value="Source (i.e. AR Centre or Bank Name)" />
                     <iais:value width="6" cssClass="col-md-6" display="true" >
                         <iais:optionText value="${donorDto.sourceAddress}"/>
                         <input type="hidden" name="source${arDonorIndex}" id="source${arDonorIndex}" value="${donorDto.source}" onchange="sourceChange(this,'${DataSubmissionConsts.AR_SOURCE_OTHER}', 'otherSource${arDonorIndex}Row','${arDonorIndex}');"/>
                     </iais:value>
                 </iais:row>

                 <iais:row id="otherSource${arDonorIndex}Row" style="${donorDto.source eq DataSubmissionConsts.AR_SOURCE_OTHER ? '' : 'display: none'}">
                     <iais:field width="6" cssClass="col-md-6" value="Source (Others)" />
                     <iais:value width="6" cssClass="col-md-6" display="true" >
                         <c:out value="${donorDto.otherSource}" />
                         <iais:input maxLength="100" type="hidden" name="otherSource${arDonorIndex}" id="otherSource${arDonorIndex}" value="${donorDto.otherSource}" onchange="removeAges('${arDonorIndex}')" />
                     </iais:value>
                 </iais:row>


                 <c:if test="${donorFrom == 'ar' && not empty donorDto.donorSampleKey}">
                     <iais:row id="type${arDonorIndex}Row">
                         <iais:field width="6" cssClass="col-md-6" value="Please Indicate" mandatory="true"/>
                         <iais:value width="6" cssClass="col-md-6">
                             <c:forEach items="${donorUsedTypes}" var="donorUsedType">
                                 <c:set var="donorUsedTypeCode" value="${donorUsedType.code}"/>
                                 <div class="form-check col-xs-12"  style="padding-left: 0px;">
                                     <input class="form-check-input" type="checkbox"
                                            name="pleaseIndicate${arDonorIndex}"
                                            value="${donorUsedTypeCode}"
                                            id="pleaseIndicateCheck${arDonorIndex}${donorUsedTypeCode}"
                                            <c:if test="${StringUtil.stringContain(donorDto.pleaseIndicate,donorUsedTypeCode)}">checked</c:if>
                                            onchange="indicateSampleType('${arDonorIndex}','${donorUsedTypeCode}');"
                                            aria-invalid="false">
                                     <label class="form-check-label"
                                            for="pleaseIndicateCheck${arDonorIndex}${donorUsedTypeCode}"><span
                                             class="check-square"></span>
                                         <c:out value="${donorUsedType.codeValue}"/></label>
                                 </div>
                             </c:forEach>
                             <span id="error_pleaseIndicate${donorDto.arDonorIndex}" name="iaisErrorMsg" class="error-msg"></span>
                         </iais:value>
                     </iais:row >
                 </c:if>

                 <c:if test="${donorFrom == 'ar' && not empty donorDto.donorSampleKey}">
                 <div id="selectAgeLists">
                 <iais:row id="${arDonorIndex}AR_DUT_001Row" style="${donorDto.donorIndicateFresh?'':'display:none'}">
                     <iais:field width="6" cssClass="col-md-6" value="Use Donor's Fresh Oocytes Collected At Age" mandatory="true"/>
                     <iais:value width="4" cssClass="col-md-4">
                         <iais:select name="age${arDonorIndex}" firstOption="Please Select"  optionsSelections="${donorDto.ageList}" value="${donorDto.age}"
                                      cssClass="donorAgeDonation${arDonorIndex}"/>
                     </iais:value>
                 </iais:row>

                 <iais:row id="${arDonorIndex}AR_DUT_002Row" style="${donorDto.donorIndicateFrozen?'':'display:none'}">
                     <iais:field width="6" cssClass="col-md-6" value="Use Donor's Frozen Oocytes Collected At Age" mandatory="true"/>
                     <iais:value width="4" cssClass="col-md-4">
                         <iais:select name="frozenOocyteAge${arDonorIndex}" firstOption="Please Select"  optionsSelections="${donorDto.frozenOocyteAgeList}" value="${donorDto.frozenOocyteAge}"
                                      cssClass="donorAgeDonation${arDonorIndex}"/>
                     </iais:value>
                 </iais:row>

                 <iais:row id="${arDonorIndex}AR_DUT_003Row" style="${donorDto.donorIndicateEmbryo?'':'display:none'}">
                     <iais:field width="6" cssClass="col-md-6" value="Use Donor's Frozen Embryos Collected At Age" mandatory="true"/>
                     <iais:value width="4" cssClass="col-md-4">
                         <iais:select name="frozenEmbryoAge${arDonorIndex}" firstOption="Please Select"  optionsSelections="${donorDto.frozenEmbryoAgeList}" value="${donorDto.frozenEmbryoAge}"
                                      cssClass="donorAgeDonation${arDonorIndex}"/>
                     </iais:value>
                 </iais:row>

                 <iais:row id="${arDonorIndex}AR_DUT_005Row" style="${donorDto.donorIndicateFrozenSperm?'':'display:none'}">
                     <iais:field width="6" cssClass="col-md-6" value="Use Donor's Frozen Sperm Collected At Age" mandatory="true"/>
                     <iais:value width="4" cssClass="col-md-4">
                         <iais:select name="frozenSpermAge${arDonorIndex}" firstOption="Please Select"  optionsSelections="${donorDto.frozenSpermAgeList}" value="${donorDto.frozenSpermAge}"
                                      cssClass="donorAgeDonation${arDonorIndex}"/>
                     </iais:value>
                 </iais:row>

                 <iais:row id="${arDonorIndex}AR_DUT_004Row" style="${donorDto.donorIndicateFreshSperm?'':'display:none'}">
                     <iais:field width="6" cssClass="col-md-6" value="Use Donor's Fresh Sperm Collected At Age" mandatory="true"/>
                     <iais:value width="4" cssClass="col-md-4">
                         <iais:select name="freshSpermAge${arDonorIndex}" firstOption="Please Select"  optionsSelections="${donorDto.freshSpermAgeList}" value="${donorDto.freshSpermAge}"
                                      cssClass="donorAgeDonation${arDonorIndex}"/>
                     </iais:value>
                 </iais:row>
                 </div>
                 </c:if>

                 <c:if test="${donorFrom == 'iui' && not empty donorDto.donorSampleKey}">
                     <div id="iuiDonor">
                     <iais:row>
                         <iais:field width="6" cssClass="col-md-6" value="Donor sample used" mandatory="false"/>
                         <iais:value width="3" cssClass="col-md-3" display="true">
                             <c:out value="Donor's Sperm(s) used"/>
                         </iais:value>
                     </iais:row>

                     <iais:row id="age${arDonorIndex}Row">
                         <iais:field width="6" cssClass="col-md-6" value="Age of donor when sperm was collected" mandatory="true"/>
                         <iais:value width="4" cssClass="col-md-4">
                             <c:if test="${'DONTY004' eq donorDto.ageType}">
                                 <iais:select name="frozenSpermAge${arDonorIndex}" firstOption="Please Select"  optionsSelections="${donorDto.frozenSpermAgeList}" value="${donorDto.age}"
                                              cssClass="donorAgeDonation${arDonorIndex}"/>
                             </c:if>
                             <c:if test="${'DONTY005' eq donorDto.ageType}">
                                 <iais:select name="freshSpermAge${arDonorIndex}" firstOption="Please Select"  optionsSelections="${donorDto.freshSpermAgeList}" value="${donorDto.age}"
                                              cssClass="donorAgeDonation${arDonorIndex}"/>
                             </c:if>
                         </iais:value>
                     </iais:row>
                     </div>
                 </c:if>


                     <iais:row id="relation${arDonorIndex}Row" style="${donorDto.directedDonation && !empty donorDto.donorSampleKey ? '' : 'display: none;'}">
                         <iais:field width="6" cssClass="col-md-6" value="Donor relation to patient" mandatory="true" />
                         <iais:value width="3" cssClass="col-md-3" >
                             <div class="form-check" style="padding-left: 0px;">
                                 <input class="form-check-input"
                                        type="radio"
                                        name="relation${arDonorIndex}"
                                        value="${DataSubmissionConsts.DONOR_RELATION_TO_PATIENT_FRIEND}"
                                        id="ownPremisesRadioYes"
                                        <c:if test="${donorDto.relation eq DataSubmissionConsts.DONOR_RELATION_TO_PATIENT_FRIEND}">checked</c:if>
                                        aria-invalid="false" >
                                 <label class="form-check-label"
                                        for="relation${arDonorIndex}RadioYes"><span
                                         class="check-circle"></span>Friend</label>
                             </div>
                         </iais:value>
                         <iais:value width="3" cssClass="col-md-3" >
                             <div class="form-check" style="padding-left: 0px;">
                                 <input class="form-check-input" type="radio"
                                        name="relation${arDonorIndex}"
                                        value="${DataSubmissionConsts.DONOR_RELATION_TO_PATIENT_RELATIVE}"
                                        id="relation${arDonorIndex}RadioNo"
                                        <c:if test="${donorDto.relation eq DataSubmissionConsts.DONOR_RELATION_TO_PATIENT_RELATIVE}">checked</c:if>
                                        aria-invalid="false" >
                                 <label class="form-check-label"
                                        for="relation${arDonorIndex}RadioNo"><span
                                         class="check-circle"></span>Relative</label>
                             </div>
                         </iais:value>
                         <iais:value width="6" cssClass="col-md-6"/>
                         <iais:value width="3" cssClass="col-md-3">
                             <span id="error_relation${arDonorIndex}" name="iaisErrorMsg" class="error-msg"></span>
                         </iais:value>
                     </iais:row>


                 <input type="hidden" name="resetDonor${arDonorIndex}" id="resetDonor${arDonorIndex}" value="${donorDto.resetDonor}"/>
                 <h3></h3>
             </div>
        </c:forEach>
             <c:if test="${donorDtos.size()< arAddDonorMaxSize}">
         <iais:row >
             <iais:value width="5" cssClass="col-md-3" display="true">
                 <a class="addDonor"   onclick="addDonor()"style="text-decoration:none;">+ Add Another Donor</a>
             </iais:value>
         </iais:row>
             </c:if>
     </div>
     </div>
</div>
<input type="hidden" name="DSERR019TipShow" value="${empty DS_ERR019Message ? '' : 1}" id="DSERR019TipShow">
<iais:confirm msg="${DS_ERR019Message}" needCancel="false" popupOrder="DSERR019Tip"  yesBtnDesc="ok"   yesBtnCls="btn btn-primary"  callBack="DSERR019MessageTipClose()" />

<input type="hidden" name="donorMessageTipShow" value="${empty donorResultMoreValue ? '' : 1}" id="donorMessageTipShow">
<iais:confirm msg="${donorMessageTip}" needCancel="false" popupOrder="donorMessageTip"  yesBtnDesc="ok"   yesBtnCls="btn btn-primary"  callBack="donorMessageTipClose()" />