<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<div class="main-content">
    <div class="container">
        <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
            <br>
            <div class="navigation-gp">
                <%@ include file="common/dashboardDropDown.jsp" %>
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1>New Application</h1>
                    </div>
                </div>
            </div>

            <div class="instruction-content center-content">
                <h2>SERVICES SELECTED</h2>
                <label>MOH-Approved Facility Certifier</label>

                <div class="gray-content-box">
                    <div class="h3-with-desc">
                        <h3>Company Info</h3>
                    </div>
                    <div class="license-info-gp">
                        <div class="license-info-row">
                            <div class="licnese-info">
                                <p>UEN Number: xxxxx</p>
                            </div>
                            <div class="licnese-info">
                                <p>Company Name: xxxxx</p>
                            </div>
                            <div class="licnese-info">
                                <p>Block: xxx</p>
                            </div>
                            <div class="licnese-info">
                                <p>Street Name: xxxx</p>
                            </div>
                            <div class="licnese-info">
                                <p>Floor and Unit No.: xxx-xxx</p>
                            </div>
                            <div class="licnese-info">
                                <p>Postal Code: xxxxxx</p>
                            </div>
                        </div>
                    </div>
                </div>
                <h3>Before You Begin</h3>
                <ul class="">
                    <li>
                        <p>This form will take approximately 10 mins to complete. You may save your progress at any time
                            and resume your application later</p>
                    </li>
                </ul>
                <div class="application-tab-footer">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a class="back" href="#" onclick="doBack()"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="text-right text-center-mobile"><a class="btn btn-primary next"
                                                                          onclick="doNext()" data-toggle="modal"
                                                                          data-target="#saveDraft"
                                                                          href="javascript:void(0);">Proceed</a></div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<script>
    function doNext() {
        showWaiting();
        $("#mainForm").submit();
    }

    // function doBack() {
    //     showWaiting();
    //     $("#mainForm").submit();
    // }
</script>