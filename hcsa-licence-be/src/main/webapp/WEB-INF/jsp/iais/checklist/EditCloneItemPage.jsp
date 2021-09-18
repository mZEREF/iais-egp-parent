<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %><%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 10/14/2019
  Time: 1:46 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<webui:setLayout name="iais-intranet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<style>
  .form-check-gp {
    width: 50%;
    float: left;
  }

  .col-md-3 {
    width: 50%;
  }
</style>


<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" name="itemId"
    value="<iais:mask name="itemId" value="${itemRequestAttr.itemId}"/><%--don't remove--%>">

    <br><br>

    <div class="form-horizontal">
      <iais:row>
        <iais:field value="Checklist Item" required="true"></iais:field>
        <div class="col-xs-5 col-md-3">
          <textarea cols="70" rows="7" name="checklistItem" id="checklistItem" maxlength="500"><c:out value="${itemRequestAttr.checklistItem}"> </c:out></textarea>
          <span id="error_checklistItem" name="iaisErrorMsg" class="error-msg"></span>
        </div>
      </iais:row>

      <iais:row>
        <iais:field value="Risk Level" required="true"></iais:field>
        <div class="col-xs-5 col-md-3">
          <iais:select name="riskLevel" id="riskLevel" codeCategory="CATE_ID_RISK_LEVEL"
                       firstOption="Please Select" value="${itemRequestAttr.riskLevel}"></iais:select>
        </div>
      </iais:row>

      <iais:row>
        <iais:field value="Answer Type" required="true"></iais:field>
          <div class="col-xs-5 col-md-3">
            <iais:select name="answerType" id="answerType" codeCategory="CATE_ID_ANSWER_TYPE"
                         firstOption="Please Select" filterValue="ANTP003, ANTP001" value="${itemRequestAttr.answerType}"></iais:select>
          </div>
      </iais:row>

      <iais:action>
        <div class="col-xs-12 col-sm-6">
          <a href="#" class="back" onclick="doCancel();"><em class="fa fa-angle-left"></em> Back</a>
        </div>
        <div class="text-right text-center-mobile">
          <a class="btn btn-primary next" href="javascript:void(0);" onclick="Utils.clearClickStatus('form-horizontal');">Clear</a>
          <a class="btn btn-primary next" onclick="javascript:editCloneItem();">Save</a>
        </div>
      </iais:action>
    </div>
  </div>
</div>


</form>
</div>

<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<script type="text/javascript">
    function editCloneItem() {
        SOP.Crud.cfxSubmit("mainForm", "editCloneItem");
    }

    function doCancel() {
        SOP.Crud.cfxSubmit("mainForm", "doCancel");
    }

</script>