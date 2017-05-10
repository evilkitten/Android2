Const PLAYER_BULLET_SPEED#=0.25
Const PLAYER_SHOOT_RANGE=3.9
Const PLAYER_BULLET_Y_OFFSET#=0.8

Global CURRENT_BULLET
Global BULLET_DISTANCE#
Global BULLET_DIRECTION
Global BULLET_GHOST.GHOST

Function PlayerFire()
	LAST_PLAYER_FIRE=FRAME
	SpawnBullet()
End Function

Function DestroyBullet()
	Delete BULLET_GHOST
	
	FreeEntity  CURRENT_BULLET
	CURRENT_BULLET=0
	BULLET_DISTANCE=0.0
End Function

;~IDEal Editor Parameters:
;~F#9#E
;~C#Blitz3D