<iais:row>
    <div class="col-xs-12 col-md-6">
        <p class="bold">Yellow Fever Vaccination </p>
    </div>
</iais:row>
<iais:row>
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Do you provide Yellow Fever Vaccination Service"/>
    <input type="hidden" class="provideYfVsVal" name="provideYfVsVal" value="${appSvcOtherInfoDto.provideYfVs}"/>
    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input provideYfVs" <c:if test="${'1' == appSvcOtherInfoDto.provideYfVs}">checked="checked"</c:if>  type="radio" name="provideYfVs" value = "1" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </iais:value>

    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input provideYfVs" <c:if test="${'0' == appSvcOtherInfoDto.provideYfVs}">checked="checked"</c:if>  type="radio" name="provideYfVs" value = "0" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>No</label>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" mandatory="" value=""/>
    <iais:value width="7" cssClass="col-md-7">
        <span class="error-msg col-md-7" name="iaisErrorMsg" id="error_provideYfVs"></span>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" value="Business Name"/>
    <iais:value width="7" cssClass="col-md-7" display="true">
        <c:forEach var="docShowDto" items="${currSvcInfoDto.appSvcBusinessDtoList}" varStatus="stat">
            <c:if test="${stat.index != 0}">
                <c:if test="${stat.index != stat.index-1}">,</c:if>
            </c:if>
            <c:out value="${docShowDto.businessName}"/>
        </c:forEach>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" value="Address"/>
    <iais:value width="7" cssClass="col-md-7" display="true">
        <c:out value="${orgUse.email}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" value="Applicant Name"/>
    <iais:value width="7" cssClass="col-md-7" display="true">
       <c:out value="${orgUse.displayName}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" value="Designation"/>
    <iais:value width="7" cssClass="col-md-7" display="true">
        <c:out value="${orgUse.designation}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" value="Contact number"/>
    <iais:value width="7" cssClass="col-md-7" display="true">
        <c:out value="${orgUse.mobileNo}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Date of Commencement"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:datePicker cssClass="yfCommencementDate field-date" name="yfCommencementDate"
                         value="${appSvcOtherInfoDto.yfCommencementDateStr}"/>
    </iais:value>
</iais:row>


