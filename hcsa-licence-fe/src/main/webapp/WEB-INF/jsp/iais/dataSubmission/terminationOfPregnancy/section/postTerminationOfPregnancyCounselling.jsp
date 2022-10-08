<c:set var="preTerminationDto" value="${terminationOfPregnancyDto.preTerminationDto}"/>
<c:if test="${preTerminationDto.secCounsellingResult !='TOPSP001' && preTerminationDto.secCounsellingResult !='TOPSP003' && preTerminationDto.counsellingResult!='TOPPCR003'}">

    <c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}"/>
    <c:set var="postTerminationDto" value="${terminationOfPregnancyDto.postTerminationDto}"/>
    <%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
    <div class="form-horizontal patientPatails">
        <iais:row>
            <iais:field width="5" value="Whether Given Counselling" mandatory="true"/>
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
        <div id="counsellingRslts"
             <c:if test="${postTerminationDto.givenPostCounselling !=true}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Result of Counselling" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:select name="counsellingRslt" firstOption="Please Select"
                                 codeCategory="TOP_POST_COUNSELLING_RESULT"
                                 value="${postTerminationDto.counsellingRslt}" cssClass="counsellingRslt"
                                 id="otherCounsellorIdTypeLabel"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_counsellingRslt"></span>
                </iais:value>
            </iais:row>
        </div>
        <div id="otherCounsellingRslts"
             <c:if test="${postTerminationDto.counsellingRslt != 'TOPCR007' || postTerminationDto.givenPostCounselling !=true}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Result of Counselling - Others" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input maxLength="100" type="text" name="otherCounsellingRslt"
                                value="${postTerminationDto.otherCounsellingRslt}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_otherCounsellingRslt"></span>
                </iais:value>
            </iais:row>
        </div>
        <div id="ifCounsellingNotGivens"
             <c:if test="${postTerminationDto.givenPostCounselling !=false}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Reason for No Counselling" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input maxLength="100" type="text" name="ifCounsellingNotGiven"
                                value="${postTerminationDto.ifCounsellingNotGiven}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_ifCounsellingNotGiven"></span>
                </iais:value>
            </iais:row>
        </div>
        <div id="numGivenPostCounselling"
             <c:if test="${postTerminationDto.givenPostCounselling !=true}">style="display: none"</c:if>>
            <iais:row>
                <c:set var="toolMsg"><iais:message key="DS_MSG014" paramKeys="1" paramValues="counsellor"/></c:set>
                <iais:field width="5" value="Counsellor ID Type"
                            mandatory="true" info="${toolMsg}"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:select name="counsellorIdType" firstOption="Please Select"
                                 codeCategory="CATE_ID_DS_ID_TYPE_DTV"
                                 value="${postTerminationDto.counsellorIdType}" cssClass="counsellorIdType"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_counsellorIdType"></span>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field width="5" value="Counsellor ID No." mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input maxLength="20" type="text" name="counsellorIdNo"
                                value="${postTerminationDto.counsellorIdNo}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_counsellorIdNo"></span>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field width="5" value="Name of Counsellor" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input maxLength="66" type="text" name="counsellorName"
                                value="${postTerminationDto.counsellorName}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_counsellorName"></span>
                </iais:value>
            </iais:row>
        </div>
        <div id="numCounsellingGivenDoc"
             <c:if test="${postTerminationDto.givenPostCounselling !=true}">style="display: none"</c:if> >
            <iais:row>
                <c:set var="toolMsgPost"><iais:message key="DS_MSG018" escape="false" paramKeys="1"
                                                       paramValues="patient"/></c:set>
                <iais:field width="5" value="Doctor's Professional Regn / MCR No." info="${toolMsgPost}"
                            style="padding-right: 0px;"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input maxLength="20" type="text" name="counsellingReignNo"
                                value="${postTerminationDto.counsellingReignNo}"/>
                </iais:value>
            </iais:row>
        </div>
        <div id="numGivenPostCounsellings"
             <c:if test="${postTerminationDto.givenPostCounselling !=true}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Date of Counselling" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:datePicker name="counsellingDate" value="${postTerminationDto.counsellingDate}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_counsellingDate"></span>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field width="5" value="Place Where Counselling Was Done" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7 partial-search-container" style="width:100%">
                    <%--<iais:select name="counsellingPlace" firstOption="Please Select" codeCategory="TOP_PRE_COUNSELLING_PLACE" value="${postTerminationDto.counsellingPlace}" cssClass="counsellingPlace"/>--%>
                    <iais:select name="TopPlace" options="TopPlace" id="TopPlace"
                                 value="${postTerminationDto.counsellingPlace}" cssClass="TopPlace"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_TopPlace"></span>
                </iais:value>
            </iais:row>
        </div>

    </div>
    </div>
</c:if>
<script>
    $(document).ready(function () {
        givenPostCounselling();
        counsellingRslt();
        otherCounsellingRslt();
        ifCounsellingNotGiven();
    });

    function givenPostCounselling() {
        $('input[name=givenPostCounselling]').change(function () {
            if ($('#radioYes').prop('checked')) {
                $('#numGivenPostCounselling').show();
                $('#numGivenPostCounsellings').show();
                $('#numCounsellingGivenDoc').show();
            }
            if ($('#radioNo').prop('checked')) {
                $('#numGivenPostCounselling').hide();
                $('#numGivenPostCounsellings').hide();
                $('#numCounsellingGivenDoc').hide();
            }
        });
    }

    function otherCounsellingRslt() {
        $('#otherCounsellorIdTypeLabel').change(function () {
            var counsellingRslt = $('#otherCounsellorIdTypeLabel').val();
            if (counsellingRslt == "TOPCR007") {
                $('#otherCounsellingRslts').show();
            } else {
                $('#otherCounsellingRslts').hide();
            }
        });
    }

    function counsellingRslt() {
        $('input[name=givenPostCounselling]').change(function () {
            if ($('#radioYes').prop('checked')) {
                $('#counsellingRslts').show();
            } else {
                $('#counsellingRslts').hide();
                $('#otherCounsellingRslts').hide();
                fillValue($('#counsellingRslts'), null);
                $('#otherCounsellingRslts').val(null);
            }
        });
    }

    function ifCounsellingNotGiven() {
        $('input[name=givenPostCounselling]').change(function () {
            if ($('#radioNo').prop('checked')) {
                $('#ifCounsellingNotGivens').show();
            } else if ($('#radioYes').prop('checked')) {
                $('#ifCounsellingNotGivens').hide();
            }
        });
    }

    $(document).ready(function () {
        // Initialize select2
        $("#TopPlace").select2({
            matcher: matchCustom
        });
        $('.select2-container--default').attr('style', 'width:100%');
    });

    function matchCustom(params, data) {
        // If there are no search terms, return all of the data
        if ($.trim(params.term).length < 3) {
            return null;
        }

        // Do not display the item if there is no 'text' property
        if (typeof data.text === 'undefined') {
            return null;
        }

        // `params.term` should be the term that is used for searching
        // `data.text` is the text that is displayed for the data object
        if (data.text.toUpperCase().indexOf(params.term.toUpperCase()) > -1) {
            var modifiedData = $.extend({}, data, true);
            modifiedData.text += ' (matched)';

            // You can return modified objects from here
            // This includes matching the `children` how you want in nested data sets
            return modifiedData;
        }

        // Return `null` if the term should not be displayed
        return null;
    }
</script>