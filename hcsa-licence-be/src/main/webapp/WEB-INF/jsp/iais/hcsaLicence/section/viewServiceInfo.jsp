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
<div class="panel-main-content service-pannel">
<input style="display: none" value="${NOT_VIEW}" id="view">
<input type="hidden" id="oldAppSubmissionDto" value="${appSubmissionDto.oldAppSubmissionDto==null}">
<c:set var="appGrpPremisesDtoList" value="${appSubmissionDto.appGrpPremisesDtoList}"></c:set>
<c:set var="oldAppGrpPremisesDtoList" value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList}"></c:set>
<c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST012')}">
  <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/viewSvcBusiness.jsp" />
</c:if>

<c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST008')}">
  <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/viewSvcVehicle.jsp" />
</c:if>

<c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST001')}">
  <div class="amended-service-info-gp">
    <label class="title-font-size">${stepNameMap['SVST001']}</label>
    <c:forEach var="appSvcLaboratoryDisciplinesDto"
               items="${currentPreviewSvcInfo.appSvcLaboratoryDisciplinesDtoList}" varStatus="status">
      <div class="amend-preview-info">
        <p><label class="preview-title col-xs-2 col-md-3" style="padding-right: 0%;padding-left: 0px;">Mode of Service Delivery ${status.index+1}:</label>
        <div class="col-xs-7 col-md-7">
            <span class="newVal " attr="${appGrpPremisesDtoList[status.index].address}">
              <c:out value="${appGrpPremisesDtoList[status.index].address}"/>
            </span>
          <br>
          <span class="oldVal " attr="${oldAppGrpPremisesDtoList[status.index].address}" style="display: none">
              <c:out value="${oldAppGrpPremisesDtoList[status.index].address}"/>
            </span>
        </div>
        </p>

        <div class="form-check-gp">
          <div class="row">
            <div class="col-xs-12" style="margin-left: 0px;padding-left: 0px;">
              <c:set var="oldAppSvcChckListDtoList"
                     value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcLaboratoryDisciplinesDtoList[status.index].appSvcChckListDtoList}" />
              <c:forEach var="checkList" items="${appSvcLaboratoryDisciplinesDto.appSvcChckListDtoList}" varStatus="statuss">
                <div class="form-check ">
                  <div class="form-check-label " aria-label="premise-1-cytology" style="padding-left:0px;">
                    <div class="col-xs-6 col-md-6">
                        <span class="newVal " attr="${checkList.chkName}${checkList.check}">
                          <c:if test="${checkList.check}">
                            <input style="cursor: default;" class="form-check-input" checked type="checkbox" disabled>
                            <label class="form-check-label" style="color: #212529;"><span class="check-square"></span>${checkList.chkName}</label>
                          </c:if>
                        </span>
                    </div>
                    <div class="col-xs-6 col-md-6">
                        <span class="oldVal "
                              attr="${oldAppSvcChckListDtoList[statuss.index].chkName}${oldAppSvcChckListDtoList[statuss.index].check}" style="display: none">
                              <c:if test="${oldAppSvcChckListDtoList[statuss.index].check}">
                                <input style="cursor: default" class="form-check-input" checked type="checkbox" disabled>
                                <label class="form-check-label" style="color: #212529;">
                                    <span class="check-square"></span>
                                      ${oldAppSvcChckListDtoList[statuss.index].chkName}
                                  </label>
                              </c:if>
                        </span>
                    </div>
                  </div>
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
  <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/viewSvcClinicalDirector.jsp" />
</c:if>

