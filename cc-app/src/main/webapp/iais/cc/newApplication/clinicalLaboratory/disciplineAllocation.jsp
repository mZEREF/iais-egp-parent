<%--
  Created by IntelliJ IDEA.
  User: tai13
  Date: 10/11/2019
  Time: 5:29 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<header>
    <div class="container">
        <div class="row">
            <div class="col-xs-10 col-lg-6">
                <div class="logo-img"><a href="#"><img src="img/moh-logo.svg" alt="Ministry of Health" width="235" height="64">
                    <p class="logo-txt">Integrated Application and Inspection System</p></a></div>
            </div>
            <div class="col-xs-2 col-lg-6">
                <ul class="list-inline hidden-xs hidden-sm">
                    <li class="site-fontsizer-cont"><a class="decrease-font fontsizer">A-</a></li>
                    <li class="site-fontsizer-cont"><a class="decrease-font fontsizer">A+</a></li>
                </ul>
                <div class="sg-gov-logo hidden-xs hidden-sm"><a href="https://www.gov.sg/"> <img src="img/singapore-gov-logo.svg" alt="Singapore Government" width="270" height="42"></a></div>
            </div>
        </div>
    </div>
</header>
<div class="dashboard" style="background-image:url('img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <div class="col-xs-10 col-xs-offset-1 col-lg-offset-9 col-lg-3">
                    <div class="dropdown profile-dropdown"><a class="profile-btn btn" id="profileBtn" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" href="javascript:;">Tan Mei Ling Joyce</a>
                        <ul class="dropdown-menu" aria-labelledby="profileBtn">
                            <li class="dashboard-icon"><a href="#">Dashboard</a></li>
                            <li class="management-account"><a href="#">Manage Account</a></li>
                            <li class="logout"><a href="#">Logout</a></li>
                        </ul>
                    </div>
                </div>
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1>New Licence Application</h1>
                        <h3>You are applying for <b>Clinical Laboratory</b> | <b>Blood Banking</b></h3>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp steps-tab">
                    <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                        <li role="presentation"><a href="application-premises.html">Premises</a></li>
                        <li class="complete" role="presentation"><a href="application-document.html">Primary <br> Documents</a></li>
                        <li class="incomplete active" role="presentation"><a href="application-service-related-clinical-lab-lab-discipline.html">Service-Related <br> Information</a></li>
                        <li role="presentation"><a href="application-preview.html">Preview & Submit</a></li>
                        <li class="disabled" role="presentation"><a href="#">Payment</a></li>
                    </ul>
                    <div class="tab-nav-mobile visible-xs visible-sm">
                        <div class="swiper-wrapper" role="tablist">
                            <div class="swiper-slide"><a href="application-premises.html">Premises</a></div>
                            <div class="swiper-slide"><a href="application-document.html">Primary Documents</a></div>
                            <div class="swiper-slide"><a href="application-service-related-clinical-lab-lab-discipline.html">Service-Related Information</a></div>
                            <div class="swiper-slide"><a href="application-preview.html">Preview & Submit</a></div>
                            <div class="swiper-slide"><a href="#">Payment</a></div>
                        </div>
                        <div class="swiper-button-prev"></div>
                        <div class="swiper-button-next"></div>
                    </div>
                    <div class="tab-content">
                        <div class="tab-pane active" id="serviceInformationTab" role="tabpanel">
                            <div class="multiservice">
                                <div class="tab-gp side-tab clearfix">
                                    <ul class="nav nav-pills nav-stacked hidden-xs hidden-sm" role="tablist">
                                        <li class="active" role="presentation"><a href="application-service-related-clinical-lab-lab-discipline.html">Clinical Laboratory</a></li>
                                        <li class="complete" role="presentation"><a href="application-service-related-blood-banking-lab-discipline.html">Blood Banking</a></li>
                                    </ul>
                                    <div class="mobile-side-nav-tab visible-xs visible-sm">
                                        <select id="serviceSelect" aria-label="serviceSelectMobile">
                                            <option value="clinical-lab">Clinical Laboratory</option>
                                            <option value="blood-banking">Blood Banking</option>
                                        </select>
                                    </div>
                                    <div class="tab-content">
                                        <div class="tab-pane active" id="clinicalLab" role="tabpanel">
                                            <h2 class="service-title">SERVICE 1 OF 2: <b>CLINICAL LABORATORY</b></h2>
                                            <div class="visible-xs visible-sm servive-subtitle">
                                                <p>Step 3 of 5</p>
                                                <h3>Discipline Allocation</h3>
                                            </div>
                                            <ul class="progress-tracker">
                                                <li class="tracker-item completed" data-service-step="laboratory-disciplines">Laboratory Disciplines</li>
                                                <li class="tracker-item completed" data-service-step="clinical-governance-officer">Clinical Governance Officers</li>
                                                <li class="tracker-item active" data-service-step="discipline-allocation">Discipline Allocation</li>
                                                <li class="tracker-item disabled" data-service-step="principal-officers">Principal Officers</li>
                                                <li class="tracker-item disabled">Documents</li>
                                            </ul>
                                            <div class="application-service-steps">
                                                <div class="discipline-allocation">
                                                    <h2>Discipline Allocation</h2>
                                                    <p>Please ensure that each laboratory discipline is assigned to a clinical governance officer.</p>
                                                    <div class="table-gp">
                                                        <table class="table discipline-table">
                                                            <thead>
                                                            <tr>
                                                                <th>Premises</th>
                                                                <th>Laboratory Disciplines</th>
                                                                <th>Clinical Governance Officers</th>
                                                            </tr>
                                                            </thead>
                                                            <tbody>
                                                            <tr>
                                                                <td rowspan="4">
                                                                    <p class="visible-xs visible-sm table-row-title">Premises</p>
                                                                    <p>16 Raffles Quay # 01-03, 048581</p>
                                                                </td>
                                                                <td>
                                                                    <p class="visible-xs visible-sm table-row-title">Laboratory Disciplines</p>
                                                                    <p>Cytology</p>
                                                                </td>
                                                                <td>
                                                                    <p class="visible-xs visible-sm table-row-title">Clinical Governance Officers</p>
                                                                    <select class="table-select officer-allocation-select" id="officerAllocationSelect1" aria-labelledby="officerAllocationSelect1">
                                                                        <option>Option 1</option>
                                                                        <option>Option 2</option>
                                                                        <option>Option 3</option>
                                                                        <option>Option 4</option>
                                                                        <option>Option 5</option>
                                                                    </select>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <p class="visible-xs visible-sm table-row-title">Laboratory Disciplines</p>
                                                                    <p>HIV Testing</p>
                                                                </td>
                                                                <td>
                                                                    <p class="visible-xs visible-sm table-row-title">Clinical Governance Officers</p>
                                                                    <select class="table-select officer-allocation-select" id="officerAllocationSelect2" aria-labelledby="officerAllocationSelect2">
                                                                        <option>Option 1</option>
                                                                        <option>Option 2</option>
                                                                        <option>Option 3</option>
                                                                        <option>Option 4</option>
                                                                        <option>Option 5</option>
                                                                    </select>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <p class="visible-xs visible-sm table-row-title">Laboratory Disciplines</p>
                                                                    <p>Medical Microbiology</p>
                                                                </td>
                                                                <td>
                                                                    <p class="visible-xs visible-sm table-row-title">Clinical Governance Officers</p>
                                                                    <select class="table-select officer-allocation-select" id="officerAllocationSelect3" aria-labelledby="officerAllocationSelect3">
                                                                        <option>Option 1</option>
                                                                        <option>Option 2</option>
                                                                        <option>Option 3</option>
                                                                        <option>Option 4</option>
                                                                        <option>Option 5</option>
                                                                    </select>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <p class="visible-xs visible-sm table-row-title">Laboratory Disciplines</p>
                                                                    <p>Immunology</p>
                                                                </td>
                                                                <td>
                                                                    <p class="visible-xs visible-sm table-row-title">Clinical Governance Officers</p>
                                                                    <select class="table-select officer-allocation-select" id="officerAllocationSelect4" aria-labelledby="officerAllocationSelect4">
                                                                        <option>Option 1</option>
                                                                        <option>Option 2</option>
                                                                        <option>Option 3</option>
                                                                        <option>Option 4</option>
                                                                        <option>Option 5</option>
                                                                    </select>
                                                                </td>
                                                            </tr>
                                                            </tbody>
                                                        </table>
                                                        <p>Click <a href="#">here</a> to assign a laboratory discipline to multiple clinical governance officers.</p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-pane" id="bloodBanking" role="tabpanel"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="application-tab-footer">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <p><a class="back" href="application-service-related-clinical-lab-clinical-officer.html"><i class="fa fa-angle-left"></i> Back</a></p>
                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <div class="button-group"><a class="btn btn-secondary" href="#">Save as Draft</a><a class="next btn btn-primary disabled" href="#">Next</a></div>
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
<footer class="footerlogin">
    <div class="container">
        <div class="row">
            <div class="col-xs-12 col-md-7">
                <div class="footer-link">
                    <ul class="list-inline">
                        <li><a href="#">Privacy Statement</a></li>
                        <li><a href="#">Terms Of Use</a></li>
                        <li><a href="#">Rate This E-Service</a></li>
                        <li><a href="#">Sitemap</a></li>
                    </ul>
                </div>
            </div>
            <div class="col-xs-12 col-md-5">
                <div class="copyright">
                    <p class="text-right">	&copy; <span class="year">2019</span>  Government Of Singapore. Last Updated 18 Feb 2018.</p>
                </div>
            </div>
        </div>
    </div>
</footer>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/particles.js"></script>
<script src="js/app.js"></script>
<script src="js/lib/stats.js"></script>
<script src="js/scrollup.js"></script>
<script src="js/anchor.js"></script>
<script src="js/mynav.js"></script>
<script src="js/navbarscroll.js"></script>
<script src="js/dropdown.js"></script>
<script src="js/jquery.nice-select.js"></script>
<script src="js/swiper.js"></script>
<script src="js/jquery.mCustomScrollbar.js"></script>
<script src="js/cpl_app.js"></script>

</body>
</html>
