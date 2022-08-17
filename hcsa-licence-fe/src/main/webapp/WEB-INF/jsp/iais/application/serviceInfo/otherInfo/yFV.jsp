<iais:row cssClass="row control control-caption-horizontal yFVSDiv">
    <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Do you provide Yellow Fever Vaccination Service&nbsp;"/>
    <input type="hidden" class="provideYfVsVal" name="provideYfVsVal" value="${provideYfVs}"/>
    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input provideYfVs" <c:if test="${'1' == provideYfVs}">checked="checked"</c:if>  type="radio" name="provideYfVs" value = "1" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </iais:value>

    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input provideYfVs" <c:if test="${'0' == provideYfVs}">checked="checked"</c:if>  type="radio" name="provideYfVs" value = "0" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>No</label>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="6" cssClass="col-md-6" value="Business Name"/>
    <iais:value width="6" cssClass="col-md-6" display="true">
        <c:forEach var="docShowDto" items="${currSvcInfoDto.appSvcBusinessDtoList}" varStatus="stat">
            <c:if test="${stat.index != 0}">
                <c:if test="${stat.index != stat.index-1}">,</c:if>
            </c:if>
            <c:out value="${docShowDto.businessName}"/>
        </c:forEach>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Date of Commencement"/>
    <iais:value width="6" cssClass="col-md-6">
        <iais:datePicker cssClass="yfCommencementDate field-date" name="yfCommencementDate"
                         value="${yfCommencementDateStr}"/>
    </iais:value>
</iais:row>



