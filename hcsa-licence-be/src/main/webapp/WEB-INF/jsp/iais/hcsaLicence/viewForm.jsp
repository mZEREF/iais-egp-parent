<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="egp-blank"/>
<webui:setLayout name="iais-blank"/>--%>


<style>
  body{
    font-size: 14px;padding: 2%;
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
      <label style="font-size: 2.2rem">LABORATORY DISCIPLINES</label>
      <c:forEach var="appSvcLaboratoryDisciplinesDto" items="${currentPreviewSvcInfo.appSvcLaboratoryDisciplinesDtoList}" varStatus="status">
        <div class="amend-preview-info">
          <p><span class="preview-title col-xs-2 col-md-2" style="padding-right: 0%">Premises ${status.index+1}</span><span>${appGrpPremisesDtoList[status.index].address} </span><%--${appSvcLaboratoryDisciplinesDto.premiseGetAddress}--%>
            <wrms:value width="7">
              <span class="newVal " attr="${appGrpPremisesDtoList[status.index].address}" style="display: none" ><label><c:out value=""/></label></span>
              <span class="oldVal compareTdStyle" attr="${oldAppGrpPremisesDtoList[status.index].address}" style="display: none"><label><c:out value="${oldAppGrpPremisesDtoList[status.index].address}"/></label></span>
            </wrms:value>
          </p>

          <div class="form-check-gp">
            <div class="row">
              <div class="col-xs-12" style="margin-left: 2%">
                <c:forEach var="checkList" items="${appSvcLaboratoryDisciplinesDto.appSvcChckListDtoList}" varStatus="statuss">
                  <div class="form-check active">
                      <p class="form-check-label " aria-label="premise-1-cytology"><span class="check-square "></span><span class="col-xs-6 col-md-6">${checkList.chkName}</span>
                        <wrms:value width="7">
                          <span class="newVal compareTdStyle" attr="${checkList.chkName}"  style="display: none"><label><c:out value="${checkList.chkName}"/></label></span>
                          <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcLaboratoryDisciplinesDtoList[status.index].appSvcChckListDtoList[statuss.index].chkName}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcLaboratoryDisciplinesDtoList[status.index].appSvcChckListDtoList[statuss.index].chkName}"/></label></span>
                        </wrms:value>
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
      <label style="font-size: 2.2rem">CLINICAL GOVERNANCE OFFICER</label>
      <div class="amend-preview-info">
        <c:forEach var="cgo" items="${currentPreviewSvcInfo.appSvcCgoDtoList}" varStatus="status">
        <p><strong class="col-xs-6">Clinical Governance Officer ${status.index+1}</strong><span class="col-xs-4 col-md-4">${cgo.name}</span></p>
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <table class="col-xs-12">
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> <span class="col-xs-6 col-md-6"><iais:code code="${cgo.salutation}" /></span>
                      <span class="col-xs-6 col-md-6">
                             <wrms:value width="7">
                               <span class="newVal compareTdStyle" attr="${cgo.salutation}" style="display: none"><label><iais:code code="${cgo.salutation}"/></label></span>
                               <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].salutation}" style="display: none"><label><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].salutation}"/></label></span>
                             </wrms:value>

                        </span>

                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> <span class="col-xs-6 col-md-6">${cgo.name }</span>
                      <span class="col-xs-6 col-md-6">
                           <span class="col-xs-6 col-md-6">
                                 <wrms:value width="7">
                                   <span class="newVal compareTdStyle" attr="${cgo.name }" style="display: none"><label><c:out value="${cgo.name }"/></label></span>
                                   <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].name}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].name}"/></label></span>
                                 </wrms:value>

                           </span>

                        </span>

                    </p>
                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type
                    </p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-6">${cgo.idType }</span>
                      <span class="col-xs-6 col-md-6">
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${cgo.idType }" style="display: none"><label><c:out value="${cgo.idType }"/></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idType}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idType}"/></label></span>
                            </wrms:value>
                        </span>
                    </p>
                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No. </p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-6">${cgo.idNo }</span>
                      <span class="col-xs-6 col-md-6">

                         <wrms:value width="7">
                           <span class="newVal compareTdStyle" attr="${cgo.idNo }"  style="display: none"><label><c:out value="${cgo.idNo }"/></label></span>
                           <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo}"/></label></span>
                         </wrms:value>

                       </span>

                    </p>
                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation </p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-6"><iais:code code="${cgo.designation}"/></span>
                      <span  class="col-xs-6 col-md-6">
                        <wrms:value width="7">
                          <span class="newVal compareTdStyle" attr="${cgo.designation}" style="display: none"><label><c:out value="${cgo.designation}"/></label></span>
                          <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].designation}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].designation}"/></label></span>
                        </wrms:value>
                        </span>
                    </p>
                  </td>
                </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Type</p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <span class="col-xs-6 col-md-6">
                            <c:choose>
                              <c:when test="${cgo.professionType=='PROF001'}">Dentist</c:when>
                              <c:when test="${cgo.professionType=='PROF002'}">Doctor</c:when>
                              <c:when test="${cgo.professionType=='PROF003'}">Nurse</c:when>
                            </c:choose>
                        </span>
                      <wrms:value width="7">
                        <span class="newVal compareTdStyle" attr="${cgo.professionType  }" style="display: none"><label><c:out value="${cgo.professionType}"/></label></span>
                        <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].professionType}" style="display: none">
                          <label>
                            <c:choose>
                              <c:when test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].professionType=='PROF001'  }"><c:out value="Dentist"/></c:when>
                              <c:when test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].professionType=='PROF002'  }"><c:out value="Doctor"/></c:when>
                              <c:when test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].professionType=='PROF003'  }"><c:out value="Nurse"/></c:when>
                            </c:choose>
                          </label>
                        </span>
                      </wrms:value>
                      </span>
                    </p>
                  </td>
                </tr>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Regn No.</p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> <span class="col-xs-6 col-md-6">${cgo.profRegNo}</span>
                      <span  class="col-xs-6 col-md-6">
                              <wrms:value width="7">
                                <span class="newVal compareTdStyle" attr="${cgo.profRegNo }" style="display: none"><label><c:out value="${cgo.profRegNo}"/></label></span>
                                <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo}"/></label></span>
                              </wrms:value>
                        </span>
                    </p>
                  </td>
                </tr>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Specialty</p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-6"> ${cgo.speciality }</span>
                      <span  class="col-xs-6 col-md-6">
                              <wrms:value width="7">
                                <span class="newVal compareTdStyle" attr="${cgo.speciality }" style="display: none"><label><c:out value=" ${cgo.speciality }"/></label></span>
                                <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}"/></label></span>
                              </wrms:value>
                        </span>
                    </p>
                  </td>
                </tr>
                <c:if test="${'other' == cgo.speciality}">
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                    </td>
                    <td>
                      <p><c:out value="${cgo.specialityOther}"/></p>
                    </td>
                  </tr>
                </c:if>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Subspeciality or relevant qualification </p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> </p>
                  </td>
                </tr>

                <tr>
                <td class="col-xs-6">
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No.</p>
                </td>
                <td>
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> <span class="col-xs-6 col-md-6">${cgo.mobileNo}</span>

                    <span class="col-xs-6 col-md-6">
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${cgo.mobileNo}"  style="display: none"><label><c:out value="${cgo.mobileNo}"/></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].mobileNo}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].mobileNo}"/></label></span>
                            </wrms:value>
                        </span>
                  </p>
                </td>
              </tr>

                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address</p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> <span class="col-xs-6 col-md-6">${cgo.emailAddr}</span>

                      <span class="col-xs-6 col-md-6">
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${cgo.emailAddr}"  style="display: none"><label><c:out value="${cgo.emailAddr}"/></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].emailAddr}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].mobileNo}"/></label></span>
                            </wrms:value>
                        </span>
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
      <label style="font-size: 2.2rem">DISCIPLINE ALLOCATION</label>
      <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <table class="table discipline-table" border="1px">
                <thead >
                <tr >
                  <th  style="text-align: center">Premises</th>
                  <th  style="text-align: center">Radiological Modalities</th>
                  <th  style="text-align: center">Clinical Governance Officers</th>
                </tr>
                </thead>
                <c:forEach var="appGrpPrem" items="${appSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
                  <c:set var="hciNameOldAppSubmissionDtos" value=" ${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}"/>
                  <c:set var="conveyanceVehicleNoOldAppSubmissionDtos" value=" ${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}"/>
                  <c:if test="${hciNameOldAppSubmissionDtos!='' && hciNameOldAppSubmissionDtos!=null}" >
                    <c:set value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}" var="oldAppSubmissionDto"></c:set>
                  </c:if>
                    <c:set var="reloadMapValue" value="${appGrpPrem.premisesIndexNo}"/>

                  <tbody>
                  <c:forEach var="disciplineAllocation" items="${reloadDisciplineAllocationMap[reloadMapValue]}" varStatus="stat">
                    <c:set value="${reloadOld[reloadMapValue]}" var="reloaded"></c:set>
                    ${stat.end}
                    <tr>
                      <c:if test="${stat.first}">
                        <td style="text-align: center" rowspan="${reloadDisciplineAllocationMap[reloadMapValue].size()}">
                            ${appGrpPrem.address}
                          <wrms:value width="7">
                            <span class="newVal " attr="${appGrpPrem.address} "  style="display: none"><label><c:out value=""/></label></span>
                            <span class="oldVal compareTdStyle" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].address}" style="display: none"><label><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].address}"/></label></span>
                          </wrms:value>
                        </td>
                      </c:if>
                      <td style="text-align: center">
                        <p>${disciplineAllocation.chkLstName} </p>
                        <wrms:value width="7">
                          <span class="newVal " attr="${disciplineAllocation.chkLstName}"  style="display: none"><label><c:out value="${disciplineAllocation.chkLstName}"/></label></span>
                          <span class="oldVal compareTdStyle" attr="${reloadOld[reloadMapValue][stat.index].chkLstName}" style="display: none"><label><c:out value="${reloadOld[reloadMapValue][stat.index].chkLstName}"/></label></span>
                        </wrms:value>

                      </td>
                      <td style="text-align: center">
                          ${disciplineAllocation.cgoSelName}

                        <wrms:value width="7">
                          <span class="newVal " attr="${disciplineAllocation.cgoSelName}"  style="display: none"><label><c:out value="${disciplineAllocation.cgoSelName}"/></label></span>
                          <span class="oldVal compareTdStyle" attr="${reloadOld[reloadMapValue][stat.index].cgoSelName}" style="display: none"><label><c:out value="${reloadOld[reloadMapValue][stat.index].cgoSelName}"/></label></span>
                        </wrms:value>

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


  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST004')}">
    <div class="amended-service-info-gp">
      <label style="font-size: 2.2rem">PRINCIPAL OFFICERS</label>
      <div class="amend-preview-info">
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <c:forEach items="${currentPreviewSvcInfo.appSvcPrincipalOfficersDtoList}" var="po" varStatus="status">
                <p><strong class="col-xs-6">Principal Officers ${status.index+1}:</strong><span class="col-xs-6">${po.name}</span></p>
                <table class="col-xs-12">
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-6"><iais:code code="${po.salutation}"/></span>
                        <span class="col-xs-6 col-md-6">
                          <wrms:value width="7">
                            <span class="newVal compareTdStyle" attr="${po.salutation}" style="display: none"><label><iais:code code="${po.salutation}"/></label></span>
                            <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].salutation}" style="display: none"><label><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].salutation}"/></label></span>
                          </wrms:value>

                       </span>

                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-6">${po.name}</span>

                        <span  class="col-xs-6 col-md-6">

                           <wrms:value width="7">
                             <span class="newVal compareTdStyle" attr="${po.name}" style="display: none"><label><c:out value="${po.name}"/></label></span>
                             <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].name}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].name}"/></label></span>
                           </wrms:value>

                        </span>

                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-6">${po.idType}</span>
                        <span class="col-xs-6 col-md-6">
                           <wrms:value width="7">
                             <span class="newVal compareTdStyle" attr="${po.idType}"  style="display: none"><label><c:out value="${po.idType}"/></label></span>
                             <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idType}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idType}"/></label></span>
                           </wrms:value>
                        </span>

                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No.</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-6">${po.idNo}</span>
                        <span  class="col-xs-6 col-md-6">
                              <wrms:value width="7">
                                <span class="newVal compareTdStyle" attr="${po.idNo}"  style="display: none"><label><c:out value="${po.idNo}"/></label></span>
                                <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo}"/></label></span>
                              </wrms:value>

                        </span>


                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-6"><iais:code code="${po.designation}"/></span>
                        <span  class="col-xs-6 col-md-6">
                          <wrms:value width="7">
                            <span class="newVal compareTdStyle" attr="${po.designation}" style="display: none"><label><iais:code code="${po.designation}"/></label></span>
                            <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].designation}" style="display: none"><label><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].designation}"/></label></span>
                          </wrms:value>

                        </span>

                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Office Telephone No.


                      </p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-6">${po.officeTelNo}</span>
                        <span  class="col-xs-6 col-md-6">
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${po.officeTelNo}" style="display: none"><label><c:out value="${po.officeTelNo}"/></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].officeTelNo}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].officeTelNo}"/></label></span>
                            </wrms:value>
                        </span>
                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No.</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-6">${po.mobileNo}</span>
                        <span class="col-xs-6 col-md-6">
                              <wrms:value width="7">
                                <span class="newVal compareTdStyle" attr="${po.mobileNo}" style="display: none"><label><c:out value="${po.mobileNo}"/></label></span>
                                <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].mobileNo}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].mobileNo}"/></label></span>
                              </wrms:value>

                          </span>

                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></span><span class="col-xs-6 col-md-6">${po.emailAddr}</span>
                        <span class="col-xs-6 col-md-6">
                          <wrms:value width="7">
                            <span class="newVal compareTdStyle" attr="${po.emailAddr}" style="display: none"><label><c:out value="${po.emailAddr}"/></label></span>
                            <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].emailAddr}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].emailAddr}"/></label></span>
                          </wrms:value>
                        </span>
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

  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST006')}">
    <div class="amended-service-info-gp">
      <label style="font-size: 2.2rem">SERVICE PERSONNEL</label>
      <div class="amend-preview-info">
        <c:forEach items="${currentPreviewSvcInfo.appSvcPersonnelDtoList}" var="appSvcPersonnelDtoList" varStatus="status">
          <div class="form-check-gp">
            <div class="row">
              <div class="col-xs-12">
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
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square">${appSvcPersonnelDtoList.designation}</span>
                            <span class="col-xs-6 col-md-6">
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.designation}" style="display: none"><label></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].designation}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].designation}"/></label></span>
                            </wrms:value>
                          </span>
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square">${appSvcPersonnelDtoList.name}</span>

                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.name}" style="display: none"><label></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"/></label></span>
                            </wrms:value>
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Regn No.</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.profRegNo}
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.profRegNo}" style="display: none"><label></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo}"/></label></span>
                            </wrms:value>
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years) </p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.wrkExpYear}
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.wrkExpYear}" style="display: none"><label></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}"/></label></span>
                            </wrms:value>
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
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.name}" style="display: none"><label></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"/></label></span>
                            </wrms:value>
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.quaification}
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.quaification}" style="display: none"><label></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].quaification}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].quaification}"/></label></span>
                            </wrms:value>
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years)</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.wrkExpYear}
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.wrkExpYear}" style="display: none"><label></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}"/></label></span>
                            </wrms:value>
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
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.name}" style="display: none"><label><c:out value="${appSvcPersonnelDtoList.name}"/></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"/></label></span>
                            </wrms:value>
                            </span>
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.designation}
                            <span class="col-xs-6 col-md-6">
                          <wrms:value width="7">
                            <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.designation}" style="display: none"><label><c:out value="${appSvcPersonnelDtoList.designation}"/></label></span>
                            <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].designation}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].designation}"/></label></span>
                          </wrms:value>
                        </span></p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.quaification}

                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.quaification}" style="display: none"><label><c:out value="${appSvcPersonnelDtoList.quaification}"/></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].quaification}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].quaification}"/></label></span>
                            </wrms:value>

                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years)</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.wrkExpYear}

                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.wrkExpYear}" style="display: none"><label><c:out value="${appSvcPersonnelDtoList.wrkExpYear}"/></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}"/></label></span>
                            </wrms:value>

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
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Radiation Safety Officer

                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.name}" style="display: none"><label></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"/></label></span>
                            </wrms:value>
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.quaification}
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.quaification}" style="display: none"><label></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].quaification}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].quaification}"/></label></span>
                            </wrms:value>
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years)</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.wrkExpYear}
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.wrkExpYear}" style="display: none"><label></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}"/></label></span>
                            </wrms:value>
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
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> Medical Physicist</p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.name}" style="display: none"><label><c:out value="${appSvcPersonnelDtoList.name}"/></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"/></label></span>
                            </wrms:value>
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
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Registered Nurse</p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.name}" style="display: none"><label></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"/></label></span>
                            </wrms:value>
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Registration No</p>
                        </td>
                        <td>
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.profRegNo}
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${appSvcPersonnelDtoList.profRegNo}" style="display: none"><label></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo}"/></label></span>
                            </wrms:value>
                          </p>
                        </td>
                      </tr>
                    </c:when>
                  </c:choose>
                </table>
              </div>
            </div>

          </div>

        </c:forEach>

      </div>

    </div>
  </c:if>
  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST007')}">
    <div class="amended-service-info-gp">
      <label style="font-size: 2.2rem">MEDALERT PERSON</label>
      <div class="amend-preview-info">
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <c:forEach items="${currentPreviewSvcInfo.appSvcMedAlertPersonList}" var="appSvcMedAlertPerson" varStatus="status">

                <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Medalert Person ${status.index+1}: </p>
                </div>
                <table class="col-xs-8">

                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"><iais:code code="${appSvcMedAlertPerson.salutation}"/></span>

                        <wrms:value width="7">
                          <span class="newVal compareTdStyle" attr="${appSvcMedAlertPerson.salutation}" style="display: none"><label><c:out value="${appSvcPersonnelDtoList.salutation}"/></label></span>
                          <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].salutation}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].salutation}"/></label></span>
                        </wrms:value>
                      </p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"><iais:code code="${appSvcMedAlertPerson.name}"/></span>
                        <wrms:value width="7">
                          <span class="newVal compareTdStyle" attr="${appSvcMedAlertPerson.name}" style="display: none"><label><c:out value="${appSvcPersonnelDtoList.name}"/></label></span>
                          <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].name}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].name}"/></label></span>
                        </wrms:value>
                      </p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"><iais:code code="${appSvcMedAlertPerson.idType}"/></span>
                        <wrms:value width="7">
                          <span class="newVal compareTdStyle" attr="${appSvcMedAlertPerson.idType}" style="display: none"><label><c:out value="${appSvcPersonnelDtoList.idType}"/></label></span>
                          <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idType}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idType}"/></label></span>
                        </wrms:value>
                      </p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No.</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"><iais:code code="${appSvcMedAlertPerson.idNo}"/></span>
                        <wrms:value width="7">
                          <span class="newVal compareTdStyle" attr="${appSvcMedAlertPerson.idNo}" style="display: none"><label><c:out value="${appSvcPersonnelDtoList.idNo}"/></label></span>
                          <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idNo}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idNo}"/></label></span>
                        </wrms:value>
                      </p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No.</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"><iais:code code="${appSvcMedAlertPerson.mobileNo}"/></span>
                        <wrms:value width="7">
                          <span class="newVal compareTdStyle" attr="${appSvcMedAlertPerson.mobileNo}" style="display: none"><label></label></span>
                          <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].mobileNo}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].mobileNo}"/></label></span>
                        </wrms:value>
                      </p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"><iais:code code="${appSvcMedAlertPerson.emailAddr}"/></span>
                        <wrms:value width="7">
                          <span class="newVal compareTdStyle" attr="${appSvcMedAlertPerson.emailAddr}" style="display: none"><label></label></span>
                          <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].emailAddr}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].emailAddr}"/></label></span>
                        </wrms:value>
                      </p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Preferred Mode of Receiving MedAlert</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                        <c:choose>
                          <c:when test="${appSvcMedAlertPerson.preferredMode=='1'}">Email</c:when>
                          <c:when test="${appSvcMedAlertPerson.preferredMode=='2'}">SMS</c:when>
                          <c:when test="${appSvcMedAlertPerson.preferredMode=='3'}">Email  SMS</c:when>
                        </c:choose>
                        <wrms:value width="7">
                          <span class="newVal compareTdStyle" attr="${appSvcMedAlertPerson.preferredMode}" style="display: none"><label></label></span>
                          <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].preferredMode}" style="display: none"><label>
                            <c:choose>
                              <c:when test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].preferredMode=='1'}">
                                <c:out value="Email"/>
                              </c:when>
                              <c:when test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].preferredMode==2}">
                                <c:out value="SMS"/>
                              </c:when>
                              <c:when test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].preferredMode==3}">
                                <c:out value="Email  SMS"/>
                              </c:when>
                            </c:choose>
                          </label></span>
                        </wrms:value>
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
      <label style="font-size: 2.2rem">SERVICE-RELATED DOCUMENTS</label>
      <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <table class="col-xs-12">
                <c:forEach var="svcDoc" items="${currentPreviewSvcInfo.appSvcDocDtoLit}" varStatus="status">
                  <tr>
                    <td>
                      <div class="field col-sm-6 control-label formtext"><label>${svcDoc.upFileName}</label></div>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <span class="fileType" style="display:none">Docment1</span><span class="fileFilter" style="display:none">png</span><span class="fileMandatory" style="display:none">Yes</span>
                    </td>
                  </tr>
                  <tr class="col-xs-12">
                    <td>
                        <%-- <a href="${pageContext.request.contextPath}/file-repo?filerepo=svcFileRoId${currentSvcCode}${status.index}&fileRo${status.index}=<iais:mask name="svcFileRoId${currentSvcCode}${status.index}" value="${svcDoc.fileRepoId}"/>&fileRepoName=${svcDoc.docName}" title="Download" class="downloadFile">${svcDoc.docName}</a>--%>
                      <div class="fileList">
                     <span class="filename server-site" id="130">
                       <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"
                     value="${svcDoc.fileRepoId}"/>&fileRepoName=${svcDoc.docName}" title="Download" class="downloadFile">${svcDoc.docName}</a>

                       (${svcDoc.docSize} KB)</span>

                        <wrms:value width="7">
                       <span class="newVal " attr="${svcDoc.docSize}${svcDoc.docName}" style="display: none"><label>
                        <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"  value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].fileRepoId}"/>&fileRepoName=${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docName}" title="Download" class="downloadFile">
                            ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docName}
                        </a> <c:out value="(${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docSize})KB"/>
                       </label></span>
                          <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docSize}${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docName}" style="display: none"><label>

                          <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"  value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].fileRepoId}"/>&fileRepoName=${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docName}" title="Download" class="downloadFile">
                              ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docName}
                          </a>
                         <c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docSize}"/></label></span>
                        </wrms:value>

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
<style>
  .compareTdStyle{display:inline;padding:.2em .6em .3em;line-height:1;color:#000;text-align:center;vertical-align:baseline;border-radius:.5em;font-size:100%;background-color:#FF0}
  *{
    word-wrap: break-word
  }
</style>
<script type="text/javascript">
    $(document).ready(function(){
        var svcId = "";

    });


    hightLightChangeVal('newVal', 'oldVal');
    function hightLightChangeVal(newValClass, oldValClass) {
        $('.' + oldValClass).each(function () {
            var oldVal = $(this).attr('attr');
            var newEle = $(this).parent().find('.' + newValClass);
            var newVal = newEle.length > 0 ? newEle.attr('attr') : '';
            if (oldVal.length > 0 && newVal.length > 0) {
                if (oldVal != newVal) {
                    $(this).show();
                } else {
                    $(this).hide();
                }
            }else  if($("#view").val()==""){
                if (oldVal != newVal) {
                    $(this).show();
                    $(this).html("NA");
                } else {
                    $(this).hide();
                }

            }
        });
    }
</script>