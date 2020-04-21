<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1>Declaration</h1>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <div class="main-content">
        <div class="tab-gp steps-tab">
            <div class="tab-content text-center">
                <div class="tab-pane active" id="premisesTab" role="tabpanel">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="new-premise-form-conveyance">
                                <div class="form-horizontal">
                                    <iais:row>
                                        <div class="col-xs-12">
                                            <div class="bg-title">
                                                <h2>TERMS OF USE</h2>
                                            </div>
                                            <textarea id="blockOutDesc" name="blockOutDesc" rows="95" class="col-xs-12">1.  Thank you for visiting the Ministry of Health corporate website. Access to any page in www.moh.gov.sg (henceforth known as "Web Site") is governed by the terms and conditions stipulated below (“Terms”). Your access and use of this Web Site constitutes your acceptance and agreement to be bound by these terms and conditions of use (“Terms of Use”). If you do not consent to be bound by these Terms of Use, please discontinue your access and do not use this Web Site.   In addition, the use of Datasets (as defined in the Singapore Open Data Licence set out below) contained in this Website is subject to the Singapore Open Data Licence; in the event of any inconsistency between these Terms of Use and the Singapore Open Data Licence, the former shall prevail.

2.  This Web Site is maintained by and belongs to the Ministry of Health.

3.  In case of any violation of these Terms of Use, we reserve the right to seek all remedies available under the law and in equity for such violations. These Terms of Use apply in respect of any and all visits to this Web Site, both now and in the future.

4.  In these Terms, any reference to “Ministry of Health”, “we”, “our” and “us” refers to the Government of Singapore.Variation
5.  The Ministry of Health may, from time to time and at its sole and absolute discretion, amend these Terms of Use without prior notice. The amended Terms of Use will be posted on this Web Site. Your access and use of this Web Site after the amended Terms of Use have been posted, will constitute your acceptance and agreement to be bound by the amended Terms of Use. To avoid doubt, references to these “Terms of Use” shall include such Terms of Use as amended from time to time by the Ministry of Health and posted on this Web Site.

Relying On Information
6.   The Ministry of Health provides this Web Site as a general information source only and is not involved in giving professional advice here. The Web Site may not cover all information available on a particular issue. Before relying on this Web Site, you should do your own checks or obtain professional advice relevant to your particular circumstances.

Security
7.   Where appropriate, the Ministry of Health uses available technology to protect the security of communications made through this Web Site. However, the Ministry of Health does not accept liability for the security, authenticity, integrity or confidentiality of any transactions and other communications made through the Web Site. Internet communications may be susceptible to interference or interception by third parties. Despite best efforts, the Ministry of Health makes no warranties that the Web Site is free of infection by computer viruses or other unauthorised software. You should take appropriate steps to keep your information, software and equipment secure. This includes clearing your Internet browser cookies and cache before and after using any services on the Web Site. You should keep your passwords confidential. Please note that we will never ask you for your SingPass or any other login password.

Proprietary Rights
8.   All materials contained in this Web Site (the “Contents”), including without limitation text, images, music, computer programmes and all other kinds of works, are protected by applicable copyright, trademark and/or other intellectual property laws . All rights, title and/or interests in the Contents are owned by, licensed to or controlled by the Ministry of Health.

Privacy Policy
9.   Click here to view this Web Site’s Privacy Policy Statement.

Restrictions on use
10.  You may access and use this Web Site and the Contents in the manner permitted in these Terms of Use.

11.   Except as otherwise provided), the Contents of this Web Site shall not be reproduced, republished, uploaded, posted, transmitted or otherwise distributed in any way, without the prior written permission of the Ministry of Health. You acknowledge that any use of this Web Site or the Contents otherwise than in accordance with these Terms of Use will constitute a violation of these Terms of Use and the relevant intellectual property rights subsisting therein.

12.    Modification of any of the Contents or use of the Contents for any other purpose will be a violation of the Ministry of Health's copyright and other intellectual property rights. Graphics and images on this Web Site are protected by copyright and may not be reproduced or appropriated in any manner without prior written permission of their respective owners.

13.  The design and layout of this Web Site is protected by intellectual property and other laws, and may not be copied or imitated in whole or in part. No logo, graphic, sound, image or search engine from this Web Site may be copied or transmitted without the prior written permission of the Ministry of Health.

Disclaimer of Warranties and Liability
14.   The Contents of this Web Site are provided on an "as is" basis. While the Ministry of Health has made every reasonable effort to ensure that the Contents on this Web Site have been obtained from reliable sources, we do not make any representations or warranties whatsoever, and to the fullest extent permitted by law, the Ministry of Health hereby disclaims all express, implied, and/or statutory warranties of any kind to you or any third party, howsoever arising and whether arising from usage or custom or trade or by operation of law or otherwise, including without limitation to:

i.   any representations or warranties as to the accuracy, completeness, correctness, currency, reliability, timeliness, non-infringement, title, merchantability, quality or fitness for any particular purpose of the Contents of this Web Site;

ii.  any representations or warranties that the Contents and functions available on this Web Site shall be uninterrupted or error-free, or that defects will be corrected or that this Web Site and the hosting servers are and will be free of all viruses, other malicious codes, programmes or macros and/or other harmful elements.

15.   The Ministry of Health will not be responsible or liable to you or any third party for any errors in, or for the results obtained or consequences arising from the use of any of the Contents, and damage or loss whatsoever and howsoever caused, including without limitation, any direct or indirect, special or consequential damages, loss of income, revenue or profits, lost, or damaged data, or damage to your computer, software, server or any other kind of property, howsoever arising directly or indirectly from:

(a) your access to or use of this Web Site;

(b) any loss of access to or use of this Web Site, howsoever caused;

(c) any inaccuracy or incompleteness in, or errors or omissions in the transmission of, the Contents;

(d) any delay or interruption in the transmission of the Contents on this Web Site, whether caused by delay or interruption in transmission over the internet or otherwise; or

(e) any decision made or action taken by you or any third party in reliance upon the Contents.

Indemnity
16.  You hereby agree to indemnify the Ministry of Health and hold the Ministry of Health harmless from and against any and all claims, losses, liabilities, costs, and expenses (including but not limited to legal costs and expenses on a full indemnity basis) made against or suffered or incurred by the Ministry of Health however arising directly or indirectly from:

(a) your access to or use of this Web Site; and/or

(b) your breach of any of these Terms of Use.</textarea>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <div class="col-xs-12">
                                            <input type="checkbox" id="agreeCheckBox" style="margin-top: 19px"
                                                   value="admin"
                                                   name="role" checked>
                                            <label>I agree to the Terms and
                                                Conditions</label>
                                        </div>
                                    </iais:row>
                                    <iais:row>
                                        <button type="button" class="search btn" onclick="declaration();">Declaration
                                        </button>
                                    </iais:row>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</form>


</form>
<script type="text/javascript">
    function declaration() {
        if ($('#agreeCheckBox').is(':checked')) {
            SOP.Crud.cfxSubmit("mainForm");
        } else {
            alert('Please check ');
        }

    }

</script>