<%--
  Created by IntelliJ IDEA.
  User: JiaHao_Chen
  Date: 2019/11/13
  Time: 16:29
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>Risk Configuration</h2>
                        <div class="gray-content-box">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <div class="text-center-mobile"><a class="btn btn-primary next" onclick="javascript:configGolbalRisk();">Configuration Of Global Risk</a></div>
                                </div>
                            </div>
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <div class="text-center-mobile"><a class="btn btn-primary next" onclick="javascript:configIndividualRisk();">Configuration Of Individual Compliance Risk Assessment</a></div>
                                </div>
                            </div>
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <div class="text-center-mobile"><a class="btn btn-primary next" onclick="javascript:configFinancialRisk();">Configuration Of Financial Scheme Audit Risk Assessment</a></div>
                                </div>
                            </div>
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <div class="text-center-mobile"><a class="btn btn-primary next" onclick="javascript:configLeadershipRisk();">Configuration Of Leadership And Governance Risk Assessment</a></div>
                                </div>
                            </div>
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <div class="text-center-mobile"><a class="btn btn-primary next" onclick="javascript:configLegislativeRisk();"> Configuration Of Legislative Breaches Risk Assessment</a></div>
                                </div>
                            </div>
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <div class="text-center-mobile"><a class="btn btn-primary next" onclick="javascript:configWeightageRisk();"> Configuration OF Risk Weightage for each service</a></div>
                                </div>
                            </div>
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <div class="text-center-mobile"><a class="btn btn-primary next" onclick="javascript:configLecenceTenure();">  Configuration Of Licence Tenure Matrix</a></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</form>
<script type="text/javascript">
    function configGolbalRisk() {
        SOP.Crud.cfxSubmit("mainForm","golbalRiskConfig");
    }
    function configIndividualRisk() {
        SOP.Crud.cfxSubmit("mainForm","indivdualConfig");
    }
    function configFinancialRisk() {
        SOP.Crud.cfxSubmit("mainForm","financialConig");
    }
    function configLegislativeRisk() {
        SOP.Crud.cfxSubmit("mainForm","legistlativeConfig");
    }
    function configWeightageRisk() {
        SOP.Crud.cfxSubmit("mainForm","weightageConfig");
    }
    function configLecenceTenure() {
        SOP.Crud.cfxSubmit("mainForm","licTenureConfig");
    }
    function configLeadershipRisk(){
        SOP.Crud.cfxSubmit("mainForm","leadershipConfig");
    }
    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backLastPage");
    }
</script>
