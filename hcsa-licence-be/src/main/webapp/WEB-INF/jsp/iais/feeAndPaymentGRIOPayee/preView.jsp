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
                <div class="bg-title col-xs-12 col-md-12">
                    <strong>Add a GIRO Payee</strong>
                </div>
                <div class="row">Note: This function is to add a GIRO Payee who submitted a manual application.</div>
                <div class="row">&nbsp;</div>
                <div class="row">The GIRO arrangement must be approved by the bank, otherwise GIRO deductions for that payee will fail.</div>
                <div class="row">&nbsp;</div>
                <div class="row">&nbsp;</div>
                <div class="row">Enter GIRO Payee Details</div>
                <div class="panel-body">
                    <div class="panel-main-content">
                        <iais:section title="" id = "supPoolList">
                            <iais:row>
                                <iais:field value="HCI Code(s) :"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <c:forEach items="${hciSession.rows}" var="hci">
                                        ${hci.hciCode}<br>
                                    </c:forEach>
                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="HCI Name(s) :"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <c:forEach items="${hciSession.rows}" var="hci">
                                        ${hci.hciName}<br>
                                    </c:forEach>
                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Account Name :" mandatory="true"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <c:out value="${acctName}"/>
                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Bank Code :"  mandatory="true"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <c:out value="${bankCode}"/>
                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Branch Code :"  mandatory="true"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <c:out value="${branchCode}"/>
                                </div>
                            </iais:row>

                            <iais:row>
                                <iais:field value="Bank Name :" mandatory="true"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <c:out value="${bankName}"/>
                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Bank Account No. :" mandatory="true"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <c:out value="${bankAccountNo}"/>
                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Customer Reference No. :" mandatory="true"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <c:out value="${cusRefNo}"/>
                                </div>
                            </iais:row>
                            <iais:row>
                                <iais:field value="GIRO Form :" mandatory="true"/>
                                <div class="col-sm-7 col-md-4 col-xs-10">
                                    <div class="file-upload-gp">
                                        <span>
                                            <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${docStatus.index}&fileRo${docStatus.index}=<iais:mask name="fileRo${docStatus.index}" value="${docDto.fileRepoId}"/>&fileRepoName=${docDto.docName}">${docDto.docName}</a>
                                        </span>
                                    </div>
                                </div>
                            </iais:row>
                        </iais:section>
                        <div class="col-xs-12 col-md-12">
                            <iais:action style="text-align:right;">
                                <button class="btn btn-primary" type="button"  onclick="javascript:doBack()">Back</button>
                                <button class="btn btn-primary" type="button"  onclick="javascript:doSubmit()">Submit</button>
                            </iais:action>
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
