<iais:row>
  <iais:field value="NRIC" width="11"/>
  <iais:value width="11">
    <label><c:out value="${licensee.licenseeIndividualDto.idNo}"/></label>
  </iais:value>
</iais:row>
<iais:row>
    <iais:field value="Name" width="11"/>
    <iais:value width="11">
      <label><c:out value="${solo_login_name}"/></label>
    </iais:value>
</iais:row>
<%-- Address start --%>
  <iais:row cssClass="postalCodeDiv">
    <iais:field value="Postal Code" mandatory="false" width="11"/>
    <iais:value width="11">
      <label><c:out  value="${licensee.postalCode}" /></label>
    </iais:value>
  </iais:row>
  <iais:row>
    <iais:field value="Address Type" mandatory="false" width="11"/>
    <iais:value width="11">
      <label> <iais:code code="${licensee.addrType}"/></label>
    </iais:value>
  </iais:row>
  <iais:row cssClass="address">
    <iais:field value="Block / House No." width="5" cssClass="blkNoLabel"/>
    <iais:value width="11">
      <label><c:out  value="${licensee.blkNo}" /></label>
    </iais:value>
  </iais:row>
  <iais:row>
    <iais:field value="Floor / Unit No." width="11" cssClass="floorUnitLabel"/>
    <iais:value width="5">
      <label><c:out  value="${licensee.floorNo}" /> - <c:out  value="${licensee.unitNo}" /></label>
    </iais:value>
  </iais:row>
  <iais:row cssClass="address">
    <iais:field value="Street Name" mandatory="false" width="11"/>
    <iais:value width="11">
      <label><c:out  value="${licensee.streetName}" /></label>
    </iais:value>
  </iais:row>

  <iais:row cssClass="address">
    <iais:field value="Building Name" width="11"/>
    <iais:value width="11">
      <label><c:out  value="${licensee.buildingName}" /></label>
    </iais:value>
  </iais:row>
  <%-- Address end --%>

  <iais:row>
    <iais:field value="Mobile No." mandatory="false" width="11"/>
    <iais:value width="11">
      <label><c:out  value="${licensee.mobileNo}" /></label>
    </iais:value>
  </iais:row>

  <iais:row>
    <iais:field value="Email" mandatory="false" width="11"/>
    <iais:value width="11">
      <label><c:out  value="${licensee.emilAddr}" /></label>
    </iais:value>
  </iais:row>