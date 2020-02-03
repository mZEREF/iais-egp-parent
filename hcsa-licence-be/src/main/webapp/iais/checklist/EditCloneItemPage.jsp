<%--
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
      <div class="form-group">
        <div class="col-xs-5 col-md-3">
          <iais:field value="Checklist Item" required="true"></iais:field>
          <div class="col-xs-5 col-md-3">
            <input type="text" name="checklistItem" value="${itemRequestAttr.checklistItem}"/>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-xs-5 col-md-3">
          <iais:field value="Risk Level" required="true"></iais:field>
          <div class="col-xs-5 col-md-3">
            <iais:select name="riskLevel" id="riskLevel" codeCategory="CATE_ID_RISK_LEVEL"
                         firstOption="Select Risk Level" value="${itemRequestAttr.riskLevel}"></iais:select>
          </div>
        </div>
      </div>


      <div class="form-group">
        <div class="col-xs-5 col-md-3">
          <iais:field value="Answer Type" required="true"></iais:field>
          <div class="col-xs-5 col-md-3">
            <iais:select name="answerType" id="answerType" codeCategory="CATE_ID_ANSWER_TYPE"
                         firstOption="Select Answer Type" value="${itemRequestAttr.answerType}"></iais:select>
          </div>
        </div>
      </div>
    </div>

    <div class="col-xs-12 col-sm-6">
      <p><a class="back" onclick="doCancel();"><em class="fa fa-angle-left"></em> Back</a></p>
    </div>
    <div class="text-right text-center-mobile">
      <a class="btn btn-primary next" href="javascript:void(0);" onclick="Utils.clearClickStatus();">Clear</a>
      <a class="btn btn-primary next" onclick="javascript:editCloneItem();">Edit</a>
    </div>
</div>

</div>

</div>


</form>
</div>

<script src="/hcsa-licence-web/iais/js/CommonUtils.js"></script>
<script type="text/javascript">
    function editCloneItem() {
        SOP.Crud.cfxSubmit("mainForm", "editCloneItem");
    }

    function doCancel() {
        SOP.Crud.cfxSubmit("mainForm", "doCancel");
    }

</script>