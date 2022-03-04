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
                <c:set var="oldCheckList" value="${oldAppSvcChckListDtoList[statuss.index]}"/>
                <div class="form-check ">
                  <div class="form-check-label col-xs-12 col-md-12" aria-label="premise-1-cytology" style="padding-left:0px;">
                    <div class="col-xs-6 col-md-6">
                    <span class="newVal " attr="${checkList.chkName}${checkList.check}<c:out value="${checkList.otherScopeName}"/>">
                      <c:if test="${checkList.check}">
                        <input style="cursor: default;" class="form-check-input" checked type="checkbox" disabled>
                        <label class="form-check-label" style="color: #212529;">
                          <span class="check-square"></span>
                            <c:if test="${'Please indicate' == checkList.chkName}" var="showOtherScopeName">
                              <c:out value="${checkList.otherScopeName}"/>
                            </c:if>
                            <c:if test="${!showOtherScopeName}">
                              ${checkList.chkName}
                            </c:if>
                        </label>
                      </c:if>
                    </span>
                    </div>
                    <div class="col-xs-6 col-md-6">
                      <span class="oldVal "
                            attr="${oldCheckList.chkName}${oldCheckList.check}<c:out value="${oldCheckList.otherScopeName}"/>" style="display: none">
                        <c:if test="${oldCheckList.check}">
                          <input style="cursor: default" class="form-check-input" checked type="checkbox" disabled>
                          <label class="form-check-label" style="color: #212529;">
                              <span class="check-square"></span>
                                <c:if test="${'Please indicate' == oldCheckList.chkName}" var="showOtherScopeName">
                                  <c:out value="${oldCheckList.otherScopeName}"/>
                                </c:if>
                                <c:if test="${!showOtherScopeName}">
                                  ${oldCheckList.chkName}
                                </c:if>
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
  <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/viewSvcGovernanceOfficer.jsp" />
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
            <table aria-describedby="" class="table discipline-table">
              <thead>
              <tr>
                <th scope="col">Mode of Service Delivery</th>
                <th scope="col">${stepNameMap['SVST001']}</th>
                <th scope="col">Clinical Governance Officers</th>
                <th scope="col">Section Leader</th>
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
                <td rowspan="${reloadDisciplineAllocationMap[reloadMapValue].size()}">
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
              <td>
                <div class="">
                  <span class="newVal " attr="${disciplineAllocation.chkLstName}${disciplineAllocation.check}">
                    <c:if test="${disciplineAllocation.check}">
                      <c:out value="${disciplineAllocation.chkLstName}"/>
                    </c:if>
                  </span>
                  <br>
                  <span class="oldVal "
                    attr="${reloadOld[oldReloadMapValue][stat.index].chkLstName}${reloadOld[oldReloadMapValue][stat.index].check}"
                    style="display: none">
                    <c:if test="${reloadOld[oldReloadMapValue][stat.index].check}">
                      <c:out value="${reloadOld[oldReloadMapValue][stat.index].chkLstName}"/>
                    </c:if>
                  </span>
                </div>
              </td>
              <td>
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
              <td>
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
  <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/viewSvcPrincipalOfficers.jsp" />
</c:if>
<c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST014')}">
  <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/viewSvcKeyAppointmentHolder.jsp" />
</c:if>
<c:if test="${fn:contains(hcsaServiceStepSchemeDtoList, 'SVST007')}">
  <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/viewSvcMedAlert.jsp" />
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
