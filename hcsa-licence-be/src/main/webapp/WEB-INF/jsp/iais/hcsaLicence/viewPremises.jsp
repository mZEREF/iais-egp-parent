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
                                      <label>Premises ${status.index+1} </label>
                                    </div>
                                  </div>

                                  <div class="row">
                                    <div class="col-md-6">
                                      Premises Type
                                    </div>
                                    <div class="col-md-6">
                                      <div class="col-md-6">
                                        <span class="newVal " attr="${appGrpPremDto.premisesType}">
                                          <c:if test="${appGrpPremDto.premisesType=='OFFSITE'}">
                                            Off-site
                                          </c:if>
                                         <c:if test="${appGrpPremDto.premisesType=='ONSITE'}">
                                            On-site
                                         </c:if>
                                          <c:if test="${appGrpPremDto.premisesType=='CONVEYANCE'}">
                                            Conveyance
                                         </c:if>
                                      </div>
                                      <div class="col-md-6">
                                        <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType}" style="display: none">
                                           <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType=='OFFSITE'}">
                                             Off-site
                                           </c:if>
                                         <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType=='ONSITE'}">
                                           On-site
                                         </c:if>
                                          <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].premisesType=='CONVEYANCE'}">
                                            Conveyance
                                          </c:if>
                                        </span>
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
                                          <span class="newVal " attr="${appGrpPremDto.conveyanceVehicleNo}"><c:out value="${appGrpPremDto.conveyanceVehicleNo}"/></span>
                                        </div>
                                        <div  class="col-md-6">
                                             <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}"
                                                   style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].conveyanceVehicleNo}"/></span>
                                        </div>
                                      </div>
                                    </div>
                                  </c:if>

                                  <c:if test="${'ONSITE'==appGrpPremDto.premisesType}">
                                    <div class="row">
                                      <div class="col-md-6">
                                        Fire Safety & Shelter Bureau Ref No.
                                      </div>
                                      <div class="col-md-6">
                                        <div class="col-md-6">
                                          <span class="newVal " attr="${appGrpPremDto.scdfRefNo}"><c:out value="${appGrpPremDto.scdfRefNo}"/></span>
                                        </div>
                                        <div class="col-md-6">
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

                                    <div class="row">
                                      <div class="col-md-6">
                                      Name of HCI
                                      <a class="btn-tooltip styleguide-tooltip" id="hciNameClick" <c:if test="${empty appGrpPremDto.applicationViewHciNameDtos}">style="display: none" </c:if> data-toggle="tooltip" data-html="true" title="" data-original-title="">i</a>
                                    </div>
                                      <div  class="col-md-7" style="position: absolute;z-index: 100;left: 40%;background-color: #EEEEEE;display: none;" id="hciNameShowOrHidden">
                                        <c:forEach items="${appGrpPremDto.applicationViewHciNameDtos}" var="applicationViewHciNameDtos">
                                          <p>The HCI name is currently used by another licensee</p>
                                          <br>
                                          <table    border="1px" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;text-align: center;background-color: #ffffff">
                                            <tr>
                                              <td  class="col-md-4">Name of Licensee</td>
                                              <td  class="col-md-4">HCI Name</td>
                                              <td  class="col-md-4">Service Name</td>
                                            </tr>
                                            <tr>
                                              <td>${applicationViewHciNameDtos.licensee}</td>
                                              <td>${applicationViewHciNameDtos.hciName}</td>
                                              <td>${applicationViewHciNameDtos.serviceName}</td>
                                            </tr>
                                          </table>
                                        </c:forEach>
                                      </div>

                                      <div class="col-md-6">
                                        <div class="col-md-6">
                                                <span class="newVal " attr="${appGrpPremDto.hciName}"><c:out value="${appGrpPremDto.hciName}"/></span>
                                        </div>
                                        <div class="col-md-6">
                                             <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}"
                                                   style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].hciName}"/></span>

                                        </div>
                                      </div>
                                    </div>
                                  </c:if>

                                  <div class="row">
                                    <div class="col-md-6">
                                      Postal Code
                                      <a class="btn-tooltip styleguide-tooltip" id="addressClick" <c:if test="${empty appGrpPremDto.applicationViewAddress}">style="display: none" </c:if> data-toggle="tooltip" data-html="true" title="" data-original-title="">i</a>
                                    </div>
                                      <div  class="col-md-7" style="position: absolute;z-index: 100;left: 40%;background-color: #EEEEEE;display: none" id="addressShowOrHidden">
                                        <c:forEach items="${appGrpPremDto.applicationViewAddress}" var="applicationViewAddress">
                                          <p>The address of the premises keyed in by applicant is currently used by another licensee</p>
                                          <table   border="1px" style="border-collapse: collapse;border-top: 0px solid #000000 ;padding: 8px;text-align: center;background-color: #ffffff">
                                            <tr>
                                              <td  class="col-md-4">Name of Licensee</td>
                                              <td  class="col-md-4">HCI Name</td>
                                              <td  class="col-md-4">Service Name</td>
                                            </tr>
                                            <tr>
                                              <td>${applicationViewAddress.licensee}</td>
                                              <td>${applicationViewAddress.hciName}</td>
                                              <td>${applicationViewAddress.serviceName}</td>
                                            </tr>
                                          </table>
                                        </c:forEach>
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
                                      Floor No.
                                    </div>
                                    <div class="col-md-6">
                                      <div class="col-md-6">
                                           <span class="newVal " attr="${appGrpPremDto.floorNo}"><c:out value="${appGrpPremDto.floorNo}"/></span>

                                      </div>
                                      <div class="col-md-6">
                                             <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].floorNo}" style="display: none"><c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].floorNo}"/></span>
                                      </div>
                                    </div>
                                  </div>


                                  <div class="row">
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


                                  <div class="row">
                                    <div class="col-md-6">
                                      Street Name
                                    </div>
                                    <div class="col-md-6">
                                      <div class="col-md-6">
                                             <span class="newVal "
                                                   attr="${appGrpPremDto.streetName}"><c:out
                                                     value="${appGrpPremDto.streetName}"/></span>

                                      </div>
                                      <div class="col-md-6">
                                             <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].streetName}"
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
                                            <span class="newVal " attr="${appGrpPremDto.buildingName}"><c:out value="${appGrpPremDto.buildingName}"/></span>
                                      </div>
                                      <div class="col-md-6">
                                               <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].buildingName}"
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

                                  <div class="row">
                                    <div class="col-md-6">
                                      Operating Hours (Start)
                                    </div>
                                    <div class="col-md-6">
                                      <div class="col-md-6">
                                        <c:if test="${appGrpPremDto.wrkTimeFrom==null}">
                                             <span class="newVal "  attr=""></span>
                                        </c:if>
                                        <c:if test="${appGrpPremDto.wrkTimeFrom!=null}">
                                          <span class="newVal "  attr="${appGrpPremDto.wrkTimeFrom}">
                                                    <fmt:formatDate value="${appGrpPremDto.wrkTimeFrom}"
                                                                    pattern="HH : mm"></fmt:formatDate>
                                                </span>
                                        </c:if>
                                      </div>
                                      <div class="col-md-6">
                                        <c:if test="${appSubmissionDto.oldAppSubmissionDto!=null}">
                                          <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeFrom==null}">
                                            <span class=" oldVal" attr="" style="display: none"></span>
                                          </c:if>
                                          <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeFrom!=null}">
                                          <span class=" oldVal" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeFrom}" style="display: none">
                                              <fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeFrom}"
                                                              pattern="HH : mm"></fmt:formatDate></span>
                                          </c:if>
                                        </c:if>
                                      </div>
                                    </div>
                                  </div>

                                  <div class="row">
                                    <div class="col-md-6">
                                      Operating Hours (End)
                                    </div>
                                    <div class="col-md-6">
                                      <div class="col-md-6">
                                        <c:if test="${appGrpPremDto.wrkTimeTo==null}">
                                           <span class="newVal " attr=""></span>
                                        </c:if>
                                        <c:if test="${appGrpPremDto.wrkTimeTo!=null}">
                                           <span class="newVal " attr="${appGrpPremDto.wrkTimeTo}"><fmt:formatDate value="${appGrpPremDto.wrkTimeTo}" pattern="HH : mm"></fmt:formatDate></span>
                                        </c:if>

                                      </div>
                                      <div class="col-md-6">
                                        <c:if test="${appSubmissionDto.oldAppSubmissionDto!=null}">
                                          <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeTo==null}">
                                            <span class="oldVal " attr="" style="display: none"></span>
                                          </c:if>
                                          <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeTo!=null}">
                                           <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeTo}"
                                                 style="display: none"> <fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].wrkTimeTo}"
                                                                                        pattern="HH : mm"></fmt:formatDate></span>
                                          </c:if>
                                        </c:if>
                                      </div>
                                    </div>
                                  </div>
                                  <c:choose>
                                    <c:when test="${!empty appGrpPremDto.appPremPhOpenPeriodList}">
                                      <c:set var="phLength" value="${appGrpPremDto.appPremPhOpenPeriodList.size()-1}"/>
                                    </c:when>
                                    <c:otherwise>
                                      <c:set var="phLength" value="0"/>
                                    </c:otherwise>
                                  </c:choose>
                                  <c:forEach varStatus="statu"  begin="0" end="${phLength}">
                                    <c:set var="appPremPhOpenPeriod" value="${appGrpPremDto.appPremPhOpenPeriodList[statu.index]}"/>
                                    <div class="row">
                                      <div class="col-md-6">
                                        Select Public Holiday
                                      </div>
                                      <div class="col-md-6">
                                        <div class="col-md-6">
                                             <span class="newVal " attr="${appPremPhOpenPeriod.dayName}">
                                               <c:out value="${appPremPhOpenPeriod.dayName}"/></span>
                                        </div>
                                        <div class="col-md-6">
                                           <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].dayName}" style="display: none">
                                             <c:out value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].dayName}"/></span>
                                        </div>
                                      </div>
                                    </div>

                                    <div class="row">
                                      <div class="col-md-6">
                                        Public Holidays Operating Hours (Start)
                                      </div>
                                      <div class="col-md-6">
                                          <div class="col-md-6">
                                             <span class="newVal " attr="${appPremPhOpenPeriod.startFrom}">
                                               <fmt:formatDate value="${appPremPhOpenPeriod.startFrom}" pattern="HH : mm"></fmt:formatDate></span>
                                          </div>
                                            <div class="col-md-6">
                                              <span class="oldVal" attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].startFrom}" style="display: none">
                                              <fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].startFrom}" pattern="HH : mm"></fmt:formatDate>
                                            </div>
                                      </div>
                                    </div>

                                    <div class="row">
                                      <div class="col-md-6">
                                        Public Holidays Operating Hours (End)
                                      </div>
                                      <div class="col-md-6">
                                          <div class="col-md-6">
                                              <span class="newVal " attr="${appPremPhOpenPeriod.endTo}"><fmt:formatDate value="${appPremPhOpenPeriod.endTo}"
                                                                                                                        pattern="HH : mm"></fmt:formatDate>
                                              </span>
                                          </div>
                                          <div class="col-md-6">
                                               <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].endTo}"
                                                     style="display: none"><fmt:formatDate value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index].appPremPhOpenPeriodList[statu.index].endTo}" pattern="HH : mm"></fmt:formatDate></span>
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
                                <c:forEach var="appGrpPrimaryDocDto" items="${appSubmissionDto.appGrpPrimaryDocDtos}" varStatus="status">
                                  <tr>
                                    <td>
                                      <div class="field col-sm-12 control-label formtext"><label>${appGrpPrimaryDocDto.svcComDocName}:</label></div>
                                    </td>
                                  </tr>
                                  <tr>
                                    <td>
                                      <div class="col-xs-6 col-md-6">
                                        <c:if test="${appGrpPrimaryDocDto.docSize!=null}">
                                              <span class="newVal " attr="${appGrpPrimaryDocDto.docSize}${appGrpPrimaryDocDto.docName}">
                                              <a  href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"
                                                     value="${appGrpPrimaryDocDto.fileRepoId}"/>&fileRepoName=${appGrpPrimaryDocDto.docName}" title="Download" class="downloadFile">${appGrpPrimaryDocDto.docName}</a><c:out value="(${appGrpPrimaryDocDto.docSize})KB"/>
                                            </span>
                                        </c:if>
                                        <c:if test="${appGrpPrimaryDocDto.docSize==null}">
                                              <span class="newVal " attr="${appGrpPrimaryDocDto.docSize}${appGrpPrimaryDocDto.docName}">
                                            </span>
                                        </c:if>
                                      </div>
                                      <div class="col-xs-6 col-md-6">
                                        <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize!=null}">
                                              <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize}${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName}"  style="display: none">
                                                <a  href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"
                                                    value="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].fileRepoId}"/>&fileRepoName=${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName}" title="Download" class="downloadFile">${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName}</a><c:out value="(${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize})KB"/>
                                              </span>
                                        </c:if>
                                        <c:if test="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize==null}">
                                              <span class="oldVal " attr="${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docSize}${appSubmissionDto.oldAppSubmissionDto.appGrpPrimaryDocDtos[status.index].docName}"  style="display: none">
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

                                      <div class="row">
                                        <div class="col-md-6">
                                          UEN Number
                                        </div>
                                        <div class="col-md-6">
                                          <div class="col-md-6">
                                            <span>${newLicenceDto.uenNo}
                                            <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                            </span>
                                          </div>
                                        </div>
                                      </div>

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
                                    <c:forEach items="${BoardMember}" var="Board" varStatus="status">
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

                                        <div class="row">
                                          <div class="col-md-6">
                                            ID No.
                                          </div>
                                          <div class="col-md-6">
                                            <div class="col-md-6">
                                              <span class="newVal " attr="${Board.idNo}">${Board.idNo}
                                              <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                              </span>
                                            </div>
                                          </div>
                                        </div>
                                      <div class="row">
                                        <div class="col-xs-12 col-md-12" style="position: absolute;z-index: 100;background-color: #F5F5F5">
                                          <label style="font-weight: normal">The Professional has existing disciplinary records in HERIMS</label><span style="position: absolute;right: 0px;color: black" onclick="closeThis(this)">X</span>
                                          <table   border="1px" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px">
                                            <tr>
                                              <td>Indentification No.</td>
                                              <td>Case No.</td>
                                              <td>Case Type Description</td>
                                              <td>Case Status Description</td>
                                              <td>Offence Description</td>
                                              <td>Outcome Description</td>
                                              <td>Outcome Issue Date</td>
                                              <td>Prosecuton Outcome Description</td>
                                              <td>Created Date</td>
                                              <td>Update Date</td>
                                            </tr>
                                          </table>
                                        </div>
                                      </div>
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

                                    <c:forEach items="${AuthorisedPerson}" var="Authorised" varStatus="status">
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

                                      <div class="row">
                                        <div class="col-md-6">
                                          ID No.
                                        </div>
                                        <div class="col-md-6">
                                          <div  class="col-md-6">
                                            <span  class="newVal " attr="${Authorised.idNumber}">${Authorised.idNumber}
                                             <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                            </span>
                                          </div>
                                        </div>
                                      </div>

                                      <div class="row">
                                        <div class="col-xs-12 col-md-12" style="position: absolute;z-index: 100;background-color: #F5F5F5">
                                          <label style="font-weight: normal">The Professional has existing disciplinary records in HERIMS</label><span style="position: absolute;right: 0px;color: black" onclick="closeThis(this)">X</span>
                                          <table   border="1px" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px">
                                            <tr>
                                              <td>Indentification No.</td>
                                              <td>Case No.</td>
                                              <td>Case Type Description</td>
                                              <td>Case Status Description</td>
                                              <td>Offence Description</td>
                                              <td>Outcome Description</td>
                                              <td>Outcome Issue Date</td>
                                              <td>Prosecuton Outcome Description</td>
                                              <td>Created Date</td>
                                              <td>Update Date</td>
                                            </tr>
                                          </table>
                                        </div>
                                      </div>

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
<script>

    function closeThis(obj){
        $(obj).next("table").attr("style","display: none");
        $(obj).attr("style","display: none");
        $(obj).prev("label").attr("style","display: none");
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
      let jQuery = $('#hciNameShowOrHidden').attr('style');
      if(jQuery.match("display: none")){
          $('#hciNameShowOrHidden').show();
          $('#addressShowOrHidden').hide();
      }else {
          $('#hciNameShowOrHidden').hide();
      }
  });

  $('#addressClick').click(function () {
      let jQuery = $('#addressShowOrHidden').attr('style');
      if(jQuery.match("display: none")){
          $('#addressShowOrHidden').show();
          $('#hciNameShowOrHidden').hide();
      }else {
          $('#addressShowOrHidden').hide();
      }
  });



</script>