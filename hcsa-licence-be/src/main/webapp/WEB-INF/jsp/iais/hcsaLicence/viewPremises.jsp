<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  String webroot=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.BE_CSS_ROOT;
  String webRootCommon = IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT;
%>
<style>
  tr {padding: 8px}
  td {padding: 8px}
</style>
<input style="display: none" value="${NOT_VIEW}" id="view">
<c:set var="appEdit" value="${appEditSelectDto}"/>
<c:set value="${pageAppEditSelectDto}" var="pageEdit"></c:set>
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
                <div class="hidden" id="errorMessage">
                  <iais:error>
                    <div class="error">
                      <h2><iais:message key="PRF_ERR002" escape="true"></iais:message></h2>
                    </div>
                  </iais:error>
                </div>
                <div class="row">
                  <div class="col-xs-12">
                    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                      <div class="panel panel-default">
                        <div class="panel-heading" id="headingPremise" role="tab">
                          <h4 class="panel-title"><a  class="collapsed" role="button" data-toggle="collapse" href="#collapsePremise"
                                                      aria-expanded="true" aria-controls="collapsePremise">Mode of Service Delivery</a>
                          </h4>
                        </div>
                        <div class="panel-collapse collapse" id="collapsePremise" role="tabpanel"
                             aria-labelledby="headingPremise">
                          <div class="panel-body">
                            <p class="text-right">
                              <c:if test="${rfi=='rfi'}">
                                <c:if test="${(appEdit.premisesEdit || appEdit.premisesListEdit)&& canEidtPremise }">
                                  <input class="form-check-input" id="premisesCheckbox" type="checkbox"
                                         name="editCheckbox" <c:if test="${pageEdit.premisesEdit}">checked</c:if> aria-invalid="false" value="premises">
                                </c:if>
                              </c:if>
                            </p>
                            <c:forEach var="appGrpPremDto" items="${appSubmissionDto.appGrpPremisesDtoList}"
                                       varStatus="status">
                              <div class="panel-main-content">
                                <div class="preview-info">
                                  <div class="row">
                                    <div class="col-md-6">
                                      <label>Mode of Service Delivery ${status.index+1} </label>
                                    </div>
                                  </div>

                                  <div class="row">
                                    <div class="col-md-6">
                                      Mode of Service Delivery
                                    </div>
                                    <div class="col-md-6">
                                      <div class="col-md-6">
                                        <span class="newVal " attr="${appGrpPremDto.premisesType}">
                                          <c:if test="${appGrpPremDto.premisesType=='OFFSITE'}">
                                             Off-site
                                          </c:if>
                                         <c:if test="${appGrpPremDto.premisesType=='ONSITE'}">
                                            Premises
                                         </c:if>
                                          <c:if test="${appGrpPremDto.premisesType=='CONVEYANCE'}">
                                             Conveyance
                                         </c:if>
                                          <c:if test="${appGrpPremDto.premisesType=='EASMTS'}">
                                             Conveyance(in a mobile clinic / ambulance)
                                          </c:if>
                                      </div>
                                      <div class="col-md-6">
                                        <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType}" style="display: none">
                                           <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType=='OFFSITE'}">
                                             Off-site
                                           </c:if>
                                         <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType=='ONSITE'}">
                                           Premises
                                         </c:if>
                                          <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType=='CONVEYANCE'}">
                                            Conveyance
                                          </c:if>
                                          <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType=='EASMTS'}">
                                            Conveyance(in a mobile clinic / ambulance)
                                          </c:if>
                                        </span>
                                      </div>
                                    </div>
                                  </div>

                                  <c:if test="${'ONSITE'==appGrpPremDto.premisesType}">
                                    <div class="row">
                                      <div class="col-md-6">
                                        Fire Safety & Shelter Bureau Ref. No.
                                      </div>
                                      <div class="col-md-6">
                                        <div class="col-md-12">
                                          <span class="newVal " attr="${appGrpPremDto.scdfRefNo}"><c:out value="${appGrpPremDto.scdfRefNo}"/></span>
                                          <br>
                                          <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].scdfRefNo}" style="display: none">
                                            <c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].scdfRefNo}"/>
                                          </span>
                                        </div>

                                      </div>
                                    </div>
                                    <div class="row">
                                      <div class="col-md-6">
                                        Fire Safety Certificate Issued Date
                                      </div>
                                      <div class="col-md-6">
                                        <div class="col-md-6">
                                             <span class="newVal " attr="${appGrpPremDto.certIssuedDt}">
                                            <fmt:formatDate value="${appGrpPremDto.certIssuedDt}" pattern="dd/MM/yyyy"/>
                                          </span>
                                        </div>
                                        <div class="col-md-6">
                                          <c:if test="${appSubmissionDto.oldAppSubmissionDto!=null}">
                                             <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].certIssuedDt}"
                                                   style="display: none"><fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].certIssuedDt}" pattern="dd/MM/yyyy"/>
                                                </span>
                                          </c:if>
                                        </div>
                                      </div>
                                    </div>
                                  </c:if>
                                  <div class="row">
                                    <div class="col-md-6">
                                      Name of HCI
                                      <a class="btn-tooltip styleguide-tooltip" id="hciNameClick" <c:if test="${empty appGrpPremDto.applicationViewHciNameDtos}">style="display: none" </c:if> data-toggle="tooltip" data-html="true" title="" data-original-title="">i</a>
                                    </div>
                                    <div  class="col-md-7" style="position: absolute;z-index: 100;left: 40%;background-color: #EEEEEE;display: none;margin-top: 2%;overflow-y: scroll" id="hciNameShowOrHidden">
                                      <p>The  Name of HCI is currently used by another licensee</p>
                                      <br>
                                      <table    border="1px" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;text-align: center;background-color: #ffffff;width: 100%">
                                        <tr>
                                          <td  class="col-md-4">Name of Licensee</td>
                                          <td  class="col-md-4">HCI Name</td>
                                          <td  class="col-md-4">Service Name</td>
                                        </tr>
                                        <c:forEach items="${appGrpPremDto.applicationViewHciNameDtos}" var="applicationViewHciNameDtos">
                                          <tr>
                                            <td>${applicationViewHciNameDtos.licensee}</td>
                                            <td>${applicationViewHciNameDtos.hciName}</td>
                                            <td>${applicationViewHciNameDtos.serviceName}</td>
                                          </tr>
                                        </c:forEach>
                                      </table>

                                    </div>

                                    <div class="col-md-6">
                                      <div class="col-md-12">
                                        <span class="newVal " attr="${appGrpPremDto.hciName}"><c:out value="${appGrpPremDto.hciName}"/></span>
                                        <br>
                                        <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}"
                                              style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}"/></span>
                                      </div>

                                    </div>
                                  </div>
                                  <c:if test="${'CONVEYANCE'==appGrpPremDto.premisesType}">
                                    <div class="row">
                                      <div class="col-md-6">
                                        Vehicle No.
                                      </div>
                                      <div class="col-md-6">
                                        <div  class="col-md-6">
                                          <span class="newVal " attr="${appGrpPremDto.conveyanceVehicleNo}"><c:out value="${appGrpPremDto.conveyanceVehicleNo}"/></span>
                                        </div>
                                        <div  class="col-md-6">
                                             <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}"
                                                   style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}"/></span>
                                        </div>
                                      </div>
                                    </div>
                                  </c:if>
                                  <div class="row">
                                    <div class="col-md-6">
                                      Postal Code
                                      <a class="btn-tooltip styleguide-tooltip" id="addressClick" <c:if test="${empty appGrpPremDto.applicationViewAddress}">style="display: none" </c:if> data-toggle="tooltip" data-html="true" title="" data-original-title="">i</a>
                                    </div>
                                      <div  class="col-md-7"  style="position: absolute;z-index: 100;left: 40%;background-color: #EEEEEE;margin-top:2%;display: none;overflow-y: scroll;" id="addressShowOrHidden">
                                          <p>The address of the mode of service delivery keyed in by applicant is currently used by another licensee</p>
                                          <table   border="1px" style="border-collapse: collapse;border-top: 0px solid #000000 ;padding: 8px;text-align: center;background-color: #ffffff;width: 100%">
                                            <tr>
                                              <td  class="col-md-4">Name of Licensee</td>
                                              <td  class="col-md-4">HCI Name</td>
                                              <td  class="col-md-4">Service Name</td>
                                            </tr>
                                            <c:forEach items="${appGrpPremDto.applicationViewAddress}" var="applicationViewAddress">
                                            <tr>
                                              <td>${applicationViewAddress.licensee}</td>
                                              <td>${applicationViewAddress.hciName}</td>
                                              <td>${applicationViewAddress.serviceName}</td>
                                            </tr>
                                            </c:forEach>
                                          </table>

                                      </div>


                                    <div class="col-md-6">
                                      <div  class="col-md-6">
                                           <span class="newVal " attr="${appGrpPremDto.postalCode}"><c:out value="${appGrpPremDto.postalCode}"/></span>
                                      </div>
                                      <div  class="col-md-6">
                                           <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].postalCode}" style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].postalCode}"/></span>
                                      </div>
                                    </div>
                                  </div>

                                  <div class="row">
                                    <div class="col-md-6">
                                      Address Type
                                    </div>
                                    <div class="col-md-6">
                                      <div class="col-md-6">
                                              <span class="newVal " attr="${appGrpPremDto.addrType}">
                                              <c:if test="${appGrpPremDto.addrType=='ADDTY001'}"> Apt Blk</c:if>
                                              <c:if test="${appGrpPremDto.addrType=='ADDTY002'}"> Without Apt Blk</c:if>

                                              </span>

                                      </div>
                                      <div class="col-md-6">
                                             <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].addrType}" style="display: none">
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
                                           <span class="newVal " attr="${appGrpPremDto.blkNo}"><c:out value="${appGrpPremDto.blkNo}"/></span>
                                      </div>
                                      <div class="col-md-6">
                                          <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].blkNo}" style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].blkNo}"/></span>
                                      </div>
                                    </div>
                                  </div>

                                  <div class="row">
                                    <div class="col-md-6">
                                      Floor / Unit No.
                                    </div>
                                    <div class="col-md-6">
                                      <div class="col-md-6">
                                           <span class="newVal " attr="${appGrpPremDto.floorNo}${appGrpPremDto.unitNo}">
                                             <c:out value="${appGrpPremDto.floorNo}-${appGrpPremDto.unitNo}"/>
                                           </span>

                                      </div>
                                      <div class="col-md-6">
                                             <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].floorNo}${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].unitNo}" style="display: none">
                                               <c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].floorNo}-${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].unitNo}"/>
                                             </span>
                                      </div>
                                    </div>
                                  </div>


                               <%--   <div class="row">
                                    <div class="col-md-6">
                                      Unit No.
                                    </div>
                                    <div class="col-md-6">
                                      <div class="col-md-6"><span class="newVal " attr="${appGrpPremDto.unitNo}"><c:out value="${appGrpPremDto.unitNo}"/></span>

                                      </div>
                                      <div class="col-md-6">
                                              <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].unitNo}"
                                                    style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].unitNo}"/></span>
                                      </div>
                                    </div>
                                  </div>
