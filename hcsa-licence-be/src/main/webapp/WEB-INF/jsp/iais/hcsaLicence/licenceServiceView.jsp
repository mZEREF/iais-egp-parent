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
                                  <c:if test="${appEdit.premisesEdit || appEdit.premisesListEdit }">
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
                                        <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType=='ONSITE'}">On-site</c:if>
                                        <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType=='CONVEYANCE'}">Conveyance</c:if>
                                        <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType=='OFFSIET'}">Off-site</c:if>
                                        <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType==null}">-</c:if>
                                        <wrms:value width="7">
                                        <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                          <span class="oldVal compareTdStyle"
                                                attr="${appGrpPremDto.premisesType}"
                                                style="display: none"><label><c:out
                                                  value="${appGrpPremDto.premisesType}"/></label></span>
                                        </wrms:value>
                                      </div>
                                    </div>


                                    <c:if test="${'CONVEYANCE'==appGrpPremDto.premisesType}">
                                      <div class="row">
                                        <div class="col-md-6">
                                         Vehicle No
                                        </div>
                                        <div class="col-md-6">
                                            ${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}
                                              <wrms:value width="7">
                                          <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}"
                                                style="display: none"><label><c:out value=""/></label></span>
                                                <span class="oldVal compareTdStyle"
                                                      attr="${appGrpPremDto.conveyanceVehicleNo}"
                                                      style="display: none"><label><c:out
                                                        value="${appGrpPremDto.conveyanceVehicleNo}"/></label></span>
                                              </wrms:value>
                                        </div>
                                      </div>
                                    </c:if>

                                    <c:if test="${'ONSITE'==appGrpPremDto.premisesType}">
                                      <div class="row">
                                        <div class="col-md-6">
                                         Fire Safety Certificate Issued Date
                                        </div>
                                        <div class="col-md-6">
                                          <c:if test="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].certIssuedDt}">
                                            <c:out value="-"/>
                                          </c:if>
                                          <fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].certIssuedDt}" pattern="dd/MM/yyyy"/>
                                          <wrms:value width="7">
                                          <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].certIssuedDt ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].certIssuedDt}"
                                                style="display: none"><label><c:out value=""/></label></span>
                                            <span class="oldVal compareTdStyle"
                                                  attr="${appGrpPremDto.certIssuedDt}"
                                                  style="display: none"><label>
                                              <c:if test="${empty appGrpPremDto.certIssuedDt}">
                                                <c:out value="${appGrpPremDto.certIssuedDt}"/>
                                              </c:if>
                                              <c:if test="${not empty appGrpPremDto.certIssuedDt}">
                                                <fmt:formatDate value="${appGrpPremDto.certIssuedDt}" pattern="dd/MM/yyyy"/>
                                              </c:if>

                                              </label></span>
                                          </wrms:value>
                                        </div>
                                      </div>
                                      <div class="row">
                                        <div class="col-md-6">
                                         Fire Safety Shelter Bureau Ref. No.
                                        </div>
                                        <div class="col-md-6">
                                            ${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].scdfRefNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].scdfRefNo}
                                              <wrms:value width="7">
                                          <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].scdfRefNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].scdfRefNo}"
                                                style="display: none"><label><c:out value=""/></label></span>
                                                <span class="oldVal compareTdStyle"
                                                      attr="${appGrpPremDto.scdfRefNo}"
                                                      style="display: none"><label><c:out
                                                        value="${appGrpPremDto.scdfRefNo}"/></label></span>
                                              </wrms:value>
                                        </div>

                                      </div>
                                      <div class="row">
                                        <div class="col-md-6">
                                         Name of HCI
                                        </div>
                                        <div class="col-md-6">
                                            ${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}
                                              <wrms:value width="7">
                                          <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}"
                                                style="display: none"><label><c:out value=""/></label></span>
                                                <span class="oldVal compareTdStyle"
                                                      attr="${appGrpPremDto.hciName}"
                                                      style="display: none"><label><c:out
                                                        value="${appGrpPremDto.hciName}"/></label></span>
                                              </wrms:value>
                                        </div>

                                      </div>

                                    </c:if>

                                    <div class="row">
                                      <div class="col-md-6">
                                      Postal Code
                                      </div>
                                      <div class="col-md-6">
                                          ${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].postalCode ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].postalCode}
                                            <wrms:value width="7">
                                        <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].postalCode ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].postalCode}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                              <span class="oldVal compareTdStyle"
                                                    attr="${appGrpPremDto.postalCode}"
                                                    style="display: none"><label><c:out
                                                      value="${appGrpPremDto.postalCode}"/></label></span>
                                            </wrms:value>
                                      </div>

                                    </div>

                                    <div class="row">
                                      <div class="col-md-6">
                                       Address Type
                                      </div>
                                      <div class="col-md-6">
                                        <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].addrType=='ADDTY001'}"> Apt Blk</c:if>
                                        <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].addrType=='ADDTY002'}"> Without Apt Blk</c:if>
                                        <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].addrType== null}">-</c:if>
                                        <wrms:value width="7">
                                        <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].addrType ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].addrType}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                          <span class="oldVal compareTdStyle"
                                                attr="${appGrpPremDto.addrType}"
                                                style="display: none">
                                            <label>
