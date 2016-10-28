Global PIVOT_PLAYER

Const PLAYER_STATE_IDLE=0
Const PLAYER_STATE_WALK=1

Global PLAYER_STATE
Global PLAYER_COLLISION_RADIUS#=0.25
Global PLAYER_Y_OFFSET#

Function InitialisePlayer()
	If (PIVOT_PLAYER)
		FreeEntity PIVOT_PLAYER
		PIVOT_PLAYER=0
	End If
	
	PIVOT_PLAYER=LoadAnimMesh(VisualDir()+"Android.3ds")
	PositionEntity PIVOT_PLAYER,CAPSULEX,PLAYER_Y_OFFSET,CAPSULEY,True
	
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
End Function

Function PaintPlayer()
	Local Texture=LoadTexture(VisualDir()+"Android_Mat.png")
	PaintChildren(PIVOT_PLAYER,Texture,95,95,95)
	FreeTexture Texture
End Function

Function InitialisePlayerAnimSequences()
	LoadAnimSeq(PIVOT_PLAYER,VisualDir()+"Android_Anim.3ds")
End Function

Function SetPlayerAnimation(State=PLAYER_STATE_IDLE)
	If (State<>PLAYER_STATE)
		Animate PIVOT_PLAYER,State,0.1,State,1.0
		PLAYER_STATE=State
	End If
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D