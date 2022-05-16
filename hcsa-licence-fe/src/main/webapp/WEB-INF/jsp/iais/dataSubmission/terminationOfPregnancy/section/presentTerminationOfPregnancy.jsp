<c:set var="preTerminationDto" value="${terminationOfPregnancyDto.preTerminationDto}" />
<c:if test="${preTerminationDto.secCounsellingResult !='TOPSP001' && preTerminationDto.secCounsellingResult !='TOPSP002'}">
<c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}" />
<c:set var="terminationDto" value="${terminationOfPregnancyDto.terminationDto}" />
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="form-horizontal patientPatails">
    <iais:row>
        <iais:field width="5" value="Type of Termination of Pregnancy" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="topType" firstOption="Please Select" id="topType" codeCategory="TOP_TYPE_TERMINATION_PREGNANCY"
                         value="${terminationDto.topType}" cssClass="topType"/>
        </iais:value>
    </iais:row>
    <div id="spTypes" <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Type of Surgical Procedure" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="spType" firstOption="Please Select" id="spType" codeCategory="TOP_TYPE_OF_SURGICAL_PROCEDURE"
                             value="${terminationDto.spType}" cssClass="spType"/>
                <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_spType"></span>
            </iais:value>
        </iais:row>
    </div>
    <div id="otherSpTypes" <c:if test="${terminationDto.spType!='TOPTSP003' || (terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003')}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Type of Surgical Procedure - others" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="66" type="text" name="otherSpType" id="otherSpType" value="${terminationDto.otherSpType}" />
                <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_otherSpType"></span>
            </iais:value>
        </iais:row>
    </div>
    <div id="anTypes" <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Type of Anaesthesia" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="anType" firstOption="Please Select" codeCategory="TOP_TYPE_OF_ANAESTHESIA"
                             value="${terminationDto.anType}" cssClass="anType" id="anType"/>
                <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_anType"></span>
            </iais:value>
        </iais:row>
    </div>
    <div id="otherAnTypes" <c:if test="${terminationDto.anType!='TOPTA004' || (terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003')}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Other Type of Anaesthesia" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="66" type="text" name="otherAnType" id ="otherAnType" value="${terminationDto.otherAnType}"/>
                <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_otherAnType"></span>
            </iais:value>
        </iais:row>
    </div>
    <div id="drugTypes" <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Type of Drug" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="drugType" firstOption="Please Select" id="drugType" codeCategory="TOP_TYPE_OF_DRUG"
                             value="${terminationDto.drugType}" cssClass="drugType"/>
            </iais:value>
        </iais:row>
    </div>
    <div id="otherDrugTypes" <c:if test="${terminationDto.drugType!='TOPTOD005' || (terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002')}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Type of Drug (Others)" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="66" type="text" name="otherDrugType" id="otherDrugType" value="${terminationDto.otherDrugType}" />
            </iais:value>
        </iais:row>
    </div>
    <iais:row>
        <iais:field width="5" value="Result of Termination of Pregnancy - Any Complications" mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="complicationForOperRslt"
                       value="1"
                       id="complicationForOperRsltYes"
                       <c:if test="${terminationDto.complicationForOperRslt}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="complicationForOperRsltYes"><span
                        class="check-circle"></span>Yes</label>
            </div>
            <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_complicationForOperRslt"></span>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="complicationForOperRslt"
                       value="0"
                       id="complicationForOperRsltNo"
                       <c:if test="${terminationDto.complicationForOperRslt == false}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="complicationForOperRsltNo"><span
                        class="check-circle"></span>No</label>
            </div>
        </iais:value>
    </iais:row>
    <div id="ariseOperationComplications" <c:if test="${terminationDto.complicationForOperRslt != true}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Complications Arising From Operation" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="66" type="text" name="ariseOperationComplication" value="${terminationDto.ariseOperationComplication}" />
            </iais:value>
        </iais:row>
    </div>
    <iais:row>
        <c:set var="toolMsg"><iais:message key="DS_MSG015" paramKeys="1" paramValues="counsellor"/></c:set>
        <iais:field width="5" value="Date of Termination of Pregnancy" mandatory="true" info="${toolMsg}"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="topDate" value="${terminationDto.topDate}"/>
        </iais:value>
    </iais:row>
    <div id="performedOwns" <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Is Termination of Pregnancy by Surgery performed in own premises?" mandatory="true"/>
            <iais:value width="3" cssClass="col-md-3">
                <div class="form-check">
                    <input class="form-check-input"
                           type="radio"
                           name="performedOwn"
                           value="1"
                           id="performedOwnYes"
                           <c:if test="${terminationDto.performedOwn}">checked</c:if>
                           aria-invalid="false">
                    <label class="form-check-label"
                           for="performedOwnYes"><span
                            class="check-circle"></span>Yes</label>
                </div>
                <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_performedOwn"></span>
            </iais:value>
            <iais:value width="4" cssClass="col-md-4">
                <div class="form-check">
                    <input class="form-check-input"
                           type="radio"
                           name="performedOwn"
                           value="0"
                           id="performedOwnNo"
                           <c:if test="${terminationDto.performedOwn == false}">checked</c:if>
                           aria-invalid="false">
                    <label class="form-check-label"
                           for="performedOwnNo"><span
                            class="check-circle"></span>No</label>
                </div>
            </iais:value>
        </iais:row>
    </div>
