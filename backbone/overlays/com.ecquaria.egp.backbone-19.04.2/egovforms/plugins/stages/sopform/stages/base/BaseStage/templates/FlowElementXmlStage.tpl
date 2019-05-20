<Stage id="${id}" type="${FQN}">
    <Properties>
		{for i in properties.propertiesData}
		<Entry name="${i_index}">
			<Value>${i}</Value>
		</Entry>
		{/for}
	</Properties>
</Stage>
