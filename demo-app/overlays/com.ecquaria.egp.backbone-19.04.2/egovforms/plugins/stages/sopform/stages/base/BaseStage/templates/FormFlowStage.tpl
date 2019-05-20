<div id="${id}" class="formFlowStage">
    <img src="${definition.icon}"/><br/>
    <span class="formFlowName">
	    {if properties.propertiesData && properties.propertiesData.stageName}
	    	${properties.propertiesData.stageName}
	    {else}
	  		${id}
	    {/if}
	</span>
</div>
