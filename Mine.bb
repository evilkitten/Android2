Type MINE
	Field X
	Field Y
End Type

Const MINE_FILE$="Mine"
Const MINE_SIZE#=1.0
Const MINE_Y_OFFSET#=0.1
Const MINE_COLOUR_RED=-65536
Const MINE_COLOUR_YELLOW=-256

Global MINE_MASTER

Global MINE_MAT
Global MINE_MAT_UPDATE_DURATION=500
Global MINE_MAT_UPDATE_LAST
Global MINE_SWITCH

Function UnInitialiseMineFiles()
	If (TEST) Then Return	
	
;	DeleteFile MineAnimFile()
	DeleteFile MineMatFile()
End Function

Function InitialiseMineFiles()
	;UnPackAsset(PACK_MINE_ANIM_START,PACK_MINE_ANIM_LENGTH)
	UnPackAsset(PACK_MINE_MAT_START,PACK_MINE_MAT_LENGTH)
End Function

;Function MineAnimFile$()
;	Return AnimFile(MINE_FILE)
;End Function

Function MineMatFile$()
	Return MatFile(MINE_FILE)
End Function

Function BuildMineMaster()
	If (MINE_MASTER)
		RemoveMines
	End If
	
	InitialiseMineFiles
	
	MINE_MASTER=CreateMesh()
	Local Surface=CreateSurface(MINE_MASTER)
	
	If (Not(MINE_MAT))
		MINE_MAT=SetMineMatAnimation()
	End If
	
	UnInitialiseMineFiles
	
	PositionEntity MINE_MASTER,0,0,0,True
End Function

Function SpawnMine(X,Z)
	Local M.MINE=New MINE
	M\X=X
	M\Y=Z
	AddMineGeometry(M)
	GhostMine(M)
End Function

Function FinaliseMines()
	NameEntity MINE_MASTER,MAP_MINE_NAME
End Function

Function AddMineGeometry(M.MINE)
	Local Mesh=CreateQuad()
	ScaleMesh Mesh,MINE_SIZE,MINE_SIZE,0.01
	RotateMesh Mesh,90,0,0
	AddMeshToSurface(Mesh,MINE_MASTER,M\X-0.5,GROUND_BASELINE_Y+MINE_Y_OFFSET,M\Y-0.5)
	FreeEntity Mesh
End Function

Function SetMineMatAnimation()
	Local Texture
	
	If (SPECTRUM_MODE)
		Texture=CreateTexture(4,4,257,2)
		Local B1=TextureBuffer(Texture,0)
		Local B2=TextureBuffer(Texture,1)
		
		LockBuffer B1
		LockBuffer B2
		
		Local X
		Local Y
		
		For Y=0 To 3
			For X=0 To 3
				If (((X Mod 3)<>0) And ((Y Mod 3)<>0))
					WritePixelFast X,Y,MINE_COLOUR_RED,B1
					WritePixelFast X,Y,MINE_COLOUR_YELLOW,B2
				Else
					WritePixelFast X,Y,MINE_COLOUR_YELLOW,B1
					WritePixelFast X,Y,MINE_COLOUR_RED,B2
				End If
			Next
		Next
		
		UnlockBuffer B2
		UnlockBuffer B1
		
	Else
		Texture=AcquireAnimTextureMap(MineMatFile())
	End If
	
	Return Texture
End Function

Function UpdateMines()
	If ((MilliSecs()-MINE_MAT_UPDATE_LAST)>MINE_MAT_UPDATE_DURATION)
		MINE_MAT_UPDATE_LAST=MilliSecs()
		MINE_SWITCH=Not MINE_SWITCH
		PaintMine()
	End If
End Function

Function MineExplosion()
	RuntimeError("BOOM! : Mine_MineExplosion method called")
End Function

Function PaintMine()
	EntityTexture MINE_MASTER,MINE_MAT,MINE_SWITCH
	EntityFX MINE_MASTER,MINE_SWITCH
End Function

Function RemoveMines()
	Local Surface=GetSurface(MINE_MASTER,1)
	ClearSurface Surface,True,True
	
;	FreeEntity MINE_MASTER
;	MINE_MASTER=0
End Function
;~IDEal Editor Parameters:
;~F#0#12#19#22#26#39#41#45#4D#71#7D#82
;~C#Blitz3D