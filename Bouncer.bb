Const DIRECTION_UNDEFINED=-2
Const BOUNCER_Y_OFFSET#=0.0

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
	
	BOUNCER_MASTER=CreateCone(3);LoadAnimMesh()
	
	PaintBouncerMaster
	SetBouncerPhysics
	
	;ScaleEntity BOUNCER_MASTER,0.75-(0.35*SPECTRUM_MODE),0.25+(0.15*SPECTRUM_MODE),0.5,True
	
	HideEntity BOUNCER_MASTER
	PositionEntity BOUNCER_MASTER,0,-100,0
End Function

Function SetBouncerPhysics()
	;
End Function

Function PaintBouncerMaster()
	;Local Texture=LoadTexture(VisualDir()+"Bouncer_Mat.png")
	;PaintChildren(BOUNCER_MASTER,Texture,255,0,0)
	;FreeTexture Texture
	EntityShininess BOUNCER_MASTER,0.25+(SPECTRUM_MODE*0.75)
End Function

Function SpawnBouncer(X#,Z#)
	Local B.BOUNCER=New BOUNCER
	B\Entity=CopyEntity(BOUNCER_MASTER)
	
	PositionEntity B\Entity,X#-0.5,BOUNCER_Y_OFFSET#,Z#-.5,True
	EntityPickMode B\Entity,2
	
	GhostBouncer(B)
End Function

Function FinaliseBouncerDirections()
	;Optimise this later if req'd. First check for vertical blockage, if none, check for horizontal - if none of either, bouncer motion is vertical
	
	Local B.BOUNCER
	For B=Each BOUNCER
		
		Local X#=EntityX(B\Entity,True);Int(Floor(EntityX(B\Entity,True)))
		Local Y#=EntityZ(B\Entity,True);Int(Floor(EntityZ(B\Entity,True)))
		
		X=X-0.5
		Y=Y-0.5
		
		Local BlockedVertical=(IsBlockedUp(X,Y) Or IsBlockedDown(X,Y))
		Local BlockedHorizontal=(IsBlockedLeft(X,Y) Or IsBlockedRight(X,Y))
		
		TurnEntity B\Entity,0,(180*Rand(0,1))+(90*BlockedVertical),0,True
	Next
End Function

Function MoveBouncer(B.BOUNCER)
	Local Picked=EntityPick(B\Entity,0.333)
	
	If (Picked)
		TurnEntity B\Entity,0,180,0,True
	Else
		MoveEntity B\Entity,0,0,0.1*TICK
		
		;Wraparound
		If (EntityX(B\Entity,True)<0)
			PositionEntity B\Entity,EntityX(B\Entity,True)+MAPSIZEX,BOUNCER_Y_OFFSET,EntityZ(B\Entity,True),True
		Else
			If (EntityX(B\Entity,True)>=MAPSIZEX)
				PositionEntity B\Entity,EntityX(B\Entity,True)-MAPSIZEX,BOUNCER_Y_OFFSET,EntityZ(B\Entity,True),True
			End If
		End If
	End If
End Function
;~IDEal Editor Parameters:
;~F#B#F#28
;~C#Blitz3D