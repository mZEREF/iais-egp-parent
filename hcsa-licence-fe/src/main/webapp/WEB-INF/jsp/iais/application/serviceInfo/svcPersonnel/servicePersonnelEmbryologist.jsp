<div class="personnel-content">
    <input type="hidden" class="not-refresh" name="${logo}emCount" value="size"/>
    <iais:row cssClass="personnel-header">
        <iais:value width="5" cssClass="col-xs-12 col-md-6">
            <strong>
                <c:out value="Embryologist "/>
                <label class="assign-psn-item">${index+1}</label>
            </strong>
        </iais:value>
        <iais:value width="7" cssClass="col-xs-12 col-md-4 text-right">
            <span class="error-msg" name="iaisErrorMSg" id="error_personError${index}"></span>
            <div class="removeEditDiv <c:if test="${index == 0}">hidden</c:if>">
                <h4 class="text-danger">
                    <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                </h4>
            </div>
        </iais:value>
    </iais:row>
    <%----%>
    <input type="hidden" name="isPartEdit" value="0"/>
    <%--        name--%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Name"/>
        <iais:value width="3" cssClass="col-md-3">
            <iais:select cssClass="salutation" name="${logo}salutation${index}" firstOption="Please Select"
                         codeCategory="CATE_ID_SALUTATION" value="${appSvcPersonnelDto.salutation}"/>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input maxLength="66" type="text" cssClass="name" name="${logo}name${index}"
                        value="${appSvcPersonnelDto.name}"/>
        </iais:value>
    </iais:row>
    <%--         qualification   --%>
    <iais:row>
        <iais:field width="5" value="Qualification"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input cssClass="qualification" maxLength="66" type="text" name="${logo}qualification${index}"
                        value="${appSvcPersonnelDto.qualification}"/>
        </iais:value>
    </iais:row>
    <%--           Relevant working experience(Years) --%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Working Experience(in term of years)"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input cssClass="wrkExpYear" maxLength="2" type="text" name="${logo}wrkExpYear${index}"
                        value="${appSvcPersonnelDto.wrkExpYear}"/>
        </iais:value>
    </iais:row>
    <%--  Number of AR procedures done under supervision  --%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Number of AR procedures done under supervision"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input cssClass="numberSupervision" maxLength="2" type="text" name="${logo}numberSupervision${index}"
                        value="${appSvcPersonnelDto.numberSupervision}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field value="Is the Embryologist authorized?" mandatory="true" width="5"/>
        <iais:value width="3" cssClass="col-md-3 form-check">
            <input
                    <c:if test="${'1'==appSvcPersonnelDto.embryologistAuthorized}">checked="checked"</c:if>
                    class="form-check-input locateWtihNonHcsa" type="radio" name="${logo}embryologistAuthorized${index}"
                    value="1"
                    aria-invalid="false">
            <label class="form-check-label"><span class="check-circle"></span>Yes</label>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4 form-check">
            <input
                    <c:if test="${'0'==appSvcPersonnelDto.embryologistAuthorized}">checked="checked"</c:if>
                    class="form-check-input locateWtihNonHcsa" type="radio" name="${logo}embryologistAuthorized${index}"
                    value="0"
                    aria-invalid="false">
            <label class="form-check-label"><span class="check-circle"></span>No</label>
        </iais:value>
        <iais:value cssClass="col-md-offset-4 col-md-8 col-xs-12">
            <span class="error-msg " name="iaisErrorMsg" id="error_${logo}embryologistAuthorized${status.index}"></span>
        </iais:value>
    </iais:row>
</div>



