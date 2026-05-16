execute as @e[type=lightning_bolt] at @s run function nova_structures:find_allays
execute as @e[type=wither_skeleton, tag=!withering] run function nova_structures:add_withering
schedule function nova_structures:tick 5t