<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<webui:setLayout name="iais-internet"/>
<%@include file="./dashboard.jsp" %>
<%@include file="serviceMenu/comm/comFun.jsp"%>
<style>
    .table-info-display {
        margin: 20px 0px 5px 0px;
        background: #efefef;
        padding: 8px;
        border-radius: 8px;
        -moz-border-radius:8px;
        -webkit-border-radius:8px;

    }

    .table-count {
        float: left;
        margin-top: 5px;
    }
    .nav ul.pagination{
        padding-top: 7px;
    }

    .nav ul.pagination > li{
        padding-left: 3px;
    }

    .dashboard-gp .dashboard-tile-item .dashboard-tile h1.dashboard-count {
        margin-left: -5px;
    }
    @media only screen and (max-width: 767px){
        .table-gp table.table > tbody > tr > td {
            display: inline-grid;
            display: -ms-inline-grid;
            width: 35%;
            padding: 0;
            border: 0;
            margin-bottom: 15px;
            padding-right: 2%;
        }
    }

</style>
<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="guide_action_type" id="guide_action_type"/>
        <input type="hidden" name="guide_action_value" id="guide_action_value"/>
        <input type="hidden" name="crud_action_type_form"/>
        <input type="hidden" value="" id="isNeedDelete" name="isNeedDelete"/>
        <div class="main-content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="prelogin-content">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="center-content">
                                    <div class="row">
                                        <div class="col-xs-12 col-md-12">
                                            <div class="self-assessment-gp">