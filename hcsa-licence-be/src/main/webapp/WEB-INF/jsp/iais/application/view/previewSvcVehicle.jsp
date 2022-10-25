<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <iais:row>
        <label class="app-title">${currStepName}</label>
    </iais:row>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach items="${currentPreviewSvcInfo.appSvcVehicleDtoList}" var="vehicleDto" varStatus="status">
            <iais:row>
                <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><strong>Vehicle<c:if test="${currentPreviewSvcInfo.appSvcVehicleDtoList.size() > 1}"> ${status.index+1}</c:if>: </strong></p>
                </div>
            </iais:row>
            <div class="vehicleContent">
                <iais:row>
                    <iais:field width="5" value="Vehicle Number"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${vehicleDto.displayName}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Chassis Number"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${vehicleDto.chassisNum}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Engine Number"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${vehicleDto.engineNum}" />
                    </iais:value>
                </iais:row>
            </div>
        </c:forEach>
    </div>
</div>


