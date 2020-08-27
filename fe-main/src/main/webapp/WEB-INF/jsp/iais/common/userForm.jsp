<iais:row>
    <iais:field value="Name" width="11" required="true"/>
    <iais:value width="11">
        <iais:input type="text" name="name" id="name" maxLength="66" value="${inter_user_attr.displayName}"/>
            <span class="error-msg" name="errorMsg" id="error_displayName"></span>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field value="Salutation" width="11" required="true"/>
    <iais:value width="11">
        <iais:select cssClass="Salutation" name="salutation" id="salutation" value="${inter_user_attr.salutation}"
                     codeCategory="CATE_ID_SALUTATION"  firstOption="Please Select" />
            <span class="error-msg" name="errorMsg" id="error_salutation"></span>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field value="ID Type" width="11" required="true"/>
        <%--<c:choose>--%>
            <%--<c:when test="${'Y'.equals(canEditFlag)}">--%>
                    <iais:value width="11">
                        <iais:select name="idType" id="idType" value="${inter_user_attr.idType}"
                                     codeCategory="CATE_ID_ID_TYPE" firstOption="Please Select"/>
                        <span class="error-msg" name="errorMsg" id="error_idType"></span>
                    </iais:value>
            <%--</c:when>--%>
            <%--<c:otherwise>--%>
                    <%--<iais:value width="11">--%>
                        <%--<input name="idType" id="idType" type="text" value="<iais:code code= "${inter_user_attr.idType}"/>" readonly/>--%>
                        <%--<span class="error-msg" name="errorMsg" id="error_idType"></span>--%>
                    <%--</iais:value>--%>
            <%--</c:otherwise>--%>
        <%--</c:choose>--%>
</iais:row>
<iais:row>
    <iais:field value="ID No" width="11" required="true"/>
    <iais:value width="11">
        <%--<c:choose>--%>
            <%--<c:when test="${'Y'.equals(canEditFlag)}">--%>
                <input type="text" name="idNo" id="idNo" value="${inter_user_attr.identityNo}" maxlength="9"/>
                <span class="error-msg" name="errorMsg" id="error_idNo"></span>
            <%--</c:when>--%>
            <%--<c:otherwise>--%>
                    <%--<input type="text" name="idNo" id="idNo" value="${inter_user_attr.identityNo}" maxlength="9" readonly/>--%>
            <%--</c:otherwise>--%>
        <%--</c:choose>--%>
            <span class="error-msg" name="errorMsg" id="error_identityNo"></span>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field value="Designation" width="11" required="true"/>
    <iais:value width="11">
        <iais:select cssClass="designation" name="designation" codeCategory="CATE_ID_DESIGNATION" value="${inter_user_attr.designation}" firstOption="Please Select"/>
            <span class="error-msg" name="errorMsg" id="error_designation"></span>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field value="Mobile No" width="11" required="true"/>
    <iais:value width="11">
        <iais:input type="text" name="mobileNo" id="mobileNo" maxLength="8" value="${inter_user_attr.mobileNo}"/>
            <span class="error-msg" name="errorMsg" id="error_mobileNo"></span>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field value="Office/Telephone No" width="11" required="true"/>
    <iais:value width="11">
        <iais:input type="text" name="officeNo" id="officeNo" maxLength="8" value="${inter_user_attr.officeTelNo}"/>
            <span class="error-msg" name="errorMsg" id="error_officeTelNo"></span>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field value="Email" width="11" required="true"/>
    <iais:value width="11">
        <iais:input type="text" name="email" id="email" maxLength="66" value="${inter_user_attr.email}"/>
            <span class="error-msg" name="errorMsg" id="error_email"></span>
    </iais:value>
</iais:row>