<%--                                              <c:out--%>
<%--                                                  value="${appGrpPremDto.addrType}"/>--%>
                                              <c:if test="${appGrpPremDto.addrType=='ADDTY001'}"> Apt Blk</c:if>
                                              <c:if test="${appGrpPremDto.addrType=='ADDTY002'}"> Without Apt Blk</c:if>
                                            </label>
                                          </span>
                                        </wrms:value>
                                      </div>
                                    </div>


                                    <div class="row">
                                      <div class="col-md-6">
                                        Block / House No.
                                      </div>
                                      <div class="col-md-6">
                                          ${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].blkNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].blkNo}
                                            <wrms:value width="7">
                                        <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].blkNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].blkNo}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                              <span class="oldVal compareTdStyle"
                                                    attr="${appGrpPremDto.blkNo}"
                                                    style="display: none"><label><c:out
                                                      value="${appGrpPremDto.blkNo}"/></label></span>
                                            </wrms:value>
                                      </div>

                                    </div>

                                    <div class="row">
                                      <div class="col-md-6">
                                        Floor No.
                                      </div>
                                      <div class="col-md-6">
                                          ${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].floorNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].floorNo}
                                        <wrms:value width="7">
                                        <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].floorNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].floorNo}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                          <span class="oldVal compareTdStyle"
                                                attr="${appGrpPremDto.floorNo}"
                                                style="display: none"><label><c:out
                                                  value="${appGrpPremDto.floorNo}"/></label></span>
                                        </wrms:value>
                                      </div>
                                    </div>


                                    <div class="row">
                                      <div class="col-md-6">
                                        Unit No.
                                      </div>
                                      <div class="col-md-6">
                                          ${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].unitNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].unitNo}
                                            <wrms:value width="7">
                                        <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].unitNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].unitNo}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                              <span class="oldVal compareTdStyle"
                                                    attr="${appGrpPremDto.unitNo}"
                                                    style="display: none"><label><c:out
                                                      value="${appGrpPremDto.unitNo}"/></label></span>
                                            </wrms:value>
                                      </div>

                                    </div>


                                    <div class="row">
                                      <div class="col-md-6">
                                       Street Name
                                      </div>
                                      <div class="col-md-6">
                                          ${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].streetName ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].streetName}
                                            <wrms:value width="7">
                                        <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].streetName ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].streetName}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                              <span class="oldVal compareTdStyle"
                                                    attr="${appGrpPremDto.streetName}"
                                                    style="display: none"><label><c:out
                                                      value="${appGrpPremDto.streetName}"/></label></span>
                                            </wrms:value>
                                      </div>

                                    </div>
                                    <div class="row">
                                      <div class="col-md-6">
                                       Building Name
                                      </div>
                                      <div class="col-md-6">
                                          ${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].buildingName ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].buildingName}
                                            <wrms:value width="7">
                                        <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].buildingName ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].buildingName}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                              <span class="oldVal compareTdStyle"
                                                    attr="${appGrpPremDto.buildingName}"
                                                    style="display: none"><label><c:out
                                                      value="${appGrpPremDto.buildingName}"/></label></span>
                                            </wrms:value>
                                      </div>
                                    </div>

                                    <c:if test="${'ONSITE'==appGrpPremDto.premisesType}">
                                      <div class="row">
                                        <div class="col-md-6">
                                          Are you co-locating with another licensee ?
                                        </div>
                                        <div class="col-md-6">
                                          No
                                        </div>
                                      </div>
                                      <div class="row">
                                        <div class="col-md-6">
                                         Office Telephone No.
                                        </div>
                                        <div class="col-md-6">
                                            ${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].offTelNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].offTelNo}

                                          <wrms:value width="7">
                                            <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].offTelNo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].offTelNo}"
                                                  style="display: none"><label><c:out value=""/></label></span>
                                            <span class="oldVal compareTdStyle"
                                                  attr="${appGrpPremDto.offTelNo}"
                                                  style="display: none"><label><c:out
                                                    value="${appGrpPremDto.offTelNo}"/></label></span>
                                          </wrms:value>
                                        </div>
                                      </div>

                                    </c:if>

                                    <div class="row">
                                      <div class="col-md-6">
                                        Operating Hours (Start)
                                      </div>
                                      <div class="col-md-6">
