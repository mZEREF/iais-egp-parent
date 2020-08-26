
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<style>
  body{
    font-size: 14px;padding: 2%;
  }
  *{
    word-wrap: break-word
  }
  p {
    margin: 0 0 0px;
  }
</style>
<div class="panel-main-content">
  <input style="display: none" value="${NOT_VIEW}" id="view">
  <c:set var="appGrpPremisesDtoList" value="${appSubmissionDto.appGrpPremisesDtoList}"></c:set>
  <c:set var="oldAppGrpPremisesDtoList" value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList}"></c:set>
  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST007')}">

  </c:if>

  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST001')}">
    <div class="amended-service-info-gp">
      <label style="font-size: 2.2rem">${stepNameMap['SVST001']}</label>
      <c:forEach var="appSvcLaboratoryDisciplinesDto" items="${currentPreviewSvcInfo.appSvcLaboratoryDisciplinesDtoList}" varStatus="status">
        <div class="amend-preview-info">
          <p><span class="preview-title col-xs-2 col-md-2" style="padding-right: 0%">Premises ${status.index+1}</span>
          <div class="col-xs-6">
            <span class="newVal " attr="${appGrpPremisesDtoList[status.index].address}" ><c:out value="${appGrpPremisesDtoList[status.index].address}"/></span>

          </div>
          <div class="col-xs-6">
            <span class="oldVal " attr="${oldAppGrpPremisesDtoList[status.index].address}" style="display: none" ><c:out value="${oldAppGrpPremisesDtoList[status.index].address}"/></span>

          </div>

          </p>

          <div class="form-check-gp">
            <div class="row">
              <div class="col-xs-12" style="margin-left: 2%">
                <c:forEach var="checkList" items="${appSvcLaboratoryDisciplinesDto.appSvcChckListDtoList}" varStatus="statuss">
                  <div class="form-check active">
                    <p class="form-check-label " aria-label="premise-1-cytology">
                        <span class="check-square " style="margin-top: 2%;">
                         <div class="col-xs-6 col-md-6">
                           <span class="newVal " style="margin-left: 3%" attr="${checkList.chkName}" ><c:out value="${checkList.chkName}"/></span>
                          </div>
                          <div class="col-xs-6 col-md-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcLaboratoryDisciplinesDtoList[status.index].appSvcChckListDtoList[statuss.index].chkName}"  style="display: none"><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcLaboratoryDisciplinesDtoList[status.index].appSvcChckListDtoList[statuss.index].chkName}"/></span>
                          </div>
                         </span>
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
  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST002')}">
    <div class="amended-service-info-gp">
      <label style="font-size: 2.2rem">${stepNameMap['SVST002']}</label>
      <div class="amend-preview-info">
        <c:forEach var="cgo" items="${currentPreviewSvcInfo.appSvcCgoDtoList}" varStatus="status">
        <p><strong class="col-xs-6">Clinical Governance Officer ${status.index+1}</strong><span class="col-xs-4 col-md-4"></span>
        </p>
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <table class="col-xs-12">
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>

                    <div class="col-xs-6 col-md-6">
                          <span class="newVal " attr="${cgo.salutation}" ><iais:code code="${cgo.salutation}"/></span>
                    </div>
                    <div class="col-xs-6 col-md-6">
                      <span class=" oldVal" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].salutation}" style="display: none"><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].salutation}"/></span>

                    </div>

                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div class="col-xs-6">
                      <span class="newVal " attr="${cgo.name}" ><iais:code code="${cgo.name}"/></span>

                    </div>
                    <div class="col-xs-6">
                      <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].name}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].name}</span>
                    </div>
                    </p>
                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type
                    </p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div class="col-xs-6">
                      <span class="newVal " attr="${cgo.idType}" ><c:out value="${cgo.idType}"/></span>

                    </div>
                    <div class="col-xs-6">
                      <span class=" oldVal" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idType}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idType}</span>

                    </div>
                    </p>
                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No. </p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div class="col-xs-6">
                      <span class="newVal " attr="${cgo.idNo}"><c:out value="${cgo.idNo}"/></span>
                    <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                    </div>
                    <div class="col-xs-6">
                      <span class="oldVal" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo}"  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo}</span>
                    </div>
                    </p>
                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation </p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div class="col-xs-6 col-md-6">
                      <span class="newVal " attr="${cgo.designation}"><iais:code code="${cgo.designation}" /></span>

                    </div>
                    <div class="col-xs-6 col-md-6">
                      <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].designation}" style="display: none"><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].designation}"/></span>
                    </div>
                    </p>
                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Type</p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div class="col-xs-6">
                      <span class="newVal " attr="${cgo.professionType}" ><iais:code code="${cgo.professionType}"/></span>
                    </div>
                    <div class="col-xs-6">
                      <span class="oldVal" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].professionType}" style="display: none"><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].professionType}"/></span>
                    </div>
                    </p>
                  </td>
                </tr>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Regn No.</p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div class="col-xs-6">
                      <span class="newVal " attr="${cgo.profRegNo}"><c:out value="${cgo.profRegNo}"/></span>
                      <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                    </div>
                    <div class="col-xs-6">
                      <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo}" style="display: none"><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo}"/></span>
                    </div>
                    </p>
                  </td>
                </tr>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Specialty</p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div class="col-xs-6">
                      <span class="newVal " attr="${cgo.speciality}" >
                        <c:choose>
                          <c:when test="${'other' == cgo.speciality}">
                            <c:out value="Others"/>
                          </c:when>
                          <c:otherwise>
                            <c:out value="${cgo.speciality}"/>
                          </c:otherwise>
                        </c:choose>
                        </span>
                    </div>
                    <div class="col-xs-6">
                      <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}</span>
                    </div>

                    </p>
                  </td>
                </tr>
                <c:if test="${'other' == cgo.speciality}">
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                    </td>
                    <td>
                      <div class="col-xs-6">
                      <span class="newVal " attr="other"><c:out value="${cgo.specialityOther}"/></span>
                      </div>
                    </td>
                  </tr>
                </c:if>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Subspecialty or relevant qualification</p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div class="col-xs-6">
                      <span class="newVal " attr="${cgo.subSpeciality}" ><c:out value="${cgo.subSpeciality}"/></span>
                    </div>
                    <div class="col-xs-6">
                      <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].subSpeciality}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].subSpeciality}</span>
                    </div>
                    </p>
                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No.</p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div class="col-xs-6">
                      <span class="newVal " attr="${cgo.mobileNo}" ><c:out value="${cgo.mobileNo}"/></span>
                    </div>
                    <div class="col-xs-6">
                      <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].mobileNo}"  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].mobileNo}</span>
                    </div>
                    </p>
                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address</p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div  class="col-xs-6">
                      <span class="newVal " attr="${cgo.emailAddr}" ><c:out value="${cgo.emailAddr}"/></span>
                    </div>
                    <div  class="col-xs-6">
                      <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].emailAddr}"  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].emailAddr}</span>
                    </div>
                    </p>
                  </td>
                </tr>
              </table>
            </div>
          </div>
        </div>
      </div>
      </c:forEach>
    </div>
  </c:if>

  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList,'SVST003')}">
    <div class="amended-service-info-gp">
      <label style="font-size: 2.2rem">${stepNameMap['SVST003']}</label>
      <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <table class="table discipline-table" border="1px">
                <thead >
                <tr >
                  <th  style="text-align: center">Premises</th>
                  <th  style="text-align: center">${stepNameMap['SVST001']}</th>
                  <th  style="text-align: center">Clinical Governance Officers</th>
                </tr>
                </thead>
                <c:forEach var="appGrpPrem" items="${appSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
                  <c:set var="hciNameOldAppSubmissionDtos" value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}"/>
                  <c:set var="conveyanceVehicleNoOldAppSubmissionDtos" value=" ${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}"/>
                  <c:if test="${hciNameOldAppSubmissionDtos!='' && hciNameOldAppSubmissionDtos!=null}" >
                    <c:set value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}" var="oldAppSubmissionDto"></c:set>
                  </c:if>
                  <c:set var="reloadMapValue" value="${appGrpPrem.premisesIndexNo}"/>
                  <c:set var="oldReloadMapValue" value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesIndexNo}"></c:set>
                  <c:forEach var="disciplineAllocation" items="${reloadDisciplineAllocationMap[reloadMapValue]}" varStatus="stat">
                    <c:set value="${reloadOld[reloadMapValue]}" var="reloaded"></c:set>
                    <tr>
                      <c:if test="${stat.first}">
                        <td style="text-align: center" rowspan="${reloadDisciplineAllocationMap[reloadMapValue].size()}">
                          <div class="col-xs-6">
                            <span class="newVal " attr="${appGrpPrem.address}"><c:out value="${appGrpPrem.address}"/></span>

                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].address}"  style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].address}"/></span>
                          </div>

                        </td>
                      </c:if>
                      <td style="text-align: center">
                        <p>
                        <div  class="col-xs-6">
                          <span class="newVal " attr="${disciplineAllocation.chkLstName}"><c:out value="${disciplineAllocation.chkLstName}"/></span>
                        </div>
                        <div  class="col-xs-6">
                          <span class="oldVal " attr="${reloadOld[oldReloadMapValue][stat.index].chkLstName}"  style="display: none"><c:out value="${reloadOld[oldReloadMapValue][stat.index].chkLstName}"/></span>
                        </div>
                        </p>

                      </td>
                      <td style="text-align: center">
                        <p>
                        <div class="col-xs-6">
                          <span class="newVal " attr="${disciplineAllocation.cgoSelName}" ><c:out value="${disciplineAllocation.cgoSelName}"/></span>
                        </div>
                        <div class="col-xs-6">
                          <span class="oldVal " attr="${reloadOld[oldReloadMapValue][stat.index].cgoSelName}"  style="display: none"><c:out value="${reloadOld[oldReloadMapValue][stat.index].cgoSelName}"/></span>
                        </div>
                        </p>
                      </td>
                    </tr>
                  </c:forEach>
                  </tbody>
                </c:forEach>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </c:if>

  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST006')}">
    <div class="amended-service-info-gp">
      <label style="font-size: 2.2rem">${stepNameMap['SVST006']}</label>
      <div class="amend-preview-info">
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <c:forEach items="${currentPreviewSvcInfo.appSvcPersonnelDtoList}" var="appSvcPersonnelDtoList" varStatus="status">
                <p><strong class="col-xs-6">Service Personnel ${status.index+1}:</strong></p>
                <span class="col-xs-6"></span>
                <table class="col-xs-12">
                  <c:choose>
                    <c:when test="${currentPreviewSvcInfo.serviceCode=='BLB'}">
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation</p>
                        </td>
                        <td>
                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.designation}"><c:out value="${appSvcPersonnelDtoList.designation}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].designation}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].designation}</span>
                          </div>


                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}"><c:out value="${appSvcPersonnelDtoList.name}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}</span>

                          </div>

                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Regn No.</p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.profRegNo}" ><c:out value="${appSvcPersonnelDtoList.profRegNo}"/></span>
                            <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo}</span>
                          </div>

                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years) </p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.wrkExpYear}" ><c:out value="${appSvcPersonnelDtoList.wrkExpYear}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}</span>
                          </div>

                          </p>
                        </td>
                      </tr>
                    </c:when>
                    <c:when test="${currentPreviewSvcInfo.serviceCode=='TCB'}">
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                        </td>
                        <td>

                          <div class="col-xs-6 col-md-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}" ><c:out value="${appSvcPersonnelDtoList.name}"/></span>
                          </div>
                          <div class="col-xs-6 col-md-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}</span>
                          </div>

                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification</p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.qualification}" ><c:out value="${appSvcPersonnelDtoList.qualification}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].qualification}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].qualification}</span>
                          </div>

                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years)</p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.wrkExpYear}" ><c:out value="${appSvcPersonnelDtoList.wrkExpYear}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}</span>
                          </div>


                          </p>
                        </td>
                      </tr>

                    </c:when>
                    <c:when test="${appSvcPersonnelDtoList.personnelType=='SPPT001'}">
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>PersonnelType</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Radiology Professional</p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}" ><c:out value="${appSvcPersonnelDtoList.name}"/></span>

                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}" style="display: none"><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"/></span>
                          </div>

                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation</p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.designation}" ><c:out value="${appSvcPersonnelDtoList.designation}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].designation}" style="display: none"><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].designation}"/></span>
                          </div>


                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification</p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.qualification}"><c:out value="${appSvcPersonnelDtoList.qualification}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].qualification}" style="display: none"><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].qualification}"/></span>
                          </div>

                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years)</p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.wrkExpYear}" ><c:out value="${appSvcPersonnelDtoList.wrkExpYear}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}" style="display: none"><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}"/></span>
                          </div>


                          </p>
                        </td>
                      </tr>
                    </c:when>
                    <c:when test="${appSvcPersonnelDtoList.personnelType=='SPPT002'}">
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>PersonnelType</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                            <span class="col-xs-6 col-md-6">Radiation Safety Officer</span>
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}" ><c:out value="${appSvcPersonnelDtoList.name}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}</span>

                          </div>


                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification</p>
                        </td>
                        <td>

                          <div  class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.qualification}" ><c:out value="${appSvcPersonnelDtoList.qualification}"/></span>

                          </div>
                          <div  class="col-xs-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].qualification}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].qualification}</span>
                          </div>


                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years)</p>
                        </td>
                        <td>

                          <div  class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.wrkExpYear}" ><c:out value="${appSvcPersonnelDtoList.wrkExpYear}"/></span>

                          </div>
                          <div  class="col-xs-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}</span>
                          </div>

                          </p>
                        </td>
                      </tr>
                    </c:when>
                    <c:when test="${appSvcPersonnelDtoList.personnelType=='SPPT003'}">
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>PersonnelType</p>
                        </td>
                        <td>
                          <span class="col-xs-6 col-md-6"> Medical Physicist</span>
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}"><c:out value="${appSvcPersonnelDtoList.name}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal  " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}" style="display: none"><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"/></span>

                          </div>


                          </p>
                        </td>
                      </tr>
                    </c:when>
                    <c:when test="${appSvcPersonnelDtoList.personnelType=='SPPT004'}">
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>PersonnelType</p>
                        </td>
                        <td>

                          <span class="col-xs-6 col-md-6">Registered Nurse</span></p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}" ><c:out value="${appSvcPersonnelDtoList.name}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}</span>

                          </div>


                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Registration No</p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.profRegNo}"><c:out value="${appSvcPersonnelDtoList.profRegNo}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo}</span>

                          </div>

                          </p>
                        </td>
                      </tr>
                    </c:when>
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
      <label style="font-size: 2.2rem">${stepNameMap['SVST004']}</label>
      <div class="amend-preview-info">
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <c:forEach items="${currentPreviewSvcInfo.appSvcPrincipalOfficersDtoList}" var="po" varStatus="status">
                <p><strong class="col-xs-6">Principal Officers ${status.index+1}:</strong></p>
                <table class="col-xs-12">
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${po.salutation}" ><iais:code code="${po.salutation}"/></span>

                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].salutation}" style="display: none"><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].salutation}"/></span>
                      </div>

                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>

                      <div  class="col-xs-6">
                        <span class="newVal " attr="${po.name}" ><c:out value="${po.name}"/></span>
                      </div>
                      <div  class="col-xs-6">
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].name}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].name}</span>
                      </div>
                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${po.idType}" ><c:out value="${po.idType}"/></span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idType}"  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idType}</span>
                      </div>

                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No.</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${po.idNo}"><c:out value="${po.idNo}"/></span>
                        <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                    </div>
                      <div class="col-xs-6">
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo}"  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo}</span>
                      </div>

                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${po.designation}" ><iais:code code="${po.designation}"/></span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].designation}"  style="display: none"><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].designation}"/></span>
                      </div>


                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Office Telephone No.
                      </p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${po.officeTelNo}"><c:out value="${po.officeTelNo}"/></span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].officeTelNo}" style="display: none"><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].officeTelNo}"/></span>
                      </div>


                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No.</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${po.mobileNo}" ><c:out value="${po.mobileNo}"/></span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].mobileNo}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].mobileNo}</span>
                      </div>
                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <div  class="col-xs-6">
                        <span class="newVal " attr="${po.emailAddr}" ><c:out value="${po.emailAddr}"/></span>
                      </div>
                      <div  class="col-xs-6">
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].emailAddr}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].emailAddr}</span>
                      </div>

                      </p>
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
      <label style="font-size: 2.2rem">${stepNameMap['SVST007']}</label>
      <div class="amend-preview-info">
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <c:forEach items="${currentPreviewSvcInfo.appSvcMedAlertPersonList}" var="appSvcMedAlertPerson" varStatus="status">
                <p><strong class="col-xs-6">Medalert Person ${status.index+1}:</strong></p>
                <span class="col-xs-6"></span>
                <table class="col-xs-12">

                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <div  class="col-xs-6">
                        <span class="newVal " attr="${appSvcMedAlertPerson.salutation}" ><iais:code code="${appSvcMedAlertPerson.salutation}"/></span>
                      </div>
                      <div  class="col-xs-6">
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].salutation}" style="display: none"><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].salutation}"/></span>
                      </div>
                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcMedAlertPerson.name}" ><c:out value="${appSvcMedAlertPerson.name}"/></span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].name}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].name}</span>
                      </div>


                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcMedAlertPerson.idType}"><c:out value="${appSvcMedAlertPerson.idType}"/></span>

                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idType}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idType}</span>
                      </div>


                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No.</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcMedAlertPerson.idNo}"><c:out value="${appSvcMedAlertPerson.idNo}"/></span>
                        <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                    </div>
                      <div class="col-xs-6">
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idNo}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idNo}</span>
                      </div>


                      </p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No.</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcMedAlertPerson.mobileNo}" ><c:out value="${appSvcMedAlertPerson.mobileNo}"/></span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].mobileNo}" style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].mobileNo}</span>
                      </div>
                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcMedAlertPerson.emailAddr}" ><c:out value="${appSvcMedAlertPerson.emailAddr}"/></span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].emailAddr}" style="display: none"><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].emailAddr}"/></span>
                      </div>

                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Preferred Mode of Receiving MedAlert</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <div class="col-xs-6">
                           <span class="newVal " attr="${appSvcMedAlertPerson.preferredMode}">
                            <c:choose>
                              <c:when test="${appSvcMedAlertPerson.preferredMode=='1'}">
                                <c:out value="Email"/>
                              </c:when>
                              <c:when test="${appSvcMedAlertPerson.preferredMode==2}">
                                <c:out value="SMS"/>
                              </c:when>
                              <c:when test="${appSvcMedAlertPerson.preferredMode==3}">
                                <c:out value="Email  SMS"/>
                              </c:when>
                            </c:choose>
                         </span>
                      </div>

                      <div class="col-xs-6">
                      <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].preferredMode}" style="display: none">
                           <c:choose>
                             <c:when test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].preferredMode=='1'}">Email</c:when>
                             <c:when test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].preferredMode=='2'}">SMS</c:when>
                             <c:when test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].preferredMode=='3'}">Email  SMS</c:when>
                             <c:when test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].preferredMode==null}">-</c:when>
                           </c:choose>
                      </span>
                      </div>

                      </p>
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
  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST005')}">
    <div class="amended-service-info-gp">
      <label style="font-size: 2.2rem">${stepNameMap['SVST005']}</label>
      <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <table class="col-xs-12">
                <c:forEach var="svcDoc" items="${currentPreviewSvcInfo.appSvcDocDtoLit}" varStatus="status">
                  <tr>
                    <td>
                      <div class="field col-sm-12 control-label formtext"><label>${svcDoc.upFileName}</label></div>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <span class="fileType" style="display:none">Docment1</span><span class="fileFilter" style="display:none">png</span><span class="fileMandatory" style="display:none">Yes</span>
                    </td>
                  </tr>
                  <tr >
                    <td>
                        <%-- <a href="${pageContext.request.contextPath}/file-repo?filerepo=svcFileRoId${currentSvcCode}${status.index}&fileRo${status.index}=<iais:mask name="svcFileRoId${currentSvcCode}${status.index}" value="${svcDoc.fileRepoId}"/>&fileRepoName=${svcDoc.docName}" title="Download" class="downloadFile">${svcDoc.docName}</a>--%>

                          <div class="col-xs-6">
                             <span class="newVal " attr="${svcDoc.docSize}${svcDoc.docName}" >
                                    <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"
                                      value="${svcDoc.fileRepoId}"/>&fileRepoName=${svcDoc.docName}" title="Download" class="downloadFile">${svcDoc.docName}</a> <c:out value="(${svcDoc.docSize})KB"/>
                                   </span>
                          </div>
                          <div class="col-xs-6">
                                <span class="oldVal " attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docSize}${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docName}" style="display: none">
                                  <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"  value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].fileRepoId}"/>&fileRepoName=${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docName}" title="Download" class="downloadFile">
                                      ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docName}
                                  </a>
                                  <c:out value="(${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docSize})KB"/></span>
                          </div>

                    </td>
                  </tr>
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
    $(document).ready(function(){
        var svcId = "";
    <c:if test="${rfi=='rfi'}">
        $('.panel-body').attr("style","background-color: #999999;");
        </c:if>
    });
    $('#primaryCheckbox').click(function () {
      let jQuery = $(this).closest("div.panel-body");
        let jQuery1 = jQuery.attr("style");
        if(""==jQuery1){
            jQuery.attr("style","background-color: #999999;");
        }else {
            jQuery.attr("style","");
        }
    });
    $("#serviceCheckbox").click(function () {
        let jQuery = $(this).closest("div.panel-body");
        let jQuery1 = jQuery.attr("style");
        if(""==jQuery1){
            jQuery.attr("style","background-color: #999999;");
        }else {
            jQuery.attr("style","");
        }
    });
    $("#premisesCheckbox").click(function () {
        let jQuery = $(this).closest("div.panel-body");
        let jQuery1 = jQuery.attr("style");
        if(""==jQuery1){
            jQuery.attr("style","background-color: #999999;");
        }else {
            jQuery.attr("style","");
        }
    });
    hightLightChangeVal('newVal', 'oldVal');
    function hightLightChangeVal(newValClass, oldValClass) {
        $('.' + oldValClass).each(function () {
            var oldVal = $(this).attr('attr');
            var newEle = $(this).parent().prev().find('.' + newValClass);
            var newVal = newEle.length > 0 ? newEle.attr('attr') : '';
            if (oldVal.length > 0 && newVal.length > 0) {
                if (oldVal != newVal) {
                    $(this).show();
                    var newHtml=$(this).parent().prev().find('.' + newValClass).html();
                    var oldHtml=$(this).html();
                    $(this).html(newHtml);
                    $(this).parent().prev().find('.' + newValClass).html(oldHtml);
                    $(this).attr("class","newVal compareTdStyle");
                } else {
                    $(this).hide();
                }
            }
        });
    }
</script>
