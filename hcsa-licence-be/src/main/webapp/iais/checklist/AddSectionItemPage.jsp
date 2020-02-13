<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/12/2019
  Time: 1:12 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<webui:setLayout name="iais-intranet"/>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">

  <span id="error_sectionName" name="iaisErrorMsg" class="error-msg"></span>
  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="instruction-content center-content">

            <h2>Add Section Item</h2>
              <div class="gray-content-box">
                <div class="form-horizontal">
                  <div class="form-group">
                    <label class="col-xs-12 col-md-3 control-label" >Section Name</label>
                    <div class="col-xs-12 col-md-2">
                      <input type="text" name="section" value="" />
                    </div>
                  </div>
                </div>
                <div class="form-horizontal">
                  <div class="form-group">
                    <label class="col-xs-12 col-md-3 control-label">Section Description</label>
                    <div class="col-xs-12 col-md-5">
                      <input type="text" name="sectionDesc" value="" />
                    </div>
                  </div>
                </div>
              </div>

            <div class="application-tab-footer">
              <div class="row">
                <div class="col-xs-12 col-sm-6">
                </div>
                <div class="col-xs-12 col-sm-6">
                  <div class="text-right text-center-mobile">
                    <a class="btn btn-primary next" onclick="javascript:doBack();">Cancel</a>
                    <a class="btn btn-primary next" onclick="javascript:addSectionItem();">Add Section Item</a>
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
<%@include file="/include/validation.jsp"%>
<script type="text/javascript">
    function addSectionItem() {
        SOP.Crud.cfxSubmit("mainForm","addSectionItem");
    }

    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backLastPage");
    }
</script>