<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<div class="col-xs-12">
  <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
    <div class="panel panel-default">
      <div class="panel-heading" id="headingOne" role="tab">
        <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne" class="">Filter</a></h4>
      </div>
      <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
        <div class="panel-body">
          <div class="panel-main-content">
            <form class="form-inline">
              <div class="form">
                <label class="col-xs-12 col-md-3 control-label" for="hciCode">HCI Code</label>
                <div class="col-xs-12 col-md-3">
                  <input id="hciCode" name="hciCode" type="text">
                </div>
              </div>
              <div class="form">
                <label class="col-xs-12 col-md-3 control-label" for="ApplicationNo">Application No.</label>
                <div class="col-xs-12 col-md-3">
                  <input id="ApplicationNo" name="ApplicationNo" type="text">
                </div>
              </div>
            </form>
            <form class="form-inline">
              <div class="form">
                <label class="col-xs-12 col-md-3 control-label" for="hciName">HCI Name</label>
                <div class="col-xs-12 col-md-3">
                  <input id="hciName" name="hciName" type="text">
                </div>
              </div>
              <div class="form">
                <label class="col-xs-12 col-md-3 control-label" for="applicationType">Application Type</label>
                <div class="col-xs-12 col-md-3" style="margin-bottom: 15px;">
                  <select id="applicationType">
                    <option selected="" value="Please Choose">Please Choose</option>
                    <option value="New">New</option>
                    <option value="Renewal">Renewal</option>
                    <option value="Request for change">Request for change</option>
                  </select>
                </div>
              </div>
            </form>
            <form class="form-inline">
              <div class="form">
                <label class="col-xs-12 col-md-3 control-label" for="hciAddress">HCI Address</label>
                <div class="col-xs-12 col-md-3">
                  <input id="hciAddress" name="hciAddress" type="text">
                </div>
              </div>
              <div class="form">
                <label class="col-xs-12 col-md-3 control-label" for="applicationStatus">Application Status</label>
                <div class="col-xs-12 col-md-3" style="margin-bottom: 15px;">
                  <select id="applicationStatus" style="display: none;" s>
                    <option selected="" value="Please Choose">Please Choose</option>
                    <option value="In Progress">In Progress</option>
                    <option value="Pending Admin Screening">Pending Admin Screening</option>
                  </select>
                </div>
              </div>
            </form>
            <form class="form-inline">
              <div class="form">
                <label class="col-xs-12 col-md-3 control-label" for="inspectionType">Inspection Type</label>
                <div class="col-xs-12 col-md-3">
                  <select id="inspectionType" style="display: none;">
                    <option selected="" value="Please Choose">Please Choose</option>
                    <option value="Pending Admin Screening">Pending Admin Screening</option>
                    <option value="Post-Inspection">Post-Inspection</option>
                  </select>
                </div>
              </div>
              <div class="form">
                <label class="col-xs-12 col-md-3 control-label" for="licencePeriod">Licence Period</label>
                <div class="col-xs-12 col-md-3">
                  <iais:body >
                  <iais:row>
                    <iais:value width="10">
                      <iais:datePicker id = "licencePeriod" name = "licencePeriod" value="20/10/2019"></iais:datePicker>
                    </iais:value>
                  </iais:row>
                  </iais:body>
                </div>
              </div>
            </form>
            <form class="form-inline">
              <div class="form">
                <label class="col-xs-12 col-md-3 control-label" for="inspectorName">Inspector Name</label>
                <div class="col-xs-12 col-md-3">
                  <select id="inspectorName" style="display: none;">
                    <option selected="" value="Please Choose">Please Choose</option>
                    <option value="Marry">Marry</option>
                    <option value="Jenny">Jenny</option>
                    <option value="Valerie">Valerie</option>
                  </select>
                </div>
              </div>
            </form>
          </div>
        </div>
        <div class="panel-body" align="center">
        <form class="form-inline">
          <div class="form">
            <div class="col-xs-10 col-md-3">
              <a class="btn btn-secondary" style="background-color: #2199E8;">Search</a>
            </div>
          </div>
          <div class="form">
            <div class="col-xs-10 col-md-3">
              <a class="btn btn-secondary" style="background-color: #2199E8;">Clear</a>
            </div>
          </div>
        </form>
        </div>
      </div>
    </div>
  </div>
  <div class="components">
    <div class="table-gp">
      <table class="table">
        <thead>
        <tr>
          <th>S/N</th>
          <th>Application No.</th>
          <th>Application Type</th>
          <th>HCI Code</th>
          <th>HCI Name / Address</th>
          <th>Service Name</th>
          <th>Licence Expiry Date (dd/mm/yyyy)</th>
          <th>Inspection Date (dd/mm/yyyy)</th>
          <th>Inspection Type</th>
          <th>Inspector</th>
          <th>Inspection Lead</th>
        </tr>
        </thead>
        <tbody>

        </tbody>
      </table>
      <div class="table-footnote">
        <div class="row">
          <div class="col-xs-6 col-md-4">
            <p class="count">5 out of 25</p>
          </div>
          <div class="col-xs-6 col-md-8 text-right">
            <div class="nav">
              <ul class="pagination">
                <li class="hidden"><a href="#" aria-label="Previous"><span aria-hidden="true"><i class="fa fa-chevron-left"></i></span></a></li>
                <li class="active"><a href="#">1</a></li>
                <li><a href="#">2</a></li>
                <li><a href="#">3</a></li>
                <li><a href="#" aria-label="Next"><span aria-hidden="true"><i class="fa fa-chevron-right"></i></span></a></li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>