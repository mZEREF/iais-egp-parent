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
            <iais:field width="5" value="Type of Surgical Procedure "/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="spType" firstOption="Please Select" id="spType" codeCategory="TOP_TYPE_OF_SURGICAL_PROCEDURE"
                             value="${terminationDto.spType}" cssClass="spType"/>
            </iais:value>
        </iais:row>
    </div>
    <div id="otherSpType" <c:if test="${terminationDto.spType!='TOPTSP003'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Type of Surgical Procedure - others"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="66" type="text" name="otherSpType" value="${terminationDto.otherSpType}" />
            </iais:value>
        </iais:row>
    </div>
    <div id="anTypes" <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Type of Anaesthesia"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="anType" firstOption="Please Select" codeCategory="TOP_TYPE_OF_ANAESTHESIA"
                             value="${terminationDto.anType}" cssClass="anType" id="anType"/>
            </iais:value>
        </iais:row>
    </div>
    <div id="otherAnType" <c:if test="${terminationDto.anType!='TOPTA004'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Other Type of Anaesthesia"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="66" type="text" name="otherAnType"/>
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
    <div id="otherDrugTypes" <c:if test="${terminationDto.drugType!='TOPTOD005'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Type of Drug (Others)" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="66" type="text" name="otherDrugType" value="${terminationDto.otherDrugType}" />
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
<%--<div id="topPlacelaceLabel" <c:if test="${terminationDto.performedOwn == null}">style="display: none"</c:if>>--%>
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
    <%--</div>--%>
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
            <iais:field width="5" value="Is Termination of Pregnancy Drug taken in own premises?" mandatory="true"/>
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
                <iais:field width="5" value="Place of Drug taken for Termination of Pregnancy" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7" display="true" id="topDrugPlace">
                    ${'unknown'}
                </iais:value>
            </iais:row>
        </div>
        <div id="topDrugPlaces" <c:if test="${terminationDto.takenOwn == null || terminationDto.takenOwn == true}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Place of Drug taken for Termination of Pregnancy" mandatory="true"/>
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
                <iais:input maxLength="66" type="text" name="otherTopDrugPlace" value="${terminationDto.otherTopDrugPlace}"/>
            </iais:value>
        </iais:row>
    </div>
    <iais:row>
        <iais:field width="5" value="Doctor Professional Regn No." mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" name="doctorRegnNo" value="${terminationDto.doctorRegnNo}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Name of Doctor who performed the Termination of Pregnancy" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="doctorName" value="${terminationDto.doctorName}" />
        </iais:value>
    </iais:row>
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
        /*$('input[name=performedOwn]').change(function () {
            topPlacelaceLabel();
        });*/
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
    });
    function spTypes() {
        var topType= $('#topType').val();
        if(topType == "TOPTTP001" || topType == "TOPTTP003" ){
            $('#spTypes').show();
        }else {
            $('#spTypes').hide();
        }
    }
    function performedOwns() {
        var topType= $('#topType').val();
        if(topType == "TOPTTP001" || topType == "TOPTTP003" ){
            $('#performedOwns').show();
        }else {
            $('#performedOwns').hide();
        }
    }
    /*function topPlacelaceLabel() {
        if ($('input[name=performedOwn]').prop('checked')) {
            $('#topPlacelaceLabel').show();
        }else {
            $('#topPlacelaceLabel').hide();
        }
    }*/
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
        }
    }
    function anType() {
        var anType= $('#anType').val();
        if(anType == "TOPTA004"){
            $('#otherAnType').show();
        }else {
            $('#otherAnType').hide();
        }
    }
    function spType() {
        var spType= $('#spType').val();
        if(spType == "TOPTSP003"){
            $('#otherSpType').show();
        }else {
            $('#otherSpType').hide();
        }
    }
    function anTypes() {
        var topType= $('#topType').val();
        if(topType == "TOPTTP001" || topType == "TOPTTP003" ){
            $('#anTypes').show();
        }else {
            $('#anTypes').hide();
        }
    }
    function topType() {
        var topType= $('#topType').val();
        if(topType == "TOPTTP001" || topType == "TOPTTP002" ){
            $('#drugTypes').show();
        }else {
            $('#drugTypes').hide();
        }
    }
    function drugType() {
        var drugType= $('#drugType').val();
        if(drugType == "TOPTOD005"){
            $('#otherDrugTypes').show();
        }else {
            $('#otherDrugTypes').hide();
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
</script>
