<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 1/6/2020
  Time: 12:54 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<style>
</style>

<div class="main-content">
  <form  method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

      <div class="bg-title"><h2>${h2TitleAttr}</h2></div>
      <div>
        <c:choose>
          <c:when test="${configSessionAttr.id != null}">
              You have successfully submit checklist configuration to SYSTEM.
          </c:when>
        </c:choose>
      </div>

    <div class="application-tab-footer">
      <div class="row">
        <div class="col-xs-12 col-sm-6">
          <div class="text-right text-center-mobile">
            <a class="btn btn-primary next"  id="docBack" onclick="doBack();">Back</a>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>

<script>
    function doBack() {
        SOP.Crud.cfxSubmit("mainForm", "doBack");
    }
</script>