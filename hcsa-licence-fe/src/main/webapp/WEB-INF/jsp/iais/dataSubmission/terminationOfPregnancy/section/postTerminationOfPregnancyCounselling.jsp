<c:set var="preTerminationDto" value="${terminationOfPregnancyDto.preTerminationDto}" />
<c:if test="${preTerminationDto.secCounsellingResult !='TOPSP001' && preTerminationDto.secCounsellingResult !='TOPSP002'}">
<c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}" />
<c:set var="postTerminationDto" value="${terminationOfPregnancyDto.postTerminationDto}" />
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="form-horizontal patientPatails">
    <iais:row>
        <iais:field width="5" value="Whether given Post-counselling" mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="givenPostCounselling"
                       value="1"
                       id="radioYes"
                       <c:if test="${postTerminationDto.givenPostCounselling == true}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="radioYes"><span
                        class="check-circle"></span>Yes</label>
            </div>
            <span class="error-msg" name="iaisErrorMsg" id="error_givenPostCounselling"></span>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="givenPostCounselling"
                       value="0"
                       id="radioNo"
                       <c:if test="${postTerminationDto.givenPostCounselling == false}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="radioNo"><span
                        class="check-circle"></span>No</label>
            </div>
        </iais:value>
    </iais:row>
    <div id="counsellingRslts" <c:if test="${postTerminationDto.givenPostCounselling == false}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Post-Counselling Result"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="counsellingRslt" firstOption="Please Select" codeCategory="TOP_POST_COUNSELLING_RESULT"
                             value="${postTerminationDto.counsellingRslt}" cssClass="counsellingRslt" onchange="checkMantory(this, '#otherCounsellorIdTypeLabel', 'TOPCR007')"/>
            </iais:value>
        </iais:row>
    </div>
    <iais:row>
        <iais:field width="5" id="otherCounsellorIdTypeLabel" value="Post-Counselling Result - Others" mandatory="${postTerminationDto.counsellingRslt eq 'TOPCR007' ? true : false}"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" name="otherCounsellingRslt" value="${postTerminationDto.otherCounsellingRslt}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_otherCounsellingRslt"></span>
        </iais:value>
    </iais:row>

    <div id="ifCounsellingNotGivens" <c:if test="${postTerminationDto.givenPostCounselling !=false}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Reason If post-counselling was not given"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="100" type="text" name="ifCounsellingNotGiven" value="${postTerminationDto.ifCounsellingNotGiven}" />
            </iais:value>
        </iais:row>
    </div>
    <iais:row>
        <c:set var="toolMsg"><iais:message key="DS_MSG014" paramKeys="1" paramValues="counsellor"/></c:set>
        <iais:field width="5" id="counsellorIdTypeLabel" value="Post-Termination Counsellor ID Type"
                    mandatory="${preTerminationDto.counsellingGiven != true ? false : preTerminationDto.counsellingGiven }" info="${toolMsg}"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="counsellorIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                         value="${postTerminationDto.counsellorIdType}" cssClass="counsellorIdType"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellorIdType"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">Post-Termination Counsellor ID No.
            <span id="counsellorIdNo" class="mandatory">
                <c:if test="${postTerminationDto.givenPostCounselling == true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" name="counsellorIdNo" value="${postTerminationDto.counsellorIdNo}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellorIdNo"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">Post Termination Counsellor Name
            <span id="counsellorName" class="mandatory">
                <c:if test="${postTerminationDto.givenPostCounselling == true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="counsellorName" value="${postTerminationDto.counsellorName}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellorName"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">Post-Counselling Date
            <span id="counsellingDate" class="mandatory">
                <c:if test="${postTerminationDto.givenPostCounselling == true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="counsellingDate" value="${postTerminationDto.counsellingDate}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellingDate"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">Place of Post-Counselling
            <span id="counsellingPlace" class="mandatory">
                <c:if test="${postTerminationDto.givenPostCounselling == true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="counsellingPlace" firstOption="Please Select" codeCategory="TOP_PRE_COUNSELLING_PLACE"
                         value="${postTerminationDto.counsellingPlace}" cssClass="counsellingPlace"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellingPlace"></span>
        </iais:value>
    </iais:row>
</div>
</c:if>
<script>
    $(document).ready(function() {
        counsellingRslt();
        otherCounsellingRslt();
        ifCounsellingNotGiven();
    });
    $(document).ready(function () {
        $('input[name=givenPostCounselling]').change(function () {
            if($('#radioYes').prop('checked')) {
                $('#counsellorIdNo').text('*');
                $('#counsellorName').text('*');
                $('#counsellingDate').text('*');
                $('#counsellingPlace').text('*');
            }
            if($('#radioNo').prop('checked')) {
                $('#counsellorIdNo').text('');
                $('#counsellorName').text('');
                $('#counsellingDate').text('');
                $('#counsellingPlace').text('');
            }
            checkMantory('#radioYes', "#counsellorIdTypeLabel");
        });
    });
    function otherCounsellingRslt(){
        /*$('#counsellingRslt').change(function () {

            var counsellingRslt = $('#counsellingRslt option:selected').val();

            if (counsellingRslt == "TOPCR007") {
                $('#otherCounsellingRslt').attr("style", "display: block");
            } else {
                $('#otherCounsellingRslt').attr("style", "display: none");
            }
        });*/
        $('input[name=counsellingRslt]').change(function () {
            checkMantory('#counsellingRslt', "#otherCounsellorIdTypeLabel");
        });
    }
    function counsellingRslt(){
        $('input[name=givenPostCounselling]').change(function () {
            if($('#radioYes').prop('checked')) {
                $('#counsellingRslts').show();
            } else {
                $('#counsellingRslts').hide();
            }
        });
    }
    function ifCounsellingNotGiven(){
        $('input[name=givenPostCounselling]').change(function () {
            var givenPostCounselling= $('input[name=givenPostCounselling]').val();
            if($('#radioNo').prop('checked')) {
                $('#ifCounsellingNotGivens').show();
            } else if($('#radioYes').prop('checked') || givenPostCounselling == null){
                $('#ifCounsellingNotGivens').hide();
            }
        });
    }
</script>