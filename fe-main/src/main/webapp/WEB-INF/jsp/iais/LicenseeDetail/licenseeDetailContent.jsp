<%-- Address start --%>
  <iais:row cssClass="postalCodeDiv">
    <iais:field value="Postal Code " mandatory="true" width="11"/>
    <iais:value width="11">
      <iais:input cssClass="postalCode" maxLength="6" type="text" name="postalCode" value="${licensee.postalCode}" />
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
    <iais:field value="Block / House No." width="5" cssClass="blkNoLabel"/>
    <iais:value width="11">
      <iais:input maxLength="10" type="text" name="blkNo" id="blkNo" value="${licensee.blkNo}"/>
    </iais:value>
  </iais:row>
  <iais:row>
    <iais:field value="Floor / Unit No." width="11" cssClass="floorUnitLabel"/>
    <iais:value width="3">
      <iais:input maxLength="3" type="text" name="floorNo" id="floorNo" value="${licensee.floorNo}"/>
    </iais:value>
    <iais:value width="2" cssClass="col-sm-2 col-md-1 text-center"><p>-</p></iais:value>
    <iais:value width="3">
      <iais:input maxLength="5" type="text" name="unitNo" id="unitNo" value="${licensee.unitNo}"/>
    </iais:value>
  </iais:row>
  <iais:row cssClass="address">
    <iais:field value="Street Name" mandatory="true" width="11"/>
    <iais:value width="11">
      <iais:input maxLength="32" type="text" name="streetName" id="streetName" value="${licensee.streetName}"/>
    </iais:value>
  </iais:row>

  <iais:row cssClass="address">
    <iais:field value="Building Name" width="11"/>
    <iais:value width="11">
      <iais:input maxLength="66" type="text" name="buildingName" id="buildingName" value="${licensee.buildingName}"/>
    </iais:value>
  </iais:row>
  <%-- Address end --%>

  <iais:row>
    <iais:field value="Mobile No." mandatory="true" width="11"/>
    <iais:value width="11">
      <iais:input type="text" name="telephoneNo" maxLength="8" value="${licensee.officeTelNo}"/>
    </iais:value>
  </iais:row>

  <iais:row>
    <iais:field value="Email Address" mandatory="true" width="11"/>
    <iais:value width="11">
      <iais:input type="text" name="emailAddr" maxLength="320" value="${licensee.emilAddr}"/>
    </iais:value>
  </iais:row>