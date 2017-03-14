Global PIVOT_PLAYER

Const PLAYER_STATE_IDLE=0
Const PLAYER_STATE_WALK=1

Global PLAYER_STATE
Global PLAYER_COLLISION_RADIUS#=0.25;0.375
Global PLAYER_Y_OFFSET#

Const PLAYER_FILE$="Android"

Function PlayerAnimFile$()
	Return AnimFile(PLAYER_FILE)
End Function

Function PlayerMatFile$()
	Return MatFile(PLAYER_FILE)
End Function

Function InitialisePlayer()
	If (PIVOT_PLAYER)
		FreeEntity PIVOT_PLAYER
		PIVOT_PLAYER=0
	End If
	
	PIVOT_PLAYER=LoadAnimMesh(PlayerAnimFile())
	PositionEntity PIVOT_PLAYER,CAPSULEX,GROUND_BASELINE_Y#+PLAYER_Y_OFFSET,CAPSULEY,True
	
	FinalisePlayer
End Function

Function FinalisePlayer()
	InitialisePlayerAnimSequences
	PaintPlayer
	SetPlayerPhysics
End Function

Function SetPlayerPhysics()
	ScaleEntity PIVOT_PLAYER,1,0.5,1,True
	PLAYER_Y_OFFSET=0.0
	EntityRadius PIVOT_PLAYER,PLAYER_COLLISION_RADIUS+0.5;+(Float(Not(SPECTRUM_MODE)*0.15))
	EntityPickMode PIVOT_PLAYER,1
End Function

Function PaintPlayer()
	Local Texture=AcquireTextureMap(PlayerMatFile())
	PaintChildren(PIVOT_PLAYER,Texture,95,95,95)
	FreeTexture Texture
End Function

Function InitialisePlayerAnimSequences()
	LoadAnimSeq(PIVOT_PLAYER,PlayerAnimFile())
End Function

Function SetPlayerAnimation(State=PLAYER_STATE_IDLE)
	If (State<>PLAYER_STATE)
		Animate PIVOT_PLAYER,State,1.0,State,1.0
		PLAYER_STATE=State
	End If
End Function
;~IDEal Editor Parameters:
;~F#B#F#13#1F#25#2C#32#36
;~C#Blitz3D