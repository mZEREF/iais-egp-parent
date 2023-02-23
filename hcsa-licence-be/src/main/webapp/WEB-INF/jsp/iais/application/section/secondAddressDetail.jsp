<div class="premContents form-horizontal">
    <input type="hidden" class="not-refresh not-clear" name="${prefix}Count" value="1"/>
    <input type="hidden" class="not-refresh isPartEdit" name="${prefix}isPartEdit" value="0"/>
    <input type="hidden" class="not-refresh not-clear indexNo" name="${prefix}index" value="${appGrpSecondAddr.indexNo}"/>
    <input class="not-refresh addressSize" type="hidden" name="addressSize" value="1"/>
    <input class="not-refresh retrieveflags" type="hidden" name="${prefix}retrieveflags" value="${appGrpSecondAddr.clickRetrieve ? 1 : 0}"/>
        <div class="form-group">
            <div class="col-xs-12 col-md-6">
                <p class="app-title">Secondary Address <span class="premHeader">${status.index+1}</span></p>
            </div>
            <div class="col-xs-12 col-md-4 text-right removeEditDiv">
                <c:if test="${(isRfi || isRfc || isRenew)}">
                    <c:if test="${AppSubmissionDto.appEditSelectDto.premisesEdit}">
                        <c:set var="canEdit" value="false"/>
                        <a class="premises-summary-preview addressEdit app-font-size-16">
                            <em class="fa fa-pencil-square-o"></em><span style="display: inline-block;">&nbsp;</span>Edit</a>
                    </c:if>
                </c:if>
            </div>
            <div class="col-xs-12 col-md-3 text-right removeDIV">
                <h4 class="text-danger"><em class="fa fa-times-circle del-size-36 removeBtns"></em></h4>
            </div>

        </div>

    <iais:row cssClass="postalCodeDiv">
        <iais:field value="Postal Code" mandatory="true" width="5"/>
        <iais:value cssClass="col-xs-10 col-md-5">
            <iais:input cssClass="postalCode" maxLength="6" type="text" name="${prefix}postalCode${status.index}" value="${appGrpSecondAddr.postalCode}"/>
        </iais:value>
        <div class="col-xs-7 col-sm-6 col-md-3">
            <p><a class="retrieveAddr <c:if test="${!canEdit}">hidden</c:if>">Retrieve your address</a></p>
        </div>
    </iais:row>
    <iais:row>
        <iais:field value="Address Type" mandatory="true" width="5"/>
        <iais:value width="7" cssClass="addressType">
            <iais:select cssClass="addrType" name="${prefix}addrType${status.index}" codeCategory="CATE_ID_ADDRESS_TYPE" needSort="false"
                         firstOption="Please Select" value="${appGrpSecondAddr.addrType}" />
        </iais:value>
    </iais:row>
    <iais:row cssClass="address">
        <iais:field value="Block / House No." width="5" cssClass="blkNoLabel"/>
        <iais:value width="7">
            <iais:input cssClass="blkNo" maxLength="10" type="text" name="${prefix}blkNo${status.index}" value="${appGrpSecondAddr.blkNo}"/>
        </iais:value>
    </iais:row>
    <iais:row cssClass="operationDiv">
        <iais:field value="Floor / Unit No." width="5" cssClass="floorUnitLabel"/>
        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
            <div class="row">
                <iais:value cssClass="col-xs-12 col-md-5 ">
                    <input class="floorNo" maxlength="3" type="text" data-base="FloorNos" name="${status.index}FloorNos0" value="${appGrpSecondAddr.floorNo}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_${status.index}FloorNos0"></span>
                </iais:value>
                <div class="col-xs-12 col-md-2 text-center"><p>-</p></div>
                <iais:value cssClass="col-xs-12 col-md-5 ">
                    <input class="unitNo" maxlength="5" type="text" data-base="UnitNos" name="${status.index}UnitNos0" value="${appGrpSecondAddr.unitNo}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_${status.index}UnitNos0"></span>
                </iais:value>
            </div>
        </iais:value>
        <div class="operationAdlDiv hidden">
            <div class=" col-xs-7 col-sm-4 col-md-2 ">
                <p>(Additional)&nbsp;&nbsp;&nbsp;&nbsp;</p>
            </div>
            <div class=" col-xs-7 col-sm-4 col-md-1 text-center">
                <p class="text-danger opDel"><em class="fa fa-times-circle del-size-36"></em></p>
            </div>
        </div>
    </iais:row>
    <c:set var="hasAddFU" value="${appGrpSecondAddr.appPremisesOperationalUnitDtos.size()>0}" />
    <div class="operationDivGroup">
        <c:if test="${hasAddFU}">
            <c:forEach var="operationDto" items="${appGrpSecondAddr.appPremisesOperationalUnitDtos}" varStatus="opStat">
                <c:set var="opIndex" value="${opStat.index + 1}" />
                <iais:row cssClass="operationDiv">
                    <iais:field value="" width="5"/>
                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-5">
                        <div class="row">
                            <iais:value cssClass="col-xs-12 col-md-5 ">
                                <input class="floorNo" maxlength="3" type="text" data-base="FloorNos" name="${premValue}FloorNos${opIndex}" value="${operationDto.floorNo}" />
                                <span class="error-msg" name="iaisErrorMsg" id="error_${premValue}FloorNos${opIndex}"></span>
                            </iais:value>
                            <div class="col-xs-12 col-md-2 text-center"><p>-</p></div>
                            <iais:value cssClass="col-xs-12 col-md-5 ">
                                <input class="unitNo" maxlength="5" type="text" data-base="UnitNos" name="${premValue}UnitNos${opIndex}" value="${operationDto.unitNo}"/>
                                <span class="error-msg" name="iaisErrorMsg" id="error_${premValue}UnitNos${opIndex}"></span>
                            </iais:value>
                        </div>
                        <span class="error-msg" name="iaisErrorMsg" id="error_${premValue}FloorUnits${opIndex}"></span>
                    </iais:value>
                    <div class="operationAdlDiv">
                        <div class=" col-xs-7 col-sm-4 col-md-2 ">
                            <p>(Additional)&nbsp;&nbsp;&nbsp;&nbsp;</p>
                        </div>
                        <div class=" col-xs-7 col-sm-4 col-md-1 text-center">
                            <p class="text-danger opDel"><em class="fa fa-times-circle del-size-36"></em></p>
                        </div>
                    </div>
                </iais:row>
            </c:forEach>
        </c:if>
        <!--prem operational -->
        <iais:row cssClass="addOpDiv hidden">
            <iais:field value="" width="5"/>
            <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                <span class="addOperational"><a style="text-decoration:none;">+ Add Additional Floor/Unit No.</a></span>
            </iais:value>
        </iais:row>
    </div>
    <iais:row cssClass="address">
        <iais:field value="Street Name" mandatory="true" width="5"/>
        <iais:value width="5" cssClass="col-md-5">
            <iais:input cssClass="streetName" maxLength="32" type="text" name="${prefix}streetName${status.index}" value="${appGrpSecondAddr.streetName}"/>
        </iais:value>
    </iais:row>
    <iais:row cssClass="address">
        <iais:field value="Building Name" width="5"/>
        <iais:value width="5" cssClass="col-md-5">
            <iais:input cssClass="buildingName" maxLength="66" type="text" name="${prefix}buildingName${status.index}" value="${appGrpSecondAddr.buildingName}"/>
        </iais:value>
    </iais:row>
</div>