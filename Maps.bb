Include"Shared.bb"

Const MAP_BOTTOM_BOUND%	=	1		;	Y-1
Const MAP_TOP_BOUND%			=	2		;	Y+1
Const MAP_LEFT_BOUND%			=	4		;	X-1
Const MAP_RIGHT_BOUND%			=	8		;	X+1

Dim HAS_WALL%(0,0)
Dim WALL_FLAG%(0,0)

Const MAP_MAZEOFDEATH$="Maze Of Death"

;Global CAPSULEX;Shared with Editor
;Global CAPSULEY;Shared with Editor

Function GameLoadMapData(Name$)
	Local Path$=MapsDir()+Name+MAP_EXTENSION
	If (FileType(Path)=1)
		Local File=ReadFile(Path)
		If (File)
			;ReadData
			;[Block]
			Local Size=MAPSIZEX*MAPSIZEY
			
			Local TempBank=CreateBank(Size*0.375)
			ReadBytes(TempBank,File,0,BankSize(TempBank))
			
			CloseFile File
			;[End Block]	
			;DeCompress
			;[Block]
			
			Local CompressedSize=BankSize(TempBank)
			
			If (MAP_BANK)
				FreeBank MAP_BANK
			End If
			
			MAP_BANK=CreateBank(Size)
			Local Bits
			
			Local SmallOffset
			Local Triplet
			Local Byte
			Local Offset
			
			For SmallOffset=0 To CompressedSize-1 Step 3
				Local B1=PeekByte(TempBank,SmallOffset) And 255
				Local B2=PeekByte(TempBank,SmallOffset+1) And 255
				Local B3=PeekByte(TempBank,SmallOffset+2) And 255
				
				Triplet = (B1 +(B2 Shl 8) +(B3 Shl 16)) And 16777215
				
				For Bits=0 To 23 Step 3
					
					Byte=(Triplet Shr Bits) And 7
					PokeByte MAP_BANK,Offset,Byte
					Offset=Offset+1
				Next
			Next
			
			FreeBank TempBank
			
			
			
			
			;FIX BECAUSE GEOMETRY CREATION INVERTED THE Y AXIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			Local LineByte
			TempBank=CreateBank(Size)
			CopyBank  MAP_BANK,0,TempBank,0,Size
			
			For Offset=0 To Size-1 Step MAPSIZEX
				For LineByte=0 To MAPSIZEX-1
					Byte=PeekByte(TempBank,Offset+LineByte) 
					PokeByte MAP_BANK,((Size-(Offset))-MAPSIZEX)+LineByte,Byte
				Next
			Next
			
			FreeBank TempBank
			;END OF FIX;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
			;[End Block]
			
			;Process
			;[Block]
			ProcessMapData
			BuildWallGeometries
			;[End Block]
			
			;Finalise
			;[Block]
			FreeBank MAP_BANK
			;[End Block]
			
			Return True
			
		End If
	End If
	Return False
End Function

Function ProcessMapData()
	;PopulateWorldFrom Map Data
	Local X
	Local Y
	Local Value
	Local WallFlag
	Dim HAS_WALL%(MAPSIZEX,MAPSIZEY)
	
	For Y= MAPSIZEY-1 To 0 Step -1
		For X=0 To MAPSIZEX-1
			Value=PeekArrayValue(X,Y)
			If (Value)
				If (Value=MAP_WALL)
				
					WallFlag=0;
					
					If (X>0)	Then If (PeekArrayValue(X-1,Y)<>MAP_WALL) Then WallFlag = WallFlag + MAP_LEFT_BOUND;
					If (X<(MAPSIZEX-1)) Then If (PeekArrayValue(X+1,Y)<>MAP_WALL) Then WallFlag = WallFlag + MAP_RIGHT_BOUND;
					
					If (Y>0)	Then If (PeekArrayValue(X,Y-1)<>MAP_WALL) Then WallFlag = WallFlag + MAP_BOTTOM_BOUND;
					If (Y<(MAPSIZEY-1))	Then If (PeekArrayValue(X,Y+1)<>MAP_WALL) Then WallFlag = WallFlag + MAP_TOP_BOUND;
					
					;Boundaries
					If(X=0) Then WallFlag=WallFlag+MAP_LEFT_BOUND
					If(X=(MAPSIZEX-1)) Then WallFlag=WallFlag+MAP_RIGHT_BOUND
					If(Y=0) Then WallFlag=WallFlag+MAP_BOTTOM_BOUND
					If(Y=(MAPSIZEY-1)) Then WallFlag=WallFlag+MAP_TOP_BOUND
					
					HAS_WALL(X,Y)=WallFlag;
					
				Else	
					Populate(X+1,Y+1,Value)	; Adjust for byte offset by +1,+1 : Best doing the offset here than in the populate function.
				End If
			End If
		Next
	Next
	FinalisePopulation
End Function

Function Populate(X,Z,Value)
	Select Value
			
		Case MAP_BOUNCER
			SpawnBouncer(X,Z)
			
		Case MAP_MILLITOID:
			SpawnMillitoid(X,Z,2^(Rand(0,3)))
			
		Case MAP_CAPSULE:
			CAPSULEX=X
			CAPSULEY=Z
		Default:
			;Value=MAP_GROUND
			;Do nothing.
	End Select
End Function

Function FinalisePopulation()
	FinaliseBouncerDirections 
	InitialiseGround
End Function
;~IDEal Editor Parameters:
;~F#F#64#8B#9D
;~C#Blitz3D