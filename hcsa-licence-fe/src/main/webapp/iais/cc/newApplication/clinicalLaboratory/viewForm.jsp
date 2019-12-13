<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
      <c:forEach var="appGrpPremisesDto" items="${appGrpPremisesDtoList}" varStatus="status">
        <div class="amend-preview-info">
          <p><span class="preview-title">Premises ${status.index+1}</span>: ${appGrpPremisesDto.address}</p>
          <div class="form-check-gp">
            <div class="row">
              <div class="col-xs-12">
                <c:forEach var="appSvcLaboratoryDisciplinesDto" items="${currentPreviewSvcInfo.appSvcLaboratoryDisciplinesDtoList}">
                <c:forEach var="checkList" items="${appSvcLaboratoryDisciplinesDto.appSvcChckListDtoList}">
                <div class="form-check active">
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${checkList.chkName}</p>
                </div>
                </c:forEach>
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
            <c:forEach var="appGrpPremisesDto" items="${appGrpPremisesDtoList}" varStatus="status">
              <c:set var="hciName" value="${appGrpPremisesDto.hciName}"/>
                <input type="hidden" name="premId" value=""/>
                <table class="table discipline-table">
                  <thead>
                  <tr>
                    <th>Premises</th>
                    <th>Laboratory Disciplines</th>
                    <th>Clinical Governance Officers</th>
                  </tr>
                  </thead>
                    <tbody>
                    <c:forEach var="disciplineAllocation" items="${reloadDisciplineAllocationMap[hciName]}" varStatus="status">
                      <%--<c:set value="${premisesIndexNo}${status.index}" var="cgoName"/>--%>
                      <tr>
                        <c:if test="${status.first}">
                          <td rowspan="4">
                            <p class="visible-xs visible-sm table-row-title">${appGrpPremisesDto.address}</p>
                            <%--<input type="hidden" name="${premisesAndChkLst.premisesIndexNo}" value="${premisesAndChkLst.premisesIndexNo}" />--%>
                            <%--<p>${appGrpPremisesDto.address} </p>--%>
                          </td>
                        </c:if>
                        <td>
                          <%--<p class="visible-xs visible-sm table-row-title">Laboratory Disciplines</p>--%>
                          <%--<input type="hidden" name="${cgoName}" value="${chkLst.chkLstConfId}"/>--%>
                          <p>${disciplineAllocation.chkLstName}</p>
                        </td>
                        <td>
                          <%--<p class="visible-xs visible-sm table-row-title">Clinical Governance Officers</p>--%>
                          <p>${disciplineAllocation.cgoSelName}</p>
                        </td>
                      </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:forEach>
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

            </div>
          </div>
        </div>
      </div>
  </div>



</div>












<div id="control--printerFriendly--1" class="section control " style="overflow: visible;"><%--
    <tr height="1" class="incomplete">
      <td style="width: 100%;" class="first last">
        <div class="control-set-font control-font-header section-header">
          <h2>DisciplineAllocation</h2>
        </div>
        <div id="" class="control control-caption-horizontal" style="overflow: visible;">
          <div class="form-group form-horizontal control-set-alignment formgap">
            <div class="col-sm-9 control-label formtext">
              <c:forEach var="allocation" items="${currentPreviewSvcInfo.appSvcDisciplineAllocationDtoList}">
                <table class="table discipline-table">
                  <thead>
                  <tr>
                    <th>Premises</th>
                    <th>Laboratory Disciplines</th>
                    <th>Clinical Governance Officers</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr>
                    <td rowspan="4">
                      <p>${allocation.premiseVal} </p>
                    </td>
                    <td>
                      <p>Laboratory Disciplines</p>
                    </td>
                    <td>
                      <p>${allocation.idNo}</p>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </c:forEach>
            </div>
          </div>
      </td>
    </tr>


    <tr height="1" class="incomplete">
      <td style="width: 100%;" class="first last">
        <div class="control-set-font control-font-header section-header">
          <h2>SvcPrincipalOfficers</h2>
        </div>
        <div id="" class="control control-caption-horizontal" style="overflow: visible;">
          <div class="form-group form-horizontal control-set-alignment formgap">
            <div class="col-sm-9 control-label formtext">
              <c:forEach items="${currentPreviewSvcInfo.appSvcPrincipalOfficersDtoList}" var="po">
                <table>
                  <tr>
                    <td>${po.salutation}:</td><td>${po.name}</td>
                  </tr>
                  <tr>
                    <td>${po.idType}:</td><td>${po.idNo}</td>
                  </tr>
                  <tr>
                    <td>Designation:</td><td>${po.designation}</td>
                  </tr>
                  <tr>
                    <td>MobileNo:</td><td>${po.mobileNo}</td>
                  </tr>
                  <tr>
                    <td>EmailAddress:</td><td>${po.emailAddr}</td>
                  </tr>
                </table>
              </c:forEach>
            </div>
          </div>
        </div>
      </td>
    </tr>


    <tr height="1" class="incomplete">
      <td style="width: 100%;" class="first last">
        <div class="control-set-font control-font-header section-header">
          <h2>SvcPrincipalOfficers</h2>
        </div>
        <div id="" class="control control-caption-horizontal" style="overflow: visible;">
          <div class="form-group form-horizontal control-set-alignment formgap">
            <div class="col-sm-9 control-label formtext">
              <c:forEach items="${currentPreviewSvcInfo.appSvcDocDtoLit}" var="doc">
                <table>
                  <tr>
                    <td>***doc type***</td>
                    <td><a id="">${doc.fileName}</a></td>
                  </tr>
                </table>
              </c:forEach>
            </div>
          </div>
        </div>
      </td>
    </tr>

    </tbody>
  </table>
  <div id="control--printerFriendly--1**errorMsg_section_bottom" class="error_placements"></div>
--%></div>

<script type="text/javascript">
    $(document).ready(function(){
        var svcId = "";

    });

</script>