--%>
                                  <c:forEach items="${appGrpPremDto.appPremisesOperationalUnitDtos}" var="appPremisesOperationalUnitDto" varStatus="unitIndex">
                                    <div class="row">
                                      <div class="col-md-6">

                                      </div>
                                      <div class="col-md-6">
                                        <div class="col-md-6">
                                          <span class="newVal " attr="${appPremisesOperationalUnitDto.floorNo}${appPremisesOperationalUnitDto.unitNo}">
                                            <c:choose>
                                              <c:when test="${appPremisesOperationalUnitDto.floorNo!=null &&  appPremisesOperationalUnitDto.unitNo!=null}">
                                                <c:out value="${appPremisesOperationalUnitDto.floorNo}-${appPremisesOperationalUnitDto.unitNo}"/>
                                              </c:when>
                                              <c:otherwise>
                                                <c:out value="${appPremisesOperationalUnitDto.floorNo}${appPremisesOperationalUnitDto.unitNo}"/>
                                              </c:otherwise>
                                            </c:choose>

                                          </span>
                                        </div>
                                        <div class="col-md-6">
                                          <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremisesOperationalUnitDtos[unitIndex.index].floorNo}${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremisesOperationalUnitDtos[unitIndex.index].unitNo}" style="display: none">
                                            <c:choose>
                                              <c:when test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremisesOperationalUnitDtos[unitIndex.index].floorNo!=null && appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremisesOperationalUnitDtos[unitIndex.index].unitNo!=null}">
                                                <c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremisesOperationalUnitDtos[unitIndex.index].floorNo}-${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremisesOperationalUnitDtos[unitIndex.index].unitNo}"/>
                                              </c:when>
                                              <c:otherwise>
                                                <c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremisesOperationalUnitDtos[unitIndex.index].floorNo}${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremisesOperationalUnitDtos[unitIndex.index].unitNo}"/>
                                              </c:otherwise>
                                            </c:choose>

                                          </span>
                                        </div>
                                      </div>
                                    </div>
                                  </c:forEach>


                                  <div class="row">
                                    <div class="col-md-6">
                                      Street Name
                                    </div>
                                    <div class="col-md-6">
                                      <div class="col-md-12">
                                         <span class="newVal " attr="${appGrpPremDto.streetName}">${appGrpPremDto.streetName}</span>
                                         <br>
                                         <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].streetName}" style="display: none">${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].streetName}</span>
                                      </div>
                                    </div>

                                  </div>
                                  <div class="row">
                                    <div class="col-md-6">
                                      Building Name
                                    </div>
                                    <div class="col-md-6">
                                      <div class="col-md-12">
                                        <span class="newVal " attr="${appGrpPremDto.buildingName}">${appGrpPremDto.buildingName}</span>
                                        <br>
                                        <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].buildingName}" style="display: none">${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].buildingName }</span>
                                      </div>
                                      <div class="col-md-6">

                                      </div>
                                    </div>
                                  </div>

                                  <c:if test="${'ONSITE'==appGrpPremDto.premisesType}">
                                    <div class="row">
                                      <div class="col-md-6">
                                        Are you co-locating with another licensee?
                                      </div>
                                      <div class="col-md-6">
                                        <div class="col-md-6">
                                           <span class="newVal" attr="${appGrpPremDto.locateWithOthers}">
                                            <c:if test="${appGrpPremDto.locateWithOthers=='0'}">
                                              No
                                            </c:if>
                                             <c:if test="${appGrpPremDto.locateWithOthers=='1'}">
                                               Yes
                                             </c:if>
                                          </span>
                                        </div>
                                        <div class="col-md-6">
                                           <span class="oldVal" style="display: none" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].locateWithOthers}">
                                            <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].locateWithOthers=='0'}">
                                              No
                                            </c:if>
                                             <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].locateWithOthers=='1'}">
                                               Yes
                                             </c:if>
                                          </span>
                                        </div>

                                      </div>
                                    </div>
                                    <div class="row">
                                      <div class="col-md-6">
                                        Office Telephone No.
                                      </div>
                                      <div class="col-md-6">
                                        <div class="col-md-6">
                                               <span class="newVal "
                                                     attr="${appGrpPremDto.offTelNo}"><c:out
                                                       value="${appGrpPremDto.offTelNo}"/></span>

                                        </div>
                                        <div class="col-md-6">
                                                <span class="oldVal" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].offTelNo}"
                                                      style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].offTelNo}"/></span>
                                        </div>
                                      </div>
                                    </div>
                                  </c:if>
                                  <c:if test="${appGrpPremDto.premisesType=='OFFSITE' || appGrpPremDto.premisesType=='ONSITE' || appGrpPremDto.premisesType=='CONVEYANCE'}">
                                    <div class="row">
                                      <div class="col-md-6">
                                        Operating Hours
                                      </div>
                                      <div class="col-md-6">
                                        <span></span>
                                      </div>
                                    </div>
                                    <div class="row">
                                      <div class="col-md-6">Weekly</div>
                                      <div class="col-md-6">
                                        <div class="col-md-4"><span>Start</span></div>
                                        <div class="col-md-4"><span>End</span></div>
                                        <div class="col-md-4"><span>24 Hours</span></div>
                                      </div>
                                    </div>
                                    <c:forEach items="${appGrpPremDto.weeklyDtoList}" var="weeklyDto" varStatus="weekSta">
                                      <div class="row">
                                        <div class="col-md-6">
                                          <div class="col-md-12" style="padding: 0px">
                                          <span class="newVal" attr="${weeklyDto.selectValList}">
                                             <c:forEach items="${weeklyDto.selectValList}" var="selectVal" varStatus="in">
                                               <iais:code code="${selectVal}"/><c:if test="${!in.last}">,</c:if>
                                             </c:forEach>
                                          </span>
                                            <br>
                                            <span class="oldVal" style="display: none" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].weeklyDtoList[weekSta.index].selectValList}">
                                            <c:forEach items="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].weeklyDtoList[weekSta.index].selectValList}" var="oldSelectValList" varStatus="in">
                                              <iais:code code="${oldSelectValList}"/><c:if test="${!in.last}">,</c:if>
                                            </c:forEach>
                                          </span>
                                          </div>

                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-4" style="padding-right: 0px">
                                            <span class="newVal" attr="${weeklyDto.startFrom}">
                                              <c:if test="${weeklyDto.selectAllDay}">
                                              </c:if>
                                              <c:if test="${!weeklyDto.selectAllDay}">
                                                <fmt:formatDate value="${weeklyDto.startFrom}" pattern="HH : mm"/>
                                              </c:if>
                                            </span>
                                            <span class="oldVal" style="display: none" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].weeklyDtoList[weekSta.index].startFrom}">
                                              <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].weeklyDtoList[weekSta.index].selectAllDay}">
                                              </c:if>
                                              <c:if test="${!appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].weeklyDtoList[weekSta.index].selectAllDay}">
                                                <fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].weeklyDtoList[weekSta.index].startFrom}" pattern="HH : mm"/>
                                              </c:if>
                                            </span>

                                          </div>
                                          <div class="col-md-4" style="padding-right: 0px">

                                            <span class="newVal" attr="${weeklyDto.endTo}">
                                              <c:if test="${weeklyDto.selectAllDay}">

                                              </c:if>
                                              <c:if test="${!weeklyDto.selectAllDay}">
                                                <fmt:formatDate value="${weeklyDto.endTo}" pattern="HH : mm"/>
                                              </c:if>
                                            </span>
                                            <span class="oldVal" style="display: none" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].weeklyDtoList[weekSta.index].endTo}">
                                               <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].weeklyDtoList[weekSta.index].selectAllDay}">

                                               </c:if>
                                               <c:if test="${!appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].weeklyDtoList[weekSta.index].selectAllDay}">
                                                 <fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].weeklyDtoList[weekSta.index].endTo}" pattern="HH : mm"/>
                                               </c:if>
                                            </span>
                                          </div>
                                          <div class="col-md-4" style="padding-right: 0px">
                                            <div class="col-md-6" style="padding: 0px">
                                            <span class="newVal" attr="${weeklyDto.selectAllDay}">
                                              <c:if test="${weeklyDto.selectAllDay}">
                                                <div class="form-check active">
                                                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                                </div>
                                              </c:if>
                                            </span>
                                            </div>
                                            <div class="col-md-6" style="padding: 0px">
                                            <span class="oldVal" style="display: none" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].weeklyDtoList[weekSta.index].selectAllDay}">
                                              <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].weeklyDtoList[weekSta.index].selectAllDay}">
                                                <div class="form-check active">
                                                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                                </div>
                                              </c:if>
                                            </span>
                                            </div>
                                          </div>
                                        </div>
                                      </div>
                                    </c:forEach>
                                    <div class="row">
                                      <div class="col-md-6">Public Holiday</div>
                                      <div class="col-md-6">
                                        <div class="col-md-4"></div>
                                        <div class="col-md-4"></div>
                                        <div class="col-md-4"></div>
                                      </div>
                                    </div>
                                    <c:forEach items="${appGrpPremDto.phDtoList}" var="op" varStatus="opSta">
                                      <div class="row">
                                        <div class="col-md-6">
                                          <div class="col-md-12" style="padding: 0px">
                                          <span class="newVal" attr="${op.selectValList}">
                                               <c:forEach items="${op.selectValList}" var="phDto" varStatus="in">
                                                 <iais:code code="${phDto}"></iais:code><c:if test="${!in.last}">,</c:if>
                                               </c:forEach>
                                          </span>
                                            <br>
                                            <span class="oldVal" style="display: none" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].phDtoList[opSta.index].selectValList}">
                                              <c:forEach items="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].phDtoList[opSta.index].selectValList}" var="oldPhDtoList" varStatus="in">
                                                <iais:code code="${oldPhDtoList}"/><c:if test="${!in.last}">,</c:if>
                                              </c:forEach>
                                          </span>
                                          </div>

                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-4" style="padding-right: 0px">
                                            <span class="newVal" attr="${op.startFrom}">
                                              <c:if test="${op.selectAllDay}">
                                              </c:if>
                                              <c:if test="${!op.selectAllDay}">
                                                <fmt:formatDate value="${op.startFrom}" pattern="HH : mm"/>
                                              </c:if>
                                            </span>
                                            <span class="oldVal" style="display: none" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].phDtoList[opSta.index].startFrom}">
                                              <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].phDtoList[opSta.index].selectAllDay}">
                                              </c:if>
                                              <c:if test="${!appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].phDtoList[opSta.index].selectAllDay}">
                                                <fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].phDtoList[opSta.index].startFrom}" pattern="HH : mm"/>
                                              </c:if>
                                            </span>

                                          </div>

                                          <div class="col-md-4" style="padding-right: 0px">
                                            <span class="newVal" attr="${op.endTo}">
                                              <c:if test="${op.selectAllDay}">

                                              </c:if>
                                               <c:if test="${!op.selectAllDay}">
                                                 <fmt:formatDate value="${op.endTo}" pattern="HH : mm"/>
                                               </c:if>

                                            </span>
                                            <span class="oldVal" style="display: none" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].phDtoList[opSta.index].endTo}">
                                              <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].phDtoList[opSta.index].selectAllDay}">

                                              </c:if>
                                              <c:if test="${!appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].phDtoList[opSta.index].selectAllDay}">
                                                <fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].phDtoList[opSta.index].endTo}" pattern="HH : mm"/>
                                              </c:if>
                                            </span>

                                          </div>

                                          <div class="col-md-4" style="padding-right: 0px">
                                            <div class="col-md-6" style="padding: 0px" >
                                              <span class="newVal" attr="${op.selectAllDay}">
                                                <c:if test="${op.selectAllDay}">
                                                  <div class="form-check active">
                                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                                  </div>
                                                </c:if>
                                              </span>
                                            </div>
                                            <div class="col-md-6" style="padding: 0px">
                                              <span class="oldVal" style="display: none" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].phDtoList[opSta.index].selectAllDay}">
                                                 <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].phDtoList[opSta.index].selectAllDay}">
                                                   <div class="form-check active">
                                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                                  </div>
                                                 </c:if>
                                              </span>
                                            </div>
                                          </div>
                                        </div>

                                      </div>
                                    </c:forEach>
                                    <div class="row">
                                      <div class="col-md-6">Event</div>
                                      <div class="col-md-6">
                                        <div class="col-md-4"></div>
                                        <div class="col-md-4"></div>
                                        <div class="col-md-4"></div>
                                      </div>
                                    </div>
                                    <c:forEach var="eventDto" items="${appGrpPremDto.eventDtoList}" varStatus="eventSta">
                                      <div class="row">
                                        <div class="col-md-6">
                                          <div class="col-md-12" style="padding: 0px">
                                            <span class="newVal" attr="${eventDto.eventName}">${eventDto.eventName}</span>
                                            <br>
                                            <span class="oldVal" style="display: none" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].eventDtoList[eventSta.index].eventName}">
                                                ${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].eventDtoList[eventSta.index].eventName}
                                            </span>
                                          </div>


                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                            <div class="col-md-6" style="padding: 0px">
                                            <span class="newVal" attr="${eventDto.startDate}">
                                              <fmt:formatDate value="${eventDto.startDate}" pattern="dd/MM/yyyy"/>
                                            </span>
                                              <span class="oldVal" style="display: none" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].eventDtoList[eventSta.index].startDate}">
                                              <fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].eventDtoList[eventSta.index].startDate}" pattern="dd/MM/yyyy"/>
                                            </span>
                                            </div>

                                          </div>
                                          <div class="col-md-6">
                                            <div class="col-md-6" style="padding: 0px">
                                            <span class="newVal" attr="${eventDto.endDate}">
                                                <fmt:formatDate value="${eventDto.endDate}"  pattern="dd/MM/yyyy"/>
                                            </span>
                                              <span class="oldVal" style="display: none" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].eventDtoList[eventSta.index].endDate}">
                                              <fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].eventDtoList[eventSta.index].endDate}" pattern="dd/MM/yyyy"/>
                                            </span>
                                            </div>
                                          </div>
                                        </div>
                                      </div>
                                    </c:forEach>
                                  </c:if>
                                  <c:if test="${appGrpPremDto.premisesType=='EASMTS'}">
                                    <div class="row">
                                      <div class="col-md-6">
                                        For public/in-house use only?
                                      </div>
                                      <div class="col-md-6">
                                          <div class="col-md-6">
                                            <span class="newVal " attr="${appGrpPremDto.easMtsCoLocation}">
                                              <c:if test="${appGrpPremDto.easMtsCoLocation=='0'}">
                                                No
                                              </c:if>
                                                <c:if test="${appGrpPremDto.easMtsCoLocation=='1'}">
                                                  Yes
                                                </c:if>
                                            </span>
                                          </div>
                                          <div class="col-md-6">
                                            <span class="oldVal " style="display: none" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].easMtsCoLocation}">
                                                <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].easMtsCoLocation=='0'}">
                                                  No
                                                </c:if>
                                                 <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].easMtsCoLocation=='1'}">
                                                  Yes
                                                 </c:if>
                                            </span>
                                          </div>
                                      </div>
                                    </div>
                                    <div class="row">
                                      <div class="col-md-6">
                                        Public email
                                      </div>
                                      <div class="col-md-6">
                                          <div class="col-md-12">
                                            <span class="newVal " attr="${appGrpPremDto.easMtsPubEmail}">
                                              <c:out value="${appGrpPremDto.easMtsPubEmail}"></c:out>
                                            </span>
                                            <br>
                                            <span class="oldVal " style="display: none" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].easMtsPubEmail}">
                                                 <c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].easMtsPubEmail}"></c:out>
                                            </span>
                                          </div>
                                      </div>
                                    </div>
                                    <div class="row">
                                      <div class="col-md-6">
                                        Public Hotline
                                      </div>
                                      <div class="col-md-6">
                                        <div class="col-md-6">
                                          <span class="newVal " attr="${appGrpPremDto.easMtsPubHotline}">
                                            <c:out value="${appGrpPremDto.easMtsPubHotline}"></c:out>
                                          </span>
                                        </div>
                                        <div class="col-md-6">
                                          <span class="oldVal " style="display: none" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].easMtsPubHotline}">
                                            <c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].easMtsPubHotline}"></c:out>
                                          </span>
                                        </div>
                                      </div>
                                    </div>
                                    <div class="row">
                                      <div class="col-md-6">
                                        Are you co-locating with another licensee?
                                      </div>
                                      <div class="col-md-6">
                                        <div class="col-md-6">
                                          <span class="newVal " attr="${appGrpPremDto.easMtsCoLocation}">
                                            <c:if test="${appGrpPremDto.easMtsCoLocation=='0'}">No</c:if>
                                            <c:if test="${appGrpPremDto.easMtsCoLocation=='1'}">Yes</c:if>
                                          </span>
                                        </div>
                                        <div class="col-md-6">
                                          <span class="oldVal " style="display: none" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].easMtsCoLocation}">
                                            <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].easMtsCoLocation=='0'}">No</c:if>
                                            <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].easMtsCoLocation=='1'}">Yes</c:if>
                                          </span>
                                        </div>
                                      </div>
                                    </div>
                                  </c:if>
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
                                  <input class="form-check-input" <c:if test="${pageEdit.docEdit}">checked</c:if>  id="primaryCheckbox" type="checkbox" name="editCheckbox" aria-invalid="false" value="primary">
                                </c:if>
                              </c:if>
                            </p>
                            <div class="elemClass-1561088919456">
                              <div  class="page section control  container-s-1" style="margin: 10px 0px">
                                <div class="control-set-font control-font-header section-header">
                                  <label style="font-size: 2.2rem">Uploaded Documents</label>
                                </div>
                                <table class="col-xs-12 col-md-12">
