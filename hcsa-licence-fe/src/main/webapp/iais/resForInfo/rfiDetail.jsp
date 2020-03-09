<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="egov-cloud" uri="ecquaria/sop/egov-cloud" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<form  method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
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
                                            <iais:value width="18" >
                                                <span>${licPreReqForInfoDto.dueDateSubmission}</span>
<%--                                                <iais:datePicker  name = "Due_date" dateVal="${licPreReqForInfoDto.dueDateSubmission}" ></iais:datePicker>--%>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="checkbox" readonly name="reqType" checked/>Information
                                                </label>
                                                <c:choose>
                                                    <c:when test="${licPreReqForInfoDto.needDocument}">
                                                        <label>
                                                            <input type="checkbox" readonly name="reqType" checked />Supporting Documents
                                                        </label>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <label>
                                                            <input type="checkbox" readonly name="reqType" />Supporting Documents
                                                        </label>
                                                    </c:otherwise>
                                                </c:choose>

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
                                                    <textarea id="userReply_rfi" name="userReply" rows="8" style=" font-weight:normal;" cols="70">${licPreReqForInfoDto.userReply}</textarea>
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <c:if test="${licPreReqForInfoDto.needDocument}">
                                            <iais:row>
                                                <iais:value width="18">
                                                    <div class="file-upload-gp">

                                                        &nbsp;&nbsp;&nbsp;<div id="uploadFileName"></div>
                                                        <input class="selectedFile commDoc" id="commonDoc"  name = "UploadFile" type="file" style="display: none;" aria-label="selectedFile1" >
                                                        <a class="btn btn-file-upload btn-secondary" >Attachment</a><br/>
                                                    </div>

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
    $("#commonDoc").change(function () {
        $("#uploadFileName").text(this.files[0].name)
    })


</script>

