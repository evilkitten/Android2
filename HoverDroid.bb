Type HOVERDROID
	Field Entity
End Type

Const HOVERDROID_Y_OFFSET#=0.5
Const HOVERDROID_FILE$="Hoverdroid"

Global HOVERDROID_MASTER

Function HoverdroidAnimFile$()
	Return AnimFile(HOVERDROID_FILE)
End Function

Function HoverdroidMatFile$()
	Return MatFile(HOVERDROID_FILE)
End Function

Function BuildHoverdroidMaster()
	If (HOVERDROID_MASTER)
		FreeEntity HOVERDROID_MASTER
		HOVERDROID_MASTER=0
	End If
	
	HOVERDROID_MASTER=LoadAnimMesh(HoverdroidAnimFile())
	
	PaintHoverdroidMaster
	SetHoverdroidPhysics
	
;	ScaleEntity HOVERDROID_MASTER,1.0,(0.5+(0.5*SPECTRUM_MODE)),1.0,True
	
	HideEntity HOVERDROID_MASTER
	PositionEntity HOVERDROID_MASTER,0,-140,0
End Function

Function SetHoverdroidPhysics()
	;
End Function

Function PaintHoverdroidMaster()
	Local Texture=AcquireTextureMap(HoverdroidMatFile())
	PaintChildren(HOVERDROID_MASTER,Texture,0,0,255)
	FreeTexture Texture
	EntityShininess HOVERDROID_MASTER,0.25+(SPECTRUM_MODE*0.75)
End Function

Function SpawnHoverdroid(X#,Z#)
	Local H.HOVERDROID=New HOVERDROID
	H\Entity=CopyEntity(HOVERDROID_MASTER)
;	EntityBox H\Entity,0.5,0,0.5,0.6,1.0,0.6
	PositionEntity H\Entity,X#-0.5,GROUND_BASELINE_Y#+HOVERDROID_Y_OFFSET#,Z#-0.5,True
	RotateEntity H\Entity,0,Rand(1,4)*90,0,True
	EntityPickMode H\Entity,3
	AddShadow(H\Entity)
	GhostHoverdroid(H)
End Function

Function MoveHoverdroid(H.HOVERDROID)
	Local Picked=EntityPick(H\Entity,0.5)
	
	If (Picked)
		Local Direction=Rand(1,3)
		TurnEntity H\Entity,0,Floor(90.0*Direction),0,True
		
	Else
		
		MoveEntity H\Entity,0,0,0.1*TICK
	End If
	
	;Wraparound
	If (EntityX(H\Entity,True)<0)
		PositionEntity H\Entity,EntityX(H\Entity,True)+MAPSIZEX,GROUND_BASELINE_Y#+HOVERDROID_Y_OFFSET,EntityZ(H\Entity,True),True
	Else
		If (EntityX(H\Entity,True)>=MAPSIZEX)
			PositionEntity H\Entity,EntityX(H\Entity,True)-MAPSIZEX,GROUND_BASELINE_Y#+HOVERDROID_Y_OFFSET,EntityZ(H\Entity,True),True
		End If
	End If
	
End Function
;~IDEal Editor Parameters:
;~F#0#9#D#11#22#26#2D#38
;~C#Blitz3D