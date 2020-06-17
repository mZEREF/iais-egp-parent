<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 2/28/2020
  Time: 10:42 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<div class="main-content" >
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div >
            <div class="container">
                <div class="row">
                    <div class="col-xs-12">
                        <br><br>
                        <div class="prelogin-title">
                        </div>

                        <br>
                        <span id="error_dateError" name="iaisErrorMsg" class="error-msg"></span>
                        <br><br>
                        <iais:section title="">

                        <iais:row>
                            <iais:field value="Preferred date for inspection (Start)"  required="true"/>
                            <iais:value width="18">
                                <iais:datePicker id = "inspStartDate" name = "inspStartDate"  value="${inspStartDate}"></iais:datePicker>
                                <span id="error_inspStartDate" name="iaisErrorMsg" class="error-msg"></span>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Preferred date for inspection (End)" required="true"/>
                            <iais:value width="18">
                                <iais:datePicker id = "inspEndDate" name = "inspEndDate"  value="${inspEndDate}"></iais:datePicker>
                                <span id="error_inspEndDate" name="iaisErrorMsg" class="error-msg"></span>
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
                        <div class="button-group">
                            <a class="btn btn-secondary" onclick="Utils.submit('mainForm', 'doCancel')">Cancel</a>
                            <a class="btn btn-primary next" onclick="Utils.submit('mainForm', 'doSubmit')">Submit</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<br><br>

<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>