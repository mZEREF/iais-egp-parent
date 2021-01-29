<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
</style>
<div class="panel-main-content">
  <input style="display: none" value="${NOT_VIEW}" id="view">
  <input type="hidden" id="oldAppSubmissionDto" value="${appSubmissionDto.oldAppSubmissionDto==null}">
  <c:set var="appGrpPremisesDtoList" value="${appSubmissionDto.appGrpPremisesDtoList}"></c:set>
  <c:set var="oldAppGrpPremisesDtoList" value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList}"></c:set>
  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST007')}">

  </c:if>

  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST001')}">
    <div class="amended-service-info-gp">
      <label style="font-size: 2.2rem">${stepNameMap['SVST001']}</label>
      <c:forEach var="appSvcLaboratoryDisciplinesDto"
                 items="${currentPreviewSvcInfo.appSvcLaboratoryDisciplinesDtoList}" varStatus="status">
        <div class="amend-preview-info">
          <p><span class="preview-title col-xs-2 col-md-2" style="padding-right: 0%">Premises ${status.index+1}</span>
          <div class="col-xs-5">
            <span class="newVal " attr="${appGrpPremisesDtoList[status.index].address}"><c:out
                    value="${appGrpPremisesDtoList[status.index].address}"/></span>

          </div>
          <div class="col-xs-5">
            <span class="oldVal " attr="${oldAppGrpPremisesDtoList[status.index].address}" style="display: none"><c:out
                    value="${oldAppGrpPremisesDtoList[status.index].address}"/></span>

          </div>

          </p>

          <div class="form-check-gp">
            <div class="row">
              <div class="col-xs-12" style="margin-left: 2%">
                <c:forEach var="checkList" items="${appSvcLaboratoryDisciplinesDto.appSvcChckListDtoList}"
                           varStatus="statuss">
                  <div class="form-check ">
                    <p class="form-check-label " aria-label="premise-1-cytology">
                    <div class="col-xs-6 col-md-6">
                           <span class="newVal " style="margin-left: 3%" attr="${checkList.chkName}${checkList.check}">
                             <input style="cursor: default" class="form-check-input"
                                    <c:if test="${checkList.check}">checked</c:if> type="checkbox" disabled>
                              <label class="form-check-label"><span
                                      class="check-square"></span>${checkList.chkName}</label>
                           </span>
                    </div>
                    <div class="col-xs-6 col-md-6">
                            <span class="oldVal "
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcLaboratoryDisciplinesDtoList[status.index].appSvcChckListDtoList[statuss.index].chkName}${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcLaboratoryDisciplinesDtoList[status.index].appSvcChckListDtoList[statuss.index].check}"
                                  style="display: none">
                              <input style="cursor: default" class="form-check-input"
                                     <c:if test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcLaboratoryDisciplinesDtoList[status.index].appSvcChckListDtoList[statuss.index].check}">checked</c:if>
                                     type="checkbox" disabled>
                              <label class="form-check-label"><span
                                      class="check-square"></span>${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcLaboratoryDisciplinesDtoList[status.index].appSvcChckListDtoList[statuss.index].chkName}</label>
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
  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST002')}">
    <div class="amended-service-info-gp">
      <label style="font-size: 2.2rem">${stepNameMap['SVST002']}</label>
      <div class="amend-preview-info">
        <c:forEach var="cgo" items="${currentPreviewSvcInfo.appSvcCgoDtoList}" varStatus="status">
        <p><strong class="col-xs-6">Clinical Governance Officer <c:if
                test="${fn:length(currentPreviewSvcInfo.appSvcCgoDtoList)>1}">${status.index+1}</c:if>:</strong><span
                class="col-xs-4 col-md-4"></span>
        </p>
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <table class="col-xs-12">
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
                    <div class="col-xs-6">
                      <span class="newVal " attr="${cgo.name}"><iais:code code="${cgo.name}"/></span>

                    </div>
                    <div class="col-xs-6">
                      <span class="oldVal "
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].name}"
                            style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].name}</span>
                    </div>

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
                      <span class="newVal " attr="${cgo.idType}"><c:out value="${cgo.idType}"/></span>

                    </div>
                    <div class="col-xs-6">
                      <span class=" oldVal"
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idType}"
                            style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idType}</span>

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
                        <span class="newVal " attr="${cgo.idNo}">
                         <c:out value="${cgo.idNo}"/>
                         <c:if test="${empty hashMap[cgo.idNo]}">
                            <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                          </c:if>
                          <c:if test="${not empty hashMap[cgo.idNo]}">
                            <img src="/hcsa-licence-web/img/2020109171436.png" width="25"
                               onclick="showThisTableNewService(this)" height="25" alt="NETS">
                          </c:if>
                      </span>
                      </div>

                    <div class="col-xs-6 img-show">
                      <span class="oldVal"
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo}"
                            style="display: none">
                          ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo}
                        <c:if test="${empty hashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo]}">
                          <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                        </c:if>
                         <c:if test="${not empty hashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo]}">
                           <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableOldService(this)"
                                width="25" height="25" alt="NETS">
                         </c:if>
                      </span>
                    </div>

                    <c:if test="${not empty hashMap[cgo.idNo]}">
                      <div class="row new-img-show" style="display: none">
                        <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left: -50%;width: 140%;margin-top: 5%">
                          <label style="font-weight: normal">The Professional has existing disciplinary records in
                            HERIMS</label><span style="position: absolute;right: 0px;color: black"
                                                onclick="closeThis(this)">X</span>
                          <table border="1px"
                                 style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                            <tr>
                              <td>Indentification No.</td>
                              <td>Case No.</td>
                              <td>Case Type Description</td>
                              <td>Case Status Description</td>
                              <td>Offence Description</td>
                              <td>Outcome Description</td>
                              <td>Outcome Issue Date</td>
                              <td>Prosecution Outcome Description</td>
                              <td>Created Date</td>
                              <td>Update Date</td>
                            </tr>
                            <c:forEach items="${hashMap[cgo.idNo]}" var="map">
                              <tr>
                                <td>${map.identificationNo}</td>
                                <td>${map.caseNo}</td>
                                <td>${map.caseType}</td>
                                <td>${map.caseStatus}</td>
                                <td>${map.offenceDesc}</td>
                                <td>${map.outcome}</td>
                                <td><fmt:formatDate value="${map.issueDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                <td>${map.prosecutionOutcome}</td>
                                <td><fmt:formatDate value="${map.createdDate}"
                                                    pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                <td><fmt:formatDate value="${map.updatedDate}"
                                                    pattern="dd/MM/yyyy"></fmt:formatDate></td>
                              </tr>
                            </c:forEach>
                            <tr></tr>

                          </table>
                        </div>
                      </div>
                    </c:if>
                    <c:if test="${not empty hashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo]}">
                      <div class="row old-img-show" style="display: none">
                        <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left: -50%;width: 140%;margin-top: 5%">
                          <label style="font-weight: normal">The Professional has existing disciplinary records in
                            HERIMS</label><span style="position: absolute;right: 0px;color: black"
                                                onclick="closeThis(this)">X</span>
                          <table border="1px"
                                 style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center;">
                            <tr>
                              <td>Indentification No.</td>
                              <td>Case No.</td>
                              <td>Case Type Description</td>
                              <td>Case Status Description</td>
                              <td>Offence Description</td>
                              <td>Outcome Description</td>
                              <td>Outcome Issue Date</td>
                              <td>Prosecution Outcome Description</td>
                              <td>Created Date</td>
                              <td>Update Date</td>
                            </tr>
                            <c:forEach
                                    items="${hashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo]}"
                                    var="map">
                              <tr>
                                <td>${map.identificationNo}</td>
                                <td>${map.caseNo}</td>
                                <td>${map.caseType}</td>
                                <td>${map.caseStatus}</td>
                                <td>${map.offenceDesc}</td>
                                <td>${map.outcome}</td>
                                <td><fmt:formatDate value="${map.issueDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                <td>${map.prosecutionOutcome}</td>
                                <td><fmt:formatDate value="${map.createdDate}"
                                                    pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                <td><fmt:formatDate value="${map.updatedDate}"
                                                    pattern="dd/MM/yyyy"></fmt:formatDate></td>
                              </tr>
                            </c:forEach>
                          </table>
                        </div>
                      </div>
                    </c:if>
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
                      <span class="newVal " attr="${cgo.profRegNo}"><c:out value="${cgo.profRegNo}"/>
                        <c:if test="${empty listHashMap[cgo.profRegNo]}">
                          <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                        </c:if>
                        <c:if test="${not empty listHashMap[cgo.profRegNo]}">
                          <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNewService(this)"
                               width="25" height="25" alt="NETS">
                        </c:if>
                      </span>
                    </div>
                    <div class="col-xs-6 img-show">
                      <span class="oldVal "
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo}"
                            style="display: none">
                        <iais:code
                                code="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo}"/>
                       <c:if test="${empty listHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo]}">
                         <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                       </c:if>
                        <c:if test="${not empty listHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo]}">
                          <img src="/hcsa-licence-web/img/2020109171436.png" width="25"
                               onclick="showThisTableOldService(this)" height="25" alt="NETS">
                        </c:if>
                      </span>
                    </div>
                    <c:if test="${not empty listHashMap[cgo.profRegNo]}">
                      <div class="row new-img-show" style="display: none">
                        <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left: -50%;width: 140%;margin-top: 5%">
                          <label style="font-weight: normal">The Professional has existing disciplinary records in
                            PRS</label><span style="position: absolute;right: 0px;color: black"
                                             onclick="closeThis(this)">X</span>
                          <table border="1px"
                                 style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                            <tr>
                              <td>Professional Regn. No.</td>
                              <td>Complaints</td>
                              <td>Final Outcome</td>
                              <td>Fine Amounts</td>
                              <td>Restrictive Practice Start Date</td>
                              <td>Restrictive Practice End Date</td>
                              <td>Suspension Start Date</td>
                              <td>Suspension End Date</td>
                              <td>Other disciplinary action Start Date</td>
                              <td>Other Disciplinary action End Date</td>
                            </tr>
                            <c:forEach items="${listHashMap[cgo.profRegNo]}" var="list">
                              <tr>
                                <td>${cgo.profRegNo}</td>
                                <td style="text-align: left">
                                  <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint1}</
                                  <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint2}</p>
                                  <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint3}</p
                                </td>
                                <td>${list.finaloutcome}</td>
                                <td>${list.fineamount}</td>
                                <td>${list.restrictstartdate}</td>
                                <td>${list.restrictenddate}</td>
                                <td>${list.suspendstartdate}</td>
                                <td>${list.suspendenddate}</td>
                                <td>${list.otherstartdate}</td>
                                <td>${list.otherenddate}</td>
                              </tr>
                            </c:forEach>
                          </table>
                        </div>
                      </div>
                    </c:if>
                    <c:if test="${not empty listHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo]}">
                      <div class="row old-img-show" style="display: none">
                        <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left: -50%;width: 140%;margin-top: 5%">
                          <label style="font-weight: normal">The Professional has existing disciplinary records in
                            PRS</label><span style="position: absolute;right: 0px;color: black"
                                             onclick="closeThis(this)">X</span>
                          <table border="1px"
                                 style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                            <tr>
                              <td>Professional Regn. No.</td>
                              <td width="10%">Complaints</td>
                              <td>Final Outcome</td>
                              <td>Fine Amounts</td>
                              <td>Restrictive Practice Start Date</td>
                              <td>Restrictive Practice End Date</td>
                              <td>Suspension Start Date</td>
                              <td>Suspension End Date</td>
                              <td>Other disciplinary action Start Date</td>
                              <td>Other Disciplinary action End Date</td>
                            </tr>
                            <c:forEach
                                    items="${listHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo]}"
                                    var="list">
                              <tr>
                                <td>${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo}</td>
                                <td style="text-align: left">
                                  <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint1}</p>
                                  <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint2}</p>
                                  <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint3}</p>
                                </td>
                                <td>${list.finaloutcome}</td>
                                <td>${list.fineamount}</td>
                                <td>${list.restrictstartdate}</td>
                                <td>${list.restrictenddate}</td>
                                <td>${list.suspendstartdate}</td>
                                <td>${list.suspendenddate}</td>
                                <td>${list.otherstartdate}</td>
                                <td>${list.otherenddate}</td>
                              </tr>
                            </c:forEach>
                          </table>
                        </div>
                      </div>
                    </c:if>
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
                      <span class="oldVal "
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}"
                            style="display: none">
                        <c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}"/>
                      </span>
                    </div>
                    </p>
                  </td>
                </tr>

                <c:choose>
                  <c:when test="${'other' == cgo.speciality && 'other' == currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}">
                    <tr>
                      <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                      </td>
                      <td>
                        <div class="col-xs-6">
                        <span class="newVal " attr="${cgo.specialityOther}"><c:out
                                value="${cgo.specialityOther}"/></span>
                        </div>
                        <c:if test="${'other' == currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}">
                          <div class="col-xs-6">
                          <span class="oldVal " style="display: none"
                                attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].specialityOther}"><c:out
                                  value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].specialityOther}"/></span>
                          </div>
                        </c:if>
                        <c:if test="${'other' != currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}">
                          <div class="col-xs-6">
                            <span class="oldVal" attr="" style="display: none"></span>
                          </div>
                        </c:if>
                      </td>
                    </tr>
                  </c:when>
                  <c:when test="${'other' != cgo.speciality && 'other' == currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}">
                    <tr>
                      <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                      </td>
                      <td>
                        <div class="col-xs-6">
                        <span class="newVal " attr="${cgo.specialityOther}"><c:out
                                value="${cgo.specialityOther}"/></span>
                        </div>
                        <c:if test="${'other' == currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}">
                          <div class="col-xs-6">
                          <span class="oldVal " style="display: none"
                                attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].specialityOther}"><c:out
                                  value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].specialityOther}"/></span>
                          </div>
                        </c:if>
                        <c:if test="${'other' != currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}">
                          <div class="col-xs-6">
                            <span class="oldVal" attr="" style="display: none"></span>
                          </div>
                        </c:if>
                      </td>
                    </tr>
                  </c:when>
                  <c:when test="${'other' == cgo.speciality && 'other' != currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}">
                    <tr>
                      <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                      </td>
                      <td>
                        <div class="col-xs-6">
                        <span class="newVal " attr="${cgo.specialityOther}"><c:out
                                value="${cgo.specialityOther}"/></span>
                        </div>
                        <c:if test="${'other' == currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}">
                          <div class="col-xs-6">
                          <span class="oldVal " style="display: none"
                                attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].specialityOther}"><c:out
                                  value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].specialityOther}"/></span>
                          </div>
                        </c:if>
                        <c:if test="${'other' != currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].speciality}">
                          <div class="col-xs-6">
                            <span class="oldVal" attr="" style="display: none"></span>
                          </div>
                        </c:if>
                      </td>
                    </tr>
                  </c:when>
                </c:choose>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Subspecialty
                      or relevant qualification</p>
                  </td>
                  <td>
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                    <div class="col-xs-6">
                      <span class="newVal " attr="${cgo.subSpeciality}"><c:out value="${cgo.subSpeciality}"/></span>
                    </div>
                    <div class="col-xs-6">
                      <span class="oldVal "
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].subSpeciality}"
                            style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].subSpeciality}</span>
                    </div>
                    </p>
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
                    <div class="col-xs-6">
                      <span class="newVal " attr="${cgo.emailAddr}"><c:out value="${cgo.emailAddr}"/></span>
                    </div>
                    <div class="col-xs-6">
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

  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList,'SVST003')}">
    <div class="amended-service-info-gp">
      <label style="font-size: 2.2rem">${stepNameMap['SVST003']}</label>
      <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12">
              <table class="table discipline-table" border="1px">
                <thead>
                <tr>
                  <th style="text-align: center">Premises</th>
                  <th style="text-align: center">${stepNameMap['SVST001']}</th>
                  <th style="text-align: center">Clinical Governance Officers</th>
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
                    <c:set value="${reloadOld[reloadMapValue]}" var="reloaded"></c:set>
                    <tr>
                      <c:if test="${stat.first}">
                        <td style="text-align: center"
                            rowspan="${reloadDisciplineAllocationMap[reloadMapValue].size()}">
                          <div class="col-xs-6">
                            <span class="newVal " attr="${appGrpPrem.address}"><c:out
                                    value="${appGrpPrem.address}"/></span>

                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].address}"
                                  style="display: none"><c:out
                                    value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].address}"/></span>
                          </div>

                        </td>
                      </c:if>
                      <td style="text-align: center">
                        <p>
                        <div class="col-xs-6">
                          <span class="newVal " attr="${disciplineAllocation.chkLstName}${disciplineAllocation.check}">
                            <c:out value="${disciplineAllocation.chkLstName}"/>
                          </span>
                        </div>
                        <div class="col-xs-6">
                          <span class="oldVal "
                                attr="${reloadOld[oldReloadMapValue][stat.index].chkLstName}${reloadOld[oldReloadMapValue][stat.index].check}"
                                style="display: none"><c:out
                                  value="${reloadOld[oldReloadMapValue][stat.index].chkLstName}"/></span>
                        </div>
                        </p>

                      </td>
                      <td style="text-align: center">
                        <p>
                        <div class="col-xs-6">
                          <span class="newVal "
                                attr="${disciplineAllocation.cgoSelName}${disciplineAllocation.check}"><c:out
                                  value="${disciplineAllocation.cgoSelName}"/></span>
                        </div>
                        <div class="col-xs-6">
                          <span class="oldVal "
                                attr="${reloadOld[oldReloadMapValue][stat.index].cgoSelName}${reloadOld[oldReloadMapValue][stat.index].check}"
                                style="display: none"><c:out
                                  value="${reloadOld[oldReloadMapValue][stat.index].cgoSelName}"/></span>
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
              <c:forEach items="${currentPreviewSvcInfo.appSvcPersonnelDtoList}" var="appSvcPersonnelDtoList"
                         varStatus="status">
                <p><strong class="col-xs-6">Service Personnel <c:if
                        test="${fn:length(currentPreviewSvcInfo.appSvcPersonnelDtoList)>1}">${status.index+1}</c:if>:</strong>
                </p>
                <span class="col-xs-6"></span>
                <table class="col-xs-12">
                  <c:choose>
                    <c:when test="${currentPreviewSvcInfo.serviceCode=='BLB'}">
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation
                          </p>
                        </td>
                        <td>
                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.designation}"><c:out
                                    value="${appSvcPersonnelDtoList.designation}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].designation}"
                                  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].designation}</span>
                          </div>

                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name
                          </p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}"><c:out
                                    value="${appSvcPersonnelDtoList.name}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"
                                  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}</span>

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
                            <span class="newVal " attr="${appSvcPersonnelDtoList.profRegNo}"><c:out
                                    value="${appSvcPersonnelDtoList.profRegNo}"/>
                              <c:if test="${empty listHashMap[appSvcPersonnelDtoList.profRegNo]}">
                                <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                              </c:if>
                              <c:if test="${not empty listHashMap[appSvcPersonnelDtoList.profRegNo]}">
                                <img src="/hcsa-licence-web/img/2020109171436.png"
                                     onclick="showThisTableNewService(this)" width="25" height="25" alt="NETS">
                              </c:if>
                            </span>
                          </div>
                          <div class="col-xs-6 img-show">
                            <span class="oldVal "
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo}"
                                  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo}
                              <c:if test="${empty listHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo]}">
                                <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                              </c:if>
                               <c:if test="${not empty listHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo]}">
                                 <img src="/hcsa-licence-web/img/2020109171436.png"
                                      onclick="showThisTableOldService(this)" width="25" height="25" alt="NETS">
                               </c:if>
                            </span>
                          </div>
                          <c:if test="${not empty listHashMap[appSvcPersonnelDtoList.profRegNo]}">
                            <div class="row new-img-show" style="display: none">
                              <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left: -50%;width: 140%;margin-top: 5%">
                                <label style="font-weight: normal">The Professional has existing disciplinary records in
                                  PRS</label><span style="position: absolute;right: 0px;color: black"
                                                   onclick="closeThis(this)">X</span>
                                <table border="1px"
                                       style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                                  <tr>
                                    <td>Professional Regn. No.</td>
                                    <td>Complaints</td>
                                    <td>Final Outcome</td>
                                    <td>Fine Amounts</td>
                                    <td>Restrictive Practice Start Date</td>
                                    <td>Restrictive Practice End Date</td>
                                    <td>Suspension Start Date</td>
                                    <td>Suspension End Date</td>
                                    <td>Other disciplinary action Start Date</td>
                                    <td>Other Disciplinary action End Date</td>
                                  </tr>
                                  <c:forEach items="${listHashMap[appSvcPersonnelDtoList.profRegNo]}" var="list">
                                    <tr>
                                      <td>${appSvcPersonnelDtoList.profRegNo}</td>
                                      <td style="text-align: left">
                                        <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint1}</p>
                                        <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint2}</p>
                                        <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint3}</p>
                                      </td>
                                      <td>${list.finaloutcome}</td>
                                      <td>${list.fineamount}</td>
                                      <td>${list.restrictstartdate}</td>
                                      <td>${list.restrictenddate}</td>
                                      <td>${list.suspendstartdate}</td>
                                      <td>${list.suspendenddate}</td>
                                      <td>${list.otherstartdate}</td>
                                      <td>${list.otherenddate}</td>
                                    </tr>
                                  </c:forEach>
                                </table>
                              </div>
                            </div>
                          </c:if>
                          <c:if test="${not empty listHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo]}">
                            <div class="row old-img-show" style="display: none">
                              <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left: -50%;width: 140%;margin-top: 5%">
                                <label style="font-weight: normal">The Professional has existing disciplinary records in
                                  PRS</label><span style="position: absolute;right: 0px;color: black"
                                                   onclick="closeThis(this)">X</span>
                                <table border="1px"
                                       style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                                  <tr>
                                    <td>Professional Regn. No.</td>
                                    <td>Complaints</td>
                                    <td>Final Outcome</td>
                                    <td>Fine Amounts</td>
                                    <td>Restrictive Practice Start Date</td>
                                    <td>Restrictive Practice End Date</td>
                                    <td>Suspension Start Date</td>
                                    <td>Suspension End Date</td>
                                    <td>Other disciplinary action Start Date</td>
                                    <td>Other Disciplinary action End Date</td>
                                  </tr>
                                  <c:forEach
                                          items="${listHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo]}"
                                          var="list">
                                    <tr>
                                      <td>${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo}</td>
                                      <td style="text-align: left">
                                        <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint1}</p>
                                        <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint2}</p>
                                        <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint3}</p>
                                      </td>
                                      <td>${list.finaloutcome}</td>
                                      <td>${list.fineamount}</td>
                                      <td>${list.restrictstartdate}</td>
                                      <td>${list.restrictenddate}</td>
                                      <td>${list.suspendstartdate}</td>
                                      <td>${list.suspendenddate}</td>
                                      <td>${list.otherstartdate}</td>
                                      <td>${list.otherenddate}</td>
                                    </tr>
                                  </c:forEach>
                                </table>
                              </div>
                            </div>
                          </c:if>

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
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}"
                                  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}</span>
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
                          <div class="col-xs-6 col-md-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}"><c:out
                                    value="${appSvcPersonnelDtoList.name}"/></span>
                          </div>
                          <div class="col-xs-6 col-md-6">
                            <span class="oldVal "
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"
                                  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}</span>
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
                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.qualification}"><c:out
                                    value="${appSvcPersonnelDtoList.qualification}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].qualification}"
                                  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].qualification}</span>
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
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}"
                                  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}</span>
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

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}"><c:out
                                    value="${appSvcPersonnelDtoList.name}"/></span>

                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"
                                  style="display: none"><c:out
                                    value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"/></span>
                          </div>

                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation
                          </p>
                        </td>
                        <td>
                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.designation}"><c:out
                                    value="${appSvcPersonnelDtoList.designation}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].designation}"
                                  style="display: none"><c:out
                                    value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].designation}"/></span>
                          </div>

                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification
                          </p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.qualification}"><c:out
                                    value="${appSvcPersonnelDtoList.qualification}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].qualification}"
                                  style="display: none"><c:out
                                    value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].qualification}"/></span>
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
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}"
                                  style="display: none"><c:out
                                    value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}"/></span>
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

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}"><c:out
                                    value="${appSvcPersonnelDtoList.name}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"
                                  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}</span>

                          </div>


                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification
                          </p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.qualification}"><c:out
                                    value="${appSvcPersonnelDtoList.qualification}"/></span>

                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].qualification}"
                                  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].qualification}</span>
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
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}"
                                  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}</span>
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

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}"><c:out
                                    value="${appSvcPersonnelDtoList.name}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal  "
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"
                                  style="display: none"><c:out
                                    value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"/></span>
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

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}"><c:out
                                    value="${appSvcPersonnelDtoList.name}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"
                                  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}</span>

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
                            <span class="newVal " attr="${appSvcPersonnelDtoList.profRegNo}"><c:out
                                    value="${appSvcPersonnelDtoList.profRegNo}"/>
                              <c:if test="${empty listHashMap[appSvcPersonnelDtoList.profRegNo]}">
                                <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                              </c:if>
                              <c:if test="${not empty listHashMap[appSvcPersonnelDtoList.profRegNo]}">
                                <img src="/hcsa-licence-web/img/2020109171436.png"
                                     onclick="showThisTableNewService(this)" width="25" height="25" alt="NETS">
                              </c:if>
                            </span>
                          </div>
                          <div class="col-xs-6 img-show">
                            <span class="oldVal "
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo}"
                                  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo}
                              <c:if test="${empty listHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo]}">
                                <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                              </c:if>
                              <c:if test="${not empty listHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo]}">
                                <img src="/hcsa-licence-web/img/2020109171436.png"
                                     onclick="showThisTableNewService(this)" width="25" height="25" alt="NETS">
                              </c:if>
                            </span>

                          </div>
                          <c:if test="${not empty listHashMap[appSvcPersonnelDtoList.profRegNo]}">
                            <div class="row new-img-show" style="display: none">
                              <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left: -50%;width: 140%;margin-top: 5%">
                                <label style="font-weight: normal">The Professional has existing disciplinary records in
                                  PRS</label><span style="position: absolute;right: 0px;color: black"
                                                   onclick="closeThis(this)">X</span>
                                <table border="1px"
                                       style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                                  <tr>
                                    <td>Professional Regn. No.</td>
                                    <td>Complaints</td>
                                    <td>Final Outcome</td>
                                    <td>Fine Amounts</td>
                                    <td>Restrictive Practice Start Date</td>
                                    <td>Restrictive Practice End Date</td>
                                    <td>Suspension Start Date</td>
                                    <td>Suspension End Date</td>
                                    <td>Other disciplinary action Start Date</td>
                                    <td>Other Disciplinary action End Date</td>
                                  </tr>
                                  <c:forEach items="${listHashMap[appSvcPersonnelDtoList.profRegNo]}" var="list">
                                    <tr>
                                      <td>${appSvcPersonnelDtoList.profRegNo}</td>
                                      <td style="text-align: left">
                                        <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint1}</p>
                                        <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint2}</p>
                                        <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint3}</p>
                                      </td>
                                      <td>${list.finaloutcome}</td>
                                      <td>${list.fineamount}</td>
                                      <td>${list.restrictstartdate}</td>
                                      <td>${list.restrictenddate}</td>
                                      <td>${list.suspendstartdate}</td>
                                      <td>${list.suspendenddate}</td>
                                      <td>${list.otherstartdate}</td>
                                      <td>${list.otherenddate}</td>
                                    </tr>
                                  </c:forEach>
                                </table>
                              </div>
                            </div>
                          </c:if>
                          <c:if test="${not empty listHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo]}">
                            <div class="row old-img-show" style="display: none">
                              <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left: -50%;width: 140%;margin-top: 5%">
                                <label style="font-weight: normal">The Professional has existing disciplinary records in
                                  PRS</label><span style="position: absolute;right: 0px;color: black"
                                                   onclick="closeThis(this)">X</span>
                                <table border="1px"
                                       style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                                  <tr>
                                    <td>Professional Regn. No.</td>
                                    <td>Complaints</td>
                                    <td>Final Outcome</td>
                                    <td>Fine Amounts</td>
                                    <td>Restrictive Practice Start Date</td>
                                    <td>Restrictive Practice End Date</td>
                                    <td>Suspension Start Date</td>
                                    <td>Suspension End Date</td>
                                    <td>Other disciplinary action Start Date</td>
                                    <td>Other Disciplinary action End Date</td>
                                  </tr>
                                  <c:forEach
                                          items="${listHashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo]}"
                                          var="list">
                                    <tr>
                                      <td>${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].profRegNo}</td>
                                      <td style="text-align: left">
                                        <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint1}</p>
                                        <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint2}</p>
                                        <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint3}</p>
                                      </td>
                                      <td>${list.finaloutcome}</td>
                                      <td>${list.fineamount}</td>
                                      <td>${list.restrictstartdate}</td>
                                      <td>${list.restrictenddate}</td>
                                      <td>${list.suspendstartdate}</td>
                                      <td>${list.suspendenddate}</td>
                                      <td>${list.otherstartdate}</td>
                                      <td>${list.otherenddate}</td>
                                    </tr>
                                  </c:forEach>
                                </table>
                              </div>
                            </div>
                          </c:if>

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
                            <span class="newVal " attr="${appSvcPersonnelDtoList.name}"><c:out
                                    value="${appSvcPersonnelDtoList.name}"/></span>
                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}"
                                  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].name}</span>

                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td class="col-xs-6">
                          <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification
                          </p>
                        </td>
                        <td>

                          <div class="col-xs-6">
                            <span class="newVal " attr="${appSvcPersonnelDtoList.qualification}"><c:out
                                    value="${appSvcPersonnelDtoList.qualification}"/></span>

                          </div>
                          <div class="col-xs-6">
                            <span class="oldVal "
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].qualification}"
                                  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].qualification}</span>
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
                                  attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}"
                                  style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPersonnelDtoList[status.index].wrkExpYear}</span>
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
      <label style="font-size: 2.2rem">${stepNameMap['SVST004']}</label>
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
                  <p><strong class="col-xs-6">Deputy Principal Officer <c:if
                          test="${DPO_SIZE>1}">${dpoIndex}</c:if>:</strong></p>
                  <c:set var="dpoIndex" value="${dpoIndex+1}"></c:set>
                </c:if>
                <table class="col-xs-12">
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
                      <div class="col-xs-6">
                        <span class="newVal " attr="${po.name}"><c:out value="${po.name}"/></span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].name}"
                              style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].name}</span>
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
                        <span class="newVal " attr="${po.idType}"><c:out value="${po.idType}"/></span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idType}"
                              style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idType}</span>
                      </div>

                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-6">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID
                        No.</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                      <div class="col-xs-6 img-show">
                        <span class="newVal " attr="${po.idNo}"><c:out value="${po.idNo}"/>
                          <c:if test="${empty hashMap[po.idNo]}">
                            <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS"/>
                          </c:if>
                          <c:if test="${not empty hashMap[po.idNo]}">
                            <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNewService(this)"
                                 width="25" height="25" alt="NETS">
                          </c:if>
                          </span>
                      </div>
                      <div class="col-xs-6 img-show">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo}"
                              style="display: none">
                           <c:out value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo}"/>
                          <c:if test="${empty hashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo]}">
                            <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS"/>
                          </c:if>
                          <c:if test="${not empty hashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo]}">
                            <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableOldService(this)"
                                 width="25" height="25" alt="NETS"/>
                          </c:if>
                        </span>
                      </div>
                      <c:if test="${not empty hashMap[po.idNo]}">
                        <div class="row new-img-show" style="display: none">
                          <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left: -50%;width: 140%;margin-top: 5%">
                            <label style="font-weight: normal">The Professional has existing disciplinary records in
                              HERIMS</label><span style="position: absolute;right: 0px;color: black"
                                                  onclick="closeThis(this)">X</span>
                            <table border="1px"
                                   style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                              <tr>
                                <td>Indentification No.</td>
                                <td>Case No.</td>
                                <td>Case Type Description</td>
                                <td>Case Status Description</td>
                                <td>Offence Description</td>
                                <td>Outcome Description</td>
                                <td>Outcome Issue Date</td>
                                <td>Prosecution Outcome Description</td>
                                <td>Created Date</td>
                                <td>Update Date</td>
                              </tr>
                              <c:forEach items="${hashMap[po.idNo]}" var="map">
                                <tr>
                                  <td>${map.identificationNo}</td>
                                  <td>${map.caseNo}</td>
                                  <td>${map.caseType}</td>
                                  <td>${map.caseStatus}</td>
                                  <td>${map.offenceDesc}</td>
                                  <td>${map.outcome}</td>
                                  <td><fmt:formatDate value="${map.issueDate}"
                                                      pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                  <td>${map.prosecutionOutcome}</td>
                                  <td><fmt:formatDate value="${map.createdDate}"
                                                      pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                  <td><fmt:formatDate value="${map.updatedDate}"
                                                      pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                </tr>
                              </c:forEach>
                              <tr></tr>

                            </table>
                          </div>
                        </div>
                      </c:if>

                      <c:if test="${not empty hashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo]}">
                        <div class="row old-img-show" style="display: none">
                          <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left: -50%;width: 140%;margin-top: 5%">
                            <label style="font-weight: normal">The Professional has existing disciplinary records in
                              HERIMS</label><span style="position: absolute;right: 0px;color: black"
                                                  onclick="closeThis(this)">X</span>
                            <table border="1px"
                                   style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                              <tr>
                                <td>Indentification No.</td>
                                <td>Case No.</td>
                                <td>Case Type Description</td>
                                <td>Case Status Description</td>
                                <td>Offence Description</td>
                                <td>Outcome Description</td>
                                <td>Outcome Issue Date</td>
                                <td>Prosecution Outcome Description</td>
                                <td>Created Date</td>
                                <td>Update Date</td>
                              </tr>
                              <c:forEach
                                      items="${hashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo]}"
                                      var="map">
                                <tr>
                                  <td>${map.identificationNo}</td>
                                  <td>${map.caseNo}</td>
                                  <td>${map.caseType}</td>
                                  <td>${map.caseStatus}</td>
                                  <td>${map.offenceDesc}</td>
                                  <td>${map.outcome}</td>
                                  <td><fmt:formatDate value="${map.issueDate}"
                                                      pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                  <td>${map.prosecutionOutcome}</td>
                                  <td><fmt:formatDate value="${map.createdDate}"
                                                      pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                  <td><fmt:formatDate value="${map.updatedDate}"
                                                      pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                </tr>
                              </c:forEach>
                              <tr></tr>

                            </table>
                          </div>
                        </div>
                      </c:if>

                      </p>
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
                      <div class="col-xs-6">
                        <span class="newVal " attr="${po.emailAddr}"><c:out value="${po.emailAddr}"/></span>
                      </div>
                      <div class="col-xs-6">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].emailAddr}"
                              style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].emailAddr}</span>
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
  <c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST007')}">
    <div class="amended-service-info-gp">
    <label style="font-size: 2.2rem">${stepNameMap['SVST007']}</label>
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
              <table class="col-xs-12">

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
                    <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcMedAlertPerson.name}"><c:out
                                value="${appSvcMedAlertPerson.name}"/></span>
                    </div>
                    <div class="col-xs-6">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].name}"
                              style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].name}</span>
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
                        <span class="newVal " attr="${appSvcMedAlertPerson.idType}"><c:out
                                value="${appSvcMedAlertPerson.idType}"/></span>

                    </div>
                    <div class="col-xs-6">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idType}"
                              style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idType}</span>
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
                        <span class="newVal " attr="${appSvcMedAlertPerson.idNo}"><c:out
                                value="${appSvcMedAlertPerson.idNo}"/>
                          <c:if test="${empty hashMap[appSvcMedAlertPerson.idNo]}">
                            <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                          </c:if>
                          <c:if test="${not empty hashMap[appSvcMedAlertPerson.idNo]}">
                            <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNewService(this)"
                                 width="25" height="25" alt="NETS">
                          </c:if>
                        </span>
                    </div>
                    <div class="col-xs-6 img-show">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idNo}"
                              style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idNo}
                            <c:if test="${empty hashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idNo]}">
                              <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                            </c:if>
                           <c:if test="${not empty hashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idNo]}">
                             <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableOldService(this)"
                                  width="25" height="25" alt="NETS">
                           </c:if>
                        </span>
                    </div>
                    <c:if test="${not empty hashMap[appSvcMedAlertPerson.idNo]}">
                      <div class="row new-img-show" style="display: none">
                        <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left: -50%;width: 140%;margin-top: 5%">
                          <label style="font-weight: normal">The Professional has existing disciplinary records in
                            HERIMS</label><span style="position: absolute;right: 0px;color: black"
                                                onclick="closeThis(this)">X</span>
                          <table border="1px"
                                 style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                            <tr>
                              <td>Indentification No.</td>
                              <td>Case No.</td>
                              <td>Case Type Description</td>
                              <td>Case Status Description</td>
                              <td>Offence Description</td>
                              <td>Outcome Description</td>
                              <td>Outcome Issue Date</td>
                              <td>Prosecution Outcome Description</td>
                              <td>Created Date</td>
                              <td>Update Date</td>
                            </tr>
                            <c:forEach items="${hashMap[appSvcMedAlertPerson.idNo]}" var="map">
                              <tr>
                                <td>${map.identificationNo}</td>
                                <td>${map.caseNo}</td>
                                <td>${map.caseType}</td>
                                <td>${map.caseStatus}</td>
                                <td>${map.offenceDesc}</td>
                                <td>${map.outcome}</td>
                                <td><fmt:formatDate value="${map.issueDate}"
                                                    pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                <td>${map.prosecutionOutcome}</td>
                                <td><fmt:formatDate value="${map.createdDate}"
                                                    pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                <td><fmt:formatDate value="${map.updatedDate}"
                                                    pattern="dd/MM/yyyy"></fmt:formatDate></td>
                              </tr>
                            </c:forEach>

                          </table>
                        </div>
                      </div>
                    </c:if>
                    <c:if test="${not empty hashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idNo]}">
                      <div class="row old-img-show" style="display: none">
                        <div style="position: absolute;z-index: 100;background-color: #F5F5F5;margin-left: -50%;width: 140%;margin-top: 5%">
                          <label style="font-weight: normal">The Professional has existing disciplinary records in
                            HERIMS</label><span style="position: absolute;right: 0px;color: black"
                                                onclick="closeThis(this)">X</span>
                          <table border="1px"
                                 style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                            <tr>
                              <td>Indentification No.</td>
                              <td>Case No.</td>
                              <td>Case Type Description</td>
                              <td>Case Status Description</td>
                              <td>Offence Description</td>
                              <td>Outcome Description</td>
                              <td>Outcome Issue Date</td>
                              <td>Prosecution Outcome Description</td>
                              <td>Created Date</td>
                              <td>Update Date</td>
                            </tr>
                            <c:forEach
                                    items="${hashMap[currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idNo]}"
                                    var="map">
                              <tr>
                                <td>${map.identificationNo}</td>
                                <td>${map.caseNo}</td>
                                <td>${map.caseType}</td>
                                <td>${map.caseStatus}</td>
                                <td>${map.offenceDesc}</td>
                                <td>${map.outcome}</td>
                                <td><fmt:formatDate value="${map.issueDate}"
                                                    pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                <td>${map.prosecutionOutcome}</td>
                                <td><fmt:formatDate value="${map.createdDate}"
                                                    pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                <td><fmt:formatDate value="${map.updatedDate}"
                                                    pattern="dd/MM/yyyy"></fmt:formatDate></td>
                              </tr>
                            </c:forEach>

                          </table>
                        </div>
                      </div>
                    </c:if>

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

                    <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcMedAlertPerson.emailAddr}"><c:out
                                value="${appSvcMedAlertPerson.emailAddr}"/></span>
                    </div>
                    <div class="col-xs-6">
                        <span class="oldVal "
                              attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].emailAddr}"
                              style="display: none"><c:out
                                value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].emailAddr}"/></span>
                    </div>

                  </td>
                </tr>
                <tr>
                  <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Preferred
                      Mode of Receiving MedAlert</p>
                  </td>
                  <td>

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
                      <span class="oldVal "
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].preferredMode}"
                            style="display: none">
                           <c:choose>
                             <c:when test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].preferredMode=='1'}">Email</c:when>
                             <c:when test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].preferredMode=='2'}">SMS</c:when>
                             <c:when test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].preferredMode=='3'}">Email SMS</c:when>
                           </c:choose>
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
                      <span class="fileType" style="display:none">Docment1</span><span class="fileFilter"
                                                                                       style="display:none">png</span><span
                            class="fileMandatory" style="display:none">Yes</span>
                    </td>
                  </tr>
                  <tr>
                    <td>
                        <%-- <a href="${pageContext.request.contextPath}/file-repo?filerepo=svcFileRoId${currentSvcCode}${status.index}&fileRo${status.index}=<iais:mask name="svcFileRoId${currentSvcCode}${status.index}" value="${svcDoc.fileRepoId}"/>&fileRepoName=${svcDoc.docName}" title="Download" class="downloadFile">${svcDoc.docName}</a>--%>

                      <div class="col-xs-6">
                        <c:if test="${svcDoc.docSize!=null}">
                              <span class="newVal " attr="${svcDoc.md5Code}${svcDoc.fileRepoId}${svcDoc.docName}">
                                    <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"
                                      value="${svcDoc.fileRepoId}"/>&fileRepoName=${svcDoc.docName}" title="Download"
                                       class="downloadFile">${svcDoc.docName}</a> <c:out value="(${svcDoc.docSize})KB"/>
                                   </span>
                        </c:if>
                        <c:if test="${svcDoc.docSize==null}">
                              <span class="newVal " attr="${svcDoc.md5Code}${svcDoc.fileRepoId}${svcDoc.docName}">

                              </span>
                        </c:if>
                      </div>
                      <div class="col-xs-6">

                        <c:if test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docSize!=null}">
                                  <span class="oldVal "
                                        attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].md5Code}${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].fileRepoId}${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docName}"
                                        style="display: none">
                                  <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"  value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].fileRepoId}"/>&fileRepoName=${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docName}"
                                     title="Download" class="downloadFile">
                                      ${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docName}
                                  </a>
                                  <c:out value="(${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docSize})KB"/>
                                </span>
                        </c:if>
                        <c:if test="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docSize==null}">
                                <span class="oldVal "
                                      attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].md5Code}${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].fileRepoId}${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcDocDtoLit[status.index].docName}"
                                      style="display: none">

                                </span>
                        </c:if>

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

  <input type="hidden" value="${beEicGatewayClient}" id="beEicGatewayClient">

