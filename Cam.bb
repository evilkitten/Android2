Global CAM
Global CAM_PIVOT

Const CAM_LIMIT=13

Function CreateGameCamera()
	If CAM_PIVOT
		FreeEntity CAM_PIVOT
		CAM_PIVOT=0
	End If
	CAM_PIVOT=CreatePivot()
	PositionEntity CAM_PIVOT,CAPSULEX,PLAYER_Y_OFFSET,CAPSULEY
	
	CAM=CreateCamera(CAM_PIVOT)
	CameraZoom CAM,1.0-(0.915*SPECTRUM_MODE)
	CameraViewport CAM,0,0,GraphicsHeight(),GraphicsHeight()
	CameraProjMode CAM,1+SPECTRUM_MODE
	AmbientLight 63+(64*SPECTRUM_MODE),63+(64*SPECTRUM_MODE),63+(64*SPECTRUM_MODE)
	
	TranslateEntity CAM,0,CAM_LIMIT,-(SPECTRUM_MODE*CAM_LIMIT*0.45)+(Not(SPECTRUM_MODE))
	
	RotateEntity ( CAM, 90-(30*SPECTRUM_MODE),0,0 );
	
	Local CAM_LIGHT=CreateLight(1,CAM_PIVOT)
	
	LightColor CAM_LIGHT,192+(63*SPECTRUM_MODE),192+(63*SPECTRUM_MODE),192+(63*SPECTRUM_MODE)
	RotateEntity CAM_LIGHT,135+(15*SPECTRUM_MODE),15-(15*SPECTRUM_MODE),0,True
End Function

Function UpdateCamera()
	Local X#=EntityX(PIVOT_PLAYER,True)
	Local Z#=EntityZ(PIVOT_PLAYER,True)
	
	If (Z>(MAPSIZEY-(CAM_LIMIT+SPECTRUM_MODE))) Then Z#=(MAPSIZEY-(CAM_LIMIT+SPECTRUM_MODE))
	
	If (Z<(CAM_LIMIT-(Not(SPECTRUM_MODE)))) Then Z#=(CAM_LIMIT-(Not(SPECTRUM_MODE)))
	
	PositionEntity CAM_PIVOT,X#,PLAYER_Y_OFFSET,Z-SPECTRUM_MODE,True
End Function

Function PaintChildren(Parent,Texture,AltR,AltG,AltB)
	Local Children
	Local Iter
	Local Child
	
	If EntityClass(Parent)="Mesh"
		If (SPECTRUM_MODE)
			EntityColor Parent,AltR,AltG,AltB
		Else
			EntityTexture Parent,Texture
		End If
	End If
	
	Children=CountChildren(Parent)
	If (Children)
		For Iter=1 To Children
			Child=GetChild(Parent,Iter)
			If EntityClass(Child)="Mesh"
				If (SPECTRUM_MODE)
					EntityColor Child,AltR,AltG,AltB
				Else
					EntityTexture Child,Texture
				End If
				PaintChildren(Child,Texture,AltR,AltG,AltB)
			End If
		Next
	End If
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D