<div class="personnel-content">
    <input type="hidden" class="not-refresh not-clear" name="${logo}emCount" value="size"/>
    <input type="hidden" class="not-refresh indexNo" name="${logo}indexNo" value="${appSvcPersonnelDto.indexNo}"/>
    <input type="hidden" class="not-refresh isPartEdit" name="${logo}isPartEdit" value="0"/>
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
            <strong>
                <c:out value="Embryologist"/>
                <span class="assign-psn-item">${index+1}</span>
            </strong>
        </div>
        <div class="col-xs-12 col-md-6 text-right removeEditDiv <c:if test="${index == 0}">hidden</c:if>">
            <h4 class="text-danger">
                <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
            </h4>
        </div>
    </iais:row>
    <%----%>
    <input type="hidden" name="isPartEdit" value="0"/>
    <%--        name--%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Name"/>
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
        <iais:field width="5" cssClass="col-md-5" value="Qualification"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input cssClass="qualification" maxLength="66" type="text" name="${logo}qualification${index}"
                        value="${appSvcPersonnelDto.qualification}"/>
        </iais:value>
    </iais:row>
    <%--           Relevant working experience(Years) --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Relevant working experience (Years)"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input cssClass="wrkExpYear" maxLength="2" type="text" name="${logo}wrkExpYear${index}"
                        value="${appSvcPersonnelDto.wrkExpYear}"/>
        </iais:value>
    </iais:row>
    <%--  Number of AR procedures done under supervision  --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Number of AR procedures done under supervision"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input cssClass="numberSupervision" maxLength="2" type="text" name="${logo}numberSupervision${index}"
                        value="${appSvcPersonnelDto.numberSupervision}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field value="Is the Embryologist authorized?" cssClass="col-md-5" mandatory="true"/>
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