<div id="topPlacelaceLabel" <c:if test="${(terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003') || terminationDto.performedOwn==null}">style="display: none"</c:if>>
        <div id="topPlaceYes" <c:if test="${terminationDto.performedOwn == null || terminationDto.performedOwn == false}">style="display: none"</c:if>>
            <iais:row cssClass="topPlace">
                <iais:field width="5" value="Place of Termination of Pregnancy by Surgery" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7" display="true" id="topPlace">
                    ${'unknown'}
                </iais:value>
            </iais:row>
        </div>
        <div id="topPlaceNo" <c:if test="${terminationDto.performedOwn == null || terminationDto.performedOwn == true}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Place of Termination of Pregnancy by Surgery" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:select name="topPlace" options="TopPlace"  value="${terminationDto.topPlace}" cssClass="topPlace"/>
                </iais:value>
                <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_topPlace"></span>
            </iais:row>
        </div>
    </div>
    <div id="pregnancyOwns" <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Is Drug prescribed for Termination of Pregnancy in own premises?" mandatory="true"/>
            <iais:value width="3" cssClass="col-md-3">
                <div class="form-check">
                    <input class="form-check-input"
                           type="radio"
                           name="pregnancyOwn"
                           value="1"
                           id="pregnancyOwnYes"
                           <c:if test="${terminationDto.pregnancyOwn == true}">checked</c:if>
                           aria-invalid="false">
                    <label class="form-check-label"
                           for="pregnancyOwnYes"><span
                            class="check-circle"></span>Yes</label>
                </div>
                <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_pregnancyOwn"></span>
            </iais:value>
            <iais:value width="4" cssClass="col-md-4">
                <div class="form-check">
                    <input class="form-check-input"
                           type="radio"
                           name="pregnancyOwn"
                           value="0"
                           id="pregnancyOwnNo"
                           <c:if test="${terminationDto.pregnancyOwn == false}">checked</c:if>
                           aria-invalid="false">
                    <label class="form-check-label"
                           for="pregnancyOwnNo"><span
                            class="check-circle"></span>No</label>
                </div>
            </iais:value>
        </iais:row>
    </div>
    <div id="prescribeTopPlaceLabel" <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002'}">style="display: none"</c:if>>
        <div id="prescribeTopPlace" <c:if test="${terminationDto.pregnancyOwn == false}">style="display: none"</c:if>>
            <iais:row cssClass="topPlace">
                <iais:field width="5" value="Place of Drug Prescribed for Termination of Pregnancy" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7" display="true" id="prescribeTopPlace">
                    ${'unknown'}
                </iais:value>
            </iais:row>
        </div>
        <div id="prescribeTopPlaces" <c:if test="${terminationDto.pregnancyOwn == null || terminationDto.pregnancyOwn == true}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Place of Drug Prescribed for Termination of Pregnancy" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:select name="prescribeTopPlace" options="TopPlace"  value="${terminationDto.prescribeTopPlace}"
                                 cssClass="prescribeTopPlace"/>
                </iais:value>
                <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_prescribeTopPlace"></span>
            </iais:row>
        </div>
    </div>
    <div id="takenOwns" <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Is Termination of Pregnancy Drug used in own premises?" mandatory="true"/>
            <iais:value width="3" cssClass="col-md-3">
                <div class="form-check">
                    <input class="form-check-input"
                           type="radio"
                           name="takenOwn"
                           value="1"
                           id="takenOwnYes"
                           <c:if test="${terminationDto.takenOwn == true}">checked</c:if>
                           aria-invalid="false">
                    <label class="form-check-label"
                           for="takenOwnYes"><span
                            class="check-circle"></span>Yes</label>
                </div>
                <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_takenOwn"></span>
            </iais:value>
            <iais:value width="4" cssClass="col-md-4">
                <div class="form-check">
                    <input class="form-check-input"
                           type="radio"
                           name="takenOwn"
                           value="0"
                           id="takenOwnNo"
                           <c:if test="${terminationDto.takenOwn == false}">checked</c:if>
                           aria-invalid="false">
                    <label class="form-check-label"
                           for="takenOwnNo"><span
                            class="check-circle"></span>No</label>
                </div>
            </iais:value>
        </iais:row>
    </div>
    <div id="takenOwnLabel" <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002'}">style="display: none"</c:if>>
        <div id="topDrugPlace" <c:if test="${terminationDto.takenOwn == false}">style="display: none"</c:if>>
            <iais:row cssClass="topDrugPlace">
                <iais:field width="5" value="Place of Drug used  for Termination of Pregnancy" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7" display="true" id="topDrugPlace">
                    ${'unknown'}
                </iais:value>
            </iais:row>
        </div>
        <div id="topDrugPlaces" <c:if test="${terminationDto.takenOwn == null || terminationDto.takenOwn == true}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Place of Drug used  for Termination of Pregnancy" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:select name="topDrugPlace" options="TopDrugPlace"  id="otherTopDrugPlace" value="${terminationDto.topDrugPlace}"
                                 cssClass="topDrugPlace"/>
                </iais:value>
                <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_topDrugPlace"></span>
            </iais:row>
        </div>
    </div>
    <div id="otherTopDrugPlaces" <c:if test="${terminationDto.topDrugPlace!='AR_SC_001'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Other Places where Drug for Termination of Pregnancy is used" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="66" type="text" name="otherTopDrugPlace" id="otherTopDrugPlaceText" value="${terminationDto.otherTopDrugPlace}"/>
            </iais:value>
        </iais:row>
    </div>
    <iais:row>
        <iais:value width="7" cssClass="col-md-7">
            <strong>Doctor who Performed the Termination of Pregnancy</strong>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="3" value="Professional Registration Number" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7" style="width: 380px;">
            <iais:input maxLength="20" type="text" name="doctorRegnNo" value="${terminationDto.doctorRegnNo}" onchange="clearDockerSelection()"/>
            <span id="doctorRegnNoMsg" name="iaisErrorMsg" class="error-msg"></span>
        </iais:value>
        <iais:value width="3" cssClass="col-md-3" display="true">
            <a class="ValidateDoctor" onclick="validateDoctors()">
                Validate Doctor
            </a>
            <div>
                <span id="msg" name="iaisErrorMsg" class="error-msg"></span>
            </div>
        </iais:value>
    </iais:row>
   <%-- <iais:row>
        <iais:field width="5" value="Name of Doctor" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7" id="names">
            &lt;%&ndash;<iais:input maxLength="66" type="text" name="doctorName" value="${terminationDto.doctorName}" />&ndash;%&gt;
            ${terminationDto.doctorName}
        </iais:value>
    </iais:row>--%>
    <iais:row>
        <iais:field width="5" value="Name of Doctor" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7" display="true" id="names">
            ${terminationDto.doctorName}
        </iais:value>
    </iais:row>
