<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2021/5/24
  Time: 9:48
  To change this template use File | Settings | File Templates.
--%>
<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.BE_CSS_ROOT;
%>
<style>
    @media only screen and (min-width: 1201px) and (max-width: 1920px) {
        .dashboard-multiple .dashboard-tile-item .dashboard-tile {
            border-radius: 18px;
            height: 265px;
            margin-bottom: 55px;
        }
    }
    .dashboard-chart .dashboard-tile-item .dashboard-tile a.empty-chart {
        cursor: default;
        background-image: url('<%=webroot1%>img/chart-0.png');
        background-repeat: no-repeat;
        background-position: center 25%;
    }
</style>

<input type="hidden" name="overAllVal" value='${dashOverAllCircleKpi}'/>
<input type="hidden" name="asoVal" value='${dashAsoCircleKpi}'/>
<input type="hidden" name="psoVal" value='${dashPsoCircleKpi}'/>
<input type="hidden" name="preInspVal" value='${dashPreInspCircleKpi}'/>
<input type="hidden" name="inspVal" value='${dashInspCircleKpi}'/>
<input type="hidden" name="postInspVal" value='${dashPostInspCircleKpi}'/>
<input type="hidden" name="ao1Val" value='${dashAo1CircleKpi}'/>
<input type="hidden" name="ao2Val" value='${dashAo2CircleKpi}'/>
<input type="hidden" name="ao3Val" value='${dashAo3CircleKpi}'/>
<input type="hidden" id="dashSysStageVal" name="dashSysStageVal" value=''/>

<div class="main-content">
    <div class="row">
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row">
                        <div class="col-md-6 col-xs-12 col-lg-6">
                            <a data-tab="#" href="javascript:;" style="cursor: default;">
                                <div id="canvas-holder">
                                    <canvas id="overAllCanvas"></canvas>
                                </div>
                                <%--<p class="dashboard-txt main-chart-text font-color-black"> Overall</p>--%>
                            </a>
                        </div>
                        <div class="col-md-6 col-xs-12 col-lg-6">
                            <div class="form-horizontal filter-box">
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label">Application type</label>
                                    <div class="col-xs-12 col-sm-6 col-md-5" style="padding-left: unset;padding-top: 1%;">
                                        <iais:select cssClass="" name="appType" firstOption="" needSort="true" options="appTypeOption" multiValues="${dashAppTypeCheckList}" multiSelect="true" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label">Service Licence</label>
                                    <div class="col-xs-12 col-sm-6 col-md-5" style="padding-left: unset;padding-top: 1%;">
                                        <iais:select cssClass="" name="svcLic" firstOption="" needSort="true" options="dashServiceOption" multiValues="${dashSvcCheckList}" multiSelect="true" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-12 col-xs-12">
                            <div style="text-align:right;">
                                <button class="btn btn-secondary" type="button" id="sysClearBtn" name="sysClearBtn">Clear</button>
                                <button class="btn btn-primary" type="button" id="sysSearchBtn" name="sysSearchBtn">Search</button>
                            </div>
                        </div>
                        <hr>
                        <div class="col-xs-12">
                            <h3>
                                <span>Applications at each stage </span>
                            </h3>
                            <div class="dashboard-chart dashboard-multiple">
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a id="sysAsoCanvas" data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="asoCanvas"></canvas>
                                            </div>
                                            <p align="center" > Admin Screening</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a id="sysPsoCanvas" data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="psoCanvas"></canvas>
                                            </div>
                                            <p align="center" > Professional Screening</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a id="sysPreInspCanvas" data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="preInspCanvas"></canvas>
                                            </div>
                                            <p align="center" > Pre-Inspection</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a id="sysInspCanvas" data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="inspCanvas"></canvas>
                                            </div>
                                            <p align="center" > Inspection</p>
                                        </a>
                                    </div>
                                </div>

                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a id="sysPostInspCanvas" data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="postInspCanvas"></canvas>
                                            </div>
                                            <p align="center" > Post-Inspection</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a id="sysAo1Canvas" data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="ao1Canvas"></canvas>
                                            </div>
                                            <p align="center" > AO1</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a id="sysAo2Canvas" data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="ao2Canvas"></canvas>
                                            </div>
                                            <p align="center" > AO2</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a id="sysAo3Canvas" data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="ao3Canvas"></canvas>
                                            </div>
                                            <p align="center" > AO3</p>
                                        </a>
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
<%@include file="beDashboardFun.jsp"%>
<script>
    $(document).ready(function () {

        chartRegister();

        $('#sysAsoCanvas').click(function () {
            showWaiting();
            $('#dashSysStageVal').val('ASO');
            intraDashboardSubmit('sysdet');
        });
        $('#sysPsoCanvas').click(function () {
            showWaiting();
            $('#dashSysStageVal').val('PSO');
            intraDashboardSubmit('sysdet');
        });
        $('#sysPreInspCanvas').click(function () {
            showWaiting();
            $('#dashSysStageVal').val('PRE');
            intraDashboardSubmit('sysdet');
        });
        $('#sysInspCanvas').click(function () {
            showWaiting();
            $('#dashSysStageVal').val('INS');
            intraDashboardSubmit('sysdet');
        });
        $('#sysPostInspCanvas').click(function () {
            showWaiting();
            $('#dashSysStageVal').val('POT');
            intraDashboardSubmit('sysdet');
        });
        $('#sysAo1Canvas').click(function () {
            showWaiting();
            $('#dashSysStageVal').val('AO1');
            intraDashboardSubmit('sysdet');
        });
        $('#sysAo2Canvas').click(function () {
            showWaiting();
            $('#dashSysStageVal').val('AO2');
            intraDashboardSubmit('sysdet');
        });
        $('#sysAo3Canvas').click(function () {
            showWaiting();
            $('#dashSysStageVal').val('AO3');
            intraDashboardSubmit('sysdet');
        });


        initChart('aso', 'sysAsoCanvas', false, null);

        initChart('pso', 'sysPsoCanvas', false, null);

        initChart('preInsp', 'sysPreInspCanvas', false, null);

        initChart('insp', 'sysInspCanvas', false, null);

        initChart('postInsp', 'sysPostInspCanvas', false, null);

        initChart('ao1', 'sysAo1Canvas, false, null');

        initChart('ao2', 'sysAo2Canvas', false, null);

        initChart('ao3', 'sysAo3Canvas', false, null);

        initChart('overAll', 'overAllCanvas', true, 'Overall');



        $('#sysClearBtn').click(function () {
            doClear();
        });

        $('#sysSearchBtn').click(function () {
            showWaiting();
            let dashSwitchActionValue = $('#switchAction').val();
            intraDashboardSubmit(dashSwitchActionValue);
        });
    });


</script>
