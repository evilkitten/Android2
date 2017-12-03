Global GAME_PAUSE=False
Const GAME_MOVEMENT_SPEED#=0.1

;Global GAME_DIFFICULTY - Not yet in use....

Function GameUpdate()
	If (Not(GAME_PAUSE))
		GameUpdateEnemies
		GameUpdateBullets
		AuditGhostPositions
	
		PlayerControlResponse
	End If
	
	UpdateMines
	AdditonalControlInputs
End Function

Function GameUpdateEnemies()
	Local mm.MILLITOID
	
	For mm.MILLITOID=Each MILLITOID
		MoveMillitoid(mm)
	Next
	
	Local bb.BOUNCER
	
	For bb=Each BOUNCER
		MoveBouncer(bb)
	Next
	
	Local hh.HOVERDROID
	
	For hh=Each HOVERDROID
		MoveHoverdroid(hh)
	Next
End Function

Function GameUpdateBullets()
	MoveBullet()
	UpdateLaser
End Function
;~IDEal Editor Parameters:
;~F#5#12
;~C#Blitz3D