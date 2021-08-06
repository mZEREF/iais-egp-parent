<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 12/11/2019
  Time: 1:51 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>


<style>
  .column-sort{
    margin-top: 3px;
  }
</style>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<div class="main-content">
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
  <input type="hidden" name="currentValidateId" value="">
  <span id="error_checklistItem" name="iaisErrorMsg" class="error-msg"></span>
  <br><br><br>


  <div class="components">
    <iais:pagination  param="checklistItemSearch" result="checklistItemResult"/>
    <div class="table-gp">
      <table aria-describedby="" class="table">
        <thead>
        <tr>
          <th scope="col" style="display: none"></th>
          <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
          <iais:sortableHeader needSort="false"  field="" value=" "></iais:sortableHeader>
          <iais:sortableHeader needSort="true"   field="CLAUSE_NO" value="Regulation Clause Number"></iais:sortableHeader>
          <iais:sortableHeader needSort="true"   field="CLAUSE" value="Regulations"></iais:sortableHeader>
          <iais:sortableHeader needSort="true"   field="CHECKLISTITEM" value="Checklist Item"></iais:sortableHeader>
          <iais:sortableHeader needSort="true"   field="RISK_LEVEL" value="Risk Level"></iais:sortableHeader>
        </tr>
        </thead>
        <tbody>
        <c:choose>
          <c:when test="${empty checklistItemResult.rows}">
            <tr>
              <td colspan="6">
                <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
              </td>
            </tr>
          </c:when>
          <c:otherwise>
            <c:forEach var = "item" items = "${checklistItemResult.rows}" varStatus="status">
              <tr>
                <td class="row_no">${(status.index + 1) + (checklistItemSearch.pageNo - 1) * checklistItemSearch.pageSize}</td>
                <td><input name="itemCheckbox" id="itemCheckbox" type="checkbox" value="${item.itemId}" /></td>
                <td style="width: 20%">${item.regulationClauseNo}</td>
                <td style="width: 40%">${item.regulationClause}</td>
                <td style="width: 30%" >${item.checklistItem}</td>
                <td style="width: 20%"><iais:code code="${item.riskLevel}"></iais:code></td>
              </tr>
            </c:forEach>
          </c:otherwise>


        </c:choose>
        </tbody>
      </table>

    </div>


    <div class="table-footnote">
      <div class="row">
        <div class="col-xs-6 col-md-4">

        </div>
        <div class="col-xs-6 col-md-8 text-right">
          <div class="nav">
            <ul class="pagination"></ul>
            <br><br><br>
            <p></p>
            <div class="text-right text-center-mobile">
              <a    onclick="doCancel()" class="btn btn-secondary">Cancel</a>
              <a  id="customAdhocItembtnId"  class="btn btn-primary custom">Custom Adhoc Item</a>
              <a  id="adhocSectionbtnId" class="btn btn-primary addToSection">Add to Adhoc Section</a>
            </div>
          </div>



        </div>
      </div>



    </div>

  </div>

</form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<script>
    "use strict";
    customAdhocItembtnId.onclick = function(){
        SOP.Crud.cfxSubmit("mainForm", "customItem");
    }

    adhocSectionbtnId.onclick = function(){
        SOP.Crud.cfxSubmit("mainForm", "appendToTail");
    }

    function doCancel(){
        SOP.Crud.cfxSubmit("mainForm", "doCancel");
    }

    function sortRecords(sortFieldName,sortType){
        SOP.Crud.cfxSubmit("mainForm","doSort",sortFieldName,sortType);
    }

    function jumpToPagechangePage(){
        SOP.Crud.cfxSubmit("mainForm","changePage");
    }

</script>
