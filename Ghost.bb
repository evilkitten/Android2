Global GHOST_GEOMETRY
Global GHOST_WIDTH#
Const GHOST_MAP_PROPORTION#=0.25

Type GHOST
	Field EntityL
	Field EntityR
End Type

Function GhostEntity.GHOST(Entity)
	Local G.GHOST=New GHOST
	G\EntityL=CopyEntity(Entity)
	G\EntityR=CopyEntity(Entity)
	
	Local X#=EntityX#(Entity,True)
	Local Y#=EntityY#(Entity,True)
	Local Z#=EntityZ#(Entity,True)
	
	Local Pitch#=EntityPitch#(Entity,True)
	Local Yaw#=EntityYaw#(Entity,True)
	Local Roll#=EntityRoll#(Entity,True)
	
	ScaleEntity G\EntityL,1.0,1.0,1.0;,True
	ScaleEntity G\EntityR,1.0,1.0,1.0;,True
	
	EntityParent G\EntityL,Entity,False
	EntityParent G\EntityR,Entity,False
	
	PositionEntity G\EntityL,Float(X#-MAPSIZEX),Y#,Z#,True
	RotateEntity G\EntityL,Pitch#,Yaw#,Roll#,True
	
	PositionEntity G\EntityR,Float(X#+MAPSIZEX),Y#,Z#,True
	RotateEntity G\EntityR,Pitch#,Yaw#,Roll#,True
	
	Return G
End Function

Function BuildGhostMesh()
	If (GHOST_GEOMETRY)
		;FreeEntity GHOST_GEOMETRY
		;GHOST_GEOMETRY=0
		Return
	End If
	GHOST_WIDTH=MAPSIZEX*GHOST_MAP_PROPORTION#
	GHOST_GEOMETRY=BuildGhostGeometry()
End Function

Function BuildGhostGeometry()
	Local Mesh=CreateMesh()
	Local GHOST_Surface=CreateSurface(Mesh)
	
	Local X
	
	Local Surface=GetSurface(WALL_MESH,1)
	Local IterVertices
	Local Vertex
	Local Triangle
	Local MaxTris=CountTriangles(Surface)
	
	For Triangle=0 To MaxTris-1
		For IterVertices=0 To 2
			Vertex=TriangleVertex(Surface,Triangle,IterVertices)
			X=VertexX(Surface,Vertex)
			If (X>(MAPSIZEX-GHOST_WIDTH) Or (X<GHOST_WIDTH))
				;At least one vertex of this triangle lies at the outer regions of the map, so needs to be added to the "Ghost"
				CopyGhostTriangle(Surface,GHOST_Surface,Triangle)
				
				;Exit to skip the remaining vertices of this triangle and move on to next triangle
				Exit
			End If
		Next
	Next
	
	UpdateNormals Mesh
	
	Return Mesh
	
End Function

Function CopyGhostTriangle(MapSurface,GhostSurface,Triangle)
	;Collate Vertex Data
	
	Local v0=TriangleVertex(MapSurface,Triangle,0)
	Local v1=TriangleVertex(MapSurface,Triangle,1)
	Local v2=TriangleVertex(MapSurface,Triangle,2)
	
	Local x0#=VertexX(MapSurface,v0)
	Local x1#=VertexX(MapSurface,v1)
	Local x2#=VertexX(MapSurface,v2)
	
	Local y0#=VertexY(MapSurface,v0)
	Local y1#=VertexY(MapSurface,v1)
	Local y2#=VertexY(MapSurface,v2)
	
	Local z0#=VertexZ(MapSurface,v0)
	Local z1#=VertexZ(MapSurface,v1)
	Local z2#=VertexZ(MapSurface,v2)
	
	Local uv_a0#=VertexU#(MapSurface,v0)
	Local uv_a1#=VertexU#(MapSurface,v1)
	Local uv_a2#=VertexU#(MapSurface,v2)
	
	Local uv_b0#=VertexV#(MapSurface,v0)
	Local uv_b1#=VertexV#(MapSurface,v1)
	Local uv_b2#=VertexV#(MapSurface,v2)
	
	;Identify whether geometry should be Ghosted on LEFT or RIGHT side (or both)
	Local MinX=MAPSIZEX+2
	Local MaxX=0-2
	
	If (x0>MaxX) Then MaxX=x0
	If (x1>MaxX) Then MaxX=x1
	If (x2>MaxX) Then MaxX=x2
	
	If (x0<MinX) Then MinX=x0
	If (x1<MinX) Then MinX=x1
	If (x2<MinX) Then MinX=x2
	
	;
	
	Local g0,g1,g2
	
	If (MaxX>(MAPSIZEX-GHOST_WIDTH))
		g0=AddVertex(GhostSurface,(0-MAPSIZEX)+x0,y0,z0,uv_a0#,uv_b0#)
		g1=AddVertex(GhostSurface,(0-MAPSIZEX)+x1,y1,z1,uv_a1#,uv_b1#)
		g2=AddVertex(GhostSurface,(0-MAPSIZEX)+x2,y2,z2,uv_a2#,uv_b2#)
		
		AddTriangle(GhostSurface,g0,g1,g2)
		
	End If
	
	If (MinX<GHOST_WIDTH)	
		g0=AddVertex(GhostSurface,x0+MAPSIZEX,y0,z0,uv_a0#,uv_b0#)
		g1=AddVertex(GhostSurface,x1+MAPSIZEX,y1,z1,uv_a1#,uv_b1#)
		g2=AddVertex(GhostSurface,x2+MAPSIZEX,y2,z2,uv_a2#,uv_b2#)
		
		AddTriangle(GhostSurface,g0,g1,g2)
		
	End If
	
End Function

Function CombineGhostGeometry()
	AddMesh GHOST_GEOMETRY,WALL_MESH
	FreeEntity GHOST_GEOMETRY
	GHOST_GEOMETRY=0
End Function

Function GhostTree(T.TREE)
	T\G=GhostEntity(T\Entity)
End Function

Function GhostMine(M.MINE)
	Local Mesh=CreateQuad()
	ScaleMesh Mesh,MINE_SIZE,MINE_SIZE,0.01
	RotateMesh Mesh,90,0,0
	AddMeshToSurface(Mesh,MINE_MASTER,(M\X-MAPSIZEX)-0.5,GROUND_BASELINE_Y+MINE_Y_OFFSET,M\Y-0.5)
	AddMeshToSurface(Mesh,MINE_MASTER,(M\X+MAPSIZEX)-0.5,GROUND_BASELINE_Y+MINE_Y_OFFSET,M\Y-0.5)
	FreeEntity Mesh
End Function

Function GhostBouncer(B.BOUNCER)
	B\G=GhostEntity(B\Entity)
End Function

Function GhostHoverdroid(H.HOVERDROID)
	H\G=GhostEntity(H\Entity)
End Function

Function GhostMillitoid(M.MILLITOID)
	M\G=GhostEntity(M\Entity)
	Local Segment
	For Segment=1 To M\Segments
		Local SegmentHandle=M\Segment[Segment-1]
		Local S.MILLITOIDSEGMENT=Object.MILLITOIDSEGMENT(SegmentHandle)
		S\G=GhostEntity(S\Entity)
	Next
End Function

Function GhostBullet()
	BULLET_GHOST=GhostEntity(CURRENT_BULLET)
End Function

Function RemoveGhost(G.GHOST)
	If (G\EntityL) Then FreeEntity G\EntityL
	If (G\EntityR) Then FreeEntity G\EntityR
	
	Delete G
End Function

Function AuditGhostPositions()
	;This is required because the entity Parenting in Blitz3D is bugged
	Local G.GHOST
	For G=Each GHOST
		Local Parent=GetParent(G\EntityL)
		Local X#=EntityX(Parent,True)
		Local Y#=EntityY(Parent,True)
		Local Z#=EntityZ(Parent,True)
		
		Local LX#=EntityX(G\EntityL,True)
		;Local LY#=EntityY(G\EntityL,True)
		Local LZ#=EntityZ(G\EntityL,True)
		
		Local RX#=EntityX(G\EntityR,True)
		;Local RY#=EntityY(G\EntityR,True)
		Local RZ#=EntityZ(G\EntityR,True)
		
		If ( (Int(LX#) = Int(X#-MAPSIZEX)) And (Int(RX#) = Int(X#+MAPSIZEX)) And (Int(LZ#) = Int(Z#)) And (Int(RZ#) = Int(Z#)) )
			; For some reason <> acts very strangely here. Maybe FP inaccuracy but DebugLog did not show difference
			
		Else
			PositionEntity G\EntityL,X#-MAPSIZEX,Y#,Z#,True
			PositionEntity G\EntityR,X#+MAPSIZEX,Y#,Z#,True
		End If
	Next
End Function
;~IDEal Editor Parameters:
;~F#4#9#25#2F#4F#8E#94#98#A1#A5#A9#B3#BE
;~C#Blitz3D