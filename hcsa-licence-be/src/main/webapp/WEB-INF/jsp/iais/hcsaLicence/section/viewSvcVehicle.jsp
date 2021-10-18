<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<div class="amended-service-info-gp">
    <label class="title-font-size">${stepNameMap['SVST008']}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                <c:forEach var="appSvcVehicleDto" items="${currentPreviewSvcInfo.appSvcVehicleDtoList}" varStatus="status">
                    <p>
                        <strong class="col-xs-6">
                            Vehicle
                            <c:if test="${fn:length(currentPreviewSvcInfo.appSvcVehicleDtoList)>1}">
                                ${status.index+1}
                            </c:if>:
                        </strong>
                        <span class="col-xs-4 col-md-4"></span>
                    </p>
                    <span class="col-xs-6"></span>
                    <table aria-describedby="" class="col-xs-12">
                        <thead style="display: none">
                        <tr>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Vehicle Number</p>
                            </td>
                            <td >
                                <div class="col-xs-6">
                                    <c:set var="displayVehicleName" value="${appSvcVehicleDto.displayName}" />
                                    <span class="newVal " attr="${displayVehicleName}">
                                        <c:out value="${displayVehicleName}"></c:out>
                                    </span>
                                </div>
                                <div class="col-xs-6">
                                    <c:set var="oldDisplayVehicleName" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcVehicleDtoList[status.index].displayName}" />
                                    <span class="oldVal " style="display: none" attr="${oldDisplayVehicleName}">
                                        <c:out value="${oldDisplayVehicleName}"></c:out>
                                    </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Chassis Number</p>
                            </td>
                            <td >
                                <div class="col-xs-6">
                                <span  class="newVal " attr="${appSvcVehicleDto.chassisNum}">
                                    <c:out value="${appSvcVehicleDto.chassisNum}"></c:out>
                                </span>
                                </div>
                                <div class="col-xs-6">
                                   <span  class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcVehicleDtoList[status.index].chassisNum}">
                                     <c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcVehicleDtoList[status.index].chassisNum}"></c:out>
                                   </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Engine Number</p>
                            </td>
                            <td >
                                <div class="col-xs-6">
                                    <span class="newVal " attr="${appSvcVehicleDto.engineNum}">
                                       <c:out value="${appSvcVehicleDto.engineNum}"></c:out>
                                    </span>
                                </div>
                                <div class="col-xs-6">
                                    <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcVehicleDtoList[status.index].engineNum}">
                                      <c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcVehicleDtoList[status.index].engineNum}"></c:out>
                                    </span>
                                </div>
                            </td>
                        </tr>
                    </table>
                </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>