<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#dpSovenorInventoryDetails">
                Sovenor Inventory Details
            </a>
        </h4>
    </div>
    <div id="dpSovenorInventoryDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <c:set var="sovenorInventoryDto" value="${dpSuperDataSubmissionDto.dpSovenorInventoryDto}"/>
                <iais:row>
                    <iais:field width="6" value="HCI Name" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${sovenorInventoryDto.hciName}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Drug Name" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${sovenorInventoryDto.drugName}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Batch Number" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${sovenorInventoryDto.batchNumber}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Drug Strength (ug/h)" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${sovenorInventoryDto.drugStrength}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Quantity of Drug Purchased" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${sovenorInventoryDto.quantityDrugPurchased}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Purchase Date" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${sovenorInventoryDto.purchaseDate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Delivery Date" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${sovenorInventoryDto.deliveryDate}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Expiry Date" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${sovenorInventoryDto.expiryDate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Quantity of balance stock as at 31 Dec 2017 (total for each batch number)" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${sovenorInventoryDto.quantityBalanceStock}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Quantity of expired stock as at 31 Dec 2017 (total for each batch number)" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${sovenorInventoryDto.quantityExpiredStock}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Remarks (if any)" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${sovenorInventoryDto.remarks}"/>
                    </iais:value>
                </iais:row>

            </div>
        </div>
    </div>
</div>