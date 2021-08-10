<div class="licensee-detail">
  <iais:row cssClass="company-no ${dto.licenseeType == companyType ? '' : 'hidden'}">
    <iais:field width="5" value="UEN No."/>
    <iais:value width="7">
      <iais:code code="${dto.uenNo}" />
    </iais:value>
  </iais:row>

  <iais:row cssClass="ind-no ${dto.licenseeType == individualType ? '' : 'hidden'}">
    <iais:field width="5" mandatory="true" value="ID No."/>
    <iais:value width="3">
      <iais:select name="idType" firstOption="Please Select" codeCategory="CATE_ID_ID_TYPE" value="${dto.idType}" />
    </iais:value>
    <iais:value width="3">
      <iais:input maxLength="9" type="text" name="idNumber" value="${dto.idNumber}" />
    </iais:value>
  </iais:row>

  <iais:row>
    <iais:field value="Licensee Name" mandatory="true" width="5"/>
    <iais:value width="7">
      <iais:input maxLength="66" type="text" name="licenseeName" id="licenseeName" value="${dto.licenseeName}"/>
    </iais:value>
  </iais:row>

  <%-- Address start --%>
  <iais:row cssClass="postalCodeDiv">
    <iais:field value="Postal Code " mandatory="true" width="5"/>
    <iais:value width="7">
      <iais:input cssClass="postalCode" maxLength="6" type="text" name="postalCode" value="${dto.postalCode}" />
    </iais:value>
    <iais:value width="2">
      <p><a class="retrieveAddr <%--<c:if test="${!canEdit || readOnly}">hidden</c:if>--%>">Retrieve your address</a></p>
    </iais:value>
  </iais:row>
  <iais:row>
    <iais:field value="Address Type" mandatory="true" width="5"/>
    <iais:value width="7">
      <iais:select name="addrType" codeCategory="CATE_ID_ADDRESS_TYPE" firstOption="Please Select"
                   value="${dto.addrType}" cssClass="addrTypeSel"/>
    </iais:value>
  </iais:row>
  <iais:row cssClass="address">
    <iais:field value="Block / House No." width="5" cssClass="blkNoLabel"/>
    <iais:value width="7">
      <iais:input maxLength="10" type="text" name="blkNo" id="blkNo" value="${dto.blkNo}"/>
    </iais:value>
  </iais:row>
  <iais:row>
    <iais:field value="Floor / Unit No." width="5" cssClass="floorUnitLabel"/>
    <iais:value width="2">
      <iais:input maxLength="3" type="text" name="floorNo" id="floorNo" value="${dto.floorNo}"/>
    </iais:value>
    <iais:value width="1" cssClass="col-sm-2 col-md-1 text-center"><p>-</p></iais:value>
    <iais:value width="2">
      <iais:input maxLength="5" type="text" name="unitNo" id="unitNo" value="${dto.unitNo}"/>
    </iais:value>
  </iais:row>
  <iais:row cssClass="address">
    <iais:field value="Street Name" mandatory="true" width="5"/>
    <iais:value width="7">
      <iais:input maxLength="32" type="text" name="streetName" id="streetName" value="${dto.streetName}"/>
    </iais:value>
  </iais:row>

  <iais:row cssClass="address">
    <iais:field value="Building Name" width="5"/>
    <iais:value width="7">
      <iais:input maxLength="66" type="text" name="buildingName" id="buildingName" value="${dto.buildingName}"/>
    </iais:value>
  </iais:row>
  <%-- Address end --%>

  <iais:row>
    <iais:field value="Mobile No." mandatory="true" width="5"/>
    <iais:value width="7">
      <iais:input type="text" name="telephoneNo" maxLength="8" value="${dto.telephoneNo}"/>
    </iais:value>
  </iais:row>

  <iais:row>
    <iais:field value="Email Address" mandatory="true" width="5"/>
    <iais:value width="7">
      <iais:input type="text" name="emailAddr" maxLength="320" value="${dto.emailAddr}"/>
    </iais:value>
  </iais:row>
</div>