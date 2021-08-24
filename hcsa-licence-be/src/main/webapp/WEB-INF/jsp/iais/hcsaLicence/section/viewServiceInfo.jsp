<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>
  body {
    font-size: 14px;
    padding: 2%;
  }

  * {
    word-wrap: break-word
  }

  p {
    margin: 0 0 0px;
  }
  .check-square{
      border-color: #999999 !important;
  }
  .check-square:before{
      color: #999999 !important;
  }
</style>
<div class="panel-main-content">
  <input style="display: none" value="${NOT_VIEW}" id="view">
  <input type="hidden" id="oldAppSubmissionDto" value="${appSubmissionDto.oldAppSubmissionDto==null}">
  <c:set var="appGrpPremisesDtoList" value="${appSubmissionDto.appGrpPremisesDtoList}"></c:set>
  <c:set var="oldAppGrpPremisesDtoList" value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList}"></c:set>
  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST012')}">
    <div class="amended-service-info-gp">
      <label class="title-font-size">${stepNameMap['SVST012']}</label>
      <div class="amend-preview-info">
        <c:forEach var="businessDto" items="${currentPreviewSvcInfo.appSvcBusinessDtoList}" varStatus="status">
          <div class="col-xs-12">
            <strong >
              <c:choose>
                <c:when test="${'ONSITE' == businessDto.premType}">
                  <c:out value="Premises"/>
                </c:when>
                <c:when test="${'CONVEYANCE' == businessDto.premType}">
                  <c:out value="Conveyance"/>
                </c:when>
                <c:when test="${'OFFSITE'  == businessDto.premType}">
                  <c:out value="Off-site"/>
                </c:when>
                <c:when test="${'EASMTS'  == businessDto.premType}">
                  <c:out value="Conveyance"/>
                </c:when>
              </c:choose>
            </strong>
            : ${businessDto.premAddress}
          </div>
          <span class="col-xs-6"></span>
          <table  class="col-xs-12" aria-describedby="">
            <thead style="display: none">
              <tr>
                <th scope="col"></th>
              </tr>
            </thead>
            <tr>
              <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Business Name</p>
              </td>
              <td >
                <div class="col-xs-6">
                    <span class="newVal " attr="${businessDto.businessName}">
                        <c:out value="${businessDto.businessName}"></c:out>
                    </span>
                </div>
                <div class="col-xs-6">
                    <span class="oldVal " style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcBusinessDtoList[status.index].businessName}">
                        <c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcBusinessDtoList[status.index].businessName}"></c:out>
                    </span>
                </div>
              </td>
            </tr>
          </table>
        </c:forEach>
      </div>
    </div>
  </c:if>

  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST008')}">
    <div class="amended-service-info-gp">
      <label class="title-font-size">${stepNameMap['SVST008']}</label>
      <div class="amend-preview-info">
        <c:forEach var="appSvcVehicleDto" items="${currentPreviewSvcInfo.appSvcVehicleDtoList}" varStatus="status">
          <p><strong class="col-xs-6">Vehicle <c:if
                  test="${fn:length(currentPreviewSvcInfo.appSvcVehicleDtoList)>1}">${status.index+1}</c:if>:</strong><span
                  class="col-xs-4 col-md-4"></span>
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
                    <span class="newVal " attr="${appSvcVehicleDto.vehicleName}">
                        <c:out value="${appSvcVehicleDto.vehicleName}"></c:out>
                    </span>
                </div>
                <div class="col-xs-6">
                    <span class="oldVal " style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcVehicleDtoList[status.index].vehicleName}">
                        <c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcVehicleDtoList[status.index].vehicleName}"></c:out>
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
  </c:if>

  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST001')}">
    <div class="amended-service-info-gp">
      <label class="title-font-size">${stepNameMap['SVST001']}</label>
      <c:forEach var="appSvcLaboratoryDisciplinesDto"
                 items="${currentPreviewSvcInfo.appSvcLaboratoryDisciplinesDtoList}" varStatus="status">
        <div class="amend-preview-info">
          <p><label class="preview-title col-xs-2 col-md-3" style="padding-right: 0%;padding-left: 0px;">Mode of Service Delivery ${status.index+1}:</label>
          <div class="col-xs-2 col-md-3">
            <span class="newVal " attr="${appGrpPremisesDtoList[status.index].address}"><c:out
                    value="${appGrpPremisesDtoList[status.index].address}"/></span>
          <br>
          <span class="oldVal " attr="${oldAppGrpPremisesDtoList[status.index].address}" style="display: none"><c:out
                  value="${oldAppGrpPremisesDtoList[status.index].address}"/></span>
          </div>


          </p>

          <div class="form-check-gp">
            <div class="row">
              <div class="col-xs-12" style="margin-left: 0px;padding-left: 0px;">
                <c:set var="oldAppSvcChckListDtoList"
                       value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcLaboratoryDisciplinesDtoList[status.index].appSvcChckListDtoList}" />
                <c:forEach var="checkList" items="${appSvcLaboratoryDisciplinesDto.appSvcChckListDtoList}" varStatus="statuss">
                  <div class="form-check ">
                    <p class="form-check-label " aria-label="premise-1-cytology">
                    <div class="col-xs-12 col-md-12">
                        <span class="newVal " attr="${checkList.chkName}${checkList.check}">
                             <input style="cursor: default;" class="form-check-input"
                                    <c:if test="${checkList.check}">checked</c:if> type="checkbox" disabled>
                              <label class="form-check-label"><span class="check-square"></span>${checkList.chkName}</label>
                        </span>
                        <span class="oldVal "
                              attr="${oldAppSvcChckListDtoList[statuss.index].chkName}${oldAppSvcChckListDtoList[statuss.index].check}" style="display: none">
                                  <input style="cursor: default" class="form-check-input"
                                         <c:if test="${oldAppSvcChckListDtoList[statuss.index].check}">checked</c:if>
                                         type="checkbox" disabled>
                                  <label class="form-check-label">
                                    <span class="check-square"></span>
                                      ${oldAppSvcChckListDtoList[statuss.index].chkName}
                                  </label>
                        </span>
                    </div>
                    </p>
                  </div>
                </c:forEach>
              </div>
            </div>
          </div>
        </div>
      </c:forEach>
    </div>

  </c:if>
  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST009')}">
    <div class="amended-service-info-gp">
      <label class="title-font-size">Key Clinical Personnel</label>
      <div class="amend-preview-info">
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <c:forEach var="appSvcClinicalDirectorDto" items="${currentPreviewSvcInfo.appSvcClinicalDirectorDtoList}" varStatus="status">
                <p><strong class="col-xs-6">Key Clinical Personnel <c:if
                        test="${fn:length(currentPreviewSvcInfo.appSvcClinicalDirectorDtoList)>1}">${status.index+1}</c:if>:</strong><span
                        class="col-xs-4 col-md-4"></span>
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
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Board</p>
                    </td>
                    <td >
                      <div class="col-xs-6">
                          <span class="newVal " attr="${appSvcClinicalDirectorDto.professionBoard}">
                            <iais:code code="${appSvcClinicalDirectorDto.professionBoard}"></iais:code>
                          </span>
                      </div>
                      <div class="col-xs-6">
                          <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].professionBoard}">
                            <iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].professionBoard}"></iais:code>
                          </span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Regn. No.</p>
                    </td>
                    <td>
                      <div class="col-xs-6 img-show">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.profRegNo}">
                            ${appSvcClinicalDirectorDto.profRegNo}
                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                              <jsp:param name="idNo" value="${appSvcClinicalDirectorDto.profRegNo}"/>
                              <jsp:param name="methodName" value="showThisTableNewService"/>
                            </jsp:include>
                        </span>
                      </div>
                      <div class="col-xs-6 img-show">
                        <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].profRegNo}">
                            ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].profRegNo}
                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                              <jsp:param name="idNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].profRegNo}"/>
                              <jsp:param name="methodName" value="showThisTableOldService"/>
                            </jsp:include>
                        </span>
                      </div>
                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                        <jsp:param name="profRegNo" value="${appSvcClinicalDirectorDto.profRegNo}"/>
                        <jsp:param name="cssClass" value="new-img-show"/>
                        <jsp:param name="style" value="margin-left: -50%;"/>
                      </jsp:include>
                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                        <jsp:param name="profRegNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].profRegNo}"/>
                        <jsp:param name="cssClass" value="old-img-show"/>
                        <jsp:param name="style" value="margin-left: -50%;"/>
                      </jsp:include>
                    </td>
                  </tr>
                  <c:if test="${'MTS'==currentPreviewSvcInfo.serviceCode}">
                    <tr>
                      <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Not registered with a Professional Board</p>
                      </td>
                      <td>
                        <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.noRegWithProfBoard}">
                          <c:choose>
                            <c:when test="${appSvcClinicalDirectorDto.noRegWithProfBoard=='1'}">
                              <div class="form-check active">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                              </div>
                            </c:when>
                            <c:otherwise>
                              <div class="form-check ">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                              </div>
                            </c:otherwise>
                          </c:choose>
                        </span>
                        </div>
                        <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].noRegWithProfBoard}">
                           <c:choose>
                             <c:when test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].noRegWithProfBoard=='1'}">
                               <div class="form-check active">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                              </div>
                             </c:when>
                             <c:otherwise>
                               <div class="form-check ">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                              </div>
                             </c:otherwise>
                           </c:choose>
                        </span>
                        </div>
                      </td>
                    </tr>
                  </c:if>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation</p>
                    </td>
                    <td>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.salutation}">
                          <iais:code code="${appSvcClinicalDirectorDto.salutation}"></iais:code>
                        </span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].salutation}">
                          <iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].salutation}"></iais:code>
                        </span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                    </td>
                    <td>
                      <div class="col-xs-12 img-show">
                      <span class="newVal " attr="${appSvcClinicalDirectorDto.name}">
                        <c:out value="${appSvcClinicalDirectorDto.name}"/>
                        <c:if test="${not empty proHashMap[appSvcClinicalDirectorDto.profRegNo]}">
                          <c:if test="${proHashMap[appSvcClinicalDirectorDto.profRegNo].name==appSvcClinicalDirectorDto.name}">
                            <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                          </c:if>
                          <c:if test="${proHashMap[appSvcClinicalDirectorDto.profRegNo].name!=appSvcClinicalDirectorDto.name}">
                            <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNewService(this)" width="25" height="25" alt="NETS">
                          </c:if>
                        </c:if>

                      </span>
                        <br>
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].name}"
                              style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].name}
                        <c:if test="${not empty proHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].profRegNo]}">
                          <c:if test="${proHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].profRegNo].name==currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].name}">
                            <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                          </c:if>
                          <c:if test="${proHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].profRegNo].name!=currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].name}">
                             <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableOldService(this)"
                                  width="25" height="25" alt="NETS">
                          </c:if>
                        </c:if>

                      </span>
                      </div>
                      <c:if test="${not empty proHashMap[appSvcClinicalDirectorDto.profRegNo]}">
                        <div class="row new-img-show" style="display: none">
                          <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left:20%;width: 35%">
                            <label style="font-weight: normal">The name of this personnel as listed in PRS is:
                            </label><span style="position: absolute;right: 0px;color: black"
                                          onclick="closeThis(this)">X</span>
                            <table aria-describedby="" border="1px" class="col-xs-12" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff">
                              <tr>
                                <td>${proHashMap[appSvcClinicalDirectorDto.profRegNo].name}</td>
                              </tr>
                            </table>
                          </div>
                        </div>
                      </c:if>
                      <c:if test="${not empty proHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].profRegNo]}">
                        <div class="row old-img-show" style="display: none">
                          <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left:20%;width: 35%">
                            <label style="font-weight: normal">The name of this personnel as listed in PRS is:
                            </label><span style="position: absolute;right: 0px;color: black"
                                          onclick="closeThis(this)">X</span>
                            <table aria-describedby="" border="1px" class="col-xs-12" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff">
                              <tr>
                                <td>${proHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].profRegNo].name}</td>
                              </tr>

                            </table>
                          </div>
                        </div>
                      </c:if>

                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type</p>
                    </td>
                    <td>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.idType}">
                            <iais:code code="${appSvcClinicalDirectorDto.idType}"></iais:code>
                        </span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].idType}">
                            <iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].idType}"></iais:code>
                        </span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No.</p>
                    </td>
                    <td>
                      <div class="col-xs-6 img-show">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.idNo}">
                            ${appSvcClinicalDirectorDto.idNo}
                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                              <jsp:param name="idNo" value="${appSvcClinicalDirectorDto.idNo}"/>
                              <jsp:param name="methodName" value="showThisTableNewService"/>
                            </jsp:include>
                        </span>
                      </div>
                      <div class="col-xs-6 img-show">
                        <span class="oldVal " style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].idNo}">
                              ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].idNo}
                          <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                            <jsp:param name="idNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].idNo}"/>
                            <jsp:param name="methodName" value="showThisTableOldService"/>
                          </jsp:include>
                        </span>
                      </div>
                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                        <jsp:param name="idNo" value="${appSvcClinicalDirectorDto.idNo}"/>
                        <jsp:param name="cssClass" value="new-img-show"/>
                        <jsp:param name="style" value="margin-left: -50%;"/>
                      </jsp:include>
                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                        <jsp:param name="idNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].idNo}"/>
                        <jsp:param name="cssClass" value="old-img-show"/>
                        <jsp:param name="style" value="margin-left: -50%;"/>
                      </jsp:include>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation</p>
                    </td>
                    <td>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.designation}">
                            <iais:code code="${appSvcClinicalDirectorDto.designation}"></iais:code>
                        </span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].designation}">
                            <iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].designation}"></iais:code>
                        </span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Speciality</p>
                    </td>
                    <td>
                      <div class="col-xs-6">
                        <span class="newVal" attr="${appSvcClinicalDirectorDto.speciality}">
                          <iais:code code="${appSvcClinicalDirectorDto.speciality}"></iais:code>
                        </span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal"  style="display: none"
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].speciality}">
                          <iais:code
                                  code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].speciality}"></iais:code>
                        </span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Date when specialty was obtained</p>
                    </td>
                    <td>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.specialtyGetDate}">
                            <fmt:formatDate value="${appSvcClinicalDirectorDto.specialtyGetDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                        </span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].specialtyGetDate}">
                          <fmt:formatDate value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].specialtyGetDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                        </span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Registration Date</p>
                    </td>
                    <td>
                      <div class="col-xs-12">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.typeOfCurrRegi}">
                            ${appSvcClinicalDirectorDto.typeOfCurrRegi}
                        </span>
                        <br>
                        <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].typeOfCurrRegi}">
                            ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].typeOfCurrRegi}
                        </span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Current Registration Date</p>
                    </td>
                    <td>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.currRegiDate}">
                          <fmt:formatDate value="${appSvcClinicalDirectorDto.currRegiDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                        </span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].currRegiDate}">
                          <fmt:formatDate value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].currRegiDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                        </span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Practicing Certificate End Date </p>
                    </td>
                    <td>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.praCerEndDate}">
                          <fmt:formatDate value="${appSvcClinicalDirectorDto.praCerEndDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                        </span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].praCerEndDate}">
                           <fmt:formatDate value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].praCerEndDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                        </span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Register </p>
                    </td>
                    <td>
                      <div class="col-xs-12">
                          <span class="newVal " attr="${appSvcClinicalDirectorDto.typeOfRegister}">
                              ${appSvcClinicalDirectorDto.typeOfRegister}
                          </span>
                        <br>
                        <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].typeOfRegister}">
                            ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].typeOfRegister}
                          </span>
                      </div>

                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant Experience </p>
                    </td>
                    <td>
                      <div class="col-xs-12">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.relevantExperience}">
                            ${appSvcClinicalDirectorDto.relevantExperience}
                        </span>
                        <br>
                        <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].relevantExperience}">
                            ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].relevantExperience}
                        </span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Clinical Director (CD) holds a valid certification issued by an Emergency Medical Services ("EMS") Medical Directors workshop </p>
                    </td>
                    <td>
                      <div class="col-xs-6">
                        <span class="newVal" attr="${appSvcClinicalDirectorDto.holdCerByEMS}">
                          <c:if test="${appSvcClinicalDirectorDto.holdCerByEMS=='1'}">Yes</c:if>
                          <c:if test="${appSvcClinicalDirectorDto.holdCerByEMS=='0'}">No</c:if>
                        </span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].holdCerByEMS}">
                          <c:if test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].holdCerByEMS=='1'}">Yes</c:if>
                          <c:if test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].holdCerByEMS=='0'}">No</c:if>
                        </span>
                      </div>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Expiry Date (ACLS) </p>
                    </td>
                    <td>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.aclsExpiryDate}">
                            <fmt:formatDate value="${appSvcClinicalDirectorDto.aclsExpiryDate}" pattern="dd/MM/yyyy"/>
                        </span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].aclsExpiryDate}">
                           <fmt:formatDate value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].aclsExpiryDate}" pattern="dd/MM/yyyy"/>
                        </span>
                      </div>
                    </td>
                  </tr>
                  <c:if test="${'MTS'==currentPreviewSvcInfo.serviceCode}">

                    <tr>
                      <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Expiry Date (BCLS and AED) </p>
                      </td>
                      <td>
                        <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.bclsExpiryDate}">
                            <fmt:formatDate value="${appSvcClinicalDirectorDto.bclsExpiryDate}" pattern="dd/MM/yyyy"/>
                        </span>
                        </div>
                        <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].bclsExpiryDate}">
                            <fmt:formatDate value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].bclsExpiryDate}" pattern="dd/MM/yyyy"/>
                        </span>
                        </div>
                      </td>
                    </tr>
                  </c:if>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No.  </p>
                    </td>
                    <td>
                      <div  class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.mobileNo}">
                            ${appSvcClinicalDirectorDto.mobileNo}
                        </span>
                      </div>
                      <div  class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].mobileNo}">
                            ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].mobileNo}
                        </span>
                      </div>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address  </p>
                    </td>
                    <td>
                      <div class="col-xs-12">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.emailAddr}">
                            ${appSvcClinicalDirectorDto.emailAddr}
                        </span>
                        <br>
                        <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].emailAddr}">
                            ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index].emailAddr}
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
  </c:if>
  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST010')}">
    <div class="amended-service-info-gp">
      <label class="title-font-size">${stepNameMap['SVST010']}</label>
      <div class="amend-preview-info">
        <c:set value="${currentPreviewSvcInfo.appSvcChargesPageDto}" var="appSvcChargesPageDto"></c:set>
        <c:forEach items="${appSvcChargesPageDto.generalChargesDtos}" var="generalChargesDtos" varStatus="status">
          <p><strong class="col-xs-6">General Conveyance Charges <c:if
                  test="${fn:length(currentPreviewSvcInfo.appSvcChargesPageDto.generalChargesDtos)>1}">${status.index+1}</c:if>:</strong><span
                  class="col-xs-4 col-md-4"></span>
          </p>
          <span class="col-xs-6"></span>
          <table aria-describedby="" class="col-xs-12">
            <tr>
              <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Charge</p>
              </td>
              <td>
                <div class="col-xs-12">
                  <span class="newVal " attr="${generalChargesDtos.chargesType}">
                    <iais:code code="${generalChargesDtos.chargesType}"></iais:code>
                  </span>
                  <br>
                  <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.generalChargesDtos[status.index].chargesType}">
                     <iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.generalChargesDtos[status.index].chargesType}"></iais:code>
                  </span>
                </div>
              </td>
            </tr>
            <tr>
              <td  class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Amount From</p>
              </td>
              <td>
                <div class="col-xs-6">
                  <span class="newVal " attr="${generalChargesDtos.minAmount}">
                      ${generalChargesDtos.minAmount}
                  </span>
                </div>
                <div class="col-xs-6">
                  <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.generalChargesDtos[status.index].minAmount}">
                      ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.generalChargesDtos[status.index].minAmount}
                  </span>
                </div>
              </td>
            </tr>
            <tr>
              <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Amount To</p>
              </td>
              <td>
                <div class="col-xs-6">
                  <span class="newVal " attr="${generalChargesDtos.maxAmount}">
                      ${generalChargesDtos.maxAmount}
                  </span>
                </div>
                <div class="col-xs-6">
                  <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.generalChargesDtos[status.index].maxAmount}">
                      ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.generalChargesDtos[status.index].maxAmount}
                  </span>
                </div>
              </td>
            </tr>
            <tr>
              <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Remarks</p>
              </td>
              <td>
                <div class="col-xs-12">
                  <span class="newVal " attr="${generalChargesDtos.remarks}">
                      ${generalChargesDtos.remarks}
                  </span>
                  <br>
                  <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.generalChargesDtos[status.index].remarks}">
                      ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.generalChargesDtos[status.index].remarks}
                  </span>
                </div>

              </td>
            </tr>
          </table>
        </c:forEach>
        <c:forEach items="${appSvcChargesPageDto.otherChargesDtos}" var="otherChargesDtos" varStatus="status">
          <p><strong class="col-xs-6">Medical Equipment and Other Charges <c:if
                  test="${fn:length(currentPreviewSvcInfo.appSvcChargesPageDto.otherChargesDtos)>1}">${status.index+1}</c:if>:</strong><span
                  class="col-xs-4 col-md-4"></span>
          </p>
          <span class="col-xs-6"></span>
          <table aria-describedby="" class="col-xs-12">
            <tr>
              <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Category</p>
              </td>
              <td>
                <div class="col-xs-12">
                  <span class="newVal " attr="${otherChargesDtos.chargesCategory}">
                    <iais:code code="${otherChargesDtos.chargesCategory}"></iais:code>
                  </span>
                  <br>
                  <span class="oldVal "  style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].chargesCategory}">
                     <iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].chargesCategory}"></iais:code>
                  </span>
                </div>
              </td>
            </tr>
            <tr>
              <td  class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Charge</p>
              </td>
              <td>
                <div class="col-xs-12">
                  <span class="newVal " attr="${otherChargesDtos.chargesType}">
                    <iais:code code="${otherChargesDtos.chargesType}"></iais:code>
                  </span>
                  <br>
                  <span class="oldVal " style="display: none" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].chargesType}">
                     <iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].chargesType}"></iais:code>
                  </span>
                </div>
              </td>
            </tr>
            <tr>
              <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Amount From</p>
              </td>
              <td>
                <div class="col-xs-6">
                  <span class="newVal " attr="${otherChargesDtos.minAmount}">
                      ${otherChargesDtos.minAmount}
                  </span>
                </div>
                <div class="col-xs-6">
                  <span class="oldVal " style="display: none"  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].minAmount}">
                      ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].minAmount}
                  </span>
                </div>
              </td>
            </tr>
            <tr>
              <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Amount To</p>
              </td>
              <td>
                <div class="col-xs-6">
                  <span class="newVal " attr="${otherChargesDtos.maxAmount}">
                      ${otherChargesDtos.maxAmount}
                  </span>
                </div>
                <div class="col-xs-6">
                  <span class="oldVal " style="display: none"  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].maxAmount}">
                      ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].maxAmount}
                  </span>
                </div>
              </td>
            </tr>
            <tr>
              <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Remarks</p>
              </td>
              <td>
                <div class="col-xs-12">
                  <span class="newVal " attr="${otherChargesDtos.remarks}">
                      ${otherChargesDtos.remarks}
                  </span>
                  <span class="oldVal " style="display: none"  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].remarks}">
                      ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcChargesPageDto.otherChargesDtos[status.index].remarks}
                  </span>
                </div>
              </td>
            </tr>
          </table>
        </c:forEach>
      </div>

    </div>
  </c:if>

  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST002')}">
    <div class="amended-service-info-gp">
      <label class="title-font-size">${stepNameMap['SVST002']}</label>
      <div class="amend-preview-info">
        <c:forEach var="cgo" items="${currentPreviewSvcInfo.appSvcCgoDtoList}" varStatus="status">
        <p><strong class="col-xs-6">Clinical Governance Officer <c:if
                test="${fn:length(currentPreviewSvcInfo.appSvcCgoDtoList)>1}">${status.index+1}</c:if>:</strong><span
                class="col-xs-4 col-md-4"></span>
        </p>
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <table aria-describedby="" class="col-xs-12">
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation</p>
                  </td>
                  <td>

                      <div class="col-xs-6 col-md-6">
                      <span class="newVal " attr="${cgo.salutation}"><iais:code code="${cgo.salutation}"/></span>
                    </div>
                      <div class="col-xs-6 col-md-6">
                      <span class=" oldVal"
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].salutation}"
                            style="display: none"><iais:code
                              code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].salutation}"/></span>

                    </div>

                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                  </td>
                  <td>
                    <div class="col-xs-12 img-show">
                      <span class="newVal " attr="${cgo.name}">
                        <c:out value="${cgo.name}"/>
                        <c:if test="${not empty proHashMap[cgo.profRegNo]}">
                          <c:if test="${proHashMap[cgo.profRegNo].name==cgo.name}">
                            <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                          </c:if>
                          <c:if test="${proHashMap[cgo.profRegNo].name!=cgo.name}">
                            <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNewService(this)" width="25" height="25" alt="NETS">
                          </c:if>
                        </c:if>

                      </span>
                      <br>
                      <span class="oldVal "
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].name}"
                            style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].name}
                        <c:if test="${not empty proHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo]}">
                          <c:if test="${proHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo].name==currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].name}">
                            <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                          </c:if>
                          <c:if test="${proHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo].name!=currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].name}">
                             <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableOldService(this)"
                                  width="25" height="25" alt="NETS">
                          </c:if>
                        </c:if>

                      </span>
                    </div>
                    <c:if test="${not empty proHashMap[cgo.profRegNo]}">
                      <div class="row new-img-show" style="display: none">
                        <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left:20%;width: 35%">
                          <label style="font-weight: normal">The name of this personnel as listed in PRS is:
                          </label><span style="position: absolute;right: 0px;color: black"
                                        onclick="closeThis(this)">X</span>
                          <table aria-describedby="" border="1px" class="col-xs-12" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff">
                            <tr>
                              <td>${proHashMap[cgo.profRegNo].name}</td>
                            </tr>
                          </table>
                        </div>
                      </div>
                    </c:if>
                    <c:if test="${not empty proHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo]}">
                      <div class="row old-img-show" style="display: none">
                        <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left:20%;width: 35%">
                          <label style="font-weight: normal">The name of this personnel as listed in PRS is:
                          </label><span style="position: absolute;right: 0px;color: black"
                                        onclick="closeThis(this)">X</span>
                          <table aria-describedby="" border="1px" class="col-xs-12" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff">
                            <tr>
                              <td>${proHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo].name}</td>
                            </tr>

                          </table>
                        </div>
                      </div>
                    </c:if>

                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID
                      Type
                    </p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div class="col-xs-6">
                      <span class="newVal " attr="<iais:code code="${cgo.idType}"/>"><iais:code code="${cgo.idType}"/></span>

                    </div>
                    <div class="col-xs-6">
                      <span class=" oldVal"
                            attr="<iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idType}"/>"
                            style="display: none"><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idType}"/></span>

                    </div>
                    </p>
                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No.</p>
                  </td>
                  <td>
                      <div class="col-xs-6 img-show">
                        <span class="newVal" attr="${cgo.idNo}">
                          <c:out value="${cgo.idNo}"/>
                          <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                            <jsp:param name="idNo" value="${cgo.idNo}"/>
                            <jsp:param name="methodName" value="showThisTableNewService"/>
                          </jsp:include>
                      </span>
                      </div>

                    <div class="col-xs-6 img-show">
                      <span class="oldVal" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo}"
                            style="display: none">
                          ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo}
                          <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                            <jsp:param name="idNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo}"/>
                            <jsp:param name="methodName" value="showThisTableOldService"/>
                          </jsp:include>
                      </span>
                    </div>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                      <jsp:param name="idNo" value="${cgo.idNo}"/>
                      <jsp:param name="cssClass" value="new-img-show"/>
                      <jsp:param name="style" value="margin-left: -50%;"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                      <jsp:param name="idNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo}"/>
                      <jsp:param name="cssClass" value="old-img-show"/>
                      <jsp:param name="style" value="margin-left: -50%;"/>
                    </jsp:include>
                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation
                    </p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div class="col-xs-6 col-md-6">
                      <span class="newVal " attr="${cgo.designation}"><iais:code code="${cgo.designation}"/></span>

                    </div>
                    <div class="col-xs-6 col-md-6">
                      <span class="oldVal "
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].designation}"
                            style="display: none"><iais:code
                              code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].designation}"/></span>
                    </div>
                    </p>
                  </td>
                </tr>
                <c:if test="${cgo.designation=='DES999'||currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].designation=='DES999'}">
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      </p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <div class="col-xs-12 col-md-12">
                        <span class="newVal " attr="${cgo.otherDesignation}"><iais:code code="${cgo.otherDesignation}"/></span>
                        <br>
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].otherDesignation}"
                              style="display: none"><iais:code
                                code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].otherDesignation}"/></span>
                      </div>
                      </p>
                    </td>
                  </tr>
                </c:if>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional
                      Type</p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div class="col-xs-6">
                      <span class="newVal " attr="${cgo.professionType}"><iais:code
                              code="${cgo.professionType}"/></span>
                    </div>
                    <div class="col-xs-6">
                      <span class="oldVal"
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].professionType}"
                            style="display: none"><iais:code
                              code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].professionType}"/></span>
                    </div>
                    </p>
                  </td>
                </tr>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional
                      Regn. No.</p>
                  </td>
                  <td>
                    <div class="col-xs-6 img-show">
                      <span class="newVal " attr="${cgo.profRegNo}">
                        <c:out value="${cgo.profRegNo}"/>
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                          <jsp:param name="idNo" value="${cgo.profRegNo}"/>
                          <jsp:param name="methodName" value="showThisTableNewService"/>
                        </jsp:include>
                      </span>
                    </div>
                    <div class="col-xs-6 img-show">
                      <span class="oldVal "
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo}"
                            style="display: none">
                        <c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo}"/>
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                          <jsp:param name="idNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo}"/>
                          <jsp:param name="methodName" value="showThisTableOldService"/>
                        </jsp:include>
                      </span>
                    </div>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                      <jsp:param name="profRegNo" value="${cgo.profRegNo}"/>
                      <jsp:param name="cssClass" value="new-img-show"/>
                      <jsp:param name="style" value="margin-left: -50%;"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                      <jsp:param name="profRegNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo}"/>
                      <jsp:param name="cssClass" value="old-img-show"/>
                      <jsp:param name="style" value="margin-left: -50%;"/>
                    </jsp:include>
                  </td>
                </tr>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Specialty
                    </p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div class="col-xs-6">
                      <span class="newVal " attr="${cgo.speciality}">
                         <c:out value="${cgo.speciality}"/>
                      </span>
                      <br>
                      <span class="oldVal "
                          attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}"
                          style="display: none">
                        <c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}"/>
                      </span>
                    </div>
                    </p>
                  </td>
                </tr>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Sub-specialty</p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div class="col-xs-12">
                      <span class="newVal " attr="${cgo.subSpeciality}"><c:out value="${cgo.subSpeciality}"/></span>
                      <br>
                      <span class="oldVal "
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].subSpeciality}"
                            style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].subSpeciality}</span>
                    </div>
                    </p>
                  </td>
                </tr>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification</p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div class="col-xs-12">
                      <span class="newVal " attr="${cgo.qualification}"><c:out value="${cgo.qualification}"/></span>
                      <br>
                      <span class="oldVal "
                          attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].qualification}"
                          style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].qualification}</span>
                  </div>
                    </p>
                  </td>
                </tr>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Other Qualification</p>
                  </td>
                  <td>
                    <div class="col-xs-6">
                      <span class="newVal " attr="${cgo.otherQualification}"><c:out value="${cgo.otherQualification}"/></span>
                      <br>
                      <span class="oldVal "
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].otherQualification}"
                            style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].otherQualification}</span>
                    </div>

                  </td>
                </tr>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile
                      No.</p>
                  </td>
                  <td>
                    <div class="col-xs-6">
                      <span class="newVal " attr="${cgo.mobileNo}"><c:out value="${cgo.mobileNo}"/></span>
                    </div>
                    <div class="col-xs-6">
                      <span class="oldVal "
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].mobileNo}"
                            style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].mobileNo}</span>
                    </div>

                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email
                      Address</p>
                  </td>
                  <td>
                    <div class="col-xs-12">
                      <span class="newVal " attr="${cgo.emailAddr}"><c:out value="${cgo.emailAddr}"/></span>
                      <br>
                      <span class="oldVal "
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].emailAddr}"
                            style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].emailAddr}</span>
                    </div>

                  </td>
                </tr>
              </table>
            </div>
          </div>
        </div>
      </c:forEach>
      </div>
    </div>
  </c:if>
  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST013')}">
    <div class="amended-service-info-gp">
      <label class="title-font-size">${stepNameMap['SVST013']}</label>
      <div class="amend-preview-info">
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <c:forEach items="${currentPreviewSvcInfo.appSvcSectionLeaderList}" var="sectionLeader"
                   varStatus="status">
          <p><strong class="col-xs-6">Section Leader <c:if
                  test="${fn:length(currentPreviewSvcInfo.appSvcSectionLeaderList)>1}">${status.index+1}</c:if>:</strong>
          </p>
          <span class="col-xs-6"></span>
          <table aria-describedby="" class="col-xs-12">

            <tr>
              <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation
                </p>
              </td>
              <td>
                <div class="col-xs-6">
                        <span class="newVal " attr="${sectionLeader.salutation}"><iais:code
                                code="${sectionLeader.salutation}"/></span>
                </div>
                <div class="col-xs-6">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcSectionLeaderList[status.index].salutation}"
                              style="display: none"><iais:code
                                code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcSectionLeaderList[status.index].salutation}"/></span>
                </div>
              </td>
            </tr>
            <tr>
              <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name
                </p>
              </td>
              <td>
                <div class="col-xs-12">
                  <span class="newVal " attr="${sectionLeader.name}"><c:out value="${sectionLeader.name}"/></span>
                  <br>
                  <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcSectionLeaderList[status.index].name}" style="display: none">
                      ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcSectionLeaderList[status.index].name}
                  </span>
                </div>
              </td>
            </tr>
            <tr>
              <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification</p>
              </td>
              <td>
                <div class="col-xs-6">
                  <span class="newVal " attr="<iais:code code="${sectionLeader.qualification}"/>"><iais:code code="${sectionLeader.qualification}"/></span>

                </div>
                <div class="col-xs-6">
                        <span class="oldVal "
                              attr="<iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcSectionLeaderList[status.index].qualification}"/>"
                              style="display: none"><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcSectionLeaderList[status.index].qualification}"/></span>
                </div>

              </td>
            </tr>
            <tr>
              <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Working Experience (in terms of years)</p>
              </td>
              <td>
                <div class="col-xs-6">
                        <span class="newVal " attr="${sectionLeader.wrkExpYear}"><c:out
                                value="${sectionLeader.wrkExpYear}"/></span>
                </div>
                <div class="col-xs-6">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcSectionLeaderList[status.index].wrkExpYear}"
                              style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcSectionLeaderList[status.index].wrkExpYear}</span>
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
  </c:if>
  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList,'SVST003')}">
    <div class="amended-service-info-gp">
      <label class="title-font-size">${stepNameMap['SVST003']}</label>
      <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <c:if test="${reloadDisciplineAllocationMap_size>0}">
                <table aria-describedby="" class="table discipline-table" border="1px">
                  <thead>
                  <tr>
                    <th scope="col" style="text-align: center">Mode of Service Delivery</th>
                    <th scope="col" style="text-align: center">${stepNameMap['SVST001']}</th>
                    <th scope="col" style="text-align: center">Clinical Governance Officers</th>
                    <th scope="col" style="text-align: center">Section Leader</th>
                  </tr>
                  </thead>
                  <c:forEach var="appGrpPrem" items="${appSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
                    <c:set var="hciNameOldAppSubmissionDtos"
                           value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}"/>
                    <c:set var="conveyanceVehicleNoOldAppSubmissionDtos"
                           value=" ${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}"/>
                    <c:if test="${hciNameOldAppSubmissionDtos!='' && hciNameOldAppSubmissionDtos!=null}">
                      <c:set value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}"
                             var="oldAppSubmissionDto"></c:set>
                    </c:if>
                    <c:set var="reloadMapValue" value="${appGrpPrem.premisesIndexNo}"/>
                    <c:set var="oldReloadMapValue"
                           value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesIndexNo}"></c:set>
                    <c:forEach var="disciplineAllocation" items="${reloadDisciplineAllocationMap[reloadMapValue]}"
                               varStatus="stat">
                      <c:set value="${reloadOld[reloadMapValue]}" var="reloaded" />
                      <tr>
                        <c:if test="${stat.first}">
                          <td style="text-align: center" rowspan="${reloadDisciplineAllocationMap[reloadMapValue].size()}">
                            <div class="col-xs-12">
                            <span class="newVal " attr="${appGrpPrem.address}"><c:out value="${appGrpPrem.address}"/>
                            <br>
                            </span>
                              <span class="oldVal"
                                    attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].address}"
                                    style="display: none">
                                <c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].address}"/>
                            </span>
                            </div>
                          </td>
                        </c:if>
                        <td style="text-align: center">
                          <p>
                          <div class="col-xs-12">
                          <span class="newVal " attr="${disciplineAllocation.chkLstName}${disciplineAllocation.check}">
                            <c:out value="${disciplineAllocation.chkLstName}"/>
                          </span>
                          <br>
                          <span class="oldVal "
                                attr="${reloadOld[oldReloadMapValue][stat.index].chkLstName}${reloadOld[oldReloadMapValue][stat.index].check}"
                                style="display: none">
                            <c:out value="${reloadOld[oldReloadMapValue][stat.index].chkLstName}"/>
                          </span>
                          </div>
                          </p>
                        </td>
                        <td style="text-align: center">
                          <p>
                          <div class="col-xs-12">
                            <span class="newVal "
                                attr="${disciplineAllocation.cgoSelName}${disciplineAllocation.check}">
                              <c:out value="${disciplineAllocation.cgoSelName}"/>
                            </span>
                          <br>
                            <span class="oldVal "
                                attr="${reloadOld[oldReloadMapValue][stat.index].cgoSelName}${reloadOld[oldReloadMapValue][stat.index].check}"
                                style="display: none">
                              <c:out value="${reloadOld[oldReloadMapValue][stat.index].cgoSelName}"/>
                            </span>
                          </div>
                          </p>
                        </td>
                        <td style="text-align: center">
                          <p>
                            <div class="col-xs-12">
                              <span class="newVal" attr="${disciplineAllocation.sectionLeaderName}${disciplineAllocation.check}">
                                <c:out value="${disciplineAllocation.sectionLeaderName}"/>
                              </span>
                              <br>
                              <span class="oldVal "
                                    attr="${reloadOld[oldReloadMapValue][stat.index].sectionLeaderName}${reloadOld[oldReloadMapValue][stat.index].check}"
                                    style="display: none">
                                <c:out value="${reloadOld[oldReloadMapValue][stat.index].sectionLeaderName}"/>
                              </span>
                            </div>
                          </p>
                        </td>
                      </tr>
                    </c:forEach>
                    </tbody>
                  </c:forEach>
                </table>
              </c:if>
            </div>
          </div>
        </div>
      </div>
    </div>
  </c:if>

  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST006')}">
    <div class="amended-service-info-gp">
      <label class="title-font-size">${stepNameMap['SVST006']}</label>
      <div class="amend-preview-info">
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <c:forEach items="${currentPreviewSvcInfo.appSvcPersonnelDtoList}" var="appSvcPersonnelDtoList"
                         varStatus="status">
                <c:set var="oldAppSvcPersonnelDtoList"
                       value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index]}"/>
                <p><strong class="col-xs-6">Service Personnel <c:if
                        test="${fn:length(currentPreviewSvcInfo.appSvcPersonnelDtoList)>1}">${status.index+1}</c:if>:</strong>
                </p>
                <span class="col-xs-6"></span>
                <table aria-describedby="" class="col-xs-12">
                  <c:choose>
                    <c:when test="${currentPreviewSvcInfo.serviceCode=='BLB'}">
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology">
                            <span class="check-square"></span>Designation
                          </p>
                        </td>
                        <td>
                          <div class="col-xs-12">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.designation}">
                              <c:out value="${appSvcPersonnelDtoList.designation}"/>
                            </span>
                            <br>
                            <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.designation}" style="display: none">
                              <c:out value="${oldAppSvcPersonnelDtoList.designation}"/>
                            </span>
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology">
                            <span class="check-square"></span>Name
                          </p>
                        </td>
                        <td>
                          <div class="col-xs-12 img-show">
                            <span class="newVal" attr="${appSvcPersonnelDtoList.name}">
                              <c:out value="${appSvcPersonnelDtoList.name}"/>
                              <c:if test="${not empty proHashMap[appSvcPersonnelDtoList.profRegNo]}">
                                  <c:if test="${proHashMap[appSvcPersonnelDtoList.profRegNo].name==appSvcPersonnelDtoList.name}">
                                    <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                  </c:if>
                                  <c:if test="${proHashMap[appSvcPersonnelDtoList.profRegNo].name!=appSvcPersonnelDtoList.name}">
                                    <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNewService(this)" width="25" height="25" alt="NETS">
                                  </c:if>
                              </c:if>
                            </span>
                            <br/>
                            <span class="oldVal" attr="${oldAppSvcPersonnelDtoList.name}" style="display: none">
                                <c:out value="${oldAppSvcPersonnelDtoList.name}"/>
                                <c:if test="${not empty proHashMap[oldAppSvcPersonnelDtoList.profRegNo]}">
                                    <c:if test="${proHashMap[oldAppSvcPersonnelDtoList.profRegNo].name==oldAppSvcPersonnelDtoList.name}">
                                       <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS"></span>
                                    </c:if>
                                    <c:if test="${proHashMap[oldAppSvcPersonnelDtoList.profRegNo].name!=oldAppSvcPersonnelDtoList.name}">
                                      <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNewService(this)" width="25" height="25" alt="NETS">
                                    </c:if>
                                </c:if>
                            </span>
                          </div>
                          <c:if test="${not empty proHashMap[appSvcPersonnelDtoList.profRegNo]}">
                            <div class="row new-img-show" style="display: none">
                              <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left:20%;width: 35%">
                                <label style="font-weight: normal">The name of this personnel as listed in PRS is:
                                </label><span style="position: absolute;right: 0px;color: black"
                                                   onclick="closeThis(this)">X</span>
                                <table aria-describedby="" border="1px" class="col-xs-12" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff">
                                  <tr>
                                    <td>${proHashMap[appSvcPersonnelDtoList.profRegNo].name}</td>
                                  </tr>
                                </table>
                              </div>
                            </div>
                          </c:if>
                          <c:if test="${not empty proHashMap[oldAppSvcPersonnelDtoList.profRegNo]}">
                            <div class="row old-img-show" style="display: none">
                              <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left:20%;width: 35%">
                                <label style="font-weight: normal">The name of this personnel as listed in PRS is:
                                  </label><span style="position: absolute;right: 0px;color: black"
                                                   onclick="closeThis(this)">X</span>
                                <table aria-describedby="" border="1px" class="col-xs-12" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff">
                                  <tr>
                                    <td>${proHashMap[oldAppSvcPersonnelDtoList.profRegNo].name}</td>
                                  </tr>

                                </table>
                              </div>
                            </div>
                          </c:if>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional
                            Regn. No.</p>
                        </td>
                        <td>
                          <div class="col-xs-6 img-show">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.profRegNo}">
                              <c:out value="${appSvcPersonnelDtoList.profRegNo}"/>
                              <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                <jsp:param name="idNo" value="${appSvcPersonnelDtoList.profRegNo}"/>
                                <jsp:param name="methodName" value="showThisTableNewService"/>
                              </jsp:include>
                            </span>
                          </div>
                          <div class="col-xs-6 img-show">
                            <span class="oldVal" attr="${oldAppSvcPersonnelDtoList.profRegNo}" style="display: none">
                                ${oldAppSvcPersonnelDtoList.profRegNo}
                                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                  <jsp:param name="idNo" value="${oldAppSvcPersonnelDtoList.profRegNo}"/>
                                  <jsp:param name="methodName" value="showThisTableOldService"/>
                                </jsp:include>
                            </span>
                          </div>
                          <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                            <jsp:param name="profRegNo" value="${appSvcPersonnelDtoList.profRegNo}"/>
                            <jsp:param name="cssClass" value="new-img-show"/>
                            <jsp:param name="style" value="margin-left: -50%;"/>
                          </jsp:include>
                          <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                            <jsp:param name="profRegNo" value="${oldAppSvcPersonnelDtoList.profRegNo}"/>
                            <jsp:param name="cssClass" value="old-img-show"/>
                            <jsp:param name="style" value="margin-left: -50%;"/>
                          </jsp:include>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant
                            working experience (Years) </p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.wrkExpYear}"><c:out
                                    value="${appSvcPersonnelDtoList.wrkExpYear}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${oldAppSvcPersonnelDtoList.wrkExpYear}"
                                  style="display: none">${oldAppSvcPersonnelDtoList.wrkExpYear}</span>
                          </div>

                          </p>
                        </td>
                      </tr>
                    </c:when>
                    <c:when test="${currentPreviewSvcInfo.serviceCode=='TSB'}">
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name
                          </p>
                        </td>
                        <td>
                          <div class="col-xs-6 col-md-12">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}">
                              <c:out value="${appSvcPersonnelDtoList.name}"/>
                            </span>
                            <br>
                            <span class="oldVal" attr="${oldAppSvcPersonnelDtoList.name}" style="display:none">
                              <c:out value="${oldAppSvcPersonnelDtoList.name}"/>
                            </span>
                          </div>

                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification
                          </p>
                        </td>
                        <td>
                          <div class="col-xs-12">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.qualification}"><c:out
                                    value="${appSvcPersonnelDtoList.qualification}"/></span>
                            <br>
                            <span class="oldVal "
                                  attr="${oldAppSvcPersonnelDtoList.qualification}"
                                  style="display: none">${oldAppSvcPersonnelDtoList.qualification}</span>
                          </div>


                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant
                            working experience (Years)</p>
                        </td>
                        <td>
                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.wrkExpYear}"><c:out
                                    value="${appSvcPersonnelDtoList.wrkExpYear}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${oldAppSvcPersonnelDtoList.wrkExpYear}"
                                  style="display: none">${oldAppSvcPersonnelDtoList.wrkExpYear}</span>
                          </div>

                        </td>
                      </tr>

                    </c:when>
                    <c:when test="${appSvcPersonnelDtoList.personnelType=='SPPT001'}">
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Select
                            Service Personnel</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Radiology
                            Professional</p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name
                          </p>
                        </td>
                        <td>

                          <div class="col-xs-12">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}">
                              <c:out value="${appSvcPersonnelDtoList.name}"/>
                            </span>
                            <br>
                            <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.name}" style="display: none">
                              <c:out value="${oldAppSvcPersonnelDtoList.name}"/>
                            </span>
                          </div>

                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation
                          </p>
                        </td>
                        <td>
                          <div class="col-xs-12">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.designation}">
                              ${appSvcPersonnelDtoList.designation}
                            </span>
                            <br>
                            <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.designation}" style="display: none">
                                ${oldAppSvcPersonnelDtoList.designation}
                            </span>
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification
                          </p>
                        </td>
                        <td>

                          <div class="col-xs-12">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.qualification}">
                             ${appSvcPersonnelDtoList.qualification}
                            </span>
                            <br>
                            <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.qualification}" style="display: none">
                                ${oldAppSvcPersonnelDtoList.qualification}
                            </span>
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant
                            working experience (Years)</p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.wrkExpYear}"><c:out
                                    value="${appSvcPersonnelDtoList.wrkExpYear}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${oldAppSvcPersonnelDtoList.wrkExpYear}"
                                  style="display: none"><c:out
                                    value="${oldAppSvcPersonnelDtoList.wrkExpYear}"/></span>
                          </div>


                        </td>
                      </tr>
                    </c:when>
                    <c:when test="${appSvcPersonnelDtoList.personnelType=='SPPT002'}">
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Select
                            Service Personnel</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                            <span class="col-xs-6 col-md-6">Radiation Safety Officer</span>
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name
                          </p>
                        </td>
                        <td>

                          <div class="col-xs-12">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}">
                              <c:out value="${appSvcPersonnelDtoList.name}"/>
                            </span>
                            <br>
                            <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.name}" style="display: none">
                              <c:out value="${oldAppSvcPersonnelDtoList.name}"/>
                            </span>
                          </div>

                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification
                          </p>
                        </td>
                        <td>

                          <div class="col-xs-12">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.qualification}"><c:out
                                    value="${appSvcPersonnelDtoList.qualification}"/>
                            </span>
                            <br>
                            <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.qualification}"
                                  style="display: none">${oldAppSvcPersonnelDtoList.qualification}
                            </span>
                          </div>


                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant
                            working experience (Years)</p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.wrkExpYear}"><c:out
                                    value="${appSvcPersonnelDtoList.wrkExpYear}"/></span>

                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${oldAppSvcPersonnelDtoList.wrkExpYear}"
                                  style="display: none">${oldAppSvcPersonnelDtoList.wrkExpYear}</span>
                          </div>

                        </td>
                      </tr>
                    </c:when>
                    <c:when test="${appSvcPersonnelDtoList.personnelType=='SPPT003'}">
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Select
                            Service Personnel</p>
                        </td>
                        <td>
                          <span class="col-xs-6 col-md-6"> Medical Physicist</span>
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name
                          </p>
                        </td>
                        <td>

                          <div class="col-xs-12">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}">
                              <c:out value="${appSvcPersonnelDtoList.name}"/>
                            </span>
                            <br>
                            <span class="oldVal  " attr="${oldAppSvcPersonnelDtoList.name}" style="display: none">
                              <c:out value="${oldAppSvcPersonnelDtoList.name}"/>
                            </span>
                          </div>
                        </td>
                      </tr>
                    </c:when>
                    <c:when test="${appSvcPersonnelDtoList.personnelType=='SPPT004'}">
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Select
                            Service Personnel</p>
                        </td>
                        <td>

                          <span class="col-xs-6 col-md-6">Registered Nurse</span></p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name
                          </p>
                        </td>
                        <td>

                          <div class="col-xs-12">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}">
                              <c:out value="${appSvcPersonnelDtoList.name}"/>
                            </span>
                            <br>
                            <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.name}" style="display: none">
                              <c:out value="${oldAppSvcPersonnelDtoList.name}"/>
                            </span>
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional
                            Regn. No.</p>
                        </td>
                        <td>

                          <div class="col-xs-6 img-show">
                            <span class="newVal" attr="${appSvcPersonnelDtoList.profRegNo}">
                              <c:out value="${appSvcPersonnelDtoList.profRegNo}"/>
                              <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                <jsp:param name="idNo" value="${appSvcPersonnelDtoList.profRegNo}"/>
                                <jsp:param name="methodName" value="showThisTableNewService"/>
                              </jsp:include>
                            </span>
                          </div>
                          <div class="col-xs-6 img-show">
                            <span class="oldVal" attr="${oldAppSvcPersonnelDtoList.profRegNo}" style="display: none">
                                ${oldAppSvcPersonnelDtoList.profRegNo}
                                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                  <jsp:param name="idNo" value="${oldAppSvcPersonnelDtoList.profRegNo}"/>
                                  <jsp:param name="methodName" value="showThisTableOldService"/>
                                </jsp:include>
                            </span>
                          </div>
                          <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                            <jsp:param name="profRegNo" value="${appSvcPersonnelDtoList.profRegNo}"/>
                            <jsp:param name="cssClass" value="new-img-show"/>
                            <jsp:param name="style" value="margin-left: -50%;"/>
                          </jsp:include>
                          <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                            <jsp:param name="profRegNo" value="${oldAppSvcPersonnelDtoList.profRegNo}"/>
                            <jsp:param name="cssClass" value="old-img-show"/>
                            <jsp:param name="style" value="margin-left: -50%;"/>
                          </jsp:include>
                        </td>
                      </tr>
                    </c:when>
                    <c:otherwise>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name
                          </p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}">
                              <c:out value="${appSvcPersonnelDtoList.name}"/>
                            </span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.name}" style="display: none">
                              <c:out value="${oldAppSvcPersonnelDtoList.name}"/>
                            </span>
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification
                          </p>
                        </td>
                        <td>

                          <div class="col-xs-12">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.qualification}">
                              ${appSvcPersonnelDtoList.qualification}
                            </span>
                            <br>
                            <span class="oldVal " attr="${oldAppSvcPersonnelDtoList.qualification}" style="display: none">
                                ${oldAppSvcPersonnelDtoList.qualification}
                            </span>
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant
                            working experience (Years)</p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.wrkExpYear}"><c:out
                                    value="${appSvcPersonnelDtoList.wrkExpYear}"/></span>

                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${oldAppSvcPersonnelDtoList.wrkExpYear}"
                                  style="display: none">${oldAppSvcPersonnelDtoList.wrkExpYear}</span>
                          </div>

                        </td>
                      </tr>
                    </c:otherwise>
                  </c:choose>
                </table>
              </c:forEach>
            </div>
          </div>
        </div>

      </div>

    </div>
  </c:if>
  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST004')}">
    <div class="amended-service-info-gp">
      <label class="title-font-size">${stepNameMap['SVST004']}</label>
      <div class="amend-preview-info">
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <c:set value="1" var="poIndex"></c:set>
              <c:set value="1" var="dpoIndex"></c:set>
              <c:forEach items="${currentPreviewSvcInfo.appSvcPrincipalOfficersDtoList}" var="po" varStatus="status">
                <c:if test="${po.psnType =='PO'}">
                  <p><strong class="col-xs-6">Principal Officer(s) <c:if test="${PO_SIZE>1}">${poIndex}</c:if>:</strong>
                  </p>
                  <c:set var="poIndex" value="${poIndex+1}"></c:set>
                </c:if>
                <c:if test="${po.psnType =='DPO'}">
                  <p><strong class="col-xs-6">Nominee <c:if
                          test="${DPO_SIZE>1}">${dpoIndex}</c:if>:</strong></p>
                  <c:set var="dpoIndex" value="${dpoIndex+1}"></c:set>
                </c:if>
                <table aria-describedby="" class="col-xs-12">
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation</p>
                    </td>
                    <td>
                        <span class="check-square"></span>
                        <div class="col-xs-6">
                          <span class="newVal " attr="${po.salutation}"><iais:code code="${po.salutation}"/></span>
                        </div>
                        <div class="col-xs-6">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].salutation}"
                              style="display: none"><iais:code
                                code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].salutation}"/></span>
                      </div>

                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                    </td>
                    <td>
                      <div class="col-xs-12">
                        <span class="newVal " attr="${po.name}"><c:out value="${po.name}"/></span>
                        <br>
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].name}" style="display: none">
                            ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].name}
                        </span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID
                        Type</p>
                    </td>
                    <td>
                      <div class="col-xs-6">
                        <span class="newVal " attr="<iais:code code="${po.idType}"/>"><iais:code code="${po.idType}"/></span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal "
                              attr="<iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idType}"/>"
                              style="display: none"><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idType}"/></span>
                      </div>

                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID
                        No.</p>
                    </td>
                    <td>
                      <div class="col-xs-6 img-show">
                        <span class="newVal" attr="${po.idNo}">
                          <c:out value="${po.idNo}"/>
                          <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                            <jsp:param name="idNo" value="${po.idNo}"/>
                            <jsp:param name="methodName" value="showThisTableNewService"/>
                          </jsp:include>
                        </span>
                      </div>
                      <div class="col-xs-6 img-show">
                        <span class="oldVal"
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo}"
                              style="display: none">
                            <c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo}"/>
                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                              <jsp:param name="idNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo}"/>
                              <jsp:param name="methodName" value="showThisTableOldService"/>
                            </jsp:include>
                        </span>
                      </div>
                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                        <jsp:param name="idNo" value="${po.idNo}"/>
                        <jsp:param name="cssClass" value="new-img-show"/>
                        <jsp:param name="style" value="margin-left: -50%;"/>
                      </jsp:include>
                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                        <jsp:param name="idNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo}"/>
                        <jsp:param name="cssClass" value="old-img-show"/>
                        <jsp:param name="style" value="margin-left: -50%;"/>
                      </jsp:include>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation</p>
                    </td>
                    <td>

                      <div class="col-xs-6">
                        <span class="newVal " attr="${po.designation}"><iais:code code="${po.designation}"/></span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].designation}"
                              style="display: none"><iais:code
                                code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].designation}"/></span>
                      </div>
                    </td>
                  </tr>
                  <c:if test="${po.designation=='DES999'|| currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].designation=='DES999'}">
                    <tr>
                      <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                      </td>
                      <td>

                        <div class="col-xs-6">
                          <span class="newVal " attr="${po.otherDesignation}"><iais:code code="${po.otherDesignation}"/></span>
                        </div>
                        <div class="col-xs-6">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].otherDesignation}"
                              style="display: none"><iais:code
                                code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].otherDesignation}"/></span>
                        </div>
                      </td>
                    </tr>
                  </c:if>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Office
                        Telephone No.
                      </p>
                    </td>
                    <td>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${po.officeTelNo}"><c:out value="${po.officeTelNo}"/></span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].officeTelNo}"
                              style="display: none"><c:out
                                value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].officeTelNo}"/></span>
                      </div>

                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile
                        No.</p>
                    </td>
                    <td>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${po.mobileNo}"><c:out value="${po.mobileNo}"/></span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].mobileNo}"
                              style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].mobileNo}</span>
                      </div>

                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email
                        Address</p>
                    </td>
                    <td>
                      <div class="col-xs-12">
                        <span class="newVal " attr="${po.emailAddr}"><c:out value="${po.emailAddr}"/></span>
                        <br>
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].emailAddr}" style="display: none">
                            ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].emailAddr}
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

  </c:if>
  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST014')}">
    <div class="amended-service-info-gp">
      <label class="title-font-size">${stepNameMap['SVST014']}</label>
      <div class="amend-preview-info">
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <c:forEach items="${currentPreviewSvcInfo.appSvcKeyAppointmentHolderDtoList}" var="keyAppointmentHolder"
                         varStatus="status">
                <p><strong class="col-xs-6">Key Appointment Holder <c:if
                        test="${fn:length(currentPreviewSvcInfo.appSvcKeyAppointmentHolderDtoList)>1}">${status.index+1}</c:if>:</strong>
                </p>
                <span class="col-xs-6"></span>
                <table aria-describedby="" class="col-xs-12">

                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation
                      </p>
                    </td>
                    <td>
                      <div class="col-xs-6">
                              <span class="newVal " attr="${keyAppointmentHolder.salutation}"><iais:code
                                      code="${keyAppointmentHolder.salutation}"/></span>
                      </div>
                      <div class="col-xs-6">
                              <span class="oldVal "
                                    attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcKeyAppointmentHolderDtoList[status.index].salutation}"
                                    style="display: none"><iais:code
                                      code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcKeyAppointmentHolderDtoList[status.index].salutation}"/></span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name
                      </p>
                    </td>
                    <td>
                      <div class="col-xs-12">
                        <span class="newVal " attr="${keyAppointmentHolder.name}"><c:out value="${keyAppointmentHolder.name}"/></span>
                        <br>
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcKeyAppointmentHolderDtoList[status.index].name}" style="display: none">
                            ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcKeyAppointmentHolderDtoList[status.index].name}
                        </span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID
                        Type</p>
                    </td>
                    <td>
                      <div class="col-xs-6">
                        <span class="newVal " attr="<iais:code code="${keyAppointmentHolder.idType}"/>"><iais:code code="${keyAppointmentHolder.idType}"/></span>

                      </div>
                      <div class="col-xs-6">
                              <span class="oldVal "
                                    attr="<iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcKeyAppointmentHolderDtoList[status.index].idType}"/>"
                                    style="display: none"><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcKeyAppointmentHolderDtoList[status.index].idType}"/></span>
                      </div>

                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID
                        No.</p>
                    </td>
                    <td>
                      <div class="col-xs-6 img-show">
                              <span class="newVal" attr="${keyAppointmentHolder.idNo}">
                                <c:out value="${keyAppointmentHolder.idNo}"/>
                                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                  <jsp:param name="idNo" value="${keyAppointmentHolder.idNo}"/>
                                  <jsp:param name="methodName" value="showThisTableNewService"/>
                                </jsp:include>
                              </span>
                      </div>
                      <div class="col-xs-6 img-show">
                              <span class="oldVal"
                                    attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcKeyAppointmentHolderDtoList[status.index].idNo}"
                                    style="display: none">
                                  <c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcKeyAppointmentHolderDtoList[status.index].idNo}"/>
                                  <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                    <jsp:param name="idNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcKeyAppointmentHolderDtoList[status.index].idNo}"/>
                                    <jsp:param name="methodName" value="showThisTableOldService"/>
                                  </jsp:include>
                              </span>
                      </div>
                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                        <jsp:param name="idNo" value="${keyAppointmentHolder.idNo}"/>
                        <jsp:param name="cssClass" value="new-img-show"/>
                        <jsp:param name="style" value="margin-left: -50%;"/>
                      </jsp:include>
                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                        <jsp:param name="idNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcKeyAppointmentHolderDtoList[status.index].idNo}"/>
                        <jsp:param name="cssClass" value="old-img-show"/>
                        <jsp:param name="style" value="margin-left: -50%;"/>
                      </jsp:include>
                    </td>
                  </tr>

                </table>
              </c:forEach>
            </div>
          </div>
        </div>
      </div>
    </div>
  </c:if>
  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST007')}">
    <div class="amended-service-info-gp">
    <label class="title-font-size">${stepNameMap['SVST007']}</label>
    <div class="amend-preview-info">
      <div class="form-check-gp">
        <div class="row">
          <div class="col-xs-12">
            <c:forEach items="${currentPreviewSvcInfo.appSvcMedAlertPersonList}" var="appSvcMedAlertPerson"
                       varStatus="status">
              <p><strong class="col-xs-6">MedAlert Person <c:if
                      test="${fn:length(currentPreviewSvcInfo.appSvcMedAlertPersonList)>1}">${status.index+1}</c:if>:</strong>
              </p>
              <span class="col-xs-6"></span>
              <table aria-describedby="" class="col-xs-12">

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation
                    </p>
                  </td>
                  <td>
                    <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcMedAlertPerson.salutation}"><iais:code
                                code="${appSvcMedAlertPerson.salutation}"/></span>
                    </div>
                    <div class="col-xs-6">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].salutation}"
                              style="display: none"><iais:code
                                code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].salutation}"/></span>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name
                    </p>
                  </td>
                  <td>
                    <div class="col-xs-12">
                      <span class="newVal " attr="${appSvcMedAlertPerson.name}"><c:out value="${appSvcMedAlertPerson.name}"/></span>
                      <br>
                      <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].name}" style="display: none">
                          ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].name}
                      </span>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID
                      Type</p>
                  </td>
                  <td>
                    <div class="col-xs-6">
                        <span class="newVal " attr="<iais:code code="${appSvcMedAlertPerson.idType}"/>"><iais:code code="${appSvcMedAlertPerson.idType}"/></span>

                    </div>
                    <div class="col-xs-6">
                        <span class="oldVal "
                              attr="<iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idType}"/>"
                              style="display: none"><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idType}"/></span>
                    </div>

                  </td>
                </tr>
                <tr>
                  <td class="col-xs-6 img-show">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID
                      No.</p>
                  </td>
                  <td>
                    <div class="col-xs-6 img-show">
                        <span class="newVal " attr="${appSvcMedAlertPerson.idNo}">
                          <c:out value="${appSvcMedAlertPerson.idNo}"/>
                          <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                            <jsp:param name="idNo" value="${appSvcMedAlertPerson.idNo}"/>
                            <jsp:param name="methodName" value="showThisTableNewService"/>
                          </jsp:include>
                        </span>
                    </div>
                    <div class="col-xs-6 img-show">
                        <span class="oldVal"
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idNo}"
                              style="display: none">
                            ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idNo}
                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                              <jsp:param name="idNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idNo}"/>
                              <jsp:param name="methodName" value="showThisTableOldService"/>
                            </jsp:include>
                        </span>
                    </div>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                      <jsp:param name="idNo" value="${appSvcMedAlertPerson.idNo}"/>
                      <jsp:param name="cssClass" value="new-img-show"/>
                      <jsp:param name="style" value="margin-left: -50%;"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                      <jsp:param name="idNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idNo}"/>
                      <jsp:param name="cssClass" value="old-img-show"/>
                      <jsp:param name="style" value="margin-left: -50%;"/>
                    </jsp:include>
                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile
                      No.</p>
                  </td>
                  <td>
                    <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcMedAlertPerson.mobileNo}"><c:out
                                value="${appSvcMedAlertPerson.mobileNo}"/></span>
                    </div>
                    <div class="col-xs-6">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].mobileNo}"
                              style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].mobileNo}</span>
                    </div>

                  </td>
                </tr>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email
                      Address</p>
                  </td>
                  <td>

                    <div class="col-xs-12">
                        <span class="newVal " attr="${appSvcMedAlertPerson.emailAddr}"><c:out
                                value="${appSvcMedAlertPerson.emailAddr}"/></span>
                      <br>
                      <span class="oldVal "
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].emailAddr}"
                            style="display: none">
                          <c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].emailAddr}"/>
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
  </c:if>
  <%-- Document --%>
  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST005')}">
    <div class="amended-service-info-gp">
      <label class="title-font-size">${stepNameMap['SVST005']}</label>
      <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <table aria-describedby="" class="col-xs-12">
                <c:forEach var="svcDoc" items="${currentPreviewSvcInfo.multipleSvcDoc}" varStatus="status">
                  <c:set value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.multipleSvcDoc[svcDoc.key]}" var="oldSvcDoc"></c:set>
                  <tr>
                    <td>
                      <div class="field col-sm-12 control-label formtext"><label>${svcDoc.key}</label></div>
                    </td>
                  </tr>
                  <c:forEach items="${svcDoc.value}" var="sinage" varStatus="inx">
                    <tr>
                      <td>
                        <div class="col-xs-6">
                          <c:if test="${sinage.docSize!=null}">
                              <span class="newVal " attr="${sinage.md5Code}${sinage.docName}">
                                <iais:downloadLink fileRepoIdName="fileRo${inx.index}" fileRepoId="${sinage.fileRepoId}" docName="${sinage.docName}"/>
                                <c:out value="(${sinage.docSize} KB)"/>
                              </span>
                          </c:if>
                          <c:if test="${sinage.docSize==null}">
                              <span class="newVal " attr="${sinage.md5Code}${sinage.docName}"></span>
                          </c:if>
                        </div>
                        <div class="col-xs-6">
                          <c:if test="${oldSvcDoc[inx.index].docSize!=null}">
                              <span class="oldVal " attr="${oldSvcDoc[inx.index].md5Code}${oldSvcDoc[inx.index].docName}">
                                <iais:downloadLink fileRepoIdName="fileRo${inx.index}" fileRepoId="${oldSvcDoc[inx.index].fileRepoId}" docName="${oldSvcDoc[inx.index].docName}"/>
                                <c:out value="(${oldSvcDoc[inx.index].docSize} KB)"/>
                            </span>
                          </c:if>
                          <c:if test="${oldSvcDoc[inx.index].docSize==null}">
                                <span class="oldVal " attr="${oldSvcDoc[inx.index].md5Code}${oldSvcDoc[inx.index].docName}"></span>
                          </c:if>
                        </div>
                      </td>
                    </tr>
                  </c:forEach>
                </c:forEach>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </c:if>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        <c:if test="${rfi=='rfi'}">
        $('.panel-body').attr("style", "background-color: #999999;");
        </c:if>
    });

    function showThisTableNewService(obj) {
        $(obj).closest('div.img-show').closest('td').find("div.new-img-show").show();
    }

    function showThisTableOldService(obj) {
        $(obj).closest('div.img-show').closest('td').find('div.old-img-show').show();
    }

</script>
