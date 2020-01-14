<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<%--<webui:setLayout name="egp-blank"/>--%>
<webui:setLayout name="iais-blank"/>
<%--<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>--%>
<c:set var="appGrpPremisesDtoList" value="${appSubmissionDto.appGrpPremisesDtoList}"></c:set>
<div class="panel-main-content">

  <div class="amended-service-info-gp">
    <h2>LABORATORY DISCIPLINES</h2>
    <c:forEach var="appSvcLaboratoryDisciplinesDto" items="${currentPreviewSvcInfo.appSvcLaboratoryDisciplinesDtoList}" varStatus="status">
        <div class="amend-preview-info">
          <p><span class="preview-title col-xs-6 col-md-4">Premises ${status.index+1}:</span> ${appSvcLaboratoryDisciplinesDto.premiseGetAddress}
            <wrms:value width="7">
              <span class="newVal compareTdStyle" attr="${appSvcLaboratoryDisciplinesDto.premiseGetAddress}" style="display: none" ><label><c:out value="         Premises ${status.index+1}  : ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcLaboratoryDisciplinesDtoList[status.index].premiseGetAddress}"/></label></span>
              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcLaboratoryDisciplinesDtoList[status.index].premiseGetAddress}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcLaboratoryDisciplinesDtoList[status.index].premiseGetAddress}"/></label></span>
            </wrms:value>
          </p>

          <div class="form-check-gp">
            <div class="row">
              <div class="col-xs-12">
                <c:forEach var="checkList" items="${appSvcLaboratoryDisciplinesDto.appSvcChckListDtoList}" varStatus="statuss">
                <div class="form-check active">
                  <p class="form-check-label " aria-label="premise-1-cytology"><span class="check-square "></span><span class="col-xs-6 col-md-4">${checkList.chkName}</span>
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


  <div class="amended-service-info-gp">
    <h2>CLINICAL GOVERNANCE OFFICER</h2>
      <div class="amend-preview-info">
        <c:forEach var="cgo" items="${currentPreviewSvcInfo.appSvcCgoDtoList}" varStatus="status">
        <p>Clinical Governance Officer ${status.index+1}:</p>
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
                <table class="col-xs-8">
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation:
                      </p></p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> <span class="col-xs-6 col-md-4"><iais:code code="${cgo.salutation}" /></span>
                        <span class="col-xs-6 col-md-4">
                             <wrms:value width="7">
                               <span class="newVal compareTdStyle" attr="${cgo.salutation}" style="display: none"><label><iais:code code="${cgo.salutation}"/></label></span>
                               <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].salutation}" style="display: none"><label><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].salutation}"/></label></span>
                             </wrms:value>

                        </span>

                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> <span class="col-xs-6 col-md-4">${cgo.name }</span>
                        <span class="col-xs-6 col-md-4">
                           <span class="col-xs-6 col-md-4">
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
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type:
                      </p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-4">${cgo.idType }</span>
                        <span class="col-xs-6 col-md-4">
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${cgo.idType }" style="display: none"><label><c:out value="${cgo.idType }"/></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idType}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idType}"/></label></span>
                            </wrms:value>
                        </span>
                      </p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-4">${cgo.idNo }</span>
                       <span class="col-xs-6 col-md-4">

                         <wrms:value width="7">
                           <span class="newVal compareTdStyle" attr="${cgo.idNo }"  style="display: none"><label><c:out value="${cgo.idNo }"/></label></span>
                           <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo}"/></label></span>
                         </wrms:value>

                       </span>

                      </p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-4"><iais:code code="${cgo.designation}"/></span>
                        <span  class="col-xs-6 col-md-4">

                        <wrms:value width="7">
                          <span class="newVal compareTdStyle" attr="${cgo.designation}" style="display: none"><label><c:out value="${cgo.designation}"/></label></span>
                          <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].designation}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].designation}"/></label></span>
                        </wrms:value>


                        </span>

                      </p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Regn Type:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> <span class="col-xs-6 col-md-4">${cgo.professionType  }</span>
                        <span  class="col-xs-6 col-md-4">

                        <wrms:value width="7">
                          <span class="newVal compareTdStyle" attr="${cgo.professionType  }" style="display: none"><label><c:out value="${cgo.professionType  }"/></label></span>
                          <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].professionType}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].professionType}"/></label></span>
                        </wrms:value>
                        </span>

                      </p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Regn No.:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> <span class="col-xs-6 col-md-4">${cgo.professionRegoNo }</span>
                        <span  class="col-xs-6 col-md-4">
                              <wrms:value width="7">
                                <span class="newVal compareTdStyle" attr="${cgo.professionRegoNo }" style="display: none"><label><c:out value="${cgo.professionRegoNo }"/></label></span>
                                <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].professionRegoNo}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].professionRegoNo}"/></label></span>
                              </wrms:value>

                        </span>

                    </p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Specialty:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-4"> ${cgo.speciality }</span>
                        <span  class="col-xs-6 col-md-4">
                              <wrms:value width="7">
                                <span class="newVal compareTdStyle" attr=" ${cgo.speciality }" style="display: none"><label><c:out value=" ${cgo.speciality }"/></label></span>
                                <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}"/></label></span>
                              </wrms:value>

                        </span>


                      </p>
                    </td>
                  </tr>
                  <c:if test="${'other' == cgo.speciality}">
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                    </td>
                    <td>
                      <p><c:out value="${cgo.specialityOther}"/></p>
                    </td>
                  </tr>
                  </c:if>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Subspeciality or relevant qualification:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> </p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No.:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> <span class="col-xs-6 col-md-4">${cgo.mobileNo}</span>

                        <span class="col-xs-6 col-md-4">
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${cgo.mobileNo}"  style="display: none"><label><c:out value="${cgo.mobileNo}"/></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].mobileNo}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].mobileNo}"/></label></span>
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

  <div class="amended-service-info-gp">
    <h2>DISCIPLINE ALLOCATION</h2>
    <div class="amend-preview-info">
      <p></p>
      <div class="form-check-gp">
        <div class="row">
          <div class="col-xs-12">
                <table class="table discipline-table">
                  <thead>
                  <tr>
                    <th>Premises</th>
                    <th>Laboratory Disciplines</th>
                    <th>Clinical Governance Officers</th>
                  </tr>
                  </thead>
                  <c:forEach var="appGrpPrem" items="${appGrpPremisesDtoList}" varStatus="status">
                    <c:if test="${appGrpPrem.hciName != '' && appGrpPrem.hciName!= null}">
                      <c:set var="reloadMapValue" value="${appGrpPrem.hciName}"/>
                    </c:if>
                    <c:if test="${appGrpPrem.conveyanceVehicleNo != '' && appGrpPrem.conveyanceVehicleNo!= null}">
                      <c:set var="reloadMapValue" value="${appGrpPrem.conveyanceVehicleNo}"/>
                    </c:if>
                    <tbody>
                    <c:forEach var="disciplineAllocation" items="${reloadDisciplineAllocationMap[reloadMapValue]}" varStatus="stat">
                      ${stat.end}
                      <tr>
                        <c:if test="${stat.first}">
                          <td rowspan="${reloadDisciplineAllocationMap[reloadMapValue].size()}">
                            <p class="visible-xs visible-sm table-row-title">${appGrpPrem.address}</p>
                          </td>
                        </c:if>
                        <td>
                          <p>${disciplineAllocation.chkLstName}</p>
                        </td>
                        <td>
                          <p>${disciplineAllocation.cgoSelName}</p>
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

  <div class="amended-service-info-gp">
    <h2>PRINCIPAL OFFICERS</h2>
      <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <c:forEach items="${currentPreviewSvcInfo.appSvcPrincipalOfficersDtoList}" var="po" varStatus="status">
                <table class="col-xs-8">
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-4"><iais:code code="${po.salutation}"/></span>
                       <span class="col-xs-6 col-md-4">
                          <wrms:value width="7">
                            <span class="newVal compareTdStyle" attr="${po.salutation}" style="display: none"><label><iais:code code="${po.salutation}"/></label></span>
                            <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].salutation}" style="display: none"><label><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].salutation}"/></label></span>
                          </wrms:value>

                       </span>

                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-4">${po.name}</span>

                        <span  class="col-xs-6 col-md-4">

                           <wrms:value width="7">
                             <span class="newVal compareTdStyle" attr="${po.name}" style="display: none"><label><c:out value="${po.name}"/></label></span>
                             <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].name}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].name}"/></label></span>
                           </wrms:value>

                        </span>

                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-4">${po.idType}</span>
                        <span class="col-xs-6 col-md-4">
                           <wrms:value width="7">
                             <span class="newVal compareTdStyle" attr="${po.idType}"  style="display: none"><label><c:out value="${po.idType}"/></label></span>
                             <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idType}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idType}"/></label></span>
                           </wrms:value>
                        </span>

                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-4">${po.idNo}</span>
                        <span  class="col-xs-6 col-md-4">
                              <wrms:value width="7">
                                <span class="newVal compareTdStyle" attr="${po.idNo}"  style="display: none"><label><c:out value="${po.idNo}"/></label></span>
                                <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo}"/></label></span>
                              </wrms:value>

                        </span>


                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-4"><iais:code code="${po.designation}"/></span>
                        <span  class="col-xs-6 col-md-4">
                          <wrms:value width="7">
                            <span class="newVal compareTdStyle" attr="${po.designation}" style="display: none"><label><iais:code code="${po.designation}"/></label></span>
                            <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].designation}" style="display: none"><label><iais:code code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].designation}"/></label></span>
                          </wrms:value>

                        </span>

                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Office Telephone:


                      </p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-4">${po.officeTelNo}</span>
                        <span  class="col-xs-6 col-md-4">
                            <wrms:value width="7">
                              <span class="newVal compareTdStyle" attr="${po.officeTelNo}" style="display: none"><label><c:out value="${po.officeTelNo}"/></label></span>
                              <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].officeTelNo}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].officeTelNo}"/></label></span>
                            </wrms:value>

                        </span>


                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>MobileNo:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><span class="col-xs-6 col-md-4">${po.mobileNo}</span>
                          <span class="col-xs-6 col-md-4">
                              <wrms:value width="7">
                                <span class="newVal compareTdStyle" attr="${po.mobileNo}" style="display: none"><label><c:out value="${po.mobileNo}"/></label></span>
                                <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].mobileNo}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].mobileNo}"/></label></span>
                              </wrms:value>

                          </span>


                      </p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>EmailAddress:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></span><span class="col-xs-6 col-md-4">${po.emailAddr}</span>
                        <span class="col-xs-6 col-md-4">
                          <wrms:value width="7">
                            <span class="newVal compareTdStyle" attr="${po.emailAddr}" style="display: none"><label><c:out value="${po.emailAddr}"/></label></span>
                            <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].emailAddr}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].emailAddr}"/></label></span>
                          </wrms:value>
                        </span>
                      </p>
                    </td>
                  </tr>
                </table>
                <div class="row">
                  <div class="col-xs-8">
                   <hr>
                  </div>
                </div>
              </c:forEach>
            </div>
          </div>
        </div>
      </div>
  </div>


  <div class="amended-service-info-gp">
    <h2>SERVICE SPECIFIC DOCUMENTS</h2>
      <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <table class="col-xs-12">
              <c:forEach var="svcDoc" items="${currentPreviewSvcInfo.appSvcDocDtoLit}" varStatus="status">
                <tr>
                  <td>
                  <div class="field col-sm-4 control-label formtext"><label>Docment1 for Premise1:</label></div>
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
                     <span class="filename server-site" id="130"><a href="${pageContext.request.contextPath}
                     /file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"
                     value="${svcDoc.fileRepoId}"/>&fileRepoName=${svcDoc.docName}" title="Download" class="downloadFile">${svcDoc.docName}</a>

                       (${svcDoc.docSize} KB)</span>

                     <wrms:value width="7">
                       <span class="newVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docSize}+${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docName}"><label>
                        <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"  value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].fileRepoId}"/>&fileRepoName=${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docName}" title="Download" class="downloadFile">
                            ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docName}
                        </a> <c:out value="(${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docSize})KB"/>
                       </label></span>
                       <span class="oldVal compareTdStyle" attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docSize}+${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docName}" style="display: none"><label><c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docSize}"/></label></span>
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

</div>
<style>
  .compareTdStyle{display:inline;padding:.2em .6em .3em;line-height:1;color:#000;text-align:center;vertical-align:baseline;border-radius:.5em;font-size:100%;background-color:#FF0}
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
            }else {
                if (oldVal != newVal) {
                    $(this).show();
                   /* $(this).val("NA");*/
                } else {
                    $(this).hide();
                }

            }
        });
    }
</script>