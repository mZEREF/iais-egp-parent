<div class="viewPrem hidden form-horizontal">
    <iais:row>
        <div class="col-xs-12 col-md-6">
            <label class="app-title">Secondary Address &nbsp;&nbsp;<span class="premHeader" style="font-size: 2.0rem;"></span></label>
        </div>
        <div class="col-xs-12 col-md-3 text-right removeEditDiv hidden">
            <h4 class="text-danger">
                <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
            </h4>
        </div>
    </iais:row>
    <input type="hidden" class="id" value="">
    <iais:row cssClass="postalCodeDiv">
        <iais:field value="Postal Code" mandatory="true" width="5"/>
        <iais:value cssClass="col-xs-10 col-md-5">
            <iais:input cssClass="postalCode" maxLength="6" type="text" name="postalCode0"
                        value="${appGrpSecondAddr.postalCode}"/>
        </iais:value>
        <div class="col-xs-7 col-sm-6 col-md-3">
            <p><a class="retrieveAddr">Retrieve your address</a></p>
        </div>
    </iais:row>

    <iais:row>
        <iais:field value="Address Type" mandatory="true" width="5"/>
        <iais:value width="7" cssClass="addressType">
            <iais:select cssClass="addrType" name="addrType0" codeCategory="CATE_ID_ADDRESS_TYPE"
                         needSort="false"
                         firstOption="Please Select" value="${appGrpSecondAddr.addrType}"/>
        </iais:value>
    </iais:row>

    <iais:row cssClass="address">
        <iais:field value="Block / House No." width="5" cssClass="blkNoLabel"/>
        <iais:value width="7">
            <iais:input cssClass="blkNo" maxLength="10" type="text" name="blkNo0"
                        value="${appGrpSecondAddr.blkNo}"/>
        </iais:value>
    </iais:row>


    <iais:row cssClass="operationDiv">
        <input type="hidden" class="othersId" value="">
        <iais:field value="Floor / Unit No." width="5" cssClass="floorUnitLabel"/>
        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
            <div class="row">
                <iais:value cssClass="col-xs-12 col-md-5 ">
                    <input class="floorNo" maxlength="3" type="text" data-base="FloorNo"
                           name="0FloorNo0"
                           value="${appGrpSecondAddr.floorNo}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_0FloorNo0"></span>
                </iais:value>
                <div class="col-xs-12 col-md-2 text-center"><p>-</p></div>
                <iais:value cssClass="col-xs-12 col-md-5 ">
                    <input class="unitNo" maxlength="5" type="text" data-base="UnitNo" name="0UnitNo0"
                           value="${appGrpSecondAddr.unitNo}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_0UnitNo0"></span>
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

    <div class="operationDivGroup">
        <!--prem operational -->
        <iais:row cssClass="addOpDiv">
            <iais:field value="" width="5"/>
            <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                    <span class="addOperational"><a
                            style="text-decoration:none;">+ Add Additional Floor/Unit No.</a></span>
            </iais:value>
        </iais:row>
    </div>


    <iais:row cssClass="address">
        <iais:field value="Street Name" mandatory="true" width="5"/>
        <iais:value width="5" cssClass="col-md-5">
            <iais:input cssClass="streetName" maxLength="32" type="text" name="streetName0"
                        value="${appGrpSecondAddr.streetName}"/>
        </iais:value>
    </iais:row>
    <iais:row cssClass="address">
        <iais:field value="Building Name" width="5"/>
        <iais:value width="5" cssClass="col-md-5">
            <iais:input cssClass="buildingName" maxLength="66" type="text" name="buildingName0"
                        value="${appGrpSecondAddr.buildingName}"/>
        </iais:value>
    </iais:row>
</div>


