<div class="personnel-content">
    <input type="hidden" class="not-refresh not-clear" name="${logo}noCount" value="size"/>
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
                <c:out value="ServicePersonnel"/>
                <span class="assign-psn-item">${index+1}</span>
            </strong>
        </div>
        <div class="col-xs-12 col-md-6 text-right removeEditDiv <c:if test="${index == 0}">hidden</c:if>">
            <h4 class="text-danger">
                <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
            </h4>
        </div>
    </iais:row>
    <input type="hidden" name="isPartEdit" value="0"/>
    <iais:row>
        <iais:value width="10" cssClass="col-md-10 col-xs-12">
            <span class="error-msg" name="iaisErrorMSg" id="error_personError"></span>
        </iais:value>
    </iais:row>
    <%--        name--%>
    <iais:row>
        <iais:field width="5"  mandatory="true" value="Name" cssClass="col-md-5"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" name="${logo}name${index}"
                        value="${appSvcPersonnelDto.name}"></iais:input>
        </iais:value>
    </iais:row>
    <%--         qualification   --%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Qualification" cssClass="col-md-5"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" name="${logo}qualification${index}"
                        value="${appSvcPersonnelDto.qualification}"></iais:input>
        </iais:value>
    </iais:row>
    <%--           Relevant working experience(Years) --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Relevant working experience(Years)"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input cssClass="wrkExpYear" maxLength="2" type="text" name="${logo}wrkExpYear${index}"
                        value="${appSvcPersonnelDto.wrkExpYear}"/>
        </iais:value>
    </iais:row>
</div>


