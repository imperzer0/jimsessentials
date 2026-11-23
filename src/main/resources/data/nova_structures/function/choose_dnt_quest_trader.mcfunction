execute as @e[type=villager,tag=!trader,tag=dnt_quest_trader,limit=1,sort=nearest] run function nova_structures:add_trade

advancement revoke @a only minecraft:give_quest_trader_trade