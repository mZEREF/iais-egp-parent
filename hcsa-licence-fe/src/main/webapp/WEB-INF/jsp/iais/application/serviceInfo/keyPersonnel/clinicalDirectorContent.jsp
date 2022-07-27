<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<c:set var="pcdType" value="${ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR}"/>
<c:set var="clinicalDirectorList" value="${currSvcInfoDto.appSvcClinicalDirectorDtoList}"/>

<c:if test="${empty psnContent}">
    <c:set var="psnContent" value="person-content"/>
</c:if>

    <iais:row cssClass="row col-xs-12 col-md-12 text-right">
        <c:if test="${AppSubmissionDto.needEditController }">
            <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
            <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                <div class="app-font-size-16">
                    <a class="back" id="RfcSkip" href="javascript:void(0);">
                        Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em>
                    </a>
                </div>
            </c:if>
            <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
        </c:if>
    </iais:row>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" name="rfiObj" value="<c:if test="${requestInformationConfig == null}">0</c:if><c:if test="${requestInformationConfig != null}">1</c:if>"/>

<div class="row cdForm">
    <iais:row cssClass="col-md-12 col-xs-12">
        <div class="row control control-caption-horizontal">
            <div class=" form-group form-horizontal formgap">
                <iais:value width="10" cssClass="control-label formtext col-md-5 col-xs-5">
                    <label  class="control-label control-set-font control-font-label">
                        <p class="app-title"><c:out value="${currStepName}"/></p>
                    </label>
                </iais:value>
            </div>
        </div>
    </iais:row>

    <c:choose>
        <c:when test="${empty clinicalDirectorDtoList}">
            <c:set var="pageLength" value="1"/>
        </c:when>
        <c:when test="${clinicalDirectorConfig.mandatoryCount > clinicalDirectorDtoList.size() }">
            <c:set var="pageLength" value="${clinicalDirectorConfig.mandatoryCount}"/>
        </c:when>
        <c:otherwise>
            <c:set var="pageLength" value="${clinicalDirectorDtoList.size()}"/>
        </c:otherwise>
    </c:choose>
    <input type="hidden" name="cdLength" value="${pageLength}" />
    <c:forEach begin="0" end="${pageLength-1}" step="1" varStatus="cdStat">
        <c:set var="index" value="${cdStat.index}" />
        <c:set var="clinicalDirectorDto" value="${clinicalDirectorDtoList[index]}"/>

        <div class="clinicalDirectorContent">
            <input type="hidden" class="not-refresh isPartEdit" name="isPartEdit${index}" value="0"/>
            <input type="hidden" class="not-refresh psnEditField" name="psnEditField${index}" value="<c:out value="${clinicalDirectorDto.psnEditFieldStr}" />"/>
            <input type="hidden" class="not-refresh cdIndexNo" name="cdIndexNo${index}" value="${clinicalDirectorDto.indexNo}"/>
            <input type="hidden" class="not-refresh licPerson" name="licPerson" value="${clinicalDirectorDto.licPerson ? 1 : 0}"/>

            <iais:row cssClass="edit-content">
                <c:if test="${canEdit}">
                    <div class="text-right app-font-size-16">
                        <a class="edit psnEdit" href="javascript:void(0);">
                            <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                        </a>
                    </div>
                </c:if>
            </iais:row>

            <iais:row cssClass="row control control-caption-horizontal">
                <div class="col-md-12 col-xs-12 ">
                    <div class=" form-group form-horizontal formgap">
                        <div class="control-label formtext col-md-5 col-xs-5">
                            <p class="bold">${singleName}
                                <label class="assign-psn-item"><c:if test="${clinicalDirectorDtoList.size() > 1}">${index+1}</c:if></label>
                            </p>
                        </div>
                    </div>

                    <div class="col-md-7 col-xs-7 text-right removeBtn">
                        <c:if test="${!isRFI && !isRFC && !isRenew}">
                            <h4 class="text-danger">
                                <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                            </h4>
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
            </iais:row>

            <iais:row>
                <iais:value width="10" cssClass="col-md-10 col-xs-12">
                    <span class="error-msg" name="iaisErrorMSg" id="error_${perfix}personError${index}"></span>
                </iais:value>
            </iais:row>

            <c:if test="${'APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType || requestInformationConfig != null}">
                <iais:row>
                    <div class="col-sm-10">
                        <label class="control-font-label">
                            <c:if test="${!empty clinicalDirectorDto.name && !empty clinicalDirectorDto.idNo && !empty clinicalDirectorDto.idType}">
                                ${clinicalDirectorDto.name}, ${clinicalDirectorDto.idNo} (<iais:code code="${clinicalDirectorDto.idType}"/>)
                            </c:if>
                        </label>
                    </div>
                </iais:row>
            </c:if>

            <div class="col-md-12 col-xs-12">
                <iais:row cssClass="row ${'true' == canEdit && '-1' != clinicalDirectorDto.assignSelect && not empty clinicalDirectorDto.assignSelect ? 'hidden':''}">
                    <iais:field width="12"  cssClass="col-md-5"  mandatory="true" value="Assign a ${singleName} Person"/>
                    <iais:value width="5" cssClass="col-sm-5 col-md-7" id="assignSelect">
                        <iais:select cssClass="assignSel" name="${perfix}assignSelect${index}" options="PERSON_OPTIONS" needSort="false"
                                     value="${clinicalDirectorDto.assignSelect}"/>
                    </iais:value>
                </iais:row>
            </div>

            <div class="col-md-12 col-xs-12 person-detail <c:if test="${'-1' == clinicalDirectorDto.assignSelect || empty clinicalDirectorDto.assignSelect}">hidden</c:if>">
                <c:choose>
                    <c:when test="${clinicalDirectorDto.licPerson}">
                        <input class="licPerson" type="hidden" name="${perfix}licPerson${index}" value="1"/>
                    </c:when>
                    <c:otherwise>
                        <input class="licPerson" type="hidden" name="${perfix}licPerson${index}" value="0"/>
                    </c:otherwise>
                </c:choose>
                <iais:row cssClass="row control control-caption-horizontal">
                    <div class="form-group form-horizontal formgap">
                        <iais:field width="5" cssClass="control-label formtext col-md-5 col-xs-5 professionBoardLabel" mandatory="false" value="Professional Board"/>
                        <c:if test="${'EAS' == currentSvcCode}">
                            <span class="mandatory">*</span>
                        </c:if>
                    </div>
                    <iais:value width="7" cssClass="col-md-7 col-xs-12">
                        <iais:select cssClass="professionBoard" name="${perfix}professionBoard${index}" codeCategory="CATE_ID_PROFESSION_BOARD"
                                     value="${clinicalDirectorDto.professionBoard}" firstOption="Please Select"/>
                    </iais:value>
                </iais:row>

                <iais:row cssClass="row control control-caption-horizontal">
                    <div class="form-group form-horizontal formgap">
                        <iais:field width="5" cssClass="col-md-5 profRegNoLabel" id="p" mandatory="true" value="Professional Regn. No."/>
                        <c:if test="${'EAS' == currentSvcCode}">
                            <span class="mandatory">*</span>
                        </c:if>
                    </div>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${perfix}profRegNo${index}" value="${clinicalDirectorDto.profRegNo}"/>
                    </iais:value>
                </iais:row>

                <iais:row cssClass="row control control-caption-horizontal">
                    <div class="form-group form-horizontal formgap">
                        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Name"/>
                        <iais:value width="3" cssClass="col-md-3">
                            <iais:select cssClass="salutation" name="${perfix}salutation${index}" firstOption="Please Select"
                                         codeCategory="CATE_ID_SALUTATION" value="${clinicalDirectorDto.salutation}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4">
                            <iais:input maxLength="66" type="text" cssClass="name" name="${perfix}name${index}" value="${clinicalDirectorDto.name}"/>
                        </iais:value>
                    </div>
                </iais:row>

                <iais:row cssClass="row control control-caption-horizontal">
                    <div class="form-group form-horizontal formgap">
                        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="ID No."/>
                        <iais:value width="3" cssClass="col-md-3">
                            <iais:select cssClass="idType"  name="${perfix}idType${index}" needSort="false" value="${clinicalDirectorDto.idType}"
                                         firstOption="Please Select" codeCategory="CATE_ID_ID_TYPE"></iais:select>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4">
                            <iais:input cssClass="idNo" maxLength="20" type="text" name="${perfix}idNo${index}"
                                        value="${clinicalDirectorDto.idNo}"></iais:input>
                        </iais:value>
                    </div>
                </iais:row>

                <iais:row cssClass="row control control-caption-horizontal nationalityDiv">
                    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Country of issuance"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select firstOption="Please Select" name="${perfix}nationality${index}" codeCategory="CATE_ID_NATIONALITY"
                                     cssClass="nationality" value="${clinicalDirectorDto.nationality}"/>
                    </iais:value>
                </iais:row>
                <iais:row cssClass="row control control-caption-horizontal">
                    <iais:field width="5" mandatory="" value=""/>
                    <iais:value width="7" cssClass="col-md-7 col-xs-12">
                        <span class="error-msg" name="iaisErrorMSg" id="error_${perfix}idTypeNo${index}"></span>
                    </iais:value>
                </iais:row>

                <iais:row cssClass="row control control-caption-horizontal">
                    <div class="form-group form-horizontal formgap">
                        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Designation"/>
                        <iais:value width="7" cssClass="col-md-7" id="designation">
                            <iais:select cssClass="designation" name="${perfix}designation${index}" value="${clinicalDirectorDto.designation}"
                                         options="designationOpList" firstOption="Please Select"
                                         onchange="toggleOnVal(this, 'DES999', '.otheDesignationDiv');"/>
                        </iais:value>
                    </div>
                </iais:row>

                <iais:row cssClass="row control control-caption-horizontal hidden other-designation">
                    <div class="form-group form-horizontal formgap">
                        <iais:field width="5" cssClass="col-md-5" value=""/>
                        <iais:value width="7" cssClass="col-md-7 speciality" display="true">
                            <iais:input maxLength="100" type="text" cssClass="otherDesignation" name="otherDesignation${index}" value="${clinicalDirectorDto.otherDesignation}"/>
                        </iais:value>
                    </div>
                </iais:row>

                <iais:row cssClass="row control control-caption-horizontal">
                    <div class="form-group form-horizontal formgap">
                        <iais:field width="5" cssClass="col-md-5" value="Specialty"/>
                        <iais:value width="7" cssClass="col-md-7 speciality" display="true">
                            <c:out value="${clinicalDirectorDto.speciality}"/>
                        </iais:value>
                    </div>
                </iais:row>

                <iais:row cssClass="row control control-caption-horizontal">
                    <div class="form-group form-horizontal formgap">
                        <iais:field width="5" cssClass="col-md-5 specialtyGetDateLabel" mandatory="false" value="Date when specialty was obtained"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:datePicker cssClass="specialtyGetDate field-date" name="${perfix}specialtyGetDate${index}"
                                             value="${clinicalDirectorDto.specialtyGetDateStr}"/>
                        </iais:value>
                    </div>
                </iais:row>

                <iais:row cssClass="row control control-caption-horizontal">
                    <div class="form-group form-horizontal formgap">
                        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Type of Registration Date"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="50" type="text" cssClass="typeOfCurrRegi" name="${perfix}typeOfCurrRegi${index}"
                                        value="${clinicalDirectorDto.typeOfCurrRegi}"/>
                        </iais:value>
                    </div>
                </iais:row>

                <iais:row cssClass="row control control-caption-horizontal">
                    <div class="form-group form-horizontal formgap">
                        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Current Registration Date"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:datePicker cssClass="currRegiDate field-date" name="${perfix}currRegiDate${index}" value="${clinicalDirectorDto.currRegiDateStr}"/>
                        </iais:value>
                    </div>
                </iais:row>

                <iais:row cssClass="row control control-caption-horizontal">
                    <div class="form-group form-horizontal formgap">
                        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Practicing Certificate End Date"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:datePicker cssClass="praCerEndDate field-date" name="${perfix}praCerEndDate${index}" value="${clinicalDirectorDto.praCerEndDateStr}"/>
                        </iais:value>
                    </div>
                </iais:row>

                <iais:row cssClass="row control control-caption-horizontal">
                    <div class="form-group form-horizontal formgap">
                        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Type of Register"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="50" type="text" cssClass="typeOfRegister" name="${perfix}typeOfRegister${index}"
                                        value="${clinicalDirectorDto.typeOfRegister}"/>
                        </iais:value>
                    </div>
                </iais:row>

                <iais:row cssClass="row control control-caption-horizontal">
                    <div class="form-group form-horizontal formgap">
                        <iais:field width="5" cssClass="col-md-5 relevantExperienceLabel" mandatory="false" value="Relevant Experience"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="180" type="text" cssClass="relevantExperience" name="${perfix}relevantExperience${index}"
                                        value="${clinicalDirectorDto.typeOfRegister}"/>
                        </iais:value>
                    </div>
                </iais:row>
                    <%--TODO....--%>
                <iais:row cssClass="row control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap holdCerByEMSDiv">
                        <iais:field width="5" cssClass="col-md-5" mandatory="false" value="Clinical Governance Officer (CGO) holds a valid certification issued by an Emergency Medical Services ('EMS') Medical Directors workshop&nbsp;"/>
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
                </iais:row>

                <iais:row cssClass="row control control-caption-horizontal">
                    <div class="form-group form-horizontal formgap">
                        <iais:field width="5" cssClass="col-md-5" mandatory="false" value="Expiry Date (ACLS)"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:datePicker cssClass="aclsExpiryDate" name="${perfix}aclsExpiryDate${index}"
                                             value="${clinicalDirectorDto.aclsExpiryDateStr}"/>
                        </iais:value>
                    </div>
                </iais:row>

                <c:if test="${'MT' == currentSvcCode}">
                    <iais:row cssClass="row control control-caption-horizontal">
                        <div class="form-group form-horizontal formgap">
                            <iais:field width="5" cssClass="col-md-5" mandatory="${pcdType ? 'true' : 'false'}" value="Expiry Date (BCLS and AED)"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:datePicker cssClass="bclsExpiryDate" name="${perfix}bclsExpiryDate${index}"
                                                 value="${clinicalDirectorDto.bclsExpiryDateStr}"/>
                            </iais:value>
                        </div>
                    </iais:row>
                </c:if>

                <iais:row cssClass="row control control-caption-horizontal">
                    <div class="form-group form-horizontal formgap">
                        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Mobile No."/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="8" type="text" cssClass="mobileNo" name="${perfix}mobileNo${index}" value="${clinicalDirectorDto.mobileNo}"/>
                        </iais:value>
                    </div>
                </iais:row>

                <iais:row cssClass="row control control-caption-horizontal">
                    <div class="form-group form-horizontal formgap">
                        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Email Address"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="320" type="text" cssClass="emailAddr" name="${perfix}emailAddr${index}" value="${clinicalDirectorDto.emailAddr}"/>
                        </iais:value>
                    </div>
                </iais:row>
                <hr/>
            </div>
        </div>
    </c:forEach>

    <c:if test="${ requestInformationConfig==null}">
        <c:choose>
            <c:when test="${!empty clinicalDirectorDtoList}">
                <c:set var="cdLength" value="${clinicalDirectorDtoList.size()}"/>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${AppSubmissionDto.needEditController}">
                        <c:set var="cdLength" value="0"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="cdLength" value="${clinicalDirectorConfig.mandatoryCount}"/>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        <c:set var="needAddPsn" value="true"/>
        <c:choose>
            <c:when test="${clinicalDirectorConfig.status =='CMSTAT003'}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
            <c:when test="${cdLength >= clinicalDirectorConfig.maximumCount}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
            <c:when test="${AppSubmissionDto.needEditController && !canEdit}">
                <c:set var="needAddPsn" value="false"/>
            </c:when>
        </c:choose>
        <div class="col-md-12 col-xs-12 addClinicalDirectorDiv <c:if test="${!needAddPsn}">hidden</c:if>">
            <span class="addClinicalDirectorBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">+ Add <c:out value="${singleName}"/></span>
            </span>
        </div>
    </c:if>
