<c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}"/>
<c:set var="familyPlanDto" value="${terminationOfPregnancyDto.familyPlanDto}"/>
<c:set var="patientInformationDto" value="${terminationOfPregnancyDto.patientInformationDto}"/>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="form-horizontal patientPatails">
    <iais:row>
        <iais:field width="5" value="Contraceptive History" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="contraHistory" firstOption="Please Select" codeCategory="TOP_CONTRACEPTIVE_HISTORY"
                         value="${familyPlanDto.contraHistory}" id="contraHistorys" cssClass="contraHistory"/>
        </iais:value>
    </iais:row>
    <div id="mostRecentContraMethods" <c:if test="${familyPlanDto.contraHistory!='TOPCH003'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Most Recent Contraceptive Methods Used" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="mostRecentContraMethod" firstOption="Please Select" codeCategory="TOP_CONTRACEPTIVE_METHODS"
                             value="${familyPlanDto.mostRecentContraMethod}" id="contraMethod" cssClass="mostRecentContraMethod"/>
            </iais:value>
        </iais:row>
    </div>
        <div id="otherContraMethods" <c:if test="${familyPlanDto.mostRecentContraMethod!='TOPMRC007'}">style="display: none"</c:if>>
        <iais:row>
            <label class="col-xs-5 col-md-4 control-label">Other Contraceptive Method Used
                <span id="otherContraMethod" class="mandatory">
                    <c:if test="${familyPlanDto.contraHistory =='TOPCH003' && familyPlanDto.mostRecentContraMethod =='TOPMRC007'}">*</c:if>
                </span>
            </label>
            <%--<iais:field width="5" id="otherContraMethodLabel" value="Other Contraceptive Method Used"
                        mandatory="${familyPlanDto.mostRecentContraMethod eq 'TOPMRC007' ? true : false}"/>--%>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="100" type="text" name="otherContraMethod"  id="otherContraMethodText" value="${familyPlanDto.otherContraMethod}"/>
            </iais:value>
        </iais:row>
        </div>
    <iais:row>
        <iais:field width="5" value="No. of Previous Termination(s) of Pregnancy" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="2" type="text" name="previousTopNumber" value="${familyPlanDto.previousTopNumber}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_previousTopNumber"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="First Day of Last Menstrual Period" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="firstDayOfLastMenstPer" value="${familyPlanDto.firstDayOfLastMenstPer}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_firstDayOfLastMenstPer"></span>
        </iais:value>
    </iais:row>
    <%--<iais:row>
        <iais:field width="5" value="Patient Age(Years)"/>
        <iais:value width="7" cssClass="col-md-7" display="true" id="age">
            ${patientInformationDto.patientAge}
        </iais:value>
    </iais:row>--%>

    <iais:row>
        <iais:field width="5" value="Gestation Age based on Ultrasound"/>
        <iais:value width="7" cssClass="col-md-7">
            <div class="form-group">
                <label class="col-xs-5 col-md-4 control-label"><strong>(1)Weeks<span class="mandatory">*</span></strong></label>
                    <div class="col-sm-7 col-md-5 col-xs-7 col-md-7">
                        <input maxLength="2" type="text" name="gestAgeBaseOnUltrWeek" value="<c:out value="${familyPlanDto.gestAgeBaseOnUltrWeek}"/>" />
                        <span class="error-msg" name="iaisErrorMsg" id="error_gestAgeBaseOnUltrWeek"></span>
                    </div>
            </div>
            <div class="form-group">
                <label class="col-xs-5 col-md-4 control-label"><strong>(2)Days<span class="mandatory">*</span></strong></label>
                <div class="col-sm-7 col-md-5 col-xs-7 col-md-7">
                    <input maxLength="2" type="text" name="gestAgeBaseOnUltrDay" value="<c:out value="${familyPlanDto.gestAgeBaseOnUltrDay}"/>"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_gestAgeBaseOnUltrDay"></span>
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
                    <input maxLength="2" type="text" name="gestAgeBaseNotOnUltrWeek" value="<c:out value="${familyPlanDto.gestAgeBaseNotOnUltrWeek}"/>"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_gestAgeBaseNotOnUltrWeek"></span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-xs-5 col-md-4 control-label"><strong>(2)Days</strong></label>
                <div class="col-sm-7 col-md-5 col-xs-7 col-md-7">
                    <input maxLength="2" type="text" name="gestAgeBaseNotOnUltrDay" value="<c:out value="${familyPlanDto.gestAgeBaseNotOnUltrDay}"/>" />
                    <span class="error-msg" name="iaisErrorMsg" id="error_gestAgeBaseNotOnUltrDay"></span>
                </div>
            </div>
        </iais:value>
    </iais:row>
    <div id="abortChdMoreWksGenders" <c:if test="${familyPlanDto.gestAgeBaseOnUltrWeek==null || !(familyPlanDto.gestAgeBaseOnUltrWeek).matches('[0-9]+') || familyPlanDto.gestAgeBaseOnUltrWeek<15}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Gender of the Aborted Child if Gestation Age is 15 weeks and above" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="abortChdMoreWksGender" firstOption="Please Select" codeCategory="TOP_GENDER_OF_PREGNANT_CHILDREN_UN"
                             value="${familyPlanDto.abortChdMoreWksGender}" cssClass="abortChdMoreWksGender"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_abortChdMoreWksGender"></span>
            </iais:value>
        </iais:row>
    </div>
    <iais:row>
        <iais:field width="5" value="Main Reason for Request to Terminate Pregnancy" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select  id = "mainTopReason" name="mainTopReason" firstOption="Please Select" codeCategory="TOP_REASONS_TERMINATION_PREGNANCY"
                         value="${familyPlanDto.mainTopReason}" cssClass="mainTopReason"/>
        </iais:value>
    </iais:row>
    <div id="topRiskConditions" <c:if test="${familyPlanDto.mainTopReason!='TOPRTP004'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Indicate the Maternal High Risk condition(s) that led to the Request to Terminate Pregnancy" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="100" type="text" name="topRiskCondition" value="${familyPlanDto.topRiskCondition}"/>
            </iais:value>
        </iais:row>
    </div>
    <div id="topMedConditions" <c:if test="${familyPlanDto.mainTopReason!='TOPRTP006'}">style="display: none"</c:if> >
        <iais:row>
            <iais:field width="5" value="Indicate the Medical Condition(s) that led to the Request to Terminate Pregnancy" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="100" type="text" name="topMedCondition" value="${familyPlanDto.topMedCondition}"/>
            </iais:value>
        </iais:row>
    </div>
    <div id="otherMainTopReason" <c:if test="${familyPlanDto.mainTopReason!='TOPRTP011'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Other Reason for Termination of Pregnancy" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="1000" type="text" name="otherMainTopReason" value="${familyPlanDto.otherMainTopReason}"/>
            </iais:value>
        </iais:row>
    </div>
    <div id="subRopReasons" <c:if test="${familyPlanDto.mainTopReason!='TOPRTP003'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Type of Fetal Anomalies" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="subRopReason" firstOption="Please Select" id="subRopReason" codeCategory="TOP_SUB_CAUSES_TERMINATION_PREGNANCY"
                             value="${familyPlanDto.subRopReason}" cssClass="subRopReason"/>
            </iais:value>
        </iais:row>
    </div>
    <div id="otherSubTopReasons" <c:if test="${(familyPlanDto.mainTopReason!='TOPRTP003' && familyPlanDto.mainTopReason!='TOPRTP006') || (familyPlanDto.subRopReason != 'TOPSCTP007' && familyPlanDto.subRopReason != 'TOPSCTP008')}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Other Type of Fetal Anomalies (Please specify)" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="1000" type="text" name="otherSubTopReason" id="otherSubTopReasonsText" value="${familyPlanDto.otherSubTopReason}"/>
            </iais:value>
        </iais:row>
    </div>
