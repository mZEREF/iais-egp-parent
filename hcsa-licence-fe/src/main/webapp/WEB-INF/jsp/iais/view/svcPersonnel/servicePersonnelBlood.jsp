<div class="personnel-content">

    <iais:row cssClass="personnel-header">
        <iais:value width="5" cssClass="col-xs-12 col-md-6">
            <strong>
                <c:out value="ServicePersonnel "/>
                <label class="assign-psn-item">${index+1}</label>
            </strong>
        </iais:value>
    </iais:row>

    <%--        name--%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Name" cssClass="col-sm-5"/>
        <iais:value width="7" cssClass="col-sm-5 col-md-7">
            <c:out value="${appSvcPersonnelDto.name}"/>
        </iais:value>
    </iais:row>

    <%--         qualification   --%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Qualification" cssClass="col-sm-5"/>
        <iais:value width="7" cssClass="col-sm-5 col-md-7">
            <c:out value="${appSvcPersonnelDto.qualification}"/>
        </iais:value>
    </iais:row>

    <%--           Relevant working experience(Years) --%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Relevant working experience(Years)"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.wrkExpYear}"/>
        </iais:value>
    </iais:row>

</div>