</div>
<%@include file="../../common/prsLoading.jsp"%>
<script>
    $(document).ready(function () {
        refresh();
        holdCerByEMS();
        addClinicalDirectorBtn();
        removeClinicalDirector();
        showOtherSpecialty();
        profRegNoBlur();
        designationBindEvent();
        doEdite();
        assignSelectBindEvent();

        initNationality('div.clinicalDirectorContent', '.idType', '.nationalityDiv');

        //rfc,renew,rfi
        var appType = $('input[name="applicationType"]').val();
        var rfiObj = $('input[name="rfiObj"]').val();
        if (('APTY005' == appType || 'APTY004' == appType) || '1' == rfiObj) {
            disabledPage();
            $('select').prop('disabled',true);
            $('.date_picker').addClass('disabled-placeHolder');
            $('input.holdCerByEMS:checked').each(function () {
                $(this).closest('div').find('label span.check-circle').addClass('radio-disabled');
            });
        }

        // init
        $('div.clinicalDirectorContent').each(function () {
            var $currContent = $(this);
            var assignSelVal = $currContent.find('.assignSel:input').val();
            console.info("init ---- " + assignSelVal);
            if (isEmpty(assignSelVal)) {
                $currContent.find('select.assignSel option').eq(0).prop("selected", true);
            } else if ("-1" != assignSelVal && 'newOfficer' != assignSelVal) {
                var data;
                try{
                    data = $.parseJSON($currContent.find('.psnEditField:input').val());
                } catch (e) {
                    data = {};
                };
                if ('1' == $(this).find('.licPerson:input').val()) {
                    disableCdContent($currContent.find('div.person-detail'), data);
                }
            }
            var prgNo = $currContent.find('input.profRegNo').val();
            if (!isEmpty(prgNo)) {
                var assignSelectVal = $currContent.find('select.assignSel').val();
                var licPerson = $currContent.find('input.licPerson').val();
                var needControlName = isNeedControlName(assignSelectVal, licPerson, appType);
                prsCallBackFuns.setEdit($currContent, 'disabled', false, needControlName);
            }
            // designation
            $(this).find('.designation').triggerHandler('change');
            // update select tag
            $(this).find('select').niceSelect("update");
            toggleOnSelect($(this).find('.idType'), 'IDTYPE003', $(this).find('.nationalityDiv'));
        });

        if("${errormapIs}"=='error'){
            $('.edit').trigger('click');
        }
    });

    var holdCerByEMS = function() {
        $('.holdCerByEMS').unbind('click');
        $('.holdCerByEMS').click(function () {
            var holdCerByEMSVal = $(this).val();
            $(this).closest('div.holdCerByEMSDiv').find('input[name="holdCerByEMSVal"]').val(holdCerByEMSVal);
        });
    };

    function addClinicalDirectorBtn(){
       let cdLength = $('.clinicalDirectorContent').length;
       $('.addClinicalDirectorBtn').unbind('click');
       $('.addClinicalDirectorBtn').click(function () {
           showWaiting();
           let target = $('div.clinicalDirectorContent:first');
           let src = target.clone();
           $('div.addClinicalDirectorDiv').before(src);
           cdLength = cdLength + 1 ;
           $('input[name="cdLength"]').val(cdLength);
           let $currContent = $('div.clinicalDirectorContent:last');
           clearFields($currContent);
           var assignSelVal = $currContent.find('.assignSel:input').val();
           // console.info("init ---- " + assignSelVal);
           if (assignSelVal == -1) {
               let s = $currContent.find('.assignSel').unbind('change');
               console.log(s)
               assignSel(s, $(s).closest('div.clinicalDirectorContent').find('div.person-detail'));
           }
           removeClinicalDirector();
           showOtherSpecialty();
           assignSelectBindEvent();
           designationBindEvent();
           profRegNoBlur();
           $('.date_picker').datepicker({
               format:"dd/mm/yyyy",
               autoclose:true,
               todayHighlight:true,
               orientation:'bottom'
           });

           //hidden add more
           if (cdLength >= '${clinicalDirectorConfig.maximumCount}') {
               $('.addClinicalDirectorDiv').addClass('hidden');
           }
           if(cdLength <= '${clinicalDirectorConfig.mandatoryCount}'){
               //remove del btn for mandatory count
           }
           $('.clinicalDirectorContent').each(function (k,v) {
               $(this).find('.assign-psn-item').html(k+1);

           });
           $('#isEditHiddenVal').val('1');
           refreshPremise();
           initNationality('div.clinicalDirectorContent:last', '.idType', '.nationalityDiv');
           dismissWaiting();
       })
   }

    function refreshPremise() {
        refresh();
    }

    function refresh(){
        var cdLength = $('.clinicalDirectorContent').length;
        $('input[name="cdLength"]').val(cdLength);
        //reset number
        $('div.clinicalDirectorContent').each(function (k,v) {
            toggleTag($(this).find('div.removeBtn'), k != 0);
            $(this).find('select.assignSel').prop('name','assignSel'+k);
            $(this).find('select.assignSel').prop('id','assignSel'+k);

            $('div.clinicalDirectorContent').find('select').niceSelect('update');
            //display add more
            if (cdLength < '${clinicalDirectorConfig.maximumCount}') {
                $('.addClinicalDirectorDiv').removeClass('hidden');
            }
            if(cdLength <= 1){
                $('.clinicalDirectorContent:eq(0) .assign-psn-item').html('');
            }
            $('#isEditHiddenVal').val('1');
        });
    }

   var removeClinicalDirector = function () {
       $('.removeBtn').unbind('click');
       $('.removeBtn').click(function () {
           $(this).closest('div.clinicalDirectorContent').remove();
           var cdLength = $('.clinicalDirectorContent').length;
           $('input[name="cdLength"]').val(cdLength);
           //reset number
           $('div.clinicalDirectorContent').each(function (k,v) {
               $(this).find('.assign-psn-item').html(k+1);
               $(this).find('input.profRegNo').prop('name','profRegNo'+k);
               $(this).find('input.name').prop('name','name'+k);
               $(this).find('input.idNo').prop('name','idNo'+k);
               $(this).find('input.otherSpecialty').prop('name','otherSpecialty'+k);
               $(this).find('input.specialtyGetDate').prop('name','specialtyGetDate'+k);
               $(this).find('input.typeOfCurrRegi').prop('name','typeOfCurrRegi'+k);
               $(this).find('input.currRegiDate').prop('name','currRegiDate'+k);
               $(this).find('input.praCerEndDate').prop('name','praCerEndDate'+k);
               $(this).find('input.typeOfRegister').prop('name','typeOfRegister'+k);
               $(this).find('input.holdCerByEMSVal').prop('name','holdCerByEMSVal'+k);
               $(this).find('input.holdCerByEMS').prop('name','holdCerByEMS'+k);
               $(this).find('input.relevantExperience').prop('name','relevantExperience'+k);
               $(this).find('input.aclsExpiryDate').prop('name','aclsExpiryDate'+k);
               $(this).find('input.bclsExpiryDate').prop('name','bclsExpiryDate'+k);
               $(this).find('input.mobileNo').prop('name','mobileNo'+k);
               $(this).find('input.emailAddr').prop('name','emailAddr'+k);
               $(this).find('input.transportYear').prop('name','transportYear'+k);
               $(this).find('input.isPartEdit').prop('name','isPartEdit'+k);
               $(this).find('input.cdIndexNo').prop('name','cdIndexNo'+k);
               $(this).find('input.licPerson').prop('name','licPerson'+k);

               $(this).find('select.assignSel').prop('name','assignSel'+k);
               $(this).find('select.assignSel').prop('id','assignSel'+k);
               $(this).find('select.professionBoard').prop('name','professionBoard'+k);
               $(this).find('select.salutation').prop('name','salutation'+k);
               $(this).find('select.idType').prop('name','idType'+k);
               $(this).find('select.designation').prop('name','designation'+k);
               $(this).find('select.specialty').prop('name','specialty'+k);
           });
           $('div.clinicalDirectorContent').find('select').niceSelect('update');
           //display add more
           if (cdLength < '${clinicalDirectorConfig.maximumCount}') {
               $('.addClinicalDirectorDiv').removeClass('hidden');
           }
           if(cdLength <= 1){
               $('.clinicalDirectorContent:eq(0) .assign-psn-item').html('');
           }
           $('#isEditHiddenVal').val('1');
       });
   }

   var showOtherSpecialty = function () {
       $('select.specialty').unbind('change');
       $('select.specialty').change(function () {
           var $otherSpecialtyEle = $(this).closest('.clinicalDirectorContent').find('div.otherSpecialtyDiv');
           var val = $(this).val();
           if ('EAMS006' == val) {
               $otherSpecialtyEle.removeClass('hidden');
           } else {
               $otherSpecialtyEle.addClass('hidden');
           }
       });
   };

   function assignSelectBindEvent() {
       $('.assignSel').on('change', function() {
           clearErrorMsg();
           // console.log(this)
           assignSel(this, $(this).closest('div.clinicalDirectorContent').find('div.person-detail'));
       });
   }

   function designationBindEvent() {

       $('.designation').unbind('change');
       $('.designation').on('change', function() {
           var thisVal = $(this).val();
           var currContEle = $(this).closest('div.clinicalDirectorContent');
           designationChange(currContEle, thisVal);
       });
   }

    function designationChange(currContEle, designationVal) {
        console.log(designationVal);
        if('DES999' == designationVal){
            currContEle.find('div.other-designation').removeClass('hidden');
        } else{
            currContEle.find('div.other-designation').addClass('hidden');
        }
    }

   var profRegNoBlur = function () {
       $('input.profRegNo').unbind('blur');
       $('input.profRegNo').blur(function(event, action){
           var prgNo = $(this).val();
           var $prsLoadingContent = $(this).closest('div.clinicalDirectorContent');
           //prs loading
           prdLoading($prsLoadingContent, prgNo, action, prsCallBackFuns);
       });
   };

    function disableCdContent($current, data) {
        if (isEmpty(data) || isEmpty($current)) {
            return;
        }
        $.each(data, function(i, val) {
            //console.info(i + " : " + val);
            var $input = $current.find('.' + i + ':input');
            if ($input.length > 0 && !val) {
                disableContent($input);
            }
        });
    }

    var prsCallBackFuns ={
        fillData:function ($prsLoadingEle, data, needControlName) {
            console.info(data);
            var specialty = data.specialty ;
            if(isEmpty(specialty)){
                specialty = '';
            }
            var name = data.name;
            /*if(!isEmpty(data) && !isEmpty(data.regno) && !isEmpty(name) && isEmpty(specialty)){
                specialty = 'No specialty';
            }*/
            var specialtyGetDate = '';
            if(!isEmpty(data.entryDateSpecialist)){
                specialtyGetDate = data.entryDateSpecialist[0];
            }
            var typeOfCurrRegi = '';
            var currRegiDate = '';
            var praCerEndDate = '';
            var typeOfRegister = '';
            if(!isEmpty(data.registration)){
                var registration = data.registration[0];
                typeOfCurrRegi = registration['Registration Type'];
                currRegiDate = registration['Registration Start Date'];
                praCerEndDate = registration['PC End Date'];
                typeOfRegister = registration['Register Type'];
            }

            $prsLoadingEle.find('.specialty-label').html(specialty);
            if(needControlName){
                $prsLoadingEle.find('.name').val(name);
            }
            $prsLoadingEle.find('.specialtyGetDate').val(specialtyGetDate);
            $prsLoadingEle.find('.typeOfCurrRegi').val(typeOfCurrRegi);
            $prsLoadingEle.find('.currRegiDate').val(currRegiDate);
            $prsLoadingEle.find('.praCerEndDate').val(praCerEndDate);
            $prsLoadingEle.find('.typeOfRegister').val(typeOfRegister);

            var dateSpan = $prsLoadingEle.find('.specialtyGetDateLabel').find('.mandatory');
            if(data.regno == null || data.name == null || !isEmpty(specialty)){
                dateSpan.removeClass('hidden');
            }else {
                dateSpan.addClass('hidden');
            }
        },
        setEdit:function ($prsLoadingEle, propStyle, canEdit, needControlName) {
            var nameEle = $prsLoadingEle.find('.name');
            var specialtyGetDateEle = $prsLoadingEle.find('.specialtyGetDate');
            var typeOfCurrRegiEle = $prsLoadingEle.find('.typeOfCurrRegi');
            var currRegiDateEle = $prsLoadingEle.find('.currRegiDate');
            var praCerEndDateEle = $prsLoadingEle.find('.praCerEndDate');
            var typeOfRegisterEle = $prsLoadingEle.find('.typeOfRegister');
            if(needControlName){
                controlEdit(nameEle, propStyle, canEdit);
            }
            controlEdit(specialtyGetDateEle, propStyle, canEdit);
            controlEdit(typeOfCurrRegiEle, propStyle, canEdit);
            controlEdit(currRegiDateEle, propStyle, canEdit);
            controlEdit(praCerEndDateEle, propStyle, canEdit);
            controlEdit(typeOfRegisterEle, propStyle, canEdit);

            if ('EAS' == '${currentSvcCode}'){
                var specialityField = $prsLoadingEle.find('.specialityField');
                var relevantExperienceSpan = $prsLoadingEle.find('.relevantExperienceLabel').find('.mandatory');
                if(isEmpty(specialityField.text()) && !isEmpty($prsLoadingEle.find('input.profRegNo').val())){
                    if (relevantExperienceSpan.length==0){
                        $prsLoadingEle.find('.relevantExperienceLabel').append('<span class="mandatory">*</span>');
                    }
                    relevantExperienceSpan.removeClass('hidden');
                }else {
                    relevantExperienceSpan.addClass('hidden');
                }
            }
        }
    };

    function fillClinicalDirector($current, data, psnType) {
        if (isEmpty($current)) {
            return;
        }
        if (isEmpty(data)) {
            $current.addClass('hidden');
            clearFields($content);
            return;
        }
        console.log(data);
        $.each(data, function(i, val) {
            if (i == 'psnEditDto') {
                //console.info(val);
                if (data.licPerson) {
                    disableCdContent($current, val);
                }
            } else if(i == 'licPerson'){
                var licPerson = data.licPerson;
                // alert(licPerson);
                if (licPerson){
                    $current.closest('div.clinicalDirectorContent').find('input.licPerson').val('1');
                }else{
                    $current.closest('div.clinicalDirectorContent').find('input.licPerson').val('0');
                }
            } else if(i == 'speciality'){
                var speciality = data.speciality;
                if(isEmpty(speciality)){
                    $current.find('.specialty-label').html('');
                }else{
                    $current.find('.specialty-label').html(speciality);
                }

            } else if(i == 'specialtyGetDate'){
                var specialtyGetDateStr = data.specialtyGetDateStr;
                if(isEmpty(specialtyGetDateStr)){
                    $current.find('.specialtyGetDate').val('');
                }else{
                    $current.find('.specialtyGetDate').val(specialtyGetDateStr);
                }
            } else if(i == 'currRegiDate'){
                var currRegiDateStr = data.currRegiDateStr;
                if(isEmpty(currRegiDateStr)){
                    $current.find('.currRegiDate').val('');
                }else{
                    $current.find('.currRegiDate').val(currRegiDateStr);
                }
            } else if(i == 'praCerEndDate'){
                var praCerEndDateStr = data.praCerEndDateStr;
                if(isEmpty(praCerEndDateStr)){
                    $current.find('.praCerEndDate').val('');
                }else{
                    $current.find('.praCerEndDate').val(praCerEndDateStr);
                }
            } else if(i == 'aclsExpiryDate'){
                var aclsExpiryDateStr = data.aclsExpiryDateStr;
                if(isEmpty(aclsExpiryDateStr)){
                    $current.find('.aclsExpiryDate').val('');
                }else{
                    $current.find('.aclsExpiryDate').val(aclsExpiryDateStr);
                }
            } else if(i == 'bclsExpiryDate'){
                var bclsExpiryDateStr = data.bclsExpiryDateStr;
                if(isEmpty(bclsExpiryDateStr)){
                    $current.find('.bclsExpiryDate').val('');
                }else{
                    $current.find('.bclsExpiryDate').val(bclsExpiryDateStr);
                }
            } else {
                var $input = $current.find('.' + i + ':input');
                if ($input.length == 0) {
                    return;
                }
                var type = $input[0].type, tag = $input[0].tagName.toLowerCase();
                //console.info("Field - " + i + " : " + val);
                //console.info("Tag - " + tag + " : " + type);
                if (type == 'radio') {
                    $input.filter('[value="' + val + '"]').prop('checked', true);
                    $input.filter(':not([value="' + val + '"])').prop('checked', false);
                } else if (type == 'checkbox') {
                    if ($.isArray(val)) {
                        $input.prop('checked', false);
                        for (var v in val) {
                            if (curVal == v) {
                                $(this).prop('checked', true);
                            }
                        }
                    } else {
                        $input.filter('[value="' + val + '"]').prop('checked', true);
                        $input.filter(':not([value="' + val + '"])').prop('checked', false);
                    }
                } else if (tag == 'select') {
                    var oldVal = $input.val();
                    $input.val(val);
                    if (isEmpty($input.val())) {
                        $input[0].selectedIndex = 0;
                    }
                    if ($input.val() != oldVal) {
                        $input.niceSelect("update");
                    }
                    if(i == 'designation'){
                        designationChange($current, val);
                    }
                } else {
                    $input.val(val);
                }
            }
        });
        var prgNo = $current.find(('input.profRegNo')).val();
        console.info("prgNo: " + prgNo);
        if (!isEmpty(prgNo)) {
            $current.find('.profRegNo').trigger('blur');
        }
    }

    var assignSel = function (srcSelector, targetSelector) {
        var assignSelVal = $(srcSelector).val();
        // console.info(assignSelVal);
        var $content = $(targetSelector);
        // init
        unDisableContent($content);
        <c:if test="${'true' == canEdit}">
        $content.find('input.cdIndexNo').val('');
        </c:if>
        $content.find('.specialty-label').html('');
        $content.find('div.other-designation').addClass('hidden');
        if('-1' == assignSelVal) {
            $content.addClass('hidden');
            clearFields($content);
        } else if('newOfficer' == assignSelVal) {
            $content.removeClass('hidden');
            clearFields($content);
            $content.closest('div.clinicalDirectorContent').find('input.licPerson').val('0');
        } else {
            $content.removeClass('hidden');
            var arr = assignSelVal.split(',');
            var nationality = arr[0];
            var idType = arr[1];
            var idNo = arr[2];
            loadSelectPsn($content, nationality, idType, idNo, 'CD', fillClinicalDirector);
        }
    }
    var doEdite = function () {
        $('a.cdEdit').click(function () {
            var $currContent = $(this).closest('div.clinicalDirectorContent');
            $currContent.find('input.isPartEdit').val('1');
            $currContent.find('.edit-content').addClass('hidden');
            $currContent.find('input[type="text"]').prop('disabled', false);
            $currContent.find('select').prop('disabled', false);
            $currContent.find('div.nice-select').removeClass('disabled');
            $currContent.find('input[type="text"]').css('border-color', '');
            $currContent.find('input[type="text"]').css('color', '');
            $currContent.find('.date_picker').removeClass('disabled-placeHolder');
            $currContent.find('input.holdCerByEMS').each(function () {
                $(this).closest('div').find('label span.check-circle').removeClass('radio-disabled');
            });
            $currContent.find('input[type="checkbox"]').prop('disabled',false);
            $currContent.find('input[type="radio"]').prop('disabled',false);
            $currContent.find('.assignSel').prop('disabled',false);
            $('#isEditHiddenVal').val('1');

            var appType = $('input[name="applicationType"]').val();
            var assignSelectVal = $currContent.find('select.assignSel').val();
            var licPerson = $currContent.find('input.licPerson').val();
            var prgNo = $currContent.find('input.profRegNo').val();
            if(!isEmpty(prgNo)){
                var needControlName = isNeedControlName(assignSelectVal, licPerson, appType);
                prsCallBackFuns.setEdit($currContent, 'disabled', false, needControlName);
            }
        });
    }


</script>