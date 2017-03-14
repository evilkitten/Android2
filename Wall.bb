Global WALL_TEXTURE
Global WALL_MESH

Const WALL_FILE$="Wall"

Function WallMatFile$()
	Return MatFile(WALL_FILE)
End Function

Function BuildWallGeometries()
	BuildWallMesh
	BuildGhostMesh
	CombineGhostGeometry
	FinaliseWalls
End Function

Function BuildWallMesh()
	If (WALL_MESH)
		FreeEntity WALL_MESH
		WALL_MESH=0
	End If
	WALL_MESH=BuildWallGeometry()
End Function

Function FinaliseWalls()
	PaintWall
	SetWallPhysics
End Function

Function PaintWall()
	If (WALL_TEXTURE)
		FreeTexture WALL_TEXTURE
		WALL_TEXTURE=0
	End If
		
	If (Not(SPECTRUM_MODE))
		WALL_TEXTURE=AcquireTextureMap(WallMatFile())
		ScaleTexture WALL_TEXTURE,1/MAPSIZEX,1.0
		EntityTexture WALL_MESH,WALL_TEXTURE
	Else
		EntityColor WALL_MESH,255,255,255
		EntityShininess WALL_MESH,1.0
		EntityFX WALL_MESH,4
	End If
End Function

Function SetWallPhysics()
	EntityPickMode WALL_MESH,2,True
End Function

Function BuildWallGeometry(Parent=0,AddRoof=True)
	Local Mesh=CreateMesh(Parent)
	Local Surf=CreateSurface(Mesh)
	
	Dim WALL_FLAG%(MAPSIZEX,MAPSIZEY);
	
	Local W=MAPSIZEX-1
	Local H=MAPSIZEY-1
	
	Local i%,j%
	Local x%,y%
	Local x0%,y0%
	Local x1%,y1%
	
	Local v
	
	For j = 0 To H
		For i = 0 To W
			; this cell has a bottom wall
			If ((HAS_WALL(i,j) And MAP_BOTTOM_BOUND))
				; if this wall hasn't already been built
				If ((WALL_FLAG(i,j) And MAP_BOTTOM_BOUND) = 0)
					x0 = i : x1 = i : y0 = j : y1 = j;
					; find last cell that has this wall
					For x = x0 To W
						If ((HAS_WALL(x,j) And MAP_BOTTOM_BOUND)=0) Then Exit;
						WALL_FLAG(x,j) = WALL_FLAG(x,j) Or MAP_BOTTOM_BOUND : x1=x;
					Next;
					
					; build the wall.
					v = AddVertex	(Surf, x0  , 1,y0 ) : VertexNormal(Surf, v  ,  0, 0,-1);
					AddVertex	(Surf, x1+1, 1,y0   ) : VertexNormal(Surf, v+1,  0, 0,-1);
					AddVertex	(Surf, x1+1, 0,y0   ) : VertexNormal(Surf, v+2,  0, 0,-1);
					AddVertex	(Surf, x0  , 0,y0     ) : VertexNormal(Surf, v+3,  0, 0,-1);
					AddTriangle	(Surf, v,v+1,v+2);
					AddTriangle	(Surf, v,v+2,v+3);
				EndIf;
			EndIf;
			
			; this cell has a top wall
			If ((HAS_WALL(i,j) And MAP_TOP_BOUND))
				If ((WALL_FLAG(i,j) And MAP_TOP_BOUND) = 0)
					x0 = i : x1 = i : y0 = j : y1 = j;
					For x = x0 To W
						If ((HAS_WALL(x,j) And MAP_TOP_BOUND)=0) Then Exit;
						WALL_FLAG(x,j) = WALL_FLAG(x,j) Or MAP_TOP_BOUND : x1=x;
					Next;
					v = AddVertex	(Surf, x1+1, 1,y0+1) : VertexNormal(Surf, v  ,  0, 0,+1);
					AddVertex	(Surf, x0  , 1,y0+1) : VertexNormal(Surf, v+1,  0, 0,+1);
					AddVertex	(Surf, x0  , 0,y0+1) : VertexNormal(Surf, v+2,  0, 0,+1);
					AddVertex	(Surf, x1+1, 0,y0+1) : VertexNormal(Surf, v+3,  0, 0,+1);
					AddTriangle	(Surf, v,v+1,v+2);
					AddTriangle	(Surf, v,v+2,v+3);
				EndIf;
			EndIf;
			; this cell has a left wall
			If ((HAS_WALL(i,j) And MAP_LEFT_BOUND))
				If ((WALL_FLAG(i,j) And MAP_LEFT_BOUND) = 0)
					x0 = i : x1 = i : y0 = j : y1 = j;
					For y = y0 To H
						If ((HAS_WALL(i,y) And MAP_LEFT_BOUND)=0) Then Exit;
						WALL_FLAG(i,y) = WALL_FLAG(i,y) Or MAP_LEFT_BOUND : y1=y;
					Next;
					v = AddVertex	(Surf, x0, 1,y1+1) : VertexNormal(Surf, v  , -1, 0, 0);
					AddVertex	(Surf, x0, 1,y0  ) : VertexNormal(Surf, v+1, -1, 0, 0);
					AddVertex	(Surf, x0, 0,y0  ) : VertexNormal(Surf, v+2, -1, 0, 0);
					AddVertex	(Surf, x0, 0,y1+1) : VertexNormal(Surf, v+3, -1, 0, 0);
					AddTriangle	(Surf, v,v+1,v+2);
					AddTriangle	(Surf, v,v+2,v+3);
				EndIf;
			EndIf;
			; this cell has a right wall
			If ((HAS_WALL(i,j) And MAP_RIGHT_BOUND))
				If ((WALL_FLAG(i,j) And MAP_RIGHT_BOUND) = 0)
					x0 = i : x1 = i : y0 = j : y1 = j;
					For y = y0 To H
						If ((HAS_WALL(i,y) And MAP_RIGHT_BOUND)=0) Then Exit;
						WALL_FLAG(i,y) = WALL_FLAG(i,y) Or MAP_RIGHT_BOUND: y1=y;
					Next
					v = AddVertex	(Surf, x0+1, 1,y0 ) : VertexNormal(Surf, v  , +1, 0, 0);
					AddVertex	(Surf, x0+1, 1,y1+1) : VertexNormal(Surf, v+1, +1, 0, 0);
					AddVertex	(Surf, x0+1, 0,y1+1) : VertexNormal(Surf, v+2, +1, 0, 0);
					AddVertex	(Surf, x0+1, 0,y0  ) : VertexNormal(Surf, v+3, +1, 0, 0);
					AddTriangle	(Surf, v,v+1,v+2);
					AddTriangle	(Surf, v,v+2,v+3);
				EndIf
			EndIf
		Next
	Next
	
	Dim WALL_FLAG(0,0)
	Dim HAS_WALL(0,0)
	
	If (AddRoof) Then AddRoofGeometry(Surf)
	
	UpdateNormals Mesh
	
	CalculateUVs(Surf)
	
	Return Mesh
	
