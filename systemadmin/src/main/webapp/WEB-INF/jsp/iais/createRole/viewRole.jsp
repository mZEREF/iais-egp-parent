<%--
  Created by IntelliJ IDEA.
  User: yaoxia
  Date: 2019/12/23
  Time: 15:49
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<webui:setLayout name="iais-intranet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <div class="form-group">
      <label class="col-xs-12 col-md-4 control-label"><h2>Role ID</h2></label>
      <div class="col-xs-8 col-sm-6 col-md-5">
        <input id="roleID" size="30" type="text" value="" name="roleID" disabled="disabled">
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-12 col-md-4 control-label"><h2>user domain</h2></label>
      <div class="col-xs-8 col-sm-6 col-md-5">
        <input  size="30" type="text" value="" name="roleName" disabled="disabled">
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-12 col-md-4 control-label"><h2>Role Name</h2></label>
      <div class="col-xs-8 col-sm-6 col-md-5">
        <input id="roleName" size="30" type="text" value="" name="roleName" disabled="disabled">
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-12 col-md-4 control-label"><h2>remakes</h2></label>
      <div class="col-xs-8 col-sm-6 col-md-5">
        <input id="remakes" size="30" type="text" value="" name="remakes" disabled="disabled">
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-12 col-md-4 control-label"><h2>tags</h2></label>
      <div class="col-xs-8 col-sm-6 col-md-5">
        <input id="tag" size="30" type="text" value="" name="roleTag"  disabled="disabled">
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-12 col-md-4 control-label"><h2>isSystem</h2></label>
      <div class="col-xs-8 col-sm-6 col-md-5">
        <input  size="30" type="text" value="" name="isSystem"  disabled="disabled">
      </div>
    </div>
    <div class="form-group">
      <div class="col-md-6">
      </div>
      <div class="col-md-2">
        <div class="components">
          <a class="btn btn-secondary" data-toggle="collapse" data-target="#advfilter" id="cancel">cancel</a>
        </div>
      </div>
      <div class="col-md-2">
        <div class="components">
          <a class="btn btn-secondary" data-toggle="collapse" data-target="#advfilter" id="save">save</a>
        </div>
      </div>
      <div class="col-md-1">
        <div class="components">
          <a class="btn btn-secondary" data-toggle="collapse" data-target="#advfilter" id="clear">clear</a>
        </div>
      </div>

    </div>

  </form>

</div>

<script type="text/javascript">
    $("#clear").click(function () {
        $('#remakes').val('');
        $('#roleName').val('');
        $('#tag').val('');
    });

    $("#save").click(function () {

        SOP.Crud.cfxSubmit("mainForm", "saveRole");

    });
    $("#cancel").click(function () {
        SOP.Crud.cfxSubmit("mainForm", "cancel");

    });


</script>
</>