<%--                                <c:forEach var="appGrpPrimaryDocDto" items="${appSubmissionDto.appGrpPrimaryDocDtos}" varStatus="status">
                                  <tr>
                                    <td>
                                      <div class="field col-sm-12 control-label formtext"><label>${appGrpPrimaryDocDto.svcComDocName}:</label></div>
                                    </td>
                                  </tr>
                                  <tr>
                                    <td>
                                      <div class="col-xs-6 col-md-6">
                                        <c:if test="${appGrpPrimaryDocDto.docSize!=null}">
                                              <span class="newVal " attr="${appGrpPrimaryDocDto.md5Code}${appGrpPrimaryDocDto.docName}">
                                              <a hidden href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"
                                                     value="${appGrpPrimaryDocDto.fileRepoId}"/>&fileRepoName=${appGrpPrimaryDocDto.docName}" title="Download" class="downloadFile"><span id="${appGrpPrimaryDocDto.fileRepoId}Down">trueDown</span></a>
                                                <a onclick="doVerifyFileGo('${appGrpPrimaryDocDto.fileRepoId}')">${appGrpPrimaryDocDto.docName}<c:out value="(${appGrpPrimaryDocDto.docSize})KB"/></a>
                                            </span>
                                        </c:if>
                                        <c:if test="${appGrpPrimaryDocDto.docSize==null}">
                                              <span class="newVal " attr="${appGrpPrimaryDocDto.md5Code}${appGrpPrimaryDocDto.docName}">
                                            </span>
                                        </c:if>
                                      </div>
                                      <div class="col-xs-6 col-md-6">
                                        <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize!=null}">
                                              <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].md5Code}${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName}"  style="display: none">
                                                <a  href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"
                                                    value="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].fileRepoId}"/>&fileRepoName=${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName}" title="Download" class="downloadFile">${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName}</a><c:out value="(${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize})KB"/>
                                              </span>
                                        </c:if>
                                        <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize==null}">
                                              <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].md5Code}${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName}"  style="display: none">
                                              </span>
                                        </c:if>

                                      </div>
                                    </td>
                                  </tr>
                                  </c:forEach>--%>
                                  <c:forEach items="${appSubmissionDto.multipleGrpPrimaryDoc}" var="appGrpPrimaryDocDto">
                                    <c:set value="${appSubmissionDto.oldAppSubmissionDto.multipleGrpPrimaryDoc[appGrpPrimaryDocDto.key]}" var="oldAppGrpPrimaryDocDto"></c:set>
                                    <tr>
                                      <td>
                                        <div class="field col-sm-12 control-label formtext"><label>${appGrpPrimaryDocDto.key}:</label></div>
                                      </td>
                                    </tr>
                                    <c:forEach items="${appGrpPrimaryDocDto.value}" var="sinage" varStatus="inx">

                                      <tr>
                                        <td>
                                          <div class="col-xs-6 col-md-6 ">
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
                                          <div class="col-xs-6 col-md-6">
                                            <c:if test="${oldAppGrpPrimaryDocDto[inx.index].docSize!=null}">
                                                 <span class="oldVal " attr="${oldAppGrpPrimaryDocDto[inx.index].md5Code}${oldAppGrpPrimaryDocDto[inx.index].docName}"  style="display: none"
                                                   <iais:downloadLink fileRepoIdName="fileRo${inx.index}" fileRepoId="${oldAppGrpPrimaryDocDto[inx.index].fileRepoId}" docName="${oldAppGrpPrimaryDocDto[inx.index].docName}"/>
                                                   <c:out value="(${oldAppGrpPrimaryDocDto[inx.index].docSize} KB)"/>
                                                 </span>
                                            </c:if>
                                            <c:if test="${oldAppGrpPrimaryDocDto[inx.index].docSize==null}">
                                               <span class="oldVal " attr="${oldAppGrpPrimaryDocDto[inx.index].md5Code}${oldAppGrpPrimaryDocDto[inx.index].docName}"  style="display: none">
                                               </span>
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
                                    <div class="field col-sm-12 control-label formtext">

                                      <div class="row" style="margin-top: 1%;margin-bottom: 1%">
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

                                      <div class="row img-show">
                                        <div class="col-md-6">
                                          UEN Number
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                            <span class="newVal " attr="${newLicenceDto.uenNo}">${newLicenceDto.uenNo}
                                              <c:if test="${empty hashMap[newLicenceDto.uenNo]}">
                                                <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                              </c:if>
                                              <c:if test="${not empty hashMap[newLicenceDto.uenNo]}">
                                                <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNew(this)" width="25" height="25" alt="NETS">
                                              </c:if>
                                            </span>
                                          </div>
                                          <div class="col-md-6">
                                            <span class="oldVal " style="display: none" attr="${oldLicenceDto.uenNo}">
                                              ${oldLicenceDto.uenNo}
                                                   <c:if test="${empty hashMap[oldLicenceDto.uenNo]}">
                                                     <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                                   </c:if>
                                              <c:if test="${not empty hashMap[oldLicenceDto.uenNo]}">
                                                <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNew(this)" width="25" height="25" alt="NETS">
                                              </c:if>
                                            </span>
                                          </div>
                                        </div>
                                      </div>
                                      <c:if test="${not empty hashMap[newLicenceDto.uenNo]}">
                                        <div class="row new-img-show" >
                                          <div class="col-xs-12 col-md-12" style="position: absolute;z-index: 100;background-color: #F5F5F5">
                                            <label style="font-weight: normal">The Professional has existing disciplinary records in HERIMS</label><span style="position: absolute;right: 0px;color: black" onclick="closeThis(this)">X</span>
                                            <table   border="1px" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
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
                                              <c:forEach items="${hashMap[newLicenceDto.uenNo]}" var="map">
                                                <tr>
                                                  <td>${map.identificationNo}</td>
                                                  <td>${map.caseNo}</td>
                                                  <td>${map.caseType}</td>
                                                  <td>Case Status Description</td>
                                                  <td>${map.offenceDesc}</td>
                                                  <td>${map.outcome}</td>
                                                  <td><fmt:formatDate value="${map.issueDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                  <td>${map.prosecutionOutcome}</td>
                                                  <td><fmt:formatDate value="${map.createdDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                  <td><fmt:formatDate value="${map.updatedDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                </tr>
                                              </c:forEach>
                                              <tr></tr>

                                            </table>
                                          </div>
                                        </div>
                                      </c:if>
                                      <c:if test="${not empty hashMap[oldLicenceDto.uenNo]}">
                                        <div class="row old-img-show" >
                                          <div class="col-xs-12 col-md-12" style="position: absolute;z-index: 100;background-color: #F5F5F5">
                                            <label style="font-weight: normal">The Professional has existing disciplinary records in HERIMS</label><span style="position: absolute;right: 0px;color: black" onclick="closeThis(this)">X</span>
                                            <table   border="1px" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
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
                                              <c:forEach items="${hashMap[oldLicenceDto.uenNo]}" var="map">
                                                <tr>
                                                  <td>${map.identificationNo}</td>
                                                  <td>${map.caseNo}</td>
                                                  <td>${map.caseType}</td>
                                                  <td>Case Status Description</td>
                                                  <td>${map.offenceDesc}</td>
                                                  <td>${map.outcome}</td>
                                                  <td><fmt:formatDate value="${map.issueDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                  <td>${map.prosecutionOutcome}</td>
                                                  <td><fmt:formatDate value="${map.createdDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                  <td><fmt:formatDate value="${map.updatedDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                </tr>
                                              </c:forEach>
                                              <tr></tr>

                                            </table>
                                          </div>
                                        </div>
                                      </c:if>
                                      <div class="row">
                                        <div class="col-md-6">
                                          Name of Licensee
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6 ">
                                                <span class="newVal " attr="${newLicenceDto.name}">
                                                  <c:out value="${newLicenceDto.name}"/>
                                                </span>
                                          </div>
                                          <div class="col-md-6">
                                                  <span class="oldVal " attr="${oldLicenceDto.name}" style="display: none"><c:out value="${oldLicenceDto.name}"/></span>
                                          </div>
                                        </div>
                                      </div>

                                      <div class="row">
                                        <div class="col-md-6">
                                          Postal Code
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                                  <span class="newVal " attr="${newLicenceDto.postalCode}"><c:out value="${newLicenceDto.postalCode}"/></span>
                                          </div>
                                          <div class="col-md-6">
                                                 <span class="oldVal " attr="${oldLicenceDto.postalCode}" style="display: none"><c:out value="${oldLicenceDto.postalCode}"/></span>

                                          </div>
                                        </div>
                                      </div>

                                      <div class="row">
                                        <div class="col-md-6">
                                          Address Type
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                                 <span class="newVal " attr="${newLicenceDto.addrType}"><c:out value="${newLicenceDto.addrType}"/></span>

                                          </div>
                                          <div class="col-md-6">
                                                 <span class="oldVal" attr="${oldLicenceDto.addrType}" style="display: none"><c:out value="${oldLicenceDto.addrType}"/></span>
                                          </div>
                                        </div>
                                      </div>

                                      <div class="row">
                                        <div class="col-md-6">
                                          Blk No.
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                                 <span class="newVal " attr="${newLicenceDto.unitNo}"><c:out value="${newLicenceDto.unitNo}"/></span>

                                          </div>
                                          <div class="col-md-6">
                                                    <span class="oldVal " attr="${oldLicenceDto.unitNo}" style="display: none"><c:out value="${oldLicenceDto.unitNo}"/></span>
                                          </div>
                                        </div>
                                      </div>

                                      <div class="row">
                                        <div class="col-md-6">
                                          Floor No.
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                                 <span class="newVal " attr="${newLicenceDto.floorNo}"><c:out value="${newLicenceDto.floorNo}"/></span>

                                          </div>
                                          <div class="col-md-6">
                                                   <span class="oldVal " attr="${oldLicenceDto.floorNo}" style="display: none"><c:out value="${oldLicenceDto.floorNo}"/></span>
                                          </div>
                                        </div>
                                      </div>

                                      <div class="row">
                                        <div class="col-md-6">
                                          Unit No.
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                                   <span class="newVal " attr="${newLicenceDto.unitNo}"><c:out value="${newLicenceDto.unitNo}"/></span>
                                          </div>
                                          <div class="col-md-6">
                                                 <span class="oldVal " attr="${oldLicenceDto.unitNo}" style="display: none">${oldLicenceDto.unitNo}</span>

                                          </div>
                                        </div>
                                      </div>

                                      <div class="row">
                                        <div class="col-md-6">
                                          Street Name
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                                   <span class="newVal " attr="${newLicenceDto.streetName}"><c:out value="${newLicenceDto.streetName}"/></span>
                                          </div>
                                          <div class="col-md-6">
                                                 <span class="oldVal " attr="${oldLicenceDto.streetName}" style="display: none"><c:out value="${oldLicenceDto.streetName}"/></span>

                                          </div>
                                        </div>
                                      </div>

                                      <div class="row">
                                        <div class="col-md-6">
                                          Building Name
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                                  <span class="newVal " attr="${newLicenceDto.buildingName}"><c:out value="${newLicenceDto.buildingName}"/></span>
                                          </div>
                                          <div class="col-md-6">
                                                  <span class="oldVal " attr="${oldLicenceDto.buildingName}" style="display: none"><c:out value="${oldLicenceDto.buildingName}"/></span>

                                          </div>
                                        </div>
                                      </div>

                                      <div class="row">
                                        <div class="col-md-6">
                                          Office Telephone No.
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                                  <span class="newVal " attr="${newLicenceDto.officeTelNo}"><c:out value="${newLicenceDto.officeTelNo}"/></span>
                                          </div>
                                          <div class="col-md-6">
                                                 <span class="oldVal " attr="${oldLicenceDto.officeTelNo}" style="display: none"><c:out value="${oldLicenceDto.officeTelNo}"/></span>

                                          </div>
                                        </div>
                                      </div>

                                      <div class="row">
                                        <div class="col-md-6">
                                          Office Email Address
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                                  <span class="newVal " attr="${newLicenceDto.emilAddr}"><c:out value="${newLicenceDto.emilAddr}"/></span>
                                          </div>
                                          <div class="col-md-6">
                                                 <span class="oldVal " attr="${oldLicenceDto.emilAddr}" style="display: none"><c:out value="${oldLicenceDto.emilAddr}"/></span>

                                          </div>
                                        </div>
                                      </div>
                                    <c:forEach items="${appSubmissionDto.boardMember}" var="Board" varStatus="status">
                                      <div class="row" style="margin-top: 1%;margin-bottom: 1%">
                                      <div class="col-md-6"><label>Board Member ${status.index+1}</label></div>
                                      </div>
                                      <div class="row">
                                        <div class="col-md-6">
                                          Salutation
                                        </div>
                                        <div class="col-md-6">
                                          <span class="newVal " attr="${Board.salutation}"><c:out value="${Board.salutation}"/></span>
                                        </div>
                                      </div>


                                      <div class="row">
                                        <div class="col-md-6">
                                          Name
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                            <span class="newVal " attr="${Board.name}">${Board.name}</span>
                                          </div>
                                        </div>
                                      </div>

                                        <div class="row img-show">
                                          <div class="col-md-6">
                                            ID No.
                                          </div>
                                          <div class="col-md-6">
                                            <div class="col-md-6">
                                              <span class="newVal " attr="${Board.idNo}">${Board.idNo}
                                          <%--      <c:if test="${empty hashMap[Board.idNo]}">--%>
                                                  <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                            <%--    </c:if>
                                                <c:if test="${not empty hashMap[Board.idNo]}">
                                                  <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNew(this)" width="25" height="25" alt="NETS">
                                                </c:if>--%>
                                              </span>
                                            </div>
                                          </div>
                                        </div>
                                 <%--     <c:if test="${not empty hashMap[Board.idNo]}">
                                        <div class="row new-img-show" style="display: none">
                                          <div  style="position: absolute;z-index: 100;background-color: #F5F5F5;width: 140%">
                                            <label style="font-weight: normal">The Professional has existing disciplinary records in HERIMS</label><span style="position: absolute;right: 0px;color: black" onclick="closeThis(this)">X</span>
                                            <table   border="1px" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
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
                                              <c:forEach items="${hashMap[Board.idNo]}" var="map">
                                                <tr>
                                                  <td>${map.identificationNo}</td>
                                                  <td>${map.caseNo}</td>
                                                  <td>${map.caseType}</td>
                                                  <td>${map.caseStatus}</td>
                                                  <td>${map.offenceDesc}</td>
                                                  <td>${map.outcome}</td>
                                                  <td><fmt:formatDate value="${map.issueDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                  <td>${map.prosecutionOutcome}</td>
                                                  <td><fmt:formatDate value="${map.createdDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                  <td><fmt:formatDate value="${map.updatedDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                </tr>
                                              </c:forEach>
                                              <tr></tr>

                                            </table>
                                          </div>
                                        </div>
                                      </c:if>--%>
                                        <div class="row">
                                          <div class="col-md-6">
                                            Designation
                                          </div>
                                          <div class="col-md-6">
                                            <div class="col-md-6">
                                              <span  class="newVal " attr="${Board.designation}"><iais:code code="${Board.designation}"/></span>
                                            </div>
                                          </div>
                                        </div>


                                    </c:forEach>

                                    <c:forEach items="${appSubmissionDto.authorisedPerson}" var="Authorised" varStatus="status">
                                      <div class="row" style="margin-top: 1%;margin-bottom: 1%">
                                        <div class="col-md-6">
                                          <label>Authorised Person ${status.index+1}</label>
                                        </div>
                                      </div>

                                      <div class="row">
                                        <div class="col-md-6">
                                          Name
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                            <span  class="newVal " attr="${Authorised.displayName}">${Authorised.displayName}</span>
                                          </div>
                                        </div>
                                      </div>

                                      <div class="row img-show">
                                        <div class="col-md-6">
                                          ID No.
                                        </div>
                                        <div class="col-md-6">
                                          <div  class="col-md-6">
                                            <span  class="newVal " attr="${Authorised.idNumber}">${Authorised.idNumber}
                                            <%--  <c:if test="${empty hashMap[Authorised.idNumber]}">--%>
                                                <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                             <%-- </c:if>
                                              <c:if test="${not empty hashMap[Authorised.idNumber]}">
                                                <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNew(this)" width="25" height="25" alt="NETS">
                                              </c:if>--%>
                                            </span>
                                          </div>
                                        </div>
                                      </div>
                                     <%-- <c:if test="${not empty hashMap[Authorised.idNumber]}">
                                        <div class="row new-img-show" style="display:none;">
                                          <div style="position: absolute;z-index: 100;background-color: #F5F5F5;width: 140%">
                                            <label style="font-weight: normal">The Professional has existing disciplinary records in HERIMS</label><span style="position: absolute;right: 0px;color: black" onclick="closeThis(this)">X</span>
                                            <table   border="1px" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
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
                                              <c:forEach items="${hashMap[Authorised.idNumber]}" var="map">
                                                <tr>
                                                  <td>${map.identificationNo}</td>
                                                  <td>${map.caseNo}</td>
                                                  <td>${map.caseType}</td>
                                                  <td>${map.caseStatus}</td>
                                                  <td>${map.offenceDesc}</td>
                                                  <td>${map.outcome}</td>
                                                  <td><fmt:formatDate value="${map.issueDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                  <td>${map.prosecutionOutcome}</td>
                                                  <td><fmt:formatDate value="${map.createdDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                  <td><fmt:formatDate value="${map.updatedDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                </tr>
                                              </c:forEach>
                                              <tr></tr>

                                            </table>
                                          </div>
                                        </div>
                                      </c:if>--%>


                                      <div class="row">
                                        <div class="col-md-6">
                                          Designation
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                            <span  class="newVal " attr="${Authorised.designation}"><iais:code code="${Authorised.designation}"/></span>
                                          </div>
                                        </div>
                                      </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Office Telephone
                                          </div>
                                          <div class="col-md-6">
                                            <div  class="col-md-6">
                                              <span  class="newVal " attr="${Authorised.officeTelNo}">${Authorised.officeTelNo}</span>
                                            </div>
                                          </div>
                                        </div>

                                        <div class="row">
                                          <div class="col-md-6">
                                            Mobile No.
                                          </div>
                                          <div class="col-md-6">
                                            <div class="col-md-6">
                                              <span class="newVal" attr="${Authorised.mobileNo}">${Authorised.mobileNo}</span>
                                            </div>
                                          </div>
                                        </div>


                                        <div class="row">
                                          <div class="col-md-6">
                                            Email Address
                                          </div>
                                          <div class="col-md-6">
                                            <div class="col-md-6">
                                              <span class="newVal " attr="${Authorised.email}">${Authorised.email}</span>
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
                                  <input class="form-check-input" <c:if test="${pageEdit.serviceEdit}">checked</c:if> id="serviceCheckbox" type="checkbox" name="editCheckbox" aria-invalid="false" value="service">
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
                      <c:choose>
                        <c:when test="${appSubmissionDto.appType == 'APTY005'&&RFC_HCAI_NAME_CHNAGE!='false'}">

                        </c:when>
                        <c:when test="${isSingle=='N'}">

                        </c:when>
                        <c:otherwise>
                          <div class="panel panel-default svc-content">

                            <div class="panel-heading" id="declarations" role="tab">
                              <h4 class="panel-title"><a class="svc-pannel-collapse collapsed" role="button" data-toggle="collapse"
                                                         href="#collapseDeclarations" aria-expanded="true"
                                                         aria-controls="declarations">Declarations
                              </a></h4>
                            </div>
                            <div class=" panel-collapse collapse" id="collapseDeclarations" role="tabpanel"
                                 aria-labelledby="headingServiceInfo0">
                              <div class="panel-body">
                                <%@include file="declarations.jsp"%>
                              </div>
                            </div>

                          </div>
                        </c:otherwise>
                      </c:choose>

                    </div>
                  </div>
                </div>
              </div>
              <iais:confirm msg="GENERAL_ACK018"  needCancel="false" callBack="tagConfirmCallbacksupport()" popupOrder="support" ></iais:confirm>
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
<script>

    function closeThis(obj){
       $(obj).closest('div.row').attr("style","display:none");
    }
  $(document).ready(function () {
      <c:if test="${pageAppEditSelectDto.docEdit}">
      $('#primaryCheckbox').closest("div.panel-body").attr("style","");
      </c:if>
      <c:if test="${pageAppEditSelectDto.premisesEdit}">
      $('#premisesCheckbox').closest("div.panel-body").attr("style","");
      </c:if>
      <c:if test="${pageAppEditSelectDto.serviceEdit}">
      $('#serviceCheckbox').closest("div.panel-body").attr("style","");
      </c:if>
  });

  $('#hciNameClick').click(function () {
      var jQuery = $('#hciNameShowOrHidden').attr('style');
      if(jQuery.match("display: none")){
          var a= $('#hciNameShowOrHidden').height();
          var b= $('.panel-body').height();
          if(a>b){
              $('#hciNameShowOrHidden').height(b)
          }else {
              $('#hciNameShowOrHidden').height(a)
          }
          $('#hciNameShowOrHidden').show();
          $('#addressShowOrHidden').hide();
      }else {
          $('#hciNameShowOrHidden').hide();
      }
  });

  $('#addressClick').click(function () {
      var jQuery = $('#addressShowOrHidden').attr('style');
      if(jQuery.match("display: none")){
        var a= $('#addressShowOrHidden').height();
        var b= $('.panel-body').height();
        if(a>b){
            $('#addressShowOrHidden').height(b)
        }else {
            $('#addressShowOrHidden').height(a)
        }
          $('#addressShowOrHidden').show();
          $('#hciNameShowOrHidden').hide();
      }else {
          $('#addressShowOrHidden').hide();
      }
  });


    function showThisTableNew(obj) {
        $(obj).closest('div.img-show').next("div.new-img-show").removeAttr("style");
    }
    function showThisTableOld(obj) {
        $(obj).closest('div.img-show').next("div.old-img-show").removeAttr("style");
    }
    function doVerifyFileGo(verify) {
        showWaiting();
        var data = {"repoId":verify};
        $.post(
            "${pageContext.request.contextPath}/verifyFileExist",
            data,
            function (data) {
                if(data != null ){
                    if(data.verify == 'N'){
                        $('#support').modal('show');
                    }else {
                        $("#"+verify+"Down").click();
                    }
                    dismissWaiting();
                }
            }
        )
    }

    function tagConfirmCallbacksupport(){
        $('#support').modal('hide');
    }
</script>