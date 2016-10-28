Global GROUND_TEXTURE
Global GROUND_PLANE

Function BuildGround()
	If (GROUND_PLANE)
		FreeEntity GROUND_PLANE
		GROUND_PLANE=0
	End If
	
	GROUND_PLANE=CreatePlane(1)
		
	If GROUND_TEXTURE
		FreeTexture GROUND_TEXTURE
		GROUND_TEXTURE=0
	End If
	
	If (Not(SPECTRUM_MODE))
		GROUND_TEXTURE=LoadTexture("F:\Resources\Dark Matter\Textures\BMP\Natural\grass2.BMP")
		ScaleTexture GROUND_TEXTURE,MAPSIZEX*0.1,MAPSIZEY*0.1
		EntityTexture GROUND_PLANE,GROUND_TEXTURE;EntityColor pp,0,255,0
		FreeTexture GROUND_TEXTURE
		GROUND_TEXTURE=0
	Else
		EntityColor GROUND_PLANE,0,255,0
	End If
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D