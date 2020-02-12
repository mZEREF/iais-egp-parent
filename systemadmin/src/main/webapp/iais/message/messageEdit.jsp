<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<meta http-equiv="Content-Type" content="text/html charset=gb2312">

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<form id = "messageForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>

    <br><br>
    <div class="main-content">
        <div class="container">
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-xs-4 col-md-2 control-label" >Type</label>
                    <div class="col-xs-5 col-md-3">
                        <iais:select name="domainType" options="domainTypeSelect" firstOption="Please select" value="${msgRequestDto.domainType}" ></iais:select>
                        <span id="error_domainType" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-xs-4 col-md-2 control-label" >Message Type</label>
                    <div class="col-xs-5 col-md-3">
                        <iais:select name="msgType" options="msgTypeSelect" firstOption="Please select" value="${msgRequestDto.msgType}" ></iais:select>
                        <span id="error_msgType" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-xs-4 col-md-2 control-label" >Module</label>
                    <div class="col-xs-5 col-md-3">
                        <iais:select name="module" options="moduleTypeSelect" firstOption="Please select" value="${msgRequestDto.module}"></iais:select>
                        <span id="error_module" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </div>


               <div class="form-group">
                    <label class="col-xs-4 col-md-2 control-label" >Description</label>
                    <div class="col-xs-10 col-md-3">
                        <input type="text" name="description" value="${msgRequestDto.description}" />
                        <span id="error_description" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-xs-4 col-md-2 control-label" >Message</label>
                    <div class="col-xs-5 col-md-3">
                        <input type="text" name="message" value="${msgRequestDto.message}" />
                        <span id="error_message" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-xs-4 col-md-2 control-label" >Status</label>
                    <div class="col-xs-5 col-md-3">
                        <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS"
                                     firstOption="Select Status" value="${msgRequestDto.status}" filterValue="CMSTAT002"></iais:select>
                        <span id="error_status" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="application-tab-footer">
        <td>
            <div class="text-right text-center-mobile">

                <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: doEdit('${msgRequestDto.id}');">UPDATE</a>
                <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: doCancel();">Cancel</a>
            </div>

        </td>
    </div>



</form>

<%@include file="/include/validation.jsp"%>
<script type="text/javascript">
    function doEdit(id){
        if(confirm('Are sure you want to edit ? ')){
            SOP.Crud.cfxSubmit("mainForm", "doEdit", id);
        }
    }

    function doCancel(){
        SOP.Crud.cfxSubmit("mainForm","doCancel");
    }

</script>
