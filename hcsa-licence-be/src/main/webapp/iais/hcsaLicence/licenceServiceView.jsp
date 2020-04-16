<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
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
                            <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapsePremise"
                                                       aria-expanded="true" aria-controls="collapsePremise">Premises</a>
                            </h4>
                          </div>
                          <div class="panel-collapse collapse in" id="collapsePremise" role="tabpanel"
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
                                        <c:if test="${appGrpPremDto.premisesType=='ONSITE'}">On-site</c:if>
                                        <c:if test="${appGrpPremDto.premisesType=='CONVEYANCE'}">Conveyance</c:if>
                                        <c:if test="${appGrpPremDto.premisesType=='OFFSIET'}">Off-site</c:if>
                                        <wrms:value width="7">
                                        <span class="newVal " attr="${appGrpPremDto.premisesType}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                          <span class="oldVal compareTdStyle"
                                                attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType}"
                                                style="display: none"><label><c:out
                                                  value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType}"/></label></span>
                                        </wrms:value>
                                      </div>
                                    </div>


                                    <c:if test="${'CONVEYANCE'==appGrpPremDto.premisesType}">
                                      <div class="row">
                                        <div class="col-md-6">
                                         Vehicle No
                                        </div>
                                        <div class="col-md-6">
                                            ${appGrpPremDto.conveyanceVehicleNo}
                                              <wrms:value width="7">
                                          <span class="newVal " attr="${appGrpPremDto.conveyanceVehicleNo}"
                                                style="display: none"><label><c:out value=""/></label></span>
                                                <span class="oldVal compareTdStyle"
                                                      attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}"
                                                      style="display: none"><label><c:out
                                                        value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}"/></label></span>
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
                                            ${appGrpPremDto.certIssuedDt}
                                        </div>
                                        <wrms:value width="7">
                                          <span class="newVal " attr="${appGrpPremDto.certIssuedDt}"
                                                style="display: none"><label><c:out value=""/></label></span>
                                          <span class="oldVal compareTdStyle"
                                                attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].certIssuedDt}"
                                                style="display: none"><label><c:out
                                                  value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].certIssuedDt}"/></label></span>
                                        </wrms:value>
                                      </div>
                                      <div class="row">
                                        <div class="col-md-6">
                                         Fire Safety Shelter Bureau Ref. No.
                                        </div>
                                        <div class="col-md-6">
                                            ${appGrpPremDto.scdfRefNo}
                                        </div>
                                        <wrms:value width="7">
                                          <span class="newVal " attr="${appGrpPremDto.scdfRefNo}"
                                                style="display: none"><label><c:out value=""/></label></span>
                                          <span class="oldVal compareTdStyle"
                                                attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].scdfRefNo}"
                                                style="display: none"><label><c:out
                                                  value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].scdfRefNo}"/></label></span>
                                        </wrms:value>
                                      </div>
                                      <div class="row">
                                        <div class="col-md-6">
                                         Name of HCI
                                        </div>
                                        <div class="col-md-6">
                                            ${appGrpPremDto.hciName}
                                        </div>
                                        <wrms:value width="7">
                                          <span class="newVal " attr="${appGrpPremDto.hciName}"
                                                style="display: none"><label><c:out value=""/></label></span>
                                          <span class="oldVal compareTdStyle"
                                                attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}"
                                                style="display: none"><label><c:out
                                                  value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}"/></label></span>
                                        </wrms:value>
                                      </div>

                                    </c:if>

                                    <div class="row">
                                      <div class="col-md-6">
                                      Postal Code
                                      </div>
                                      <div class="col-md-6">
                                          ${appGrpPremDto.postalCode}
                                      </div>
                                      <wrms:value width="7">
                                        <span class="newVal " attr="${appGrpPremDto.postalCode}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                        <span class="oldVal compareTdStyle"
                                              attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].postalCode}"
                                              style="display: none"><label><c:out
                                                value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].postalCode}"/></label></span>
                                      </wrms:value>
                                    </div>

                                    <div class="row">
                                      <div class="col-md-6">
                                       Address Type
                                      </div>
                                      <div class="col-md-6">
                                        <c:if test="${appGrpPremDto.addrType=='ADDTY001'}"> Apt Blk</c:if>
                                        <c:if test="${appGrpPremDto.addrType=='ADDTY002'}"> Without Apt Blk</c:if>
                                        <wrms:value width="7">
                                        <span class="newVal " attr="${appGrpPremDto.addrType}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                          <span class="oldVal compareTdStyle"
                                                attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].addrType}"
                                                style="display: none"><label><c:out
                                                  value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].addrType}"/></label></span>
                                        </wrms:value>
                                      </div>
                                    </div>


                                    <div class="row">
                                      <div class="col-md-6">
                                        Block / House No.
                                      </div>
                                      <div class="col-md-6">
                                          ${appGrpPremDto.blkNo}
                                      </div>
                                      <wrms:value width="7">
                                        <span class="newVal " attr="${appGrpPremDto.blkNo}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                        <span class="oldVal compareTdStyle"
                                              attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].blkNo}"
                                              style="display: none"><label><c:out
                                                value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].blkNo}"/></label></span>
                                      </wrms:value>
                                    </div>

                                    <div class="row">
                                      <div class="col-md-6">
                                        Floor No.
                                      </div>
                                      <div class="col-md-6">
                                          ${appGrpPremDto.floorNo}
                                        <wrms:value width="7">
                                        <span class="newVal " attr="${appGrpPremDto.floorNo}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                          <span class="oldVal compareTdStyle"
                                                attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].floorNo}"
                                                style="display: none"><label><c:out
                                                  value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].floorNo}"/></label></span>
                                        </wrms:value>
                                      </div>
                                    </div>


                                    <div class="row">
                                      <div class="col-md-6">
                                        Unit No.
                                      </div>
                                      <div class="col-md-6">
                                          ${appGrpPremDto.unitNo}
                                      </div>
                                      <wrms:value width="7">
                                        <span class="newVal " attr="${appGrpPremDto.unitNo}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                        <span class="oldVal compareTdStyle"
                                              attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].unitNo}"
                                              style="display: none"><label><c:out
                                                value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].unitNo}"/></label></span>
                                      </wrms:value>
                                    </div>


                                    <div class="row">
                                      <div class="col-md-6">
                                       Street Name
                                      </div>
                                      <div class="col-md-6">
                                          ${appGrpPremDto.streetName}
                                            <wrms:value width="7">
                                        <span class="newVal " attr="${appGrpPremDto.streetName}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                              <span class="oldVal compareTdStyle"
                                                    attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].streetName}"
                                                    style="display: none"><label><c:out
                                                      value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].streetName}"/></label></span>
                                            </wrms:value>
                                      </div>

                                    </div>
                                    <div class="row">
                                      <div class="col-md-6">
                                       Building Name
                                      </div>
                                      <div class="col-md-6">
                                          ${appGrpPremDto.buildingName}
                                            <wrms:value width="7">
                                        <span class="newVal " attr="${appGrpPremDto.buildingName}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                              <span class="oldVal compareTdStyle"
                                                    attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].buildingName}"
                                                    style="display: none"><label><c:out
                                                      value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].buildingName}"/></label></span>
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
                                            ${appGrpPremDto.offTelNo}
                                        </div>
                                        <wrms:value width="7">
                                          <span class="newVal " attr="${appGrpPremDto.offTelNo}"
                                                style="display: none"><label><c:out value=""/></label></span>
                                          <span class="oldVal compareTdStyle"
                                                attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].offTelNo}"
                                                style="display: none"><label><c:out
                                                  value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].offTelNo}"/></label></span>
                                        </wrms:value>
                                      </div>

                                    </c:if>

                                    <div class="row">
                                      <div class="col-md-6">
                                        Operating Hours (Start)
                                      </div>
                                      <div class="col-md-6">
                                          ${appGrpPremDto.onsiteStartHH}: ${appGrpPremDto.onsiteStartMM}
                                        <wrms:value width="7">
                                        <span class="newVal " attr="${appGrpPremDto.wrkTimeFrom}" style="display: none"><label><c:out
                                                value=""/></label></span>
                                          <span class="oldVal compareTdStyle"
                                                attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeFrom}"
                                                style="display: none"><label><c:out
                                                  value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeFrom}"/></label></span>
                                        </wrms:value>
                                      </div>
                                    </div>

                                    <div class="row">
                                      <div class="col-md-6">
                                       Operating Hours (End)
                                      </div>
                                      <div class="col-md-6">
                                          ${appGrpPremDto.onsiteEndHH}: ${appGrpPremDto.onsiteEndMM}
                                            <wrms:value width="7">
                                              <span class="newVal " attr="${appGrpPremDto.wrkTimeTo}"
                                              style="display: none"><label><c:out value=""/></label></span>
                                              <span class="oldVal compareTdStyle"
                                                    attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeTo}"
                                                    style="display: none"><label><c:out
                                                      value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeTo}"/></label></span>
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
                                        ${appPremPhOpenPeriod.dayName}
                                      <wrms:value width="7">
                                          <span class="newVal " attr="${appPremPhOpenPeriod.phDate}" style="display: none"><label><c:out
                                                  value=""/></label></span>
                                        <span class="oldVal compareTdStyle"
                                              attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].phDate}"
                                              style="display: none"><label><c:out
                                                value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].dayName}"/></label></span>
                                      </wrms:value>
                                    </div>
                                  </div>

                                  <div class="row">
                                    <div class="col-md-6">
                                     Public Holidays Operating Hours (Start)
                                    </div>
                                    <div class="col-md-6">
                                        ${appPremPhOpenPeriod.convStartFromHH}:  ${appPremPhOpenPeriod.convStartFromMM}
                                          <wrms:value width="7">
                                          <span class="newVal " attr="${appPremPhOpenPeriod.convStartFromHH}" style="display: none"><label><c:out
                                                  value=""/></label></span>
                                            <span class="oldVal compareTdStyle"
                                                  attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].convStartFromHH}"
                                                  style="display: none"><label><c:out
                                                    value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].convStartFromHH}"/></label></span>
                                          </wrms:value>
                                    </div>
                                  </div>

                                  <div class="row">
                                    <div class="col-md-6">
                                      Public Holidays Operating Hours (End)
                                    </div>
                                    <div class="col-md-6">
                                        ${appPremPhOpenPeriod.convEndToHH}:  ${appPremPhOpenPeriod.convEndToMM}

                                          <wrms:value width="7">
                                          <span class="newVal " attr="${appPremPhOpenPeriod.endTo}"
                                                style="display: none"><label><c:out value=""/></label></span>
                                            <span class="oldVal compareTdStyle"
                                                  attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].endTo}"
                                                  style="display: none"><label><c:out
                                                    value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].endTo}"/></label></span>
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
                          <div class="panel-heading" id="headingOne" role="tab">
                            <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapseOne"
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
                                            ${newLicenceDto.name}
                                              <wrms:value width="7">
                                                  <span class="newVal compareTdStyle" attr="${newLicenceDto.name}"
                                                        style="display: none"><label><c:out
                                                          value="${newLicenceDto.name}"/></label></span>
                                                <span class="oldVal compareTdStyle" attr="${oldLicenceDto.name}"
                                                      style="display: none"><label><c:out
                                                        value="${oldLicenceDto.name}"/></label></span>
                                              </wrms:value>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                           Postal Code
                                          </div>
                                          <div class="col-md-6">
                                            ${newLicenceDto.postalCode}
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Address Type
                                          </div>
                                          <div class="col-md-6">
                                            ${newLicenceDto.addrType}
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Blk No.
                                          </div>
                                          <div class="col-md-6">
                                            ${newLicenceDto.unitNo}
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Floor No.
                                          </div>
                                          <div class="col-md-6">
                                            ${newLicenceDto.floorNo}
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Unit No.
                                          </div>
                                          <div class="col-md-6">
                                            ${newLicenceDto.unitNo}
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Street Name
                                          </div>
                                          <div class="col-md-6">
                                            ${newLicenceDto.streetName}
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Building Name
                                          </div>
                                          <div class="col-md-6">
                                            ${newLicenceDto.buildingName}
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Office Telephone No.
                                          </div>
                                          <div class="col-md-6">
                                            ${newLicenceDto.officeTelNo}
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Office Email Address
                                          </div>
                                          <div class="col-md-6">
                                            ${newLicenceDto.emilAddr}
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
                                           Email Address
                                          </div>
                                          <div class="col-md-6">
                                            modelan@gmail.com
                                          </div>
                                        </div>
                                        <div class="row">
                                          <div class="col-md-6">
                                            <label>Authorised Person 2</label>
                                          </div>
                                          <div class="col-md-6">
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
                            <h4 class="panel-title"><a class="svc-pannel-collapse" role="button" data-toggle="collapse"
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
                              <%@include file="/iais/hcsaLicence/viewForm.jsp"%>
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
  .compareTdStyle {
    display: inline;
    padding: .2em .6em .3em;
    line-height: 1;
    color: #000;
    text-align: center;
    vertical-align: baseline;
    border-radius: .5em;
    font-size: 100%;
    background-color: #FF0
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