<%--                                          ${appGrpPremDto.onsiteStartHH} : ${appGrpPremDto.onsiteStartMM}--%>
                                            <c:if test="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeFrom}">
                                                <c:out value="-"/>
                                            </c:if>
                                            <c:if test="${!empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeFrom}">
                                              <fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeFrom}"
                                                              pattern="HH : mm"></fmt:formatDate>
                                            </c:if>
                                        <wrms:value width="7">
                                        <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeFrom ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeFrom}" style="display: none"><label><c:out
                                                value=""/></label></span>
                                          <span class="oldVal compareTdStyle"
                                                attr="${appGrpPremDto.wrkTimeFrom}"
                                                style="display: none">
                                            <fmt:formatDate value="${appGrpPremDto.wrkTimeFrom}"
                                                            pattern="HH : mm"></fmt:formatDate>
                                          </span>
                                        </wrms:value>
                                      </div>
                                    </div>

                                    <div class="row">
                                      <div class="col-md-6">
                                       Operating Hours (End)
                                      </div>
                                      <div class="col-md-6">
<%--                                          ${appGrpPremDto.onsiteEndHH} : ${appGrpPremDto.onsiteEndMM}--%>
                                            <c:if test="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeTo}">
                                              <c:out value="-"/>
                                            </c:if>
                                            <c:if test="${!empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeTo}">
                                              <fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeTo}"
                                                              pattern="HH : mm"></fmt:formatDate>
                                            </c:if>
                                            <wrms:value width="7">
                                              <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeTo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeTo}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                              <span class="oldVal compareTdStyle"
                                                    attr="${appGrpPremDto.wrkTimeTo}"
                                                    style="display: none">
                                                  <fmt:formatDate value="${appGrpPremDto.wrkTimeTo}"
                                                                  pattern="HH : mm"></fmt:formatDate>
                                              </span>
                                            </wrms:value>
                                      </div>
                                    </div>



                                <c:forEach items="${appGrpPremDto.appPremPhOpenPeriodList}"
                                           var="appPremPhOpenPeriod" varStatus="statu">

                                  <div class="row">
                                    <div class="col-md-6">
                                      Select Public Holiday
                                    </div>
                                    <div class="col-md-6">
                                      ${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].dayName ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].dayName}
                                      <wrms:value width="7">
                                          <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].phDate ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].phDate}" style="display: none"><label><c:out
                                                  value=""/></label></span>
                                        <span class="oldVal compareTdStyle"
                                              attr="${appPremPhOpenPeriod.phDate}"
                                              style="display: none"><label><c:out
                                                value="${appPremPhOpenPeriod.dayName}"/></label></span>
                                      </wrms:value>
                                    </div>
                                  </div>

                                  <div class="row">
                                    <div class="col-md-6">
                                     Public Holidays Operating Hours (Start)
                                    </div>
                                    <div class="col-md-6">
                                      <c:if test="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].convStartFromHH && empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].convStartFromMM}">
                                        <c:out value="-"/>
                                      </c:if>
                                      <c:if test="${!empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].convStartFromHH && !empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].convStartFromMM}">
                                        <c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].convStartFromHH} : ${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].convStartFromMM}"/>
                                      </c:if>
                                          <wrms:value width="7">
                                          <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].convStartFromHH ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].convStartFromHH}" style="display: none"><label><c:out
                                                  value=""/></label></span>
                                            <span class="oldVal compareTdStyle"
                                                  attr="${appPremPhOpenPeriod.convStartFromHH}"
                                                  style="display: none"><label><c:out
                                                    value="${appPremPhOpenPeriod.convStartFromHH} : ${appPremPhOpenPeriod.convStartFromMM}"/></label></span>
                                          </wrms:value>
                                    </div>
                                  </div>

                                  <div class="row">
                                    <div class="col-md-6">
                                      Public Holidays Operating Hours (End)
                                    </div>
                                    <div class="col-md-6">
