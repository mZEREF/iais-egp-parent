<div id="${id}" class="splitColumn split-container">
	<div class="splitColumnHeader">
		<h4 class="formFlowName split-header">
			{if properties.propertiesData && properties.propertiesData.stageName}
				${properties.propertiesData.stageName}
			{else}
				${id}
			{/if}
		</h4>
	</div>
	<div class="stagesList"/>
</div>