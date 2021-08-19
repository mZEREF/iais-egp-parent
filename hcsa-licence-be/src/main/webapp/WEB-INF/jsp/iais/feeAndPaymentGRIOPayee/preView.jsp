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
                                    <div class="table-responsive">
                                        <div class="table-gp">
                                            <table aria-describedby="" class="table">
                                                <thead>
                                                <tr align="center">
                                                    <th scope="col" style="width:2%">&nbsp;</th>
                                                    <iais:sortableHeader needSort="true"
                                                                         field="UEN_NO"
                                                                         value="UEN"/>
                                                    <iais:sortableHeader needSort="true" field="Licence No."
                                                                         value="LICENCE_NO"/>
                                                    <iais:sortableHeader needSort="true" field="Service Type"
                                                                         value="SVC_NAME"/>
                                                    <iais:sortableHeader needSort="true" field="Licensee"
                                                                         value="LICENSEE_NAME"/>
                                                </tr>
                                                </thead>
                                                <tbody class="form-horizontal">
                                                <c:choose>
                                                    <c:when test="${empty hciSession.rows}">
                                                        <tr>
                                                            <td colspan="15">
                                                                <iais:message key="GENERAL_ACK018"
                                                                              escape="true"/>
                                                            </td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="pool"
                                                                   items="${hciSession.rows}"
                                                                   varStatus="status">
                                                            <tr>
                                                                <td class="form-check"
                                                                    onclick="javascript:controlCease()">
                                                                    <input class="form-check-input licenceCheck"
                                                                           id="orgPer${status.index + 1}"
                                                                           type="checkbox"
                                                                           name="opIds"
                                                                           value="${pool.orgId}">
                                                                    <label class="form-check-label"
                                                                           for="orgPer${status.index + 1}"><span
                                                                            class="check-square"></span>
                                                                    </label>
                                                                </td>
                                                                <td >
                                                                    <c:out value="${pool.uenNo}"/>
                                                                </td>
                                                                <td>
                                                                    <c:out value="${pool.licenceNo}"/>
                                                                </td>
                                                                <td >
                                                                    <c:out value="${pool.svcName}"/>
                                                                </td>
                                                                <td>
                                                                    <c:out value="${pool.licenseeName}"/>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:otherwise>
                                                </c:choose>
                                                </tbody>
                                            </table>
                                        </div>

                                    </div>

                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">Account Name <span class="mandatory">*</span></label>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <c:out value="${acctName}"/>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">Bank Code <span class="mandatory">*</span></label>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <c:out value="${bankCode}"/>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">Branch Code <span class="mandatory">*</span></label>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <c:out value="${branchCode}"/>
                                        </div>
                                    </iais:row>

                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">Bank Name <span class="mandatory">*</span></label>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <iais:code code="${bankName}"/>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">Bank Account No. <span class="mandatory">*</span></label>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <c:out value="${bankAccountNo}"/>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">Customer Reference No. <span class="mandatory">*</span></label>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <c:out value="${cusRefNo}"/>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">GIRO Form <span class="mandatory">*</span></label>
                                        <div class="document-upload-gp col-sm-7 col-md-6 col-xs-10">
                                            <div class="document-upload-list">
                                                <div class="file-upload-gp">
                                                    <input id="selectFile" name="selectFile" type="file" class="iptFile" style="display: none;">
                                                    <div id="uploadFileBox" class="file-upload-gp">
                                                        <c:forEach var="attachmentDto" items="${giroAcctFileDto.attachmentDtos}"
                                                                   varStatus="docStatus">
                                                            <p class="fileList">
                                                                <iais:downloadLink fileRepoIdName="fileRo${docStatus.index}" fileRepoId="${attachmentDto.id}" docName="${attachmentDto.docName}"/>
                                                                <br>
                                                            </p>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <label class="col-xs-0 col-md-4 ">Internal Remarks </label>
                                        <div class="col-sm-7 col-md-6 col-xs-10">
                                            <c:out value="${remarks}"/>
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
