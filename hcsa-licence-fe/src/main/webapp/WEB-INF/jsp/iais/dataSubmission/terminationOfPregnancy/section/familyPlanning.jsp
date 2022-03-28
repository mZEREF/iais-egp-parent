<c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}" />
<c:set var="familyPlanDto" value="${terminationOfPregnancyDto.familyPlanDto}" />
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="form-horizontal patientPatails">
    <iais:row>
        <iais:field width="5" value="Contraceptive History" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="contraHistory" firstOption="Please Select" codeCategory="TOP_CONTRACEPTIVE_HISTORY"
                         value="${familyPlanDto.contraHistory}" cssClass="contraHistory"/>
        </iais:value>
    </iais:row>
    <div id="mostRecentContraMethods" <c:if test="${familyPlanDto.gestAgeBaseOnUltrWeek!='TOPCH001'}">style="display: none"</c:if> >
        <iais:row>
            <iais:field width="5" value="Most Recent Contraceptive Methods Used" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="mostRecentContraMethod" firstOption="Please Select" codeCategory="TOP_CONTRACEPTIVE_METHODS"
                             value="${familyPlanDto.mostRecentContraMethod}" cssClass="mostRecentContraMethod"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_mostRecentContraMethod"></span>
            </iais:value>
        </iais:row>
    </div>
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
                <label class="col-xs-5 col-md-4 control-label"><strong>(1)Weeks<span class="mandatory">*</span></strong></label>
                    <div class="col-sm-7 col-md-5 col-xs-7 col-md-7">
                        <input maxLength="2" type="text" name="gestAgeBaseOnUltrWeek" value="${familyPlanDto.gestAgeBaseOnUltrWeek}" />
                        <span class="error-msg" name="iaisErrorMsg" id="error_gestAgeBaseOnUltrWeek"></span>
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
    <div id="abortChdMoreWksGenders" <c:if test="${familyPlanDto.gestAgeBaseOnUltrWeek<15}">style="display: none"</c:if> >
        <iais:row>
            <iais:field width="5" value="Gender of the Aborted Child if Gestation Age is 15 weeks and above" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="abortChdMoreWksGender" firstOption="Please Select" codeCategory="TOP_GENDER_OF_PREGNANT_CHILDREN"
                             value="${familyPlanDto.abortChdMoreWksGender}" cssClass="abortChdMoreWksGender"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_abortChdMoreWksGender"></span>
            </iais:value>
        </iais:row>
    </div>
    <iais:row>
        <iais:field width="5" value="Main Reason for Request to Terminate Pregnancy" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="mainTopReason" firstOption="Please Select" codeCategory="TOP_REASONS_TERMINATION_PREGNANCY"
                         value="${familyPlanDto.mainTopReason}" cssClass="mainTopReason"/>
        </iais:value>
    </iais:row>
    <div id="topRiskConditions" <c:if test="${familyPlanDto.mainTopReason!='TOPRTP005'}">style="display: none"</c:if> >
        <iais:row>
            <iais:field width="5" value="Indicate the Maternal High Risk condition(s) that led to the Request to Terminate Pregnancy" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="66" type="text" name="topRiskCondition" value="${familyPlanDto.topRiskCondition}" />
                <span class="error-msg" name="iaisErrorMsg" id="error_topRiskCondition"></span>
            </iais:value>
        </iais:row>
    </div>
    <div id="topMedConditions" <c:if test="${familyPlanDto.mainTopReason!='TOPRTP002'}">style="display: none"</c:if> >
        <iais:row>
            <iais:field width="5" value="Indicate the Medical Condition(s) that led to the Request to Terminate Pregnancy" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="66" type="text" name="topMedCondition" value="${familyPlanDto.topMedCondition}" />
                <span class="error-msg" name="iaisErrorMsg" id="error_topMedCondition"></span>
            </iais:value>
        </iais:row>
    </div>
    <div id="otherMainTopReason" <c:if test="${familyPlanDto.mainTopReason!='TOPRTP008'}">style="display: none"</c:if> >
        <iais:row>
            <iais:field width="5" value="Other Main Reason for Termination of Pregnancy" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="66" type="text" name="otherMainTopReason" value="${familyPlanDto.otherMainTopReason}" />
                <span class="error-msg" name="iaisErrorMsg" id="error_otherMainTopReason"></span>
            </iais:value>
        </iais:row>
    </div>
    <div id="subRopReasons" <c:if test="${familyPlanDto.mainTopReason!='TOPRTP004'}">style="display: none"</c:if> >
        <iais:row>
            <iais:field width="5" value="Type of Fetal Anomalies" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="subRopReason" firstOption="Please Select" codeCategory="TOP_SUB_CAUSES_TERMINATION_PREGNANCY"
                             value="${familyPlanDto.subRopReason}" cssClass="subRopReason"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_subRopReason"></span>
            </iais:value>
        </iais:row>
    </div>
    <div id="otherSubTopReason" style="${familyPlanDto.subRopReason ==null || familyPlanDto.subRopReason !='TOPSCTP003' || !familyPlanDto.subRopReason eq 'TOPSCTP006' ? 'display: none' : ''}" >
        <iais:row>
            <iais:field width="5" value="Other Type of Fetal Anomalies (Please specify)" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="66" type="text" name="otherSubTopReason" value="${familyPlanDto.otherSubTopReason}" />
                <span class="error-msg" name="iaisErrorMsg" id="error_otherSubTopReason"></span>
            </iais:value>
        </iais:row>
    </div>
