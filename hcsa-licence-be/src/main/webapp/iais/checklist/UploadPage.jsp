<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 12/30/2019
  Time: 4:02 PM
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
<style>
</style>

<form  method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
<%@ include file="/include/formHidden.jsp" %>
  <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.HcsaChklItemDelegator"/>
  <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.validation.HcsaChklItemValidate"/>
  <input type="hidden" name="valProfiles" id="valProfiles" value=""/>


  <div class="main-content">
    <div class="container">
      <div class="row">

        <br><br><br>
        <div class="col-xs-12">
          <div class="tab-gp steps-tab">
            <div class="tab-content">
              <div class="tab-pane active" id="documentsTab" role="tabpanel">
                <div class="document-info-list">
                  <ul>
                    <li>
                      <p>The maximum file size for each upload is 4MB. </p>
                    </li>
                    <li>
                      <p>Acceptable file formats are PDF, JPG and PNG. </p>
                    </li>
                    <li>
                      <p>All files are mandatory.</p>
                    </li>
                  </ul>
                </div>


                <div class="document-upload-gp">
                  <h2>PRIMARY DOCUMENTS</h2>
                  <div class="document-upload-list">
                    <h3>Fire Safety Certificate (FSC) from SCDF</h3>
                    <p><a href="#" target="_blank">Preview</a></p>
                  </div>
                  <div class="document-upload-list">
                    <h3>Urban Redevelopmenet Authority (URA) grant of written permission</h3>
                    <div class="file-upload-gp">
                      <input id="selectedFile" name="selectedFile" type="file" style="display: none;" aria-label="selectedFile1"><a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
                    </div>
                  </div>
                </div>

                <div class="application-tab-footer">
                  <div class="row">
                    <div class="col-xs-12 col-sm-6">
                      <p><a id = "docBack" class="back"><em class="fa fa-angle-left"></em> Back</a></p>
                    </div>
                    <div class="col-xs-12 col-sm-6">
                      <div class="button-group"><a class="btn btn-primary next" id="docNext">Next</a></div>
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
<script src=/systemadmin/js/CommonUtils.js'></script>
<script>

    $('#selectedFile').change(function () {
        var file = $(this).val();

    });


    $('#docNext').click(function () {
        SOP.Crud.cfxSubmit("mainForm", "submitUploadData");
    });
</script>