<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-task.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <div class="bg-title">
                                <h2>
                                    <span>Task Re-assignment</span>
                                </h2>
                            </div>
                            <iais:body >
                                <iais:section title="">
                                    <iais:row>
                                        <iais:field value="${iais_Login_User_Info_Attr.userName}:" required="true"/>
                                        <iais:value width="5">
                                                <iais:select name="reassignUserId" cssClass="reassignUserIdDropdown" firstOption="Please Select" options="userOption" value="${selectedUser}"/>
                                                <br><span data-err-ind="reassignUserId" class="error-msg"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Remarks:"/>
                                        <iais:value width="6">
                                            <textarea style="resize:none" name="reassignRemarks" cols="65" rows="6" title="content" MAXLENGTH="2000"><c:out value="${inspectionTaskPoolListDto.reassignRemarks}"/></textarea>
                                        </iais:value>
                                    </iais:row>
                                    <iais:action >
                                        <a href="/bsb-web/eservicecontinue/INTRANET/MohBsbReassignTaskList" class="back" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                                        <button class="btn btn-primary" id="submitBtn" style="float:right" type="button">Submit</button>
                                    </iais:action>
                                </iais:section>
                            </iais:body>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>