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
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content" id="clearSelect">
                        <div class="bg-title">
                            <h2>Audit Information</h2>
                        </div>
                        <iais:row>
                            <iais:field value="Facility Name" width="15" required="false"/>
                            <iais:value width="10">
                                <p style="font-size: large"></p>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Facility Classification" width="15"
                                        required="false"/>
                            <iais:value width="10">
                                <p style="font-size: large"></p>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Facility Type" width="15"
                                        required="false"/>
                            <iais:value width="10">
                                <p style="font-size: large"></p>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Audit Type" width="15" required="false"/>
                            <iais:value width="10">
                                <iais:select name="auditType" id="auditType"
                                             value="${auditType}"
                                             codeCategory="CATE_ID_BSB_AUDIT_TYPE" firstOption="Please Select"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Remarks" width="15" required="false"/>
                            <iais:value width="10">
                                <textarea id=""
                                          name=""
                                          cols="70"
                                          rows="7"
                                          maxlength="2000"></textarea>
                            </iais:value>
                        </iais:row>

                        <div class="row">
                            <div class="col-xs-12 col-sm-6">
                                <a class="back" id="back" href="/system-admin-web/eservice/INTRANET/MohOfficerSubmitRevocation"><em class="fa fa-angle-left"></em> Back</a>
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
    <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
    <%@include file="/WEB-INF/jsp/include/utils.jsp" %>
</div>