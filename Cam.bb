Global CAM
Global CAM_PIVOT

Global CAM_FADE
Global CAM_FADE_VALUE#

Global CAM_FADE_MESH

Global FRAME

Global MAX_POLYGONS

Const TICKRATE#=16.6667
Global TICK

Const CAM_FADE_RATE#=0.0056 ;3 seconds at 60fps

Const CAM_FADE_STATE_TRANSP=0
Const CAM_FADE_STATE_OPAQUE=1
Const CAM_FADE_STATE_CLEARING=2
Const CAM_FADE_STATE_HIDING=3

Const CAM_LIMIT=13

Function CreateGameCamera()
	If (CAM_PIVOT)
		;FreeEntity CAM_PIVOT
		;CAM_PIVOT=0
		Return
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
	
	InitialiseFadeMesh()
End Function

Function UpdateCamera()
	Local X#=EntityX(PIVOT_PLAYER,True)
	Local Z#=EntityZ(PIVOT_PLAYER,True)
	
	If (Z>(MAPSIZEY-(CAM_LIMIT+SPECTRUM_MODE))) Then Z#=(MAPSIZEY-(CAM_LIMIT+SPECTRUM_MODE))
	
	If (Z<(CAM_LIMIT-(Not(SPECTRUM_MODE)))) Then Z#=(CAM_LIMIT-(Not(SPECTRUM_MODE)))
	
	PositionEntity CAM_PIVOT,X#,PLAYER_Y_OFFSET,Z-SPECTRUM_MODE,True
End Function

Function DisplayOverlay()
	If (TrisRendered()>MAX_POLYGONS)
		MAX_POLYGONS=TrisRendered()
	End If
	
	Text 0,0,"Polygons "+Str(MAX_POLYGONS)
	Text 0,20,"Frame "+Str(FRAME)
	
	If (GAME_PAUSE)
		Text 0,40,"PAUSE"
	End If
End Function

Function Render()
	UpdateFade
	UpdateCamera
	UpdateWorld
	RenderWorld
	FinaliseRender
End Function

Function FinaliseRender()
	DisplayOverlay
	
	If ((MilliSecs()-TIMESTAMPTEST)>TICKRATE)
		TIMESTAMPTEST=MilliSecs()
		TICK=True
		If (Not(GAME_PAUSE))
			FRAME=FRAME+1
		End If
	Else
		TICK=False
	End If
	
	Flip (False);
End Function
	

Function UpdateFade()
	Local UpdateFadeAlpha=False
	
	Select (CAM_FADE)
		Case CAM_FADE_STATE_CLEARING:
			CAM_FADE_VALUE#=CAM_FADE_VALUE#-CAM_FADE_RATE#
			If (CAM_FADE_VALUE#<0.0)
				CAM_FADE_VALUE#=0.0
				CAM_FADE=CAM_FADE_STATE_TRANSP
				HideEntity CAM_FADE_MESH
			Else
				UpdateFadeAlpha=True
			End If
			
		Case CAM_FADE_STATE_HIDING:
			UpdateFadeAlpha=True
			CAM_FADE_VALUE#=CAM_FADE_VALUE#+CAM_FADE_RATE#
			If (CAM_FADE_VALUE#>1.0)
				CAM_FADE_VALUE#=1.0
				CAM_FADE=CAM_FADE_STATE_OPAQUE
			End If
			
		Default:
				;Currently not fading. Ignore
	End Select
	
	If (UpdateFadeAlpha)
		EntityAlpha CAM_FADE_MESH,CAM_FADE_VALUE
	End If
End Function

Function SetFadeOpaque(R=0,G=0,B=0)
	EntityColor CAM_FADE_MESH,R,G,B
	
	If (CAM_FADE=CAM_FADE_STATE_TRANSP)
		ShowEntity CAM_FADE_MESH
		CAM_FADE=CAM_FADE_STATE_HIDING
	End If
End Function

Function SetFadeTransparent()
	If (CAM_FADE=CAM_FADE_STATE_OPAQUE)
		CAM_FADE=CAM_FADE_STATE_CLEARING
	End If
End Function

Function PaintChildren(Parent,Texture,AltR,AltG,AltB)
	Local Children
	Local Iter
	Local Child
	
	If ((EntityClass(Parent)="Mesh") Or (EntityClass(Parent)="Plane"))
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
			If (EntityClass(Child)="Mesh")
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

Function CreateQuad(Parent=False)
	Local Mesh=CreateMesh(Parent)
	Local Surface=CreateSurface(Mesh)
	
	AddVertex(Surface,-0.5,0.5,0,1,0)
	VertexColor Surface,0,255,255,255
	AddVertex(Surface,0.5,0.5,0,0,0)
	VertexColor Surface,1,255,255,255
	AddVertex(Surface,-0.5,-0.5,0,1,1)
	VertexColor Surface,2,255,255,255
	AddVertex(Surface,0.5,-0.5,0,0,1)
	VertexColor Surface,3,255,255,255	
	AddTriangle(Surface,0,1,2)
	AddTriangle(Surface,3,2,1)
	
	UpdateNormals Mesh
	
	Return Mesh
End Function

Function AddMeshToSurface(SourceMesh, DestinationMesh,X#=0.0,Y#=0.0,Z#=0.0)
	Local Vz#
	Local Vx#
	Local Vy#
			
	Local VNx#
	Local VNy#
	Local VNz#
	
	Local Vu#
	Local Vv#
	Local Vw#
	Local VR
	Local VG
	Local VB
	
	Local SourceSurface=GetSurface(SourceMesh,1)
	Local DestinationSurface=GetSurface(DestinationMesh,1)
	Local MaxVertixces=CountVertices(SourceSurface)
	Local DestinationVertices=CountVertices(DestinationSurface)
	
	
	Local Itervertices
			
	For Itervertices = 0 To MaxVertixces-1
		Vx = VertexX#(SourceSurface, Itervertices)+X#
		Vy = VertexY#(SourceSurface, Itervertices)+Y#
		Vz = VertexZ#(SourceSurface, Itervertices)+Z#
		Vu#  = VertexU#(SourceSurface, Itervertices)
		Vv#  = VertexV#(SourceSurface, Itervertices)		
		Vw#  = VertexW#(SourceSurface, Itervertices)
		
		Local v=AddVertex(DestinationSurface, Vx#, Vy#, Vz#, Vu#, Vv#, Vw#)
		VertexNormal(DestinationSurface, v, VNx#, VNy#, VNz#)
		VertexColor(DestinationSurface, v, VR, VG, VB) 
	Next
	
	Local V0
	Local V1
	Local V2
		
	Local SourceTriangles
	Local IterTriangles
		
	; Copy all triangles from the source surface to the Destinationination surface.	
	SourceTriangles  = CountTriangles(SourceSurface)
	For IterTriangles = 0 To SourceTriangles-1
		
		V0 = TriangleVertex(SourceSurface, IterTriangles, 0)
		V1 = TriangleVertex(SourceSurface, IterTriangles, 1)
		V2 = TriangleVertex(SourceSurface, IterTriangles, 2)
		
		AddTriangle(DestinationSurface, V0+DestinationVertices, V1+DestinationVertices, V2+DestinationVertices)
			
	Next
End Function
;~IDEal Editor Parameters:
;~F#18#35#66#85#8E#94#B1#C5
;~C#Blitz3D