Global GROUND_TEXTURE
Global GROUND_PLANE
Const GROUND_BASELINE_Y#=0.0

Function InitialiseGroundFiles()
	;UnPackAsset(PACK_GROUND_ANIM_START,PACK_GROUND_ANIM_LENGTH)
	UnPackAsset(PACK_GROUND_MAT_START,PACK_GROUND_MAT_LENGTH)
End Function

Function UnInitialiseGroundFiles()
	If (TEST) Then Return
	
	DeleteFile GroundMatFile()
End Function

Function InitialiseGround()
	InitialiseGroundFiles
	BuildGround
	PaintGround
	UnInitialiseGroundFiles
End Function

Function GroundMatFile$()
	Return MatFile(MAP_GROUND_NAME)
End Function

Function BuildGround()
	If (GROUND_PLANE)
		;FreeEntity GROUND_PLANE
		;GROUND_PLANE=0
		Return
	End If
	
	GROUND_PLANE=CreatePlane(1)
	PositionEntity GROUND_PLANE,0,GROUND_BASELINE_Y,0
End Function

Function PaintGround()
	If (GROUND_TEXTURE)
		;FreeTexture GROUND_TEXTURE
		;GROUND_TEXTURE=0
		Return
	End If
	
	GROUND_TEXTURE=AcquireTextureMap(GroundMatFile())
	ScaleTexture GROUND_TEXTURE,MAPSIZEX*0.1,MAPSIZEY*0.1
	PaintChildren(GROUND_PLANE,GROUND_TEXTURE,0,255,0)
	FreeTexture GROUND_TEXTURE
	GROUND_TEXTURE=0
End Function
;~IDEal Editor Parameters:
;~F#4#9#F#16#1A#25
;~C#Blitz3D