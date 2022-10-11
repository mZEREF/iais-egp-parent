<div class="premisesContent">
    <div class="${isEdit == 'Y' ? '' : 'hidden'}">
    <div class="text-right app-font-size-16"><a href="javascript:void(0);"
                                                class="viewPremisesEdit"><em
            class="fa fa-pencil-square-o"></em>Edit</a></div>
    </div>
    <iais:row>
        <strong>
            <c:out value="Secondary Address "/>
            <label class="assign-psn-item"></label>
        </strong>
    </iais:row>
    <input type="hidden" class="id" value="${appGrpSecondAddr.id}">
    <div class="row">
        <div class="col-md-6">
            Postal Code
        </div>
        <div class="col-md-6">
            <div class="col-md-6">
                <div class="newVal postalCode" attr="${appGrpSecondAddr.postalCode}">
                    <c:out value="${appGrpSecondAddr.postalCode}"/>
                </div>
            </div>
            <div class="col-md-6">
                <div class="oldVal " attr="${appGrpSecondAddr.postalCode}" style="display: none">
                    <c:out value="${oldAppGrpPremDto.postalCode}"/>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            Address Type
        </div>
        <div class="col-md-6">
            <div class="col-md-6">
                  <div class="newVal addrType" attr="${appGrpSecondAddr.addrType}">
                    <iais:code code="${appGrpSecondAddr.addrType}"/>
                  </div>
            </div>
            <div class="col-md-6">
                  <div class="oldVal" attr="${oldAppGrpPremDto.addrType}" style="display: none">
                    <iais:code code="${oldAppGrpPremDto.addrType}"/>
                  </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            Block / House No.
        </div>
        <div class="col-md-6">
            <div class="col-md-6">
                <div class="newVal blkNo" attr="${appGrpSecondAddr.blkNo}">
                    <c:out value="${appGrpSecondAddr.blkNo}"/>
                </div>
            </div>
            <div class="col-md-6">
                <div class="oldVal " attr="${oldAppGrpPremDto.blkNo}" style="display: none">
                    <c:out value="${oldAppGrpPremDto.blkNo}"/>
                </div>
            </div>
        </div>
    </div>


    <div class="row appendContent">
        <div class="col-md-6">
            Floor / Unit No.
        </div>
        <div class="col-md-6">
            <div class="col-md-6">
               <div class="newVal floorNo-unitNo" attr="${appGrpSecondAddr.floorNo}${appGrpSecondAddr.unitNo}">
                 <c:out value="${appGrpSecondAddr.floorNo}-${appGrpSecondAddr.unitNo}"/>
               </div>
            </div>
            <div class="col-md-6">
               <div class="oldVal " attr="${oldAppGrpPremDto.floorNo}${oldAppGrpPremDto.unitNo}" style="display: none">
                  <c:out value="${oldAppGrpPremDto.floorNo}-${oldAppGrpPremDto.unitNo}"/>
               </div>
            </div>
        </div>
    </div>

    <c:forEach var="othersUnitNo" items="${appGrpSecondAddr.appPremisesOperationalUnitDtos}" varStatus="status">
        <div class="row addmore">
            <input type="hidden" class="othersId" value="${othersUnitNo.id}">
            <div class="col-md-6">
            </div>
            <div class="col-md-6">
                <div class="col-md-6">
                   <div class="newVal addmorecontent" attr="${othersUnitNo.floorNo}${othersUnitNo.unitNo}">
                     <c:out value="${othersUnitNo.floorNo}-${othersUnitNo.unitNo}"/>
                   </div>
                </div>
                <div class="col-md-6">
                    <div class="oldVal " attr="<c:out value="${oldAppGrpPremDto.streetName}"/>" style="display: none">
                       <c:out value="${oldAppGrpPremDto.streetName}"/>
                    </div>
            </div>
        </div>
    </c:forEach>

    <div class="row">
        <div class="col-md-6">
            Street Name
        </div>
        <div class="col-md-6">
            <div class="col-md-6">
                   <div class="newVal streetName" attr="<c:out value="${appGrpSecondAddr.streetName}"/>">
                       <c:out value="${appGrpSecondAddr.streetName}"/>
                   </div>
            </div>
            <div class="col-md-6">
                    <div class="oldVal " attr="<c:out value="${oldAppGrpPremDto.streetName}"/>" style="display: none">
                        <c:out value="${oldAppGrpPremDto.streetName}"/>
                    </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            Building Name
        </div>
        <div class="col-md-6">
            <div class="col-md-6">
                  <div class="newVal buildingName" attr="<c:out value="${appGrpSecondAddr.buildingName}"/>">
                    <c:out value="${appGrpSecondAddr.buildingName}"/>
                  </div>
            </div>
            <div class="col-md-6">
                <div class="oldVal " attr="<c:out value="${oldAppGrpPremDto.buildingName}"/>" style="display: none">
                 <c:out value="${oldAppGrpPremDto.buildingName}"/>
                </div>
            </div>
        </div>
    </div>

</div>
