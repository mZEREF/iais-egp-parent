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
<c:set var="appGrpPremisesDtoList" value="${AppSubmissionDto.appGrpPremisesDtoList}"></c:set>
<div class="panel-main-content">

  <div class="amended-service-info-gp">
    <h2>LABORATORY DISCIPLINES</h2>
    <c:forEach var="appSvcLaboratoryDisciplinesDto" items="${currentPreviewSvcInfo.appSvcLaboratoryDisciplinesDtoList}" varStatus="status">
        <div class="amend-preview-info">
          <p><span class="preview-title">Premises ${status.index+1}</span>: ${appSvcLaboratoryDisciplinesDto.premiseGetAddress}</p>
          <div class="form-check-gp">
            <div class="row">
              <div class="col-xs-12">
                <c:forEach var="checkList" items="${appSvcLaboratoryDisciplinesDto.appSvcChckListDtoList}">
                <div class="form-check active">
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${checkList.chkName}</p>
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
                <table>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.salutation}</p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.name }</p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.idType }</p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No. :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${cgo.idNo }</p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.designation  }</p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Regn Type:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.professionType  }</p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Regn No.:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.professionRegoNo }</p>
                    </td>
                  </tr>

                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Specialty:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.speciality }</p>
                    </td>
                  </tr>

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
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span> ${cgo.mobileNo}</p>
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
                          <td rowspan="5">
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
              <c:forEach items="${currentPreviewSvcInfo.appSvcPrincipalOfficersDtoList}" var="po">
                <table>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.salutation}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.name}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.idType}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.idNo}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.designation}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Office Telephone:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.officeTelNo}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>MobileNo:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.mobileNo}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>EmailAddress:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${po.emailAddr}</p>
                    </td>
                  </tr>
                </table>
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
                  <a href="${pageContext.request.contextPath}/file-repo?filerepo=svcFileRo${status.index}&fileRo${status.index}=<iais:mask name="svcfileRo${status.index}" value="${svcDoc.fileRepoId}"/>&fileRepoName=${svcDoc.docName}" title="Download" class="downloadFile">${svcDoc.docName}</a>
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

<script type="text/javascript">
    $(document).ready(function(){
        var svcId = "";

    });

</script>