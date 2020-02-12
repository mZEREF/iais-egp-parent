<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<meta http-equiv="Content-Type" content="text/html charset=gb2312">

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <div class="bg-title"><h2>Message Preview</h2></div>
        <div class="form-horizontal">
          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Type</label>
            <div class="col-xs-5 col-md-3" >
              <iais:select name="domainType"  disabled="true" options="domainTypeSelect" firstOption="Please select" value="${msgRequestDto.domainType}" ></iais:select>
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Message Type</label>
            <div class="col-xs-5 col-md-3">
              <iais:select name="msgType" disabled="true" options="msgTypeSelect" firstOption="Please select" value="${msgRequestDto.msgType}" ></iais:select>
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Module</label>
            <div class="col-xs-5 col-md-3">
              <iais:select name="module" disabled="true" options="moduleTypeSelect" firstOption="Please select" value="${msgRequestDto.module}"></iais:select>
            </div>
          </div>


          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Description</label>
            <div class="col-xs-10 col-md-3">
              <input type="text" name="description" disabled="disabled" value="${msgRequestDto.description}" />
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Message</label>
            <div class="col-xs-5 col-md-3">
              <input type="text" name="message" disabled="disabled" value="${msgRequestDto.message}" />
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label" >Status</label>
            <div class="col-xs-5 col-md-3">
              <iais:select name="status" id="status" disabled="true" codeCategory="CATE_ID_COMMON_STATUS"
                           firstOption="Select Status" value="${msgRequestDto.status}" filterValue="CMSTAT002"></iais:select>
            </div>
          </div>


    </div>

    <div class="text-right text-center-mobile">
      <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript:doConfirm();">Submit</a>
      <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript:doCancel();">Cancel</a>
    </div>

  </form>
</div>


<%@include file="/include/validation.jsp"%>
<script type="text/javascript">
    function doConfirm(id){
       SOP.Crud.cfxSubmit("mainForm", "editSubmit");
    }

    function doCancel(){
        SOP.Crud.cfxSubmit("mainForm","doCancel");
    }

</script>
