<c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}" />
<c:set var="postTerminationDto" value="${terminationOfPregnancyDto.postTerminationDto}" />
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="form-horizontal patientPatails">
    <iais:row>
        <iais:value width="6" cssClass="col-md-6">
            <%--<strong class="app-font-size-22 premHeader">title</strong>--%>
        </iais:value>
        <iais:value width="6" cssClass="col-md-6 text-right editDiv">
            <c:if test="${canEdit}">
                <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                <a id="edit" class="text-right app-font-size-16">
                    <em class="fa fa-pencil-square-o">&nbsp;</em> Edit
                </a>
            </c:if>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Whether given Post-counselling" mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="givenPostCounselling"
                       value="1"
                       id="radioYes"
                       <c:if test="${postTerminationDto.givenPostCounselling}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="radioYes"><span
                        class="check-circle"></span>Yes</label>
            </div>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="givenPostCounselling"
                       value="0"
                       id="radioNo"
                       <c:if test="${postTerminationDto.givenPostCounselling == 'false'}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="radioNo"><span
                        class="check-circle"></span>No</label>
            </div>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="If post-counselling was not given"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="ifCounsellingNotGiven" firstOption="Please Select" codeCategory=""
                         value="${postTerminationDto.ifCounsellingNotGiven}" cssClass="ifCounsellingNotGiven"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_ifCounsellingNotGiven"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Post-Termination Counsellor ID Type"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="counsellorIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                         value="${postTerminationDto.counsellorIdType}" cssClass="counsellorIdType"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellorIdType"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Post-Termination Counsellor ID No."/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" name="counsellorIdNo" value="${postTerminationDto.counsellorIdNo}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellorIdNo"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Doctor Professional Regn No."/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="counsellorName" value="${postTerminationDto.counsellorName}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellorName"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Post-Counselling Date"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="counsellingDate" value="${postTerminationDto.counsellingDate}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellingDate"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Place of Post-Counselling"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="counsellingPlace" value="${postTerminationDto.counsellingPlace}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellingPlace"></span>
        </iais:value>
    </iais:row>
</div>