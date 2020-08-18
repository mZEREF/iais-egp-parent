<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<style>
    nav{
        margin-bottom: 55px;
    }
    footer{
        margin-bottom: 0px;
        position: absolute !important;
    }
</style>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <div class="col-xs-12 col-sm-12 col-md-12" style="height: 600px">
            <div class="center-content">
                <div class="panel-group" id="accordion" style="text-align: center;" role="tablist" aria-multiselectable="true">
                    <h2>
                        <span>Types of Premise</span>
                    </h2>
                    <div class="form-group" id="serviceDivByrole" style="margin-top: 50px;">
                        <div class="col-xs-3 col-sm-3 col-md-3"></div>
                        <div class="col-xs-6 col-sm-6 col-md-6" style="text-align: left">
                            <iais:select name="stageSelect" options="primiseType"
                                         firstOption="Please Select"></iais:select>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-12">
                            <a class="btn btn-primary next" style="margin-top: 50px;"
                               href="javascript:void(0);"
                               onclick="javascript: doNext();">Next</a>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<script type="text/javascript">
    function doNext() {
        SOP.Crud.cfxSubmit("mainForm", "serviceInStage");
    }

</script>