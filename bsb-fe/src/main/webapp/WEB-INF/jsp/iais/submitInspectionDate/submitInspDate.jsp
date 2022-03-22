<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-appointment.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<%@ include file="dashboard.jsp" %>
<form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <div>
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <br><br>
                    <iais:section title="">
                        <%--@elvariable id="inspectionDateDto" type="sg.gov.moh.iais.egp.bsb.dto.appointment.InspectionDateDto"--%>
                        <iais:row>
                            <iais:field value="Preferred date for inspection (Start)" required="true"/>
                            <iais:value width="18">
                                <input type="text" autocomplete="off" name="specifyStartDate" id="specifyStartDate" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control" value="${inspectionDateDto.specifyStartDate}"/>
                                <span data-err-ind="specifyStartDate" class="error-msg" ></span>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Preferred date for inspection (End)" required="true"/>
                            <iais:value width="18">
                                <input type="text" autocomplete="off" name="specifyEndDate" id="specifyEndDate" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control" value="${inspectionDateDto.specifyEndDate}"/>
                                <span data-err-ind="specifyEndDate" class="error-msg" ></span>
                            </iais:value>
                        </iais:row>
                    </iais:section>
                </div>
            </div>
        </div>

        <div class="application-tab-footer">
            <div class="row">
                <div class="col-xs-12 col-sm-5">
                    <p></p>
                </div>
                <div class="col-xs-12 col-sm-6">
                    <%--@elvariable id="backUrl" type="java.lang.String"--%>
                    <div class="button-group">
                        <a href="${backUrl}" type="button" class="btn btn-secondary save">CANCEL</a>
                        <button class="btn btn-primary save" id="submitBtn">SUBMIT</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<br><br>