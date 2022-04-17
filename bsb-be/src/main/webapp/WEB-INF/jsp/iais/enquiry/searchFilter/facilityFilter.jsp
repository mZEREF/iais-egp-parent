<div class="col-xs-12 col-sm-12">
    <label for="facClassification" class="col-sm-5 col-md-5 control-label">Facility Classification</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="facClassification" class="error-msg"></span>
        <select name="facClassification" class="facClassification-select" id="facClassification">
            <c:forEach var="item" items="${facClassificationOps}">
                <option value="${item.value}" <c:if test="${facSearchDto.facClassification eq item.value}">selected="selected"</c:if>>${item.text}</option>
            </c:forEach>
        </select>
    </div>

    <label for="facName" class="col-sm-5 col-md-5 control-label">Facility Name</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="facName" class="error-msg"></span>
        <input type="text" id="facName" name="facName" value="${facSearchDto.facName}"/>
    </div>

    <label class="col-sm-5 col-md-5 control-label">Gazetted Area</label>
    <div class="col-sm-7 col-md-5">
        <div class="col-sm-4" style="margin-top: 8px">
            <input type="radio" name="gazettedArea" id="gazettedAreaY" value="Y" <c:if test="${facSearchDto.gazettedArea eq 'Y'}">checked</c:if> />
            <label for="gazettedAreaY">Yes</label>
        </div>
        <div class="col-sm-4" style="margin-top: 8px">
            <input type="radio" name="gazettedArea" id="gazettedAreaN" value="N" <c:if test="${facSearchDto.gazettedArea eq 'N'}">checked</c:if> />
            <label for="gazettedAreaN">No</label>
        </div>
    </div>

    <label for="facOperator" class="col-sm-5 col-md-5 control-label">Facility Operator</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="facOperator" class="error-msg"></span>
        <input type="text" id="facOperator" name="facOperator" value="${facSearchDto.facOperator}"/>
    </div>

    <label for="facAdmin" class="col-sm-5 col-md-5 control-label">Facility Administrator</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="facAdmin" class="error-msg"></span>
        <input type="text" id="facAdmin" name="facAdmin" value="${facSearchDto.facAdmin}"/>
    </div>

    <label for="facAuthorisedPerson" class="col-sm-5 col-md-5 control-label">Personnel Authorised to Access the Facility</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="facAuthorisedPerson" class="error-msg"></span>
        <input type="text" id="facAuthorisedPerson" name="facAuthorisedPerson" value="${facSearchDto.facAuthorisedPerson}"/>
    </div>

    <label for="facCommittee" class="col-sm-5 col-md-5 control-label">Biosafety Committee Personnel</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="facCommittee" class="error-msg"></span>
        <input type="text" id="facCommittee" name="facCommittee" value="${facSearchDto.facCommittee}"/>
    </div>

    <label for="facStatus" class="col-sm-5 col-md-5 control-label">Facility Status</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="facStatus" class="error-msg"></span>
        <select name="facStatus" class="facStatusSelect" id="facStatus">
            <c:forEach var="item" items="${facStatusOps}">
                <option value="${item.value}" <c:if test="${facSearchDto.facStatus eq item.value}">selected="selected"</c:if>>${item.text}</option>
            </c:forEach>
        </select>
    </div>

    <label for="afcName" class="col-sm-5 col-md-5 control-label">Approved Facility Certifier</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="afcName" class="error-msg"></span>
        <select name="afcName" class="afcName-select" id="afcName">
            <c:forEach var="item" items="${afcSelectionOps}">
                <option value="${item.value}" <c:if test="${facSearchDto.afcName eq item.value}">selected="selected"</c:if>>${item.text}</option>
            </c:forEach>
        </select>
    </div>
</div>
