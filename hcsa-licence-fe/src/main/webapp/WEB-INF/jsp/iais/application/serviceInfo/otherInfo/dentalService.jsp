<iais:row>
    <div class="col-xs-12 col-md-6">
        <p class="bold">DentalService Other Information</p>
    </div>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Type of medical records"/>
    <div class="form-check col-md-3">
        <input class="form-check-input" name="isMedicalTypeIt" value="1"
               type="checkbox" aria-invalid="false"
               <c:if test="${'1' == med.isMedicalTypeIt}">checked="checked"</c:if> />
        <label class="form-check-label">
            <span class="check-square"></span><c:out value="IT System"/>
        </label>
    </div>
    <div class="form-check col-md-3">
        <input class="form-check-input" name="isMedicalTypePaper" value="1"
               type="checkbox" aria-invalid="false"
               <c:if test="${'1' == med.isMedicalTypePaper}">checked="checked"</c:if> />
        <label class="form-check-label">
            <span class="check-square"></span><c:out value="Paper cards"/>
        </label>
    </div>
</iais:row>
<iais:row>
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="List of options for IT system and paper cards / IT system only"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:select cssClass="systemOption" name="systemOption" codeCategory="CATE_ID_OTHER_OPTION" value="${med.systemOption}" firstOption="Please Select" onchange="toggleOnSelect(this, 'MED06', 'otherInfo')"/>
    </iais:value>
</iais:row>

<iais:row id="otherInfo" style="${med.systemOption eq 'MED06' ?'' : ' display : none'}">
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Please specify"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="20" type="text" cssClass="otherSystemOption" name="otherSystemOption" value="${med.otherSystemOption}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Is clinic open to general public?"/>
    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input isOpenToPublic" <c:if test="${true == med.isOpenToPublic}">checked="checked"</c:if>  type="radio" name="isOpenToPublic" value = "1" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </iais:value>

    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input isOpenToPublic" <c:if test="${false == med.isOpenToPublic}">checked="checked"</c:if>  type="radio" name="isOpenToPublic" value = "0" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>No</label>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="GFA Value (in sqm)"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="7" type="number" cssClass="gfaValue" name="gfaValue" value="${med.gfaValue}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="I declare that I have met URA's requirements for gross floor area"/>
    <div class="form-check col-md-3">
        <input class="form-check-input" name="dsDeclaration" value="1"
               type="checkbox" aria-invalid="false"
               <c:if test="${'1' == dsDeclaration}">checked="checked"</c:if> />
        <label class="form-check-label">
            <span class="check-square"></span><c:out value=""/>
        </label>
    </div>
</iais:row>



