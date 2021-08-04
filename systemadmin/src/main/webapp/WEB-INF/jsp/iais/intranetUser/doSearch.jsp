<iais:section title="" id="demoList">
    <iais:row>
        <iais:field value="User ID:"/>
        <iais:value width="18">
            <input id="userId" name="searchUserId" type="text" value="${searchUserId}" maxlength="20">
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="Email address:"/>
        <iais:value width="18">
            <input id="emailAddress" maxlength="320" name="searchEmail" type="text" value="${searchEmail}">
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="Display Name:"/>
        <iais:value width="18">
            <input id="displayName" type="text" name="searchDisplayName" value="${searchDisplayName}">
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="Account Status:"/>
        <iais:value width="18">
            <div class="form-horizontal">
                <iais:select id="accountStatus" name="searchStatus" options="statusOption" firstOption="Please Select"
                             value="${searchStatus}"></iais:select></div>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="Roles Assigned:"/>
        <iais:value width="18">
            <div class="form-horizontal">
                <iais:select id="role" name="role" options="roleOption" firstOption="Please Select"
                             value="${role}" needSort="true"></iais:select></div>
        </iais:value>
    </iais:row>
<%--    <iais:row>--%>
<%--        <iais:field value="Privileges Assigned:"/>--%>
<%--        <iais:value width="18">--%>
<%--            <div class="form-horizontal">--%>
<%--                <iais:select id="privilege" name="privilege" options="privilegeOption" firstOption="Please Select"--%>
<%--                             value="${privilege}"></iais:select></div>--%>
<%--        </iais:value>--%>
<%--    </iais:row>--%>

    <iais:action style="text-align:center;">
        <div class="text-right">
            <a class="btn btn-secondary" id="IU_Clear"  href="#">Clear</a>
            <a class="btn btn-primary" id="IU_Search" value="doQuery" href="#">Search</a>
        </div>
    </iais:action>
</iais:section>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
