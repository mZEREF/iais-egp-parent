{if options && options.length > 0}
<table {if control.properties.width > 0} width="${control.properties.width}"{/if}
		{if control.properties.alignment != "" && control.properties.alignment != null} align="${control.properties.alignment}"{/if}>
	{var cols = control.properties.cols}
	{var i}
	{var width = (100 / cols) + '%'}
	{for option in options}
		{eval} i = option_index {/eval}
		{if option_index % cols == 0}
			{if option_index > 0} </tr> {/if}
			<tr>
		{/if}
		<td {if i < cols} width="${width}" {/if}>
			<div class="control-item-container">
				<input type="checkbox" id="${id}--${option_index}"
						name="${id}"
						class="control-input" value="${option.code|escape}"
						{if control.properties.selected.indexOf(option.code) >= 0} checked {/if} />
				{if control.properties.supportEscapeOption}
					<label id="${id}--label--${option_index}"
							for="${id}--${option_index}"
							class="checkbox-a control-label control-set-font control-font-normal">${option.description|escape}</label>
				{/if}
				{if !control.properties.supportEscapeOption}
					<label id="${id}--label--${option_index}"
							for="${id}--${option_index}"
							class="checkbox-a control-label control-set-font control-font-normal">${option.description}</label>
				{/if}
			</div>
			<div class="control-item-aux-container"
				{if control.properties.source_type == 'lookup'}
				style="display: none;"
				{/if}
				>
			</div>
		</td>
	{/for}

	${i++|eat}
	{var mod = i % cols}
	{if mod != 0}
		{var r = Form.Util.range(1, cols - mod)}
		{for j in r}
			<td {if i <= cols} width="${width}" {/if}></td>
		{/for}
	{/if}
	</tr>
</table>
{/if}