</div>
<input type="hidden" value="${showPatientAgePT}" id="showPatientAgePts" >
<script>
    $(document).ready(function() {
        otherContraMethod();
        $('#contraMethod').change(function () {
            contraMethod();
        });
        $('#subRopReason').change(function () {
            subRopReason();
        });
        $('#mainTopReason').change(function () {
        otherMainTopReason();
        });
        topRiskConditions();
        topMedConditions();
        subRopReasons();
        gestAgeBaseOnUltrWeek();
        contraHistory();
    });
    function contraMethod(){
        var mostRecentContraMethod= $('#contraMethod').val();
        if(mostRecentContraMethod == "TOPMRC007"){
            $('#otherContraMethods').show();
        }else{
            $('#otherContraMethods').hide();
        }
    }

    function otherContraMethod(){
        $('#contraMethod,#contraHistorys').change(function () {
            var contraHistory = $('#contraHistorys').val();
            var mostRecentContraMethod = $('#contraMethod').val();
            if(mostRecentContraMethod == "TOPMRC007" && contraHistory == "TOPCH003"){
                $('#otherContraMethod').text('*');
            }else {
                $('#otherContraMethod').text('');
            }
        });
    }

    function subRopReason() {
        var subRopReason= $('#subRopReason').val();
        if(subRopReason == "TOPSCTP007" || subRopReason == "TOPSCTP008"){
            $('#otherSubTopReasons').show();
        }else{
            $('#otherSubTopReasons').hide();
        }
    }
    function otherMainTopReason() {
            var mainTopReason= $('#mainTopReason').val();

            if(mainTopReason == "TOPRTP011"){
                $('#otherMainTopReason').show();
            }else {
                $('#otherMainTopReason').hide();
            }
    }
    function topRiskConditions() {
        $('#mainTopReason').change(function () {

            var mainTopReason= $('#mainTopReason').val();

            if(mainTopReason == "TOPRTP004"){
                $('#topRiskConditions').show();
            }else {
                $('#topRiskConditions').hide();
            }
        });
    }
    function topMedConditions() {
        $('#mainTopReason').change(function () {

            var mainTopReason= $('#mainTopReason').val();

            if(mainTopReason == "TOPRTP006"){
                $('#topMedConditions').show();
            }else {
                $('#topMedConditions').hide();
            }
        });
    }

    function subRopReasons(){
        $('#mainTopReason').change(function () {

            var mainTopReason= $('#mainTopReason').val();

            if(mainTopReason == "TOPRTP003"){
                $('#subRopReasons').show();
            }else {
                $('#subRopReasons').hide();
                $('#otherSubTopReasons').hide();
                fillValue($('#subRopReasons'),null);
                $('#otherSubTopReasonsText').val(null);
            }
        });
    }
    function gestAgeBaseOnUltrWeek(){
        $('[name=gestAgeBaseOnUltrWeek]').change(function () {
            console.log("1");
            var gestAgeBaseOnUltrWeek= $('[name=gestAgeBaseOnUltrWeek]').val();

            if(gestAgeBaseOnUltrWeek >=15 && !isNaN(gestAgeBaseOnUltrWeek)){
                $('#abortChdMoreWksGenders').show();
            }else {
                $('#abortChdMoreWksGenders').hide();
            }
        });
    }
    function contraHistory(){
        $('#contraHistorys').change(function () {

            var contraHistory= $('#contraHistorys').val();

            if(contraHistory == "TOPCH003"){
                $('#mostRecentContraMethods').show();
            }else {
                $('#mostRecentContraMethods').hide();
                $('#otherContraMethods').hide();
                fillValue($('#mostRecentContraMethods'),null);
                $('#otherContraMethodText').val(null);
            }
        });
    }

    $(document).ready(function() {
        if ("1" == $('#showPatientAgePts').val()) {
            $('#showPatientAgePT').modal('show');
        }
    });

</script>