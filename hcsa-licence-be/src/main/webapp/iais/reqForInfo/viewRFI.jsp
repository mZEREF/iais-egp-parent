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
                                            <iais:field value=""/>
                                            <iais:value width="18">
                                                <button class="btn btn-secondary" type="button"  onclick="javascript:doExtends('${licPreReqForInfoDto.reqInfoId}')">Extends</button>
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
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:value width="18">
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
                                                    <textarea id="userReply" rows="10" cols="100">${licPreReqForInfoDto.userReply}</textarea>
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <c:if test="${licPreReqForInfoDto.needDocument}">
                                            <iais:row >
                                                <iais:value width="18">
                                                    <div class="pop-up">
                                                        <div class="pop-up-body">

                                                            <div class="field col-sm-4 control-label formtext"><label>Docment for Premise:</label></div>
                                                            <span class="fileType" style="display:none">Docment1</span><span class="fileFilter" style="display:none">png</span><span class="fileMandatory" style="display:none">Yes</span>
                                                            <div class="control col-sm-5">
                                                                <div class="fileList ">
                                                                    <span class="filename server-site" id="130">
                                                                        <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo1&fileRo1=<iais:mask name="fileRo1" value="${licPreReqForInfoDto.fileRepoId}"/>&fileRepoName=${licPreReqForInfoDto.docName}" title="Download" class="downloadFile">${licPreReqForInfoDto.docName}(${licPreReqForInfoDto.docSize} KB)</a>
                                                                    </span>
                                                                </div>
                                                            </div>

                                                        </div>
                                                    </div>
                                                </iais:value>
                                            </iais:row>
                                        </c:if>
                                        <iais:action style="text-align:left;">
                                            <a  onclick="javascript:doBack()">< Back</a>
                                        </iais:action>
                                        <iais:action style="text-align:center;">
                                            <button class="btn btn-secondary" type="button"  onclick="javascript:doCancel('${licPreReqForInfoDto.reqInfoId}')">Cancel</button>
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
        showWaiting();SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function doExtends(reqInfoId) {
        showWaiting();SOP.Crud.cfxSubmit("mainForm", "update",reqInfoId);
    }
    function doCancel(reqInfoId) {
        showWaiting();SOP.Crud.cfxSubmit("mainForm", "cancel",reqInfoId);

    }


</script>

