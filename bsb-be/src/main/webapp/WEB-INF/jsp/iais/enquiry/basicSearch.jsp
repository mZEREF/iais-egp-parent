<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-enquiry.js"></script>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form id="mainForm"  method="post" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                            <h2>Basic Search Criteria</h2>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-12 col-md-12">
                                <div class="col-xs-12 col-md-12">
                                    <div class="components">
                                        <a class="btn btn-secondary" data-toggle="collapse"
                                           data-target="#searchCondition" aria-expanded="true">Filter</a>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="searchCondition" class="collapse" aria-expanded="true" >
                            <iais:row>
                                <iais:field value="Keyword search or part of"/>
                                <iais:value width="18">
                                    <label>
                                        <input type="text"
                                               style="width:180%; font-weight:normal;"
                                               name="searchText" maxlength="100"/>
                                    </label>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:value width="18">
                                    <input id="appChk" type="radio"
                                           name="searchChk" value="app"
                                           <c:if test="${count=='app'}">checked</c:if> /><label for="appChk">&nbsp;Application No</label>
                                </iais:value>
                                <iais:value width="18" cssClass="form-check">
                                    <input id="fnChk" type="radio"
                                           <c:if test="${count=='fac'}">checked</c:if>
                                           name="searchChk"
                                           value="fn"/><label for="fnChk">&nbsp;Facility Name</label>
                                </iais:value>
                                <iais:value width="18" cssClass="form-check">
                                    <input id="anChk" type="radio"
                                           <c:if test="${count=='approval'}">checked</c:if> value="an"
                                           name="searchChk"/><label for="anChk">&nbsp;Approval No</label>
                                </iais:value>
                                <iais:value width="18" cssClass="form-check">
                                    <input id="onChk" type="radio"
                                           <c:if test="${count=='afc'}">checked</c:if> value="on"
                                           name="searchChk"/><label for="onChk">&nbsp;Organisation Name</label>
                                </iais:value>
                            </iais:row>
                            <iais:row id="selectSearchChkMsg" cssClass="selectSearchChkMsg-select" style="display: none">
                                <div class="row" height="1"
                                     style="font-size: 1.6rem; color: #D22727; padding-left: 20px"
                                     id="selectSearchChkMsg">
                                    <iais:message key="OEN_ERR004"
                                                  escape="flase"></iais:message>
                                </div>
                            </iais:row>
                            <div class="col-xs-12 col-md-12">
                                <iais:action style="text-align:right;">
                                    <button type="button" class="btn btn-secondary" type="button"
                                            onclick="javascript:doClear();">Clear
                                    </button>
                                    <button type="button" class="btn btn-primary" type="button"
                                            onclick="javascript:doSearch();">Search
                                    </button>
                                    <button type="button" class="btn btn-primary" type="button"
                                            onclick="javascript:doAdvancedSearch();">Advanced Search
                                    </button>
                                </iais:action>
                            </div>
                        </div>
                    </div>
                    <br>
                    <%@ include file="baiscSearchResults.jsp" %>
                </div>
            </div>
        </div>
    </form>
</div>
