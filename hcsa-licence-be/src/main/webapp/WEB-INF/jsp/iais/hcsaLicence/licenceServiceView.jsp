<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-blank"/>
<style>
  *{
    font-size: 16px;
  }
</style>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input style="display: none" value="${NOT_VIEW}" id="view">
  <c:set var="appEdit" value="${applicationViewDto.appEditSelectDto}"/>
  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="tab-gp steps-tab">
            <div class="tab-content">
              <div class="tab-pane active" id="previewTab" role="tabpanel">
                <div class="preview-gp">
                  <div class="row">
                    <div class="col-xs-12 col-md-2 text-right">
                      <br>
                      <br>
                      <%--   <p class="print"><a href="#"> <em class="fa fa-print"></em>Print</a></p>--%>
                    </div>
                  </div>
                  <c:if test="${not empty errorMsg}">
                    <iais:error>
                      <div class="error">
                          ${errorMsg}
                      </div>
                    </iais:error>
                  </c:if>
                  <div class="row">
                    <div class="col-xs-12">
                      <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                        <div class="panel panel-default">
                          <div class="panel-heading" id="headingPremise" role="tab">
                            <h4 class="panel-title"><a  class="collapsed" role="button" data-toggle="collapse" href="#collapsePremise"
                                                       aria-expanded="true" aria-controls="collapsePremise">Premises</a>
                            </h4>
                          </div>
                          <div class="panel-collapse collapse" id="collapsePremise" role="tabpanel"
                               aria-labelledby="headingPremise">
                            <div class="panel-body">
                              <p class="text-right">
                                <c:if test="${rfi=='rfi'}">
                                  <c:if test="${(appEdit.premisesEdit || appEdit.premisesListEdit)&& canEidtPremise }">
                                    <input class="form-check-input" id="premisesCheckbox" type="checkbox"
                                           name="editCheckbox" aria-invalid="false" value="premises">
                                  </c:if>
                                </c:if>
                              </p>
                              <c:forEach var="appGrpPremDto" items="${appSubmissionDto.appGrpPremisesDtoList}"
                                         varStatus="status">
                                <div class="panel-main-content">
                                  <div class="preview-info">
                                    <div class="row">
                                      <div class="col-md-6">
                                        <label>Premises ${status.index+1} </label>
                                      </div>
                                      <div class="col-md-6">
                                          <div class="col-md-6">
                                            <span class="newVal compareTdStyle"
                                                  attr="${appGrpPremDto.premisesType}"><c:out
                                                    value="${appGrpPremDto.premisesType}"/></span>

                                          </div>
                                          <div class="col-md-6">
                                                <span class="oldVal" attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType}"
                                                      style="display: none"><c:out value="${ppSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType}"/></span>
                                          </div>
                                      </div>
                                    </div>
                                    <c:if test="${'CONVEYANCE'==appGrpPremDto.premisesType}">
                                      <div class="row">
                                        <div class="col-md-6">
                                         Vehicle No
                                        </div>
                                        <div class="col-md-6">
                                          <div  class="col-md-6">
                                            <span class="newVal compareTdStyle" attr="${appGrpPremDto.conveyanceVehicleNo}"><c:out value="${appGrpPremDto.conveyanceVehicleNo}"/></span>
                                          </div>
                                          <div  class="col-md-6">
                                             <span class="oldVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}"
                                                   style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}"/></span>
                                          </div>
                                        </div>
                                      </div>
                                    </c:if>

                                    <c:if test="${'ONSITE'==appGrpPremDto.premisesType}">

                                      <div class="row">
                                        <div class="col-md-6">
                                         Fire Safety Shelter Bureau Ref. No.
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                             <span class="newVal compareTdStyle"
                                                   attr="${appGrpPremDto.scdfRefNo}"><c:out
                                                     value="${appGrpPremDto.scdfRefNo}"/></span>
                                          </div>
                                          <div class="col-md-6">
                                               <span class="oldVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].scdfRefNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].scdfRefNo}"
                                                     style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].scdfRefNo}"/></span>
                                          </div>
                                        </div>
                                      </div>
                                      <div class="row">
                                        <div class="col-md-6">
                                          Fire Safety Certificate Issued Date
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                             <span class="newVal compareTdStyle"
                                                   attr="${appGrpPremDto.certIssuedDt}">
                                               <c:if test="${empty appGrpPremDto.certIssuedDt}">
                                                 <c:out value="${appGrpPremDto.certIssuedDt}"/>
                                               </c:if>
                                                <c:if test="${not empty appGrpPremDto.certIssuedDt}">
                                                  <fmt:formatDate value="${appGrpPremDto.certIssuedDt}" pattern="dd/MM/yyyy"/>
                                                </c:if>

                                              </span>
                                          </div>
                                          <div class="col-md-6">
                                              <span class="oldVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].certIssuedDt ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].certIssuedDt}"
                                                    style="display: none"><fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].certIssuedDt}" pattern="dd/MM/yyyy"/>
                                                </span>

                                          </div>
                                        </div>
                                      </div>

                                      <div class="row">
                                        <div class="col-md-6">
                                         Name of HCI
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                                <span class="newVal compareTdStyle"
                                                      attr="${appGrpPremDto.hciName}"><c:out
                                                        value="${appGrpPremDto.hciName}"/></span>
                                          </div>
                                          <div class="col-md-6">
                                             <span class="oldVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}"
                                                   style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}"/></span>

                                          </div>
                                        </div>
                                      </div>
                                    </c:if>

                                    <div class="row">
                                      <div class="col-md-6">
                                      Postal Code
                                      </div>
                                      <div class="col-md-6">
                                        <div  class="col-md-6">
                                           <span class="newVal compareTdStyle"
                                                 attr="${appGrpPremDto.postalCode}"><c:out
                                                   value="${appGrpPremDto.postalCode}"/></span>
                                        </div>
                                        <div  class="col-md-6">
                                           <span class="oldVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].postalCode ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].postalCode}"
                                                 style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].postalCode}"/></span>
                                        </div>
                                      </div>
                                    </div>

                                    <div class="row">
                                      <div class="col-md-6">
                                       Address Type
                                      </div>
                                      <div class="col-md-6">
                                          <div class="col-md-6">
                                              <span class="newVal compareTdStyle"
                                                    attr="${appGrpPremDto.addrType}">

                                              <c:if test="${appGrpPremDto.addrType=='ADDTY001'}"> Apt Blk</c:if>
                                              <c:if test="${appGrpPremDto.addrType=='ADDTY002'}"> Without Apt Blk</c:if>

                                              </span>

                                          </div>
                                          <div class="col-md-6">
                                             <span class="oldVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].addrType ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].addrType}"
                                                   style="display: none">
                                                  <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].addrType=='ADDTY001'}"> Apt Blk</c:if>
                                                  <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].addrType=='ADDTY002'}"> Without Apt Blk</c:if>
                                             </span>
                                          </div>
                                      </div>
                                    </div>
                                    <div class="row">
                                      <div class="col-md-6">
                                        Block / House No.
                                      </div>
                                      <div class="col-md-6">
                                        <div class="col-md-6">
                                           <span class="newVal compareTdStyle"
                                                 attr="${appGrpPremDto.blkNo}"><c:out
                                                   value="${appGrpPremDto.blkNo}"/></span>
                                        </div>
                                        <div class="col-md-6">
                                          <span class="oldVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].blkNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].blkNo}"
                                                style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].blkNo}"/></span>
                                        </div>

                                        </span>
                                      </div>
                                    </div>

                                    <div class="row">
                                      <div class="col-md-6">
                                        Floor No.
                                      </div>
                                      <div class="col-md-6">
                                        <div class="col-md-6">
                                           <span class="newVal compareTdStyle"
                                                 attr="${appGrpPremDto.floorNo}"><c:out
                                                   value="${appGrpPremDto.floorNo}"/></span>

                                        </div>
                                        <div class="col-md-6">
                                             <span class="oldVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].floorNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].floorNo}"
                                                   style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].floorNo}"/></span>
                                        </div>
                                      </div>
                                    </div>


                                    <div class="row">
                                      <div class="col-md-6">
                                        Unit No.
                                      </div>
                                      <div class="col-md-6">
                                          <div class="col-md-6">
                                              <span class="newVal compareTdStyle"
                                                    attr="${appGrpPremDto.unitNo}"><c:out
                                                      value="${appGrpPremDto.unitNo}"/></span>

                                          </div>
                                          <div class="col-md-6">
                                              <span class="oldVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].unitNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].unitNo}"
                                                    style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].unitNo}"/></span>
                                          </div>
                                      </div>
                                    </div>


                                    <div class="row">
                                      <div class="col-md-6">
                                       Street Name
                                      </div>
                                      <div class="col-md-6">
                                          <div class="col-md-6">
                                             <span class="newVal compareTdStyle"
                                                   attr="${appGrpPremDto.streetName}"><c:out
                                                     value="${appGrpPremDto.streetName}"/></span>

                                          </div>
                                          <div class="col-md-6">
                                             <span class="oldVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].streetName ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].streetName}"
                                                   style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].streetName}"/></span>
                                          </div>
                                      </div>

                                    </div>
                                    <div class="row">
                                      <div class="col-md-6">
                                       Building Name
                                      </div>
                                      <div class="col-md-6">
                                          <div class="col-md-6">
                                            <span class="newVal compareTdStyle"
                                                  attr="${appGrpPremDto.buildingName}"><c:out
                                                    value="${appGrpPremDto.buildingName}"/></span>
                                          </div>
                                          <div class="col-md-6">
                                               <span class="oldVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].buildingName ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].buildingName}"
                                                     style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].buildingName }"/></span>
                                          </div>
                                      </div>
                                    </div>

                                    <c:if test="${'ONSITE'==appGrpPremDto.premisesType}">
                                      <div class="row">
                                        <div class="col-md-6">
                                          Are you co-locating with another licensee ?
                                        </div>
                                        <div class="col-md-6">
                                          <span class="col-md-6">
                                             No
                                          </span>
                                        </div>
                                      </div>
                                      <div class="row">
                                        <div class="col-md-6">
                                         Office Telephone No.
                                        </div>
                                        <div class="col-md-6">
                                            <div class="col-md-6">
                                               <span class="newVal compareTdStyle"
                                                     attr="${appGrpPremDto.offTelNo}"><c:out
                                                       value="${appGrpPremDto.offTelNo}"/></span>

                                            </div>
                                            <div class="col-md-6">
                                                <span class="oldVal" attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].offTelNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].offTelNo}"
                                                      style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].offTelNo}"/></span>
                                            </div>
                                        </div>
                                      </div>
                                    </c:if>

                                    <div class="row">
                                      <div class="col-md-6">
                                        Operating Hours (Start)
                                      </div>
                                      <div class="col-md-6">
                                          <div class="col-md-6">
                                             <span class="newVal compareTdStyle"
                                                   attr="${appGrpPremDto.wrkTimeFrom}">
                                                    <fmt:formatDate value="${appGrpPremDto.wrkTimeFrom}"
                                                                    pattern="HH : mm"></fmt:formatDate>
                                                </span>
                                          </div>
                                          <div class="col-md-6">
                                             <span class=" oldVal" attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeFrom ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeFrom}" style="display: none">
                                              <fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeFrom}"
                                                              pattern="HH : mm"></fmt:formatDate></span>

                                          </div>
                                      </div>
                                    </div>

                                    <div class="row">
                                      <div class="col-md-6">
                                       Operating Hours (End)
                                      </div>
                                      <div class="col-md-6">
                                          <div class="col-md-6">
                                             <span class="newVal compareTdStyle"
                                                   attr="${appGrpPremDto.wrkTimeTo}">
                                                  <fmt:formatDate value="${appGrpPremDto.wrkTimeTo}"
                                                                  pattern="HH : mm"></fmt:formatDate>
                                              </span>

                                          </div>
                                          <div class="col-md-6">
                                               <span class="oldVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeTo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeTo}"
                                                     style="display: none"> <fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeTo}"
                                                                                            pattern="HH : mm"></fmt:formatDate></span>
                                          </div>
                                      </div>
                                    </div>
                                <c:forEach items="${appGrpPremDto.appPremPhOpenPeriodList}"
                                           var="appPremPhOpenPeriod" varStatus="statu">

                                  <div class="row">
                                    <div class="col-md-6">
                                      Select Public Holiday
                                    </div>
                                    <div class="col-md-6">
                                        <div class="col-md-6">
                                             <span class="newVal compareTdStyle" attr="${appPremPhOpenPeriod.phDate}">
                                               <c:out value="${appPremPhOpenPeriod.dayName}"/></span>
                                        </div>
                                        <div class="col-md-6">
                                           <span class="oldVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].phDate ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].phDate}" style="display: none">
                                             <c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].phDate}"/></span>

                                        </div>
                                    </div>
                                  </div>

                                  <div class="row">
                                    <div class="col-md-6">
                                     Public Holidays Operating Hours (Start)
                                    </div>
                                    <div class="col-md-6">
                                        <div class="col-md-6">
                                             <span class="newVal compareTdStyle"
                                                   attr="${appPremPhOpenPeriod.convStartFromHH}"><label><c:out
                                                     value="${appPremPhOpenPeriod.convStartFromHH} : ${appPremPhOpenPeriod.convStartFromMM}"/></label></span>
                                        </div>
                                        <div class="col-md-6">
                                          <span class="oldVal" attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].convStartFromHH ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].convStartFromHH}" style="display: none">
                                            <c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].convStartFromHH} : ${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].convStartFromMM}"/></span>

                                        </div>
                                    </div>
                                  </div>

                                  <div class="row">
                                    <div class="col-md-6">
                                      Public Holidays Operating Hours (End)
                                    </div>
                                    <div class="col-md-6">
                                        <div class="col-md-6">
                                           <span class="newVal compareTdStyle"
                                                 attr="${appPremPhOpenPeriod.endTo}">
                                              <fmt:formatDate value="${appPremPhOpenPeriod.endTo}"
                                                              pattern="HH : mm"></fmt:formatDate>
                                            </span>

                                        </div>
                                        <div class="col-md-6">
                                               <span class="oldVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].endTo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].endTo}"
                                                     style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].endTo}"/></span>
                                        </div>
                                    </div>
                                  </div>
                                </c:forEach>
                                  </div>
                                </div>
                              </c:forEach>
                            </div>
                          </div>
                        </div>

                        <div class="panel panel-default">
                          <div class="panel-heading"  id="PrimaryDocuments" role="tab">
                            <h4 class="panel-title"><a class="collapsed" role="button" data-toggle="collapse" href="#collapsePrimaryDocuments" aria-expanded="true" aria-controls="collapsePrimaryDocuments">Primary Documents</a></h4>
                          </div>
                          <div class="panel-collapse collapse" id="collapsePrimaryDocuments"  role="tabpanel" aria-labelledby="PrimaryDocuments">
                            <div class="panel-body">
                              <p class="text-right">
                                <c:if test="${rfi=='rfi'}">
                                  <c:if test="${appEdit.docEdit}">
                                    <input class="form-check-input" id="primaryCheckbox" type="checkbox" name="editCheckbox" aria-invalid="false" value="primary">
                                  </c:if>
                                </c:if>
                              </p>
                              <div class="elemClass-1561088919456">
                                <div  class="page section control  container-s-1" style="margin: 10px 0px">
                                  <div class="control-set-font control-font-header section-header">
                                    <label style="font-size: 2.2rem">Uploaded Documents</label>
                                  </div>
                                  <div class="pop-up">
                                    <div class="pop-up-body">
                                      <c:forEach var="appGrpPrimaryDocDto" items="${appSubmissionDto.appGrpPrimaryDocDtos}" varStatus="status">
                                        <div class="content-body fileUploadContainer">
                                          <div class="field col-sm-4 control-label formtext"><label>${appGrpPrimaryDocDto.svcComDocName}:</label></div>
                                          <div class="control col-sm-12">
                                            <div class="fileList">
                                              <span class="filename server-site col-xs-12 col-md-12" id="130">
                                                  <div class="col-xs-6 col-md-6">
                                                  <span class="newVal compareTdStyle" attr="${appGrpPrimaryDocDto.docSize}${appGrpPrimaryDocDto.docName}" ><c:out value="${appGrpPrimaryDocDto.docName} (${appGrpPrimaryDocDto.docSize} KB)"/></span>

                                                </div>
                                                <div class="col-xs-6 col-md-6">
                                                  <span class="oldVal" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize}${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName}"  style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName} (${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize} KB)"/></span>
                                                </div>
                                              <%--  <c:if test="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName}">
                                                  <c:out value="-"/>
                                                </c:if>
                                                <c:if test="${!empty appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName}">
                                                  <c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName} (${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize} KB)"/>
                                                </c:if>--%>
                                              </span>

                                            </div>
                                          </div>
                                        </div>
                                      </c:forEach>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>


                        <div class="panel panel-default">
                          <div class="panel-heading" id="headingOne" role="tab">
                            <h4 class="panel-title"><a class="collapsed" role="button" data-toggle="collapse" href="#collapseOne"
                                                       aria-expanded="true" aria-controls="collapseOne">Key Roles</a>
                            </h4>
                          </div>
                          <div class="panel-collapse collapse" id="collapseOne" role="tabpanel"
                               aria-labelledby="headingOne">
                            <div class="panel-body">
                              <p class="text-right">
                                <!--<input class="form-check-input" id="primaryCheckbox" type="checkbox" name="editCheckbox" aria-invalid="false" value="primary">-->
                              </p>
                              <div class="elemClass-1561088919456">
                                <div id="control--runtime--34" class="page section control  container-s-1"
                                     style="margin: 10px 0px">
                                  <div class="control-set-font control-font-header section-header">
                                    <%--  <p class="summary-header">Licensee (Company)</p>--%>
                                  </div>
                                  <div class="pop-up">
                                    <div class="pop-up-body">
                                      <div class="field col-sm-8 control-label formtext">

                                        <div class="row">
                                          <div class="col-md-6">
                                            <label>Licensee Information</label>
                                          </div>
                                          <div class="col-md-6">
                                          </div>
                                        </div>
                                        <div class="row">
                                          <div class="col-md-8">
                                          Who is the Licensee?
                                          </div>
                                          <div class="col-md-6">
                                            Local Company
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                          UEN Number
                                          </div>
                                          <div class="col-md-6">
                                            <div class="col-md-6">
                                              <span>${newLicenceDto.uenNo}</span>
                                            </div>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                          Name of Licensee
                                          </div>
                                          <div class="col-md-6">
                                              <div class="col-md-6">
                                                <span class="newVal compareTdStyle" attr="${newLicenceDto.name}"><c:out
                                                        value="${newLicenceDto.name}"/></span>

                                              </div>
                                              <div class="col-md-6">
                                                  <span class="oldVal " attr="${empty oldLicenceDto.name ? '-' : oldLicenceDto.name}"
                                                        style="display: none"><c:out
                                                          value="${oldLicenceDto.name}"/></span>
                                              </div>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                           Postal Code
                                          </div>
                                          <div class="col-md-6">
                                              <div class="col-md-6">
                                                  <span class="newVal compareTdStyle" attr="${newLicenceDto.postalCode}"><c:out
                                                          value="${newLicenceDto.postalCode}"/></span>
                                              </div>
                                              <div class="col-md-6">
                                                 <span class="oldVal " attr="${empty oldLicenceDto.postalCode ? '-' : oldLicenceDto.postalCode}"
                                                       style="display: none"><c:out
                                                         value="${oldLicenceDto.postalCode}"/></span>

                                              </div>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Address Type
                                          </div>
                                          <div class="col-md-6">
                                              <div class="col-md-6">
                                                 <span class="newVal compareTdStyle" attr="${newLicenceDto.addrType}"><c:out
                                                         value="${newLicenceDto.addrType}"/></span>

                                              </div>
                                              <div class="col-md-6">
                                                 <span class="oldVal" attr="${empty oldLicenceDto.addrType ? '-' : oldLicenceDto.addrType}"
                                                       style="display: none"><c:out
                                                         value="${oldLicenceDto.addrType}"/></span>
                                              </div>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Blk No.
                                          </div>
                                          <div class="col-md-6">
                                              <div class="col-md-6">
                                                 <span class="newVal compareTdStyle" attr="${newLicenceDto.unitNo}"><c:out
                                                         value="${newLicenceDto.unitNo}"/></span>

                                              </div>
                                              <div class="col-md-6">
                                                    <span class="oldVal " attr="${empty oldLicenceDto.unitNo ? '-' : oldLicenceDto.unitNo}"
                                                          style="display: none"><c:out
                                                            value="${oldLicenceDto.unitNo}"/></span>
                                              </div>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Floor No.
                                          </div>
                                          <div class="col-md-6">
                                              <div class="col-md-6">
                                                 <span class="newVal compareTdStyle" attr="${newLicenceDto.floorNo}"><c:out
                                                         value="${newLicenceDto.floorNo}"/></span>

                                              </div>
                                              <div class="col-md-6">
                                                   <span class="oldVal " attr="${empty oldLicenceDto.floorNo ? '-' : oldLicenceDto.floorNo}"
                                                         style="display: none"><c:out
                                                           value="${oldLicenceDto.floorNo}"/></span>
                                              </div>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Unit No.
                                          </div>
                                          <div class="col-md-6">
                                              <div class="col-md-6">
                                                   <span class="newVal compareTdStyle" attr="${newLicenceDto.unitNo}"><label><c:out
                                                           value="${newLicenceDto.unitNo}"/></label></span>
                                              </div>
                                              <div class="col-md-6">
                                                 <span class="oldVal " attr="${empty oldLicenceDto.unitNo ? '-' : oldLicenceDto.unitNo}"
                                                       style="display: none"></span>

                                              </div>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Street Name
                                          </div>
                                          <div class="col-md-6">
                                              <div class="col-md-6">
                                                   <span class="newVal compareTdStyle" attr="${newLicenceDto.streetName}"><c:out
                                                           value="${newLicenceDto.streetName}"/></span>
                                              </div>
                                              <div class="col-md-6">
                                                 <span class="oldVal " attr="${empty oldLicenceDto.streetName ? '-' : oldLicenceDto.streetName}"
                                                       style="display: none"><c:out
                                                         value="${oldLicenceDto.streetName}"/></span>

                                              </div>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Building Name
                                          </div>
                                          <div class="col-md-6">
                                              <div class="col-md-6">
                                                  <span class="newVal compareTdStyle" attr="${newLicenceDto.buildingName}"><c:out
                                                          value="${newLicenceDto.buildingName}"/></span>
                                              </div>
                                              <div class="col-md-6">
                                                  <span class="oldVal " attr="${empty oldLicenceDto.buildingName ? '-' : oldLicenceDto.buildingName}"
                                                        style="display: none"><c:out
                                                          value="${oldLicenceDto.buildingName}"/></span>

                                              </div>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Office Telephone No.
                                          </div>
                                          <div class="col-md-6">
                                              <div class="col-md-6">
                                                  <span class="newVal compareTdStyle" attr="${newLicenceDto.officeTelNo}"><c:out
                                                          value="${newLicenceDto.officeTelNo}"/></span>
                                              </div>
                                              <div class="col-md-6">
                                                 <span class="oldVal " attr="${empty oldLicenceDto.officeTelNo ? '-' : oldLicenceDto.officeTelNo}"
                                                       style="display: none"><c:out
                                                         value="${oldLicenceDto.officeTelNo}"/></span>

                                              </div>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Office Email Address
                                          </div>
                                          <div class="col-md-6">
                                              <div class="col-md-6">
                                                  <span class="newVal compareTdStyle" attr="${newLicenceDto.emilAddr}"><c:out
                                                          value="${newLicenceDto.emilAddr}"/></span>
                                              </div>
                                              <div class="col-md-6">
                                                 <span class="oldVal " attr="${empty oldLicenceDto.emilAddr ? '-' : oldLicenceDto.emilAddr}"
                                                       style="display: none"><c:out
                                                         value="${oldLicenceDto.emilAddr}"/></span>

                                              </div>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            <label>Board Member 1</label>
                                          </div>
                                          <div class="col-md-6">
                                          </div>
                                        </div>


                                        <div class="row">
                                          <div class="col-md-6">
                                            Salutation
                                          </div>
                                          <div class="col-md-6">
                                            Mr
                                          </div>
                                        </div>


                                        <div class="row">
                                          <div class="col-md-6">
                                            name
                                          </div>
                                          <div class="col-md-6">
                                            Mo Delan
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            ID No.
                                          </div>
                                          <div class="col-md-6">
                                            S8299230H
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Designation
                                          </div>
                                          <div class="col-md-6">
                                            CEO
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Designation Cessation Date
                                          </div>
                                          <div class="col-md-6">
                                            -
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Designation Cessation Reason
                                          </div>
                                          <div class="col-md-6">
                                            -
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            <label>Board Member 1</label>
                                          </div>
                                          <div class="col-md-6">
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Salutation
                                          </div>
                                          <div class="col-md-6">
                                            Mrs
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            name
                                          </div>
                                          <div class="col-md-6">
                                            Linda Tan
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Designation
                                          </div>
                                          <div class="col-md-6">
                                            CFO
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Designation Cessation Date
                                          </div>
                                          <div class="col-md-6">
                                            -
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Designation Cessation Reason
                                          </div>
                                          <div class="col-md-6">
                                            -
                                          </div>
                                        </div>


                                        <div class="row">
                                          <div class="col-md-6">
                                            <label>Authorised Person 1</label>
                                          </div>
                                          <div class="col-md-6">
                                          </div>
                                        </div>
                                        <div class="row">
                                          <div class="col-md-6">
                                            Name
                                          </div>
                                          <div class="col-md-6">
                                            Mo Delan
                                          </div>
                                        </div>
                                        <div class="row">
                                          <div class="col-md-6">
                                            ID No.
                                          </div>
                                          <div class="col-md-6">
                                            S8299230H
                                          </div>
                                        </div>
                                        <div class="row">
                                          <div class="col-md-6">
                                          Designation
                                          </div>
                                          <div class="col-md-6">
                                            CEO
                                          </div>
                                        </div>
                                        <div class="row">
                                          <div class="col-md-6">
                                           Office Telephone
                                          </div>
                                          <div class="col-md-6">
                                            64593810
                                          </div>
                                        </div>
                                        <div class="row">
                                          <div class="col-md-6">
                                           Mobile No.
                                          </div>
                                          <div class="col-md-6">
                                            92338899
                                          </div>
                                        </div>
                                        <div class="row">
                                          <div class="col-md-6">
                                            Email Address
                                          </div>
                                          <div class="col-md-6">
                                            modelan@gmail.com
                                          </div>
                                        </div>
                                        <div class="row">
                                          <div class="col-md-6">
                                            name
                                          </div>
                                          <div class="col-md-6">
                                            Linda Tan
                                          </div>
                                        </div>
                                        <div class="row">
                                          <div class="col-md-6">
                                          ID No.
                                          </div>
                                          <div class="col-md-6">
                                            S4285224D
                                          </div>
                                        </div>
                                        <div class="row">
                                          <div class="col-md-6">
                                            Designation
                                          </div>
                                          <div class="col-md-6">
                                            CFO
                                          </div>
                                        </div>
                                        <div class="row">
                                          <div class="col-md-6">
                                            Office Telephone
                                          </div>
                                          <div class="col-md-6">
                                            64593815
                                          </div>
                                        </div>
                                        <div class="row">
                                          <div class="col-md-6">
                                            Mobile No.
                                          </div>
                                          <div class="col-md-6">
                                            82331122
                                          </div>
                                        </div>
                                        <div class="row">
                                          <div class="col-md-6">
                                            Email Address
                                          </div>
                                          <div class="col-md-6">
                                            lindatan@gmail.com
                                          </div>
                                        </div>



                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>

                        <div class="panel panel-default svc-content">

                          <div class="panel-heading" id="headingServiceInfo0" role="tab">
                            <h4 class="panel-title"><a class="svc-pannel-collapse collapsed" role="button" data-toggle="collapse"
                                                       href="#collapseServiceInfo0" aria-expanded="true"
                                                       aria-controls="collapseServiceInfo">Service Related Information
                              - ${hcsaServiceDto.svcName}</a></h4>
                          </div>

                          <div class=" panel-collapse collapse" id="collapseServiceInfo0" role="tabpanel"
                               aria-labelledby="headingServiceInfo0">
                            <div class="panel-body">
                              <p class="text-right">

                                <c:if test="${appEdit.serviceEdit}">
                                    <c:if test="${rfi=='rfi'}">
                                    <input class="form-check-input" id="serviceCheckbox" type="checkbox"
                                           name="editCheckbox" aria-invalid="false" value="service">
                                    </c:if>
                                </c:if>
                              </p>
                              <%--<iframe class="svc-iframe" title=""
                                      src="${pageContext.request.contextPath}/eservice/INTRANET/MOHServiceView"
                                      id="elemId-0" width="100%" height="100%"></iframe>--%>
                              <%@include file="/WEB-INF/jsp/iais/hcsaLicence/viewForm.jsp"%>
                              <!--scrolling="no" scrollbar="no" -->
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="application-tab-footer">
                  <div class="row">
                    <div class="col-xs-12 col-sm-6">

                    </div>
                    <c:if test="${rfi=='rfi'}">
                      <div class="col-xs-12 col-sm-6">
                        <div class="button-group"><a class="next btn btn-primary" id="previewNext">SUBMIT </a></div>
                      </div>
                    </c:if>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</form>
<style>

  *{
    word-wrap: break-word
  }
</style>
<script type="text/javascript">

    $(document).ready(function () {
        //Binding method
        $('#previewNext').click(function () {
            var mainForm = document.getElementById("mainForm");
            mainForm.submit();
        });

        $('.svc-pannel-collapse').click(function () {
            $svcContenEle = $(this).closest('div.svc-content');
            $svcContenEle.find('.svc-iframe').css('height', '400px');

        });
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
                } else {
                    $(this).hide();
                }
            }
        });
    }


</script>
