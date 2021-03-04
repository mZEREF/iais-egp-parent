<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");


%>
<webui:setLayout name="iais-internet"/>
<%@ include file="../common/dashboard.jsp" %>
<form  method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>


    <div class="container">
        <div class="row">
            <div class="col-xs-12">

                <div class="tab-gp steps-tab">
                    <div class="tab-content">
                        <div class="tab-pane active" id="previewTab" role="tabpanel">
                            <div class="preview-gp">
                                <div class="row">
                                    <br/><br/><br/>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <h1>Apply for GIRO</h1>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        Click <a href="https://www.moh.gov.sg/docs/librariesprovider5/licensing-terms-and-conditions/ib_giro_form.pdf">here</a> to download the GIRO application form.
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        Click <a href="https://www.moh.gov.sg/docs/librariesprovider5/licensing-terms-and-conditions/ib_giro_form.pdf">here</a> to download the Authorisation Letter.
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        Click <a href="https://www.moh.gov.sg/docs/librariesprovider5/licensing-terms-and-conditions/ib_giro_form.pdf">here</a> to download the DCA form.
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</form>
<script>


</script>