</div>
</c:if>
<div class="modal fade" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body" >
                <div class="row">
                    <div class="col-md-12">
                        <span style="font-size: 2rem;" id="prsErrorMsg">
                            <iais:message key="DS_MSG011" escape="false" />
                        </span>
                    </div>
                </div>
            </div>
            <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6" data-dismiss="modal" onclick="cancels()">CLOSE</button>
            </div>
        </div>
    </div>
</div>
<div class="doctorNameSelectionHidden">
    <input type="hidden" name="names" id="doctorNameHidden" value="${terminationDto.doctorName}">
</div>
<script>
    $(document).ready(function() {
        $('#topType').change(function () {
            topType();
        });
        $('#topType').change(function () {
            spTypes();
        });
        $('#topType').change(function () {
            performedOwns();
        });
        $('input[name=performedOwn]').change(function () {
            topPlacelaceLabel();
        });
        $('#topType').change(function () {
            pregnancyOwns();
        });
        $('#topType').change(function () {
            prescribeTopPlaceLabel();
        });
        $('#topType').change(function () {
            takenOwns();
        });
        $('#topType').change(function () {
            takenOwnLabel();
        });
        $('#topType').change(function () {
            anTypes();
        });
        $('#anType').change(function () {
            anType();
        });
        $('#spType').change(function () {
            spType();
        });
        $('#drugType').change(function () {
            drugType();
        });
        $('input[name=complicationForOperRslt]').change(function () {
            complicationForOperRslt();
        });
        $('input[name=performedOwn]').change(function () {
            performedOwn();
        });
        $('input[name=pregnancyOwn]').change(function () {
            pregnancyOwn();
        });
        $('input[name=takenOwn]').change(function () {
            takenOwn();
        });
        $('#otherTopDrugPlace').change(function () {
            otherTopDrugPlace();
        });
        if ("1" == $('#showValidatePT').val()) {
            $('#PRS_SERVICE_DOWN').modal('show');
        }


    });
    function spTypes() {
        var topType= $('#topType').val();
        if(topType == "TOPTTP001" || topType == "TOPTTP003" ){
            $('#spTypes').show();
        }else {
            $('#spTypes').hide();
            $('#otherSpTypes').hide();
            fillValue($('#spTypes'),null);
            $('#otherSpType').val(null);
        }
    }
    function performedOwns() {
        var topType= $('#topType').val();
        if(topType == "TOPTTP001" || topType == "TOPTTP003" ){
            $('#performedOwns').show();
        }else {
            $('#performedOwns').hide();
            fillValue($('input[name=performedOwn]').removeAttr('checked'));
            $('#topPlacelaceLabel').hide();
        }
    }
    function topPlacelaceLabel() {
        /*if ($('input[name=performedOwn]').prop('checked')) {
            $('#topPlacelaceLabel').show();
        }*/
        if($('#performedOwnYes').prop('checked')) {
            $('#topPlacelaceLabel').show();
        }else if($('#performedOwnNo').prop('checked')) {
            $('#topPlacelaceLabel').show();
        }

    }
    function pregnancyOwns() {
        var topType= $('#topType').val();
        if(topType == "TOPTTP001" || topType == "TOPTTP002" ){
            $('#pregnancyOwns').show();
        }else {
            $('#pregnancyOwns').hide();
        }
    }
    function prescribeTopPlaceLabel() {
        var topType= $('#topType').val();
        if(topType == "TOPTTP001" || topType == "TOPTTP002" ){
            $('#prescribeTopPlaceLabel').show();
        }else {
            $('#prescribeTopPlaceLabel').hide();
        }
    }
    function takenOwns() {
        var topType= $('#topType').val();
        if(topType == "TOPTTP001" || topType == "TOPTTP002" ){
            $('#takenOwns').show();
        }else {
            $('#takenOwns').hide();
        }
    }
    function takenOwnLabel() {
        var topType= $('#topType').val();
        if(topType == "TOPTTP001" || topType == "TOPTTP002" ){
            $('#takenOwnLabel').show();
        }else {
            $('#takenOwnLabel').hide();
            $('#otherTopDrugPlaces').hide();
            $('#otherTopDrugPlaceText').val(null);
            fillValue($('#otherTopDrugPlace'),null);
        }
    }
    function anType() {
        var anType= $('#anType').val();
        if(anType == "TOPTA004"){
            $('#otherAnTypes').show();
        }else {
            $('#otherAnTypes').hide();
            $('#otherAnType').val(null);
        }
    }
    function spType() {
        var spType= $('#spType').val();
        if(spType == "TOPTSP003"){
            $('#otherSpTypes').show();
        }else {
            $('#otherSpTypes').hide();
            $('#otherSpType').val(null);
        }
    }
    function anTypes() {
        var topType= $('#topType').val();
        if(topType == "TOPTTP001" || topType == "TOPTTP003" ){
            $('#anTypes').show();
        }else {
            $('#anTypes').hide();
            $('#otherAnTypes').hide();
            fillValue($('#anTypes'),null);
            $('#otherAnType').val(null);
        }
    }
    function topType() {
        var topType= $('#topType').val();
        if(topType == "TOPTTP001" || topType == "TOPTTP002" ){
            $('#drugTypes').show();
        }else {
            $('#drugTypes').hide();
            $('#otherDrugTypes').hide();
            fillValue($('#drugTypes'),null);
            $('#otherDrugType').val(null);
        }
    }
    function drugType() {
        var drugType= $('#drugType').val();
        if(drugType == "TOPTOD005"){
            $('#otherDrugTypes').show();
        }else {
            $('#otherDrugTypes').hide();
            $('#otherDrugType').val(null);
        }
    }
    function complicationForOperRslt() {
        if($('#complicationForOperRsltYes').prop('checked')) {
            $('#ariseOperationComplications').show();
        }
        if($('#complicationForOperRsltNo').prop('checked')) {
            $('#ariseOperationComplications').hide();
        }
    }
    function performedOwn() {
        console.log("p");
        if($('#performedOwnYes').prop('checked')) {
            console.log("yes");
            $('#topPlaceYes').show();
        }else {
            $('#topPlaceYes').hide();
        }
        if($('#performedOwnNo').prop('checked')) {
            console.log("no");
            $('#topPlaceNo').show();
        }else {
            $('#topPlaceNo').hide();
        }
    }
    function pregnancyOwn() {
        if($('#pregnancyOwnYes').prop('checked')) {
            $('#prescribeTopPlace').show();
            $('#prescribeTopPlaces').hide();
        }
        if($('#pregnancyOwnNo').prop('checked')) {
            $('#prescribeTopPlaces').show();
            $('#prescribeTopPlace').hide();
        }
    }
    function takenOwn() {
        if($('#takenOwnYes').prop('checked')) {
            $('#topDrugPlace').show();
            $('#topDrugPlaces').hide();
            $('#otherTopDrugPlaces').hide();
            fillValue($('#otherTopDrugPlace'),null);
            $('#otherTopDrugPlaceText').val(null);

        }
        if($('#takenOwnNo').prop('checked')) {
            $('#topDrugPlaces').show();
            $('#topDrugPlace').hide();
            $('#otherTopDrugPlaces').hide();
        }
    }
    function otherTopDrugPlace() {
        var otherTopDrugPlace= $('#otherTopDrugPlace').val();
        if(otherTopDrugPlace == "AR_SC_001"){
            $('#otherTopDrugPlaces').show();
        }else {
            $('#otherTopDrugPlaces').hide();
        }
    }
    function validateDoctors() {
        console.log('loading info ...');
        showWaiting();
        var prgNo =  $('input[name="doctorRegnNo"]').val();
        if(prgNo == "" || prgNo == null || prgNo == undefined){
            clearPrsInfo();
            dismissWaiting();
            clearErrorMsg();
            $('#doctorRegnNoMsg').text('This is a mandatory field.');
            return;
        }
        var no = $('input[name="doctorRegnNo"]').val();
        var jsonData = {
            'prgNo': no
        };
        $.ajax({
            'url': '${pageContext.request.contextPath}/top/prg-input-info',
            'dataType': 'json',
            'data': jsonData,
            'type': 'GET',
            'success': function (data) {
                if (isEmpty(data)) {
                    console.log("The return data is null");
                } else if('-1' == data.statusCode || '-2' == data.statusCode) {
                    $('#prsErrorMsg').val($('#flagDocMessage').html());
                    $('#msg').text('This Doctor is not authorized to perform Termination of Pregnancy.');
                    clearPrsInfo();
                } else if (data.hasException) {
                    $('#prsErrorMsg').val($('#flagInvaMessage').html());
                    $('#msg').text('This Doctor is not authorized to perform Termination of Pregnancy.');
                    clearPrsInfo();
                } else if ('401' == data.statusCode) {
                    $('#prsErrorMsg').val($('#flagPrnMessage').html());
                    $('#msg').text('This Doctor is not authorized to perform Termination of Pregnancy.');
                    clearPrsInfo();
                } else {
                    console.log("1");
                    loadingSp(data);
                }
                dismissWaiting();
            },
            'error': function () {
                console.log('error');
                clearPrsInfo;
                dismissWaiting();
            },
        });
    }
    function cancels() {
        $('#PRS_SERVICE_DOWN').modal('hide');
    }
    var clearPrsInfo = function () {
        $('#names').find('p').text('');
    };
    function loadingSp(data) {
        console.log("2");
        const name = data.name;
        console.log(name);
        $('#names').find('p').text(name);
        $('#doctorNameHidden').val(name);
    }
    function clearDockerSelection(){
        console.log("clearDockerSelection!")
        clearErrorMsg();
        $('#names').find('p').text('');
        clearFields('.doctorNameSelectionHidden');
    }
</script>
