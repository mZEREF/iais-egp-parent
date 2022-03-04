<div class="clinicalDirectorContent">
    <input type="hidden" class="isPartEdit" name="isPartEdit${index}" value="0"/>
    <input type="hidden" class="psnEditField" name="psnEditField${index}" value="<c:out value="${clinicalDirectorDto.psnEditFieldStr}" />"/>
    <input type="hidden" class="cdIndexNo" name="cdIndexNo${index}" value="${clinicalDirectorDto.indexNo}"/>
    <input type="hidden" class="licPerson" name="licPerson" value="${clinicalDirectorDto.licPerson ? '1' : '0'}"/>
    <div class="col-md-12 col-xs-12">
        <div class="edit-content">
            <c:if test="${'true' == canEdit}">
                <p>
                <div class="text-right app-font-size-16">
                    <a class="edit cdEdit" href="javascript:void(0);">
                        <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                    </a>
                </div>
                </p>
            </c:if>
        </div>
    </div>

    <div class="col-md-12 col-xs-12">
        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">
                        <div class="cgo-header">
                            <strong><c:out value="${singleName}"/>
                                <label class="assign-psn-item"><c:if test="${clinicalDirectorDtoList.size() > 1}">${index+1}</c:if></label>
                            </strong>
                        </div>
                    </label>
                </div>

                <div class="col-md-7 col-xs-7 text-right">
                    <c:if test="${index - clinicalDirectorConfig.mandatoryCount >=0}">
                        <div class="">
                            <h4 class="text-danger">
                                <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                            </h4>
                        </div>
                    </c:if>
                </div>

                <c:if test="${'APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType || requestInformationConfig != null}">
                    <div class="col-sm-10">
                        <label class="control-font-label">
                            <c:if test="${!empty clinicalDirectorDto.name && !empty clinicalDirectorDto.idNo && !empty clinicalDirectorDto.idType}">
                                ${clinicalDirectorDto.name}, ${clinicalDirectorDto.idNo} (<iais:code code="${clinicalDirectorDto.idType}"/>)
                            </c:if>
                        </label>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <%-- Assigned person dropdown list --%>
    <div class="col-md-12 col-xs-12">
        <div class="row <c:if test="${'true' == canEdit && '-1' != clinicalDirectorDto.assignSelect && not empty clinicalDirectorDto.assignSelect}">hidden</c:if>">
            <div class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-6 control-label formtext col-md-5">
                        <label  class="control-label control-set-font control-font-label">Assign a <c:out value="${singleName}"/> Person</label>
                        <span class="mandatory">*</span>
                    </div>
                    <div class="col-sm-5 col-md-7" id="assignSelect">
                        <div class="">
                            <iais:select cssClass="assignSel" name="assignSel${index}" options="PERSON_OPTIONS" needSort="false"
                                         value="${clinicalDirectorDto.assignSelect}"></iais:select>
                            <span id="error_assignSelect${index}" name="iaisErrorMsg" class="error-msg"></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="col-md-12 col-xs-12 person-detail <c:if test="${'-1' == clinicalDirectorDto.assignSelect || empty clinicalDirectorDto.assignSelect}">hidden</c:if>">
        <c:choose>
            <c:when test="${clinicalDirectorDto.licPerson}">
                <input class="licPerson" type="hidden" name="licPerson${index}" value="1"/>
            </c:when>
            <c:otherwise>
                <input class="licPerson" type="hidden" name="licPerson${index}" value="0"/>
            </c:otherwise>
        </c:choose>
        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5 professionBoardLabel">
                    <label  class="control-label control-set-font control-font-label">Professional Board</label>
                    <c:if test="${'EAS' == currentSvcCode}">
                        <span class="mandatory">*</span>
                    </c:if>
                </div>
                <div class="col-md-7 col-xs-12">
                    <iais:select cssClass="professionBoard"  name="professionBoard${index}" codeCategory="CATE_ID_PROFESSION_BOARD" value="${clinicalDirectorDto.professionBoard}" firstOption="Please Select"></iais:select>
                </div>
            </div>
        </div>
        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5 profRegNoLabel">
                    <label  class="control-label control-set-font control-font-label">Professional Regn. No.</label>
                    <c:if test="${'EAS' == currentSvcCode}">
                        <span class="mandatory">*</span>
                    </c:if>
                </div>
                <div class="col-md-7 col-xs-12">
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="profRegNo${index}" value="${clinicalDirectorDto.profRegNo}"></iais:input>
                </div>
            </div>
        </div>

        <c:if test="${'MTS' == currentSvcCode}">
            <div class="row control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="control-label formtext col-md-5 col-xs-5">
                        <label  class="control-label control-set-font control-font-label">Not registered with a Professional Board</label>
                    </div>
                    <div class="control-label formtext col-md-7 col-xs-7 noRegWithProfBoardDiv">
                        <label  class="control-label control-set-font control-font-label">
                            <input type="hidden" class="noRegWithProfBoardVal" name="noRegWithProfBoardVal${index}" value="${clinicalDirectorDto.noRegWithProfBoard}"/>
                            <div class="control-item-container parent-form-check" >
                                <input type="checkbox" id="noRegWithProfBoard${index}" name="noRegWithProfBoard" class="control-input noRegWithProfBoard" value="1" <c:if test="${'1' == clinicalDirectorDto.noRegWithProfBoard}">checked</c:if> >
                                <label for="noRegWithProfBoard${index}" class="control-label control-set-font control-font-normal">
                                    <span class="check-square"></span>
                                </label>
                            </div>
                        </label>
                    </div>
                </div>
            </div>
        </c:if>

        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">Name</label>
                    <span class="mandatory">*</span>
                </div>
                <div class="col-sm-3 col-xs-12">
                    <iais:select cssClass="salutation"  name="salutation${index}" codeCategory="CATE_ID_SALUTATION" value="${clinicalDirectorDto.salutation}" firstOption="Please Select"></iais:select>
                </div>
                <div class="col-sm-4 col-xs-12">
                    <iais:input maxLength="110" type="text" cssClass="name field-name" name="name${index}" value="${clinicalDirectorDto.name}"></iais:input>
                </div>
            </div>
        </div>

        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">ID No.</label>
                    <span class="mandatory">*</span>
                </div>
                <div class="col-sm-3 col-xs-12">
                    <div class="">
                        <iais:select cssClass="idType"  name="idType${index}" needSort="false" value="${clinicalDirectorDto.idType}" firstOption="Please Select" codeCategory="CATE_ID_ID_TYPE"></iais:select>
                    </div>
                </div>
                <div class="col-sm-4 col-xs-12">
                    <iais:input cssClass="idNo" maxLength="20" type="text" name="idNo${index}"
                                value="${clinicalDirectorDto.idNo}"></iais:input>
                </div>
            </div>
        </div>

        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">Nationality</label>
                    <span class="mandatory">*</span>
                </div>
                <div class="col-md-7 col-xs-12">
                    <iais:select firstOption="Please Select" name="nationality${index}" codeCategory="CATE_ID_NATIONALITY"
                                 cssClass="nationality" value="${clinicalDirectorDto.nationality}" />
                </div>
            </div>
        </div>

        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label"></label>
                </div>
                <div class="col-md-7 col-xs-12">
                    <span class="error-msg" name="iaisErrorMSg" id="error_idTypeNo${index}"></span>
                </div>
            </div>
        </div>

        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">Designation</label>
                    <span class="mandatory">*</span>
                </div>
                <div class="col-md-7 col-xs-12" id="designation">
                    <iais:select cssClass="designation" name="designation${index}" value="${clinicalDirectorDto.designation}" options="designationOpList" firstOption="Please Select"></iais:select>
                </div>
            </div>
        </div>

        <div class="row control control-caption-horizontal hidden other-designation">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                </div>
                <div class="col-md-7 col-xs-12">
                    <iais:input maxLength="100" type="text" cssClass="otherDesignation" name="otherDesignation${index}" value="${clinicalDirectorDto.otherDesignation}"/>
                </div>
            </div>
        </div>

        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">Specialty</label>
                </div>
                <div class="control-label formtext col-md-7 col-xs-12" >
                     <label class="control-label control-set-font control-font-label specialty-label specialityField">
                         <c:out value="${clinicalDirectorDto.speciality}" />
                     </label>
                </div>
            </div>
        </div>


        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5 specialtyGetDateLabel">
                    <label  class="control-label control-set-font control-font-label">
                        Date when specialty was obtained
                    </label>
                </div>
                <div class="col-md-3 col-xs-12">
                    <iais:datePicker cssClass="specialtyGetDate field-name" name="specialtyGetDate${index}"
                                     value="${clinicalDirectorDto.specialtyGetDateStr}" />
                </div>
            </div>
        </div>

        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">Type of Registration Date</label>
                    <span class="mandatory">*</span>
                </div>
                <div class="col-md-7 col-xs-12">
                    <iais:input maxLength="50" type="text" cssClass="typeOfCurrRegi field-name" name="typeOfCurrRegi${index}"
                                value="${clinicalDirectorDto.typeOfCurrRegi}" />
                </div>
            </div>
        </div>

        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">Current Registration Date</label>
                    <span class="mandatory">*</span>
                </div>
                <div class="col-md-3 col-xs-12">
                    <iais:datePicker cssClass="currRegiDate field-name" name="currRegiDate${index}"
                                     value="${clinicalDirectorDto.currRegiDateStr}" />
                </div>
            </div>
        </div>

        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">Practicing Certificate End Date</label>
                    <span class="mandatory">*</span>
                </div>
                <div class="col-md-3 col-xs-12">
                    <iais:datePicker cssClass="praCerEndDate field-name" name="praCerEndDate${index}"
                                     value="${clinicalDirectorDto.praCerEndDateStr}" />
                </div>
            </div>
        </div>

        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">Type of Register</label>
                    <span class="mandatory">*</span>
                </div>
                <div class="col-md-7 col-xs-12">
                    <iais:input maxLength="50" type="text" cssClass="typeOfRegister field-name" name="typeOfRegister${index}"
                                value="${clinicalDirectorDto.typeOfRegister}"></iais:input>
                </div>
            </div>
        </div>

        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5 relevantExperienceLabel">
                    <label  class="control-label control-set-font control-font-label">Relevant Experience</label>
                    <c:if test="${'MTS' == currentSvcCode}">
                        <span class="mandatory">*</span>
                    </c:if>
                </div>
                <div class="col-md-7 col-xs-12">
                    <iais:input maxLength="180" type="text" cssClass="relevantExperience" name="relevantExperience${index}" value="${clinicalDirectorDto.relevantExperience}"></iais:input>
                </div>
            </div>
        </div>

        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap holdCerByEMSDiv">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">Clinical Governance Officer (CGO) holds a valid certification issued by an Emergency Medical Services ("EMS") Medical Directors workshop&nbsp;<span class="mandatory">*</span></label>
                </div>
                <input type="hidden" class="holdCerByEMSVal" name="holdCerByEMSVal${index}" value="${clinicalDirectorDto.holdCerByEMS}"/>
                <div class="form-check col-md-3 col-xs-3">
                    <input class="form-check-input holdCerByEMS" <c:if test="${'1' == clinicalDirectorDto.holdCerByEMS}">checked="checked"</c:if>  type="radio" name="holdCerByEMS${index}" value = "1" aria-invalid="false">
                    <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
                </div>
                <div class="form-check col-md-3 col-xs-3">
                    <input class="form-check-input holdCerByEMS" <c:if test="${'0' == clinicalDirectorDto.holdCerByEMS}">checked="checked"</c:if>  type="radio" name="holdCerByEMS${index}" value = "0" aria-invalid="false">
                    <label class="form-check-label" ><span class="check-circle"></span>No</label>
                </div>
                <span class="error-msg col-md-7" name="iaisErrorMsg" id="error_holdCerByEMS${index}"></span>
            </div>
        </div>

        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">Expiry Date (ACLS)</label>
                    <c:if test="${'EAS' == currentSvcCode}">
                        <span class="mandatory">*</span>
                    </c:if>
                </div>
                <div class="col-md-3 col-xs-12">
                    <iais:datePicker cssClass="aclsExpiryDate" name="aclsExpiryDate${index}" value="${clinicalDirectorDto.aclsExpiryDateStr}" />
                </div>
                <div class="col-md-5">
                </div>
                <div class="col-md-7">
                    <span class="error-msg" name="iaisErrorMsg" id="error_expiryDateAcls${index}"></span>
                </div>
            </div>
        </div>

        <c:if test="${'MTS' == currentSvcCode}">
            <div class="row control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="control-label formtext col-md-5 col-xs-5">
                        <label  class="control-label control-set-font control-font-label">Expiry Date (BCLS and AED)</label>
                        <span class="mandatory">*</span>
                    </div>
                    <div class="col-md-3 col-xs-12">
                        <iais:datePicker cssClass="bclsExpiryDate" name="bclsExpiryDate${index}" value="${clinicalDirectorDto.bclsExpiryDateStr}" />
                    </div>
                    <div class="col-md-5">
                    </div>
                    <div class="col-md-7">
                        <span class="error-msg" name="iaisErrorMsg" id="error_expiryDateBcls${index}"></span>
                    </div>
                </div>
            </div>
        </c:if>

        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">Mobile No.</label>
                    <span class="mandatory">*</span>
                </div>
                <div class="col-md-7 col-xs-12">
                    <iais:input maxLength="8" cssClass="mobileNo" type="text" name="mobileNo${index}" value="${clinicalDirectorDto.mobileNo}"></iais:input>
                </div>
            </div>
        </div>

        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <div class="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">Email Address</label>
                    <span class="mandatory">*</span>
                </div>
                <div class="col-md-7 col-xs-12">
                    <iais:input maxLength="320" type="text" cssClass="emailAddr" name="emailAddr${index}" value="${clinicalDirectorDto.emailAddr}"></iais:input>
                </div>
            </div>
        </div>

        <hr/>

    </div>
</div>