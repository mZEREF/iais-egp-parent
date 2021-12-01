<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.lang.String" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-data-submission.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-data-sub-common.js"></script>

<%@include file="../dashboard.jsp" %>
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
                                                                        <div class="col-10"><strong>Facility Profile</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${facilityInfo.facName}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Address</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${facilityInfo.facAddress}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Is the facility a Protected Place?</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${facilityInfo.isProtected}</p>
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
                                                                <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Biological Agents&amp;Toxins</a>
                                                            </h4>
                                                        </div>
                                                        <div id="previewBatInfo" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <div class="text-right app-font-size-16"><a href="#" id="edit" data-step-key="approvalProfile"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                                                <c:forEach var="info" items="${disposalNotification.disposalNotList}" varStatus="status">
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10">
                                                                            <strong>Agents/Toxin ${status.index+1}</strong>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Schedule Type</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><iais:code code="${info.scheduleType}"/></p>
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
                                                                            <label class="col-xs-5 col-md-4 control-label">Quantity Disposed</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${info.disposedQty}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Unit of Measurement Transferred</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><iais:code code="${info.meaUnit}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Complete Destruction/Disposal</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>Yes</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Method of Destruction</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${info.destructMethod}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Details and/or procedures of destruction</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${info.destructDetails}</p>
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
                                                                                <p>${facilityInfo.facName}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Remarks</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${disposalNotification.remarks}</p>
                                                                            </div>
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
                                                                        <div class="form-group">
                                                                            <div class="col-10"><strong>Inventory: Biological Agents</strong></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <c:forEach var="doc" items="${docMeta.get('ityBat')}">
                                                                            <c:set var="tmpId" value="${MaskUtil.maskValue('file', doc.id)}"/>
                                                                            <div class="form-group">
                                                                                <div class="col-10"><p><a href="javascript:void(0)" onclick="downloadFile('new', '${tmpId}')">${doc.filename}</a>(${String.format("%.1f", doc.size/1024.0)}KB)</p></div>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                        </c:forEach>

                                                                        <div class="form-group">
                                                                            <div class="col-10"><strong>Inventory: Toxins</strong></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <c:forEach var="doc" items="${docMeta.get('ityToxin')}">
                                                                            <c:set var="tmpId" value="${MaskUtil.maskValue('file', doc.id)}"/>
                                                                            <div class="form-group">
                                                                                <div class="col-10"><p><a href="javascript:void(0)" onclick="downloadFile('new', '${tmpId}')">${doc.filename}</a>(${String.format("%.1f", doc.size/1024.0)}KB)</p></div>
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
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div>
                                                                        <div class="form-group">
                                                                            <p>I, hereby declare that all the information I have provided here is true and accurate. The facility no longer possesses inventory of the biological agent/toxin following the destruction and/or disposal of the declared materials.</p>
                                                                            <br/>
                                                                            <div class="col-sm-4" style="margin-top: 8px">
                                                                                <input type="radio" name="ensure" id="ensureT" value="yes"/>
                                                                                <label for="ensureT">yes</label>
                                                                            </div>
                                                                            <div class="col-sm-4" style="margin-top: 8px">
                                                                                <input type="radio" name="ensure" id="ensureN" value="no"/>
                                                                                <label for="ensureN">no</label>
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