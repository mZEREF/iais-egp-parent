<c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}" />
<c:set var="familyPlanDto" value="${terminationOfPregnancyDto.familyPlanDto}" />
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
                    <a onclick="printData()" href="javascript:void(0);"><em class="fa fa-pencil-square-o">&nbsp;</em> Edit</a>
                </a>
            </c:if>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Contraceptive History" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="contraHistory" firstOption="Please Select" codeCategory="TOP_CONTRACEPTIVE_HISTORY"
                         value="${familyPlanDto.contraHistory}" cssClass="contraHistory"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Most Recent Contraceptive Methods Used" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="mostRecentContraMethod" firstOption="Please Select" codeCategory="TOP_CONTRACEPTIVE_METHODS"
                         value="${familyPlanDto.mostRecentContraMethod}" cssClass="mostRecentContraMethod"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="No. of Previous Termination of Pregnancy" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="2" type="text" name="previousTopNumber" value="${familyPlanDto.previousTopNumber}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="First Day of Last Menstrual Period" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="firstDayOfLastMenstPer" value="${familyPlanDto.firstDayOfLastMenstPer}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Gestation Age based on Ultrasound"/>
        <iais:value width="7" cssClass="col-md-7">
            <div class="form-group">
                <label class="col-xs-5 col-md-4 control-label"><strong>(1)Weeks<%--<span class="mandatory">*</span>--%></strong></label>
                    <div class="col-sm-7 col-md-5 col-xs-7 col-md-7">
                        <input maxLength="2" type="text" name="gestAgeBaseOnUltrWeek" value="${familyPlanDto.gestAgeBaseOnUltrWeek}" />
                    </div>
            </div>
            <div class="form-group">
                <label class="col-xs-5 col-md-4 control-label"><strong>(2)Days</strong></label>
                <div class="col-sm-7 col-md-5 col-xs-7 col-md-7">
                    <input maxLength="2" type="text" name="gestAgeBaseOnUltrDay" value="${familyPlanDto.gestAgeBaseOnUltrDay}"/>
                </div>
            </div>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Gestation Age not based on Ultrasound"/>
        <iais:value width="7" cssClass="col-md-7">
            <div class="form-group">
                <label class="col-xs-5 col-md-4 control-label"><strong>(1)Weeks<%--<span class="mandatory">*</span>--%></strong></label>
                <div class="col-sm-7 col-md-5 col-xs-7 col-md-7">
                    <input maxLength="2" type="text" name="gestAgeBaseNotOnUltrWeek" value="${familyPlanDto.gestAgeBaseNotOnUltrWeek}"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-xs-5 col-md-4 control-label"><strong>(2)Days</strong></label>
                <div class="col-sm-7 col-md-5 col-xs-7 col-md-7">
                    <input maxLength="2" type="text" name="gestAgeBaseNotOnUltrDay" value="${familyPlanDto.gestAgeBaseNotOnUltrDay}" />
                </div>
            </div>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Gender of the Aborted Child if Gestation Age is 15 weeks and above"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="abortChdMoreWksGender" firstOption="Please Select" codeCategory="TOP_GENDER_OF_PREGNANT_CHILDREN"
                         value="${familyPlanDto.abortChdMoreWksGender}" cssClass="abortChdMoreWksGender"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Main Reason for Request to Terminate Pregnancy" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="mainTopReason" firstOption="Please Select" codeCategory="TOP_REASONS_TERMINATION_PREGNANCY"
                         value="${familyPlanDto.mainTopReason}" cssClass="mainTopReason"/>
        </iais:value>
    </iais:row>
    <div id="otherMainTopReason" <c:if test="${familyPlanDto.mainTopReason!='TOPRTP008'}">style="display: none"</c:if> >
        <iais:row>
            <iais:field width="5" value="Other Main Reason for Termination of Pregnancy"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="2" type="text" name="otherMainTopReason" value="${familyPlanDto.otherMainTopReason}" />
            </iais:value>
        </iais:row>
    </div>
    <iais:row>
        <iais:field width="5" value="Sub Reason for Request to Terminate Pregnancy"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="subRopReason" firstOption="Please Select" codeCategory="TOP_SUB_CAUSES_TERMINATION_PREGNANCY"
                         value="${familyPlanDto.subRopReason}" cssClass="subRopReason"/>
        </iais:value>
    </iais:row>
    <div id="otherSubTopReason" <c:if test="${familyPlanDto.subRopReason!='TOPSCTP009'}">style="display: none"</c:if> >
        <iais:row>
            <iais:field width="5" value="Other Sub Reason for Request to Terminate Pregnancy" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="2" type="text" name="otherSubTopReason" value="${familyPlanDto.otherSubTopReason}" />
                <span class="error-msg" name="iaisErrorMsg" id="error_otherSubTopReason"></span>
            </iais:value>
        </iais:row>
    </div>
</div>
<script>
    $(document).ready(function() {
        $('#subRopReason').change(function () {

            var subRopReason= $('#subRopReason option:selected').val();

            if(subRopReason == "TOPSCTP009"){
                $('#otherSubTopReason').attr("style","display: block");
            }else {
                $('#otherSubTopReason').attr("style","display: none");
            }
        });
    });
    $(document).ready(function() {
        $('#mainTopReason').change(function () {

            var mainTopReason= $('#mainTopReason option:selected').val();

            if(mainTopReason == "TOPRTP008"){
                $('#otherMainTopReason').attr("style","display: block");
            }else {
                $('#otherMainTopReason').attr("style","display: none");
            }
        });
    });
</script>