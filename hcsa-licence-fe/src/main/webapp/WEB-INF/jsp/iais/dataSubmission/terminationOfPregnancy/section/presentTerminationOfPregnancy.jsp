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
    <div id="spTypes" style="${terminationDto.topType ==null || !terminationDto.topType eq'TOPTTP003' ? 'display: none' : ''}" >
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
    <div id="anTypes" style="${terminationDto.topType ==null || !terminationDto.topType eq'TOPTTP001' || !terminationDto.topType eq 'TOPTTP002' ? 'display: none' : ''}" >
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
    <div id="drugTypes" style="${terminationDto.topType == null || !terminationDto.topType eq'TOPTTP002' ? 'display: none' : ''}" >
        <iais:row>
            <iais:field width="5" value="Type of Drug" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="drugType" firstOption="Please Select" id="drugType" codeCategory="TOP_TYPE_OF_DRUG"
                             value="${terminationDto.drugType}" cssClass="drugType"/>
            </iais:value>
        </iais:row>
    </div>
    <div id="otherDrugType" <c:if test="${terminationDto.drugType!='TOPTOD004'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Type of Drug (Others)" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="66" type="text" name="otherDrugType" value="${terminationDto.otherDrugType}" />
            </iais:value>
        </iais:row>
    </div>
    <iais:row>
        <iais:field width="5" value="Result of Termination of Pregnancy – Any Complications" mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="complicationForOperRslt"
                       value="1"
                       id="complicationForOperRsltYes"
                       <c:if test="${preTerminationDto.complicationForOperRslt== true}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="complicationForOperRsltYes"><span
                        class="check-circle"></span>Yes</label>
            </div>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="complicationForOperRslt"
                       value="0"
                       id="complicationForOperRsltYesNo"
                       <c:if test="${preTerminationDto.complicationForOperRslt == false}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="complicationForOperRsltYesNo"><span
                        class="check-circle"></span>No</label>
            </div>
        </iais:value>
    </iais:row>
    <div id="ariseOperationComplication" <c:if test="${preTerminationDto.complicationForOperRslt == false}">style="display: none"</c:if>>
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
    <iais:row>
        <iais:field width="5" value="Place of Termination of Pregnancy" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="topPlace" value="${terminationDto.topPlace}" />
        </iais:value>
    </iais:row>
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
    });
    function spTypes() {
        var topType= $('#topType').val();
        if(topType == "TOPTTP001" || topType == "TOPTTP002" ){
            $('#spTypes').show();
        }else {
            $('#spTypes').hide();
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
        if(topType == "TOPTTP001" || topType == "TOPTTP002" ){
            $('#anTypes').show();
        }else {
            $('#anTypes').hide();
        }
    }
    function topType() {
        var topType= $('#topType').val();
        if(topType == "TOPTTP001" || topType == "TOPTTP003" ){
            $('#drugTypes').show();
        }else {
            $('#drugTypes').hide();
        }
    }
    function drugType() {
        var drugType= $('#drugType').val();
        if(drugType == "TOPTOD004"){
            $('#otherDrugType').show();
        }else {
            $('#otherDrugType').hide();
        }
    }
    function complicationForOperRslt() {
        if($('#complicationForOperRsltYes').prop('checked')) {
            $('#ariseOperationComplication').show();
        }
        if($('#complicationForOperRsltNo').prop('checked')) {
            $('#ariseOperationComplication').hide();
        }
    }
</script>
