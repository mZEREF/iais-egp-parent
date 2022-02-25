<iais:row>
  <iais:field value="NRIC" width="11"/>
  <iais:value width="11">
    <label><c:out value="${inter_user_attr.identityNo}"/></label>
  </iais:value>
</iais:row>
<iais:row>
  <c:if test="${myinfo_sinpass_login_evaluate == 'Y'}">
  <iais:field value="Name" width="11" mandatory="${isLoadMyInfoData == '1' ? false : true}"/>
  <iais:value width="11">
    <c:choose>
      <c:when test="${isLoadMyInfoData == '1'}">
        <label><c:out value="${inter_user_attr.displayName}"/></label>
      </c:when>
      <c:otherwise>
        <iais:input type="text" name="name" id="name" maxLength="110" value="${inter_user_attr.displayName == '-' ? '': inter_user_attr.displayName}"/>
        <span class="error-msg" name="errorMsg" id="error_displayName"></span>
      </c:otherwise>
    </c:choose>
  </iais:value>
  </c:if>
  <c:if test="${empty myinfo_sinpass_login_evaluate}">
    <iais:field value="Name" width="11"/>
    <iais:value width="11">
      <label><c:out value="${inter_user_attr.displayName}"/></label>
    </iais:value>
  </c:if>
</iais:row>
<%-- Address start --%>
  <iais:row cssClass="postalCodeDiv">
    <iais:field value="Postal Code" mandatory="${isLoadMyInfoData == '1' ? false : true}" width="11"/>
    <iais:value width="11">
      <c:choose>
        <c:when test="${isLoadMyInfoData == '1'}">
          <label><c:out  value="${licensee.postalCode}" /></label>
        </c:when>
        <c:otherwise>
          <iais:input cssClass="postalCode" maxLength="6" type="text" name="postalCode" value="${licensee.postalCode}" />
        </c:otherwise>
      </c:choose>
    </iais:value>
  </iais:row>
  <iais:row>
    <iais:field value="Address Type" mandatory="true" width="11"/>
    <iais:value width="11">
      <iais:select name="addrType" codeCategory="CATE_ID_ADDRESS_TYPE" firstOption="Please Select"
                   value="${licensee.addrType}" />
    </iais:value>
  </iais:row>
  <iais:row cssClass="address">
    <iais:field value="Block / House No." width="5"  cssClass="${isLoadMyInfoData == '1' ? '' : 'blkNoLabel'}"/>
    <iais:value width="11">
      <c:choose>
        <c:when test="${isLoadMyInfoData == '1'}">
          <label><c:out  value="${licensee.blkNo}" /></label>
        </c:when>
        <c:otherwise>
          <iais:input maxLength="10" type="text" name="blkNo" id="blkNo" value="${licensee.blkNo}"/>
        </c:otherwise>
      </c:choose>
    </iais:value>
  </iais:row>
  <iais:row>
    <iais:field value="Floor / Unit No." width="11" cssClass="${isLoadMyInfoData == '1' ? '' : 'floorUnitLabel'}"/>
    <c:choose>
      <c:when test="${isLoadMyInfoData == '1'}">
        <iais:value width="5">
          <label><c:out  value="${licensee.floorNo}" /> - <c:out  value="${licensee.unitNo}" /></label>
        </iais:value>
      </c:when>
      <c:otherwise>
        <iais:value width="3">
          <iais:input maxLength="3" type="text" name="floorNo" id="floorNo" value="${licensee.floorNo}"/>
        </iais:value>
        <iais:value width="2" cssClass="col-sm-2 col-md-1 text-center"><p>-</p></iais:value>
        <iais:value width="3">
          <iais:input maxLength="5" type="text" name="unitNo" id="unitNo" value="${licensee.unitNo}"/>
        </iais:value>
      </c:otherwise>
    </c:choose>
  </iais:row>
  <iais:row cssClass="address">
    <iais:field value="Street Name" mandatory="${isLoadMyInfoData == '1' ? false : true}" width="11"/>
    <iais:value width="11">
      <c:choose>
      <c:when test="${isLoadMyInfoData == '1'}">
        <label><c:out  value="${licensee.streetName}" /></label>
      </c:when>
        <c:otherwise>
          <iais:input maxLength="32" type="text" name="streetName" id="streetName" value="${licensee.streetName}"/>
        </c:otherwise>
      </c:choose>
    </iais:value>
  </iais:row>

  <iais:row cssClass="address">
    <iais:field value="Building Name" width="11"/>
    <iais:value width="11">
      <c:choose>
        <c:when test="${isLoadMyInfoData == '1'}">
          <label><c:out  value="${licensee.buildingName}" /></label>
        </c:when>
        <c:otherwise>
          <iais:input maxLength="66" type="text" name="buildingName" id="buildingName" value="${licensee.buildingName}"/>
        </c:otherwise>
      </c:choose>
    </iais:value>
  </iais:row>
  <%-- Address end --%>

  <iais:row>
    <iais:field value="Mobile No." mandatory="true" width="11"/>
    <iais:value width="11">
      <iais:input type="text" name="telephoneNo" maxLength="8" value="${licensee.mobileNo}"/>
    </iais:value>
  </iais:row>

  <iais:row>
    <iais:field value="Email" mandatory="true" width="11"/>
    <iais:value width="11">
      <iais:input type="text" name="emailAddr" maxLength="320" value="${licensee.emilAddr}"/>
    </iais:value>
  </iais:row>