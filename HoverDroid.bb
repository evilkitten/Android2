Type HOVERDROID
	Field Entity
	Field G.GHOST
End Type

Const HOVERDROID_Y_OFFSET#=0.5;0.25;

Global HOVERDROID_MASTER

Function InitialiseHoverdroidFiles()
	UnPackAsset(PACK_HOVERDROID_ANIM_START,PACK_HOVERDROID_ANIM_LENGTH)
	UnPackAsset(PACK_HOVERDROID_MAT_START,PACK_HOVERDROID_MAT_LENGTH)
End Function

Function UnInitialiseHoverdroidFiles()
	If (TEST) Then Return
	DeleteFile HoverdroidAnimFile()
	DeleteFile HoverdroidMatFile()
End Function

Function HoverdroidAnimFile$()
	Return AnimFile(MAP_HOVERDROID_NAME)
End Function

Function HoverdroidMatFile$()
	Return MatFile(MAP_HOVERDROID_NAME)
End Function

Function BuildHoverdroidMaster()
	If (HOVERDROID_MASTER)
		;FreeEntity HOVERDROID_MASTER
		;HOVERDROID_MASTER=0
		Return
	End If
	
	InitialiseHoverdroidFiles
	
	HOVERDROID_MASTER=LoadAnimMesh(HoverdroidAnimFile())
	
	PaintHoverdroidMaster
	SetHoverdroidPhysics
	
	UnInitialiseHoverdroidFiles
	
;	ScaleEntity HOVERDROID_MASTER,1.0,(0.5+(0.5*SPECTRUM_MODE)),1.0,True
	
	HideEntity HOVERDROID_MASTER
	PositionEntity HOVERDROID_MASTER,0,-140,0
	
End Function

Function SetHoverdroidPhysics()
	EntityPickMode HOVERDROID_MASTER,1;3
End Function

Function PaintHoverdroidMaster()
	Local Texture=AcquireTextureMap(HoverdroidMatFile())
	PaintChildren(HOVERDROID_MASTER,Texture,0,0,255)
	FreeTexture Texture
	EntityShininess HOVERDROID_MASTER,0.25+(SPECTRUM_MODE*0.75)
End Function

Function SpawnHoverdroid(X,Z)
	Local H.HOVERDROID=New HOVERDROID
	H\Entity=CopyEntity(HOVERDROID_MASTER)
	
	NameEntity H\Entity,Str(Handle(H))
	
	;EntityBox H\Entity,0,0.1,0,0.1,0.5,0.5
	;EntityBox H\Entity,0.5,0,0.5,0.6,1.0,0.6
	PositionEntity H\Entity,X-0.5,GROUND_BASELINE_Y#+(HOVERDROID_Y_OFFSET#*SPECTRUM_MODE),Z-0.5,True
	RotateEntity H\Entity,0,Rand(1,4)*90,0,True
	
	AddShadow(H\Entity)
	GhostHoverdroid(H)
End Function

Function MoveHoverdroid(H.HOVERDROID)
	Local Picked=EntityPick(H\Entity,0.25);0.5;0.3
	
	If (Picked)
		;Player Collision
		If (Picked=PIVOT_PLAYER)
			If (EntityDistance(Picked,H\Entity)<=1.2)
				PlayerCollision("Hoverdroid",H\Entity)
			End If
		Else
			
			Local Direction=Rand(1,3)
			TurnEntity H\Entity,0,Floor(90.0*Direction),0,True
		End If	
	End If
	
		MoveEntity H\Entity,0,0,(GAME_MOVEMENT_SPEED*TICK)
		
	;Wraparound
	If (EntityX(H\Entity,True)<0)
		PositionEntity H\Entity,EntityX(H\Entity,True)+MAPSIZEX,GROUND_BASELINE_Y#+HOVERDROID_Y_OFFSET,EntityZ(H\Entity,True),True
	Else
		If (EntityX(H\Entity,True)>=MAPSIZEX)
			PositionEntity H\Entity,EntityX(H\Entity,True)-MAPSIZEX,GROUND_BASELINE_Y#+HOVERDROID_Y_OFFSET,EntityZ(H\Entity,True),True
		End If
	End If
End Function

Function RemoveHoverdroid(H.HOVERDROID)
	RemoveGhost(H\G)
	FreeEntity H\Entity
	Delete H
End Function
;~IDEal Editor Parameters:
;~F#0#9#E#14#18#1C#33#37#3E#4D#69
;~C#Blitz3D