<c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST010')}">
  <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/viewSvcCharges.jsp" />
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
                  <th scope="col" style="display: none"></th>
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
                    <div class="col-xs-6 img-show">
                      <span class="newVal " attr="${cgo.name}">
                        <c:out value="${cgo.name}"/>
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                          <jsp:param name="profRegNo" value="${cgo.profRegNo}"/>
                          <jsp:param name="personName" value="${cgo.name}"/>
                          <jsp:param name="methodName" value="showThisNameTableNewService"/>
                        </jsp:include>
                      </span>
                    </div>
                    <div class="col-xs-6 img-show">
                      <span class="oldVal "
                            attr="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].name}"
                            style="display: none">${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].name}
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                            <jsp:param name="profRegNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo}"/>
                            <jsp:param name="personName" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].name}"/>
                            <jsp:param name="methodName" value="showThisNameTableOldService"/>
                        </jsp:include>
                      </span>
                    </div>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                      <jsp:param name="profRegNo" value="${cgo.profRegNo}"/>
                      <jsp:param name="cssClass" value="new-img-show"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                      <jsp:param name="profRegNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo}"/>
                      <jsp:param name="cssClass" value="old-img-show"/>
                    </jsp:include>
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
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                      <jsp:param name="idNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].idNo}"/>
                      <jsp:param name="cssClass" value="old-img-show"/>
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
                          <jsp:param name="profRegNo" value="${cgo.profRegNo}"/>
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
                          <jsp:param name="profRegNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo}"/>
                          <jsp:param name="methodName" value="showThisTableOldService"/>
                        </jsp:include>
                      </span>
                    </div>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                      <jsp:param name="profRegNo" value="${cgo.profRegNo}"/>
                      <jsp:param name="cssClass" value="new-img-show"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                      <jsp:param name="profRegNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcCgoDtoList[status.index].profRegNo}"/>
                      <jsp:param name="cssClass" value="old-img-show"/>
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
                  <th scope="col" style="display: none"></th>
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
                    <div class="col-xs-6">
                      <span class="newVal " attr="${sectionLeader.name}"><c:out value="${sectionLeader.name}"/></span>
                    </div>
                    <div class="col-xs-6">
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
                    <div class="col-xs-12">
                      <span class="newVal " attr="<iais:code code="${sectionLeader.qualification}"/>"><iais:code code="${sectionLeader.qualification}"/></span>
                      <br>
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
                    <div class="">
                      <span class="newVal " attr="${appGrpPrem.address}"><c:out value="${appGrpPrem.address}"/>
                      <br>
                      </span>
                      <span class="oldVal" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].address}"
                        style="display: none">
                          <c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].address}"/>
                      </span>
                    </div>
                </td>
              </c:if>
              <td style="text-align: center">
                <div class="">
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
              </td>
              <td style="text-align: center">
                <div class="">
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
              </td>
              <td style="text-align: center">
                <div class="">
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
  <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/viewSvcPersonnel.jsp" />
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
                <p><strong class="col-xs-6">Principal Officer <c:if test="${PO_SIZE>1}">${poIndex}</c:if>:</strong>
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
                  <th scope="col" style="display: none"></th>
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
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                      <jsp:param name="idNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index].idNo}"/>
                      <jsp:param name="cssClass" value="old-img-show"/>
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
                  <th scope="col" style="display: none"></th>
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
                    <div class="col-xs-6">
                      <span class="newVal " attr="${keyAppointmentHolder.name}"><c:out value="${keyAppointmentHolder.name}"/></span>
                    </div>
                    <div class="col-xs-6">
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
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                      <jsp:param name="idNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcKeyAppointmentHolderDtoList[status.index].idNo}"/>
                      <jsp:param name="cssClass" value="old-img-show"/>
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
                  <th scope="col" style="display: none"></th>
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
                      <span class="newVal " attr="${appSvcMedAlertPerson.name}"><c:out value="${appSvcMedAlertPerson.name}"/></span>
                    </div>
                    <div class="col-xs-6">
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
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                      <jsp:param name="idNo" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcMedAlertPersonList[status.index].idNo}"/>
                      <jsp:param name="cssClass" value="old-img-show"/>
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
                  <th scope="col" style="display: none"></th>
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
    var $target = $(obj).closest('td');
    var w1 = $target.css('width');
    var w2 = $target.prev().css('width');
    if (w1 == w2) {
      $target.find("div.disciplinary-record").children("div").css("margin-left", "-50%");
    } else {
      $target.find("div.disciplinary-record").children("div").css("margin-left", "-29%");
    }
    $(obj).closest('div.img-show').closest('td').find("div.new-img-show").show();
  }

  function showThisTableOldService(obj) {
    var $target = $(obj).closest('td');
    var w1 = $target.css('width');
    var w2 = $target.prev().css('width');
    if (w1 == w2) {
      $target.find("div.disciplinary-record").children("div").css("margin-left", "-50%");
    } else {
      $target.find("div.disciplinary-record").children("div").css("margin-left", "-29%");
    }
    $(obj).closest('div.img-show').closest('td').find('div.old-img-show').show();
  }

  function showThisNameTableNewService(obj) {
    var $target = $(obj).closest('td');
    var h = $target.css('height');
    $target.find("div.disciplinary-record").children("div").css("margin-top", h);
    $(obj).closest('div.img-show').closest('td').find("div.new-img-show").show();
  }

  function showThisNameTableOldService(obj) {
    var $target = $(obj).closest('td');
    var h = $target.css('height');
    $target.find("div.disciplinary-record").children("div").css("margin-top", h);
    $(obj).closest('div.img-show').closest('td').find('div.old-img-show').show();
  }

</script>
