<%--
  Created by IntelliJ IDEA.
  User: mjy
  Date: 2021/3/2
  Time: 16:36
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="center-content">
            <div class="intranet-content">
                <div class="row form-horizontal">
                    <div class="bg-title col-xs-12 col-md-12">
                        <h2>
                            <span>Add a GIRO Payee</span>
                        </h2>
                    </div>
                    <div class="col-xs-12 col-md-12">
                        <iais:row>
                            <div class=" col-xs-12 col-md-12">
                                Note: This function is to add a GIRO Payee who submitted a manual application.
                            </div>
                        </iais:row>
                        <iais:row>
                            <div class=" col-xs-12 col-md-12">
                                The GIRO arrangement must be approved by the bank, otherwise GIRO deductions for that payee will fail.
                            </div>
                        </iais:row>
                        <div class="row">&nbsp;</div>
                        <div class="row"><h3>Enter GIRO Payee Details</h3></div>
                        <div class="panel-body">
                            <div class="panel-main-content">
                                <iais:section title="" id = "supPoolList">
                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">HCI Code(s) :<span class="mandatory">*</span></label>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <c:forEach items="${hciSession.rows}" var="hci">
                                                ${hci.hciCode}<br>
                                            </c:forEach>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">HCI Name(s) :<span class="mandatory">*</span></label>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <c:forEach items="${hciSession.rows}" var="hci">
                                                ${hci.hciName}<br>
                                            </c:forEach>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">Account Name :<span class="mandatory">*</span></label>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <c:out value="${acctName}"/>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">Bank Code :<span class="mandatory">*</span></label>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <c:out value="${bankCode}"/>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">Branch Code :<span class="mandatory">*</span></label>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <c:out value="${branchCode}"/>
                                        </div>
                                    </iais:row>

                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">Bank Name :<span class="mandatory">*</span></label>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <c:out value="${bankName}"/>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">Bank Account No. :<span class="mandatory">*</span></label>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <c:out value="${bankAccountNo}"/>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">Customer Reference No. :<span class="mandatory">*</span></label>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <c:out value="${cusRefNo}"/>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">GIRO Form :<span class="mandatory">*</span></label>
                                        <div class="document-upload-gp col-sm-7 col-md-6 col-xs-10">
                                            <div class="document-upload-list">
                                                <div class="file-upload-gp">
                                                    <input id="selectFile" name="selectFile" type="file" class="iptFile" style="display: none;">
                                                    <div id="uploadFileBox" class="file-upload-gp">
                                                        <c:forEach var="attachmentDto" items="${giroAcctFileDto.attachmentDtos}"
                                                                   varStatus="docStatus">
                                                            <p class="fileList">
                                                                <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${docStatus.index}&fileRo${docStatus.index}=<iais:mask name="fileRo${docStatus.index}" value="${attachmentDto.id}"/>&fileRepoName=${attachmentDto.docName}">${attachmentDto.docName}</a>
                                                                <br>
                                                            </p>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </iais:row>
                                </iais:section>
                                <iais:action style="text-align:right;">
                                    <a style=" float:left;padding-top: 1.1%;text-decoration:none;" onclick="javascript:doBack()"><em class="fa fa-angle-left"> </em> Back</a>
                                    <button  class="btn btn-primary" type="button"  onclick="javascript:doSubmit()">Submit</button>
                                </iais:action>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function doBack(){
        showWaiting();
        $("[name='crud_action_type']").val("back");
        $("#mainForm").submit();
    }

    function doSubmit(){
        showWaiting();
        $("[name='crud_action_type']").val("submit");
        $("#mainForm").submit();
    }
</script>