<%--                                        ${appPremPhOpenPeriod.convEndToHH} : ${appPremPhOpenPeriod.convEndToMM}--%>
                                          <c:if test="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].endTo}">
                                              <c:out value="-"/>
                                          </c:if>
                                          <c:if test="${!empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].endTo}">
                                            <fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].endTo}"
                                                            pattern="HH : mm"></fmt:formatDate>
                                          </c:if>

                                          <wrms:value width="7">
                                          <span class="newVal " attr="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].endTo ? '-' : appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].endTo}"
                                                style="display: none"><label><c:out value=""/></label></span>
                                            <span class="oldVal compareTdStyle"
                                                  attr="${appPremPhOpenPeriod.endTo}"
                                                  style="display: none">
                                              <fmt:formatDate value="${appPremPhOpenPeriod.endTo}"
                                                              pattern="HH : mm"></fmt:formatDate>
                                            </span>
                                          </wrms:value>
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
                                  <input class="form-check-input" id="primaryCheckbox" type="checkbox" name="editCheckbox" aria-invalid="false" value="primary">
                                </c:if>
                              </p>
                              <div class="elemClass-1561088919456">
                                <div  class="page section control  container-s-1" style="margin: 10px 0px">
                                  <div class="control-set-font control-font-header section-header">
                                    <label style="font-size: 2.2rem">Uploaded Documents
                                    </label>
                                  </div>
                                  <div class="pop-up">
                                    <div class="pop-up-body">
                                      <c:forEach var="appGrpPrimaryDocDto" items="${appSubmissionDto.appGrpPrimaryDocDtos}" varStatus="status">
                                        <div class="content-body fileUploadContainer">
                                          <div class="field col-sm-4 control-label formtext"><label>${appGrpPrimaryDocDto.svcComDocName}:</label></div>
                                          <div class="control col-sm-6">
                                            <div class="fileList">
                                              <span class="filename server-site col-xs-6 col-md-12" id="130">
                                                <c:if test="${empty appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName}">
                                                  <c:out value="-"/>
                                                </c:if>
                                                <c:if test="${!empty appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName}">
                                                  <c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName} (${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize} KB)"/>
                                                </c:if>
                                              </span>
                                              <span class="col-xs-6 col-md-6">
                                                <wrms:value width="7">
                                                  <span class="newVal compareTdStyle" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize}${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName}"  style="display: none"><label><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName} (${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize} KB)"/></label></span>
                                                  <span class="oldVal compareTdStyle" attr="${appGrpPrimaryDocDto.docSize}${appGrpPrimaryDocDto.docName}" style="display: none"><label><c:out value="${appGrpPrimaryDocDto.docName} (${appGrpPrimaryDocDto.docSize} KB)"/></label></span>
                                                </wrms:value></span>

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
                                            ${newLicenceDto.uenNo}
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                          Name of Licensee
                                          </div>
                                          <div class="col-md-6">
                                            ${empty oldLicenceDto.name ? '-' : oldLicenceDto.name}
                                              <wrms:value width="7">
                                                  <span class="newVal compareTdStyle" attr="${empty oldLicenceDto.name ? '-' : oldLicenceDto.name}"
                                                        style="display: none"><label><c:out
                                                          value="${oldLicenceDto.name}"/></label></span>
                                                <span class="oldVal compareTdStyle" attr="${newLicenceDto.name}"
                                                      style="display: none"><label><c:out
                                                        value="${newLicenceDto.name}"/></label></span>
                                              </wrms:value>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                           Postal Code
                                          </div>
                                          <div class="col-md-6">
                                            ${empty oldLicenceDto.postalCode ? '-' : oldLicenceDto.postalCode}
                                              <wrms:value width="7">
                                                  <span class="newVal compareTdStyle" attr="${empty oldLicenceDto.postalCode ? '-' : oldLicenceDto.postalCode}"
                                                        style="display: none"><label><c:out
                                                          value="${oldLicenceDto.postalCode}"/></label></span>
                                                <span class="oldVal compareTdStyle" attr="${newLicenceDto.postalCode}"
                                                      style="display: none"><label><c:out
                                                        value="${newLicenceDto.postalCode}"/></label></span>
                                              </wrms:value>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Address Type
                                          </div>
                                          <div class="col-md-6">
                                            ${empty oldLicenceDto.addrType ? '-' : oldLicenceDto.addrType}
                                              <wrms:value width="7">
                                                  <span class="newVal compareTdStyle" attr="${empty oldLicenceDto.addrType ? '-' : oldLicenceDto.addrType}"
                                                        style="display: none"><label><c:out
                                                          value="${oldLicenceDto.addrType}"/></label></span>
                                                <span class="oldVal compareTdStyle" attr="${newLicenceDto.addrType}"
                                                      style="display: none"><label><c:out
                                                        value="${newLicenceDto.addrType}"/></label></span>
                                              </wrms:value>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Blk No.
                                          </div>
                                          <div class="col-md-6">
                                            ${empty oldLicenceDto.unitNo ? '-' : oldLicenceDto.unitNo}
                                              <wrms:value width="7">
                                                  <span class="newVal compareTdStyle" attr="${empty oldLicenceDto.unitNo ? '-' : oldLicenceDto.unitNo}"
                                                        style="display: none"><label><c:out
                                                          value="${oldLicenceDto.unitNo}"/></label></span>
                                                <span class="oldVal compareTdStyle" attr="${newLicenceDto.unitNo}"
                                                      style="display: none"><label><c:out
                                                        value="${newLicenceDto.unitNo}"/></label></span>
                                              </wrms:value>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Floor No.
                                          </div>
                                          <div class="col-md-6">
                                            ${empty oldLicenceDto.floorNo ? '-' : oldLicenceDto.floorNo}
                                              <wrms:value width="7">
                                                  <span class="newVal compareTdStyle" attr="${empty oldLicenceDto.floorNo ? '-' : oldLicenceDto.floorNo}"
                                                        style="display: none"><label><c:out
                                                          value="${oldLicenceDto.floorNo}"/></label></span>
                                                <span class="oldVal compareTdStyle" attr="${newLicenceDto.floorNo}"
                                                      style="display: none"><label><c:out
                                                        value="${newLicenceDto.floorNo}"/></label></span>
                                              </wrms:value>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Unit No.
                                          </div>
                                          <div class="col-md-6">
                                            ${empty oldLicenceDto.unitNo ? '-' : oldLicenceDto.unitNo}
                                              <wrms:value width="7">
                                                  <span class="newVal compareTdStyle" attr="${empty oldLicenceDto.unitNo ? '-' : oldLicenceDto.unitNo}"
                                                        style="display: none"></span>
                                                <span class="oldVal compareTdStyle" attr="${newLicenceDto.unitNo}"
                                                      style="display: none"><label><c:out
                                                        value="${newLicenceDto.unitNo}"/></label></span>
                                              </wrms:value>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Street Name
                                          </div>
                                          <div class="col-md-6">
                                            ${empty oldLicenceDto.streetName ? '-' : oldLicenceDto.streetName}
                                              <wrms:value width="7">
                                                  <span class="newVal compareTdStyle" attr="${empty oldLicenceDto.streetName ? '-' : oldLicenceDto.streetName}"
                                                        style="display: none"><label><c:out
                                                          value="${oldLicenceDto.streetName}"/></label></span>
                                                <span class="oldVal compareTdStyle" attr="${newLicenceDto.streetName}"
                                                      style="display: none"><label><c:out
                                                        value="${newLicenceDto.streetName}"/></label></span>
                                              </wrms:value>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Building Name
                                          </div>
                                          <div class="col-md-6">
                                            ${empty oldLicenceDto.buildingName ? '-' : oldLicenceDto.buildingName}
                                              <wrms:value width="7">
                                                  <span class="newVal compareTdStyle" attr="${empty oldLicenceDto.buildingName ? '-' : oldLicenceDto.buildingName}"
                                                        style="display: none"><label><c:out
                                                          value="${oldLicenceDto.buildingName}"/></label></span>
                                                <span class="oldVal compareTdStyle" attr="${newLicenceDto.buildingName}"
                                                      style="display: none"><label><c:out
                                                        value="${newLicenceDto.buildingName}"/></label></span>
                                              </wrms:value>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Office Telephone No.
                                          </div>
                                          <div class="col-md-6">
                                            ${empty oldLicenceDto.officeTelNo ? '-' : oldLicenceDto.officeTelNo}
                                              <wrms:value width="7">
                                                  <span class="newVal compareTdStyle" attr="${empty oldLicenceDto.officeTelNo ? '-' : oldLicenceDto.officeTelNo}"
                                                        style="display: none"><label><c:out
                                                          value="${oldLicenceDto.officeTelNo}"/></label></span>
                                                <span class="oldVal compareTdStyle" attr="${newLicenceDto.officeTelNo}"
                                                      style="display: none"><label><c:out
                                                        value="${newLicenceDto.officeTelNo}"/></label></span>
                                              </wrms:value>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Office Email Address
                                          </div>
                                          <div class="col-md-6">
                                            ${empty oldLicenceDto.emilAddr ? '-' : oldLicenceDto.emilAddr}
                                              <wrms:value width="7">
                                                  <span class="newVal compareTdStyle" attr="${empty oldLicenceDto.emilAddr ? '-' : oldLicenceDto.emilAddr}"
                                                        style="display: none"><label><c:out
                                                          value="${oldLicenceDto.emilAddr}"/></label></span>
                                                <span class="oldVal compareTdStyle" attr="${newLicenceDto.emilAddr}"
                                                      style="display: none"><label><c:out
                                                        value="${newLicenceDto.emilAddr}"/></label></span>
                                              </wrms:value>
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
            var newEle = $(this).parent().find('.' + newValClass);
            var newVal = newEle.length > 0 ? newEle.attr('attr') : '';
            if (oldVal.length > 0 && newVal.length > 0) {
                if (oldVal != newVal) {
                    $(this).show();
                } else {
                    $(this).hide();
                }
            } else if ($("#view").val() == "") {
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
