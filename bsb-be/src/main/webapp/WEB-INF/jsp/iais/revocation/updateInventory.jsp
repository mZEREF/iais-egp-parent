<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-revocation.js"></script>
<div class="main-content">
    <form class="form-horizontal" id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
<%--        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>--%>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content" id="clearSelect">
                        <div class="bg-title">
                            <h2>Update Inventory</h2>
                        </div>
                        <iais:row>
                            <iais:field value="Biological Agents or Toxins" width="15" required="false"/>
                            <iais:value width="10">
                                <%--                                <iais:select name="toxins" value="toxins" firstOption="Please Select"/>--%>
                                <select id="toxinsSelect">
                                    <option>Please Select</option>
                                    <option>Biological Agents</option>
                                    <option>Toxins</option>
                                </select>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Physical Possession of Agent (Initial state)" width="15"
                                        required="false"/>
                            <iais:value width="10">
                                <p style="font-size: large">Yes</p>
                                <%--<span class="error-msg" name="errorMsg" id="error_displayName"></span>--%>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Physical Possession of Agent (Current state)" width="15"
                                        required="false"/>
                            <iais:value width="10">
                                <%--                                <iais:select name="currentState" value="currentState" firstOption="Please Select"/>--%>
                                <select id="stateSelect">
                                    <option>Please Select</option>
                                    <option>Yes</option>
                                    <option>No</option>
                                </select>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Initial Quantity" width="15" required="false"/>
                            <iais:value width="10">
                                <p style="font-size: large">550</p>
                                <%--<span class="error-msg" name="errorMsg" id="error_idNo"></span>--%>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Qty to Change" width="15" required="false"/>
                            <iais:value width="10">
                                <input type="number" id="number" name="changeNumber"/>
                                <%--                                <span class="error-msg" name="errorMsg" id="error_designation"></span>--%>
                            </iais:value>
                        </iais:row>

                        <div class="row">
                            <div class="col-xs-12 col-sm-6">
                                <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                            </div>
                            <div align="right">
                                <button name="clearBtn3" id="clearButton3" type="button" class="btn btn-secondary">
                                    Clear
                                </button>
                                <button name="submitBtn3" id="submitButton3" type="button" class="btn btn-primary">
                                    Submit
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
<%--    <%@include file="/WEB-INF/jsp/include/validation.jsp" %>--%>
<%--    <%@include file="/WEB-INF/jsp/include/utils.jsp" %>--%>
</div>