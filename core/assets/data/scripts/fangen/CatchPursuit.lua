
name = "Pursuit"
function init()
end

function enter(entity)
	print(entity.team ,"entered State", name)

end

-- UPDATE --
function update(entity)   	
	
	if not entity:setTarget(entity:searchTarget(), name) then
		entity:changeStateByName("Wander")
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