End Function

Function AddRoofGeometry(Surf)
	Local W=MAPSIZEX-1
	Local H=MAPSIZEY-1
	
	Local LengthH
	Local LengthV
	
	Local v
	
	Local X
	Local Y
	
	Local i
	Local j
	
	Local x0,x1
	Local y0,y1
	
	; build roofs
	Dim WALL_FLAG(W+1,H+1) ; reset wall flags and reuse for roofs
	For j = 0 To H
		For i = 0 To W
			If (PeekArrayValue(i,j)=MAP_WALL)
				If (WALL_FLAG(i,j)=MAP_GROUND)
					LengthH = 0;
					LengthV = 0;
					; check farthest available cell to the right
					x0 = i : y0 = j;
					X = x0 : Y = y0;
					For X = x0 To W
						If (PeekArrayValue(X,j)<>MAP_WALL) Then Exit;
						If (WALL_FLAG(X,j)<>MAP_GROUND) Then Exit;
						x1 = X;
					Next;
					LengthH = x1-x0;
					; check farthest available to the bottom
					For Y = y0 To H
						If (PeekArrayValue(i,Y)<>MAP_WALL) Then Exit;
						If (WALL_FLAG(i,Y)<>MAP_GROUND) Then Exit;
						y1 = Y;
					Next;
					LengthV = y1-y0
					
					; according to the longest segment build a quad
					If (LengthH>=LengthV)
						; horizontal
						For X = x0 To x1
							; tag the cell as built
							WALL_FLAG(X,j) = 1
						Next
						; from x0 to x1+1 (+1 because we need the right segment of the cell)
						v = AddVertex	(Surf, x0,1,y0+1) : VertexNormal(Surf, v  ,  0,+1, 0);
						AddVertex	(Surf, x1+1,1,y0+1) : VertexNormal(Surf, v+1,  0,+1, 0);
						AddVertex	(Surf, x1+1,1,y0    ) : VertexNormal(Surf, v+2,  0,+1, 0);
						AddVertex	(Surf, x0    ,1,y0    ) : VertexNormal(Surf, v+3,  0,+1, 0);
						AddTriangle	(Surf, v,v+1,v+2);
						AddTriangle	(Surf, v,v+2,v+3);
					Else
						; vertical
						For Y = y0 To y1
							; tag the cell as built
							WALL_FLAG(i,Y) = 1
						Next
						v = AddVertex	(Surf, x0  , 1,y1+1) : VertexNormal(Surf, v  ,  0,+1, 0);
						AddVertex	(Surf, x0+1, 1,y1+1) : VertexNormal(Surf, v+1,  0,+1, 0);
						AddVertex	(Surf, x0+1, 1,y0  ) : VertexNormal(Surf, v+2,  0,+1, 0);
						AddVertex	(Surf, x0  , 1,y0  ) : VertexNormal(Surf, v+3,  0,+1, 0);
						AddTriangle	(Surf, v,v+1,v+2);
						AddTriangle	(Surf, v,v+2,v+3);
					EndIf;
				EndIf
			EndIf;
		Next;
	Next;
	
	Dim WALL_FLAG(0,0)
	
