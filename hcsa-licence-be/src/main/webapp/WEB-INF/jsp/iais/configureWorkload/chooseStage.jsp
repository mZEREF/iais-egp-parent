<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <div class="col-xs-12 col-sm-12 col-md-12">
            <div class="center-content">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <h3>
                        <span>Stage</span>
                    </h3>
                    <iais:section title="" id="supPoolList">
                        <iais:row>
                            <iais:field value="Select Stage"/>
                            <iais:value width="10">
                                <iais:select name="stageSelect" options="stageSelect" id="stageSelect"
                                             firstOption="Select Stage" value="${firstValue}"></iais:select>
                            </iais:value>
                        </iais:row>
                    </iais:section>
                    <div class="col-xs-12 col-sm-12">
                        <div class="button-group col-xs-6 col-sm-6">
                            <a class="btn btn-primary next" style="float: right;margin-top: 50px;"
                               href="javascript:void(0);"
                               onclick="javascript: doNext();">Next</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    function doNext() {
        SOP.Crud.cfxSubmit("mainForm", "serviceInStage");
    }

</script>