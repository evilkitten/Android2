Global GROUND_TEXTURE
Global GROUND_PLANE
Const GROUND_BASELINE_Y#=0.0

Const GROUND_FILE$="Ground"

Function InitialiseGround()
	BuildGround
	PaintGround
End Function

Function GroundMatFile$()
	Return MatFile(GROUND_FILE)
End Function

Function BuildGround()
	If (GROUND_PLANE)
		FreeEntity GROUND_PLANE
		GROUND_PLANE=0
	End If
	
	GROUND_PLANE=CreatePlane(1)
	PositionEntity GROUND_PLANE,0,GROUND_BASELINE_Y,0
End Function

Function PaintGround()
	If GROUND_TEXTURE
		FreeTexture GROUND_TEXTURE
		GROUND_TEXTURE=0
	End If
	
	GROUND_TEXTURE=AcquireTextureMap(GroundMatFile())
	ScaleTexture GROUND_TEXTURE,MAPSIZEX*0.1,MAPSIZEY*0.1
	PaintChildren(GROUND_PLANE,GROUND_TEXTURE,0,255,0)
	FreeTexture GROUND_TEXTURE
	GROUND_TEXTURE=0
End Function
;~IDEal Editor Parameters:
;~F#6#B#F#19
;~C#Blitz3D