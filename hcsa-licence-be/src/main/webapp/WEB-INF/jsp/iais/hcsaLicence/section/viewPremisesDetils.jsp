<div class="premisesContent">
<%--    <p>--%>
<%--    <div class="text-right app-font-size-16"><a href="javascript:void(0);"--%>
<%--                                                class="viewPremisesEdit"><em--%>
<%--            class="fa fa-pencil-square-o"></em>Edit</a></div>--%>
<%--    </p>--%>
    <iais:row>
        <strong>
            <c:out value="Secondary Address "/>
            <label class="assign-psn-item"></label>
        </strong>
        <%--        <div class="app-title">Secondary Address${statuss.index+1}:</div>--%>
    </iais:row>

    <div class="row">
        <div class="col-md-6">
            Postal Code
        </div>
        <div class="col-md-6">
            <div class="col-md-6">
            <span class="newVal postalCode" attr="${appGrpSecondAddr.postalCode}"><c:out
                    value="${appGrpSecondAddr.postalCode}"/></span>
            </div>
            <div class="col-md-6">
                <span class="oldVal " attr="${appGrpSecondAddr.postalCode}" style="display: none"><c:out
                        value="${appGrpSecondAddr.postalCode}"/></span>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            Address Type
        </div>
        <div class="col-md-6">
            <div class="col-md-6">
                  <span class="newVal addrType" attr="${appGrpSecondAddr.addrType}">
                    <iais:code code="${appGrpSecondAddr.addrType}"/>
                  </span>
            </div>
            <div class="col-md-6">
                              <span class="oldVal " attr="${oldAppGrpPremDto.addrType}" style="display: none">
                                <iais:code code="${oldAppGrpPremDto.addrType}"/>
                              </span>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            Block / House No.
        </div>
        <div class="col-md-6">
            <div class="col-md-6">
                <span class="newVal blkNo" attr="${appGrpSecondAddr.blkNo}"><c:out
                        value="${appGrpSecondAddr.blkNo}"/></span>
            </div>
            <div class="col-md-6">
                <span class="oldVal " attr="${oldAppGrpPremDto.blkNo}" style="display: none"><c:out
                        value="${oldAppGrpPremDto.blkNo}"/></span>
            </div>
        </div>
    </div>


    <div class="row appendContent">
        <div class="col-md-6">
            Floor / Unit No.
        </div>
        <div class="col-md-6">
            <div class="col-md-6">
                   <span class="newVal floorNo-unitNo" attr="${appGrpSecondAddr.floorNo}${appGrpSecondAddr.unitNo}">
                     <c:out value="${appGrpSecondAddr.floorNo}-${appGrpSecondAddr.unitNo}"/>
                   </span>
            </div>
            <div class="col-md-6">
                   <span class="oldVal " attr="${oldAppGrpPremDto.floorNo}${oldAppGrpPremDto.unitNo}" style="display: none">
                      <c:out value="${oldAppGrpPremDto.floorNo}-${oldAppGrpPremDto.unitNo}"/>
                   </span>
            </div>
        </div>
    </div>

    <c:forEach var="othersUnitNo" items="${appGrpSecondAddr.appPremisesOperationalUnitDtos}" varStatus="status">
        <div class="row">
            <div class="col-md-6">
            </div>
            <div class="col-md-6">
                <div class="col-md-6">
                   <span class="newVal" attr="${othersUnitNo.floorNo}${othersUnitNo.unitNo}">
                     <c:out value="${othersUnitNo.floorNo}-${othersUnitNo.unitNo}"/>
                   </span>
                </div>
                <br>
                <span class="oldVal " attr="<c:out value="${oldAppGrpPremDto.streetName}"/>" style="display: none">
                                           <c:out value="${oldAppGrpPremDto.streetName}"/>
                                       </span>
            </div>
        </div>
    </c:forEach>

    <div class="row">
        <div class="col-md-6">
            Street Name
        </div>
        <div class="col-md-6">
            <div class="col-md-12">
                   <span class="newVal streetName" attr="<c:out value="${appGrpSecondAddr.streetName}"/>">
                       <c:out value="${appGrpSecondAddr.streetName}"/>
                   </span>
            </div>
            <br>
            <span class="oldVal " attr="<c:out value="${oldAppGrpPremDto.streetName}"/>" style="display: none">
                                   <c:out value="${oldAppGrpPremDto.streetName}"/>
                               </span>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            Building Name
        </div>
        <div class="col-md-6">
            <div class="col-md-12">
                  <span class="newVal buildingName" attr="<c:out value="${appGrpSecondAddr.buildingName}"/>">
                    <c:out value="${appGrpSecondAddr.buildingName}"/>
                  </span>
            </div>
            <br>
            <span class="oldVal " attr="<c:out value="${oldAppGrpPremDto.buildingName}"/>" style="display: none">
                                <c:out value="${oldAppGrpPremDto.buildingName}"/>
                              </span>
        </div>
    </div>
</div>
