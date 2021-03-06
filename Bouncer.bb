Const DIRECTION_UNDEFINED=-2
Const BOUNCER_Y_OFFSET#=0.5

Global BOUNCER_MASTER

Type BOUNCER
	Field Entity
	Field G.GHOST
End Type

Function InitialiseBouncerFiles()
	UnPackAsset(PACK_BOUNCER_ANIM_START,PACK_BOUNCER_ANIM_LENGTH)
	UnPackAsset(PACK_BOUNCER_MAT_START,PACK_BOUNCER_MAT_LENGTH)
End Function

Function UnInitialiseBouncerFiles()
	If (TEST) Then Return
	DeleteFile BouncerAnimFile()
	DeleteFile BouncerMatFile()
End Function

Function BouncerAnimFile$()
	Return AnimFile(MAP_BOUNCER_NAME)
End Function

Function BouncerMatFile$()
	Return MatFile(MAP_BOUNCER_NAME)
End Function

Function BuildBouncerMaster()
	If (BOUNCER_MASTER)
		;FreeEntity BOUNCER_MASTER
		;BOUNCER_MASTER=0
		Return
	End If
	
	InitialiseBouncerFiles
	
	BOUNCER_MASTER=LoadAnimMesh(BouncerAnimFile())
	
	PaintBouncerMaster
	SetBouncerPhysics
	
;	ScaleEntity BOUNCER_MASTER,1.0,(0.5+(0.5*SPECTRUM_MODE)),1.0,True
	
	HideEntity BOUNCER_MASTER
	PositionEntity BOUNCER_MASTER,0,-130,0
	
	UnInitialiseBouncerFiles
	
End Function

Function SetBouncerPhysics()
	EntityPickMode BOUNCER_MASTER,3
End Function

Function PaintBouncerMaster()
	Local Texture=AcquireTextureMap(BouncerMatFile())
	PaintChildren(BOUNCER_MASTER,Texture,255,0,0)
	FreeTexture Texture
	EntityShininess BOUNCER_MASTER,0.25+(SPECTRUM_MODE*0.75)
End Function

Function SpawnBouncer(X#,Z#)
	Local B.BOUNCER=New BOUNCER
	B\Entity=CopyEntity(BOUNCER_MASTER)
	
	NameEntity B\Entity,Str(Handle(B))
	
	PositionEntity B\Entity,X#-0.5,GROUND_BASELINE_Y#+BOUNCER_Y_OFFSET#,Z#-0.5,True
	
	AddShadow(B\Entity)
	GhostBouncer(B)
End Function

Function FinaliseBouncerDirections()
	;Optimise this later if req'd. First check for vertical blockage, if none, check for horizontal - if none of either, bouncer motion is vertical
	
	Local B.BOUNCER
	For B=Each BOUNCER
		
		Local X#=EntityX#(B\Entity,True);Int(Floor(EntityX(B\Entity,True)))
		Local Y#=EntityZ#(B\Entity,True);Int(Floor(EntityZ(B\Entity,True)))
		
		X#=X#-0.5
		Y#=Y#-0.5
		
		Local BlockedVertical=(IsBlockedUp(X,Y) Or IsBlockedDown(X,Y))
		Local BlockedHorizontal=(IsBlockedLeft(X,Y) Or IsBlockedRight(X,Y))
		
		TurnEntity B\Entity,0,(180*Rand(0,1))+(90*BlockedVertical),0,True
	Next
End Function

Function MoveBouncer(B.BOUNCER)
	Local Picked=EntityPick(B\Entity,0.3)
	
	If (Picked)
		If (EntityDistance(B\Entity,Picked)<=1.25) Or (Picked=WALL_MESH)
		
			;Player Collision
			If (Picked=PIVOT_PLAYER) Then PlayerCollision("Bouncer",B\Entity)
			
			TurnEntity B\Entity,0,180,0,True
		End If
	End If
	
	MoveEntity B\Entity,0,0,GAME_MOVEMENT_SPEED*TICK
		
	;Wraparound
	If (EntityX#(B\Entity,True)<0)
		PositionEntity B\Entity,EntityX#(B\Entity,True)+MAPSIZEX,BOUNCER_Y_OFFSET,EntityZ(B\Entity,True),True
	Else
		If (EntityX#(B\Entity,True)>=MAPSIZEX)
			PositionEntity B\Entity,EntityX#(B\Entity,True)-MAPSIZEX,BOUNCER_Y_OFFSET,EntityZ(B\Entity,True),True
		End If
	End If
	
End Function

Function RemoveBouncer(B.BOUNCER)
	RemoveGhost(B\G)
	FreeEntity B\Entity
	Delete B
End Function
;~IDEal Editor Parameters:
;~F#5#A#F#15#19#1D#34#38#3F#4B#5E#78
;~C#Blitz3D