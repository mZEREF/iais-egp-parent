<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
  String webrootBe=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>

<div class="main-content">
  <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" id="action" name="action" value=""/>
    <input type="hidden" id="unlockType" name="unlockType" value=""/>
    <input type="hidden" id="unlockSuccessFlag" name="unlockSuccessFlag" value="${unlockSuccessFlag}"/>
    <div class="col-lg-12 col-xs-12">
      <div class="center-content">
        <div class="intranet-content">
          <div class="row form-horizontal">
            <div class="bg-title">
              <h2>Assisted Reproduction Submission Management</h2>
            </div>
            <div class="col-xs-12 col-md-12">
              <iais:row>
                <iais:field width="4" value="AR Centre"/>
                <iais:value width="4" cssClass="col-md-4" display="true">
                  <iais:select name="arCenterFilter" options="arMgrCenterOptsAttr"
                               firstOption="Please Select" needSort="true"
                               value="${arMgrSearchParam.params['arCenterFilter']}"></iais:select>
                </iais:value>
              </iais:row>
              <iais:row>
                <iais:field width="4" value="Submission ID"/>
                <iais:value width="4" cssClass="col-md-4" display="true">
                  <input type="text" name="submissionNoFilter" id="submissionNoFilter" value="${arMgrSearchParam.params['submissionNoFilter']}" maxlength="19"/>
                </iais:value>
              </iais:row>
              <iais:row>
                <iais:field width="4" value="Submission Type"/>
                <iais:value width="4" cssClass="col-md-4">
                  <input type="text" disabled="disabled" value="Cycle Stages"/>
                </iais:value>
                <iais:value width="4" cssClass="col-md-4">
                  <c:if test="${!empty arMgrSearchParam.params['allCycleStageFilter']}">
                    <iais:select name="cycleStageFilter" id="cycleStageFilter" firstOption="Please Select" options="arMgrStageOptsAttr" needSort="true"
                                 value="All" cssClass="clearSel" />
                  </c:if>
                  <c:if test="${empty arMgrSearchParam.params['allCycleStageFilter']}">
                    <iais:select name="cycleStageFilter" id="cycleStageFilter" firstOption="Please Select" options="arMgrStageOptsAttr" needSort="true"
                                 value="${arMgrSearchParam.params['cycleStageFilter']}" cssClass="clearSel" />
                  </c:if>
                </iais:value>
              </iais:row>
              <iais:row>
                <iais:field width="4" value="Submission Date Range"/>
                <iais:value width="4" cssClass="col-md-4">
                  <iais:datePicker id="submitDateFromFilter" name="submitDateFromFilter" value="${arMgrSearchParam.params['submitDateFromFilter']}"/>
                </iais:value>
                <iais:value width="4" cssClass="col-md-4">
                  <iais:datePicker id="submitDateToFilter" name="submitDateToFilter" value="${arMgrSearchParam.params['submitDateToFilter']}"/>
                </iais:value>
              </iais:row>
              <iais:action style="text-align:right;">
                <button class="btn btn-secondary" type="button" id="clearBtn" name="clearBtn">Clear</button>
                <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">Search</button>
              </iais:action>
            </div>
          </div>
            <br>
            <br>
            <h3>
              <span>Search Results</span>
            </h3>
            <iais:pagination param="arMgrSearchParam" result="arMgrSearchResult"/>
            <div class="table-gp">
              <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                <thead>
                <tr>
                  <th scope="col" style="display: none"></th>
                  <iais:sortableHeader needSort="false" field="" value=" "></iais:sortableHeader>                  <iais:sortableHeader needSort="false" field="NAME" value="Patient Name" param="arMgrSearchParam"></iais:sortableHeader>
                  <iais:sortableHeader needSort="false" field="ID_TYPE_DESC" value="Patient ID Type" param="arMgrSearchParam"></iais:sortableHeader>
                  <iais:sortableHeader needSort="false" field="ID_NUMBER" value="Patient ID No" param="arMgrSearchParam"></iais:sortableHeader>
                  <iais:sortableHeader needSort="false" field="DATE_OF_BIRTH" value="Patient Date of Birth" param="arMgrSearchParam"></iais:sortableHeader>
                  <iais:sortableHeader needSort="false" field="NATIONALITY_DESC" value="Patient Nationality" param="arMgrSearchParam"></iais:sortableHeader>
                </tr>
                </thead>
                <c:choose>
                  <c:when test="${empty arMgrSearchResult.rows}">
                    <tr>
                      <td colspan="6">
                        <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                      </td>
                    </tr>
                  </c:when>
                  <c:otherwise>
                    <c:forEach var="patient" items="${arMgrSearchResult.rows}" varStatus="status">
                      <tr style="display: table-row;" id="advfilter${(status.index + 1) + (arMgrSearchParam.pageNo - 1) * arMgrSearchParam.pageSize}">
                        <td><input type="checkbox" id="patientId${status.index + 1}" name="patientId" value="${patient.patientCode}" onclick="javascript:chooseAllcheckBox('${status.index + 1}');" style="display:none"/></td>
                        <td><p style="white-space: nowrap;"><c:out value="${patient.patientName}"/>
                          <c:if test="${not empty patient.patientCode}">
                            <a href="javascript:void(0);" class="accordion-toggle  collapsed" style="float: right;color: #2199E8" data-toggle="collapse" data-target="#dropdown${(status.index + 1) + (arMgrSearchParam.pageNo - 1) * arMgrSearchParam.pageSize}" onclick="getPatientByPatientCode('${patient.patientCode}','${(status.index + 1) + (arMgrSearchParam.pageNo - 1) * arMgrSearchParam.pageSize}')">
                            </a>
                          </c:if>
                        </p></td>
                        <td><c:out value="${patient.idTypeDesc}"/></td>
                        <td><c:out value="${patient.idNo}"/></td>
                        <td><fmt:formatDate value='${patient.birthday}' pattern='dd/MM/yyyy' /></td>
                        <td><c:out value="${patient.nationalityDesc}"/></td>
                      </tr>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
              </table>
            </div>
          <iais:action>
            <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
            <div class="text-right">
              <a class="btn btn-primary btn-support" id="unlockBtn">Unlock</a>
            </div>
          </iais:action>
          </div>
      </div>
    </div>
