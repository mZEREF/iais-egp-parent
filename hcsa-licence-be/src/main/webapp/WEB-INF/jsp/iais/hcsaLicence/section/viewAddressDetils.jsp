<div class="premisesContent">

    <iais:row>
        <div class="text-right app-font-size-16">
            <a class="edit viewPremisesEdit" href="javascript:void(0);">
                <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
            </a>
        </div>
    </iais:row>

    <div class="row">
        <div class="col-md-6">
            <strong><c:out value="Secondary Address "/><span class="assign-psn-item"></span> </strong>
        </div>
        <div class="col-md-6 text-right removeEditDiv removeBtnss">
            <h4 class="text-danger">
                <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
            </h4>
        </div>
    </div>


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
            <div class="col-md-6 oldPostaCode">
                <div class="oldVal" attr="${oldAppGrpSecondAddr.postalCode}" style="display: none">
                    <c:out value="${oldAppGrpSecondAddr.postalCode}"/>
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
                  <div class="oldVal" attr="${oldAppGrpSecondAddr.addrType}" style="display: none">
                    <iais:code code="${oldAppGrpSecondAddr.addrType}"/>
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
                <div class="oldVal" attr="${oldAppGrpSecondAddr.blkNo}" style="display: none">
                    <c:out value="${oldAppGrpSecondAddr.blkNo}"/>
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
               <div class="oldVal" attr="${oldAppGrpSecondAddr.floorNo}${oldAppGrpSecondAddr.unitNo}" style="display: none">
                  <c:out value="${oldAppGrpSecondAddr.floorNo}-${oldAppGrpSecondAddr.unitNo}"/>
               </div>
            </div>
        </div>
    </div>

    <c:forEach var="othersUnitNo" items="${appGrpSecondAddr.appPremisesOperationalUnitDtos}" varStatus="status">
        <c:set var="oldOthersUnitNo" value="${oldAppGrpSecondAddr.appPremisesOperationalUnitDtos[status.index]}"/>
        <div class="row addmore">
            <input type="hidden" class="othersId" value="${othersUnitNo.id}">
            <div class="col-md-6"></div>

            <div class="col-md-6">
                <div class="col-md-6 target">
                   <div class="newVal addmorecontent" attr="${othersUnitNo.floorNo}${othersUnitNo.unitNo}">
                     <c:out value="${othersUnitNo.floorNo}-${othersUnitNo.unitNo}"/>
                   </div>
                </div>

                <div class="col-md-6">
                    <div class="oldVal " attr="<c:out value="${oldOthersUnitNo.floorNo}${oldOthersUnitNo.unitNo}"/>" style="display: none">
                        <c:out value="${oldOthersUnitNo.floorNo}-${oldOthersUnitNo.unitNo}"/>
                    </div>
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
                    <div class="oldVal" attr="<c:out value="${oldAppGrpSecondAddr.streetName}"/>" style="display: none">
                        <c:out value="${oldAppGrpSecondAddr.streetName}"/>
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
                <div class="oldVal" attr="<c:out value="${oldAppGrpSecondAddr.buildingName}"/>" style="display: none">
                    <c:out value="${oldAppGrpSecondAddr.buildingName}"/>
                </div>
            </div>
        </div>
    </div>

</div>
