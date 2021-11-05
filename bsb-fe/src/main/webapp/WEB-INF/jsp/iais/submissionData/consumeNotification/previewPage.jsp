<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.lang.String" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-data-submission.js"></script>

<%@include file="../../inbox/dashboard/dashboard.jsp"%>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="previewSubmitPanel" role="tabpanel">
                                    <div class="preview-gp">
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="panel-group" role="tablist" aria-multiselectable="true">
                                                    <div class="panel panel-default">
                                                        <div class="panel-heading completed">
                                                            <h4 class="panel-title">
                                                                <a class="collapsed" data-toggle="collapse" href="#previewFacInfo">Facility Information</a>
                                                            </h4>
                                                        </div>
                                                        <div id="previewFacInfo" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Facility
                                                                            Profile</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
<%--                                                                        <c:forEach var="facInfo" items="${notification.facilityDto}">--%>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
<%--                                                                                    <p>${facInfo.name}</p>--%>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Facility Address</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
<%--                                                                                    <p>${facInfo.address}</p>--%>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Is the facility a Protected Place?</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
<%--                                                                                    <p>${facInfo.isProtect}</p>--%>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
<%--                                                                        </c:forEach>--%>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>


                                                    <div class="panel panel-default">
                                                        <div class="panel-heading completed">
                                                            <h4 class="panel-title">
                                                                <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Biological Agents &amp;Toxins</a>
                                                            </h4>
                                                        </div>
                                                        <div id="previewBatInfo" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <div class="text-right app-font-size-16"><a href="/bsb-fe/eservicecontinue/INTERNET/ConsumeNotification" data-step-key="approvalProfile"><em class="fa fa-pencil-square-o"></em>Edit</a>
                                                                </div>
                                                                <c:forEach var="info" items="${consumeNotification.consumptionNotList}" varStatus="status">
                                                                    <div class="panel-main-content form-horizontal min-row">
                                                                        <div class="form-group">
                                                                            <div class="col-10"><strong>Agents/Toxin ${status.index+1}</strong></div>
                                                                            <div class="clear"></div>
                                                                        </div>
<%--                                                                        <c:forEach var="info" items="${item.consumptionLists}">--%>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Schedule Type</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                        <p>${info.scheduleType}</p>
                                                                                    </div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Biological Agent/Toxin</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                        <p>${info.bat}</p>
                                                                                    </div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Type of Consumption</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                        <p>${info.consumeType}</p>
                                                                                    </div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Quantity Consumed</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                        <p>${info.consumedQty}</p>
                                                                                    </div>
                                                                                    <div class="clear"></div>
                                                                                </div>
<%--                                                                                <div class="form-group">--%>
<%--                                                                                    <label class="col-xs-5 col-md-4 control-label">Type Transferred</label>--%>
<%--                                                                                    <div class="col-sm-7 col-md-5 col-xs-7">--%>
<%--                                                                                        <p>${info.transferType}</p>--%>
<%--                                                                                    </div>--%>
<%--                                                                                    <div class="clear"></div>--%>
<%--                                                                                </div>--%>
<%--                                                                                <div class="form-group">--%>
<%--                                                                                    <label class="col-xs-5 col-md-4 control-label">Quantity Transferred</label>--%>
<%--                                                                                    <div class="col-sm-7 col-md-5 col-xs-7">--%>
<%--                                                                                        <p>${info.transferQty}</p>--%>
<%--                                                                                    </div>--%>
<%--                                                                                    <div class="clear"></div>--%>
<%--                                                                                </div>--%>
<%--                                                                                <div class="form-group">--%>
<%--                                                                                    <label class="col-xs-5 col-md-4 control-label">Quantity Disposed</label>--%>
<%--                                                                                    <div class="col-sm-7 col-md-5 col-xs-7">--%>
<%--                                                                                        <p>${info.disposedQty}</p>--%>
<%--                                                                                    </div>--%>
<%--                                                                                    <div class="clear"></div>--%>
<%--                                                                                </div>--%>
<%--                                                                                <div class="form-group">--%>
<%--                                                                                    <label class="col-xs-5 col-md-4 control-label">Quantity to Receive</label>--%>
<%--                                                                                    <div class="col-sm-7 col-md-5 col-xs-7">--%>
<%--                                                                                        <p>${info.receiveQty}</p>--%>
<%--                                                                                    </div>--%>
<%--                                                                                    <div class="clear"></div>--%>
<%--                                                                                </div>--%>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Unit of Measurement Transferred</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                        <p>${info.meaUnit}</p>
                                                                                    </div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                        </c:forEach>
                                                                    </div>

                                                                    <div class="panel-main-content form-horizontal min-row">
                                                                        <div class="form-group">
                                                                            <div class="col-10"><strong>Additional Details</strong></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p>${consumeNotification.facId}</p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Remarks</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p>${consumeNotification.remarks}</p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                            </div>
                                                        </div>
<%--                                                        <c:forEach var="doc" items="${docSettings}">--%>
<%--                                                            <c:set var="docFiles"--%>
<%--                                                                   value="${primaryDocs.get(doc.type)}"/>--%>
<%--                                                            <c:if test="${not empty docFiles}">--%>
<%--                                                                <div class="form-group">--%>
<%--                                                                    <div class="col-10">--%>
<%--                                                                        <strong>${doc.typeDisplay}</strong>--%>
<%--                                                                    </div>--%>
<%--                                                                    <div class="clear"></div>--%>
<%--                                                                </div>--%>
<%--                                                                <div>--%>
<%--                                                                    <c:forEach var="file"--%>
<%--                                                                               items="${docFiles}">--%>
<%--                                                                        <div class="form-group">--%>
<%--                                                                            <div class="col-10">--%>
<%--                                                                                <p>${file.filename}(${String.format("%.1f", file.size/1024.0)}KB)</p>--%>
<%--                                                                            </div>--%>
<%--                                                                            <div class="clear"></div>--%>
<%--                                                                        </div>--%>
<%--                                                                    </c:forEach>--%>
<%--                                                                </div>--%>
<%--                                                            </c:if>--%>
<%--                                                        </c:forEach>--%>
                                                    </div>

                                                    <div class="panel panel-default">
                                                        <div class="panel-heading completed">
                                                            <h4 class="panel-title">
                                                                <a class="collapsed" data-toggle="collapse"
                                                                   href="#previewDocs">Declarations</a>
                                                            </h4>
                                                        </div>
                                                        <div id="previewDocs" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group ">
                                                                        <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                                            <input type="checkbox" name="declare" id="declare" value="Y"/>
                                                                        </div>
<%--                                                                    </div>--%>
<%--                                                                    <div class="form-group ">--%>
                                                                        <div class="col-xs-10 control-label">
                                                                            <label for="declare">I, hereby declare that all the information I have provided here is true and accurate. The facility no longer possesses inventory of the biological agent/toxin following the destruction and/or disposal of the declared materials.</label>
                                                                            <span data-err-ind="declare" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <%@ include file="../InnerFooter.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>