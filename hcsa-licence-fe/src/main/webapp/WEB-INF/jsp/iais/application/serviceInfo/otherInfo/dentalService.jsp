<iais:row cssClass="edit-content">
    <c:if test="${canEdit}">
        <div class="text-right app-font-size-16">
            <a class="edit psnEdit" href="javascript:void(0);">
                <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
            </a>
        </div>
    </c:if>
</iais:row>
<iais:row>
    <div class="col-xs-12 col-md-6">
        <p class="bold">DentalService Other Information</p>
    </div>
</iais:row>

<iais:row>
    <iais:field width="3" cssClass="col-md-6" mandatory="true" value="Type of medical records"/>
    <iais:value width="4" cssClass="col-md-3">
        <label><input class="form-check-input isMedicalTypeIt" <c:if test="${'1' == med.isMedicalTypeIt}">checked="checked"</c:if>  type="checkbox" name="isMedicalTypeIt" value = "1" aria-invalid="true">&nbsp;&nbsp;&nbsp;IT System</label>
    </iais:value>
    <iais:value width="4" cssClass="col-md-3">
        <label><input class="form-check-input isMedicalTypePaper" <c:if test="${'1' == med.isMedicalTypePaper}">checked="checked"</c:if>  type="checkbox" name="isMedicalTypePaper" value = "1" aria-invalid="true">&nbsp;&nbsp;&nbsp;Paper cards</label>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="6" cssClass="col-md-6" mandatory="true" value="List of options for IT system and paper cards / IT system only"/>
    <iais:value width="6" cssClass="col-md-6">
        <iais:select cssClass="systemOption" name="systemOption" codeCategory="CATE_ID_DS_OTHER_MED_OPTION" value="${med.systemOption}" firstOption="Please Select" onchange="toggleOnSelect(this, 'MED06', 'otherInfo')"/>
    </iais:value>
</iais:row>

<iais:row id="otherInfo" style="${med.systemOption eq 'MED06' ?'' : ' display : none'}">
    <iais:field width="3" cssClass="col-md-6" mandatory="true" value="Please specify"/>
    <iais:value width="4" cssClass="col-md-6">
        <iais:input maxLength="20" type="text" cssClass="otherSystemOption" name="otherSystemOption" value="${med.otherSystemOption}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Is clinic open to general public?"/>
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
    <iais:field width="3" cssClass="col-md-6" mandatory="true" value="GFA Value (in sqm)"/>
    <iais:value width="4" cssClass="col-md-6">
        <iais:input maxLength="20" type="text" cssClass="gfaValue" name="gfaValue" value="${med.gfaValue}"/>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="3" cssClass="col-md-6" mandatory="true" value="I declare that I have met URA's requirements for gross floor area"/>
    <iais:value width="4" cssClass="col-md-6">
        <input class="form-check-input dsDeclaration" <c:if test="${'1' == appSvcOtherInfoDto.dsDeclaration}">checked="checked"</c:if>  type="checkbox"  name="dsDeclaration" value="1" aria-invalid="false">
    </iais:value>
</iais:row>

