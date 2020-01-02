<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="egov-cloud" uri="ecquaria/sop/egov-cloud" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <br><br><br>
        <div class="container">
            <div class="col-xs-12">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br>
                    <div class="panel panel-default">
                        <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                            <div class="panel-body">
                                <div class="panel-main-content">
                                    <iais:section title="" id = "supPoolList">
                                        <iais:row>
                                            <iais:field value="Title :"/>
                                            <iais:value width="18">
                                                <label>
                                                    <span>${licPreReqForInfoDto.officerRemarks}</span>
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence No :"/>
                                            <iais:value width="18">
                                                <label>
                                                    <span>${licPreReqForInfoDto.licenceNo}</span>
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Due Date :"/>
                                            <iais:value width="18">
                                                <iais:datePicker  name = "Due_date" dateVal="${licPreReqForInfoDto.dueDateSubmission}"></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="checkbox" name="reqType" checked/>Information
                                                </label>
                                                <label>
                                                    <input type="checkbox" name="reqType" />Supporting Documents
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <H3></H3>
                                        <iais:row style="text-align:center;">
                                            <iais:value width="18">
                                                <label>
                                                    <span>${licPreReqForInfoDto.officerRemarks}</span>
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row style="text-align:center;">
                                            <iais:value width="18">
                                                <label>
                                                    <textarea name="userReply" rows="10" cols="100">${licPreReqForInfoDto.userReply}</textarea>
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <c:if test="${licPreReqForInfoDto.needDocument}">
                                            <iais:row>
                                                <iais:value width="18">
                                                    <span class="compose_toolbtn qmEditorAttach dragAndDropTrap_box">
                                                        <span sizelimit="50" title="Add files less than 50M as attachments" id="AttachFrame" onmouseover="getTop().addClass(getTop().finds('a',this)[0],'underline');" onmouseout="getTop().rmClass(getTop().finds('a',this)[0],'underline');" onmousedown="getTop().LogKV('compose|toolbar|entrance|attach');" style="position: relative;">
                                                            <span style="top: 0px; left: 0px; position: absolute; cursor: pointer; width: 66px; height: 16px; overflow: hidden; background-color: rgb(255, 255, 255); zoom: 1; opacity: 0; z-index: 1;">
                                                                <input type="file" title=" " name="UploadFile" multiple="" style="font-family: Times; position: absolute; cursor: pointer; width: 2000px; height: 600px; right: 0px;">
                                                            </span>
                                                            <a class="compose_toolbtn_text ico_att" onclick="return false;" onmousedown="getTop().LogKV('compose|toolbar|entrance|attach');return false;" hidefocus="">
                                                                <span id="sAddAtt1">Attachment</span>
                                                            </a>
                                                        </span>
                                                        <a class="ico_moreupload" id="moreupload"></a>
                                                    </span>
                                                </iais:value>
                                            </iais:row>
                                        </c:if>
                                        <iais:action style="text-align:center;">
                                            <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doBack()">Back</button>
                                            <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doSubmit('${licPreReqForInfoDto.reqInfoId}')">Proceed to Submit</button>
                                        </iais:action>
                                    </iais:section>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    function doBack(){
        SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function doSubmit(reqInfoId) {
        SOP.Crud.cfxSubmit("mainForm", "submit",reqInfoId);
    }


</script>

