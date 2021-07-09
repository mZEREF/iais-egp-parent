<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2021/5/25
  Time: 10:50
  To change this template use File | Settings | File Templates.
--%>
<style>
  .font-color-black{
    color: #333333;
  }
  .cursor-default{
    cursor: default;!important;
  }
</style>
<script>

    var chartRegister  = function () {
        Chart.pluginService.register({
            beforeDraw: function (chart) {
                var width = chart.chart.width,
                    height = chart.chart.height,
                    ctx = chart.chart.ctx;
                ctx.restore();
                var fontSize = (height / 114).toFixed(2);
                ctx.font = fontSize + "em sans-serif";
                ctx.textBaseline = "middle";
                var text = chart.config.options.elements.center.text,
                    textX = Math.round((width - ctx.measureText(text).width) / 2),
                    textY = height / 2;
                ctx.fillText(text, textX, textY);
                ctx.save();
            }
        });
    };
    var initChart = function (canvasName, jumpId, isOverAll, overAllTitle) {
        var jsonStr = $('input[name="'+canvasName+'Val"]').val();
        var emptyDisplayVal = '-';
        var redSize = 0;
        var blueSize = 0;
        var yellowSize = 0;
        var stageCount = emptyDisplayVal;
        if(jsonStr != null && jsonStr != '' && jsonStr != undefined){
            var canvasJsonObj = eval(JSON.parse(jsonStr));
            redSize = canvasJsonObj.dashRedCount;
            blueSize = canvasJsonObj.dashBlueCount;
            yellowSize = canvasJsonObj.dashAmberCount;
            if('0' != canvasJsonObj.dashStageCount){
                stageCount = canvasJsonObj.dashStageCount;
            }
        }
        var labelsNameArr = new Array();
        if(isOverAll){
            labelsNameArr[0] = 'Beyond Target KPI';
            labelsNameArr[1] = 'Within Target KPI';
            labelsNameArr[2] = 'Within Reminder threshold';
        }else{
            labelsNameArr[0] = 'Red';
            labelsNameArr[1] = 'Blue';
            labelsNameArr[2] = 'Yellow';
        }
        var data = {
            labels: labelsNameArr,
            datasets: [{
                data: [redSize, blueSize, yellowSize],
                backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56"],
                hoverBackgroundColor: ["#FF6384", "#36A2EB", "#FFCE56"]
            }]
        };

        var canvasId = canvasName+'Canvas';
        if(isOverAll){
            new Chart(document.getElementById(canvasId), {
                type: 'doughnut',
                data: data,
                options: {
                    responsive: true,
                    maintainAspectRatio: true,
                    elements: {
                        center: {
                            text: stageCount
                        }
                    },
                    tooltips:{
                        enabled:true
                    },
                    title:{
                        display:true,
                        text:overAllTitle
                    },
                    cutoutPercentage: 75,
                    legend: {
                        display: true,
                        position:'bottom'
                    },
                    tooltips: {
                        callbacks: {
                            label: function (tooltipItem, data) {
                                return ' '+data.datasets[0].data[tooltipItem.index];
                            }
                        }
                    }
                }
            });
        }else{
            new Chart(document.getElementById(canvasId), {
                type: 'doughnut',
                data: data,
                options: {
                    elements: {
                        center: {
                            text: stageCount
                        }
                    },
                    cutoutPercentage: 75,
                    legend: {
                        display: false
                    },
                    tooltips: {
                        callbacks: {
                            label: function (tooltipItem, data) {
                                return ' '+data.datasets[0].data[tooltipItem.index];
                            }
                        }
                    }
                }
            });
        }
        if(emptyDisplayVal == stageCount && jumpId != null){
            $('#'+jumpId).addClass('empty-chart');
            $('#'+jumpId).unbind('click');
        }
    };

    var doStatisticsBoardClear = function () {
        showWaiting();
        $('input[name="applicationNo"]').val("");

        $('#appType').val('');
        $('#appType').next().find('span:eq(0)').html('-- Select --');
        $('#appType').next().find('input[type="checkbox"]').prop('checked',false);

        $('#svcLic').val('');
        $('#svcLic').next().find('span:eq(0)').html('-- Select --');
        $('#svcLic').next().find('input[type="checkbox"]').prop('checked',false);
        dismissWaiting();
    }

</script>