<%--    <iais:confirm msg="DS_ACK052" needFungDuoJi="true" needCancel="true" yesBtnDesc="Unlock Entire Cycle" cancelBtnDesc="Unlock Stage Only" cancelFunc="submitUnlockStage()" callBack="submitUnlockCycle()" popupOrder="unlockConfirm" ></iais:confirm>--%>
    <div class="modal fade" id="unlockConfirm" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog modal-dialog-centered" role="document" style="max-width: 600px">
        <div class="modal-content">
          <div class="modal-body" >
            <div class="row">
              <div class="col-md-12" ><span style="font-size: 2rem;"><iais:message key="DS_ACK052"/></span></div>
            </div>
          </div>
          <div class="row " style="margin-top: 5%;margin-bottom: 5%">
            <button type="button" id="unlockStgBtn" class="btn btn-secondary col-md-6" data-dismiss="modal" onclick="submitUnlockStage()">Unlock Stage Only</button>
            <button type="button" id="unlockCycBtn" class="next btn btn-primary col-md-6" data-dismiss="modal" onclick="submitUnlockCycle()">Unlock Entire Cycle</button>
          </div>
        </div>
      </div>
    </div>
    <iais:confirm msg="DS_ACK053" callBack="$('#unlockSucConfirm').modal('hide');" needCancel="false" popupOrder="unlockSucConfirm"/>
  </form>
</div>

<%@ include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript" src="<%=webrootBe%>js/datasubmission/arSubmitManage.js"></script>