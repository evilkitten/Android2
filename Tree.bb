Type TREE
	Field Entity
	Field G.GHOST
End Type

Const TREE_FILE$="Tree"

Global TREE_MASTER

Function InitialiseTreeFiles()
	;UnPackAsset(PACK_TREE_ANIM_START,PACK_TREE_ANIM_LENGTH)
	UnPackAsset(PACK_TREE_MAT_START,PACK_TREE_MAT_LENGTH)
End Function

Function UnInitialiseTreeFiles()
	If (TEST) Then Return	
	
	;DeleteFile TreeAnimFile()
	DeleteFile TreeMatFile()
End Function

;Function TreeAnimFile$()
;	Return AnimFile(TREE_FILE)
;End Function

Function TreeMatFile$()
	Return MatFile(TREE_FILE)
End Function

Function BuildTreeMaster()
	If (TREE_MASTER)
		;FreeEntity TREE_MASTER
		;TREE_MASTER=0
		Return
	End If
	
	InitialiseTreeFiles
	
	TREE_MASTER=CreateCone(3,False)
	EntityColor TREE_MASTER,16,16,16
	ScaleMesh TREE_MASTER,0.2,1,0.2
	Local Child=CreateCylinder(8-(SPECTRUM_MODE*4),True,TREE_MASTER)
	ScaleMesh Child,0.5,0.5,0.5
	PositionMesh TREE_MASTER,0,1,0
	PositionMesh Child,0,2,0
	
	PaintTreeMaster
	SetTreePhysics
	
;	ScaleEntity TREE_MASTER,1.0,(0.5+(0.5*SPECTRUM_MODE)),1.0,True
	
	UnInitialiseTreeFiles
	
	
	HideEntity TREE_MASTER
	PositionEntity TREE_MASTER,0,-180,0
End Function

Function PaintTreeMaster()
	Local Texture=AcquireTextureMap(TreeMatFile())
	Local Child=GetChild(TREE_MASTER,1)
	
	If (SPECTRUM_MODE)
		ScaleTexture Texture,16,16
	End If
	
	EntityTexture Child,Texture
	
	FreeTexture Texture
	
	EntityShininess TREE_MASTER,SPECTRUM_MODE
End Function

Function SetTreePhysics()
	EntityPickMode TREE_MASTER,3
End Function

Function SpawnTree(X#,Z#)
	Local T.TREE=New TREE
	T\Entity=CopyEntity(TREE_MASTER)
	
	NameEntity T\Entity,Str(Handle(T))
	
	EntityBox T\Entity,0,0,0,0.5,1.0,0.5
	PositionEntity T\Entity,X#-0.5,GROUND_BASELINE_Y#,Z#-0.45,True
	RotateEntity T\Entity,0,Rnd(360)*(Not(SPECTRUM_MODE)),0,True
	
	AddShadow(T\Entity)
	GhostTree(T)
End Function

Function RemoveTree(T.TREE)
	RemoveGhost(T\G)
	FreeEntity T\Entity
	Delete T
End Function
;~IDEal Editor Parameters:
;~F#9#E#19#1D#3A
;~C#Blitz3D