<Branches id="${id}">
    <Properties>
		{for i in properties.propertiesData}
		<Entry name="${i_index}">
			<Value>${i}</Value>
		</Entry>
		{/for}
	</Properties>
	{for column in columns}
		${column}
	{/for}
</Branches>