</div>

<script type="text/javascript">
    $(document).ready(function () {
        if ($('#beEicGatewayClient').val() != '') {
            alert($('#beEicGatewayClient').val());
        }
        var svcId = "";
        <c:if test="${rfi=='rfi'}">
        $('.panel-body').attr("style", "background-color: #999999;");
        </c:if>
    });
    $('#primaryCheckbox').click(function () {
        let jQuery = $(this).closest("div.panel-body");
        let jQuery1 = jQuery.attr("style");
        if ("" == jQuery1 || undefined ==jQuery1) {
            jQuery.attr("style", "background-color: #999999;");
        } else {
            jQuery.attr("style", "");
        }
    });
    $("#serviceCheckbox").click(function () {
        let jQuery = $(this).closest("div.panel-body");
        let jQuery1 = jQuery.attr("style");
        if ("" == jQuery1 || undefined ==jQuery1) {
            jQuery.attr("style", "background-color: #999999;");
        } else {
            jQuery.attr("style", "");
        }
    });
    $("#premisesCheckbox").click(function () {
        let jQuery = $(this).closest("div.panel-body");
        let jQuery1 = jQuery.attr("style");
        if ("" == jQuery1 || undefined ==jQuery1) {
            jQuery.attr("style", "background-color: #999999;");
        } else {
            jQuery.attr("style", "");
        }
    });

    function closeThis(obj) {
        $(obj).closest('div.row').attr("style", "display:none");
    }


    function showThisTableNewService(obj) {
        $(obj).closest('div.img-show').closest('td').find("div.new-img-show").removeAttr("style");
    }

    function showThisTableOldService(obj) {
        $(obj).closest('div.img-show').closest('td').find('div.old-img-show').removeAttr("style");
    }

    hideImg('newVal', 'oldVal');

    function hideImg(newValClass, oldValClass) {
        $('.' + oldValClass).each(function () {
            var oldVal = $(this).attr('attr');
            var newEle = $(this).parent().prev().find('.' + newValClass);
            var newVal = newEle.length > 0 ? newEle.attr('attr') : '';
            if (oldVal.length > 0 && newVal.length <= 0) {
                newEle.children('img').attr("style", "display: none");
            } else if (oldVal.length <= 0 && newVal.length > 0) {
                $(this).children('img').attr("style", "display: none");
            } else {
                newEle.children('img').removeAttr("style");
            }
        });
    }

    hightLightChangeVal('newVal', 'oldVal');

    function hightLightChangeVal(newValClass, oldValClass) {
        $('.' + oldValClass).each(function () {
            var oldVal = $(this).attr('attr');
            var newEle = $(this).parent().prev().find('.' + newValClass);
            var newVal = newEle.length > 0 ? newEle.attr('attr') : '';
            if ($('#oldAppSubmissionDto').val() == 'false') {
                if (oldVal.length > 0 || newVal.length > 0) {
                    if (oldVal != newVal) {
                        $(this).show();
                        var newHtml = $(this).parent().prev().find('.' + newValClass).html();
                        var oldHtml = $(this).html();
                        $(this).html(newHtml);
                        $(this).parent().prev().find('.' + newValClass).html(oldHtml);
                        $(this).attr("class", "newVal compareTdStyle");
                    } else if (oldVal.length > 0 && newVal.length <= 0) {
                        $(this).hide();
                    }
                }
            }
        });
    }

</script>
