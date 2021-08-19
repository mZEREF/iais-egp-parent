<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<div class="main-content" style="min-height: 73vh;">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <iais:body>
                        <iais:section title="" id = "demoList">
                            <br>
                            <div class="row">
                                <div class="col-xs-10 col-md-12">
                                    <div class="components">
                                        <a class="btn btn-secondary" data-toggle="collapse" name="filterBtn"
                                           data-target="#beInboxFilter">Filter</a>
                                    </div>
                                </div>
                            </div>
                            <p></p>
                            <div id = "beInboxFilter" class="collapse">
                                <iais:row>
                                    <iais:field value="Application No.:"/>
                                    <iais:value width="18">
                                        <input type="text" name="application_no" value=""/>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Application Type:"/>
                                    <iais:value width="18">
                                        <iais:select name="application_type" options="appTypeOption" cssClass="application_type"
                                                     firstOption="Please Select" needSort="true"
                                                     value=""></iais:select>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Process Type:"/>
                                    <iais:value width="18">
                                        <iais:select name="application_type" options="appTypeOption" cssClass="application_type"
                                                     firstOption="Please Select" needSort="true"
                                                     value=""></iais:select>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Facility Type:"/>
                                    <iais:value width="18">
                                        <iais:select name="facility_type" options="appTypeOption" cssClass="application_type"
                                                     firstOption="Please Select" needSort="true"
                                                     value=""></iais:select>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Agents/Toxins:"/>
                                    <iais:value width="18">
                                        <input type="text" name="Agents/Toxins" value=""/>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Application Status:"/>
                                    <iais:value width="18">
                                        <iais:select name="application_status" options="appStatusOption"
                                                     cssClass="application_status"
                                                     firstOption="Please Select"
                                                     value=""></iais:select>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:action style="text-align:right;">
                                        <button class="btn btn-secondary" type="button" id="clearBtn" name="clearBtn">Clear</button>
                                        <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">Search</button>
                                    </iais:action>
                                </iais:row>
                            </div>
                        </iais:section>
                        <h3>
                            <span>Search Results</span>
                        </h3>
                        <div class="table-gp">
                            <table class="table application-group" style="border-collapse:collapse;">
                                <thead>
                                <tr>
                                    <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="GROUP_NO" value="Application No."></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="APP_TYPE" value="Application Type"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="COU" value="Process Type"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="COU" value="Facility Type"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="SUBMIT_DT" value="Facility Name/Address"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="SUBMIT_DT" value="Agents/Toxins"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="PMT_STATUS" value="Submission Date"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="PMT_STATUS" value="Application Status"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="PMT_STATUS" value="Facility/Approval Expiry Date"></iais:sortableHeader>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>1</td>
                                    <td><a id="app001">App001</a></td>
                                    <td>New</td>
                                    <td>Facility Registration</td>
                                    <td>Uncertified Facility (FSF)</td>
                                    <td>xxx</td>
                                    <td>xxx</td>
                                    <td>14/07/2021</td>
                                    <td>Pending Higher Management</td>
                                    <td>14/09/2021</td>
                                </tr>
                                <tr>
                                    <td>2</td>
                                    <td><a id="app002">App002</a></td>
                                    <td>New</td>
                                    <td>Facility Registration</td>
                                    <td>Uncertified Facility (FSF)</td>
                                    <td>xxx</td>
                                    <td>xxx</td>
                                    <td>14/07/2021</td>
                                    <td>Pending Higher Management</td>
                                    <td>14/09/2021</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </iais:body>
                </div>
            </div>
        </div>
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp"%>
    <%@include file="/WEB-INF/jsp/include/utils.jsp"%>
</div>
<script type="text/javascript">
    $("#app001").click(function (){
        SOP.Crud.cfxSubmit("mainForm", "PrepareScreening");
    });
    $("#app002").click(function (){
        SOP.Crud.cfxSubmit("mainForm", "PrepareProcessing");
    });

</script>