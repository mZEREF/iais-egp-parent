<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1>Declaration login fail</h1>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <div class="main-content">
        <div class="tab-gp steps-tab">
            <div class="tab-content text-center">
                <div class="tab-pane active" id="premisesTab" role="tabpanel">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="new-premise-form-conveyance">
                                <div class="form-horizontal">
                                    <iais:row>
                                        <div class="col-xs-12">
                                            <textarea id="blockOutDesc" name="blockOutDesc" cols="70"
                                                      rows="7">Terms and Conditions</textarea>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <div class="col-xs-12">
                                            <input type="checkbox" id="agreeCheckBox" style="margin-top: 19px"
                                                   value="admin"
                                                   name="role" checked>
                                            <label>I agree to the Terms and
                                                Conditions</label>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <button type="button" class="search btn" onclick="declaration();">Declaration</button>
                                    </iais:row>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</form>


</form>
<script type="text/javascript">
    function declaration() {
        if($('#agreeCheckBox').is(':checked')) {
            SOP.Crud.cfxSubmit("mainForm");
        }

    }

</script>