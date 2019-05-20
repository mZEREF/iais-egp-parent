{var id = control.getElementId()}
{var type = control.properties.type}
<div id="${id}" class="control ${control.properties.developerclass}">
	<div class="control-label-span control-set-alignment label-control">
		<label id="${id}--label"
				class="${type} control-label control-set-font">
			{if control.properties.caption}
				${control.properties.caption|escape}
			{else}
				&nbsp;
			{/if}
		</label>
		<span class="upload_controls"></span>
	</div>
</div>
