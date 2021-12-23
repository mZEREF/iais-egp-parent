<!-- MOH-IAIS -->

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8"></meta>
  <meta http-equiv="x-ua-compatible" content="ie=edge"></meta>
  <meta name="viewport" content="width=device-width, initial-scale=1"></meta>
  <meta name="description" content=""></meta>
  <meta name="keywords" content=""></meta>
  <style type="text/css">
    @page {
      margin: 0cm 0cm -40px 0cm!important;
      size: 1400px 1970px;
    }
    body {
      font-family:"Open Sans", sans-serif;
      font-size: 14pt;
      font-weight: 400;
      line-height: 1.5;
      color: #212529;
      text-align: left;
    }
    .container {
      width: 100%;
      padding-right: 15px;
      padding-left: 15px;
      margin-right: auto;
      margin-left: auto;
    }
    .row {
      display: -ms-flexbox;
      display: flex;
      -ms-flex-wrap: wrap;
      flex-wrap: wrap;
      margin-right: -15px;
      margin-left: -15px;
    }
    *, ::after, ::before {
      box-sizing: border-box;
    }
    .container {
      /*
      width: 1280px;
      max-width: 1290px;
      */
      /*width: 100%;*/
      /*max-width: 100%;*/
    }
    .clearfix::after, .dl-horizontal dd::after, .container::after, .container-fluid::after, .row::after, .form-horizontal .form-group::after, .btn-toolbar::after, .btn-group-vertical > .btn-group::after, .nav::after, .navbar::after, .navbar-header::after, .navbar-collapse::after, .pager::after, .panel-body::after, .modal-header::after, .modal-footer::after {
      clear: both;
    }
    .col-xs-1, .col-sm-1, .col-md-1, .col-lg-1, .col-xs-2, .col-sm-2, .col-md-2, .col-lg-2, .col-xs-3, .col-sm-3, .col-md-3, .col-lg-3, .col-xs-4, .col-sm-4, .col-md-4, .col-lg-4, .col-xs-5, .col-sm-5, .col-md-5, .col-lg-5, .col-xs-6, .col-sm-6, .col-md-6, .col-lg-6, .col-xs-7, .col-sm-7, .col-md-7, .col-lg-7, .col-xs-8, .col-sm-8, .col-md-8, .col-lg-8, .col-xs-9, .col-sm-9, .col-md-9, .col-lg-9, .col-xs-10, .col-sm-10, .col-md-10, .col-lg-10, .col-xs-11, .col-sm-11, .col-md-11, .col-lg-11, .col-xs-12, .col-sm-12, .col-md-12, .col-lg-12 {
      position: relative;
      min-height: 1px;
      /*padding-right: 15px;*/
      /*padding-left: 15px;*/
    }
    .col-xs-12 {
      width: 100%;
    }
    .col-md-6 {
      -ms-flex: 0 0 50%;
      flex: 0 0 50%;
      max-width: 50%;
    }
    .col-xs-1, .col-xs-2, .col-xs-3, .col-xs-4, .col-xs-5, .col-xs-6, .col-xs-7, .col-xs-8, .col-xs-9, .col-xs-10, .col-xs-11, .col-xs-12 {
      float: left;
    }
    .center-content {
      /* padding: 10px 90px; */
      /*padding: 0px 0px;*/
    }
    .licence_box  {
      background-image: url("http://localhost:8080/main-web/img/license-mainbg.png");
      background-repeat: no-repeat;
      background-size: 100% auto;
      background-position: center top;
      /*background-attachment: fixed;*/
      /*height: 1700px;!*100%*!*/
      height: 2000px;
      padding: 30px 75px;
      width: 100%;
      position: relative;
    }
    .lic-headerbox {
      height: 230px;
    }
    .lic-title {
      text-align: center;
      margin-top: 5px;
    }
    .lic-title p {
      font-size: 14pt;
    }
    .lic-title h2 {
      font-size: 20pt;
      font-weight: 700;
      text-transform: uppercase;
      border: none;
      margin-bottom: 4px;
      padding-bottom: 0px;
    }
    .lic-number {
      text-align: center;
      margin-top: 10px;
    }
    .lic-number p {
      font-size: 14pt;
    }
    .lic-number h2 {
      font-size: 18pt;
      font-weight: 700;
      text-transform:uppercase;
      border:none;
    }
    .lic-info {
      margin-top: 15px;
    }
    .lic-info p {
      font-size: 14pt;
    }
    .lic-info h3 {
      font-size: 16pt;
      font-weight: 700;
      text-transform:uppercase;
      margin-bottom: 10px;
      padding-bottom: 0px;
      border:none;
    }
    .licence_box span {
      font-style: italic;
      font-size: 14px;
    }
    .lic-approved-info {
      background-color: #C2E7FE;
      border-radius: 14px;
      padding: 10px 15px;
      margin-bottom: 20px;
      margin-top: 20px;
    }
    .lic-approved-info p {
      margin-top: 10px;
    }
    .lic-dateinfo {
      margin-top: 20px;
    }
    .lic-dateinfo p {
      font-size: 14pt;
    }
    .lic-dateinfo h3 {
      font-size: 16pt;
      font-weight: 700;
      text-transform:uppercase;
      margin-bottom: 10px;
      padding-bottom: 0px;
      border:none;
    }
    .lic-signature-info {
      margin-top: 25px;
    }
    .lic-signature-info p {
      font-size: 14pt;
      font-weight: 700;
      text-transform: uppercase;
      margin-bottom: 0px;
    }
    .vehicle-number {
      display: inline-block;
      font-size: 16px;
      margin: 4px;
    }
    ul.v-numberlist li {
      list-style: none outside none;
    }
    ul.v-numberlist li::before {
      content: none;
    }
    .lic-footer {
      /* margin-top: 12.5%; */
      /* margin-bottom: 50px; */
      padding: 5px;
      position: absolute;
      /*bottom: 45px;*/
      bottom: 130px;
      width: 100%;
      text-align: center;
      margin-left: 70px;

    }
    .lic-footer p {
      font-size: 11pt;

    }
    .table-responsive {
      display: block;
      width: 100%;
      overflow-x: auto;
      -webkit-overflow-scrolling: touch;
    }
    .table {
      width: 100%;
      margin-bottom: 1rem;
      color: #212529;
      border-collapse: collapse;
      border-spacing: 0;
      background-color: transparent;
    }
    .table > thead > tr > th {
      vertical-align: bottom;
      border-bottom: 2px solid #ddd;
    }
    .table > thead > tr > th, .table > tbody > tr > th, .table > tfoot > tr > th, .table > thead > tr > td, .table > tbody > tr > td, .table > tfoot > tr > td {
      padding: 8px;
      line-height: 1.42857143;
      vertical-align: top;
      border: 0;
    }
    th {
      text-align: inherit;
    }
    p {
      margin: 0 0 10px;
    }
    p.note {
      font-size: 10pt;
    }
    .table > thead > tr > th, .table > tbody > tr > th, .table > tfoot > tr > th, .table > thead > tr > td, .table > tbody > tr > td, .table > tfoot > tr > td {
      line-height: 1.42857143;
    }
  </style>
