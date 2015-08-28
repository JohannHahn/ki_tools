
name = "Evade"

function init()
end

function enter(entity)
end

-- UPDATE --
function update(entity)  	
	 	
	if not entity:setTarget(entity:searchTarget(), name) then
		entity:changeStateByName("Wander")
	end
	
	if entity:gotHit() then
		entity:switchTeams()
		entity:changeStateByName("Pursuit")
	end	
	
	if entity:checkFuel() then
		entity:removeComponent("Seek")
		entity:changeStateByName("Wander")
	end
	
	
end

function exit(entity)
	entity:removeComponent(name)
end


function setName(inputName)	
	return name
end
	