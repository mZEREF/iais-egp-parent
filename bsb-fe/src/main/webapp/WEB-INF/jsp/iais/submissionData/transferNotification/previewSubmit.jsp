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

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-data-sub-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-data-submission.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

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
                                                                <div class="text-right app-font-size-16"><a href="#"><em
                                                                        class="fa fa-pencil-square-o"></em>Edit</a>
                                                                </div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Facility
                                                                            Profile</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
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
                                                                <div class="text-right app-font-size-16"><a href="#" data-step-key="approvalProfile"><em class="fa fa-pencil-square-o"></em>Edit</a>
                                                                </div>
                                                                <c:forEach var="tsNotList" items="${transferNot.transferNotList}" varStatus="status">
                                                                    <div class="panel-main-content form-horizontal min-row">
                                                                        <div class="form-group">
                                                                            <div class="col-10"><strong>Agents/Toxin ${status.index+1}</strong></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Transfer Code</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Schedule Type</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${tsNotList.scheduleType}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Biological Agent/Toxin</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${tsNotList.batCode}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                                <c:choose>
                                                                                    <c:when test="${tsNotList.scheduleType eq 'SCHTYPE006'}">
                                                                                        <div class="form-group">
                                                                                            <label class="col-xs-5 col-md-4 control-label">Quantity to Transfer</label>
                                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${tsNotList.transferQty}</p></div>
                                                                                            <div class="clear"></div>
                                                                                        </div>
                                                                                        <div class="form-group">
                                                                                            <label class="col-xs-5 col-md-4 control-label">Unit of Measurement</label>
                                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${tsNotList.mstUnit}</p></div>
                                                                                            <div class="clear"></div>
                                                                                        </div>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <div class="form-group">
                                                                                            <label class="col-xs-5 col-md-4 control-label">Type of Transfer</label>
                                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${tsNotList.transferType}</p></div>
                                                                                            <div class="clear"></div>
                                                                                        </div>
                                                                                        <div class="form-group">
                                                                                            <label class="col-xs-5 col-md-4 control-label">Quantity of Biological Agent</label>
                                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${tsNotList.batQty}</p></div>
                                                                                            <div class="clear"></div>
                                                                                        </div>
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </div>
                                                                    </div>
                                                                </c:forEach>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Additional Details</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Receiving Facility</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${transferNot.receiveFacility}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Date of Expected Transfer</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${transferNot.expectedTfDate}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Expected Arrival Time at Receiving Facility</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${transferNot.expArrivalTime}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Name of Courier Service Provider</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${transferNot.providerName}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Remarks</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${transferNot.remarks}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Uploaded Documents</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
                                                                        <c:forEach var="doc" items="${docMeta.get('ityBat')}">
                                                                            <div class="form-group">
                                                                                <div class="col-10"><strong>Inventory: Biological Agents</strong></div>
                                                                                <div class="col-10"><p>${doc.filename}(${String.format("%.1f", doc.size/1024.0)}KB)</p></div>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                        </c:forEach>

                                                                        <c:forEach var="doc" items="${docMeta.get('ityToxin')}">
                                                                            <div class="form-group">
                                                                                <div class="col-10"><strong>Inventory: Toxins</strong></div>
                                                                                <div class="col-10"><p>${doc.filename}(${String.format("%.1f", doc.size/1024.0)}KB)</p></div>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                        </c:forEach>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Others</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="panel panel-default">
                                                        <div class="panel-heading completed">
                                                            <h4 class="panel-title">
                                                                <a class="collapsed" data-toggle="collapse" href="#previewDec">Declarations</a>
                                                            </h4>
                                                        </div>
                                                        <div id="previewDec" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <div class="text-right app-font-size-16"><a href="#"><em
                                                                        class="fa fa-pencil-square-o"></em>Edit</a>
                                                                </div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div>
                                                                        <div class="form-group">
                                                                            <p>I will ensure to the best of my ability that the packaging of the materials and the transfer, is carried out in accordance with the requirements stipulated in the BATA Transportation Regulations</p>
                                                                            <br/>
                                                                            <div class="col-sm-4" style="margin-top: 8px">
                                                                                <input type="radio" name="declare" id="declareT" value="yes"/>
                                                                                <label for="declareT">yes</label>
                                                                            </div>
                                                                            <div class="col-sm-4" style="margin-top: 8px">
                                                                                <input type="radio" name="declare" id="declareN" value="no"/>
                                                                                <label for="declareN">no</label>
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