</head>
<body>
<div class="main-content">
  <div class="container">
    <div class="row">
      <div class="col-xs-12">
        <div class="instruction-content center-content">
          <div class="licence_box">
            <div class="lic-headerbox"></div>
            <div class="lic-title">
              <h2><br></br></h2>
              <h2>LICENCE</h2>
              <h2>UNDER THE HEALTHCARE SERVICES ACT (HCSA)</h2>
            </div>
            <div class="lic-number">
              <p>LICENCE NO.</p>
              <h2>${licenceNo}</h2>
            </div>
            <div class="lic-info">
              <p>NAME OF LICENSEE</p>
              <h3>${licenseeName}</h3>
              <br></br>
              <p>SPECIAL LICENSABLE HEALTHCARE SERVICE</p>
              <h3>${serviceName}</h3>
              <span>${hivTesting}</span>
              <p class="note"><strong>Note:</strong>
                The licensee stated above is permitted to provide the special licensable healthcare service to which this licence relates, on the condition that the licensee must at all times also be granted a licence to provide ${baseServiceName}, being a licensable healthcare service prescribed as underlying to that special licensable healthcare service.
              </p>
            </div>
            <div class="lic-approved-info">
              <div class="table-responsive">
                <table class="table">
                  <thead>
                  <tr>
                    <td></td>
                    <td><u>BUSINESS NAME</u></td>
                    <td><u>LICENSED PREMISES <br></br>(ADDRESS)</u></td>
                    <td><u>LICENSED CONVEYANCES <br></br>(VEHICLE NO.)</u></td>
                  </tr>
                  </thead>
                  <tbody>
                  <tr>
                    <td>
                      <p></p>
                      <p>1</p>
                    </td>
                    <td>
                      <p class="visible-xs visible-sm table-row-title"></p>
                      <p style ="font-weight:bold;">${businessName}</p>
                    </td>
                    <td>
                      <p class="visible-xs visible-sm table-row-title"></p>
                      <p style ="font-weight:bold;"> ${address}</p>
                    </td>
                    <td>
                      <p class="visible-xs visible-sm table-row-title"></p>
                      <p>
                      <ul class="v-numberlist">
                      ${vehicleNo}
                      </ul>
                      </p>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <div class="lic-footer">
              <p style = "text-align: left;">This licence is issued under the Healthcare Services Act 2020 and is subject to its provisions, as well as any regulations, rules, code of practices and directions issued under it, and any conditions imposed by the Director of Medical Services. The licence is valid from the licence start date until the licence end date, unless revoked, suspended, ceased or surrendered.</p>
              <p style = "text-align: center;">Ministry of Health | Page 1 of 2</p>
            </div>
          </div>
        </div>
      </div>
    </div>


    <!-- License 2 -->
    <div class="row">
      <div class="col-xs-12">
        <div class="instruction-content center-content">
          <div class="licence_box">
            <div class="lic-headerbox"></div>
            <div class="lic-title">
              <h2><br></br></h2>
              <h2>LICENCE</h2>
              <h2>UNDER THE HEALTHCARE SERVICES ACT (HCSA)</h2>
            </div>
            <div class="lic-number">
              <p>Licence No.</p>
              <h2>${licenceNo}</h2>
            </div>
            <div class="lic-approved-info">
              <div class="table-responsive">
                <table class="table">
                  <thead>
                  <tr>
                    <td></td>
                    <td><u>BUSINESS NAME</u></td>
                    <td><u>LICENSED PREMISES <br></br>(ADDRESS)</u></td>
                    <td><u>LICENSED CONVEYANCES <br></br>(VEHICLE NO.)</u></td>
                  </tr>
                  </thead>
                  <tbody>
                  <tr>
                    <td>
                      <p></p>
                      <p>1</p>
                    </td>
                    <td>
                      <p class="visible-xs visible-sm table-row-title"></p>
                      <p style ="font-weight:bold;">${businessName}</p>
                    </td>
                    <td>
                      <p class="visible-xs visible-sm table-row-title"></p>
                      <p style ="font-weight:bold;"> ${address}</p>
                    </td>
                    <td>
                      <p class="visible-xs visible-sm table-row-title"></p>
                      <p>
                      <ul class="v-numberlist">
                      ${vehicleNo2}
                      </ul>
                      </p>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div class="lic-dateinfo">
              <table width = "100%" >
                <tr align = "left">
                  <td width = "50%">
                    <div class="col-xs-12 col-md-6" style="padding-left: 0px">
                      <p>LICENCE START DATE</p>
                      <h3>${startDate}</h3>
                    </div>
                  </td>
                  <td width = "50%" align = "left">
                    <div class="col-xs-12 col-md-6">
                      <p>LICENCE END DATE</p>
                      <h3>${endDate}</h3>
                    </div>
                  </td>
                </tr>
              </table>
            </div>
            <div class="lic-signature-info">
              <p>DIRECTOR OF MEDICAL SERVICES <br></br>
                SINGAPORE </p>
              <span>This is a computer -generated document. No Signature is required</span>
            </div>
            <div class="lic-footer" >
              <p style = "text-align: left;">This licence is issued under the Healthcare Services Act 2020 and is subject to its provisions, as well as any regulations, rules, code of practices and directions issued under it, and any conditions imposed by the Director of Medical Services. The licence is valid from the licence start date until the licence end date, unless revoked, suspended, ceased or surrendered.</p>
              <p style = "text-align: center;">Ministry of Health | Page 2 of 2</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>


</body>
</html>