End Function

Function CalculateUVs(Surface)
	Local MaxTri=CountTriangles(Surface)
	Local Triangle
	Local Vertex
	
	Local XMax#
	Local XMin#
	Local YMax#
	Local YMin#
	Local ZMax#
	Local ZMin#
	
	Local TNX#
	Local TNY#
	Local TNZ#
	
	Local v
	
	For Triangle= 0 To MaxTri-1
		XMin=9999
		YMin=9999
		ZMin=9999
		
		XMax=-9999
		YMax=-9999
		ZMax=-9999
		
		;First pass : Determine Normals and size of triangle-----------------------------------------------------------------------------
		
		For Vertex=0 To 2
			v=TriangleVertex(Surface,Triangle,Vertex)
			If (VertexX(Surface,v)<XMin) Then XMin=VertexX(Surface,v)
			If (VertexX(Surface,v)>XMax) Then XMax=VertexX(Surface,v)
			
			If (VertexY(Surface,v)<YMin) Then YMin=VertexY(Surface,v)
			If (VertexY(Surface,v)>YMax) Then YMax=VertexY(Surface,v)
			
			If (VertexZ(Surface,v)<ZMin) Then ZMin=VertexZ(Surface,v)
			If (VertexZ(Surface,v)>ZMax) Then ZMax=VertexZ(Surface,v)
		Next
		
		;The good thing is, all vertices of a triangle will all have equal Normals since walls are straight and aligned with axes.
		TNX=VertexNX(Surface,v)
		TNY=VertexNY(Surface,v)
		TNZ=VertexNZ(Surface,v)
		
		Local LengthX#=XMax-XMin
		Local LengthY#=YMax-YMin
		Local LengthZ#=ZMax-ZMin
		
		;Second Pass : populate UV----------------------------------------------------------------------------------------------------------
		
		Local TexU#
		Local TexV#
		
		For Vertex=0 To 2
			v=TriangleVertex(Surface,Triangle,Vertex)
			If (TNY>0)
				;Roof
				TexU=(VertexX(Surface,v)-XMin)/LengthX
				TexV=(VertexZ(Surface,v)-ZMin)/LengthZ
				
				TexV=(TexV*0.5)+0.5
				
			Else
				If (TNX<0)
					;Bottom
					TexU=(VertexX(Surface,v)-XMin)/LengthX		
					TexV=1.0 - ((VertexY(Surface,v)-YMin)/LengthY)
					
					TexV=TexV*0.5
					
				Else
					If (TNX>0)
						;Top
						TexU=1.0 - ( (VertexX(Surface,v)-XMin)/LengthX )
						TexV=1.0 - ((VertexY(Surface,v)-YMin)/LengthY)
						
						TexV=TexV*0.5
						
					Else
						If(TNZ>0)
							;Right
							TexU=(VertexZ(Surface,v)-ZMin)/LengthZ
							TexV=1.0 - ((VertexY(Surface,v)-YMin)/LengthY)
							
							TexV=TexV*0.5
							
						Else
							If (TNZ<0)
								;Left
								TexU=1.0 - ( (VertexZ(Surface,v)-ZMin)/LengthZ)
								TexV=1.0 - ((VertexY(Surface,v)-YMin)/LengthY)
								
								TexV=TexV*0.5
								
							Else
								TexU=0
								TexV=0
							End If
						End If
					End If
					
				End If
			End If
			
			VertexTexCoords Surface,v,TexU,TexV
			
		Next
	Next
End Function	
;~IDEal Editor Parameters:
;~F#5#9#10#18#1D#2E#32#99#E8
;~C#Blitz3D