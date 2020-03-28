<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">

                        <%@ include file="./navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane" id="tabInfo" role="tabpanel">
                                <%@include file="/iais/inspectionncList/tabViewApp.jsp"%>
                            </div>
                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                <%@include file="/iais/inspectionncList/tabDocuments.jsp"%>
                            </div>

                            <div class="tab-pane " id="tabLetter" role="tabpanel" >
                                <%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
                                <script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/jquery-3.4.1.min.js"></script>
                                <table class="table">
                                    <tbody>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                subject:
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <p><input name="subject" type="text" id="subject" title="subject"  readonly value="${insEmailDto.subject}"></p>
                                            </div>
                                        </td>

                                    </tr>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                content:
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <p><textarea name="messageContent" cols="108" rows="50" class="wenbenkuang" id="htmlEditroArea" title="content"  >${insEmailDto.messageContent}</textarea></p>
                                            </div>
                                        </td>
                                    </tr>

                                    </tbody>
                                </table>
                                <p class="text-right text-center-mobile">
                                    <iais:action style="text-align:center;">
                                        <button type="button" class="btn btn-primary" onclick="javascript:doPreview();">Preview</button>&nbsp;
                                        <button type="button" class="btn btn-primary" onclick="javascript:doReload();">Reload</button>
                                    </iais:action >
                                </p>
                            </div>
                            <div class="tab-pane active" id="tabProcessing" role="tabpanel" >
                                <%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
                                <script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/jquery-3.4.1.min.js"></script>
                                <table class="table">
                                    <tbody>

                                    <div class="alert alert-info" role="alert">
                                        <p><span><strong>Processing Status Update</strong></span></p>
                                    </div>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                Current Status:
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <p>${insEmailDto.appStatus}</p>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                Internal Remarks:
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <p><textarea name="remarks" cols="90" rows="6" maxlength="300" title="content"  >${insEmailDto.remarks}</textarea></p>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                Processing Decision:<strong style="color:#ff0000;"> *</strong>
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <select id="decision-validate-email" name="decision">
                                                    <option value="Select" selected>Please Select</option>
                                                    <c:forEach items="${appTypeOption}" var="decision">
                                                        <option  value="${decision.value}">${decision.text}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr height="1" style="display: none" id="selectDecisionMsg">
                                        <td class="col-xs-2" >
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <p style="color:#ff0000;">
                                                    This field is mandatory
                                                </p>
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <p class="text-right text-center-mobile">
                                    <iais:action style="text-align:center;">
                                        <button type="button" class="btn btn-primary" onclick="javascript:doSend();">Submit</button>
                                    </iais:action >
                                </p>
                                <div class="alert alert-info" role="alert">
                                    <p><span><strong>Processing History</strong></span></p>
                                </div>
                                <table class="table">
                                    <thead>
                                    <tr align="center">
                                        <iais:sortableHeader needSort="false" field="USERNAME" value="Username"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="false"  field="WORKING GROUP" value="Working Group"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="false"  field="APP_STATUS" value="Status Update"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="false"  field="REMARKS" value="Remarks"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="false" field="UPDATED_DT" value="Last Updated"></iais:sortableHeader>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:choose>
                                        <c:when test="${empty appPremisesRoutingHistoryDtos}">
                                            <tr>
                                                <td colspan="7">
                                                    <iais:message key="ACK018" escape="true"></iais:message>
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="pool" items="${appPremisesRoutingHistoryDtos}" varStatus="status">
                                                <tr>
                                                    <td><c:out value="${pool.actionby}"/></td>
                                                    <td><c:out value="${pool.wrkGrpId}"/></td>
                                                    <td><c:out value="${pool.appStatus}"/></td>
                                                    <td><c:out value="${pool.internalRemarks}"/></td>
                                                    <td><c:out value="${pool.updatedDt}" /></td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>

                                    </tbody>
                                </table>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">

    function doSend(){
        showWaiting();
        var f=$('#decision-validate-email option:selected').val();

        if(f!="Select"){
            SOP.Crud.cfxSubmit("mainForm", "send");
        } else {
            $("#selectDecisionMsg").show();
            dismissWaiting();
        }
    }


</script>





