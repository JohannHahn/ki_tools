



-- INIT --

function init(name)
	name="Pursuit"
	
end

-- UPDATE --
function update(entity)   	
	
	
		entity.stateMachine.changeState(BoidState.PURSUIT);
	
	
	
end


function setName(name)
	name = "Pursuit"
	return name
end
	



