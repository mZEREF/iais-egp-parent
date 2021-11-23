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
                <c:if test="${donorFrom == 'ar'}">
                <iais:row >
                    <iais:field width="5" value="Please Indicate" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:forEach items="${donorUsedTypes}" var="donorUsedType">
                            <c:set var="donorUsedTypeCode" value="${donorUsedType.code}"/>
                            <div class="form-check col-xs-7" >
                                <input class="form-check-input" type="checkbox"
                                       name="pleaseIndicate${arDonorIndex}"
                                       value="${donorUsedTypeCode}"
                                       id="pleaseIndicateCheck${arDonorIndex}${donorUsedTypeCode}"
                                       <c:if test="${StringUtil.stringContain(donorDto.pleaseIndicate,donorUsedTypeCode)}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="pleaseIndicateCheck${arDonorIndex}${donorUsedTypeCode}"><span
                                        class="check-square"></span>
                                    <c:out value="${donorUsedType.codeValue}"/></label>
                            </div>
                        </c:forEach>
                    </iais:value>
                    <span id="error_pleaseIndicate${donorDto.arDonorIndex}" name="iaisErrorMsg" class="error-msg"></span>
                </iais:row >
                </c:if>

                 <iais:row >
                     <iais:field width="5" value="Donor" mandatory="true"/>
                     <iais:value width="7" cssClass="col-md-7" label="true">
                         <c:out value="${donorDto.arDonorIndex+1}"/>
                     </iais:value>
                 </iais:row>

                 <iais:row>
                     <iais:field width="5" value="Is this a Directed Donation?" mandatory="true"/>
                     <iais:value width="3" cssClass="col-md-3">
                         <div class="form-check" onclick="showDonor('${arDonorIndex}')">
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
                     <iais:value width="3" cssClass="col-md-3">
                         <div class="form-check" onclick="hideDonor('${arDonorIndex}')">
                             <input class="form-check-input" type="radio"
                                    name="directedDonation${arDonorIndex}"
                                    value="0"
                                    id="directedDonation${arDonorIndex}RadioNo"
                                    <c:if test="${!donorDto.directedDonation}">checked</c:if>
                                    aria-invalid="false">
                             <label class="form-check-label"
                                    for="directedDonation${arDonorIndex}RadioNo" ><span
                                     class="check-circle"></span>No</label>
                         </div>
                     </iais:value>
                     <iais:value width="1" cssClass="col-md-1">
                         <a  class="deleteDonor" id="deleteDonor${arDonorIndex}" onclick="deleteDonor('${arDonorIndex}')"style="text-decoration:none;">X</a>
                     </iais:value>
                 </iais:row>

                 <iais:row id="idNo${arDonorIndex}Row"  style="${!donorDto.directedDonation ? 'display: none;' : ''}">
                     <iais:field width="5" value="ID No." mandatory="true"/>
                     <iais:value width="2" cssClass="col-md-2">
                         <iais:select name="idType${arDonorIndex}" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE" value="${donorDto.idType}"
                                      cssClass="idTypeSel${arDonorIndex}" onchange="removeAges('${arDonorIndex}')"/>
                     </iais:value>
                     <iais:value width="3" cssClass="col-md-3">
                         <iais:input maxLength="20" type="text" name="idNumber${arDonorIndex}" value="${donorDto.idNumber}" onchange="removeAges('${arDonorIndex}')" />
                     </iais:value>
                     <iais:value width="2" cssClass="col-md-2" display="true">
                         <a class="validateDonor" onclick="validateDonor('${arDonorIndex}')">
                             Validate Donor
                         </a>
                         <span id="error_validateDonorYes${arDonorIndex}" name="iaisErrorMsg" class="error-msg"></span>
                     </iais:value>
                 </iais:row>

                 <iais:row id="donorSampleCode${arDonorIndex}Row"   style="${donorDto.directedDonation ? 'display: none;' : ''}">
                     <iais:field width="5" value="Donor Sample Code / ID" mandatory="true"/>
                     <iais:value width="2" cssClass="col-md-2">
                         <iais:select name="idTypeSample${arDonorIndex}" firstOption="Please Select" options="donorSampleDropDown" value="${donorDto.idType}"
                                      cssClass="idSampleTypeSel${arDonorIndex}" onchange="removeAges('${arDonorIndex}')"/>
                     </iais:value>
                     <iais:value width="3" cssClass="col-md-3">
                         <iais:input maxLength="100" type="text" name="donorSampleCode${arDonorIndex}" id="donorSampleCode${arDonorIndex}" value="${donorDto.donorSampleCode}" onchange="removeAges('${arDonorIndex}')" />
                     </iais:value>
                     <iais:value width="2" cssClass="col-md-2" display="true">
                         <a class="validateDonor" onclick="validateDonor('${arDonorIndex}')">
                             Validate Donor
                         </a>
                         <span id="error_validateDonorNo${arDonorIndex}" name="iaisErrorMsg" class="error-msg"></span>
                     </iais:value>

                 </iais:row>

                 <iais:row id="source${arDonorIndex}Row" style="${donorDto.directedDonation ? 'display: none;' : ''}">
                     <iais:field width="5" value="Source (i.e. AR Centre or Bank Name)" mandatory="true"/>
                     <iais:value width="7" cssClass="col-md-7">
                         <iais:select name="source${arDonorIndex}"  options="donorSourseDropDown" value="${donorDto.source}"
                                      cssClass="source${arDonorIndex}" onchange="sourceChange(this,'${DataSubmissionConsts.AR_SOURCE_OTHER}', 'otherSource${arDonorIndex}Row','${arDonorIndex}');"/>
                     </iais:value>
                 </iais:row>

                 <iais:row id="otherSource${arDonorIndex}Row" style="${donorDto.source eq DataSubmissionConsts.AR_SOURCE_OTHER ? '' : 'display: none'}">
                     <iais:field width="5" value="Source (Others)" mandatory="true"/>
                     <iais:value width="7" cssClass="col-md-7">
                         <iais:input maxLength="100" type="text" name="otherSource${arDonorIndex}" id="otherSource${arDonorIndex}" value="${donorDto.otherSource}" onchange="removeAges('${arDonorIndex}')" />
                     </iais:value>
                 </iais:row>

                 <c:if test="${not empty donorDto.relation}">
                 <iais:row id="relation${arDonorIndex}Row">
                     <iais:field width="5" value="Donor relation to patient" />
                     <iais:value width="7" cssClass="col-md-7" label="true">
                        <iais:code code="${donorDto.relation}"/>
                     </iais:value>
                 </iais:row>
                 </c:if>

                 <c:if test="${not empty donorDto.ageList}">
                 <iais:row id="age${arDonorIndex}Row">
                     <iais:field width="5" value="Donor's Age at Donation" mandatory="true"/>
                     <iais:value width="4" cssClass="col-md-4">
                         <iais:select name="age${arDonorIndex}" firstOption="Please Select"  optionsSelections="${donorDto.ageList}" value="${donorDto.age}"
                                      cssClass="donorAgeDonation${arDonorIndex}" onchange="repeatDonorSampleAge('${arDonorIndex}')"/>
                     </iais:value>
                 </iais:row>
                 </c:if>
                 <input type="hidden" name="resetDonor${arDonorIndex}" id="resetDonor${arDonorIndex}" value="${donorDto.resetDonor}"/>
                 <input type="hidden" name="sameDonorSampleIndexs${arDonorIndex}" id="sameDonorSampleIndexs${arDonorIndex}" value="${empty donorDto.sameDonorSampleIndexs ? '' : donorDto.sameDonorSampleIndexs}"/>
                 <h3></h3>
             </div>
        </c:forEach>
             <c:if test="${donorDtos.size()<4}">
         <iais:row >
             <iais:value width="5" cssClass="col-md-3" display="true">
                 <a class="addDonor"   onclick="addDonor()"style="text-decoration:none;">+ Add Another Donor</a>
             </iais:value>
         </iais:row>
             </c:if>
     </div>
     </div>
</div>