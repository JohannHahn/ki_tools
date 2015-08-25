-- INIT --

function init(name)
	name="Evade"
	
end

-- UPDATE --
function update(entity)   	
	
	
		entity.stateMachine.changeState(BoidState.EVADE);
	
	
	
end


function setName(name)
	name = "Evade"
	return name
end
	