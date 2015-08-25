-- INIT --

function init(name)
	name="No Target"
	
end

-- UPDATE --
function update(entity)   	
	
	
		entity.stateMachine.changeState(BoidState.NO_TARGET);
	
	
	
end


function setName(name)
	name = "No Target"
	return name
end
	