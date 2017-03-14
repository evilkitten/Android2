Const DIRECTION_UNDEFINED=-2
Const BOUNCER_Y_OFFSET#=0.5

Const BOUNCER_FILE$="Bouncer"

Global BOUNCER_MASTER

Type BOUNCER
	Field Entity
End Type

Function BouncerAnimFile$()
	Return AnimFile(BOUNCER_FILE)
End Function

Function BouncerMatFile$()
	Return MatFile(BOUNCER_FILE)
End Function

Function BuildBouncerMaster()
	If (BOUNCER_MASTER)
		FreeEntity BOUNCER_MASTER
		BOUNCER_MASTER=0
	End If
	
	BOUNCER_MASTER=LoadAnimMesh(BouncerAnimFile())
	
	PaintBouncerMaster
	SetBouncerPhysics
	
;	ScaleEntity BOUNCER_MASTER,1.0,(0.5+(0.5*SPECTRUM_MODE)),1.0,True
	
	HideEntity BOUNCER_MASTER
	PositionEntity BOUNCER_MASTER,0,-130,0
End Function

Function SetBouncerPhysics()
	;
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
	PositionEntity B\Entity,X#-0.5,GROUND_BASELINE_Y#+BOUNCER_Y_OFFSET#,Z#-0.5,True
	;EntityBox B\Entity,0.5,0,0.5,1,1,1
	EntityPickMode B\Entity,3
	AddShadow(B\Entity)
	GhostBouncer(B)
End Function

Function FinaliseBouncerDirections()
	;Optimise this later if req'd. First check for vertical blockage, if none, check for horizontal - if none of either, bouncer motion is vertical
	
	Local B.BOUNCER
	For B=Each BOUNCER
		
		Local X#=EntityX#(B\Entity,True);Int(Floor(EntityX(B\Entity,True)))
		Local Y#=EntityZ#(B\Entity,True);Int(Floor(EntityZ(B\Entity,True)))
		
		X=X-0.5
		Y=Y-0.5
		
		Local BlockedVertical=(IsBlockedUp(X,Y) Or IsBlockedDown(X,Y))
		Local BlockedHorizontal=(IsBlockedLeft(X,Y) Or IsBlockedRight(X,Y))
		
		TurnEntity B\Entity,0,(180*Rand(0,1))+(90*BlockedVertical),0,True
	Next
End Function

Function MoveBouncer(B.BOUNCER)
	Local Picked=EntityPick(B\Entity,0.2)
	
	If (Picked)
		TurnEntity B\Entity,0,180,0,True
	End If
	
	MoveEntity B\Entity,0,0,0.1*TICK
		
	;Wraparound
	If (EntityX(B\Entity,True)<0)
		PositionEntity B\Entity,EntityX(B\Entity,True)+MAPSIZEX,BOUNCER_Y_OFFSET,EntityZ(B\Entity,True),True
	Else
		If (EntityX(B\Entity,True)>=MAPSIZEX)
			PositionEntity B\Entity,EntityX(B\Entity,True)-MAPSIZEX,BOUNCER_Y_OFFSET,EntityZ(B\Entity,True),True
		End If
	End If
End Function

;~IDEal Editor Parameters:
;~F#7#B#F#13#24#28#2F#39#4C
;~C#Blitz3D