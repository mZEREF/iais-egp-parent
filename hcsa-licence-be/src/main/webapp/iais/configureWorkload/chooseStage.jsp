<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container">
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="form-group">
                        <div class="col-xs-12 col-md-8 col-lg-9">
                            <iais:select name="stageSelect" id="stageSelect" options="stageSelect" codeCategory="CATE_ID_COMMON_STATUS" firstOption="Select Stage"></iais:select>
                        </div>
                    </div>

                    <div class="col-xs-12 col-sm-6">
                        <div class="text-right text-center-mobile"><a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: doNext();">Next</a></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    function doNext() {
        SOP.Crud.cfxSubmit("mainForm","serviceInStage");
    }

</script>