<div class="personnel-content">
    <input type="hidden" class="not-refresh " name="${logo}noCount" value="size"/>
    <iais:row cssClass="personnel-header">
        <iais:value width="5" cssClass="col-xs-12 col-md-6">
            <strong>
                <c:out value="ServicePersonnel "/>
                <label class="assign-psn-item">${index+1}</label>
            </strong>
        </iais:value>
        <iais:value width="7" cssClass="col-xs-12 col-md-4 text-right">
            <div class="removeEditDiv <c:if test="${index == 0}">hidden</c:if>">
                <h4 class="text-danger">
                    <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                </h4>
            </div>
        </iais:value>
    </iais:row>
    <iais:row>
        <c:if test="${'true' == canEdit}">
            <div class="text-right app-font-size-16">
                <a id="edit" class="svcPsnEdit" href="javascript:void(0);">
                    <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                </a>
            </div>
        </c:if>
    </iais:row>
    <input type="hidden" name="isPartEdit" value="0"/>
    <iais:row>
        <iais:value width="10" cssClass="col-md-10 col-xs-12">
            <span class="error-msg" name="iaisErrorMSg" id="error_personError"></span>
        </iais:value>
    </iais:row>
    <%--        name--%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Name" cssClass="col-sm-5"/>
        <iais:value width="7" cssClass="col-sm-5 col-md-7">
            <iais:input maxLength="66" type="text" name="${logo}name${index}"
                        value="${appSvcPersonnelDto.name}"></iais:input>
        </iais:value>
    </iais:row>
    <%--         qualification   --%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Qualification" cssClass="col-sm-5"/>
        <iais:value width="7" cssClass="col-sm-5 col-md-7">
            <iais:input maxLength="66" type="text" name="${logo}qualification${index}"
                        value="${appSvcPersonnelDto.qualification}"></iais:input>
        </iais:value>
    </iais:row>
    <%--           Relevant working experience(Years) --%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Relevant working experience(Years)"/>
        <iais:value width="7" cssClass="col-sm-5 col-md-7">
            <iais:input cssClass="wrkExpYear" maxLength="2" type="text" name="${logo}wrkExpYear${index}"
                        value="${appSvcPersonnelDto.wrkExpYear}"/>
        </iais:value>
    </iais:row>
</div>


