<iais:row>
    <iais:field value="Name" width="11" required="true"/>
    <iais:value width="11">
        <iais:input type="text" name="name" id="name" value="${user.displayName}"/>
        <div class="col-xs-12">
            <span class="error-msg" name="errorMsg" id="error_displayName"></span>
        </div>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field value="Salutation" width="11" required="true"/>
    <iais:value width="11">
        <iais:select name="salutation" id="salutation" value="${user.salutation}"
                     codeCategory="CATE_ID_SALUTATION"  firstOption="Please Select" />
        <div class="col-xs-12">
            <span class="error-msg" name="errorMsg" id="error_salutation"></span>
        </div>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field value="ID Type" width="11" required="true"/>
    <c:choose>
        <c:when test="${'Y'.equals(canEditFlag)}">
                <iais:value width="11">
                    <iais:select name="idType" id="idType" value="${user.idType}"
                                 codeCategory="CATE_ID_ID_TYPE" firstOption="Please Select"/>
                </iais:value>
        </c:when>
        <c:otherwise>
                <iais:value width="11">
                    <input name="idType" id="idType" type="text" value="<iais:code code= "${user.idType}"/>" readonly/>
                </iais:value>
        </c:otherwise>
    </c:choose>
    <div class="col-xs-12">
        <span class="error-msg" name="errorMsg" id="error_idType"></span>
    </div>
</iais:row>
<iais:row>
    <iais:field value="ID No" width="11" required="true"/>
    <iais:value width="11">
        <c:choose>
            <c:when test="${'Y'.equals(canEditFlag)}">
                <input type="text" name="idNo" id="idNo" value="${user.identityNo}"/>
            </c:when>
            <c:otherwise>
                    <input type="text" name="idNo" id="idNo" value="${user.identityNo}" readonly/>
            </c:otherwise>
        </c:choose>
        <div class="col-xs-12">
            <span class="error-msg" name="errorMsg" id="error_identityNo"></span>
        </div>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field value="Designation" width="11" required="true"/>
    <iais:value width="11">
        <iais:select cssClass="designation" name="designation" codeCategory="CATE_ID_DESIGNATION" value="${user.designation}" firstOption="Please Select"/>
        <div class="col-xs-12">
            <span class="error-msg" name="errorMsg" id="error_designation"></span>
        </div>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field value="Mobile No" width="11" required="true"/>
    <iais:value width="11">
        <iais:input type="text" name="mobileNo" id="mobileNo" maxLength="8" value="${user.mobileNo}"/>
        <div class="col-xs-12">
            <span class="error-msg" name="errorMsg" id="error_mobileNo"></span>
        </div>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field value="Office/Telephone No" width="11" required="true"/>
    <iais:value width="11">
        <iais:input type="text" name="officeNo" id="officeNo" maxLength="8" value="${user.officeTelNo}"/>
        <div class="col-xs-12">
            <span class="error-msg" name="errorMsg" id="error_officeTelNo"></span>
        </div>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field value="Email" width="11" required="true"/>
    <iais:value width="11">
        <iais:input type="text" name="email" id="email" value="${user.email}"/>
        <div class="col-xs-12">
            <span class="error-msg" name="errorMsg" id="error_email"></span>
        </div>
    </iais:value>
</iais:row>