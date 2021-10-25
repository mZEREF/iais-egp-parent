<!DOCTYPE html>
<html>
<body>
<div class="main-content">
  <div class="container">
    <div class="row">
      <div class="col-xs-12">
        <div class="instruction-content center-content">
          <div class="licence_box">
            <div class="lic-number">
              <p>Licence No.</p>
              <h2>${licenceNo}</h2>
            </div>
            <div class="lic-info">
              <p>Name of Licensee</p>
              <h3>${licenseeName}</h3>
              <br></br>
              <p>Service Licence</p>
              <h3>${serviceName}</h3>
              <span>${hivTesting}</span>
            </div>
            <div class="lic-approved-info">
              <div class="table-responsive">
                <table class="table" width = "100%">
                  <thead>
                  <tr>
                    <th scope="col" width = "10%"></th>
                    <th scope="col" width = "30%" align = "left">Business Name</th>
                    <th scope="col" width = "30%" align = "left">Licensed Premises <br/>(ADDRESS)</th>
                    <th scope="col" width = "30%" align = "left">Licensed Conveyances <br/>(Vehicle No.)</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr align = "left">
                    <td>
                      <p><br/></p>
                      <p>1</p>
                    </td>
                    <td>
                      <p class="visible-xs visible-sm table-row-title"><br/></p>
                      <p>${businessName}</p>
                    </td>
                    <td>
                      <p class="visible-xs visible-sm table-row-title"><br/></p>
                      <p>${address}</p>
                    </td>
                    <td>
                      <p class="visible-xs visible-sm table-row-title"><br/></p>
                      <p>${vehicleNo}</p>
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
                    <div class="col-xs-12 col-md-6">
                      <p>Licence Start Date</p>
                      <h3>${startDate}</h3>
                    </div>
                  </td>
                  <td width = "50%" align = "left">
                    <div class="col-xs-12 col-md-6">
                      <p>Licence End Date</p>
                      <h3>${endDate}</h3>
                    </div>
                  </td>
                </tr>
              </table>

            </div>
            <br></br>
            <div class="lic-signature-info">
              <p>DIRECTOR OF MEDICAL SERVICES<br/>
                SINGAPORE </p>
              <span>This is a computer -generated document. No Signature is required</span>
            </div>
            <br></br>
            <br></br>
            <br></br>
            <br></br>
            <br></br>
            <div class="lic-footer">
              <p>This licence is issued under the Healthcare Services Act 2020 and is subject to its provisions, as well as any regulations, rules, code of practices and directions issued under it, and any conditions imposed by the Director of Medical Services. The licence is valid from the licence start date until the licence end date, unless revoked, suspended, ceased or surrendered.</p>
              <p>Ministry of Health | Page 1 of 1</p>
            </div>

          </div>


        </div>
      </div>
    </div>
  </div>
