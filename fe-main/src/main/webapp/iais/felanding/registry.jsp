<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 2/28/2020
  Time: 10:43 AM
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

<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <div class="dashboard" style="background-image:url('/web/themes/fe/img/Masthead-banner.jpg')">
      <div class="container" >
        <div class="row">
          <div class="col-xs-12">
            <div class="prelogin-title">
              <h1>Integrated Application and <br class="hidden-xs"> Inspection System (IAIS)</h1>
            </div>
          </div>
        </div>
      </div>
    </div>

    <p><a class="component-desc" style="color: black" href="javascript:void(0)" onclick="Utils.submit('mainForm', 'doBack')"><i class="fa fa-chevron-left"></i> Back to Home</a></p>
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
                          <div class="self-assessment-item completed">
                            <div class="amendLicence">
                              <div class="form-check-gp">
                                <p class="form-check-title">Do you (or your organisation) have a UEN number?<a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" title="" data-original-title="" aria-describedby="tooltip883061">i</a></p>

                                <div class="form-check progress-step-check">
                                  <input class="form-check-input" id="updatePremise" type="radio" name="amendLicenceType" aria-invalid="false">
                                  <label class="form-check-label" for="updatePremise">
                                    <span class="check-circle"></span><span class="left-content">Yes</span>
                                  </label>
                                </div>
                                <div class="form-check progress-step-check">
                                  <input class="form-check-input" id="updateService" type="radio" name="amendLicenceType" aria-invalid="false">
                                  <label class="form-check-label" for="updateService"><span class="check-circle"></span>
                                    <span class="left-content">No</span>
                                  </label>
                                </div>
                              </div>
                            </div>
                          </div>
                          <div class="self-assessment-item">
                            <div class="updateService hidden">
                              <div class="form-check-gp">
                                <p class="form-check-title">You may proceed with SingPass for licence application purposes and you will be allocated a UEN number on approval of your application.</p>
                                <a class="btn btn-primary corpPass" href="javascript:void(0)" onclick="Utils.submit('mainForm','singpassLogin')" style="background: #1F92FF; color: white">Login using SingPass</a>
                              </div>
                            </div>
                            <div class="updatePremise hidden">
                              <div class="form-check-gp">
                                <p class="form-check-title">If you (or your organisation) have a UEN number, we recommend that you register for a CorpPass to access all the digital services available on IAIS.</p>

                                <a class="btn btn-primary corpPass"  href="https://www.corppass.gov.sg/cpauth/login/homepage?TAM_OP=login" style="background: #1F92FF; color: white"  >Register for CorpPass</a>

                              </div>
                            </div>
                          </div>
                        </div>
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
</div>

<%@include file="/include/utils.jsp"%>