</div>
<script>
    $(document).ready(function() {
        subRopReason();
       mainTopReason();
        mainTopReason();
        gestAgeBaseOnUltrWeek();
        contraHistory();
    });
    function subRopReason() {
        $('#subRopReason').change(function () {

            var subRopReason= $('#subRopReason option:selected').val();

            if(subRopReason == "TOPSCTP003" || subRopReason == "TOPSCTP006"){
                $('#otherSubTopReason').attr("style","display: block");
            }else {
                $('#otherSubTopReason').attr("style","display: none");
            }
        });
    }
    function mainTopReason() {
        $('#mainTopReason').change(function () {

            var mainTopReason= $('#mainTopReason option:selected').val();

            if(mainTopReason == "TOPRTP008"){
                $('#otherMainTopReason').attr("style","display: block");
            }else {
                $('#otherMainTopReason').attr("style","display: none");
            }
            if(mainTopReason == "TOPRTP005"){
                $('#topRiskConditions').attr("style","display: block");
            }else {
                $('#topRiskConditions').attr("style","display: none");
            }
            if(mainTopReason == "TOPRTP002"){
                $('#topMedConditions').attr("style","display: block");
            }else {
                $('#topMedConditions').attr("style","display: none");
            }
        });
    }
    function mainTopReason(){
        $('#mainTopReason').change(function () {

            var mainTopReason= $('#mainTopReason option:selected').val();

            if(mainTopReason == "TOPRTP004"){
                $('#subRopReasons').attr("style","display: block");
            }else {
                $('#subRopReasons').attr("style","display: none");
            }
        });
    }
    function gestAgeBaseOnUltrWeek(){
        $('[name=gestAgeBaseOnUltrWeek]').change(function () {
            console.log("1");
            var gestAgeBaseOnUltrWeek= $('[name=gestAgeBaseOnUltrWeek]').val();

            if(gestAgeBaseOnUltrWeek >="15"){
                $('#abortChdMoreWksGenders').attr("style","display: block");
            }else {
                $('#abortChdMoreWksGenders').attr("style","display: none");
            }
        });
    }
    function contraHistory(){
        $('#contraHistory').change(function () {

            var contraHistory= $('#contraHistory option:selected').val();

            if(contraHistory == "TOPCH001"){
                $('#mostRecentContraMethods').attr("style","display: block");
            }else {
                $('#mostRecentContraMethods').attr("style","display: none");
            }
        });
    }
</script>