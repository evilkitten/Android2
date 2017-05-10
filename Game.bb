Global GAME_PAUSE=False

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
End Function
;~IDEal Editor Parameters:
;~F#2#F#23
;~C#Blitz3D