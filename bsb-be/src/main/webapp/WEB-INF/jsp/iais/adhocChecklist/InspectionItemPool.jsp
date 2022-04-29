<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<webui:setLayout name="iais-intranet"/>
<style>
  .column-sort {
    margin-top: 3px;
  }
</style>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%--@elvariable id="checklistItemResult" type="com.ecquaria.cloud.moh.iais.common.dto.SearchResult<com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto>"--%>
<%--@elvariable id="checklistItemSearch" type="com.ecquaria.cloud.moh.iais.common.dto.SearchParam"--%>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-adhoc-checklist.js"></script>
<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="currentValidateId" value="">

    <span id="error_checklistItem" name="iaisErrorMsg" class="error-msg"></span>
    <br><br><br>
    <div class="components">
      <iais:pagination param="checklistItemSearch" result="checklistItemResult"/>
      <div class="table-gp">
        <table aria-describedby="" class="table">
          <thead>
          <tr>
            <th scope="col" style="display: none"></th>
            <iais:sortableHeader needSort="false" field="" value="No."/>
            <iais:sortableHeader needSort="false" field="" value=" "/>
            <iais:sortableHeader needSort="false" field="" value="S/N"/>
            <iais:sortableHeader needSort="true" field="CHECKLISTITEM" value="Item Description"/>
            <iais:sortableHeader needSort="true" field="RISK_LEVEL" value="Risk Level"/>
          </tr>
          </thead>
          <tbody>
          <c:choose>
            <c:when test="${empty checklistItemResult.rows}">
              <tr>
                <td colspan="6">
                  <iais:message key="GENERAL_ACK018" escape="true"/>
                </td>
              </tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="item" items="${checklistItemResult.rows}" varStatus="status">
                <tr>
                  <td class="row_no">${(status.index + 1) + (checklistItemSearch.pageNo - 1) * checklistItemSearch.pageSize}</td>
                  <td><input name="itemCheckbox" id="itemCheckbox" type="checkbox" value="${item.itemId}"/></td>
                  <td class="sn_no">
                    1.${(status.index + 1) + (checklistItemSearch.pageNo - 1) * checklistItemSearch.pageSize}</td>
                  <td style="width: 30%">${item.checklistItem}</td>
                  <td style="width: 20%"><iais:code code="${item.riskLevel}"/></td>
                </tr>
              </c:forEach>
            </c:otherwise>
          </c:choose>
          </tbody>
        </table>
      </div>


      <div class="row">
        <div class="col-xs-12 col-sm-12">
          <div class="text-right text-center-mobile">
            <a id="cancelBtn" class="btn btn-secondary" href="#">Cancel</a>
            <a id="customAdhocBtn" class="btn btn-secondary" href="#">Custom Adhoc Item</a>
            <a id="addAdhocBtn" class="btn btn-primary" href="#">Add to Adhoc Section</a>
          </div>
        </div>
      </div>

    </div>
  </form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
