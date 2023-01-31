<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <iais:row>
        <label class="app-title">${currStepName}</label>
    </iais:row>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach items="${currentPreviewSvcInfo.appSvcChargesPageDto.generalChargesDtos}" var="generalChargesDto" varStatus="gcStatus">
            <iais:row>
                <div class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><strong>General Conveyance Charges<c:if test="${currentPreviewSvcInfo.appSvcChargesPageDto.generalChargesDtos.size() > 1}"> ${gcStatus.index+1}</c:if>:</strong></p>
                </div>
            </iais:row>
            <div class="chargeContent">
                <iais:row>
                    <iais:field width="5" value="Type of Charge"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${generalChargesDto.chargesType}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Amount From"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${generalChargesDto.minAmount}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Amount To"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${generalChargesDto.maxAmount}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Remarks"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${generalChargesDto.remarks}" />
                    </iais:value>
                </iais:row>
            </div>
        </c:forEach>

        <c:forEach items="${currentPreviewSvcInfo.appSvcChargesPageDto.otherChargesDtos}" var="otherChargesDto" varStatus="ocStatus">
            <iais:row>
                <div class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><strong>Medical Equipment and Other Charges<c:if test="${currentPreviewSvcInfo.appSvcChargesPageDto.otherChargesDtos.size() > 1}"> ${ocStatus.index+1}</c:if>:</strong></p>
                </div>
            </iais:row>
            <div class="chargeContent">
                <iais:row>
                    <iais:field width="5" value="Category"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${otherChargesDto.chargesCategory}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Type of Charge"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${otherChargesDto.chargesType}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Amount From"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${otherChargesDto.minAmount}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Amount To"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${otherChargesDto.maxAmount}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Remarks"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${otherChargesDto.remarks}" />
                    </iais:value>
                </iais:row>
            </div>
        </c:forEach>
    </div>
</div>
