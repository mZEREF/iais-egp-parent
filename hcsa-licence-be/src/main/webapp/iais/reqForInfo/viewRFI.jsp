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
                                                    <textarea rows="10" cols="100">${licPreReqForInfoDto.userReply}</textarea>
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <c:if test="${licPreReqForInfoDto.needDocument}">
                                            <iais:row >
                                                <iais:value width="18">
                                                <span  style="margin-right: 5px; cursor: pointer;" uploadid="157795250964805646132380337285">
                                                    <input type="button" class="ico_att" style="margin:0 3px 0 0!important;margin:0 3px 2px 0;">
                                                    <input ext="control"  type="hidden" value="${licPreReqForInfoDto.docName}" filename="${licPreReqForInfoDto.docName}" filesize="${licPreReqForInfoDto.docSize}" disabled="">
                                                    <span ui-type="filename"  class="">${licPreReqForInfoDto.docName}</span>&nbsp;
                                                    <span  name="206432" class="addrtitle">(${licPreReqForInfoDto.docSize/1024}K)</span>
                                                </span>
                                                </iais:value>
                                            </iais:row>
                                        </c:if>

                                        <iais:action style="text-align:center;">
                                            <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doAccept('${licPreReqForInfoDto.reqInfoId}')">Accept</button>
                                            <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doCancel('${licPreReqForInfoDto.reqInfoId}')">Cancel</button>
                                            <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doBack()">Back</button>
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
    function doAccept(reqInfoId) {
        SOP.Crud.cfxSubmit("mainForm", "accept",reqInfoId);
    }
    function doCancel(reqInfoId) {
        SOP.Crud.cfxSubmit("mainForm", "cancel",reqInfoId);
    }

</script>