</div>
</body>
<head>
  <meta charset="utf-8"></meta>
  <meta http-equiv="x-ua-compatible" content="ie=edge"></meta>
  <meta name="viewport" content="width=device-width, initial-scale=1"></meta>
  <meta name="description" content=""></meta>
  <meta name="keywords" content=""></meta>
  <style type="text/css">
    @page {
      size: 1400px 2030px;
    }
    * {
      page-break-inside: always;
    }

    /*****************28 April 2021*******************/

    .licence_box  {
      background-image: url("http://localhost:8080/main-web/img/license-mainbg.png");
      background-repeat: no-repeat;
      background-size: 100% auto;
      background-position: center top;
      /*background-attachment: fixed;*/
      height: 100%;
      padding: 30px 75px;
      width: 100%;
      margin-left: -70px;
    }
    .lic-number {
      text-align: center;
      margin-top: 420px;
    }
    .lic-number p {
      font-size: 14pt;
    }
    .lic-number h2 {
      font-size: 24pt;
      font-weight: 700;
      text-transform:uppercase;
      border:none;
    }
    .lic-info {
      margin-top: 35px;
      margin-left: 20px;
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
      /*background-color: rgba(130, 195, 240, 0.3);*/
      background-color: #C2E7FE;
      border-radius: 14px;
      padding: 20px;
      margin-bottom: 20px;
      margin-top: 20px;
      margin-left: 20px;
      margin-right: 20px;
    }
    .lic-approved-info p {
      margin-top: 10px;
      word-wrap:break-word;
      word-break:break-all;
    }
    .lic-dateinfo {
      margin-top: 20px;
      margin-left: 20px;
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
      margin-top: 110px;
      margin-left: 20px;
    }
    .lic-signature-info p {
      font-size: 14pt;
      font-weight: 700;
      text-transform:uppercase;
    }
    .vehicle-number {
      display: inline-block;
      font-size: 16px;
      margin: 4px;
    }
    .lic-footer {
      margin-top: 150px;
      margin-bottom: 50px;
      padding: 5px;
      margin-left: 20px;
      margin-right: 20px;
    }
    .lic-footer p {
      font-size: 11pt;
      text-align: center;
    }

    /*! MOH-IAIS 13-09-2019 */

    @charset "UTF-8";
    html {
      padding-bottom: 0;
    }

    @media only screen and (min-width : 1201px) {
      .container {
        width: 1280px;
      }
    }

    .section table.control-grid > tbody > tr:nth-child(odd),
    .section table.control-grid > tbody > tr:nth-child(even) {
      background-color: white !important;
    }

    .component-gp {
      border-bottom: 2px dotted #c0c0c0;
      padding-bottom: 50px;
    }
    .component-gp .components > * {
      margin-bottom: 50px;
    }

    .ui-block .ui-block-item {
      width: 210px;
      margin-right: 15px;
      display: inline-block;
      margin-bottom: 25px;
      vertical-align: top;
    }
    .ui-block .ui-block-item .color-block {
      width: 100%;
      height: 110px;
      padding: 20px;
      margin-bottom: 12px;
    }
    .ui-block .ui-block-item .color-block p {
      color: white;
      font-size: 1.4rem;
      margin-bottom: 7px;
    }
    .ui-block .ui-block-item .color-small-block {
      width: 100%;
      height: 30px;
      padding: 5px 20px;
      margin-bottom: 12px;
    }
    .ui-block .ui-block-item .color-small-block p {
      margin-bottom: 0;
    }
    .ui-block .ui-block-item p {
      font-size: 1.4rem;
      margin-bottom: 7px;
    }

    body {
      font-family: "Open Sans", sans-serif;
      font-size: 16px;
      -ms-text-size-adjust:100%;
      -webkit-text-size-adjust:100%
    }

    html {
      font-size: 10px;
    }

    * {
      font-family: "Open Sans", sans-serif;
    }

    h1 {
      font-size: 5.2rem;
      font-family: "Open Sans", sans-serif;
      color: #333333;
      font-weight: 600;
      margin-top: 0;
      margin-bottom: 20px;
    }
    @media only screen and (max-width : 767px) {
      h1 {
        font-size: 2.8rem;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      h1 {
        font-size: 5.2rem;
      }
    }

    h2 {
      font-size: 2.2rem;
      font-family: "Open Sans", sans-serif;
      border-bottom: 1px solid #D1D1D1;
      margin-top: 0;
      margin-bottom: 15px;
      padding-bottom: 20px;
      font-weight: 600;
    }
    @media only screen and (max-width : 767px) {
      h2 {
        font-size: 1.8rem;
        padding-bottom: 15px;
      }
    }
    h2.component-title {
      font-size: 3rem;
      border-bottom: 0;
      padding-bottom: 0;
    }
    @media only screen and (max-width : 767px) {
      h2.component-title {
        font-size: 2.2rem;
        text-align: center;
      }
    }
    h2.service-title {
      border-bottom: 0;
      margin-bottom: 50px;
      padding-bottom: 0;
    }
    @media only screen and (max-width : 767px) {
      h2.service-title {
        padding-bottom: 0;
        text-align: center;
        margin-bottom: 30px;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      h2.service-title {
        margin-top: 10px;
        text-align: center;
        margin-bottom: 30px;
      }
    }

    h3 {
      font-size: 2rem;
      font-family: "Open Sans", sans-serif;
      border-bottom: 1px solid #D1D1D1;
      margin-top: 0;
      margin-bottom: 15px;
      padding-bottom: 20px;
      font-weight: 600;
    }
    @media only screen and (max-width : 767px) {
      h3 {
        font-size: 1.6rem;
        padding-bottom: 15px;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      h3 {
        font-size: 2rem;
      }
    }
    h3.without-header-line {
      padding-bottom: 0;
      border-bottom: 0;
    }

    .h3-with-desc {
      padding-bottom: 10px;
      border-bottom: 1px solid #D1D1D1;
    }
    .h3-with-desc h3 {
      border-bottom: 0;
      padding-bottom: 0;
      margin-bottom: 12px;
    }

    p {
      font-family: "Open Sans", sans-serif;
      font-size: 16px;
    }
    @media only screen and (max-width : 767px) {
      p {
        font-size: 1.4rem;
      }
    }
    p.component-desc {
      font-size: 2rem;
    }
    @media only screen and (max-width : 767px) {
      p.component-desc {
        font-size: 1.4rem;
        text-align: center;
      }
    }
    p.dashboard-small-txt {
      font-size: 1.1rem;
    }
    p.small-txt {
      font-size: 1.4rem;
    }
    p a {
      color: #147aab;
      text-decoration: underline;
    }
    p a:hover  {
      text-decoration: none;
      color: #ff6600;
    }
    p a:focus, p a:active {
      text-decoration: none;
      color: #ff6600;
    }
    p.back {
      margin-bottom: 0;
    }
    p.back a {
      line-height: 46px;
    }
    p.addmoreOfficer {
      margin-bottom: 30px;
    }

    a {
      color: #147aab;
    }
    a:hover {
      text-decoration: none;
      color: #333;
      cursor: pointer;
    }
    a:focus, a:active {
      text-decoration: none;
      color: #ff6600;
    }

    .print i.fa.fa-print {
      margin-right: 5px;
    }

    .masthead-para p {
      font-size: 2rem;
    }
    @media only screen and (max-width : 767px) {
      .masthead-para p {
        font-size: 1.6rem;
        text-align: center;
      }
    }

    .preview-gp .panel-group {
      margin-top: 20px;
    }

    .txt-red {
      color: #D22727;
    }

    .amentment-txt {
      background-color: #E6F1D9;
      padding: 10px 15px;
      border-radius: 14px;
    }

    .fileupload-txt {
      background-color: #ececec;
      padding: 10px 15px;
      border-radius: 14px;
      margin-bottom: 10px;
    }
    .file-error {
      border: 1px solid #D22727;
    }
    .wrong-file-text {
      float: right;
      position: relative;
    }
    .attach-preview {
      padding: 10px 5px ;
      border-bottom: 1px solid #ededed;
    }
    .attach-preview a {
      margin-right: 10px;
    }
    ul {
      padding-left: 0;
    }
    ul li {
      list-style: none;
      padding-left: 16px;
      position: relative;
      font-size: 1.6rem;
      margin-bottom: 10px;
    }
    ul li:before {
      content: "";
      width: 6px;
      height: 6px;
      background-color: #333333;
      border-radius: 3px;
      position: absolute;
      top: 9px;
      left: 0;
    }
    ul.service-list li {
      font-size: 2rem;
    }
    ul.service-list li span {
      font-weight: 600;
    }
    @media only screen and (max-width : 767px) {
      ul.service-list li {
        font-size: 1.6rem;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      ul.service-list li {
        font-size: 2rem;
      }
    }

    @media only screen and (max-width : 767px) {
      h1, h2, h3, h4, p {
        text-align: left;
      }

      .servive-subtitle {
        margin-bottom: 30px;
      }

      .servive-subtitle h3,
      .servive-subtitle p {
        text-align: center;
        color: #333333;
      }

      .servive-subtitle h3 {
        border-bottom: 0;
        padding-bottom: 0;
        margin-bottom: 20px;
      }

      .servive-subtitle p {
        margin-bottom: 5px;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      h1, h2, h3, h4, p {
        text-align: left;
      }

      .servive-subtitle {
        margin-bottom: 30px;
      }

      .servive-subtitle h3,
      .servive-subtitle p {
        text-align: center;
      }

      .servive-subtitle h3 {
        border-bottom: 0;
        padding-bottom: 0;
        margin-bottom: 20px;
      }

      .servive-subtitle p {
        margin-bottom: 5px;
      }
    }
    header {
      padding-top: 15px;
      padding-bottom: 15px;
      -webkit-box-shadow: 0px 4px 6px 0px rgba(0, 0, 0, 0.25);
      -moz-box-shadow: 0px 4px 6px 0px rgba(0, 0, 0, 0.25);
      box-shadow: 0px 4px 6px 0px rgba(0, 0, 0, 0.25);
      position: relative;
      z-index: 100;
    }
    header .container .logo-img a {
      text-decoration: none;
      display: inline-block;
    }
    header .container .logo-img a:hover, header .container .logo-img a:focus, header .container .logo-img a:active {
      text-decoration: none;
      outline: 0;
    }
    header .container .logo-img img {
      width: 235px;
      display: inline-block;
      vertical-align: middle;
      height: auto;
      max-width: 100%;
    }
    header .container .logo-img p {
      margin-bottom: 0;
      color: #00486B;
      display: inline-block;
      /*width: 210px;*/
      margin-left: 22px;
      padding-left: 22px;
      border-left: 1px solid #A8A8A8;
      vertical-align: middle;
    }
    @media only screen and (max-width : 767px) {
      header .container .logo-img img {
        width: 140px;
        height: auto;
      }
      header .container .logo-img p {
        font-size: 11px;
        width: 130px;
        margin-left: 8px;
        padding-left: 8px;
      }
    }
    @media only screen and (max-width: 370px) and (min-width: 320px) {
      header .container .logo-img img {
        width: 120px;
      }
      header .container .logo-img p {
        margin-left: 7px;
        padding-left: 7px;
        font-size: 9px;
        width: 105px;
      }
    }
    header .container .col-xs-2.col-lg-6 {
      text-align: right;
      margin-top: 11px;
    }
    @media only screen and (max-width: 1200px) {
      header .container .col-xs-2.col-lg-6 {
        margin-top: 0px;
      }
    }
    header .container ul.list-inline {
      display: inline-block;
    }
    header .container ul.list-inline > li {
      padding-right: 3px;
      padding-left: 3px;
    }
    header .container ul.list-inline > li:before {
      content: none;
    }
    header .container ul.list-inline .site-fontsizer-cont .fontsizer {
      width: 28px;
      height: 28px;
      display: block;
      background-color: #EFEFEF;
      color: #333333;
      border-radius: 8px;
      text-align: center;
      line-height: 28px;
    }
    @media only screen and (max-width : 1200px) {
      header .container ul.list-inline {
        display: none;
      }
    }
    header .container .sg-gov-logo {
      display: inline-block;
      margin-left: 50px;
    }
    header .container .sg-gov-logo a {
      display: inline-block;
    }
    header .container .sg-gov-logo a img {
      width: 270px;
      display: inline-block;
    }
    @media only screen and (max-width : 1200px) {
      header .container .sg-gov-logo {
        display: none;
      }
    }
    @media only screen and (min-width : 1201px) {
      header .container {
        width: 1280px;
      }
    }
    @media only screen and (max-width : 1200px) {
      header {
        padding-top: 12px;
        padding-bottom: 12px;
      }
      header .menu-icon {
        width: 27px;
        height: 18px;
        display: block;
        margin-left: auto;
        margin-top: 25px;
      }
      header .menu-icon .icon-bar {
        width: 27px;
        display: inline-block;
        -webkit-transition: all 0.3s ease;
        -moz-transition: all 0.3s ease;
        -ms-transition: all 0.3s ease;
        -o-transition: all 0.3s ease;
        transition: all 0.3s ease;
        height: 18px;
        position: relative;
      }
      header .menu-icon .icon-bar:before, header .menu-icon .icon-bar:after {
        content: "";
        position: absolute;
        width: 27px;
        height: 2px;
        background-color: #333333;
        left: 0;
        -webkit-transition: all 0.25s ease;
        -moz-transition: all 0.25s ease;
        -ms-transition: all 0.25s ease;
        -o-transition: all 0.25s ease;
        transition: all 0.25s ease;
        -webkit-transform: rotate(0);
        -moz-transform: rotate(0);
        -ms-transform: rotate(0);
        -o-transform: rotate(0);
        transform: rotate(0);
      }
      header .menu-icon .icon-bar:before {
        top: 0;
        -webkit-box-shadow: 0 16px 0 #333333;
        box-shadow: 0 16px 0 #333333;
      }
      header .menu-icon .icon-bar:after {
        top: 50%;
        margin-top: -1px;
      }
      header .menu-icon.open .icon-bar:before {
        top: 50%;
        margin-top: -1.5px;
        -webkit-transform: rotate(225deg);
        -moz-transform: rotate(225deg);
        -ms-transform: rotate(225deg);
        -o-transform: rotate(225deg);
        transform: rotate(225deg);
        -webkit-box-shadow: none;
        box-shadow: none;
      }
      header .menu-icon.open .icon-bar:after {
        -webkit-transform: rotate(-45deg);
        -moz-transform: rotate(-45deg);
        -ms-transform: rotate(-45deg);
        -o-transform: rotate(-45deg);
        transform: rotate(-45deg);
      }
    }
    @media only screen and (max-width : 767px) {
      header .menu-icon {
        width: 18px;
        height: 14px;
        margin-top: 13px;
      }
      header .menu-icon .icon-bar {
        width: 18px;
        height: 14px;
      }
      header .menu-icon .icon-bar:before, header .menu-icon .icon-bar:after {
        width: 18px;
      }
      header .menu-icon .icon-bar:before {
        -webkit-box-shadow: 0 12px 0 #333333;
        -moz-box-shadow: 0 12px 0 #333333;
        box-shadow: 0 12px 0 #333333;
      }
    }
    @media only screen and (min-width : 993px) and (max-width : 1200px) {
      header .menu-icon {
        width: 25px;
        margin-top: 23px;
      }
      header .menu-icon .icon-bar {
        width: 25px;
      }
      header .menu-icon .icon-bar:before, header .menu-icon .icon-bar:after {
        width: 25px;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      header .menu-icon {
        width: 25px;
        margin-top: 23px;
      }
      header .menu-icon .icon-bar {
        width: 25px;
      }
      header .menu-icon .icon-bar:before, header .menu-icon .icon-bar:after {
        width: 25px;
      }
    }

    .navigation-gp {
      margin-bottom: 40px;
    }

    .navigation .nav.nav-tabs.nav-menu {
      border-bottom: 0;
    }
    .navigation .nav.nav-tabs.nav-menu li {
      margin-bottom: 0;
      padding-left: 0;
    }
    .navigation .nav.nav-tabs.nav-menu li:before {
      content: none;
    }
    .navigation .nav.nav-tabs.nav-menu li a {
      color: #333333;
      font-size: 1.8rem;
      border: 0;
      margin-right: 0;
      padding: 10px 23px;
      font-weight: 600;
      background-color: transparent;
    }
    .navigation .nav.nav-tabs.nav-menu li a span {
      position: relative;
    }
    .navigation .nav.nav-tabs.nav-menu li a span:after {
      content: "";
      position: absolute;
      width: 0;
      height: 5px;
      background-color: #1989BF;
      bottom: -8px;
      margin-left: -31px;
      left: 50%;
    }
    .navigation .nav.nav-tabs.nav-menu li a:hover, .navigation .nav.nav-tabs.nav-menu li a:focus {
      background-color: transparent;
      color: #989898;
    }
    .navigation .nav.nav-tabs.nav-menu li a:focus {
      color: #333333;
    }
    .navigation .nav.nav-tabs.nav-menu li.dropdown .dropdown-menu {
      margin-top: 0;
      top: 44px;
      -webkit-box-shadow: none;
      box-shadow: none;
      border: 0;
      width: 480px;
      background-color: #025B87 !important;
      border-radius: 0 0 14px 14px;
      overflow: hidden;
      padding: 15px 0;
    }
    .navigation .nav.nav-tabs.nav-menu li.dropdown .dropdown-menu li {
      background-color: #025B87 !important;
      padding: 0;
      border-bottom: 0 !important;
      width: 49%;
      display: inline-block;
      margin-bottom: 0;
    }
    .navigation .nav.nav-tabs.nav-menu li.dropdown .dropdown-menu li a {
      padding: 7px 23px;
      font-size: 1.6rem;
      font-weight: 400;
      line-height: 22px;
    }
    .navigation .nav.nav-tabs.nav-menu li.dropdown .dropdown-menu li a:hover {
      color: rgba(255, 255, 255, 0.5);
    }
    .navigation .nav.nav-tabs.nav-menu li.dropdown .dropdown-menu li.divider {
      width: calc(100% - 50px);
      display: block;
      background-color: #6A95AB !important;
      margin: 10px auto;
    }
    .navigation .nav.nav-tabs.nav-menu li.dropdown .dropdown-menu li:first-child a {
      padding-left: 25px;
    }
    .navigation .nav.nav-tabs.nav-menu li.dropdown .dropdown-menu li:before {
      content: none;
    }
    .navigation .nav.nav-tabs.nav-menu li.dropdown a.dropdown-toggle {
      border-radius: 14px 14px 0 0;
      padding-right: 50px;
    }
    .navigation .nav.nav-tabs.nav-menu li.dropdown a.dropdown-toggle:after {
      content: "";
      font-family: FontAwesome, sans-serif;      font-weight: 400;
      font-size: 3rem;
      color: #333333;
      line-height: 22px;
      display: inline-block;
      margin-left: 12px;
      position: absolute;
    }
    .navigation .nav.nav-tabs.nav-menu li.dropdown a.dropdown-toggle:hover:after {
      color: #989898;
    }
    .navigation .nav.nav-tabs.nav-menu li.dropdown a.dropdown-toggle:focus:after {
      color: #333333;
    }
    .navigation .nav.nav-tabs.nav-menu li.dropdown.open a {
      background-color: #025B87;
      color: white;
    }
    .navigation .nav.nav-tabs.nav-menu li.dropdown.open a.dropdown-toggle:after {
      content: "";
      font-weight: 400;
      color: white;
    }
    .navigation .nav.nav-tabs.nav-menu li:first-child a {
      padding-left: 0;
    }
    .navigation .nav.nav-tabs.nav-menu li:last-child a {
      padding-right: 0;
    }
    .navigation .nav.nav-tabs.nav-menu li.active a {
      font-weight: 700;
    }
    .navigation .nav.nav-tabs.nav-menu li.active a span:after {
      width: 62px;
    }
    .navigation .nav.nav-tabs.nav-menu li.active a:hover {
      color: #333333;
    }
    .navigation .nav.nav-tabs.nav-menu > li:after {
      content: "";
      position: absolute;
      width: 100%;
      height: 1px;
      background-color: #BFBFBF;
      bottom: -5px;
      left: 0;
    }
    .navigation .nav.nav-tabs.nav-menu li a span:after {
      left: 50%;
      margin-left: -50%;
      bottom: -15px;
    }
    .navigation .nav.nav-tabs.nav-menu li.active a span:after {
      width: 100%;
    }
    .navigation .nav.nav-tabs.nav-menu li.dropdown:hover a.dropdown-toggle {
      background-color: #025B87;
      color: white;
    }
    .navigation .nav.nav-tabs.nav-menu li.dropdown:hover a.dropdown-toggle:after {
      color: #fff;
      content: "";
    }
    .navigation .nav.nav-tabs.nav-menu li.dropdown:hover a.dropdown-toggle + .dropdown-menu {
      display: block;
    }
    .navigation .nav.nav-tabs.nav-menu a.dropdown-toggle + .dropdown-menu li a {
      color: #fff;
    }
    @media only screen and (max-width : 1200px) {
      .navigation {
        position: fixed;
        top: 65px;
        left: 0;
        width: 100vw;
        z-index: 99;
        background: #025B87;
        height: calc(100vh - 62px);
        padding-bottom: 85px;
        padding-top: 15px;
        -webkit-transform: translateX(100vw);
        -moz-transform: translateX(100vw);
        -ms-transform: translateX(100vw);
        -o-transform: translateX(100vw);
        transform: translateX(100vw);
        -webkit-transition: all 0.3s ease;
        -moz-transition: all 0.3s ease;
        -ms-transition: all 0.3s ease;
        -o-transition: all 0.3s ease;
        transition: all 0.3s ease;
      }
      .navigation .nav.nav-tabs.nav-menu {
        background: #025B87;
      }
      .navigation .nav.nav-tabs.nav-menu > li {
        float: none;
        padding: 0;
        margin-bottom: 0;
      }
      .navigation .nav.nav-tabs.nav-menu > li > a {
        color: white;
        font-size: 1.6rem;
        padding: 2vh 15px;
      }
      .navigation .nav.nav-tabs.nav-menu > li > a > span {
        display: inline-block;
      }
      .navigation .nav.nav-tabs.nav-menu > li > a > span:after {
        height: 4px;
      }
      .navigation .nav.nav-tabs.nav-menu > li > a:hover, .navigation .nav.nav-tabs.nav-menu > li > a:focus, .navigation .nav.nav-tabs.nav-menu > li > a:active {
        color: #989898;
      }
      .navigation .nav.nav-tabs.nav-menu > li:first-child a {
        padding-left: 15px;
      }
      .navigation .nav.nav-tabs.nav-menu > li.active > a > span:after {
        background-color: white;
      }
      .navigation .nav.nav-tabs.nav-menu > li.active > a:hover, .navigation .nav.nav-tabs.nav-menu > li.active > a:focus, .navigation .nav.nav-tabs.nav-menu > li.active > a:active {
        color: #989898;
      }
      .navigation .nav.nav-tabs.nav-menu > li.active > a:hover > span:after, .navigation .nav.nav-tabs.nav-menu > li.active > a:focus > span:after, .navigation .nav.nav-tabs.nav-menu > li.active > a:active > span:after {
        background-color: #989898;
      }
      .navigation .nav.nav-tabs.nav-menu > li.dropdown a.dropdown-toggle:after {
        color: white;
        font-size: 2.6rem;
      }
      .navigation .nav.nav-tabs.nav-menu > li.dropdown .dropdown-menu {
        width: 100%;
        position: relative;
        top: 0;
        padding: 0 0 2.5vh;
      }
      .navigation .nav.nav-tabs.nav-menu > li.dropdown .dropdown-menu li {
        width: 100%;
        display: block;
      }
      .navigation .nav.nav-tabs.nav-menu > li.dropdown .dropdown-menu li a {
        font-size: 1.4rem;
        padding: 1.4vh 45px;
        color: white;
      }
      .navigation .nav.nav-tabs.nav-menu > li.dropdown .dropdown-menu li a:hover {
        color: white !important;
        background: transparent !important;
      }
      .navigation .nav.nav-tabs.nav-menu > li.dropdown .dropdown-menu li:first-child a {
        padding-left: 45px;
      }
      .navigation .nav.nav-tabs.nav-menu > li.dropdown .dropdown-menu li.divider {
        width: calc(100% - 90px);
        display: block;
        margin-left: 45px;
      }
      .navigation .nav.nav-tabs.nav-menu > li.dropdown.open a.dropdown-toggle {
        color: #989898;
      }
      .navigation .nav.nav-tabs.nav-menu > li.dropdown.open a.dropdown-toggle:after {
        color: #989898;
      }
      .navigation .nav.nav-tabs.nav-menu > li:after {
        content: none;
      }
      .navigation.open {
        -webkit-transform: translateX(0);
        -moz-transform: translateX(0);
        -ms-transform: translateX(0);
        -o-transform: translateX(0);
        transform: translateX(0);
      }
    }
    @media only screen and (max-width: 370px) and (min-width: 320px) {
      .navigation {
        top: 59px;
        height: calc(100vh - 59px);
      }
      .navigation .nav.nav-tabs.nav-menu li a {
        font-size: 1.5rem;
        padding: 2.2vh 15px;
      }
      .navigation .nav.nav-tabs.nav-menu li a span:after {
        height: 3px;
      }
      .navigation .nav.nav-tabs.nav-menu li.dropdown a.dropdown-toggle:after {
        font-size: 2rem;
      }
      .navigation .nav.nav-tabs.nav-menu li.dropdown .dropdown-menu li a {
        padding: 1vh 45px;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .navigation {
        width: 50vw;
        top: 92px;
        height: calc(100vh - 65px);
      }
      .navigation.open {
        -webkit-transform: translateX(50vw);
        -moz-transform: translateX(50vw);
        -ms-transform: translateX(50vw);
        -o-transform: translateX(50vw);
        transform: translateX(50vw);
      }
    }
    @media only screen and (min-width : 993px) and (max-width : 1200px) {
      .navigation {
        width: 50vw;
        top: 92px;
        height: calc(100vh - 65px);
      }
      .navigation.open {
        -webkit-transform: translateX(50vw);
        -moz-transform: translateX(50vw);
        -ms-transform: translateX(50vw);
        -o-transform: translateX(50vw);
        transform: translateX(50vw);
      }
    }

    .profile-dropdown .profile-btn {
      /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#efefef+0,ffffff+100 */
      background: #efefef;
      /* Old browsers */
      background: -moz-linear-gradient(left, #efefef 0%, white 100%);
      /* FF3.6-15 */
      background: -webkit-linear-gradient(left, #efefef 0%, white 100%);
      /* Chrome10-25,Safari5.1-6 */
      background: linear-gradient(to right, #efefef 0%, white 100%);
      /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
      filter: progid:DXImageTransform.Microsoft.gradient( startColorstr="#efefef", endColorstr="#ffffff",GradientType=1 );
      /* IE6-9 */
      color: #333333;
      border: 1px solid #6c6c6c;
      font-size: 1.6rem;
      border-radius: 14px;
      padding: 13px 35px 13px 17px;
      width: 100%;
      text-align: left;
      position: relative;
      -webkit-transition: all 0.1s ease;
      -moz-transition: all 0.1s ease;
      -ms-transition: all 0.1s ease;
      -o-transition: all 0.1s ease;
      transition: all 0.1s ease;
    }
    .profile-dropdown .profile-btn:before {
      content: "";
      position: relative;
      width: 21px;
      height: 24px;
      background-image: url("../img/login-icon.png");
      display: inline-block;
      vertical-align: bottom;
      margin-right: 10px;
      background-size: contain;
      background-repeat: no-repeat;
    }
    .profile-dropdown .profile-btn:after {
      content: "";
      font-family: FontAwesome, sans-serif;      font-weight: 400;
      font-size: 3rem;
      color: #333333;
      line-height: 22px;
      display: inline-block;
      margin-left: 0;
      position: absolute;
      right: 15px;
    }
    .profile-dropdown .profile-btn:hover {
      border-color: #1989BF;
    }
    .profile-dropdown .profile-btn:active {
      -webkit-box-shadow: none;
      -moz-box-shadow: none;
      box-shadow: none;
    }
    .profile-dropdown .dropdown-menu {
      top: auto;
      border-radius: 0 0 14px 14px;
      width: 100%;
      margin: 0;
      background-color: white;
      -webkit-box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
      -moz-box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
      box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
      border: 1px solid white;
      border-top: 0;
      padding: 0;
    }
    .profile-dropdown .dropdown-menu > li {
      padding: 0;
      margin-bottom: 0;
      background-color: white !important;
      border-bottom: none !important;
    }
    .profile-dropdown .dropdown-menu > li:before {
      content: none;
    }
    .profile-dropdown .dropdown-menu > li > a {
      background-color: white !important;
      color: #333333;
      font-size: 1.6rem;
      line-height: 50px;
      min-height: 50px;
    }
    .profile-dropdown .dropdown-menu > li > a:hover {
      background-color: #C2E7FE !important;
    }
    .profile-dropdown .dropdown-menu > li > a:before {
      content: "";
      position: relative;
      width: 21px;
      height: 24px;
      display: inline-block;
      vertical-align: middle;
      margin-right: 10px;
      background-size: contain;
      background-repeat: no-repeat;
    }
    .profile-dropdown .dropdown-menu > li.management-account > a:before {
      background-image: url("../img/manage-icon.png");
    }
    .profile-dropdown .dropdown-menu > li.dashboard-icon > a:before {
      background-image: url("../img/dashboard-icon.png");
      background-position: 50% 50%;
    }
    .profile-dropdown .dropdown-menu > li.logout > a:before {
      background-image: url("../img/logout-icon.png");
    }
    .profile-dropdown.open .profile-btn {
      background: white;
      border-color: #1989BF;
      border-radius: 14px 14px 0 0;
      border-bottom: 0;
    }
    .profile-dropdown.open .dropdown-menu {
      border: 1px solid #1989BF;
      border-top: 0;
      overflow: hidden;
    }

    .gradient-light-grey {
      /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#efefef+0,ffffff+100 */
      background: #efefef;
      /* Old browsers */
      background: -moz-linear-gradient(left, #efefef 0%, white 100%);
      /* FF3.6-15 */
      background: -webkit-linear-gradient(left, #efefef 0%, white 100%);
      /* Chrome10-25,Safari5.1-6 */
      background: linear-gradient(to right, #efefef 0%, white 100%);
    }

    .gradient-light-blue {
      /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#efefef+0,ffffff+100 */
      background: #DFF1FC;
      /* Old browsers */
      background: -moz-linear-gradient(left, #DFF1FC 0%, #ECF7FC 100%);
      /* FF3.6-15 */
      background: -webkit-linear-gradient(left, #DFF1FC 0%, #ECF7FC 100%);
      /* Chrome10-25,Safari5.1-6 */
      background: linear-gradient(to right, #DFF1FC 0%, #ECF7FC 100%);
    }

    .gradient-orange {
      /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#efefef+0,ffffff+100 */
      background: #F38616;
      /* Old browsers */
      background: -moz-linear-gradient(left, #F38616 0%, #FBA044 100%);
      /* FF3.6-15 */
      background: -webkit-linear-gradient(left, #F38616 0%, #FBA044 100%);
      /* Chrome10-25,Safari5.1-6 */
      background: linear-gradient(to right, #F38616 0%, #FBA044 100%);
    }

    body {
      max-width: 100vw;
      overflow: hidden;
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      body {
        min-height: calc(100vh - 101px);
        padding-bottom: 101px;
        overflow: auto;
      }
    }

    body.navigation-open {
      max-height: 100vh;
      overflow: hidden;
      position: fixed;
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      body.navigation-open {
        overflow: hidden;
      }
      body.navigation-open:before {
        content: "";
        position: absolute;
        background-color: rgba(0, 0, 0, 0.6);
        left: 0;
        right: 0;
        top: 92px;
        bottom: 0;
        z-index: 99;
        -webkit-transition: all 0.35s ease;
        -moz-transition: all 0.35s ease;
        -ms-transition: all 0.35s ease;
        -o-transition: all 0.35s ease;
        transition: all 0.35s ease;
      }
    }
    @media only screen and (min-width : 993px) and (max-width : 1200px) {
      body.navigation-open:before {
        content: "";
        position: absolute;
        background-color: rgba(0, 0, 0, 0.6);
        left: 0;
        right: 0;
        top: 92px;
        bottom: 0;
        z-index: 99;
        -webkit-transition: all 0.35s ease;
        -moz-transition: all 0.35s ease;
        -ms-transition: all 0.35s ease;
        -o-transition: all 0.35s ease;
        transition: all 0.35s ease;
      }
    }

    @media only screen and (min-width : 768px) and (max-width : 992px) {
      body.nobg {
        min-height: 0px;
        padding-bottom: 0;
      }
    }

    .main-content + .main-content {
      border-top: 1px solid #bababa;
    }

    .gray-content-box {
      background-color: #fafafa;
      border-radius: 14px;
      padding: 30px;
      margin-bottom: 20px;
    }
    @media only screen and (max-width : 767px) {
      .gray-content-box {
        padding: 20px;
      }
    }

    .white-content-box {
      background-color: white;
      padding: 25px 40px;
      border-radius: 14px;
      margin-bottom: 20px;
    }

    .center-content {
      padding: 50px 90px;
    }
    @media only screen and (max-width : 767px) {
      .center-content {
        padding: 30px 0;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .center-content {
        padding: 35px 0;
      }
    }
    @media only screen and (min-width : 993px) and (max-width : 1200px) {
      .center-content {
        padding: 35px 0;
      }
    }

    .licence-btns .btn {
      margin-right: 20px;
      margin-bottom: 35px;
    }
    @media only screen and (max-width : 767px) {
      .licence-btns .btn {
        margin-bottom: 20px;
      }
      .licence-btns .btn:not(:last-child) {
        margin-right: 10px;
      }
      .licence-btns .btn:last-child {
        margin-right: 0;
      }
      .licence-btns .btn.btn-primary,
      .licence-btns .btn.btn-secondary {
        padding: 10px 25px;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .licence-btns .btn.btn-primary,
      .licence-btns .btn.btn-secondary {
        padding: 10px 25px;
      }
      .licence-btns .btn:not(:last-child) {
        margin-right: 15px;
      }
      .licence-btns .btn:last-child {
        margin-right: 0;
      }
    }
    @media only screen and (min-width : 993px) and (max-width : 1200px) {
      .licence-btns .btn.btn-primary,
      .licence-btns .btn.btn-secondary {
        padding: 10px 35px;
      }
      .licence-btns .btn:not(:last-child) {
        margin-right: 15px;
      }
      .licence-btns .btn:last-child {
        margin-right: 0;
      }
    }

    .button-group a:not(:last-child) {
      margin-right: 20px;
    }

    .btn {
      font-family: "Open Sans", sans-serif;
      text-decoration: none;
    }
    .btn.btn-primary {
      font-size: 1.6rem;
      font-weight: 700;
      background: #F2B227;
      border: 1px solid #F2B227;
      color: black;
      padding: 12px 40px;
      text-transform: uppercase;
      border-radius: 30px;
    }
    .btn.btn-primary:active, .btn.btn-primary:focus, .btn.btn-primary.click {
      background: #E19A00;
      color: black;
      border: 1px solid #E19A00;
      -webkit-box-shadow: none;
      box-shadow: none;
    }
    .btn.btn-primary:hover, .btn.btn-primary.hover {
      -webkit-box-shadow: 0px 3px 18px 0px rgba(0, 0, 0, 0.35);
      -moz-box-shadow: 0px 3px 18px 0px rgba(0, 0, 0, 0.35);
      box-shadow: 0px 3px 18px 0px rgba(0, 0, 0, 0.35);
      background: #F2B227;
      border: 1px solid #F2B227;
    }
    .btn.btn-primary.disabled {
      opacity: 0.6;
      pointer-events: none;
    }
    @media only screen and (max-width : 767px) {
      .btn.btn-primary {
        font-size: 1.4rem;
      }
    }
    .btn.btn-secondary {
      font-size: 1.6rem;
      font-weight: 700;
      background: white;
      border: 1px solid #333333;
      color: black;
      padding: 12px 40px;
      text-transform: uppercase;
      border-radius: 30px;
    }
    .btn.btn-secondary:active, .btn.btn-secondary:focus, .btn.btn-secondary.click {
      background: #E8E8E8;
      color: black;
      border: 1px solid #333333;
      -webkit-box-shadow: none;
      box-shadow: none;
    }
    .btn.btn-secondary:hover, .btn.btn-secondary.hover {
      -webkit-box-shadow: 0px 2px 18px 0px rgba(0, 0, 0, 0.18);
      -moz-box-shadow: 0px 2px 18px 0px rgba(0, 0, 0, 0.18);
      box-shadow: 0px 2px 18px 0px rgba(0, 0, 0, 0.18);
      background: white;
      border: 1px solid #333333;
    }
    .btn.btn-secondary.disabled {
      opacity: 0.35;
      pointer-events: none;
    }
    .btn.btn-md {
      font-size: .986rem;
      font-weight: 600;
      padding: 10px 25px;
      text-transform: uppercase;
      border-radius: 30px;
    }
    .btn.btn-sm {
      font-size: .775rem;
      font-weight: 500;
      padding: 5px 10px;
      text-transform: uppercase;
      border-radius: 30px;
    }
    @media only screen and (max-width : 767px) {
      .btn.btn-secondary {
        font-size: 1.4rem;
      }
      .btn.btn-secondary.btn-file-upload {
        padding: 6px 15px;
        border-radius: 29px;
      }
    }
    @media only screen and (max-width : 767px) {
      .btn.btn-primary, .btn.btn-secondary {
        padding-left: 25px;
        padding-right: 25px;
      }
    }

    .information-block {
      padding: 20px 15px 10px;
      border-radius: 14px;
      border: 2px dotted #6c6c6c;
      position: relative;
    }
    .information-block .icon-info {
      width: 30px;
      height: 30px;
      background-color: #A8A8A8;
      border-radius: 50%;
      position: absolute;
      top: 17px;
    }
    .information-block .icon-info:before {
      content: "i";
      font-size: 2rem;
      font-weight: 700;
      font-style: italic;
      color: #fff;
      text-align: center;
      width: 30px;
      display: block;
      line-height: 30px;
      left: -2px;
      position: absolute;
    }
    .information-block .info-content {
      margin-left: 45px;
    }
    .information-block .info-content ul li {
      margin-bottom: 10px;
      font-size: 1.6rem;
    }
    @media only screen and (max-width : 767px) {
      .information-block {
        border: 1px dotted #6c6c6c;
        padding: 15px 10px;
      }
      .information-block .icon-info {
        width: 25px;
        height: 25px;
        top: 15px;
      }
      .information-block .icon-info:before {
        font-size: 1.6rem;
        width: 25px;
        line-height: 25px;
        left: -1px;
      }
      .information-block .info-content {
        margin-left: 40px;
      }
      .information-block .info-content ul li {
        font-size: 1.4rem;
        margin-bottom: 5px;
      }
    }

    @media only screen and (max-width : 767px) {
      .text-center-mobile {
        text-align: center !important;
      }
    }

    hr {
      border-top: 1px solid #bababa;
      margin-bottom: 40px;
    }
    @media only screen and (max-width : 767px) {
      hr {
        margin-bottom: 25px;
      }
    }

    footer.footerlogin {
      background: #333333;
      font-size: 1.2rem;
      border-top: 0;
      position: relative;
      -webkit-box-shadow: none;
      box-shadow: none;
      padding: 30px 0 25px;
    }
    footer.footerlogin .copyright p {
      color: #cccccc;
      font-size: 1.2rem;
      margin-bottom: 0;
    }
    footer.footerlogin .footer-link ul {
      margin-bottom: 0;
      margin-left: 0;
    }
    footer.footerlogin .footer-link ul li {
      padding-left: 10px;
      padding-right: 10px;
      margin-bottom: 0;
    }
    footer.footerlogin .footer-link ul li a {
      color: #cccccc;
      font-size: 12px;
      position: relative;
    }
    footer.footerlogin .footer-link ul li a:hover, footer.footerlogin .footer-link ul li a:focus, footer.footerlogin .footer-link ul li a:active {
      color: #ff6600;;
    }
    footer.footerlogin .footer-link ul li:first-child {
      padding-left: 0;
    }
    footer.footerlogin .footer-link ul li:not(:first-child) a:before {
      content: "";
      width: 1px;
      height: 9px;
      background-color: #DFDFDF;
      position: absolute;
      top: 50%;
      left: -11px;
      margin-top: -4px;
    }
    @media only screen and (max-width : 767px) {
      footer.footerlogin {
        padding: 17px 0 15px;
      }
      footer.footerlogin .copyright p {
        font-size: 1.1rem;
        text-align: center !important;
      }
      footer.footerlogin .footer-link {
        width: 200px;
        margin-left: auto;
        margin-right: auto;
        margin-bottom: 5px;
      }
      footer.footerlogin .footer-link ul {
        text-align: center;
      }
      footer.footerlogin .footer-link ul li {
        margin-bottom: 4px;
        padding-left: 7px;
        padding-right: 7px;
      }
      footer.footerlogin .footer-link ul li a {
        position: relative;
        font-size: 11px;
      }
      footer.footerlogin .footer-link ul li a:before {
        content: none;
      }
      footer.footerlogin .footer-link ul li:before {
        content: none;
      }
      footer.footerlogin .footer-link ul li:not(:first-child) a:before {
        content: none;
      }
      footer.footerlogin .footer-link ul li:nth-child(2n) a:before {
        content: "";
        width: 1px;
        height: 9px;
        background-color: #DFDFDF;
        position: absolute;
        top: 50%;
        left: -6px;
        margin-top: -4px;
      }
      footer.footerlogin .footer-link ul li:first-child {
        padding-left: 7px;
      }
    }


    @media only screen and (min-width: 768px) and (max-width: 1200px) {
      .navigation-open .footerlogin {
        position: relative;
      }
    }
    .panel-group .panel.panel-default {
      -webkit-box-shadow: 0px 5px 24px 0px rgba(0, 0, 0, 0.2);
      -moz-box-shadow: 0px 5px 24px 0px rgba(0, 0, 0, 0.2);
      box-shadow: 0px 5px 24px 0px rgba(0, 0, 0, 0.2);
      margin-bottom: 30px;
      border: 0;
      border-radius: 14px;
      /*overflow: hidden;*/
    }
    .panel-group .panel.panel-default > .panel-heading {
      background-color: #F6F6F6;
      border-bottom: 0;
      padding-top: 20px;
      padding-bottom: 20px;
      padding-left: 45px;
      padding-right: 25px;
      border-top-left-radius: 14px;
      border-top-right-radius: 14px;
    }
    .panel-group .panel.panel-default > .panel-heading h4 {
      position: relative;
      font-size: 2rem;
    }
    .panel-group .panel.panel-default > .panel-heading h4 a {
      font-size: 2rem;
      color: #333333;
      font-weight: 600;
      padding-left: 45px;
      position: relative;
      display: block;
    }
    .panel-group .panel.panel-default > .panel-heading h4 a:before {
      font-family: FontAwesome, sans-serif;      content: "";
      font-size: 4rem;
      color: #147aab;
      line-height: 20px;
      font-weight: 400;
      vertical-align: middle;
      position: absolute;
      left: 0;
      top: 0;
      line-height: 22px;
      -webkit-transition: all 0.3s ease;
      -moz-transition: all 0.3s ease;
      -ms-transition: all 0.3s ease;
      -o-transition: all 0.3s ease;
      transition: all 0.3s ease;
    }
    .panel-group .panel.panel-default > .panel-heading h4 a.collapsed:before {
      content: "";
    }
    .panel-group .panel.panel-default > .panel-heading h4 a:focus {
      outline: 0;
    }
    .panel-group .panel.panel-default > .panel-heading + .panel-collapse > .panel-body {
      border-top: 0;
      border-bottom-left-radius: 14px;
      border-bottom-right-radius: 14px;
      padding: 20px 25px 30px 95px;
    }
    .panel-group .panel.panel-default > .panel-heading + .panel-collapse > .panel-body .panel-main-content {
      margin-right: 45px;
    }
    .panel-group .panel.panel-default > .panel-heading + .panel-collapse > .panel-body .fa.fa-pencil-square-o {
      font-size: 1.6rem;
      margin-right: 5px;
    }
    .panel-group .panel.panel-default > .panel-heading.completed h4 a:after {
      content: "";
      font-family: FontAwesome, sans-serif;      width: 22px;
      height: 22px;
      border: 0;
      background-color: #168926;
      color: #fff;
      font-size: 14px;
      display: inline-block;
      position: relative;
      text-align: center;
      border-radius: 50%;
      line-height: 22px;
      font-weight: 400;
      vertical-align: bottom;
      margin-left: 10px;
    }
    .panel-group .panel.panel-default > .panel-heading.incompleted h4 a:after {
      content: "";
      font-family: FontAwesome, sans-serif;      width: 22px;
      height: 22px;
      border: 0;
      background-color: #c4322b;
      color: #fff;
      font-size: 14px;
      display: inline-block;
      position: relative;
      text-align: center;
      border-radius: 50%;
      line-height: 22px;
      font-weight: 400;
      vertical-align: bottom;
      margin-left: 10px;
    }
    @media only screen and (max-width : 767px) {
      .panel-group .panel.panel-default > .panel-heading.completed h4 a:after, .panel-group .panel.panel-default > .panel-heading.incompleted h4 a:after {
        width: 20px;
        height: 20px;
        font-size: 11px;
        line-height: 22px;
      }
    }
    @media only screen and (max-width : 767px) {
      .panel-group .panel.panel-default {
        -webkit-box-shadow: 0px 5px 20px 0px rgba(0, 0, 0, 0.2);
        -moz-box-shadow: 0px 5px 20px 0px rgba(0, 0, 0, 0.2);
        box-shadow: 0px 5px 20px 0px rgba(0, 0, 0, 0.2);
        margin-bottom: 15px;
      }
      .panel-group .panel.panel-default > .panel-heading {
        padding: 17px 20px;
      }
      .panel-group .panel.panel-default > .panel-heading h4 {
        font-size: 1.6rem;
      }
      .panel-group .panel.panel-default > .panel-heading h4 a {
        font-size: 1.6rem;
        padding-left: 40px;
      }
      .panel-group .panel.panel-default > .panel-heading h4 a:before {
        font-size: 3.6rem;
        line-height: 17px;
      }
      .panel-group .panel.panel-default > .panel-heading + .panel-collapse > .panel-body {
        padding: 10px 10px 15px 35px;
      }
      .panel-group .panel.panel-default > .panel-heading + .panel-collapse > .panel-body .fa.fa-pencil-square-o,
      .panel-group .panel.panel-default > .panel-heading + .panel-collapse > .panel-body p.text-right:first-child {
        font-size: 1.4rem;
      }
      .panel-group .panel.panel-default > .panel-heading + .panel-collapse > .panel-body .fa.fa-pencil-square-o {
        margin-right: 10px;
      }
    }

    input[type=text],
    input[type=email],
    input[type=number] {
      border: 1px solid #6c6c6c;
      margin-bottom: 15px;
      z-index: 1;
      border-radius: 14px;
      color: #333333;
      font-family: "Open Sans", sans-serif;
      /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#efefef+0,ffffff+100 */
      background: #efefef;
      /* Old browsers */
      background: -moz-linear-gradient(left, #efefef 0%, white 100%) !important;
      /* FF3.6-15 */
      background: -webkit-linear-gradient(left, #efefef 0%, white 100%) !important;
      /* Chrome10-25,Safari5.1-6 */
      background: linear-gradient(to right, #efefef 0%, white 100%) !important;
      font-size: 1.6rem;
      line-height: 22px;
      padding: 13px 15px;
      height: auto;
      width: 100%;
    }
    input[type=text]::placeholder,
    input[type=email]::placeholder,
    input[type=number]::placeholder {
      color: #333333;
    }
    input[type=text]:active, input[type=text]:focus,
    input[type=email]:active,
    input[type=email]:focus,
    input[type=number]:active,
    input[type=number]:focus {
      outline: 0;
    }
    input[type=text]:hover, input[type=text].hover,
    input[type=email]:hover,
    input[type=email].hover,
    input[type=number]:hover,
    input[type=number].hover {
      border: 1px solid #1989BF;
    }
    input[type=text]:active, input[type=text].active, input[type=text]:focus,
    input[type=email]:active,
    input[type=email].active,
    input[type=email]:focus,
    input[type=number]:active,
    input[type=number].active,
    input[type=number]:focus {
      border: 1px solid #1989BF;
      background: white;
      -webkit-box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
      -moz-box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
      box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
    }
    input[type=text].error,
    input[type=email].error,
    input[type=number].error {
      border: 1px solid #D22727;
      background: white;
      -webkit-box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
      -moz-box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
      box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
    }
    input[type=text].disabled,
    input[type=email].disabled,
    input[type=number].disabled {
      border: 1px solid #6c6c6c;
    }
    @media only screen and (max-width : 767px) {
      input[type=text],
      input[type=email],
      input[type=number] {
        font-size: 1.4rem;
        padding: 10px 15px;
        margin-bottom: 10px;
      }
    }
    input[type=text].input-with-tooltip,
    input[type=email].input-with-tooltip,
    input[type=number].input-with-tooltip {
      width: calc(100% - 40px);
      margin-right: 10px;
    }

    textarea.form-control {
      border: 1px solid #6c6c6c;
      margin-bottom: 15px;
      z-index: 1;
      border-radius: 14px;
      color: #333333;
      font-family: "Open Sans", sans-serif;
      /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#efefef+0,ffffff+100 */
      background: #efefef;
      /* Old browsers */
      background: -moz-linear-gradient(left, #efefef 0%, white 100%);
      /* FF3.6-15 */
      background: -webkit-linear-gradient(left, #efefef 0%, white 100%);
      /* Chrome10-25,Safari5.1-6 */
      background: linear-gradient(to right, #efefef 0%, white 100%);
      font-size: 1.6rem;
      padding: 13px 15px;
    }
    textarea.form-control:hover, textarea.form-control.hover {
      border: 1px solid #1989BF;
    }
    textarea.form-control:active, textarea.form-control.active, textarea.form-control:focus {
      border: 1px solid #1989BF;
      background: white;
      -webkit-box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
      -moz-box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
      box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
    }

    input[type=number] {
      -moz-appearance: textfield;
    }

    input[type=number]::-webkit-inner-spin-button,
    input[type=number]::-webkit-outer-spin-button {
      -webkit-appearance: none;
      margin: 0;
    }

    .form-horizontal .form-group,
    .form-inline .form-group {
      margin-bottom: 25px;
    }
    @media only screen and (max-width : 767px) {
      .form-horizontal .form-group,
      .form-inline .form-group {
        margin-bottom: 10px;
      }
    }
    .form-horizontal label.control-label,
    .form-inline label.control-label {
      color: #333333;
      font-size: 1.6rem;
      text-align: left !important;
      line-height: 50px;
      padding-top: 0 !important;
    }
    @media only screen and (max-width : 767px) {
      .form-horizontal label.control-label,
      .form-inline label.control-label {
        font-size: 1.4rem;
        line-height: 26px;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .form-horizontal label.control-label,
      .form-inline label.control-label {
        font-size: 1.6rem;
        line-height: 35px;
      }
    }

    .form-horizontal .form-check {
      margin-top: 15px;
    }
    .form-horizontal p {
      line-height: 50px;
    }
    .form-horizontal .form-group .input-with-label input[type=text],
    .form-horizontal .form-group .input-with-label input[type=email],
    .form-horizontal .form-group .input-with-label input[type=number] {
      width: calc(100% - 80px);
      display: inline-block;
      margin-right: 7px;
    }
    .form-horizontal .form-group .input-with-label p {
      display: inline-block;
      width: auto;
    }
    .form-horizontal .nice-select {
      margin-bottom: 15px;
    }

    .input-with-label input[type=text],
    .input-with-label input[type=email],
    .input-with-label input[type=number] {
      width: calc(100% - 80px);
      display: inline-block;
      margin-right: 7px;
    }
    .input-with-label p {
      display: inline-block;
      width: auto;
    }

    .form-check-gp p.form-check-title {
      font-size: 2rem;
      font-weight: 600;
      margin-bottom: 12px;
    }
    @media only screen and (max-width : 767px) {
      .form-check-gp p.form-check-title {
        font-size: 1.6rem;
      }
    }

    .form-check {
      margin-bottom: 12px;
      position: relative;
    }
    .form-check input.form-check-input {
      height: auto;
      position: absolute;
      opacity: 0;
      width: 100%;
      height: 100%;
      left: 0;
      top: 0;
      z-index: 10;
      cursor: pointer;
      margin: 0;
    }
    .form-check .form-check-label {
      color: #333333;
      font-size: 1.6rem;
      font-weight: 400;
      position: relative;
      padding-left: 30px;
      min-width: 16px;
      min-height: 16px;
    }
    .form-check .form-check-label a {
      text-decoration: underline;
    }
    .form-check .form-check-label a:hover, .form-check .form-check-label a:focus, .form-check .form-check-label a:active {
      text-decoration: none;
      color: #333;
    }
    .form-check .form-check-label .check-circle {
      width: 20px;
      height: 20px;
      border: 1px solid #6c6c6c;
      display: inline-block;
      border-radius: 16px;
      margin-right: 10px;
      margin-top: 0;
      position: absolute;
      left: 0;
      top: 3px;
      background: #efefef;
      /* Old browsers */
      background: -moz-linear-gradient(left, #efefef 0%, white 100%);
      /* FF3.6-15 */
      background: -webkit-linear-gradient(left, #efefef 0%, white 100%);
      /* Chrome10-25,Safari5.1-6 */
      background: linear-gradient(to right, #efefef 0%, white 100%);
    }
    .form-check .form-check-label .check-circle:before {
      content: "";
      position: absolute;
      width: 10px;
      height: 10px;
      background-color: transparent;
      top: 50%;
      margin-top: -5px;
      left: 50%;
      margin-left: -5px;
      border-radius: 5px;
    }
    .form-check .form-check-label .check-square {
      width: 18px;
      height: 18px;
      border: 1px solid #6c6c6c;
      display: inline-block;
      border-radius: 4px;
      margin-right: 10px;
      margin-top: 0;
      position: absolute;
      left: 0;
      top: 3px;
      background: white;
    }
    .form-check .form-check-label .check-square:before {
      content: "";
      font-family: FontAwesome, sans-serif;      position: absolute;
      width: 16px;
      height: 16px;
      top: 0;
      left: 0;
      vertical-align: middle;
      text-align: center;
      line-height: 16px;
      font-size: 12px;
      color: #333;
      opacity: 0;
    }
    .form-check .form-check-label:hover a, .form-check .form-check-label:focus a, .form-check .form-check-label:active a {
      text-decoration: none;
      color: #333;
    }
    @media only screen and (max-width : 767px) {
      .form-check .form-check-label {
        font-size: 1.4rem;
        padding-left: 25px;
      }
      .form-check .form-check-label .check-square {
        top: 1px;
      }
    }
    .form-check input.form-check-input:checked + .form-check-label,
    .form-check input.form-check-input:active + .form-check-label {
      border: 0;
    }
    .form-check input.form-check-input:checked + .form-check-label span.check-circle,
    .form-check input.form-check-input:active + .form-check-label span.check-circle {
      border: 1px solid #1989BF;
    }
    .form-check input.form-check-input:checked + .form-check-label span.check-circle:before,
    .form-check input.form-check-input:active + .form-check-label span.check-circle:before {
      background-color: #147aab;
    }
    .form-check input.form-check-input:checked + .form-check-label span.check-square,
    .form-check input.form-check-input:active + .form-check-label span.check-square {
      border: 1px solid #147aab;
    }
    .form-check input.form-check-input:checked + .form-check-label span.check-square:before,
    .form-check input.form-check-input:active + .form-check-label span.check-square:before {
      color: #147aab;
      opacity: 1;
    }
    .form-check input.form-check-input:disabled {
      cursor: not-allowed;
    }
    .form-check input.form-check-input:hover + .form-check-label {
      border: 0;
    }
    .form-check input.form-check-input:hover + .form-check-label span.check-circle,
    .form-check input.form-check-input:hover + .form-check-label span.check-square {
      border: 1px solid #147aab;
    }
    .form-check input.form-check-input:hover + .form-check-label a {
      text-decoration: none;
      color: #333;
    }
    .form-check.hover .form-check-label span.check-circle,
    .form-check.hover .form-check-label span.check-square {
      border: 1px solid #147aab;
    }
    .form-check.finished span.check-circle, .form-check.active span.check-circle {
      border: 1px solid #147aab;
    }
    .form-check.finished span.check-circle:before, .form-check.active span.check-circle:before {
      background-color: #147aab;
    }
    .form-check.finished span.check-square, .form-check.active span.check-square {
      border: 1px solid #147aab;
    }
    .form-check.finished span.check-square:before, .form-check.active span.check-square:before {
      color: #1989BF;
      opacity: 1;
    }
    .form-check.disabled {
      opacity: 0.5;
    }
    .form-check.disabled .form-check-label,
    .form-check.disabled .form-check-input {
      pointer-events: none;
    }
    .form-check.progress-step-check {
      position: relative;
      width: 180px;
      border-radius: 14px;
    }
    .form-check.progress-step-check.large-form-width {
      width: 100%;
    }
    .form-check.progress-step-check .form-check-label {
      /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#efefef+0,ffffff+100 */
      border: 1px solid #6c6c6c;
      background: #efefef;
      /* Old browsers */
      background: -moz-linear-gradient(left, #efefef 0%, white 100%);
      /* FF3.6-15 */
      background: -webkit-linear-gradient(left, #efefef 0%, white 100%);
      /* Chrome10-25,Safari5.1-6 */
      background: linear-gradient(to right, #efefef 0%, white 100%);
      padding: 12px 15px 12px 45px;
      border-radius: 14px;
      position: relative;
      width: 100%;
      margin-bottom: 0;
    }
    .form-check.progress-step-check .form-check-label span.check-circle {
      margin-right: 0;
      top: 50%;
      margin-top: -8px;
      left: 15px;
      background-color: white;
    }
    .form-check.progress-step-check.selected .form-check-label {
      border: 1px solid #1989BF;
    }
    .form-check.progress-step-check.selected .form-check-label span.check-circle {
      border: 1px solid #1989BF;
    }
    .form-check.progress-step-check.selected .form-check-label span.check-circle:before {
      background-color: #1989BF;
    }
    .form-check.progress-step-check input.form-check-input:checked + .form-check-label {
      border: 1px solid #1989BF;
      background: white;
    }
    .form-check.progress-step-check input.form-check-input:checked + .form-check-label span.check-circle {
      border: 1px solid #1989BF;
    }
    .form-check.progress-step-check input.form-check-input:checked + .form-check-label span.check-circle:before {
      background-color: #1989BF;
    }
    .form-check.progress-step-check input.form-check-input:hover + .form-check-label {
      border: 1px solid #1989BF;
    }
    .form-check.progress-step-check input.form-check-input:hover + .form-check-label span.check-circle {
      border: 1px solid #1989BF;
    }
    @media only screen and (max-width : 767px) {
      .form-check.progress-step-check {
        border-radius: 12px;
      }
      .form-check.progress-step-check .form-check-label {
        padding: 12px 10px 12px 40px;
      }
    }
    .form-check.sub-form-check {
      margin-left: 30px;
      margin-bottom: 5px;
      margin-top: 5px;
    }
    @media only screen and (max-width : 767px) {
      .form-check {
        margin-bottom: 10px;
      }
    }

    .form-inline .form-group {
      width: 30%;
    }
    .form-inline .form-group label.control-label {
      width: auto;
      float: left;
    }
    .form-inline .form-group.large {
      width: 30%;
    }
    .form-inline .form-group.right-side {
      float: right;
    }
    @media only screen and (max-width : 767px) {
      .form-inline .form-group {
        width: 46%;
        display: inline-block;
        margin-right: 10px;
      }
      .form-inline .form-group.large {
        width: 100%;
        padding-left: 0px;
        padding-right: 15px;
        float: none;
      }
      .form-inline .form-group .col-xs-12 {
        padding-left: 0;
        padding-right: 0;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .form-inline .form-group {
        width: 27%;
      }
      .form-inline .form-group label.control-label {
        width: auto;
        float: left;
      }
      .form-inline .form-group .col-xs-12 {
        padding-left: 0;
        padding-right: 0;
      }
      .form-inline .form-group .search-wrap {
        margin-top: 35px;
      }
      .form-inline .form-group.large {
        width: 40%;
      }
      .form-inline .form-group:not(:last-child) {
        margin-right: 10px;
      }
    }

    .search-wrap .input-group {
      display: block;
      width: 100%;
    }
    .search-wrap .input-group input[type=text],
    .search-wrap .input-group input[type=email],
    .search-wrap .input-group input[type=number] {
      border: 1px solid #6c6c6c;
      z-index: 1;
      border-radius: 14px;
      color: #333333;
      /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#efefef+0,ffffff+100 */
      background: #efefef;
      /* Old browsers */
      background: -moz-linear-gradient(left, #efefef 0%, white 100%);
      /* FF3.6-15 */
      background: -webkit-linear-gradient(left, #efefef 0%, white 100%);
      /* Chrome10-25,Safari5.1-6 */
      background: linear-gradient(to right, #efefef 0%, white 100%);
      font-size: 1.6rem;
      line-height: 22px;
      padding: 14px 15px;
      height: auto;
    }
    .search-wrap .input-group input[type=text]::placeholder,
    .search-wrap .input-group input[type=email]::placeholder,
    .search-wrap .input-group input[type=number]::placeholder {
      color: #333333;
    }
    .search-wrap .input-group input[type=text]:active, .search-wrap .input-group input[type=text]:focus,
    .search-wrap .input-group input[type=email]:active,
    .search-wrap .input-group input[type=email]:focus,
    .search-wrap .input-group input[type=number]:active,
    .search-wrap .input-group input[type=number]:focus {
      outline: 0;
    }
    .search-wrap .input-group input[type=text]:hover,
    .search-wrap .input-group input[type=email]:hover,
    .search-wrap .input-group input[type=number]:hover {
      border: 1px solid #1989BF;
    }
    .search-wrap .input-group input[type=text]:active, .search-wrap .input-group input[type=text]:focus,
    .search-wrap .input-group input[type=email]:active,
    .search-wrap .input-group input[type=email]:focus,
    .search-wrap .input-group input[type=number]:active,
    .search-wrap .input-group input[type=number]:focus {
      border: 1px solid #1989BF;
      background: white;
      -webkit-box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
      -moz-box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
      box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
    }
    .search-wrap .input-group input[type=text].error,
    .search-wrap .input-group input[type=email].error,
    .search-wrap .input-group input[type=number].error {
      border: 1px solid #D22727;
      background: white;
      -webkit-box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
      -moz-box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
      box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
    }
    @media only screen and (max-width : 767px) {
      .search-wrap .input-group input[type=text],
      .search-wrap .input-group input[type=email],
      .search-wrap .input-group input[type=number] {
        font-size: 1.4rem;
        padding: 11px 13px;
      }
    }
    .search-wrap .input-group .input-group-btn {
      border: 0;
      padding: 0;
      margin: 0;
      position: absolute;
      width: 52px;
      height: 52px;
      right: -1px;
      top: 0;
      border-top-right-radius: 14px;
      border-bottom-right-radius: 14px;
    }
    .search-wrap .input-group .input-group-btn .btn.buttonsearch {
      padding: 0;
      border: 0;
      background: #147aab;
      width: 52px;
      height: 52px;
      text-align: center;
      vertical-align: middle;
      position: absolute;
      border-top-right-radius: 14px;
      border-bottom-right-radius: 14px;
    }
    .search-wrap .input-group .input-group-btn .btn.buttonsearch .fa.fa-search {
      font-size: 2rem;
      line-height: 22px;
      border: 0;
      padding: 0;
      margin: 0;
      color: white;
    }
    .search-wrap .input-group .input-group-btn .btn.buttonsearch:focus {
      outline: 0;
    }
    @media only screen and (max-width : 767px) {
      .search-wrap .input-group .input-group-btn {
        width: 46px;
        height: 46px;
      }
      .search-wrap .input-group .input-group-btn .btn.buttonsearch {
        width: 46px;
        height: 46px;
      }
    }

    .self-assessment-checkbox-gp {
      padding: 20px 15px;
      border: 1px solid #6c6c6c;
      border-radius: 14px;
    }
    .self-assessment-checkbox-gp p.assessment-title {
      font-weight: 600;
    }
    .self-assessment-checkbox-gp .form-check-gp:not(:last-child) {
      margin-bottom: 20px;
    }

    span.error-msg {
      font-size: 1.6rem;
      color: #D22727;
    }
    @media only screen and (max-width : 767px) {
      span.error-msg {
        font-size: 1.4rem;
      }
    }

    .nice-select {
      /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#efefef+0,ffffff+100 */
      background: #efefef;
      /* Old browsers */
      background: -moz-linear-gradient(left, #efefef 0%, white 100%);
      /* FF3.6-15 */
      background: -webkit-linear-gradient(left, #efefef 0%, white 100%);
      /* Chrome10-25,Safari5.1-6 */
      background: linear-gradient(to right, #efefef 0%, white 100%);
      border: 1px solid #6c6c6c;
      border-radius: 14px;
      font-size: 1.6rem;
      padding: 14px 15px;
      height: auto;
      line-height: 22px;
      width: 100%;
      -webkit-transition: all 0.1s ease;
      -moz-transition: all 0.1s ease;
      -ms-transition: all 0.1s ease;
      -o-transition: all 0.1s ease;
      transition: all 0.1s ease;
      white-space: normal;
      text-overflow: inherit;
      overflow: hidden;
    }
    .nice-select span.current {
      line-height: 22px;
      height: 22px;
      font-family: "Open Sans", sans-serif;
    }
    .nice-select ul.list {
      max-height: 300px;
      -webkit-box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
      -moz-box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
      box-shadow: 0px 10px 20px 0px rgba(0, 0, 0, 0.12);
      width: auto;
      left: -1px;
      right: -1px;
      border-radius: 0;
      border-bottom-left-radius: 14px;
      border-bottom-right-radius: 14px;
      border: 1px solid white;
      border-top: 0;
      background-color: white;
      margin-top: 0;
      -webkit-transform: none;
      -moz-transform: none;
      -ms-transform: none;
      -o-transform: none;
      transform: none;
    }
    .nice-select ul.list li {
      min-height: 40px;
      line-height: 25px;
      font-family: "Open Sans", sans-serif;
      margin-bottom: 0;
    }
    .nice-select ul.list li:before {
      content: none;
    }
    .nice-select ul.list li:hover, .nice-select ul.list li.selected, .nice-select ul.list li.focus, .nice-select ul.list li.selected.focus {
      background-color: #C2E7FE;
    }
    .nice-select ul.list li.selected {
      font-weight: 400;
    }
    .nice-select ul.list li:first-child {
      /**display: none;   */
    }
    .nice-select:hover, .nice-select.hover {
      border-color: #1989BF;
    }
    .nice-select:after {
      content: "";
      font-family: FontAwesome, sans-serif;      font-size: 30px;
      color: #6c6c6c;
      -webkit-transform-origin: 0 0;
      -ms-transform-origin: 0 0;
      transform-origin: 0 0;
      -webkit-transform: none;
      -moz-transform: none;
      -ms-transform: none;
      -o-transform: none;
      transform: none;
      background: none;
      border: 0;
      height: 22px;
      width: 20px;
      line-height: 22px;
      top: 50%;
      margin-top: -11px;
      right: 15px;
    }
    .nice-select.open {
      background: white;
      border-color: #1989BF;
      border-bottom-right-radius: 0;
      border-bottom-left-radius: 0;
      border-bottom: 0;
      overflow: inherit;
    }
    .nice-select.open ul.list {
      border-color: #1989BF;
      -webkit-transform: none;
      -moz-transform: none;
      -ms-transform: none;
      -o-transform: none;
      transform: none;
      overflow-y: auto;
    }
    .nice-select.open:after {
      -webkit-transform: none;
      -moz-transform: none;
      -ms-transform: none;
      -o-transform: none;
      transform: none;
    }
    @media only screen and (max-width : 767px) {
      .nice-select {
        padding: 10px 15px;
      }
      .nice-select span.current {
        font-size: 1.4rem;
      }
      .nice-select ul.list li {
        font-size: 1.4rem;
        min-height: 45px;
        line-height: 45px;
      }
    }
    .nice-select.table-select {
      padding: 4px 10px;
      border-radius: 7px;
      min-width: 110px;
    }
    .nice-select.table-select ul.list {
      border-bottom-left-radius: 7px;
      border-bottom-right-radius: 7px;
    }
    .nice-select.table-select ul.list li {
      padding-left: 10px;
      padding-right: 20px;
      line-height: 32px;
      min-height: 32px;
    }
    .nice-select.table-select:after {
      font-size: 2.4rem;
      line-height: 18px;
      width: 14px;
      height: 18px;
      right: 12px;
      margin-top: -9px;
    }
    .nice-select.table-select.open {
      border-bottom-right-radius: 0;
      border-bottom-left-radius: 0;
    }
    @media only screen and (max-width : 767px) {
      .nice-select.table-select {
        padding: 3px 10px;
      }
      .nice-select.table-select ul.list li {
        line-height: 30px;
        min-height: 30px;
      }
    }

    .input-group .input-group-addon {
      background-color: transparent;
      border: 0;
      position: relative;
    }
    .input-group .input-group-addon:before {
      content: "";
      position: absolute;
      width: 11px;
      height: 1px;
      background-color: #6c6c6c;
      left: 50%;
      margin-left: -6px;
      top: 25px;
    }

    #tabApplication .form-inline .form-group, #tabApplication .form-inline .form-group.large {
      width: 22%;
    }
    #tabApplication .form-inline .form-group:first-child {
      width: 28%;
    }
    #tabApplication .tab-search .search-wrap {
      width: 230px;
    }
    @media only screen and (max-width: 767px) {
      #tabApplication .form-inline .form-group {
        width: 45%;
      }
      #tabApplication .form-inline .form-group.large {
        width: 85%;
      }
      #tabApplication .form-inline .form-group:first-child {
        width: 85%;
      }
    }

    .tab-gp ul.nav.nav-tabs {
      border: 0;
      border-radius: 14px;
      /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#efefef+0,ffffff+100 */
      background: white;
      /* Old browsers */
      display: flex;
      width: 100%;
      -webkit-box-shadow: 0px 5px 24px 0px rgba(0, 0, 0, 0.2);
      -moz-box-shadow: 0px 5px 24px 0px rgba(0, 0, 0, 0.2);
      box-shadow: 0px 5px 24px 0px rgba(0, 0, 0, 0.2);
    }
    .tab-gp ul.nav.nav-tabs li {
      margin-bottom: 0;
      flex-basis: 100%;
      padding-left: 0;
      position: relative;
    }
    .tab-gp ul.nav.nav-tabs li a {
      background: transparent;
      font-size: 1.8rem;
      cursor: pointer;
      border: 0;
      border-radius: 0;
      margin-right: 0;
      text-align: center;
      padding: 25px 0;
      color: #333333;
      position: relative;
      height: 100%;
    }
    .tab-gp ul.nav.nav-tabs li a:hover {
      background: #EEEEEE;
      /* Old browsers */
    }
    .tab-gp ul.nav.nav-tabs li:after {
      content: "";
      position: absolute;
      width: 0;
      height: 0;
      border-style: solid;
      border-width: 10px 9px 0 9px;
      border-color: transparent;
      -webkit-transition: all 0.25s ease;
      -moz-transition: all 0.25s ease;
      -ms-transition: all 0.25s ease;
      -o-transition: all 0.25s ease;
      transition: all 0.25s ease;
      bottom: -10px;
      left: 50%;
      margin-left: -9px;
    }
    .tab-gp ul.nav.nav-tabs li:before {
      content: none;
    }
    .tab-gp ul.nav.nav-tabs li.active a {
      background: #147aab;
      color: white;
    }
    .tab-gp ul.nav.nav-tabs li.active:after {
      border-color: #147aab transparent transparent transparent;
    }
    .tab-gp ul.nav.nav-tabs li:first-child a {
      border-top-left-radius: 14px;
      border-bottom-left-radius: 14px;
    }
    .tab-gp ul.nav.nav-tabs li:last-child a {
      border-top-right-radius: 14px;
      border-bottom-right-radius: 14px;
    }
    .tab-gp .tab-content {
      padding: 50px 90px;
    }
    .tab-gp .tab-content .preview-info {
      margin-bottom: 25px;
    }
    .tab-gp .tab-content .preview-info.incompleted p {
      color: #4d4d4d;
    }
    .tab-gp .tab-content .preview-info.incompleted p:first-child {
      color: #333333;
    }
    .tab-gp .tab-content .preview-info.incompleted p:first-child:after {
      content: "";
      font-family: FontAwesome, sans-serif;      width: 22px;
      height: 22px;
      border: 0;
      background-color: #DB3D35;
      color: #fff;
      font-size: 14px;
      display: inline-block;
      position: relative;
      text-align: center;
      border-radius: 50%;
      line-height: 22px;
      font-weight: 400;
      vertical-align: bottom;
      margin-left: 10px;
    }
    @media only screen and (max-width : 767px) {
      .tab-gp .tab-content .preview-info.incompleted p:first-child:after {
        width: 20px;
        height: 20px;
        font-size: 11px;
        line-height: 22px;
      }
    }
    .tab-gp .tab-content .service-info-gp {
      margin-bottom: 40px;
    }
    .tab-gp .tab-content .service-info-gp h2 {
      margin-bottom: 30px;
    }
    @media only screen and (max-width : 767px) {
      .tab-gp .tab-content {
        padding: 30px 0;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .tab-gp .tab-content {
        padding: 35px 0;
      }
    }
    @media only screen and (min-width : 993px) and (max-width : 1200px) {
      .tab-gp .tab-content {
        padding: 35px 0;
      }
    }
    .tab-gp.dashboard-tab .nav.nav-tabs li:not(.active) + li:not(.active) a:before {
      content: "";
      position: absolute;
      height: 28px;
      width: 1px;
      background-color: #BFBFBF;
      left: 0;
      top: 50%;
      margin-top: -14px;
    }
    .tab-gp.dashboard-tab .tab-nav-mobile .swiper-wrapper .swiper-slide:not(.activeItem) + .swiper-slide:not(.activeItem) a:before {
      content: "";
      position: absolute;
      height: 28px;
      width: 1px;
      background-color: #BFBFBF;
      left: 0;
      top: 50%;
      margin-top: -14px;
    }
    .tab-gp .tab-nav-mobile {
      width: 100vw;
      margin-left: -15px;
      position: relative;
      background: #efefef;
      /* Old browsers */
      background: -moz-linear-gradient(left, #efefef 0%, white 100%);
      /* FF3.6-15 */
      background: -webkit-linear-gradient(left, #efefef 0%, white 100%);
      /* Chrome10-25,Safari5.1-6 */
      background: linear-gradient(to right, #efefef 0%, white 100%);
      /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
      filter: progid:DXImageTransform.Microsoft.gradient( startColorstr="#efefef", endColorstr="#ffffff",GradientType=1 );
      /* IE6-9 */
      -webkit-box-shadow: 0px 5px 24px 0px rgba(0, 0, 0, 0.2);
      -moz-box-shadow: 0px 5px 24px 0px rgba(0, 0, 0, 0.2);
      box-shadow: 0px 5px 24px 0px rgba(0, 0, 0, 0.2);
    }
    .tab-gp .tab-nav-mobile .swiper-wrapper .swiper-slide {
      display: table;
      height: 65px;
    }
    .tab-gp .tab-nav-mobile .swiper-wrapper .swiper-slide a {
      font-size: 1.6rem;
      color: #333333;
      display: table-cell;
      text-align: center;
      vertical-align: middle;
    }
    .tab-gp .tab-nav-mobile .swiper-wrapper .swiper-slide:after {
      content: "";
      position: absolute;
      width: 0;
      height: 0;
      left: 50%;
      margin-left: -12px;
      bottom: -10px;
      border-style: solid;
      border-width: 10px 12px 0 12px;
      border-color: transparent transparent transparent transparent;
      -webkit-transition: all 0.25s ease;
      -moz-transition: all 0.25s ease;
      -ms-transition: all 0.25s ease;
      -o-transition: all 0.25s ease;
      transition: all 0.25s ease;
    }
    .tab-gp .tab-nav-mobile .swiper-wrapper .swiper-slide.activeItem {
      background-color: #1989BF;
    }
    .tab-gp .tab-nav-mobile .swiper-wrapper .swiper-slide.activeItem a {
      color: white;
    }
    .tab-gp .tab-nav-mobile .swiper-wrapper .swiper-slide.activeItem:after {
      border-color: #1989BF transparent transparent transparent;
    }
    .tab-gp .tab-nav-mobile .swiper-button-prev,
    .tab-gp .tab-nav-mobile .swiper-button-next {
      background-image: none;
      width: 16px;
      height: 26px;
      top: 50%;
      margin-top: -13px;
    }
    .tab-gp .tab-nav-mobile .swiper-button-prev:before,
    .tab-gp .tab-nav-mobile .swiper-button-next:before {
      font-size: 3.4rem;
      font-family: FontAwesome, sans-serif;      display: block;
      width: 16px;
      height: 26px;
      line-height: 26px;
      text-align: center;
      color: #333333;
    }
    .tab-gp .tab-nav-mobile .swiper-button-prev.whitecolor:before,
    .tab-gp .tab-nav-mobile .swiper-button-next.whitecolor:before {
      color: white;
    }
    .tab-gp .tab-nav-mobile .swiper-button-prev:before {
      content: "";
    }
    .tab-gp .tab-nav-mobile .swiper-button-next:before {
      content: "";
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .tab-gp .tab-nav-mobile {
        margin-left: 0;
        width: 100%;
      }
    }
    .tab-gp.steps-tab .nav.nav-tabs li {
      counter-increment: item;
    }
    .tab-gp.steps-tab .nav.nav-tabs li a:before {
      content: counter(item);
      width: 34px;
      height: 34px;
      font-size: 1.6rem;
      line-height: 32px;
      text-align: center;
      vertical-align: middle;
      border: 1px solid #333333;
      border-radius: 50%;
      color: #333333;
      position: relative;
      display: block;
      margin-left: auto;
      margin-right: auto;
      font-weight: 600;
      margin-bottom: 17px;
    }
    .tab-gp.steps-tab .nav.nav-tabs li.complete a:before {
      border: 1px solid #168926;
    }
    .tab-gp.steps-tab .nav.nav-tabs li.complete a:after {
      content: "";
      font-family: FontAwesome, sans-serif;      width: 27px;
      height: 27px;
      border: 2px solid white;
      background-color: #168926;
      color: white;
      font-size: 14px;
      font-weight: 400;
      display: block;
      position: absolute;
      text-align: center;
      line-height: 23px;
      border-radius: 50%;
      top: 13px;
      left: 50%;
      margin-left: 5px;
    }
    .tab-gp.steps-tab .nav.nav-tabs li.incomplete a:before {
      border: 1px solid #c4322b;
    }
    .tab-gp.steps-tab .nav.nav-tabs li.incomplete a:after {
      content: "";
      font-family: FontAwesome, sans-serif;      width: 27px;
      height: 27px;
      border: 2px solid white;
      background-color: #c4322b;
      color: white;
      font-size: 14px;
      font-weight: 400;
      display: block;
      position: absolute;
      text-align: center;
      line-height: 23px;
      border-radius: 50%;
      top: 13px;
      left: 50%;
      margin-left: 5px;
    }
    .tab-gp.steps-tab .nav.nav-tabs li.active a:before {
      color: white;
      border: 1px solid white;
    }
    .tab-gp.steps-tab .tab-nav-mobile .swiper-wrapper .swiper-slide {
      counter-increment: item;
      height: 110px;
    }
    .tab-gp.steps-tab .tab-nav-mobile .swiper-wrapper .swiper-slide a {
      padding-top: 30px;
      position: relative;
    }
    .tab-gp.steps-tab .tab-nav-mobile .swiper-wrapper .swiper-slide a:before {
      content: counter(item);
      width: 30px;
      height: 30px;
      font-size: 1.4rem;
      line-height: 28px;
      text-align: center;
      vertical-align: middle;
      border: 1px solid #333333;
      border-radius: 50%;
      color: #333333;
      position: absolute;
      display: block;
      margin-left: auto;
      margin-right: auto;
      font-weight: 600;
      margin-bottom: 0;
      top: 12px;
      left: 50%;
      margin-left: -15px;
    }
    @media only screen and (max-width : 767px) {
      .tab-gp.steps-tab .tab-nav-mobile .swiper-wrapper .swiper-slide a {
        padding-right: 35px;
        padding-left: 35px;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .tab-gp.steps-tab .tab-nav-mobile .swiper-wrapper .swiper-slide a {
        padding-right: 30px;
        padding-left: 30px;
      }
    }
    .tab-gp.steps-tab .tab-nav-mobile .swiper-wrapper .swiper-slide.activeItem a:before {
      border-color: white;
      color: white;
    }
    .tab-gp.side-tab ul.nav.nav-pills.nav-stacked {
      width: 200px;
      float: left;
      background-color: white;
    }
    .tab-gp.side-tab ul.nav.nav-pills.nav-stacked li {
      padding-left: 0;
      position: relative;
      margin-bottom: 0;
    }
    .tab-gp.side-tab ul.nav.nav-pills.nav-stacked li a {
      font-size: 1.6rem;
      color: #333333;
      padding: 27px 17px;
      border-radius: 0;
      border-bottom: 1px solid #E3E3E3;
    }
    .tab-gp.side-tab ul.nav.nav-pills.nav-stacked li a:hover {
      color: #989898;
      background-color: white;
    }
    .tab-gp.side-tab ul.nav.nav-pills.nav-stacked li:first-child a {
      border-top: 1px solid #E3E3E3;
    }
    .tab-gp.side-tab ul.nav.nav-pills.nav-stacked li:before {
      content: "";
      position: absolute;
      width: 5px;
      height: 100%;
      background-color: transparent;
      top: 0;
      z-index: 10;
      left: auto;
      right: 0;
      border-radius: 0;
      -webkit-transition: all 0.25s ease;
      -moz-transition: all 0.25s ease;
      -ms-transition: all 0.25s ease;
      -o-transition: all 0.25s ease;
      transition: all 0.25s ease;
    }
    .tab-gp.side-tab ul.nav.nav-pills.nav-stacked li:after {
      content: "";
      position: absolute;
      width: 0;
      height: 0;
      right: -12px;
      margin-top: -10px;
      top: 50%;
      border-style: solid;
      border-width: 10px 0 10px 12px;
      border-color: transparent transparent transparent transparent;
      -webkit-transition: all 0.25s ease;
      -moz-transition: all 0.25s ease;
      -ms-transition: all 0.25s ease;
      -o-transition: all 0.25s ease;
      transition: all 0.25s ease;
    }
    .tab-gp.side-tab ul.nav.nav-pills.nav-stacked li.active a {
      background: #DCF4FF;
      color: #333333;
      font-weight: 700;
    }
    .tab-gp.side-tab ul.nav.nav-pills.nav-stacked li.active a:hover {
      color: #1989BF;
    }
    .tab-gp.side-tab ul.nav.nav-pills.nav-stacked li.active:before {
      background-color: #1989BF;
    }
    .tab-gp.side-tab ul.nav.nav-pills.nav-stacked li.active:after {
      border-color: transparent transparent transparent #1989BF;
    }
    .tab-gp.side-tab ul.nav.nav-pills.nav-stacked li + li {
      margin-top: 0;
    }
    .tab-gp.side-tab ul.nav.nav-pills.nav-stacked li:hover a,
    .tab-gp.side-tab ul.nav.nav-pills.nav-stacked li a:hover {
      color: #989898;
    }
    .tab-gp.side-tab .mobile-side-nav-tab {
      width: 350px;
      margin-left: auto;
      margin-right: auto;
      max-width: 85%;
    }
    .tab-gp.side-tab .tab-content {
      width: calc(100% - 200px);
      float: left;
      padding-left: 60px;
      border-left: 1px solid rgba(191, 191, 191, 0.35);
      padding-top: 25px;
    }
    @media only screen and (max-width : 767px) {
      .tab-gp.side-tab .tab-content {
        width: 100%;
        padding-left: 0;
        border-left: 0;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .tab-gp.side-tab .tab-content {
        width: 100%;
        padding-left: 0;
        border-left: 0;
      }
    }
    @media only screen and (max-width : 767px) {
      .tab-gp {
        position: relative;
      }
    }

    .progress-tracker {
      display: flex;
      margin-bottom: 50px;
    }
    .progress-tracker li {
      list-style-type: none;
      font-size: 1.6rem;
      color: #333333;
      flex-basis: 100%;
      position: relative;
      text-align: center;
      padding: 0 10px;
    }
    .progress-tracker li:before {
      content: "";
      position: relative;
      width: 15px;
      height: 15px;
      border: 1px solid #BFBFBF;
      border-radius: 50%;
      background-color: white;
      display: block;
      left: 50%;
      margin-left: -7.5px;
      top: 0;
      margin-bottom: 20px;
      z-index: 10;
    }
    .progress-tracker li:not(:first-child):after {
      content: "";
      position: absolute;
      height: 1px;
      width: 100%;
      right: 50%;
      background-color: #BFBFBF;
      top: 7px;
    }
    .progress-tracker li.active {
      font-weight: 700;
    }
    .progress-tracker li.active:before {
      background-color: #1989BF;
      border-color: #1989BF;
    }
    .progress-tracker li.active:after {
      height: 2px;
      top: 7.5px;
      background-color: #1989BF;
    }
    .progress-tracker li.completed:before {
      background-color: #1989BF;
      border-color: #1989BF;
    }
    .progress-tracker li.completed:after {
      height: 2px;
      top: 7.5px;
      background-color: #1989BF;
    }
    .progress-tracker li.disabled {
      color: #6a6a6a;
    }
    @media only screen and (max-width : 767px) {
      .progress-tracker {
        margin-bottom: 30px;
      }
      .progress-tracker li {
        font-size: 0;
      }
      .progress-tracker li:before {
        left: 50%;
        width: 12px;
        height: 12px;
        margin-left: -6px;
        margin-bottom: 0;
      }
      .progress-tracker li:not(:first-child):after {
        top: 50%;
        right: 50%;
        margin-top: -0.5px;
      }
      .progress-tracker li:not(:first-child)::after {
        content: "";
        position: absolute;
        height: 1px;
        width: 125%;
        right: 50%;
        background-color: #BFBFBF;
        top: 7px;
      }
      .progress-tracker li.completed:after {
        top: 50%;
        margin-top: -1px;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .progress-tracker {
        margin-bottom: 30px;
      }
      .progress-tracker li {
        font-size: 0;
      }
    }

    .btn-tooltip {
      font-size: 1.4rem;
      font-weight: 700;
      width: 23px;
      height: 23px;
      background-color: #333333;
      display: inline-block;
      line-height: 23px;
      color: white;
      font-style: italic;
      border-radius: 50%;
      padding-left: 8px;
      padding-right: 10px;
    }
    .btn-tooltip:hover {
      color: white;
    }
    @media only screen and (max-width : 767px) {
      .btn-tooltip {
        width: 20px;
        height: 20px;
        font-size: 1.2rem;
        padding-left: 7px;
        padding-right: 9px;
        line-height: 19px;
      }
    }

    .tooltip.in {
      opacity: 1;
      padding-bottom: 15px;
    }
    .tooltip.top .tooltip-arrow {
      bottom: 6px;
      margin-left: -8px;
      border-width: 10px 8px 0;
      border-top-color: white;
    }
    .tooltip .tooltip-inner {
      background-color: white;
      color: #333333;
      border: 0.5px solid #E8E8E8;
      border-radius: 1.4rem;
      width: 250px;
      max-width: 250px;
      padding: 15px 20px;
      text-align: left;
      -webkit-box-shadow: 0px 5px 18px 0px rgba(0, 0, 0, 0.18);
      -moz-box-shadow: 0px 5px 18px 0px rgba(0, 0, 0, 0.18);
      box-shadow: 0px 5px 18px 0px rgba(0, 0, 0, 0.18);
    }
    .tooltip .tooltip-inner p {
      font-size: 1.4rem;
      color: #333333;
      line-height: 1.5;
      margin-bottom: 0;
    }
    @media only screen and (max-width : 767px) {
      .tooltip .tooltip-inner {
        padding: 12px 15px;
        width: 200px;
      }
      .tooltip .tooltip-inner p {
        font-size: 1.2rem;
      }
    }

    .table-gp table.table > thead > tr > th {
      font-size: 1.6rem;
      color: #333333;
      font-weight: 700;
      padding: 15px 25px 15px 0;
      border-bottom: 3px solid #bababa;
      position: relative;
    }
    .table-gp table.table > thead > tr > th:after {
      cursor: pointer;
    }
    .table-gp table.table > thead > tr > th span.sort {
      cursor: pointer;
    }
    .table-gp table.table > thead > tr > th span.sort:after {
      content: "";
      font-family: FontAwesome, sans-serif;      margin-left: 10px;
      font-size: 1.4rem;
      color: #666;
    }
    .table-gp table.table > thead > tr > th span.desc {
      cursor: pointer;
    }
    .table-gp table.table > thead > tr > th span.desc:after {
      content: "";
      font-family: FontAwesome, sans-serif;      margin-left: 10px;
      font-size: 1.6rem;
      color: #147aab;
      display: inline-block;
      vertical-align: 3px;
    }
    .table-gp table.table > thead > tr > th span.asc {
      cursor: pointer;
    }
    .table-gp table.table > thead > tr > th span.asc:after {
      content: "";
      font-family: FontAwesome, sans-serif;      margin-left: 10px;
      font-size: 1.6rem;
      color: #147aab;
      display: inline-block;
      vertical-align: text-bottom;
      line-height: 12px;
    }
    @media only screen and (max-width : 767px) {
      .table-gp table.table > thead {
        display: none;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .table-gp table.table > thead {
        display: none;
      }
    }
    .table-gp table.table > tbody > tr > td {
      padding: 15px 25px 15px 0;
      border-bottom: 1px solid #E2E2E2;
      vertical-align: top;
    }
    .table-gp table.table > tbody > tr > td p {
      margin-bottom: 0;
    }
    .table-gp table.table > tbody > tr > td p a[data-toggle=collapse] {
      position: relative;
      margin-right: 25px;
    }
    .table-gp table.table > tbody > tr > td p a[data-toggle=collapse]:after {
      content: "";
      position: absolute;
      font-family: FontAwesome, sans-serif;      font-size: 2.4rem;
      line-height: 22px;
      right: -25px;
      display: inline-block;
      height: 20px;
      overflow: hidden;
    }
    @media only screen and (max-width : 767px) {
      .table-gp table.table > tbody > tr > td p a[data-toggle=collapse]:after {
        line-height: 19px;
      }
    }
    .table-gp table.table > tbody > tr > td p a[data-toggle=collapse].collapsed:after {
      content: "";
    }
    .table-gp table.table > tbody > tr > td p a[data-toggle=collapse]:active, .table-gp table.table > tbody > tr > td p a[data-toggle=collapse]:focus {
      color: #147aab;
      text-decoration: underline;
    }
    .table-gp table.table > tbody > tr > td[rowspan] {
      vertical-align: middle;
    }
    @media only screen and (max-width : 767px) {
      .table-gp table.table > tbody > tr > td[rowspan] {
        width: 100%;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .table-gp table.table > tbody > tr > td[rowspan] {
        width: 100%;
      }
    }
    .table-gp table.table > tbody > tr > td .form-check {
      margin-bottom: 0;
    }
    .table-gp table.table > tbody > tr > td .form-check .form-check-label {
      margin-bottom: 0;
    }
    .table-gp table.table > tbody > tr[data-child-row] > td:first-child {
      padding-left: 30px;
    }
    @media only screen and (max-width : 767px) {
      .table-gp table.table > tbody > tr[data-child-row] > td:first-child {
        padding-left: 0;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .table-gp table.table > tbody > tr[data-child-row] > td:first-child {
        padding-left: 0;
      }
    }
    .table-gp table.table > tbody > tr.selectedRow > td {
      border-top: 1px solid #1989BF;
      border-bottom: 1px solid #1989BF;
    }
    .table-gp table.table > tbody > tr.selectedRow > td:first-child {
      border-top-left-radius: 14px;
      border-bottom-left-radius: 14px;
      border-left: 1px solid #1989BF;
    }
    .table-gp table.table > tbody > tr.selectedRow > td:last-child {
      border-top-right-radius: 14px;
      border-bottom-right-radius: 14px;
      border-right: 1px solid #1989BF;
    }
    @media only screen and (max-width : 992px) {
      .table-gp table.table > tbody > tr.selectedRow {
        position: relative;
        border-bottom: 0;
      }
      .table-gp table.table > tbody > tr.selectedRow > td {
        border-top: 0;
        border-bottom: 0;
      }
      .table-gp table.table > tbody > tr.selectedRow > td:first-child {
        border-top-left-radius: 0;
        border-bottom-left-radius: 0;
        border-left: 0;
      }
      .table-gp table.table > tbody > tr.selectedRow > td:last-child {
        border-top-right-radius: 0;
        border-bottom-right-radius: 0;
        border-right: 0;
      }
      .table-gp table.table > tbody > tr.selectedRow:after {
        content: "";
        position: absolute;
        width: calc(100% + 14px);
        height: calc(100% + 6px);
        border: 1px solid #1989BF;
        border-radius: 14px;
        left: -7px;
        top: -8px;
      }
    }
    @media only screen and (max-width : 767px) {
      .table-gp table.table > tbody > tr {
        width: 100%;
        border-bottom: 1px solid #bababa;
      }
      .table-gp table.table > tbody > tr > td {
        display: inline-grid;
        display: -ms-inline-grid;
        width: 47%;
        padding: 0;
        border: 0;
        margin-bottom: 15px;
        padding-right: 2%;
      }
      .table-gp table.table > tbody > tr > td p {
        font-size: 1.4rem;
      }
      .table-gp table.table > tbody > tr > td p.table-row-title {
        font-weight: 600;
        margin-bottom: 3px;
        line-height: 9px;
      }
      .table-gp table.table > tbody > tr:not(:last-child) {
        margin-bottom: 20px;
      }
      .table-gp table.table > tbody > tr:not(.collapse) {
        display: block;
      }
      .table-gp table.table > tbody > tr.collapse.in {
        padding-left: 15px;
        display: block;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .table-gp table.table > tbody > tr {
        width: 100%;
        border-bottom: 1px solid #bababa;
      }
      .table-gp table.table > tbody > tr > td {
        display: inline-grid;
        display: -ms-inline-grid;
        width: 48%;
        padding: 0;
        border: 0;
        margin-bottom: 15px;
        padding-left: 1%;
      }
      .table-gp table.table > tbody > tr > td p {
        font-size: 1.6rem;
        text-align: left;
      }
      .table-gp table.table > tbody > tr > td p.table-row-title {
        font-weight: 600;
        margin-bottom: 3px;
      }
      .table-gp table.table > tbody > tr:not(:last-child) {
        margin-bottom: 20px;
      }
      .table-gp table.table > tbody > tr:not(.collapse) {
        display: block;
      }
      .table-gp table.table > tbody > tr.collapse.in {
        display: block;
        padding-left: 20px;
      }
    }
    .table-gp table.table.discipline-table .nice-select {
      width: 70%;
    }
    .table-gp .table-footnote p.count {
      color: #666;
      margin-bottom: 0;
    }
    /********************small table***********************/

    .table-sm table.table > thead > tr > th {
      font-size: 1.6rem;
      color: #333333;
      font-weight: 700;
      padding: 15px 25px 15px 0;
      border-bottom: 3px solid #bababa;
      position: relative;
    }
    .table-sm table.table > thead > tr > th:after {
      cursor: pointer;
    }
    .table-sm table.table > thead > tr > th span.sort {
      cursor: pointer;
    }
    .table-sm table.table > thead > tr > th span.sort:after {
      content: "";
      font-family: FontAwesome, sans-serif;      margin-left: 10px;
      font-size: 1.4rem;
      color: #BFBFBF;
    }
    .table-sm table.table > thead > tr > th span.desc {
      cursor: pointer;
    }
    .table-sm table.table > thead > tr > th span.desc:after {
      content: "";
      font-family: FontAwesome, sans-serif;      margin-left: 10px;
      font-size: 1.6rem;
      color: #147aab;
      display: inline-block;
      vertical-align: 3px;
    }
    .table-sm table.table > thead > tr > th span.asc {
      cursor: pointer;
    }
    .table-sm table.table > thead > tr > th span.asc:after {
      content: "";
      font-family: FontAwesome, sans-serif;      margin-left: 10px;
      font-size: 1.6rem;
      color: #147aab;
      display: inline-block;
      vertical-align: text-bottom;
      line-height: 12px;
    }
    .table-sm table.table > tbody > tr > td {
      font-size: 1.3rem;
      padding: 4px;
      line-height: 1.74876;
    }
    .table-sm table.table > tbody > tr > td p {
      font-size: 14px;
      margin-bottom: 5px;
    }


    /**************************************************/
    @media only screen and (max-width : 767px) {
      .table-gp .nav {
        background: transparent;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .table-gp .nav {
        background: transparent;
      }
    }

    @media only screen and (min-width : 1201px) {
      #tabApplication table.table > thead > tr > th:last-child,
      #tabApplication table.table > tbody > tr > td:last-child {
        width: 200px;
      }
    }

    #tabLicence table.table > thead > tr > th:nth-child(4),
    #tabLicence table.table > tbody > tr > td:nth-child(4) {
      max-width: 180px;
    }
    #tabLicence table.table > tbody > tr > td a[data-toggle=collapse] {
      padding-bottom: 20px;
      display: inline-block;
      -webkit-transition: all 0.25s ease;
      -moz-transition: all 0.25s ease;
      -ms-transition: all 0.25s ease;
      -o-transition: all 0.25s ease;
      transition: all 0.25s ease;
    }
    #tabLicence table.table > tbody > tr > td a[data-toggle=collapse].collapsed {
      padding-bottom: 0;
    }

    .addPersonnel table.table > thead > tr > th:first-child,
    .addPersonnel table.table > tbody > tr > td:first-child,
    .updatePersonnelInfo table.table > thead > tr > th:first-child,
    .updatePersonnelInfo table.table > tbody > tr > td:first-child,
    .addSubsumedService table.table > thead > tr > th:first-child,
    .addSubsumedService table.table > tbody > tr > td:first-child {
      min-width: 200px;
    }
    @media only screen and (max-width : 767px) {
      .addPersonnel table.table > thead > tr > th:first-child,
      .addPersonnel table.table > tbody > tr > td:first-child,
      .updatePersonnelInfo table.table > thead > tr > th:first-child,
      .updatePersonnelInfo table.table > tbody > tr > td:first-child,
      .addSubsumedService table.table > thead > tr > th:first-child,
      .addSubsumedService table.table > tbody > tr > td:first-child {
        min-width: 47%;
      }
    }
    @media only screen and (min-width : 993px) and (max-width : 1200px) {
      .addPersonnel table.table > thead > tr > th:first-child,
      .addPersonnel table.table > tbody > tr > td:first-child,
      .updatePersonnelInfo table.table > thead > tr > th:first-child,
      .updatePersonnelInfo table.table > tbody > tr > td:first-child,
      .addSubsumedService table.table > thead > tr > th:first-child,
      .addSubsumedService table.table > tbody > tr > td:first-child {
        min-width: 170px;
      }
    }
    .addPersonnel table.table > thead > tr > th:nth-child(4),
    .addPersonnel table.table > tbody > tr > td:nth-child(4),
    .updatePersonnelInfo table.table > thead > tr > th:nth-child(4),
    .updatePersonnelInfo table.table > tbody > tr > td:nth-child(4),
    .addSubsumedService table.table > thead > tr > th:nth-child(4),
    .addSubsumedService table.table > tbody > tr > td:nth-child(4) {
      max-width: 240px;
    }
    .addPersonnel table.table > thead > tr > th:nth-child(5), .addPersonnel table.table > thead > tr > th:last-child,
    .addPersonnel table.table > tbody > tr > td:nth-child(5),
    .addPersonnel table.table > tbody > tr > td:last-child,
    .updatePersonnelInfo table.table > thead > tr > th:nth-child(5),
    .updatePersonnelInfo table.table > thead > tr > th:last-child,
    .updatePersonnelInfo table.table > tbody > tr > td:nth-child(5),
    .updatePersonnelInfo table.table > tbody > tr > td:last-child,
    .addSubsumedService table.table > thead > tr > th:nth-child(5),
    .addSubsumedService table.table > thead > tr > th:last-child,
    .addSubsumedService table.table > tbody > tr > td:nth-child(5),
    .addSubsumedService table.table > tbody > tr > td:last-child {
      min-width: 140px;
    }

    .changeOperationAddress table.table > thead > tr > th:nth-child(2),
    .changeOperationAddress table.table > tbody > tr > td:nth-child(2) {
      max-width: 250px;
    }
    .changeOperationAddress table.table > thead > tr > th:last-child,
    .changeOperationAddress table.table > tbody > tr > td:last-child {
      max-width: 300px;
    }

    .licence-renewal-content .table-gp table thead th.premises-info {
      width: 350px;
    }
    @media only screen and (max-width : 992px) {
      .licence-renewal-content .table-gp table thead th.premises-info {
        width: auto;
      }
    }
    .licence-renewal-content .table-gp table tbody > tr > td ul {
      margin-bottom: 0;
    }
    .licence-renewal-content .table-gp table tbody > tr:last-child > td {
      border-bottom: 0;
    }
    @media only screen and (max-width : 992px) {
      .licence-renewal-content .table-gp table tbody > tr:last-child {
        border-bottom: 0;
      }
    }

    iframe .section table .control-grid > tbody > tr:nth-child(odd), iframe .section table .control-grid > tbody > tr:nth-child(even) {
      background-color: white;
    }

    .modal .modal-content {
      padding: 15px 45px;
    }
    .modal .modal-header {
      padding: 0;
    }
    .modal .modal-header .modal-close {
      width: 20px;
      height: 20px;
      display: block;
      position: relative;
      margin-left: auto;
    }
    .modal .modal-header .modal-close:before, .modal .modal-header .modal-close:after {
      content: "";
      position: absolute;
      width: 18px;
      height: 2px;
      background-color: #333333;
      left: 0;
      top: 50%;
      margin-top: -1.5px;
      -webkit-transition: all 0.25s ease;
      -moz-transition: all 0.25s ease;
      -ms-transition: all 0.25s ease;
      -o-transition: all 0.25s ease;
      transition: all 0.25s ease;
    }
    .modal .modal-header .modal-close:before {
      -webkit-transform: rotate(225deg);
      -moz-transform: rotate(225deg);
      -ms-transform: rotate(225deg);
      -o-transform: rotate(225deg);
      transform: rotate(225deg);
    }
    .modal .modal-header .modal-close:after {
      -webkit-transform: rotate(-45deg);
      -moz-transform: rotate(-45deg);
      -ms-transform: rotate(-45deg);
      -o-transform: rotate(-45deg);
      transform: rotate(-45deg);
    }
    .modal .modal-body {
      padding: 15px 0;
    }
    .modal .modal-body .modal-footer-button-group {
      margin-top: 30px;
    }
    @media only screen and (max-width : 767px) {
      .modal .modal-content {
        padding: 10px 25px;
      }
    }

    .nav ul.pagination {
      margin: 0;
    }
    .nav ul.pagination > li {
      padding-left: 0;
    }
    .nav ul.pagination > li > a,
    .nav ul.pagination > li span {
      float: none;
      border: 0;
      background-color: white;
      font-size: 1.6rem;
      color: #147aab;
    }
    .nav ul.pagination > li > a i.fa,
    .nav ul.pagination > li span i.fa {
      font-size: 1.4rem;
    }
    .nav ul.pagination > li > a:hover,
    .nav ul.pagination > li span:hover {
      background-color: white;
    }
    @media only screen and (max-width : 767px) {
      .nav ul.pagination > li > a,
      .nav ul.pagination > li span {
        font-size: 1.4rem;
        padding: 6px 10px;
      }
      .nav ul.pagination > li > a i.fa,
      .nav ul.pagination > li span i.fa {
        font-size: 1.2rem;
      }
    }
    .nav ul.pagination > li:before {
      content: none;
    }
    .nav ul.pagination > li.active a,
    .nav ul.pagination > li.active span {
      color: #666;
      pointer-events: none;
    }

    .dashboard {
      padding: 30px 0 70px;
      overflow: hidden;
      width: 100vw;
      background-size: 100% auto;
      background-position: 50% 0;
    }
    .dashboard.title-only {
      overflow: visible;
    }
    .dashboard.title-only .navigation-gp {
      margin-bottom: 0;
    }
    .dashboard.dashboard.small-dashboard-with-subtext {
      padding-bottom: 30px;
    }
    .dashboard.dashboard.small-dashboard-with-subtext.dashboard-page-title {
      margin-bottom: 0;
    }
    @media only screen and (max-width : 767px) {
      .dashboard {
        background-size: auto 100%;
        background-position: 75% 0;
      }
      .dashboard.title-only {
        padding-bottom: 50px;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .dashboard {
        background-size: auto 100%;
        background-position: 75% 0;
      }
      .dashboard.title-only {
        padding-bottom: 50px;
      }
    }

    .dashboard-gp .dashboard-tile-item {
      width: calc((100% - 90px) / 4);
      float: left;
    }
    .dashboard-gp .dashboard-tile-item .dashboard-tile {
      /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#efefef+0,ffffff+100 */
      background: #efefef;
      /* Old browsers */
      background: -moz-linear-gradient(left, #efefef 0%, white 100%);
      /* FF3.6-15 */
      background: -webkit-linear-gradient(left, #efefef 0%, white 100%);
      /* Chrome10-25,Safari5.1-6 */
      background: linear-gradient(to right, #efefef 0%, white 100%);
      /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
      filter: progid:DXImageTransform.Microsoft.gradient( startColorstr="#efefef", endColorstr="#ffffff",GradientType=1 );
      /* IE6-9 */
      -webkit-box-shadow: 0px 12px 24px 0px rgba(0, 0, 0, 0.16);
      -moz-box-shadow: 0px 12px 24px 0px rgba(0, 0, 0, 0.16);
      box-shadow: 0px 12px 24px 0px rgba(0, 0, 0, 0.16);
      border-radius: 14px;
      height: 125px;
      margin-bottom: 35px;
    }
    .dashboard-gp .dashboard-tile-item .dashboard-tile a {
      padding: 35px 35px 32px;
      display: block;
      width: 100%;
      height: 100%;
      cursor: pointer;
    }
    .dashboard-gp .dashboard-tile-item .dashboard-tile h1.dashboard-count {
      display: inline-block;
      margin-right: 20px;
      margin-bottom: 0;
      width: 30px;
      vertical-align: middle;
      color: #333333;
    }
    .dashboard-gp .dashboard-tile-item .dashboard-tile .dashboard-txt {
      font-size: 1.9rem;
      color: #333333;
      margin-bottom: 0;
      width: calc(100% - 55px);
      display: inline-block;
      vertical-align: middle;
    }
    .dashboard-gp .dashboard-tile-item .dashboard-tile:hover, .dashboard-gp .dashboard-tile-item .dashboard-tile.hover {
      /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#efefef+0,ffffff+100 */
      background: #C2E7FE;
      /* Old browsers */
      background: -moz-linear-gradient(left, #C2E7FE 0%, #FFFFFF 100%);
      /* FF3.6-15 */
      background: -webkit-linear-gradient(left, #C2E7FE 0%, #FFFFFF 100%);
      /* Chrome10-25,Safari5.1-6 */
      background: linear-gradient(to right, #C2E7FE 0%, #FFFFFF 100%);
      /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
      filter: progid:DXImageTransform.Microsoft.gradient( startColorstr="#C2E7FE", endColorstr="#FFFFFF",GradientType=1 );
      /* IE6-9 */
    }
    .dashboard-gp .dashboard-tile-item .dashboard-tile.txt-only {
      background: #f36416;
      background: -moz-linear-gradient(left, #f36416 0%, #ec6f13 100%);
      background: -webkit-linear-gradient(left, #f36416 0%, #ec6f13 100%);
      background: linear-gradient(to right, #f36416 0%, #ec6f13 100%);
      filter: progid:DXImageTransform.Microsoft.gradient( startColorstr="#f36416", endColorstr="#ec6f13",GradientType=1 );
      /* IE6-9 */
    }
    .dashboard-gp .dashboard-tile-item .dashboard-tile.txt-only .dashboard-txt {
      color: white;
      width: 100%;
      line-height: 53px;
      font-weight: 600;
    }
    .dashboard-gp .dashboard-tile-item:not(:last-child) {
      margin-right: 30px;
    }
    .dashboard-gp:before, .dashboard-gp:after {
      display: table;
      content: "";
    }
    .dashboard-gp:after {
      clear: both;
    }
    @media only screen and (max-width : 1200px) {
      .dashboard-gp .dashboard-tile-item {
        width: calc((100% - 15px) / 2);
      }
      .dashboard-gp .dashboard-tile-item .dashboard-tile {
        height: 135px;
      }
      .dashboard-gp .dashboard-tile-item .dashboard-tile a {
        padding: 15px;
      }
      .dashboard-gp .dashboard-tile-item .dashboard-tile h1.dashboard-count {
        font-size: 4rem;
        display: block;
        width: 100%;
      }
      .dashboard-gp .dashboard-tile-item .dashboard-tile .dashboard-txt {
        width: 100%;
        font-size: 1.8rem;
        line-height: 26px !important;
      }
      .dashboard-gp .dashboard-tile-item:not(:last-child) {
        margin-right: 0;
      }
      .dashboard-gp .dashboard-tile-item:nth-child(2n+1) {
        margin-right: 15px;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .dashboard-gp .dashboard-tile-item {
        width: calc((100% - 15px) / 2);
      }
      .dashboard-gp .dashboard-tile-item .dashboard-tile {
        height: 100px;
      }
      .dashboard-gp .dashboard-tile-item .dashboard-tile h1.dashboard-count {
        display: inline-block;
        font-size: 5.2rem;
        width: 30px;
      }
      .dashboard-gp .dashboard-tile-item .dashboard-tile .dashboard-txt {
        display: inline-block;
        width: calc(100% - 60px);
        font-size: 2rem;
      }
      .dashboard-gp .dashboard-tile-item .dashboard-tile.txt-only .dashboard-txt {
        width: 80%;
      }
    }
    @media only screen and (min-width : 993px) and (max-width : 1200px) {
      .dashboard-gp .dashboard-tile-item {
        width: calc((100% - 15px) / 2);
      }
      .dashboard-gp .dashboard-tile-item .dashboard-tile {
        height: 90px;
      }
      .dashboard-gp .dashboard-tile-item .dashboard-tile h1.dashboard-count {
        display: inline-block;
        font-size: 5.2rem;
        width: 30px;
      }
      .dashboard-gp .dashboard-tile-item .dashboard-tile .dashboard-txt {
        display: inline-block;
        width: calc(100% - 60px);
      }
      .dashboard-gp .dashboard-tile-item .dashboard-tile.txt-only .dashboard-txt {
        width: 70%;
      }
    }

    .dashboard-footernote .dashboard-small-txt {
      text-align: right;
    }

    .dashboard-page-title {
      margin-top: 5px;
      margin-bottom: 30px;
    }
    .dashboard-page-title h1 {
      margin-top: 20px;
      margin-bottom: 25px;
    }
    .dashboard-page-title h3 {
      font-weight: 400;
      border-bottom: 0;
      padding-bottom: 0;
    }
    @media only screen and (max-width : 1200px) {
      .dashboard-page-title {
        margin-top: 25px;
        margin-bottom: 0;
      }
      .dashboard-page-title h1,
      .dashboard-page-title h3 {
        text-align: center;
      }
    }

    .main-content .tab-gp.dashboard-tab {
      margin-top: -38px;
    }
    .main-content .tab-gp.dashboard-tab .tab-search.sticky {
      position: fixed;
      background-color: white;
      width: 1070px;
      left: 50%;
      margin-left: -535px;
      top: 0;
      z-index: 1000;
      padding-top: 15px;
      border-bottom: 3px solid #bababa;
    }
    .main-content .tab-gp.dashboard-tab .tab-search.sticky .licence-btns .btn {
      margin-bottom: 25px;
    }
    @media only screen and (min-width : 993px) and (max-width : 1200px) {
      .main-content .tab-gp.dashboard-tab .tab-search.sticky {
        width: 940px;
        padding-top: 15px;
        margin-left: -470px;
      }
    }
    @media only screen and (max-width : 767px) {
      .main-content .tab-gp.dashboard-tab .tab-search {
        margin-bottom: 10px;
      }
      .main-content .tab-gp.dashboard-tab .tab-search.sticky {
        width: auto;
        left: 15px;
        right: 12px;
        margin-left: 0;
        padding: 15px 0 0;
        top: -63px;
        border-bottom: 2px solid #bababa;
      }
      .main-content .tab-gp.dashboard-tab .tab-search.sticky .licence-btns .btn {
        margin-bottom: 15px;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .main-content .tab-gp.dashboard-tab .tab-search.sticky {
        top: 0;
        width: 720px;
        left: 50%;
        margin-left: -360px;
        border-bottom: 2px solid #bababa;
      }
    }
    .main-content .tab-gp.steps-tab {
      margin-top: -75px;
    }

    .tab-search .licence-btns {
      width: calc(100% - 470px);
      float: left;
      margin-right: 20px;
    }
    .tab-search .search-wrap {
      float: left;
      width: 300px;
      margin-right: 20px;
    }
    .tab-search .advanced-search {
      float: left;
      margin-top: 15px;
      width: 130px;
      text-align: right;
    }
    @media only screen and (max-width : 767px) {
      .tab-search .search-wrap {
        margin-right: 0;
        width: 100%;
      }
      .tab-search.license-search {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
      }
      .tab-search.license-search .licence-btns {
        flex: 100% 1 1;
        width: auto;
        margin-right: 0;
        order: 3;
        margin-top: 15px;
      }
      .tab-search.license-search .search-wrap {
        width: calc(100% - 150px);
        margin-right: 15px;
      }
      .tab-search.license-search .advanced-search {
        width: auto;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .tab-search.license-search .licence-btns {
        flex: 1;
        width: auto;
        margin-right: 0;
        order: 1;
        margin-top: 0;
        width: calc(100% - 370px);
      }
      .tab-search.license-search .licence-btns .btn {
        padding-left: 20px;
        padding-right: 20px;
      }
      .tab-search.license-search .licence-btns .btn:not(:last-child) {
        margin-right: 8px;
      }
      .tab-search.license-search .search-wrap {
        order: 2;
        width: 220px;
      }
      .tab-search.license-search .advanced-search {
        order: 3;
        width: 130px;
      }
    }

    .self-assessment-gp .self-assessment-item {
      margin-bottom: 50px;
      padding-left: 100px;
      counter-increment: item;
      position: relative;
    }
    .self-assessment-gp .self-assessment-item .form-check-gp .form-check.progress-step-check {
      width: 350px;
    }
    .self-assessment-gp .self-assessment-item .form-check-gp .form-check.progress-step-check.large-form-width {
      width: 75%;
    }
    .self-assessment-gp .self-assessment-item .form-check-gp .form-check.progress-step-check .form-check-label .left-content {
      font-weight: 700;
      width: 33%;
      display: inline-block;
      vertical-align: middle;
      margin-right: 20px;
    }
    .self-assessment-gp .self-assessment-item .form-check-gp .form-check.progress-step-check .form-check-label .right-content {
      padding-left: 20px;
      border-left: 1px solid #bababa;
      vertical-align: middle;
      width: calc(66% - 30px);
      display: inline-block;
    }
    .self-assessment-gp .self-assessment-item table {
      border-collapse: separate;
    }
    .self-assessment-gp .self-assessment-item:before {
      content: counter(item);
      width: 34px;
      height: 34px;
      font-size: 1.6rem;
      line-height: 30px;
      text-align: center;
      vertical-align: middle;
      border: 2px solid #6c6c6c;
      border-radius: 50%;
      color: white;
      position: absolute;
      top: 0;
      left: 0;
      font-weight: 600;
      margin-bottom: 17px;
      background-color: #6c6c6c;
      z-index: 10;
    }
    .self-assessment-gp .self-assessment-item:after {
      content: "";
      position: absolute;
      width: 1px;
      height: auto;
      top: 17px;
      bottom: -50px;
      background-color: #bababa;
      left: 17px;
      opacity: 0;
    }
    .self-assessment-gp .self-assessment-item.completed:not(:last-child):after {
      opacity: 1;
    }
    .self-assessment-gp .self-assessment-item.current:before {
      background-color: white;
      border-color: #1989BF;
      color: #1989BF;
    }
    @media only screen and (max-width : 992px) {
      .self-assessment-gp .self-assessment-item {
        padding-left: 45px;
        margin-bottom: 30px;
      }
      .self-assessment-gp .self-assessment-item .form-check-gp .form-check.progress-step-check {
        margin-bottom: 15px;
        width: 100%;
      }
      .self-assessment-gp .self-assessment-item .form-check-gp .form-check.progress-step-check.large-form-width {
        width: 100%;
      }
      .self-assessment-gp .self-assessment-item .form-check-gp .form-check.progress-step-check .form-check-label span.check-circle {
        top: 15px;
        margin-top: 0;
      }
      .self-assessment-gp .self-assessment-item .form-check-gp .form-check.progress-step-check .form-check-label .left-content {
        width: 100%;
        margin-right: 0;
      }
      .self-assessment-gp .self-assessment-item .form-check-gp .form-check.progress-step-check .form-check-label .right-content {
        width: 100%;
        padding-left: 0;
        border-left: 0;
        border-top: 1px solid #bababa;
        margin-top: 10px;
        padding-top: 10px;
      }
      .self-assessment-gp .self-assessment-item:before {
        width: 28px;
        height: 28px;
        line-height: 26px;
        border: 1px solid #6c6c6c;
      }
      .self-assessment-gp .self-assessment-item:after {
        top: 28px;
        bottom: -30px;
        left: 14px;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .self-assessment-gp .self-assessment-item {
        padding-left: 45px;
        margin-bottom: 30px;
      }
      .self-assessment-gp .self-assessment-item .form-check-gp .form-check.progress-step-check .form-check-label span.check-circle {
        top: 50%;
        margin-top: -8px;
      }
      .self-assessment-gp .self-assessment-item .form-check-gp .form-check.progress-step-check .form-check-label .left-content {
        width: 33%;
        margin-right: 20px;
      }
      .self-assessment-gp .self-assessment-item .form-check-gp .form-check.progress-step-check .form-check-label .right-content {
        padding-left: 20px;
        border-left: 1px solid #bababa;
        width: calc(66% - 30px);
        border-top: 0;
        margin-top: 0;
        padding-top: 0;
      }
    }
    @media only screen and (min-width : 993px) and (max-width : 1200px) {
      .self-assessment-gp {
        min-height: calc(100vh - 540px);
      }
    }

    .updatePersonnelInfo .nice-select {
      margin-bottom: 30px;
    }

    .premises-txt {
      border-bottom: 1px solid #BABABA;
      margin-bottom: 40px;
    }
    .premises-txt p {
      margin-bottom: 50px;
    }

    .premises-summary,
    .new-premise-form-conveyance,
    .new-premise-form-on-site {
      margin-top: 40px;
      margin-bottom: 30px;
    }

    .new-premise-form-conveyance,
    .new-premise-form-on-site {
      border-top: 1px solid #BABABA;
      padding-top: 40px;
    }

    .premises-summary h3 {
      margin-bottom: 10px;
    }

    .application-tab-footer {
      margin-top: 15px;
      padding-top: 30px;
      border-top: 1px solid #BABABA;
      margin-left: -90px;
      margin-right: -90px;
    }
    .application-tab-footer .button-group {
      text-align: right;
    }
    .application-tab-footer a.back,
    .application-tab-footer a.prev {
      cursor: pointer;
    }
    @media only screen and (max-width : 1200px) {
      .application-tab-footer {
        margin-left: 0;
        margin-right: 0;
      }
    }
    @media only screen and (max-width : 767px) {
      .application-tab-footer .button-group {
        text-align: center;
        margin-top: 10px;
      }
    }

    .document-info-list {
      margin-bottom: 40px;
    }

    .document-upload-gp h2 {
      margin-bottom: 40px;
    }
    .document-upload-gp .document-upload-list {
      background-color: #F8F8F8;
      border-radius: 14px;
      padding: 30px;
      margin-bottom: 20px;
    }

    .profile-info-gp,
    .new-officer-form {
      margin-top: 50px;
    }

    .new-officer-form table.control-grid {
      width: 100%;
    }

    .officer-info {
      background-color: #F8F8F8;
      border-radius: 14px;
      padding: 30px 20px;
      margin-bottom: 20px;
    }
    .officer-info h3 {
      margin-bottom: 30px;
    }
    .officer-info .profile-line {
      margin-bottom: 10px;
    }

    .laboratory-disciplines h2 {
      margin-bottom: 35px;
    }
    .laboratory-disciplines form.form-horizontal {
      margin-top: 25px;
    }
    @media only screen and (max-width : 767px) {
      .laboratory-disciplines h2 {
        display: none;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .laboratory-disciplines h2 {
        display: none;
      }
    }

    .clinical-governance-officer h2 {
      margin-bottom: 35px;
    }
    .clinical-governance-officer form.form-horizontal {
      margin-top: 25px;
    }
    @media only screen and (max-width : 767px) {
      .clinical-governance-officer h2 {
        display: none;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .clinical-governance-officer h2 {
        display: none;
      }
    }

    .discipline-allocation h2 {
      margin-bottom: 35px;
    }
    .discipline-allocation .table-gp {
      margin-top: 25px;
    }
    @media only screen and (max-width : 767px) {
      .discipline-allocation h2 {
        display: none;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .discipline-allocation h2 {
        display: none;
      }
    }

    .multiservice {
      margin-left: -90px;
      margin-right: -90px;
    }
    @media only screen and (max-width : 1200px) {
      .multiservice {
        margin-left: 0;
        margin-right: 0;
      }
    }
    @media only screen and (max-width : 767px) {
      .multiservice {
        margin-left: 0;
        margin-right: 0;
      }
    }

    .singleservice .tab-gp.side-tab .nav.nav-pills,
    .singleservice .tab-gp.side-tab .mobile-side-nav-tab {
      display: none !important;
    }
    .singleservice .tab-gp.side-tab .tab-content {
      padding-left: 0;
      padding-right: 0;
      width: 100%;
      float: none;
      border-left: 0;
    }
    .singleservice .tab-gp.side-tab .tab-content .service-title {
      display: none;
    }

    .instruction-content > * {
      margin-bottom: 40px;
    }
    .instruction-content .short-content {
      width: 65%;
    }
    @media only screen and (max-width : 767px) {
      .instruction-content > * {
        margin-bottom: 20px;
      }
      .instruction-content .short-content {
        width: 100%;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .instruction-content > * {
        margin-bottom: 20px;
      }
      .instruction-content .short-content {
        width: 65%;
      }
    }

    .license-info-gp {
      margin-top: 20px;
    }
    .license-info-gp .license-info-row .licnese-info {
      width: calc(100% - 80px);
      display: inline-block;
      vertical-align: top;
    }
    .license-info-gp .license-info-row .licnese-info p {
      position: relative;
      padding-left: 15px;
    }
    .license-info-gp .license-info-row .licnese-info p:before {
      content: "";
      width: 6px;
      height: 6px;
      background-color: #333333;
      border-radius: 3px;
      position: absolute;
      top: 9px;
      left: 0;
    }
    .license-info-gp .license-info-row .license-edit {
      margin-left: 20px;
      width: 40px;
      display: inline-block;
      text-align: right;
    }
    .license-info-gp .license-info-row .license-edit p {
      text-align: right;
    }
    .withdraw-addmore {
      background-color: #fafafa;
      border-radius: 14px;
      padding: 20px;
      margin-bottom: 10px;
      border:1px solid #d1d1d1;
    }
    .withdraw-content-box {
      background-color: #fafafa;
      border-radius: 14px;
      padding: 20px;
      margin-bottom: 10px;
      border:1px solid #d1d1d1;
    }
    .withdraw-info-gp {
      margin-top: 10px;
    }
    .withdraw-info-gp .withdraw-info-row .withdraw-info {
      width: calc(95% - 80px);
      display: inline-block;
      vertical-align: top;

    }
    .withdraw-info-gp .withdraw-info-row .withdraw-info p {
      position: relative;
      padding-left: 50px;
      font-size: 18px;
    }
    .withdraw-info-gp .withdraw-info-row .withdraw-info p:before {
      content: "";
      font: normal normal normal 14px/1 FontAwesome, sans-serif;      /*color: #a2d9e7;*/
      font-size: 30px;
      width: 6px;
      height: 6px;
      /* background-color: #333333;*/
      border-radius: 3px;
      position: absolute;
      top: 0px;
      left: 8px;
    }
    .withdraw-info-gp .withdraw-info-row .withdraw-delete {
      margin-left: 20px;
      width: 80px;
      display: inline-block;
      text-align: right;
    }
    .withdraw-info-gp .withdraw-info-row .withdraw-delete p {
      text-align: right;
    }
    /***********************/

    .cesform-box .tablebox {
      width: 100%;
      display: inline-block;
      vertical-align: top;
      padding: 30px;
      margin-bottom: 20px;
      background: #fafafa;
      border-radius: 18px;

    }
    .cesform-box .tablebox .topheader {
      border-bottom: 1px solid #d1d1d1;
      margin-bottom: 30px;
    }
    .cesform-box .tablebox h4 {
      position: relative;
      padding-left: 0px;
      font-size: 22px;
      font-weight: 600;
    }

    .cesform-box .tablebox p {
      position: relative;
      padding-left: 0px;
      font-size: 18px;
    }

    .cesform-box {
      border-radius: 18px;
      border:2px dotted #d1d1d1;
      padding: 20px;
      margin-bottom: 10px;
    }
    .cesform-box:last-child  {
      margin-bottom: 0px;
    }
    .license-info-box {
      margin-top: 10px;
    }
    .cesform-box .license-info {
      width: 100%;
      display: inline-block;
      vertical-align: top;
      padding: 20px;
      margin-bottom: 20px;
      background: #fafafa;
      border-radius: 18px;

    }
    .cesform-box .license-info h4 {
      position: relative;
      padding-left: 50px;
      font-size: 22px;
    }
    .cesform-box .license-info p {
      position: relative;
      padding-left: 50px;
      font-size: 18px;
      color: #a1a1a1;
    }
    .cesform-box .license-info p.lic-no:before {
      content: "";
      font: normal normal normal 14px/1 FontAwesome, sans-serif;      color: #a2d9e7;
      font-size: 30px;
      width: 6px;
      height: 6px;
      /* background-color: #333333;*/
      border-radius: 3px;
      position: absolute;
      top: 0px;
      left: 8px;
    }
    .cesform-box .license-info p.serv-name:before {
      content: "";
      font: normal normal normal 14px/1 FontAwesome, sans-serif;      color: #a2d9e7;
      font-size: 30px;
      width: 6px;
      height: 6px;
      /* background-color: #333333;*/
      border-radius: 3px;
      position: absolute;
      top: 0px;
      left: 8px;
    }
    .withdraw-info-gp .withdraw-info-row .withdraw-delete {
      margin-left: 20px;
      width: 80px;
      display: inline-block;
      text-align: right;
    }
    .withdraw-info-gp .withdraw-info-row .withdraw-delete p {
      text-align: right;
    }



    /**********************/
    .fa.fa-trash-o {
      font-size: 1.6rem;
      margin-right: 5px;
    }
    .fa.fa-plus-circle {
      font-size: 2.6rem;
      margin-right: 5px;
    }
    .prelogin {
      padding-top: 80px;
      padding-bottom: 45px;
      background-repeat: no-repeat;
      background-size: cover;
      background-position: 100% 0;
    }
    .prelogin .prelogin-title {
      margin-bottom: 60px;
    }
    .prelogin .prelogin-content .login-IAIS {
      width: calc((100% - 420px) * 0.9);
      display: inline-block;
      margin-right: 30px;
      vertical-align: top;
    }
    .prelogin .prelogin-content .login-IAIS .left-content {
      width: calc(100% - 290px);
      display: inline-block;
      margin-right: 20px;
      vertical-align: middle;
    }
    .prelogin .prelogin-content .login-IAIS .right-content {
      width: 265px;
      display: inline-block;
      vertical-align: middle;
    }
    .prelogin .prelogin-content .login-IAIS .right-content.login-btns a.btn-primary {
      margin-bottom: 25px;
      text-transform: unset;
    }
    .prelogin .prelogin-content .hcsa {
      width: 380px;
      display: inline-block;
      vertical-align: top;
    }
    .prelogin .white-content-box h3 {
      margin-bottom: 20px;
    }
    @media only screen and (min-width : 768px) {
      .prelogin {
        min-height: calc(100vh - 176px);
      }
    }
    @media only screen and (max-width : 1200px) {
      .prelogin {
        background-size: auto 100%;
        background-position: 50% 0;
      }
      .prelogin .prelogin-content .login-IAIS .left-content {
        width: 100%;
        display: block;
        margin-right: 0;
        margin-bottom: 30px;
      }
      .prelogin .prelogin-content .login-IAIS .right-content {
        width: 100%;
        display: block;
        text-align: center;
      }
      .prelogin .prelogin-content .login-IAIS .right-content.login-btns a.btn-primary {
        margin-bottom: 15px;
      }
    }
    @media only screen and (max-width : 767px) {
      .prelogin {
        padding-top: 50px;
        padding-bottom: 30px;
        background-position: 0 0;
      }
      .prelogin .prelogin-title {
        margin-bottom: 40px;
      }
      .prelogin .prelogin-title h1 {
        text-align: center;
      }
      .prelogin .prelogin-content .login-IAIS {
        width: 100%;
        display: block;
        margin-right: 0;
        margin-bottom: 35px;
      }
      .prelogin .prelogin-content .login-IAIS .left-content {
        width: 100%;
        display: block;
        margin-right: 0;
        margin-bottom: 30px;
      }
      .prelogin .prelogin-content .login-IAIS .right-content {
        width: 100%;
        display: block;
        text-align: center;
      }
      .prelogin .prelogin-content .login-IAIS .right-content.login-btns a.btn-primary {
        margin-bottom: 15px;
      }
      .prelogin .prelogin-content .hcsa {
        width: 100%;
        display: block;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .prelogin {
        background-position: 0 0;
      }
      .prelogin .prelogin-content .login-IAIS {
        width: 100%;
        display: block;
        margin-right: 0;
        margin-bottom: 35px;
      }
      .prelogin .prelogin-content .login-IAIS .left-content {
        width: calc(100% - 290px);
        display: inline-block;
        margin-right: 20px;
        vertical-align: middle;
      }
      .prelogin .prelogin-content .login-IAIS .right-content {
        width: 265px;
        display: inline-block;
        vertical-align: middle;
      }
      .prelogin .prelogin-content .login-IAIS .right-content.login-btns a.btn-primary {
        margin-bottom: 25px;
        text-transform: unset;
      }
      .prelogin .prelogin-content .hcsa {
        width: 100%;
        display: block;
      }
    }

    .mCSB_scrollTools .mCSB_dragger .mCSB_dragger_bar {
      background-color: #454545 !important;
      width: 2px;
      border-radius: 0;
    }
    .mCSB_scrollTools .mCSB_draggerRail {
      background-color: rgba(175, 175, 175, 0.7) !important;
    }

    .navigation .mCSB_scrollTools .mCSB_dragger .mCSB_dragger_bar {
      background-color: white !important;
      width: 5px;
      border-radius: 0;
    }
    .navigation .mCSB_scrollTools .mCSB_draggerRail {
      background-color: rgba(175, 175, 175, 0.3) !important;
    }

    .amend-preview-info {
      margin-bottom: 20px;
      position: relative;
    }
    .amend-preview-info p {
      margin-bottom: 2px;
      position: relative;
    }
    .amend-preview-info p.preview-title,
    .amend-preview-info p .preview-title {
      font-weight: 600;
    }
    .amend-preview-info.edited {
      margin-bottom: 40px;
      margin-top: 25px;
    }
    .amend-preview-info.edited:before {
      content: "";
      background-color: #E6F1D9;
      position: absolute;
      left: -17px;
      right: 0;
      top: -17px;
      bottom: -17px;
      border-radius: 14px;
    }
    .amend-preview-info .form-check-gp {
      margin-top: 10px;
    }
    .amend-preview-info .form-check-gp .form-check.active .form-check-label .check-square {
      border: 1px solid #6c6c6c;
    }
    .amend-preview-info .form-check-gp .form-check.active .form-check-label .check-square:before {
      color: #6c6c6c;
    }

    .amended-service-info-gp {
      margin-bottom: 60px;
    }
    .amended-service-info-gp h2 {
      margin-bottom: 35px;
    }
    .amended-service-info-gp h2 + .amend-preview-info.edited {
      margin-top: 50px;
    }
    .amended-service-info-gp .table-gp table.table > tbody > tr .preview-title {
      font-weight: 600;
    }
    .amended-service-info-gp .table-gp table.table > tbody > tr td[rowspan] {
      max-width: 240px;
      padding-right: 50px;
    }
    .amended-service-info-gp .table-gp table.table > tbody > tr.edited td:not([rowspan]) {
      position: relative;
    }
    .amended-service-info-gp .table-gp table.table > tbody > tr.edited td:not([rowspan]) p {
      position: relative;
    }
    .amended-service-info-gp .table-gp table.table > tbody > tr.edited td:not([rowspan]):before {
      content: "";
      background-color: #E6F1D9;
      position: absolute;
      left: -17px;
      right: 0;
      top: 5px;
      bottom: 5px;
    }
    .amended-service-info-gp .table-gp table.table > tbody > tr.edited td:not([rowspan]):first-child:before {
      border-top-left-radius: 14px;
      border-bottom-left-radius: 14px;
    }
    .amended-service-info-gp .table-gp table.table > tbody > tr.edited td:not([rowspan]):last-child:before {
      border-top-right-radius: 14px;
      border-bottom-right-radius: 14px;
    }
    .amended-service-info-gp .table-gp table.table > tbody > tr.edited td[rowspan]:before {
      border: 0;
    }
    .amended-service-info-gp .table-gp table.table > tbody > tr.edited td[rowspan] + td:before {
      border-top-left-radius: 14px;
      border-bottom-left-radius: 14px;
    }
    .amended-service-info-gp .table-gp table.table > tbody > tr:not(:first-child) td[rowspan] {
      border-top: 1px solid #bababa;
    }
    .amended-service-info-gp .table-gp table.table > tbody > tr:not(:first-child) td[rowspan] + td,
    .amended-service-info-gp .table-gp table.table > tbody > tr:not(:first-child) td[rowspan] + td + td {
      border-top: 1px solid #bababa;
    }
    @media only screen and (max-width : 767px) {
      .amended-service-info-gp .table-gp table.table > tbody > tr:not(:first-child) td[rowspan] {
        border-top: 0;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr:not(:first-child) td[rowspan] + td,
      .amended-service-info-gp .table-gp table.table > tbody > tr:not(:first-child) td[rowspan] + td + td {
        border-top: 0;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .amended-service-info-gp .table-gp table.table > tbody > tr:not(:first-child) td[rowspan] {
        border-top: 0;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr:not(:first-child) td[rowspan] + td,
      .amended-service-info-gp .table-gp table.table > tbody > tr:not(:first-child) td[rowspan] + td + td {
        border-top: 0;
      }
    }
    @media only screen and (max-width : 767px) {
      .amended-service-info-gp .table-gp table.table > tbody > tr {
        position: relative;
        border-bottom: 0;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr > td {
        position: unset;
        height: 100%;
        display: inline-block;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr > td:not([rowspan]) {
        position: unset;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr > td p {
        z-index: 1;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr > td[rowspan] {
        width: 100%;
        max-width: 100%;
        padding-right: 0;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr > td[rowspan] p {
        font-size: 1.6rem;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr.edited td:not([rowspan]) {
        position: unset;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr.edited td:not([rowspan]):before {
        top: -10px;
        border-radius: 14px;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr.edited td[rowspan] + td,
      .amended-service-info-gp .table-gp table.table > tbody > tr.edited td[rowspan] + td + td {
        position: relative;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr.edited td[rowspan] + td:before,
      .amended-service-info-gp .table-gp table.table > tbody > tr.edited td[rowspan] + td + td:before {
        bottom: -10px;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr.edited td[rowspan] + td:before {
        border-top-right-radius: 0;
        border-bottom-right-radius: 0;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr.edited td[rowspan] + td + td:before {
        border-top-left-radius: 0;
        border-bottom-left-radius: 0;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr:not(:last-child) {
        margin-bottom: 15px;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr:not(:first-child) td[rowspan] {
        border-top: 1px solid #bababa;
        margin-top: 5px;
        padding-top: 15px;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .amended-service-info-gp .table-gp table.table > tbody > tr {
        border-bottom: 0;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr > td[rowspan] {
        width: 100%;
        display: block;
        max-width: 100%;
        padding-right: 0;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr > td[rowspan] p {
        font-size: 1.8rem;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr.edited td:not([rowspan]):before {
        top: -8px;
        bottom: -6px;
      }
      .amended-service-info-gp .table-gp table.table > tbody > tr:not(:first-child) td[rowspan] {
        border-top: 1px solid #bababa;
        margin-top: 5px;
        padding-top: 20px;
      }
    }
    .amended-service-info-gp.main-block {
      position: relative;
      padding-top: 30px;
    }
    .amended-service-info-gp.main-block:before {
      content: "";
      position: absolute;
      left: -95px;
      right: -70px;
      height: 1px;
      background-color: #bababa;
      top: 0;
    }

    .document-list p {
      margin-bottom: 20px;
    }

    .amendment-preview-checkbox-gp {
      margin-top: 25px;
      margin-bottom: 40px;
    }

    .personnel-list-gp h2 {
      margin-bottom: 50px;
    }
    .personnel-list-gp .personnel-list {
      margin-bottom: 50px;
    }
    .personnel-list-gp .personnel-list .table-gp .table-footnote {
      margin-bottom: 30px;
    }
    .personnel-list-gp .personnel-list:last-child {
      margin-bottom: 0;
    }
    @media only screen and (max-width : 992px) {
      .personnel-list-gp h2 {
        margin-bottom: 30px;
      }
      .personnel-list-gp .personnel-list {
        margin-bottom: 30px;
      }
      .personnel-list-gp .personnel-list .table-gp .table-footnote {
        margin-bottom: 20px;
      }
      .personnel-list-gp .personnel-list:last-child {
        margin-bottom: 0;
      }
    }
    @media only screen and (min-width : 993px) {
      .personnel-list-gp .table-gp.personnel-info-table .table > tbody > tr > td:first-child, .personnel-list-gp .table-gp.personnel-info-table .table > tbody > tr > td:nth-child(2) {
        width: 33%;
      }
    }

    .premise-list-gp .form-inline {
      margin-bottom: 15px;
    }
    .premise-list-gp .table-gp .table > tbody > tr > td:first-child p a > i.fa {
      margin-right: 5px;
    }
    .premise-list-gp .table-gp .table > tbody > tr > td:first-child p a + a {
      margin-left: 30px;
    }
    @media only screen and (max-width : 992px) {
      .premise-list-gp .table-gp .table > tbody > tr > td:first-child {
        width: 100%;
      }
    }
    @media only screen and (min-width : 993px) and (max-width : 1200px) {
      .premise-list-gp {
        min-height: calc(100vh - 540px);
      }
    }

    .personal-detail-info .form-horizontal > .personal-info-section {
      margin-bottom: 50px;
    }
    .personal-detail-info .form-horizontal > .personal-info-section > p {
      line-height: 30px;
    }
    .personal-detail-info .personal-info-section .edit-section-gp .progress-step-check {
      width: 100%;
      margin-bottom: 0px;
    }
    .personal-detail-info .personal-info-section .edit-section-gp .cancel-edit {
      margin-top: 40px;
    }
    .personal-detail-info .personal-info-section .replace-another-personnel {
      margin-top: 30px;
    }
    .personal-detail-info .personal-info-section .change-detail-personnel {
      margin-top: 30px;
    }
    .personal-detail-info .personal-info-section .change-detail-personnel.disabled .form-group label,
    .personal-detail-info .personal-info-section .change-detail-personnel.disabled .form-group input[type=text],
    .personal-detail-info .personal-info-section .change-detail-personnel.disabled .form-group input[type=email],
    .personal-detail-info .personal-info-section .change-detail-personnel.disabled .form-group input[type=number],
    .personal-detail-info .personal-info-section .change-detail-personnel.disabled .form-group .nice-select, .personal-detail-info .personal-info-section .change-detail-personnel:not(.disabled) .form-group:nth-child(2) label,
    .personal-detail-info .personal-info-section .change-detail-personnel:not(.disabled) .form-group:nth-child(2) input[type=text],
    .personal-detail-info .personal-info-section .change-detail-personnel:not(.disabled) .form-group:nth-child(2) input[type=email],
    .personal-detail-info .personal-info-section .change-detail-personnel:not(.disabled) .form-group:nth-child(2) input[type=number],
    .personal-detail-info .personal-info-section .change-detail-personnel:not(.disabled) .form-group:nth-child(2) .nice-select, .personal-detail-info .personal-info-section .change-detail-personnel:not(.disabled) .form-group:nth-child(3) label,
    .personal-detail-info .personal-info-section .change-detail-personnel:not(.disabled) .form-group:nth-child(3) input[type=text],
    .personal-detail-info .personal-info-section .change-detail-personnel:not(.disabled) .form-group:nth-child(3) input[type=email],
    .personal-detail-info .personal-info-section .change-detail-personnel:not(.disabled) .form-group:nth-child(3) input[type=number],
    .personal-detail-info .personal-info-section .change-detail-personnel:not(.disabled) .form-group:nth-child(3) .nice-select {
      opacity: 0.5;
      pointer-events: none;
    }
    .form-horizontal .form-group textarea {
      border: 1px solid #6c6c6c;
      padding: 13px 15px;
      display: inline-block;
      border-radius: 16px;
      margin-right: 10px;
      margin-top: 0;
      background: #efefef;
      /* Old browsers */
      background: -moz-linear-gradient(left, #efefef 0%, white 100%);
      /* FF3.6-15 */
      background: -webkit-linear-gradient(left, #efefef 0%, white 100%);
      /* Chrome10-25,Safari5.1-6 */
      background: linear-gradient(to right, #efefef 0%, white 100%);
    }
    .form-horizontal .form-group textarea:hover {
      border-color: #1989BF;
    }

    .canvasContent #formPanel {
      border: 0;
      background-color: transparent;
    }
    .canvasContent #formPanel.ui-tabs .ui-tabs-panel {
      padding: 0;
    }
    .canvasContent #formPanel .form-tab-panel {
      overflow: visible;
    }
    .canvasContent #formPanel .form-tab-panel .page {
      background: transparent;
      border: 0;
      margin: 0;
      padding: 0;
    }
    .canvasContent #formPanel .form-tab-panel .formgap {
      margin: 0;
      margin-top: 25px;
    }
    .canvasContent #formPanel .form-tab-panel .formgap .formtext {
      padding: 0;
    }
    .canvasContent #formPanel .form-tab-panel .formgap .formtext label {
      font-size: 16px;
      color: #333333;
      font-family: "Open Sans", sans-serif;
    }
    @media only screen and (max-width : 767px) {
      .canvasContent #formPanel .form-tab-panel .formgap .formtext label.control-label {
        margin-bottom: 15px;
      }
    }
    .canvasContent #formPanel .form-tab-panel .formgap .radioselect {
      font-size: 16px;
      padding: 0 15px;
      margin-top: 14px;
    }
    .canvasContent #formPanel .form-tab-panel .formgap .radioselect tr {
      display: inline-block;
      width: 100%;
    }
    .canvasContent #formPanel .form-tab-panel .formgap .radioselect tr .control-item-container {
      margin-right: 15px;
    }
    .canvasContent #formPanel .form-tab-panel .formgap .radioselect tr:last-child .control-item-container {
      margin-right: 0;
    }
    .canvasContent #formPanel .form-tab-panel .formgap .radioselect input {
      position: absolute;
      opacity: 0;
      height: 100%;
      left: 0;
      top: 0;
      z-index: 10;
      cursor: pointer;
      margin: 0;
    }
    .canvasContent #formPanel .form-tab-panel .formgap .radioselect input:checked + .radio-inline span.check-circle, .canvasContent #formPanel .form-tab-panel .formgap .radioselect input:active + .radio-inline span.check-circle {
      border: 1px solid #1989BF;
    }
    .canvasContent #formPanel .form-tab-panel .formgap .radioselect input:checked + .radio-inline span.check-circle:before, .canvasContent #formPanel .form-tab-panel .formgap .radioselect input:active + .radio-inline span.check-circle:before {
      background-color: #1989BF;
    }
    .canvasContent #formPanel .form-tab-panel .formgap .radioselect label {
      font-size: 16px;
      color: #333333;
      font-family: "Open Sans", sans-serif;
      line-height: 50px;
    }
    .canvasContent #formPanel .form-tab-panel .formgap .radioselect label span.check-circle {
      width: 16px;
      height: 16px;
      border: 1px solid #6c6c6c;
      display: inline-block;
      border-radius: 16px;
      margin-right: 10px;
      margin-top: 0;
      position: absolute;
      left: 0;
      top: 4px;
      background: #efefef;
      background: -moz-linear-gradient(left, #efefef 0%, white 100%);
      background: -webkit-linear-gradient(left, #efefef 0%, white 100%);
      background: linear-gradient(to right, #efefef 0%, white 100%);
    }
    .canvasContent #formPanel .form-tab-panel .formgap .radioselect label span.check-circle:before {
      content: "";
      position: absolute;
      width: 8px;
      height: 8px;
      background-color: transparent;
      top: 50%;
      margin-top: -4px;
      left: 50%;
      margin-left: -4px;
      border-radius: 4px;
    }

    div.control label.control-font-label {
      color: #333333;
      font-size: 1.6rem;
      font-weight: 300;
      float: none;
      line-height: 50px;
    }
    div.control label.control-font-label + span.mandatory {
      line-height: 0;
      display: inline;
      float: none;
    }
    @media only screen and (max-width : 767px) {
      div.control label.control-font-label {
        font-size: 1.4rem;
        line-height: 26px;
        margin-bottom: 5px;
      }
    }

    .form-horizontal .control-label {
      text-align: left;
    }
    @media only screen and (max-width : 767px) {
      .form-horizontal .control-label {
        margin-bottom: 5px;
      }
    }

    .section > table {
      width: 100%;
      margin: 0;
    }
    .section > table > .section.control {
      margin: 0;
      border: 0;
    }

    .section table.control-grid > tbody > tr:nth-child(odd),
    .section table.control-grid > tbody > tr:nth-child(even) {
      background-color: white;
    }

    .section table.control-grid > tbody > tr > td .control.control-caption-horizontal {
      overflow: visible;
    }
    .section table.control-grid > tbody > tr > td .control.control-caption-horizontal .formgap {
      margin: 0 -15px;
      overflow: visible;
    }

    table.control-grid.columns1 > tbody > tr > td > .section.control {
      border: 0;
      margin: 0;
    }
    table.control-grid.columns1 > tbody > tr > td > .section.control > .section-header {
      background: transparent;
      padding: 0;
      margin-bottom: 25px;
    }
    table.control-grid.columns1 > tbody > tr > td > .section.control > .section-header > label {
      font-weight: 600;
      font-family: "Open Sans", sans-serif;
      font-size: 22px;
      color: #333;
      border-bottom: 1px solid #D1D1D1;
      margin-bottom: 15px;
      padding-bottom: 20px;
      display: block;
    }
    table.control-grid.columns1 > tbody > tr > td > .section.control input[type=text],
    table.control-grid.columns1 > tbody > tr > td > .section.control input[type=email],
    table.control-grid.columns1 > tbody > tr > td > .section.control input[type=number],
    table.control-grid.columns1 > tbody > tr > td > .section.control .nice-select {
      margin-bottom: 30px;
    }
    table.control-grid.columns1 > tbody > tr > td > .section.control input.hasDatepicker {
      width: 80%;
    }
    table.control-grid.columns1 > tbody > tr > td > .section.control .add-officer {
      min-height: 0;
      transition: all 0s ease;
    }
    table.control-grid.columns1 > tbody > tr > td > .section.control > table.control-grid tbody tr td .section-header label {
      font-size: 20px;
      color: #333;
      font-family: "Open Sans", sans-serif;
      border-bottom: 0;
      margin-top: 35px;
      font-weight: 600;
    }
    @media only screen and (max-width : 767px) {
      table.control-grid.columns1 > tbody > tr > td > .section.control {
        min-height: 500px;
      }
      table.control-grid.columns1 > tbody > tr > td > .section.control input[type=text],
      table.control-grid.columns1 > tbody > tr > td > .section.control input[type=email],
      table.control-grid.columns1 > tbody > tr > td > .section.control input[type=number],
      table.control-grid.columns1 > tbody > tr > td > .section.control .nice-select {
        margin-bottom: 10px;
      }
      table.control-grid.columns1 > tbody > tr > td > .section.control .add-officer.shown {
        min-height: 480px;
      }
    }
    table.control-grid.columns1 > tbody > tr > td table.control-grid {
      margin-bottom: 25px;
      margin-top: 25px;
    }
    table.control-grid.columns1 > tbody > tr > td table.control-grid > tbody > tr > td .repeatable-section-header {
      background: transparent;
      padding: 0;
      margin-bottom: 25px;
    }
    table.control-grid.columns1 > tbody > tr > td table.control-grid > tbody > tr > td .repeatable-section-header label {
      font-weight: 600;
      font-family: "Open Sans", sans-serif;
      font-size: 20px;
      color: #333;
      display: block;
      margin-bottom: 10px;
      background: transparent;
    }

    .repeatable-section-item:nth-child(2n) {
      background: transparent;
      border: 0;
    }

    #wizard-page-title {
      font-family: "Open Sans", sans-serif;
      font-size: 1.6rem;
      margin-bottom: 15px;
      padding-bottom: 0;
      font-weight: 600;
      padding-left: 0;
    }

    .officer-items h2 {
      font-size: 2.2rem;
      font-family: "Open Sans", sans-serif;
      border-bottom: 1px solid #D1D1D1;
      margin-top: 0;
      margin-bottom: 15px;
      padding-bottom: 20px;
      font-weight: 600;
      padding-left: 0;
      padding-right: 0;
    }
    .officer-items h2 p {
      font-size: 18px !important;
      margin-top: 14px;
    }

    table .profile-info-gp,
    table .new-officer-form {
      margin-top: 100px;
    }
    table .new-officer-form {
      margin-bottom: 20px;
    }
    table select.form-control.control-input {
      margin-bottom: 15px;
    }

    .ui-widget-content a {
      color: #147aab;
    }
    .ui-widget-content a:hover, .ui-widget-content a:active {
      text-decoration: none;
      color: #333;
    }
    .ui-widget-content a:focus {
      color: #333;
    }

    .control-item-container {
      position: relative;
      margin-bottom: 15px;
    }
    .control-item-container input.control-input[type=checkbox],
    .control-item-container input.control-input[type=radio] {
      height: auto;
      position: absolute;
      opacity: 0;
      width: 100%;
      height: 100%;
      left: 0;
      top: 0;
      z-index: 10;
      cursor: pointer;
      margin: 0;
    }
    .control-item-container input.control-input[type=checkbox] + label.control-label,
    .control-item-container input.control-input[type=radio] + label.control-label {
      color: #333333;
      font-size: 1.6rem;
      font-weight: 400;
      position: relative;
      padding-left: 30px;
      min-width: 16px;
      min-height: 22px;
      line-height: 22px !important;
      font-family: "Open Sans", sans-serif;
    }
    @media only screen and (max-width : 767px) {
      .control-item-container input.control-input[type=checkbox] + label.control-label,
      .control-item-container input.control-input[type=radio] + label.control-label {
        font-size: 14px;
      }
    }
    @media only screen and (min-width : 768px) and (max-width : 992px) {
      .control-item-container input.control-input[type=checkbox] + label.control-label,
      .control-item-container input.control-input[type=radio] + label.control-label {
        font-size: 16px;
      }
    }
    .control-item-container input.control-input[type=checkbox] + label.control-label a,
    .control-item-container input.control-input[type=radio] + label.control-label a {
      text-decoration: underline;
    }
    .control-item-container input.control-input[type=checkbox] + label.control-label a:hover, .control-item-container input.control-input[type=checkbox] + label.control-label a:focus, .control-item-container input.control-input[type=checkbox] + label.control-label a:active,
    .control-item-container input.control-input[type=radio] + label.control-label a:hover,
    .control-item-container input.control-input[type=radio] + label.control-label a:focus,
    .control-item-container input.control-input[type=radio] + label.control-label a:active {
      text-decoration: none;
      color: #333;
    }
    .control-item-container input.control-input[type=checkbox] + label.control-label .check-circle,
    .control-item-container input.control-input[type=radio] + label.control-label .check-circle {
      width: 16px;
      height: 16px;
      border: 1px solid #6c6c6c;
      display: inline-block;
      border-radius: 16px;
      margin-right: 10px;
      margin-top: 0;
      position: absolute;
      left: 0;
      top: 4px;
      background: #efefef;
      /* Old browsers */
      background: -moz-linear-gradient(left, #efefef 0%, white 100%);
      /* FF3.6-15 */
      background: -webkit-linear-gradient(left, #efefef 0%, white 100%);
      /* Chrome10-25,Safari5.1-6 */
      background: linear-gradient(to right, #efefef 0%, white 100%);
    }
    .control-item-container input.control-input[type=checkbox] + label.control-label .check-circle:before,
    .control-item-container input.control-input[type=radio] + label.control-label .check-circle:before {
      content: "";
      position: absolute;
      width: 8px;
      height: 8px;
      background-color: transparent;
      top: 50%;
      margin-top: -4px;
      left: 50%;
      margin-left: -4px;
      border-radius: 4px;
    }
    .control-item-container input.control-input[type=checkbox] + label.control-label .check-square,
    .control-item-container input.control-input[type=radio] + label.control-label .check-square {
      width: 18px;
      height: 18px;
      border: 1px solid #6c6c6c;
      display: inline-block;
      border-radius: 4px;
      margin-right: 10px;
      margin-top: 0;
      position: absolute;
      left: 0;
      top: 3px;
      background: white;
    }
    .control-item-container input.control-input[type=checkbox] + label.control-label .check-square:before,
    .control-item-container input.control-input[type=radio] + label.control-label .check-square:before {
      content: "";
      font-family: FontAwesome, sans-serif;      position: absolute;
      width: 18px;
      height: 18px;
      top: 0;
      left: 0;
      vertical-align: middle;
      text-align: center;
      line-height: 18px;
      font-size: 12px;
      color: white;
      opacity: 0;
    }
    @media only screen and (max-width : 767px) {
      .control-item-container input.control-input[type=checkbox] + label.control-label .check-square,
      .control-item-container input.control-input[type=radio] + label.control-label .check-square {
        width: 16px;
        height: 16px;
      }
      .control-item-container input.control-input[type=checkbox] + label.control-label .check-square::before,
      .control-item-container input.control-input[type=radio] + label.control-label .check-square::before {
        width: 14px;
        height: 14px;
        line-height: 14px;
      }
    }
    .control-item-container input.control-input[type=checkbox]:checked + label.control-label, .control-item-container input.control-input[type=checkbox]:active + label.control-label,
    .control-item-container input.control-input[type=radio]:checked + label.control-label,
    .control-item-container input.control-input[type=radio]:active + label.control-label {
      border: 0;
    }
    .control-item-container input.control-input[type=checkbox]:checked + label.control-label span.check-circle,
    .control-item-container input.control-input[type=checkbox]:checked + label.control-label span.check-square, .control-item-container input.control-input[type=checkbox]:active + label.control-label span.check-circle,
    .control-item-container input.control-input[type=checkbox]:active + label.control-label span.check-square,
    .control-item-container input.control-input[type=radio]:checked + label.control-label span.check-circle,
    .control-item-container input.control-input[type=radio]:checked + label.control-label span.check-square,
    .control-item-container input.control-input[type=radio]:active + label.control-label span.check-circle,
    .control-item-container input.control-input[type=radio]:active + label.control-label span.check-square {
      border: 1px solid #1989BF;
    }
    .control-item-container input.control-input[type=checkbox]:checked + label.control-label span.check-circle:before,
    .control-item-container input.control-input[type=checkbox]:checked + label.control-label span.check-square:before, .control-item-container input.control-input[type=checkbox]:active + label.control-label span.check-circle:before,
    .control-item-container input.control-input[type=checkbox]:active + label.control-label span.check-square:before,
    .control-item-container input.control-input[type=radio]:checked + label.control-label span.check-circle:before,
    .control-item-container input.control-input[type=radio]:checked + label.control-label span.check-square:before,
    .control-item-container input.control-input[type=radio]:active + label.control-label span.check-circle:before,
    .control-item-container input.control-input[type=radio]:active + label.control-label span.check-square:before {
      color: #1989BF;
      opacity: 1;
    }
    .control-item-container input.control-input[type=checkbox]:hover + label.control-label,
    .control-item-container input.control-input[type=radio]:hover + label.control-label {
      border: 0;
    }
    .control-item-container input.control-input[type=checkbox]:hover + label.control-label span.check-circle,
    .control-item-container input.control-input[type=checkbox]:hover + label.control-label span.check-square,
    .control-item-container input.control-input[type=radio]:hover + label.control-label span.check-circle,
    .control-item-container input.control-input[type=radio]:hover + label.control-label span.check-square {
      border: 1px solid #1989BF;
    }
    .control-item-container input.control-input[type=checkbox]:hover + label.control-label a,
    .control-item-container input.control-input[type=radio]:hover + label.control-label a {
      text-decoration: none;
      color: #333;
    }
    .control-item-container input.control-input[type=checkbox]:disabled,
    .control-item-container input.control-input[type=radio]:disabled {
      cursor: not-allowed;
    }
    .control-item-container.disabled {
      opacity: 0.5;
    }
    .control-item-container.disabled input.control-input[type=checkbox],
    .control-item-container.disabled input.control-input[type=radio],
    .control-item-container.disabled label.control-label {
      pointer-events: none;
    }
    .control-item-container.sub-form-check {
      padding-left: 30px;
    }
    .control-item-container.sub-form-check.double {
      padding-left: 60px;
    }

    table.control-grid > tbody > tr .control-caption-horizontal .control-set-alignment.control-input-span {
      width: 100%;
    }
    table.control-grid > tbody > tr .control-caption-horizontal .control-set-alignment.control-input-span > .normal-indicator {
      width: 100%;
      padding-left: 0;
    }
    table.control-grid > tbody > tr .repeatable-section-control-button {
      text-align: left;
    }
    table.control-grid > tbody > tr .repeatable-section-control-button button {
      background: transparent;
      color: #147aab;
      font-size: 16px;
      font-family: "Open Sans", sans-serif;
      font-weight: 300;
      outline: 0;
      border: 0;
      text-decoration: underline;
      padding: 0;
      margin-bottom: 35px;
    }
    table.control-grid > tbody > tr .repeatable-section-control-button button:focus, table.control-grid > tbody > tr .repeatable-section-control-button button:hover {
      outline: 0;
    }
    table.control-grid > tbody > tr .repeatable-section-control-button button:hover {
      text-decoration: none;
    }
    table.control-grid > tbody > tr .repeatable-section-control-button button:active {
      box-shadow: none;
    }

    .textmandatory {
      font-size: 11px;
      font-family: "Open Sans", sans-serif;
    }

    .section.control {
      background: transparent;
      border: 0;
      padding: 0;
      margin: 0;
    }
    .section.control .section-header {
      background: transparent;
      padding: 0;
    }
    .section.control .section-header > .summary-header {
      margin-bottom: 30px;
    }
    .section.control > .pop-up .pop-up-body {
      padding: 0;
    }
    .section.control > .pop-up .pop-up-body > .fileUploadContainer:nth-child(odd) {
      background: transparent;
      overflow-y: hidden;
    }
    .section.control > .pop-up .pop-up-body > .fileUploadContainer:nth-child(even) {
      background: transparent;
      overflow-y: hidden;
    }
    .section.control > .pop-up .pop-up-body > .fileUploadContainer {
      margin-bottom: 25px;
    }
    .section.control > .pop-up .pop-up-body > .fileUploadContainer > .formtext {
      color: #333333;
      font-size: 16px;
      width: 100%;
      text-align: left;
      padding: 0;
    }
    .section.control > .pop-up .pop-up-body > .fileUploadContainer > .formtext > label {
      font-weight: bold;
      margin-bottom: 10px;
    }
    .section.control > .pop-up .pop-up-body > .fileUploadContainer .control {
      margin: 0;
      padding: 0;
    }
    .section.control > .pop-up .pop-up-body > .fileUploadContainer span {
      margin: 0;
      padding: 0;
    }
    .section.control > .pop-up .pop-up-body > .fileUploadContainer span a {
      text-decoration: underline;
    }
    .section.control > .pop-up .pop-up-body > .fileUploadContainer span a:hover {
      text-decoration: none;
    }

    .documentContent {
      overflow: hidden;
    }
    .documentContent .section.control table > tbody > tr > td {
      margin-bottom: 25px;
    }
    .documentContent .section.control table > tbody > tr.incomplete .control .control-label > label:after {
      content: "";
      font-family: FontAwesome, sans-serif;      width: 22px;
      height: 22px;
      border: 0;
      background-color: #b01b17;
      color: #fff;
      font-size: 14px;
      display: inline-block;
      position: relative;
      text-align: center;
      border-radius: 50%;
      line-height: 22px;
      font-weight: 400;
      vertical-align: bottom;
      margin-left: 10px;
    }
    .documentContent .section.control table > tbody > tr .col-sm-4, .documentContent .section.control table > tbody > tr .col-sm-5 {
      width: 100%;
    }
    .documentContent .section.control table > tbody > tr label {
      font-size: 16px;
      font-weight: 700;
      color: #4d4d4d;
      padding: 0;
      margin: 0;
      margin-bottom: 10px;
    }
    .documentContent .section.control table > tbody > tr label.form-check-label {
      padding-left: 30px;
    }
    .documentContent .section.control table > tbody > tr .control-input {
      color: #4d4d4d;
      font-size: 16px;
      font-weight: normal;
    }
    .documentContent .section.control table > tbody > tr .normal-indicator {
      margin-bottom: 25px;
    }
    .documentContent .section.control table > tbody > tr .normal-indicator label {
      margin-bottom: 0;
      font-family: "Open Sans", sans-serif;
      color: #4d4d4d;
    }
    .documentContent .section > fieldset {
      margin: 0;
      padding: 0;
      border: 0;
    }

    .mb-0 {
      margin-bottom: 0 !important;
    }

    /*# sourceMappingURL=cpl_style.css.map */

    /*:focus {*/
    /*  outline: 2px solid #cc0000;*/
    /*}*/

    .logo-ssg {
      padding-left: 40px;
    }
    section.logo {
      background: #F0F0F0;
    }
    section.logo .logo-ssg span {
      font-size: 10px;
      font-family: Arial,Helvetica,sans-serif;
    }
    section.logo img {
      height: 27px;
    }

    /********************************/

    .dash-announce {
      background: #1a5692;
      border: 7px solid #134c85;
      color: #fff;
      padding: 25px;
    }
    .dashalert {
      border-radius: 14px;
      /*    padding-top: 10px;
      padding-bottom: 10px;*/
      border: 0;
      margin-bottom: 20px;
    }

    .dash-announce h3 {
      padding-bottom: 15px;
      border-bottom: 1px solid #1989bf;
    }

    /*# sourceMappingURL=cpl_style.css.map */

    .dialogbox-content {
      height:70vh;
      overflow-y: auto;
    }

    .search-wrap .input-group input[type="text"], .search-wrap .input-group input[type="email"], .search-wrap .input-group input[type="number"]

    {
      padding: 14px 55px 14px 15px;
    }



    /************** DSS Fixes 2 Feb/2021 *************/

    .profile-dropdown-mobile {
      width: 142%;
      margin-left: -43%;
    }


    /************** DSS mobile fix**********/

    @media only screen and (min-width: 320px) and (max-width: 479px) {

      .profile-dropdown-mobile {
        width: auto;
        margin-left: 0;
      }
      .center-content {
        padding: 30px 15px;
      }
      table.control-grid.columns1 > tbody > tr > td > .section.control {
        min-height: auto;
      }
      .navigation {
        position: fixed;
        top: 93px;
        left: 0;
        width: 100vw;
        z-index: 99;
        background: #025B87;
        height: calc(100vh - 62px);
        padding-bottom: 85px;
        padding-top: 15px;
        -webkit-transform: translateX(100vw);
        -moz-transform: translateX(100vw);
        -ms-transform: translateX(100vw);
        -o-transform: translateX(100vw);
        transform: translateX(100vw);
        -webkit-transition: all 0.3s ease;
        -moz-transition: all 0.3s ease;
        -ms-transition: all 0.3s ease;
        -o-transition: all 0.3s ease;
        transition: all 0.3s ease;
      }
      .table-gp table.table > tbody > tr > td p {
        margin-bottom: 0;
        line-height: 25px;
      }
    }


    @media only screen and (min-width : 768px) and (max-width : 992px) {
      header {
        padding-top: 12px;
        padding-bottom: 12px;
        background: #fff;
      }
      .navigation.open {
        -webkit-transform: translateX(0vw);
        -moz-transform: translateX(0vw);
        -ms-transform: translateX(0vw);
        -o-transform: translateX(0vw);
        transform: translateX(0vw);
      }
      .navigation {
        width: 100vw;
        top: 110px;
        height: calc(100vh - 65px);
      }
      footer.footerlogin {
        position: absolute;
        top: auto;
        bottom: 0;
        padding: 5px 0 5px;
        text-align: center;
      }
      footer.footerlogin .footer-link {
        width: 90%;
        margin-left: auto;
        margin-right: auto;
        text-align: center;
        margin-bottom: 7px;
      }
      footer.footerlogin .footer-link ul li {
        padding-left: 7px;
        padding-right: 7px;
      }
      footer.footerlogin .footer-link ul li a {
        font-size: 12px;
        position: relative;
      }
      footer.footerlogin .footer-link ul li a:before {
        content: none;
      }
      footer.footerlogin .footer-link ul li:not(:first-child) a:before {
        content: "";
        width: 1px;
        height: 9px;
        background-color: #DFDFDF;
        position: absolute;
        top: 50%;
        left: -8px;
        margin-top: -4.5px;
      }
      footer.footerlogin .copyright p {
        text-align: center !important;
      }
    }
  </style>
</head>
</html>