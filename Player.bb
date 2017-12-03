Include "PlayerBullet.bb"

Global PIVOT_PLAYER

Const PLAYER_STATE_IDLE=0
Const PLAYER_STATE_WALK=1

Global PLAYER_STATE
Global PLAYER_COLLISION_RADIUS#=0.01;0.375
Global PLAYER_Y_OFFSET#

Global LAST_KILLER_MINEX=9999
Global LAST_KILLER_MINEY=9999

Function UnInitialisePlayerFiles()
	If (TEST) Then Return
	
	DeleteFile PlayerAnimFile()
	DeleteFile PlayerMatFile()
End Function

Function InitialisePlayerFiles()
	UnPackAsset(PACK_ANDROID_ANIM_START,PACK_ANDROID_ANIM_LENGTH)
	UnPackAsset(PACK_ANDROID_MAT_START,PACK_ANDROID_MAT_LENGTH)
End Function

Function PlayerAnimFile$()
	Return AnimFile(APP_TITLE)
End Function

Function PlayerMatFile$()
	Return MatFile(APP_TITLE)
End Function

Function InitialisePlayer()
	If (PIVOT_PLAYER)
		;FreeEntity PIVOT_PLAYER
		;PIVOT_PLAYER=0
		Return
	End If
	
	InitialisePlayerFiles
	
	PIVOT_PLAYER=LoadAnimMesh(PlayerAnimFile())
	PositionEntity PIVOT_PLAYER,CAPSULEX,GROUND_BASELINE_Y#+PLAYER_Y_OFFSET,CAPSULEY,True
	
	FinalisePlayer
	
	UnInitialisePlayerFiles
	
End Function

Function FinalisePlayer()
	InitialisePlayerAnimSequences
	PaintPlayer
	SetPlayerPhysics
End Function

Function SetPlayerPhysics()
	ScaleEntity PIVOT_PLAYER,1,0.5,1,True
	PLAYER_Y_OFFSET=0.0
	EntityRadius PIVOT_PLAYER,0.25
	EntityPickMode PIVOT_PLAYER,3
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

Function PlayerControlResponse()
	Local X#=EntityX#(PIVOT_PLAYER,True)
	Local Y#=EntityY#(PIVOT_PLAYER,True)
	Local Z#=EntityZ#(PIVOT_PLAYER,True)
	
	Local Control[CTRL_MAX]
	GetControlInput(Control)
	
	If Control[CTRL_FIRE]
		SetPlayerAnimation(PLAYER_STATE_IDLE)
		If ((FRAME-LAST_PLAYER_FIRE)>PLAYER_FIRE_DELAY)
			PlayerFire
		End If
		
	Else
		If (Control[CTRL_HORIZONTAL])
			RotateEntity PIVOT_PLAYER,0,Int(90*Control[CTRL_HORIZONTAL]),0,True
			If (LinePick(X#,Y#,Z#,Control[CTRL_HORIZONTAL]*GAME_MOVEMENT_SPEED,0,0,PLAYER_COLLISION_RADIUS))=False
				X#=(X#+Float(Control[CTRL_HORIZONTAL]*GAME_MOVEMENT_SPEED*TICK))
				X#=(X#+MAPSIZEX)Mod MAPSIZEX
			End If
		Else
			If (Control[CTRL_VERTICAL])
				RotateEntity PIVOT_PLAYER,0,90+Int((Control[CTRL_VERTICAL]*90.0)),0,True
				If (LinePick(X#,Y#,Z#,0,0,Control[CTRL_VERTICAL]*GAME_MOVEMENT_SPEED,PLAYER_COLLISION_RADIUS))=False
					
					Z#=(Z#+Float(Control[CTRL_VERTICAL]*GAME_MOVEMENT_SPEED*TICK))
					Z#=(Z#+MAPSIZEY)Mod MAPSIZEY
				End If
				
			End If
		End If
		
		If ((Control[CTRL_HORIZONTAL]<>0)Or (Control[CTRL_VERTICAL]<>0))
			SetPlayerAnimation(PLAYER_STATE_WALK)
		Else
			SetPlayerAnimation(PLAYER_STATE_IDLE)
		End If
		
		If (PeekArrayValue(Int(Floor(X)),Int(Floor(Z)))=MAP_MINE)
				If(CheckLastKillerMine(Int(Floor(X)),Int(Floor(Z))))
					;This mine killed us last time - we can ignore it for now...
				Else
				
				;Much more straightforward than the broken B3D collision system
				SetLastKillerMine(Int(Floor(X)),Int(Floor(Z)))
				
				MineExplosion()
			End If
		Else
			ResetLastKillerMine
		End If
		
		PositionEntity PIVOT_PLAYER,X#,Y#,Z#,True
	End If
End Function
;~IDEal Editor Parameters:
;~F#E#15#1A#1E#22#34#3A#41#47#4B#52
;~C#Blitz3D